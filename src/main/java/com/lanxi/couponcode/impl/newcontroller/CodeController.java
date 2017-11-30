package com.lanxi.couponcode.impl.newcontroller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.lanxi.couponcode.impl.assist.TimeAssist;
import com.lanxi.couponcode.impl.entity.*;
import com.lanxi.couponcode.impl.newservice.*;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.annotations.Cache;
import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.couponcode.spi.consts.enums.*;
import com.lanxi.util.utils.ExcelUtil;
import com.lanxi.util.utils.LoggerUtil;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static com.lanxi.couponcode.impl.assist.PredicateAssist.*;


/**
 * Created by yangyuanjian on 2017/11/15.
 */
@EasyLog(LoggerUtil.LogLevel.INFO)
@Controller
public class CodeController implements com.lanxi.couponcode.spi.service.CouponService{
    @Resource
    private CodeService codeService;
    @Resource
    private RedisCodeService redisCodeService;
    @Resource
    private RedisEnhancedService redisEnhancedService;
    @Resource
    private OperateRecordService operateRecordService;
    @Resource
    private VerificationRecordService verificationRecordService;
    @Resource
    private MerchantService merchantService;
    @Resource
    private AccountService accountService;
    @Resource
    private ShopService shopService;
    @Resource
    private CodeAlgorithmService algorithmService;
    @Resource
    private CommodityService commodityService;

    private List<CouponCode> queryCodesHidden(String timeStart,
                                              String timeEnd,
                                              String merchantName,
                                              String commodityName,
                                              Long code,
                                              Long codeId,
                                              Page<CouponCode> page) {
        //装配查询条件
        EntityWrapper<CouponCode> wrapper = new EntityWrapper<>();
        if (timeStart != null && !timeStart.isEmpty()) {
            while (timeStart.length() < 14)
                timeStart += "0";
            wrapper.ge("create_time", timeStart);
        }
        if (timeEnd != null && !timeEnd.isEmpty()) {
            while (timeEnd.length() < 14)
                timeEnd += "9";
            wrapper.le("create_time", timeEnd);
        }
        if (merchantName != null && !merchantName.isEmpty())
            wrapper.like("merchant_name", merchantName);
        if (commodityName != null && !commodityName.isEmpty())
            wrapper.like("commodity_name", commodityName);
        if (code != null)
            wrapper.eq("code", code);
        if (codeId != null)
            wrapper.eq("code_id", codeId + "");
        //查询
        List<CouponCode> list= codeService.queryCodes(wrapper,page);
        //返回
        return list;
    }

    @Override
    @Cache(exclusionArgs = {"operaterId"})
    public RetMessage<String> queryCodes(String timeStart,
                                         String timeEnd,
                                         String merchantName,
                                         String commodityName,
                                         Long code,
                                         Long codeId,
                                         Integer pageNum,
                                         Integer pageSize,
                                         Long operaterId) {
        //TODO 参数校验
        Account account=accountService.queryAccountById(operaterId);
        if(isNull.test(account))
            return new RetMessage<>(RetCodeEnum.fail,"帐号不存在!",null);
        if(notAdmin.test(account))
            return new RetMessage<>(RetCodeEnum.fail,"非管理员!",null);
//        if(notPageArg.test(pageNum+"",pageSize+""))
//            return new RetMessage<>(RetCodeEnum.fail,"未分页!",null);
//        if(notNullOrEmpty.and(notAddressOrName).test(merchantName))
//            return new RetMessage<>(RetCodeEnum.fail,"商户名不规范!",null);
//        if(notNullOrEmpty.and(notAddressOrName).test(commodityName))
//            return new RetMessage<>(RetCodeEnum.fail,"商品名不规范!",null);
//        if(notNullOrEmpty.and(notTime).test(timeStart))
//            return new RetMessage<>(RetCodeEnum.fail,"起始时间不规范!",null);
//        if(notNullOrEmpty.and(notTime).test(timeEnd))
//            return new RetMessage<>(RetCodeEnum.fail,"结束时间不规范!",null);
        Page<CouponCode> page=new Page<>(pageNum,pageSize);
        List<CouponCode> list=queryCodesHidden(timeStart,timeEnd,merchantName,commodityName,code,codeId,page);
        //需要分页信息
        Map<String,Object> map=new HashMap<>();
        map.put("page",page);
        map.put("list",list);
        if(list!=null)
            return new RetMessage<String>(RetCodeEnum.success,"查询成功", JSON.toJSONString(map));
        else
            return new RetMessage<String>(RetCodeEnum.fail,"查询失败",null);
    }
    @Override
    public RetMessage<File> queryCodesExport(String timeStart,
                                             String timeEnd,
                                             String merchantName,
                                             String commodityName,
                                             Long code,
                                             Long codeId,
                                             Long operaterId) {
        Account account=accountService.queryAccountById(operaterId);
        RetMessage message=checkAccount.apply(account,OperateType.queryCouponCodeList);
        if(message!=null)
            return message;
        //需要展示的内容
        Map<String,String> show=new HashMap<>();
        List<CouponCode> list=queryCodesHidden(timeStart,timeEnd,merchantName,commodityName,code,codeId,null);
        File file;
        if(list!=null) {
            file = ExcelUtil.exportExcelFile(list, show);
            return new RetMessage<>(RetCodeEnum.success,"导出成功!",file);
        }else
            return new RetMessage<>(RetCodeEnum.fail,"导出失败!",null);
    }
    @Override
    public RetMessage<Boolean> destroyCode(Long codeId,
                                           Long operaterId) {
        //----------------------------------------------------------校验--------------------------------------------------------
        Boolean result=false;

        Account account=accountService.queryAccountById(operaterId);
        RetMessage message=checkAccount.apply(account,OperateType.cancelCouponCode);
        if(message!=null)
            return message;
        CouponCode code=codeService.queryCodeInfo(codeId,account.getMerchantId());
        message=checkCode.apply(code,OperateType.cancelCouponCode);
        if(message!=null)
            return message;
        //----------------------------------------------------------执行--------------------------------------------------------
        //执行注销
        boolean lock=false;
        try {
            //加锁
            lock=redisCodeService.lockCode(code,null);
            if(lock)
                result=codeService.delCode(code);
        } catch (Exception e) {

        }finally {
            if(lock)
                redisCodeService.unlockCodeForce(code,null);
        }
        //-----------------------------------------------------------------返回--------------------------------------------------------------
        //返回null,发生异常
        if(isNull.test(result))
            return new RetMessage<>(RetCodeEnum.fail,"注销时异常!",null);
        //成功
        if(result){
            //增加操作记录
            OperateRecord record=new OperateRecord();
            record.setRecordId(IdWorker.getId());
            record.setOperaterId(account.getAccountId());
            record.setAccountType(account.getAccountType());
            record.setPhone(account.getPhone());
            record.setName(account.getUserName());
            record.setTargetType(OperateTargetType.code);
            record.setType(OperateType.cancelCouponCode);
            record.setOperateTime(TimeAssist.getNow());
            record.setOperateResult("success");
            record.setDescription("销毁串码["+codeId+"]");
            operateRecordService.addRecord(record);
            return new RetMessage<>(RetCodeEnum.success,"注销成功!",null);
        }
        //失败
        if(!result)
            return new RetMessage<>(RetCodeEnum.fail,"注销失败,有其他用户正在操作该串码!",null);
        //不可能到达的代码
        return new RetMessage<>(RetCodeEnum.exception,"no thing to do!",null);
    }

    @Override
    public RetMessage<Boolean> destroyCode(Long code,
                                           @Deprecated Long merchantId,
                                           Long operaterId) {

        //-----------------------------------------------------------------校验--------------------------------------------------------------
        Account account=accountService.queryAccountById(operaterId);
        Optional<CouponCode> opt=codeService.queryCode(account.getMerchantId(),code);
        if(isNull.test(opt))
            return new RetMessage<>(RetCodeEnum.fail,"多个!",null);
        if(!opt.isPresent())
            return new RetMessage<>(RetCodeEnum.fail,"不存在!",null);
        CouponCode codeObj=opt.get();
        //-----------------------------------------------------------------调用--------------------------------------------------------------
        return destroyCode(codeObj.getCodeId(),operaterId);
    }

    @Override
    public RetMessage<Boolean> verificateCode(Long codeId,
                                              Long operaterId) {
        //TODO 锁定串码
        //TODO 校验权限
        Boolean result=false;
        //-----------------------------------------------------------------校验--------------------------------------------------------------
        Account account=accountService.queryAccountById(operaterId);
        RetMessage message=checkAccount.apply(account,OperateType.destroyCouponCode);
        if(message!=null)
            return message;

//        if(isNull.test(account))
//            return new RetMessage<>(RetCodeEnum.fail,"帐号不存在!",null);
//        if(cantVerifyCode.test(account))
//            return new RetMessage<>(RetCodeEnum.fail,"无权核销!",null);
        //查询
        CouponCode code=codeService.queryCodeInfo(codeId,account.getMerchantId());
        message=checkCode.apply(code,OperateType.destroyCouponCode);
//        if(isNull.test(code))
//            return new RetMessage<>(RetCodeEnum.fail,"不存在!",null);
//        if(diffMerchantCodeAccount.test(code,account))
//            return new RetMessage<>(RetCodeEnum.fail,"非本商户串码!",null);
//        if(cantVerify.test(code))
//            return new RetMessage<>(RetCodeEnum.fail,"已被使用,已被注销!!",null);
        //-----------------------------------------------------------------执行--------------------------------------------------------------
        boolean lock=false;
        try {
            //加锁
            lock=redisCodeService.lockCode(code,null);
            if(lock)
                result=codeService.verificateCode(code);
        } catch (Exception e) {

        }finally {
            //解锁
            if(lock)
                redisCodeService.unlockCodeForce(code,null);
        }
        //-----------------------------------------------------------------返回--------------------------------------------------------------
        //返回null,发生异常
        if(isNull.test(result))
            return new RetMessage<>(RetCodeEnum.fail,"核销时异常!",null);
        //成功
        if(result){
            //TODO 增加操作记录
            //增加操作记录
            OperateRecord record=new OperateRecord();
            record.setRecordId(IdWorker.getId());
            record.setOperaterId(account.getAccountId());
            record.setAccountType(account.getAccountType());
            record.setPhone(account.getPhone());
            record.setName(account.getUserName());
            record.setTargetType(OperateTargetType.code);
            record.setType(OperateType.destroyCouponCode);
            record.setOperateTime(TimeAssist.getNow());
            record.setOperateResult("success");
            record.setDescription("核销串码["+codeId+"]");
            operateRecordService.addRecord(record);
            return new RetMessage<>(RetCodeEnum.success,"核销成功!",null);
        }
        //失败
        if(!result)
            return new RetMessage<>(RetCodeEnum.fail,"核销失败!",null);
        //不可能到达的代码
        return new RetMessage<>(RetCodeEnum.exception,"no thing to do!",null);
    }

    @Override
    public RetMessage<Boolean> verificateCode(Long code,
                                              Long merchantId,
                                              Long operaterId) {
        //-----------------------------------------------------------------校验--------------------------------------------------------------
        Account account=accountService.queryAccountById(operaterId);
        Optional<CouponCode> opt=codeService.queryCode(account.getMerchantId(),code);
        if(isNull.test(opt))
            return new RetMessage<>(RetCodeEnum.fail,"多个!",null);
        if(!opt.isPresent())
            return new RetMessage<>(RetCodeEnum.fail,"不存在!",null);
        CouponCode codeObj=opt.get();
        //-----------------------------------------------------------------调用--------------------------------------------------------------
        return verificateCode(codeObj.getCodeId(),operaterId);
    }

    @Override
    public RetMessage<Boolean> postoneCode(Long codeId,
                                           Long operaterId) {
        //-----------------------------------------------------------------校验--------------------------------------------------------------
        Account account=accountService.queryAccountById(operaterId);
        RetMessage message=checkAccount.apply(account,OperateType.postoneCouponCode);
        if(message!=null)
            return message;
//        if(isNull.test(account))
//            return new RetMessage<>(RetCodeEnum.fail,"帐号不存在!",null);
//        if(cantPostoneCode.test(account))
//            return new RetMessage<>(RetCodeEnum.fail,"无权延期!",null);
        Boolean result=false;
        //查询
        CouponCode code=codeService.queryCode(codeId);
        message=checkCode.apply(code,OperateType.postoneCouponCode);
        if(message!=null)
            return message;
        //不存在
        if(isNull.test(code))
            return new RetMessage<>(RetCodeEnum.fail,"不存在!",null);
        if(diffMerchantCodeAccount.test(code,account))
            return new RetMessage<>(RetCodeEnum.fail,"非本商户串码!",null);
        if(cantPostone.test(code))
            return new RetMessage<>(RetCodeEnum.fail,"已被使用,已被注销!!",null);
        //-----------------------------------------------------------------执行--------------------------------------------------------------
        //执行延期(默认延长时间为设定的有效期)
        boolean lock=false;
        try {
            //加锁
            lock=redisCodeService.lockCode(code,null);
            if(lock)
                result=codeService.postoneCode(code);
        } catch (Exception e) {

        }finally {
            //解锁
            if(lock)
                redisCodeService.unlockCodeForce(code,null);
        }
        //-----------------------------------------------------------------返回--------------------------------------------------------------
        //返回null,发生异常
        if(isNull.test(result))
            return new RetMessage<>(RetCodeEnum.fail,"延期时异常!",null);
        //成功
        if(result){
            OperateRecord record=new OperateRecord();
            record.setRecordId(IdWorker.getId());
            record.setOperaterId(account.getAccountId());
            record.setAccountType(account.getAccountType());
            record.setPhone(account.getPhone());
            record.setName(account.getUserName());
            record.setTargetType(OperateTargetType.code);
            record.setType(OperateType.postoneCouponCode);
            record.setOperateTime(TimeAssist.getNow());
            record.setOperateResult("success");
            record.setDescription("延期串码["+codeId+"]");
            operateRecordService.addRecord(record);
            return new RetMessage<>(RetCodeEnum.success,"延期成功!",null);
        }
        //失败
        if(!result)
            return new RetMessage<>(RetCodeEnum.fail,"延期失败!",null);
        //不可能到达的代码
        return new RetMessage<>(RetCodeEnum.exception,"no thing to do!",null);
        //TODO 解锁串码
    }

    @Override
    public RetMessage<String> generateCode(Long merchantId,
                                            Long commodityId,
                                            String reason,
                                            Integer channel) {
        if(isNull.test(merchantId))
            return new RetMessage<>(RetCodeEnum.fail,"商户号为空!",null);
        if(isNull.test(commodityId))
            return new RetMessage<>(RetCodeEnum.fail,"商品号为空!",null);
        Merchant merchant=merchantService.queryMerchantParticularsById(merchantId);
        RetMessage message=checkMerchant.apply(merchant,OperateType.createCouponCode);
        if(message==null)
            return message;
//        if(isNull.test(merchant))
//            return new RetMessage<>(RetCodeEnum.fail,"商户不存在!",null);
//        if(!MerchantStatus.normal.equals(merchant.getMerchantStatus()))
//            return new RetMessage<>(RetCodeEnum.fail,"商户状态异常!",null);
        Commodity commodity=commodityService.queryCommodity(commodityId,merchantId);
        message=checkCommodity.apply(commodity,OperateType.createCouponCode);
        if(message==null)
            return message;
//        if(isNull.test(commodity))
//            return new RetMessage<>(RetCodeEnum.fail,"商品不存在!",null);
//        if(!CommodityStatus.shelved.equals(commodity.getStatus()))
//            return new RetMessage<>(RetCodeEnum.fail,"商品状态异常!",null);
        CodeAlgorithm algorithm=algorithmService.getCodeAlgorithm(merchantId);
        Long code=null;
        Long var=redisCodeService.getCodeVar(merchantId);
        while(cantBeCode.test(code)||codeService.checkCodeExists(merchantId,code)){
            if(var==null)
                var= algorithm.getAndIncyVar();
            code=algorithm.getCode(var);
        }
        CouponCode codeObj=new CouponCode();
        codeObj.setCodeId(IdWorker.getId());
        codeObj.setCode(code);
        codeObj.setCreateTime(TimeAssist.getNow());
        codeObj.setLifeTime(commodity.getLifeTime());
        codeObj.setOverTime(TimeAssist.addSecond(codeObj.getCreateTime(),codeObj.getLifeTime()*24L*60*60*1000));
        codeObj.setCommodityId(commodity.getCommodityId());
        codeObj.setCommodityName(commodity.getCommodityName());
        codeObj.setCommodityInfo(commodity.toJson());
        codeObj.setCodeStatus(CouponCodeStatus.undestroyed);
        codeObj.setMerchantId(commodity.getMerchantId());
        codeObj.setMerchantName(commodity.getMerchantName());
        codeObj.setClearStatus(ClearStatus.uncleared);
        codeObj.setReason(reason);
        codeObj.setChannel(channel);
        if(codeService.addCode(codeObj)){
            redisCodeService.addCode(merchantId,code);
            return new RetMessage<>(RetCodeEnum.success,"生成成功!",codeObj.toJson());
        }else
            return new RetMessage<>(RetCodeEnum.fail,"生成失败!",null);
    }

    @Override
    public RetMessage<String> couponCodeInfo(Long codeId, Long operaterId) {
        //TODO 校验权限
        //查询
        Account account=accountService.queryAccountById(operaterId);
        RetMessage message=checkAccount.apply(account,OperateType.postoneCouponCode);
        if(message!=null)
            return message;
//        if(isNull.test(account))
//            return new RetMessage<>(RetCodeEnum.fail,"帐号不存在!",null);
//        if(cantQueryCode.test(account))
//            return new RetMessage<>(RetCodeEnum.fail,"无权查询!",null);
        CouponCode code=codeService.queryCodeInfo(codeId,account.getMerchantId());
        message=checkCode.apply(code,OperateType.queryCouponCode);
        if(message!=null)
            return message;
//        if(diffMerchantCodeAccount.test(code,account))
//            return new RetMessage<>(RetCodeEnum.fail,"非本商户串码!",null);
//        if(isNull.test(code))
//            return new RetMessage<>(RetCodeEnum.fail,"不存在!",null);
        return new RetMessage<>(RetCodeEnum.success,"查询成功!",code.toJson());
    }

    @Override
    public RetMessage<String> couponCodeInfo(Long merchantId,
                                             Long code,
                                             Long operaterId) {
        Account account=accountService.queryAccountById(operaterId);
        Optional<CouponCode> opt=codeService.queryCode(account.getMerchantId(),code);
        if(opt==null)
            return new RetMessage<>(RetCodeEnum.fail,"多个!",null);
        if(!opt.isPresent())
            return new RetMessage<>(RetCodeEnum.fail,"不存在!",null);
        return couponCodeInfo(opt.get().getCodeId(),operaterId);
//        Account account=accountService.queryAccountById(operaterId);
//        if(isNull.test(account))
//            return new RetMessage<>(RetCodeEnum.fail,"帐号不存在!",null);
//        if(cantQueryCode.test(account))
//            return new RetMessage<>(RetCodeEnum.fail,"无权查询!",null);

//        Optional<CouponCode> opt=codeService.queryCode(merchantId,code);
//        if(opt==null)
//            return new RetMessage<>(RetCodeEnum.fail,"多个!",null);
//        if(!opt.isPresent())
//            return new RetMessage<>(RetCodeEnum.fail,"不存在!",null);
//        CouponCode codeObj=opt.get();
//        return new RetMessage<>(RetCodeEnum.success,"查询成功!",codeObj.toJson());
    }
}
