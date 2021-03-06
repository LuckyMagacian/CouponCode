package com.lanxi.couponcode.impl.newservice;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.CouponCode;

import java.util.List;
import java.util.Optional;

/**
 * Created by yangyuanjian on 2017/11/14.
 */
@Deprecated
public interface CodeServiceOld {
    Boolean checkCodeExists(Long codeId);
    Boolean checkCodeExists(Long merchantId,Long code);
    CouponCode generateCode(CouponCode code);
    Boolean delCode(CouponCode code);
    Boolean verificateCode(CouponCode code);
    Boolean overTimeCode(CouponCode code);
    Boolean postoneCode(CouponCode code);
    Optional<CouponCode> queryCode(Long merchantId , Long code);
    CouponCode queryCode(Long codeId);
    List<CouponCode> queryCodes(Wrapper<CouponCode> wrapper, Page<CouponCode> page);
}
