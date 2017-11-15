package com.lanxi.couponcode.impl.service;
/**
 * 
 * @author wuxiaobo
 *
 */

import com.lanxi.couponcode.impl.assist.RetMessage;


public interface LoginService {
	/*用户登录*/
	public RetMessage<Boolean> login(String phone,String password,String validateCode); 
	/*登出*/
	public Boolean logout(Long accountId);
	/*忘记密码*/
	public RetMessage<Boolean> forgetPassword(String phone,String validateCode,String newPassword,String newRepeat,Long accountId);
	/*修改密码*/
	public RetMessage<Boolean> changePassword(String oldPasswd,String newPasswd,String newRepeat,Long accountId);
}
