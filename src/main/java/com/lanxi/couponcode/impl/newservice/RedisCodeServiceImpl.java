package com.lanxi.couponcode.impl.newservice;

import com.lanxi.couponcode.impl.entity.CouponCode;
import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.couponcode.spi.consts.enums.LockResult;
import com.lanxi.util.utils.LoggerUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.lanxi.couponcode.spi.consts.enums.LockResult.*;
import static com.lanxi.couponcode.spi.assist.RedisKeyAssist.getCodeKey;
import static com.lanxi.couponcode.spi.assist.RedisKeyAssist.getVarKey;

/**
 * Created by yangyuanjian on 2017/11/15.
 */
@EasyLog (LoggerUtil.LogLevel.INFO)
@Service ("redisCodeService")
public class RedisCodeServiceImpl implements RedisCodeService {
    @Resource
    private RedisService redis;
    @Resource
    private RedisEnhancedService enhancedRedis;
    /**
     * code变量增加的步进
     */
    private static final long CODE_VAR_STEP = 1;

    private static final String froever = "-1";

    /**
     * 从redis中获取对应的code变量,本方法允许失败
     */
    @Override
    public Long getCodeVar(Long merchantId) {
        try {
            String key = getVarKey(merchantId);
            long var = redis.hincr(key, merchantId + "");
            return var;
        } catch (Exception e) {
            return null;
        }
    }

    public Boolean addCode(CouponCode code) {
        return addCode(code.getMerchantId(), code.getCode());
    }

    @Override
    public Boolean addCode(Long merchantId, Long code) {
        String key = getCodeKey(merchantId);
        return redis.hsetNx(key, code.toString(), froever);
    }

    @Override
    public Boolean checkCodeExists(Long merchantId, Long code) {
        try {
            String key = getCodeKey(merchantId);
            return redis.hexists(key, code.toString());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Boolean checkCodeExists(CouponCode code) {
        return checkCodeExists(code.getMerchantId(), code.getCode());
    }

    @Override
    public Boolean lockCode(Long merchantId, Long code, @Deprecated String locker) {
        String key = getCodeKey(merchantId);
        LockResult result = enhancedRedis.hlock(key, code.toString());
        return success.equals(result);
    }

    @Override
    public Boolean lockCode(CouponCode code, @Deprecated String locker) {
        return lockCode(code.getMerchantId(), code.getCode(), locker);
    }

    @Override
    public Boolean lockCodeForce(Long merchantId, Long code, @Deprecated String locker) {
        String key = getCodeKey(merchantId);
        LockResult result = enhancedRedis.hlockForce(key, code.toString());
        return success.equals(result);
    }

    @Override
    public Boolean lockCodeForce(CouponCode code, @Deprecated String locker) {
        return lockCodeForce(code.getMerchantId(), code.getCode(), locker);
    }

    @Override
    public Boolean unlockCode(CouponCode code, String unlocker) {
        return unlockCode(code.getMerchantId(), code.getCode(), unlocker);
    }

    @Override
    public Boolean unlockCode(Long merchantId, Long code, String unlocker) {
        String key = getCodeKey(merchantId);
        LockResult result = enhancedRedis.hunlock(key, code.toString(), unlocker);
        return success.equals(result);
    }

    @Override
    public Boolean unlockCodeForce(Long merchantId, Long code, @Deprecated String unlocker) {
        String key = getCodeKey(merchantId);
        LockResult result = enhancedRedis.hunlockForce(key, code.toString());
        return success.equals(result);
    }

    @Override
    public Boolean unlockCodeForce(CouponCode code, @Deprecated String unlocker) {
        return unlockCodeForce(code.getMerchantId(), code.getCode(), unlocker);
    }

    @Override
    public Boolean delCode(Long merchantId, Long code) {
        String key = getCodeKey(merchantId);
        return redis.hdel(key, code.toString());
    }

    @Override
    public Boolean delCode(CouponCode code) {
        return delCode(code.getMerchantId(), code.getCode());
    }

//    @Override
//    public Boolean delCode(Long merchantId, Long... codes) {
//        String key = getCodeKey(merchantId);
//        List<String> list=Stream.of(codes).parallel().map(e->e.toString()).collect(Collectors.toList());
//        return redis.hdel(key, (String[]) list.toArray());
//    }

    @Override
    public Boolean delCodeForce(Long merchantId, Long code) {
        String key = getCodeKey(merchantId);
        return redis.hdel(key, code.toString());
    }

    @Override
    public Boolean delCodeForce(CouponCode code) {
        return delCode(code.getMerchantId(), code.getCode());
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
//        String key = getCodeKey(merchantId);
//        List<String> list=Stream.of(codes).parallel().map(e->e.toString()).collect(Collectors.toList());
//        return redis.hdel(key, (String[]) list.toArray());
//    }


}
