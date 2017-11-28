package com.lanxi.couponcode.impl.newservice;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.CouponCode;

import java.util.List;
import java.util.Optional;

/**
 * Created by yangyuanjian on 2017/11/23.
 */
public interface CodeService {
    Boolean addCode(CouponCode code);
    Boolean checkCodeExists(Long codeId);
    Boolean checkCodeExists(Long merchantId,Long code);
    Boolean delCode(CouponCode code);
    Boolean verificateCode(CouponCode code);
    Boolean overTimeCode(CouponCode code);
    Boolean postoneCode(CouponCode code);
    Optional<CouponCode> queryCode(Long merchantId , Long code);
    CouponCode queryCode(Long codeId);
    CouponCode queryCodeInfo(Long codeId,Long merchantId);
    List<CouponCode> queryCodes(Wrapper<CouponCode> wrapper, Page<CouponCode> page);
}
