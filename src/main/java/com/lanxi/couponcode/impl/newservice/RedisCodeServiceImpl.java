package com.lanxi.couponcode.impl.newservice;

import com.lanxi.couponcode.impl.entity.CouponCode;
import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.util.entity.LogFactory;
import com.lanxi.util.utils.LoggerUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.lanxi.couponcode.impl.config.ConstConfig.ARTIFCAT;
import static com.lanxi.couponcode.impl.config.ConstConfig.INVALID_LONG;
import static com.lanxi.couponcode.impl.config.FunName.CODE_VALUE;
import static com.lanxi.couponcode.impl.config.FunName.MERCHANT_COUDE_VAR;

/**
 * Created by yangyuanjian on 2017/11/15.
 */
@EasyLog(LoggerUtil.LogLevel.INFO)
@Service("redisCodeService")
public class RedisCodeServiceImpl implements RedisCodeService{
    @Resource
    private RedisService redis;
    @Resource
    private RedisEnhancedService enhancedRedis;
    /**
     * code变量增加的步进
     */
    private static final long CODE_VAR_STEP = 1;
    /**从redis中获取对应的code变量,本方法允许失败*/
    @Override
    public Long getCodeVar(Long merchantId){
        try {
            String key = ARTIFCAT + MERCHANT_COUDE_VAR;
            long var = redis.hincr(key, merchantId+"");
            return var;
        } catch (Exception e) {
            return null;
        }
    }

    public Boolean addCode(CouponCode code){
        String key = ARTIFCAT + CODE_VALUE + code.getMerchantId();
        return redis.setNx(key,code.getCode().toString(),(code.getLifeTime()+1)*24L*60*60*1000);
    }

    @Override
    public Boolean addCode(Long merchantId, Long code) {
        String key = ARTIFCAT + CODE_VALUE + merchantId;
        return redis.setNx(key,code.toString(),null);
    }

    @Override
    public Boolean checkCodeExists(Long merchantId, Long code) {
        try {
            String key = ARTIFCAT + CODE_VALUE + merchantId;
            return redis.hexists(key,code.toString());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Boolean checkCodeExists(CouponCode code) {
        return checkCodeExists(code.getMerchantId(),code.getCode());
    }

    @Override
    public Boolean lockCode(Long merchantId, Long code, String locker) {
        String key = ARTIFCAT + CODE_VALUE + merchantId;
        return enhancedRedis.lock(key,code.toString(),locker);
    }

    @Override
    public Boolean lockCode(CouponCode code, String locker) {
        return lockCode(code.getMerchantId(),code.getCode(),locker);
    }

    @Override
    public Boolean lockCodeForce(Long merchantId, Long code, String locker) {
        String key = ARTIFCAT + CODE_VALUE + merchantId;
        return enhancedRedis.lockForce(key,code.toString(),locker);
    }

    @Override
    public Boolean lockCodeForce(CouponCode code, String locker) {
        return lockCodeForce(code.getMerchantId(),code.getCode(),locker);
    }

    @Override
    public Boolean unlockCode(CouponCode code, String unlocker) {
        return unlockCode(code.getMerchantId(),code.getCode(),unlocker);
    }

    @Override
    public Boolean unlockCode(Long merchantId, Long code, String unlocker) {
        String key = ARTIFCAT + CODE_VALUE + merchantId;
        return enhancedRedis.unlock(key,code.toString(),unlocker);
    }

    @Override
    public Boolean unlockCodeForce(Long merchantId, Long code, String unlocker) {
        String key = ARTIFCAT + CODE_VALUE + merchantId;
        return enhancedRedis.unlockForce(key,code.toString(),unlocker);
    }

    @Override
    public Boolean unlockCodeForce(CouponCode code, String unlocker) {
        return unlockCodeForce(code.getMerchantId(),code.getCode(),unlocker);
    }
    //TODO 取消加锁,修改加锁位置为controller
    @Override
    public Boolean delCode(Long merchantId, Long code) {
        String key = ARTIFCAT + CODE_VALUE + merchantId;
        String locker = this.getClass().getName();
        RedisEnhancedServiceImpl.LockJob job=()->{
            return redis.hdel(key,code.toString());
        };
        return enhancedRedis.workLockJob(job,key,locker);

    }

    @Override
    public Boolean delCode(CouponCode code) {
        return delCode(code.getMerchantId(),code.getCode());
    }

//    @Override
//    public Boolean delCode(Long merchantId, Long... codes) {
//        String key = ARTIFCAT + CODE_VALUE + merchantId;
//        List<String> list=Stream.of(codes).parallel().map(e->e.toString()).collect(Collectors.toList());
//        return redis.hdel(key, (String[]) list.toArray());
//    }

    @Override
    public Boolean delCodeForce(Long merchantId, Long code) {
        String key = ARTIFCAT + CODE_VALUE + merchantId;
        return redis.hdel(key,code.toString());
    }

    @Override
    public Boolean delCodeForce(CouponCode code) {
        return delCode(code.getMerchantId(),code.getCode());
    }

    @Override
    public Boolean postoneCode(Long merchantId, Long code) {
        throw new UnsupportedOperationException("hash can't expire time !");
    }

    @Override
    public Boolean postoneCode(CouponCode code) {
        throw new UnsupportedOperationException("hash can't expire time !");
    }

//    @Override
//    public Boolean delCodeForce(Long merchantId, Long... codes) {
//        String key = ARTIFCAT + CODE_VALUE + merchantId;
//        List<String> list=Stream.of(codes).parallel().map(e->e.toString()).collect(Collectors.toList());
//        return redis.hdel(key, (String[]) list.toArray());
//    }


}
