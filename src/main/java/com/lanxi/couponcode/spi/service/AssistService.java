package com.lanxi.couponcode.spi.service;

/**
 * Created by yangyuanjian on 2017/11/7.
 */
public interface AssistService {
    Boolean sendValidateCode(String phone);
    Boolean verifyCode(String phone,String code);
}
