package com.lanxi.couponcode.impl.newservice;

import com.lanxi.couponcode.impl.entity.CodeAlgorithm;

/**
 * Created by yangyuanjian on 2017/11/15.
 */
public interface CodeAlgorithmService {
    CodeAlgorithm getCodeAlgorithm(long merchantId);
}
