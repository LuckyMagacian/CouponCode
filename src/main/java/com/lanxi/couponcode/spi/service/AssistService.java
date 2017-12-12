package com.lanxi.couponcode.spi.service;

import com.lanxi.couponcode.spi.assist.RetMessage;

/**
 * Created by yangyuanjian on 2017/11/7.
 */
public interface AssistService {
    RetMessage<Boolean> sendValidateCode(String phone);
    RetMessage<Boolean> verifyCode(String phone, String code);
}
