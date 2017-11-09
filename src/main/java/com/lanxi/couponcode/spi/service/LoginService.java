package com.lanxi.couponcode.spi.service;

import com.lanxi.couponcode.impl.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.annotations.HiddenArg;
import com.lanxi.couponcode.spi.consts.annotations.RealReturnType;

/**
 * Created by yangyuanjian on 2017/11/7.
 */
public interface LoginService {
    /**
     * 用户登录<br>
     * @param phone 手机号码,后台保证系统中一个手机号只有一个账户处于可用状态<br>
     * @param password 密码<br>
     * @param validateCode 图形验证码<br>
     * @return 登录结果 成功或者 错误及对应的错误信息<br>
     */
    @RealReturnType("RetMessage")
    RetMessage<String> login(String phone,
                              String password,
                              String validateCode);

    /**
     * 登出<br>
     * @param accountId 登录账户id<br>
     * @return 登出结果 true 登出成功 | false 登出失败<br>
     */
    RetMessage<Boolean> logout(@HiddenArg Long accountId);

    /**
     * 忘记密码<br>
     * @param phone 手机号码,用于接收验证码短信<br>
     * @param validateCode 手机验证码<br>
     * @param newPassword 新密码<br>
     * @param newRepeat 重复新密码<br>
     * @param accountId 账户id<br>
     * @return 操作结果 成功或者 对应的错误信息
     */
    @RealReturnType("RetMessage")
    RetMessage<String> forgetPassword(String phone,
                          String validateCode,
                          String newPassword,
                          String newRepeat,
                          @HiddenArg Long accountId);

    /**
     * 修改密码<br>
     * @param oldPasswd 原密码<br>
     * @param newPasswd 新密码<br>
     * @param newRepeat 新密码重复<br>
     * @param accountId 账户id<br>
     * @return 操作结果 成功或 对应的错误信息<br>
     */
    @RealReturnType("RetMessage")
    RetMessage<String> changePassword(
                           String oldPasswd,
                           String newPasswd,
                           String newRepeat,
                           @HiddenArg Long accountId);
}
