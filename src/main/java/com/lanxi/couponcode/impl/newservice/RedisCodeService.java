package com.lanxi.couponcode.impl.newservice;

import com.lanxi.couponcode.impl.entity.CouponCode;

/**
 * Created by yangyuanjian on 2017/11/15.
 */
public interface RedisCodeService {
    Long getCodeVar(Long merchantId);
    Boolean addCode(Long merchantId,Long code);
    public Boolean addCode(CouponCode code);

    Boolean checkCodeExists(Long merchantId,Long code);
    Boolean checkCodeExists(CouponCode code);
    Boolean lockCode(Long merchantId,Long code,String locker);
    Boolean lockCode(CouponCode code,String locker);
    Boolean lockCodeForce(Long merchantId,Long code,String locker);
    Boolean lockCodeForce(CouponCode code,String locker);
    Boolean unlockCode(CouponCode code,String unlocker);
    Boolean unlockCode(Long merchantId,Long code,String unlocker);
    Boolean unlockCodeForce(Long merchantId,Long code,String unlocker);
    Boolean unlockCodeForce(CouponCode code,String unlocker);

    Boolean delCode(Long merchantId,Long code);
    Boolean delCode(CouponCode code);
//    Boolean delCode(Long merchantId,Long... codes);

    Boolean delCodeForce(Long merchantId,Long code);
    Boolean delCodeForce(CouponCode code);
//    Boolean delCodeForce(Long merchantId,Long... codes);

    Boolean postoneCode(Long merchantId,Long code);
    Boolean postoneCode(CouponCode code);

}
