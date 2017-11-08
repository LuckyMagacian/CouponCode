package com.lanxi.couponcode.spi.service;

import com.lanxi.couponcode.spi.consts.annotations.HiddenArg;

/**
 * Created by yangyuanjian on 2017/11/7.
 */
public interface LoginService {
    Boolean login(String phone,
                         String password,
                         String validateCode);

    Boolean forgetPassword(String phone,
                                  String validateCode,
                                  String newPassword);
    Boolean logout(String phone);

    Boolean changePassword(@HiddenArg String phone,
                           String oldPasswd,
                           String newPasswd,
                           String newRepeat);
}
