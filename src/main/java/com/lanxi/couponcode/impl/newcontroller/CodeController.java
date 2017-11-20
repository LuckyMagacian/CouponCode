package com.lanxi.couponcode.impl.newcontroller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.newservice.*;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.impl.entity.Account;
import com.lanxi.couponcode.impl.entity.CouponCode;
import com.lanxi.couponcode.impl.entity.Merchant;
import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.couponcode.spi.consts.annotations.Trim;
import com.lanxi.couponcode.spi.consts.enums.CouponCodeStatus;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.util.utils.ExcelUtil;
import com.lanxi.util.utils.LoggerUtil;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public RetMessage<String> queryCodes(String timeStart, String timeEnd, String merchantName, String commodityName, Long code, Long codeId, Integer pageNum, Integer pageSize, Long operaterId) {
        //TODO 参数校验

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
    public RetMessage<File> queryCodesExport(String timeStart, String timeEnd, String merchantName, String commodityName, Long code, Long codeId, Long operaterId) {
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
    public RetMessage<Boolean> destroyCode(Long codeId, Long operaterId) {
        //TODO 锁定串码
        //TODO 校验权限
        Boolean result=false;
        //查询
        CouponCode code=codeService.queryCode(codeId);
        //不存在
        if(code==null)
            return new RetMessage<>(RetCodeEnum.fail,"不存在!",null);
        //已被使用或注销
        if(CouponCodeStatus.undestroyed.equals(code.getCodeStatus()))
            return new RetMessage<>(RetCodeEnum.fail,"已被使用,已被注销!!",null);
        //执行注销
        result=codeService.delCode(code);
        //返回null,发生异常
        if(result==null)
            return new RetMessage<>(RetCodeEnum.fail,"注销时异常!",null);
        //成功
        if(result)
            //TODO 增加操作记录
            return new RetMessage<>(RetCodeEnum.success,"注销成功!",null);
        //失败
        if(!result)
            return new RetMessage<>(RetCodeEnum.fail,"注销失败!",null);
        //不可能到达的代码
        return new RetMessage<>(RetCodeEnum.exception,"no thing to do!",null);
        //TODO 解锁串码
    }

    @Override
    public RetMessage<Boolean> destroyCode(Long code, Long merchantId, Long operaterId) {
        //TODO 锁定串码
        //TODO 校验权限
        Optional<CouponCode> opt=codeService.queryCode(merchantId,code);
        if(opt==null)
            return new RetMessage<>(RetCodeEnum.fail,"多个!",null);
        if(!opt.isPresent())
            return new RetMessage<>(RetCodeEnum.fail,"不存在!",null);
        CouponCode codeObj=opt.get();
        Boolean result=false;
        if(CouponCodeStatus.undestroyed.equals(codeObj.getCodeStatus()))
            result=codeService.delCode(codeObj);
        //返回null,发生异常
        if(result==null)
            return new RetMessage<>(RetCodeEnum.fail,"注销时异常!",null);
        //成功
        if(result)
            //TODO 增加操作记录
            return new RetMessage<>(RetCodeEnum.success,"注销成功!",null);
        //失败
        if(!result)
            return new RetMessage<>(RetCodeEnum.fail,"注销失败!",null);
        //不可能到达的代码
        return new RetMessage<>(RetCodeEnum.exception,"no thing to do!",null);
        //TODO 解锁串码
    }

    @Override
    public RetMessage<Boolean> verificateCode(Long codeId, Long operaterId) {
        //TODO 锁定串码
        //TODO 校验权限
        Boolean result=false;
        //查询
        CouponCode code=codeService.queryCode(codeId);
        //不存在
        if(code==null)
            return new RetMessage<>(RetCodeEnum.fail,"不存在!",null);
        //已被使用或注销
        if(CouponCodeStatus.undestroyed.equals(code.getCodeStatus()))
            return new RetMessage<>(RetCodeEnum.fail,"已被使用,已被注销!!",null);
        //执行核销
        if(CouponCodeStatus.undestroyed.equals(code.getCodeStatus()))
            result=codeService.verificateCode(code);
        //返回null,发生异常
        if(result==null)
            return new RetMessage<>(RetCodeEnum.fail,"核销时异常!",null);
        //成功
        if(result)
            //TODO 增加操作记录
            return new RetMessage<>(RetCodeEnum.success,"核销成功!",null);
        //失败
        if(!result)
            return new RetMessage<>(RetCodeEnum.fail,"核销失败!",null);
        //不可能到达的代码
        return new RetMessage<>(RetCodeEnum.exception,"no thing to do!",null);
        //TODO 解锁串码
    }

    @Override
    public RetMessage<Boolean> verificateCode(Long code, Long merchantId, Long operaterId) {
        //TODO 锁定串码
        //TODO 校验权限
        Optional<CouponCode> opt=codeService.queryCode(merchantId,code);
        if(opt==null)
            return new RetMessage<>(RetCodeEnum.fail,"多个!",null);
        if(!opt.isPresent())
            return new RetMessage<>(RetCodeEnum.fail,"不存在!",null);
        CouponCode codeObj=opt.get();
        Boolean result=false;
        if(CouponCodeStatus.undestroyed.equals(codeObj.getCodeStatus()))
            result=codeService.verificateCode(codeObj);
        //返回null,发生异常
        if(result==null)
            return new RetMessage<>(RetCodeEnum.fail,"核销时异常!",null);
        //成功
        if(result)
            //TODO 增加操作记录
            return new RetMessage<>(RetCodeEnum.success,"核销成功!",null);
        //失败
        if(!result)
            return new RetMessage<>(RetCodeEnum.fail,"核销失败!",null);
        //不可能到达的代码
        return new RetMessage<>(RetCodeEnum.exception,"no thing to do!",null);
        //TODO 解锁串码
    }

    @Override
    public RetMessage<Boolean> postoneCode(Long codeId, Long operaterId) {
        //TODO 锁定串码
        //TODO 校验权限
        Boolean result=false;
        //查询
        CouponCode code=codeService.queryCode(codeId);
        //不存在
        if(code==null)
            return new RetMessage<>(RetCodeEnum.fail,"不存在!",null);
        //已被使用或注销
        if(CouponCodeStatus.undestroyed.equals(code.getCodeStatus()))
            return new RetMessage<>(RetCodeEnum.fail,"已被使用,已被注销!!",null);
        //执行延期(默认延长时间为设定的有效期)
        if(CouponCodeStatus.undestroyed.equals(code.getCodeStatus()))
            result=codeService.postoneCode(code);
        //返回null,发生异常
        if(result==null)
            return new RetMessage<>(RetCodeEnum.fail,"延期时异常!",null);
        //成功
        if(result)
            //TODO 增加操作记录
            return new RetMessage<>(RetCodeEnum.success,"延期成功!",null);
        //失败
        if(!result)
            return new RetMessage<>(RetCodeEnum.fail,"延期失败!",null);
        //不可能到达的代码
        return new RetMessage<>(RetCodeEnum.exception,"no thing to do!",null);
        //TODO 解锁串码
    }

    @Override
    public RetMessage<Boolean> generateCode(Long merchantId, Long commodityId, String reason, Integer channel) {

        return null;
    }

    @Override
    public RetMessage<String> couponCodeInfo(Long codeId, Long operaterId) {
        //TODO 校验权限
        //查询
        CouponCode code=codeService.queryCode(codeId);
        //不存在
        if(code==null)
            return new RetMessage<>(RetCodeEnum.fail,"不存在!",null);
        return new RetMessage<>(RetCodeEnum.success,"查询成功!",code.toJson());
    }

    @Override
    public RetMessage<String> couponCodeInfo(Long merchantId, Long code, Long operaterId) {
        //TODO 校验权限
        Optional<CouponCode> opt=codeService.queryCode(merchantId,code);
        if(opt==null)
            return new RetMessage<>(RetCodeEnum.fail,"多个!",null);
        if(!opt.isPresent())
            return new RetMessage<>(RetCodeEnum.fail,"不存在!",null);
        CouponCode codeObj=opt.get();
        return new RetMessage<>(RetCodeEnum.success,"查询成功!",codeObj.toJson());
    }
}
