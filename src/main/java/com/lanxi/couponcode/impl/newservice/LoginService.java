package com.lanxi.couponcode.impl.newservice;
/**
 * 
 * @author wuxiaobo
 *
 */

import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.impl.entity.Account;


public interface LoginService {
	/*登录*/
	public RetMessage<Account> login(String phone, String password, String validateCode);
	/*登出*/
	public Boolean logout(Long accountId);
	/*忘记密码*/
	public RetMessage<Boolean> forgetPassword(String phone, String validateCode, String newPassword, String newRepeat, Long accountId);
	/*修改密码*/
	public RetMessage<Boolean> changePassword(String oldPasswd, String newPasswd, String newRepeat, Long accountId);
	
}
