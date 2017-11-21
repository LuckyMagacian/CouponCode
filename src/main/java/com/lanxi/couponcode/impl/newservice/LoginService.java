package com.lanxi.couponcode.impl.newservice;


import com.lanxi.couponcode.impl.entity.Account;

/**
 * 登录操作接口
 * @author wuxiaobo
 *
 */

public interface LoginService {
	/*登录*/
	public Account login(Account account,
			String validateCode);
	/*登出*/
	public Boolean logout(Long accountId);
	/*忘记密码*/
	public Boolean forgetPassword(String validateCode,
			Account account,
			Long accountId);
	/*修改密码*/
	public Boolean changePassword(Account account);
	/*判断时间两个时间差值是否大于15分钟*/
	public Boolean localdateLtDate(String date);
}
