package com.lanxi.couponcode.impl.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.lanxi.couponcode.impl.entity.CouponCode;
import com.lanxi.couponcode.impl.entity.OperateRecord;
import com.lanxi.couponcode.impl.entity.VerificationRecord;
import com.lanxi.couponcode.spi.abstractentity.AbstractCouponCode;
import com.lanxi.couponcode.spi.consts.enums.CouponCodeStatus;
import com.lanxi.couponcode.spi.consts.enums.OperateTargetType;
import com.lanxi.couponcode.spi.consts.enums.VerificationType;
import com.lanxi.util.entity.LogFactory;
import com.lanxi.util.utils.TimeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 串码服务接口实现类<br>
 * Created by yangyuanjian on 2017/10/31.<br>
 */
@Service("couponCodeService")
public class CouponCodeServiceImpl implements  CouponCodeService{
    @Resource
    private RedisCodeService codeService;
    @Resource
    private DaoService daoService;
    /**默认串码有效期*/
    private static final int DEFAULT_LIFE_DATE=30;
    /***/
    private static final int LOCK_FAIL_WAIT_TIME=1000;
    @Override
    public boolean getnerateCode(Long merchantId,       Long commodityId,
                                 String merchantName,   String commodityName,
                                 String commodityInfo,  Integer lifeTime){
        String locker=",merchantId["+merchantId+"]\n" +
                ",commodityId["+commodityId+"]\n" +
                ",merchantName["+merchantName+"]\n" +
                ",commodityName["+commodityName+"]\n" +
                ",lifeTime["+lifeTime+"]\n" +
                ",commodityInfo["+commodityInfo+"]";
        LogFactory.info(this,"尝试生成串码!\n" +locker);
        //生成串码时 merchantId与commodityId不能为空
        if(merchantId==null||commodityId==null) {
            LogFactory.warn(this,"生成串码时,commodityId或merchantId参数为空\n"  + locker);
            return false;
        }
        if(lifeTime==null){
            LogFactory.debug(this,"生成串码时,有效期为空,设置为默认有效期["+DEFAULT_LIFE_DATE+"]\n" + locker);
            lifeTime=DEFAULT_LIFE_DATE;
        }
        //调用redis的串码生成服务生成串码
        long code=codeService.getCode(merchantId);
        LogFactory.info(this,"生成串码结果code["+code+"]\n" + locker);
        //当串码为-1时代表串码生成失败
        if(-1L==code){
            LogFactory.warn(this,"生成串码失败!\n" + locker);
            return false;
        }
        try {
            //增加串码插入到数据库
            CouponCode couponCode=new CouponCode();
            couponCode.setCodeId(IdWorker.getId());
            couponCode.setMerchantId(merchantId);
            couponCode.setCommodityId(commodityId);
            couponCode.setCodeStatus(CouponCodeStatus.undestroyed);
            couponCode.setCreateTime(TimeUtil.getDateTime());
            couponCode.setLifeTime(lifeTime);
            couponCode.setOverTime(LocalDateTime.now().plusDays(lifeTime).format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss")));
            couponCode.setMerchantName(merchantName);
            couponCode.setCommodityName(commodityName);
            couponCode.setCommodityInfo(commodityInfo);
            boolean result=couponCode.insert();
            LogFactory.info(this,"生成串码成功,记录到数据库结果["+result+"]!\n" + locker);
            if(result){
                boolean flag=codeService.addCode(merchantId,code);
                LogFactory.info(this,"生成串码成功,记录到redis结果["+flag+"]!\n" + locker);
            }
            return result;
        }catch (Exception e){
            LogFactory.error(this,"发生异常生成串码失败!\n" + locker);
            return false;
        }
    }

    @Override
    public boolean destroyCode(Long merchantId,         Long shopId,        Long commodityId,       Long operaterId,
                               String merchantName,     String shopName,    String commodityName,   String operaterPhone,
                               String verificationType, Long code,          String shopInfo
                               ){
        String locker="尝试核销串码\n" +
                ",merchantId[]\n" +
                ",shopId[]\n" +
                ",commodityId[]\n" +
                ",operaterId[]\n" +
                ",merchantName[]\n" +
                ",shopName[]\n" +
                ",commodityName[]\n" +
                ",operaterPhone[]\n" +
                ",verificationType[]\n" +
                ",code[],shopInfo[]";
        boolean lock=false;
        boolean result=false;
        try {
            LogFactory.info(this,"尝试核销串码\n" +locker);
            lock=codeService.lockCode(merchantId,code,locker);
            if(!lock){
                LogFactory.debug(this,"串码核销,锁定串码失败,等待1秒后强制解锁"+locker);
                Thread.sleep(LOCK_FAIL_WAIT_TIME);
                codeService.unlockCodeForce(merchantId,code,locker);
                lock=codeService.lockCode(merchantId,code,locker);
            }
            if(!lock){
                LogFactory.debug(this,"锁定串码失败,核销失败,"+locker);
                return false;
            }
            CouponCode couponCode= queryCouponCode(merchantId,code).orElse(null);
            if(couponCode==null||!couponCode.getCodeStatus().equals(CouponCodeStatus.undestroyed.getValue())){
                LogFactory.info(this,"核销失败,未找到串码或串码状态不是待核销状态,["+couponCode+"],\n"+locker);
                return false;
            }
            couponCode.setCodeStatus(CouponCodeStatus.destroyed);
            if(commodityId==null){
                LogFactory.debug(this,"串码商品编号参数为空["+commodityId+"],改为查找到的串码的商品编号["+couponCode.getCommodityId()+"],\n"+locker);
                commodityId=couponCode.getCommodityId();
            }
            if(couponCode.getCommodityId()!=commodityId){
                LogFactory.info(this,"核销失败,商品id不一致,串码中commodityId["+couponCode.getCommodityId()+"],\n"+locker);
                return false;
            }
            //需要将核销记录id记录到couponcode实例中去
            VerificationRecord record = new VerificationRecord();
            record.setRecordId(IdWorker.getId());
            couponCode.setDestroyRecordId(record.getRecordId());
            result=couponCode.updateById();
            LogFactory.info(this,"更新串码状态结果["+result+"],\n"+locker);
            //若更新成功增加核销记录
            if(result) {
                record.setMerchantId(merchantId);
                record.setMerchantName(merchantName);
                record.setShopId(shopId);
                record.setShopName(shopName);
                record.setShopInfo(shopInfo);
                record.setCommodityId(commodityId);
                record.setCommodityName(commodityName);
                record.setOperaterId(operaterId);
                record.setOperaterPhone(operaterPhone);
                record.setVerficateTime(TimeUtil.getDateTime());
                record.setVerificationType(VerificationType.getType(verificationType));
                boolean flag = record.insert();
                LogFactory.info(this,"增加核销记录["+record+"]结果["+flag+"],\n"+locker);
            }
            return result;
        } catch (Exception e) {
            LogFactory.error(this,"核销串码时发生异常,\n" +locker);
            if(lock){
                try {
                    codeService.unlockCode(merchantId,code,locker);
                }catch (Exception e1){

                }
            }
            return result;
        }

    }

//    @Override
//    public String quryCommodityId(Long merchantId, Long code) {
//        LogFactory.info(this,"尝试查询单个串码的商品编号,\n"+
//                "merchantId["+merchantId+"]\n"+
//                "code["+code+"]\n");
//        return queryCouponCode(merchantId,code) .map(e->e.getCommodityId()+"")
//                                                .orElse(null);
//    }
//    @Override
//    public String queryCommodityInfo(Long merchantId,Long code){
//        return queryCouponCode(merchantId,code) .map(e->e.getCommodityInfo())
//                .orElse(null);
//    }
    @Override
    public boolean postponeCode(Long merchantId,Long code,Long operaterId,String operaterInfo,String operaterPhone){
        String locker="merchantId["+merchantId+"]\n"+
                "code["+code+"]\n"+
                "operaterId["+operaterId+"]\n"+
                "operaterPhone["+operaterPhone+"]\n"+
                "operaterInfo["+operaterInfo+"]\n";
        boolean lock=false;
        boolean result=false;
        LogFactory.info(this,"尝试串码延期,\n"+locker);
        try {
            lock=codeService.lockCode(merchantId,code,locker);
            if(!lock){
                LogFactory.debug(this,"串码延期锁定串码失败,等待1秒后强制解锁"+locker);
                Thread.sleep(LOCK_FAIL_WAIT_TIME);
                codeService.unlockCodeForce(merchantId,code,locker);
                lock=codeService.lockCode(merchantId,code,locker);
            }
            if(!lock){
                LogFactory.debug(this,"锁定串码失败,串码延期失败,"+locker);
                return false;
            }
            CouponCode couponCode= queryCouponCode(merchantId,code).orElse(null);
            LogFactory.info(this,"串码延期,串码查询结果["+couponCode+"],\n"+locker);
            if(couponCode==null||!couponCode.getCodeStatus().equals(CouponCodeStatus.undestroyed.getValue())){
                LogFactory.info(this,"延期失败,串码为空或状态不是待核销状态,\n"+locker);
                return false;
            }
            //每次延期增加默认有效期的天数
            couponCode.setLifeTime(couponCode.getLifeTime()+DEFAULT_LIFE_DATE);
            couponCode.setOverTime(LocalDateTime.parse(couponCode.getOverTime(),DateTimeFormatter.ofPattern("yyyyMMddhhmmss")).plusDays(DEFAULT_LIFE_DATE).format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss")));
            result= couponCode.updateById();
            LogFactory.info(this,"延期结果["+result+"],\n"+locker);
            //更新成功,增加操作记录!
            if(result){
                OperateRecord record=new OperateRecord();
                record.setRecordId(IdWorker.getId());
                record.setOperaterId(operaterId);
                record.setOperaterPhone(operaterPhone);
                record.setOperaterInfo(operaterInfo);
                record.setTargetType(OperateTargetType.code);
                record.setDescription("串码延期");
                record.setOperateTime(TimeUtil.getDateTime());
                record.setOperateResult(result?"成功":"失败");
                record.setTargetInfo(couponCode.toJson());
                boolean flag=record.insert();
                LogFactory.info(this,"增加串码延期操作记录["+record+"]结果["+flag+"],\n"+locker);
            }
            return result;
        } catch (Exception e) {
            LogFactory.error(this,"串码延期时发生异常,\n"+locker,e);
            if(lock)
                try {
                    codeService.unlockCode(merchantId,code,locker);
                } catch (Exception e1) {

                }
            return  result;
        }

    }

    @Override
    public boolean cancelCode(Long merchantId, Long code, Long operaterId, String operaterInfo,String operaterPhone) {
        String locker= "merchantId["+merchantId+"]\n"+
                "code["+code+"]\n"+
                "operaterId["+operaterId+"]\n"+
                "operaterPhone["+operaterPhone+"]\n"+
                "operaterInfo["+operaterInfo+"]\n";
        boolean lock=false;
        boolean result=false;

        try {
            LogFactory.info(this,"尝试销毁串码,\n"+locker);
            lock=codeService.lockCode(merchantId,code,locker);
            if(!lock){
                LogFactory.debug(this,"锁定串码失败,等待1秒后强制解锁"+locker);
                Thread.sleep(LOCK_FAIL_WAIT_TIME);
                codeService.unlockCodeForce(merchantId,code,locker);
                lock=codeService.lockCode(merchantId,code,locker);
            }
            if(!lock){
                LogFactory.debug(this,"锁定串码失败,核销失败,"+locker);
                return false;
            }
            CouponCode couponCode= queryCouponCode(merchantId,code).orElse(null);
            LogFactory.info(this,"销毁串码,串码查询结果["+couponCode+"],\n"+locker);
            if(couponCode==null||!couponCode.getCodeStatus().equals(CouponCodeStatus.undestroyed.getValue())){
                LogFactory.info(this,"销毁失败,串码为空或状态不是待核销状态,\n"+
                        "merchantId["+merchantId+"]\n"+
                        "code["+code+"]\n"+
                        "operaterId["+operaterId+"]\n"+
                        "operaterPhone["+operaterPhone+"]\n"+
                        "operaterInfo["+operaterInfo+"]\n");
                return false;
            }
            couponCode.setCodeStatus(CouponCodeStatus.cancellation.getValue());
            result=couponCode.updateById();
            LogFactory.info(this,"销毁结果["+result+"],\n"+locker);
            if(result){
                OperateRecord record=new OperateRecord();
                record.setRecordId(IdWorker.getId());
                record.setOperaterId(operaterId);
                record.setOperaterPhone(operaterPhone);
                record.setOperaterInfo(operaterInfo);
                record.setTargetType(OperateTargetType.code);
                record.setDescription("销毁串码");
                record.setOperateTime(TimeUtil.getDateTime());
                record.setOperateResult(result?"成功":"失败");
                record.setTargetInfo(couponCode.toJson());
                boolean flag=record.insert();
                LogFactory.info(this,"增加串码销毁操作记录["+record+"]结果["+flag+"],\n"+locker);
            }
            return result;
        } catch (Exception e) {
            LogFactory.error(this,"串码销毁时发生异常,\n"+locker,e);
            if(lock)
                try {
                    codeService.unlockCode(merchantId,code,locker);
                } catch (Exception e1) {

                }
            return  result;
        }

    }
    @Override
    public boolean overtimeCode(Long merchantId,Long code){
        String locker= "merchantId["+merchantId+"]\n"+
                        "code["+code+"]\n";
        boolean lock=false;
        boolean result=false;
        try {
            LogFactory.info(this,"尝试串码过期,\n"+locker);
            if(!lock){
                LogFactory.debug(this,"串码过期,锁定串码失败,等待1秒后重试"+locker);
                Thread.sleep(LOCK_FAIL_WAIT_TIME);
                lock=codeService.lockCode(merchantId,code,locker);
            }
            if(!lock){
                LogFactory.debug(this,"锁定串码失败,过期失败,"+locker);
                return false;
            }
            CouponCode couponCode= queryCouponCode(merchantId,code).orElse(null);
            LogFactory.info(this,"串码过期,串码查询结果["+couponCode+"]\n"+locker);
            if(couponCode==null||!couponCode.getCodeStatus().equals(CouponCodeStatus.undestroyed.getValue())){
                LogFactory.info(this,"串码过期失败,串码为空或串码状态不是待核销状态,\n"+locker);
                return false;
            }
            couponCode.setCodeStatus(CouponCodeStatus.overtime.getValue());
            result=couponCode.updateById();
            LogFactory.info(this,"串码过期结果["+result+"]\n"+locker);
            return result;
        } catch (Exception e) {
            LogFactory.error(this,"串码过期时发生异常,\n" +locker);
            if(lock){
                try {
                    codeService.unlockCode(merchantId,code,locker);
                }catch (Exception e1){

                }
            }
            return result;
        }

    }
    @Override
    public Optional<CouponCode> queryCouponCode(Long merchantId,Long code){
        String locker= "merchantId["+merchantId+"]\n"+
                "code["+code+"]\n";
        try {
            LogFactory.info(this,"尝试查询单个串码,\n"+locker);
            //本方法只能用于查询单个串码,且参数不能为null
            if(merchantId==null||code==null){
                LogFactory.info(this,"单个串码查询时有参数为null,\n"+locker);
                return Optional.empty();
            }
            //查询串码
            List<AbstractCouponCode> list=new CouponCode().selectList(new EntityWrapper().eq("merchant_id",merchantId).eq("code",code));
            LogFactory.info(this,"单个串码查询结果["+list+"],\n"+
                    "merchantId["+merchantId+"]\n"+
                    "code["+code+"]\n");
            if(list==null||list.size()!=1){
                LogFactory.warn(this,"单个串码查询结果不唯一,返回Optional.empty()!\n" +locker);
                return Optional.empty();
            }
            LogFactory.info(this,"单个串码查询成功["+list.get(0)+"],!\n" +locker);
            return  Optional.of((CouponCode) list.get(0));
        } catch (Exception e) {
            LogFactory.error(this,"查询单个串码时发生异常,\n" +locker);
            return Optional.empty();
        }

    }
    @Override
    public List<CouponCode> queryCouponCode(String createTimeStart, String createTimeEnd,   String merchantName,
                                            String commodityName,   Long code,              Long codeId,
                                            String codeStatus   ,   Long commodityId,       Long merchantId,
                                            String overTimeStart,   String overTimeEnd,     boolean isManager
    ){
        String locker="createTimeStart["+createTimeStart+"]\n"+
                "createTimeEnd["+createTimeEnd+"]\n"+
                "merchantName["+merchantName+"]\n"+
                "commodityName["+commodityName+"]\n"+
                "code["+code+"]\n"+
                "codeId["+codeId+"]\n"+
                "codeStatus["+codeStatus+"]\n"+
                "commodityId["+commodityId+"]\n"+
                "merchantId["+merchantId+"]\n"+
                "overTimeStart["+overTimeStart+"]\n"+
                "overTimeEnd["+overTimeEnd+"]\n";
        try {
            LogFactory.info(this,"尝试根据条件查询串码,\n"+locker);
            EntityWrapper<CouponCode> wrapper=new EntityWrapper<>();
            if(createTimeStart!=null&&!createTimeStart.isEmpty()){
                while(createTimeStart.length()<14)
                    createTimeStart+="0";
                wrapper.ge("create_time",createTimeStart);
            }
            if(createTimeEnd!=null&&!createTimeEnd.isEmpty()){
                while(createTimeEnd.length()<14)
                    createTimeEnd+="9";
                wrapper.le("create_time",createTimeEnd);
            }
            if(merchantName!=null&&!merchantName.isEmpty())
                wrapper.like("merchant_name",merchantName);
            if(commodityName!=null&&!commodityName.isEmpty())
                wrapper.like("commodity_name",commodityName);
            if(code!=null)
                wrapper.eq("code",code);
            if(codeId!=null)
                wrapper.like("code_id",codeId+"");
            if(codeStatus!=null&&CouponCodeStatus.getType(codeStatus)!=null)
                wrapper.eq("code_status",codeStatus.toString());
            if(commodityId!=null)
                wrapper.like("commodity_id",commodityId+"");
            //若isManager参数为true代表是管理员操作,对商户id采用模糊查询
            if(!isManager&&merchantId!=null)
                wrapper.eq("merchant_id",merchantId);
            if(isManager&&merchantId!=null)
                wrapper.like("merchant_id",merchantId+"");
            if(overTimeStart!=null&&!overTimeStart.isEmpty()){
                while(overTimeStart.length()<14)
                    overTimeStart+="0";
                wrapper.ge("over_time",createTimeStart);
            }
            if(overTimeEnd!=null&&!overTimeEnd.isEmpty()){
                while(overTimeEnd.length()<14)
                    overTimeEnd+="9";
                wrapper.le("over_time",overTimeEnd);
            }
            LogFactory.info(this,"根据条件查询串码,实体装饰器拼装结果["+wrapper+"],\n"+locker);
            List<CouponCode> list=daoService.getCouponCodeDao().selectList(wrapper);
            LogFactory.debug(this,"根据条件查询串码,查询结果["+list+"],\n"+locker);
            LogFactory.info(this,"根据条件查询串码,查询结果数量["+list.size()+"],\n"+locker);
            return list;
        } catch (Exception e) {
            LogFactory.error(this,"查询多个串码时发生异常,\n" +locker);
            return new ArrayList<>();
        }
    }
}
