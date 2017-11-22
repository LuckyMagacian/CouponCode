package com.lanxi.couponcode.impl.newservice;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.Account;

import java.util.List;

/**
 * 账户操作接口
 * @author wuxiaobo
 *
 */
public interface AccountService {
	/*添加账户*/
	public Boolean addAccount(Account account);
	/*商户添加账户*/
	public Boolean merchantAccount(Account account);
	/*冻结账户*/
	public Boolean freezeAccount(Account account);
	/*开启账户*/
	public Boolean unfreezeAccount(Account account);
	/*删除账户*/
	public Boolean delAccount(Account account);
	/*账户查询*/
	public List<Account> queryAccounts(EntityWrapper<Account> wrapper,
			Page<Account> pageObj);
	public List<Account> merchantQueryAccounts(EntityWrapper<Account> wrapper,
			Page<Account> pageObj);
	public List<Account> queryShopAccounts(EntityWrapper<Account> wrapper,
			Page<Account> pageObj);
	/*账户详情查询*/
	public Account queryAccountInfo(Account account);
	/*手机号码验证*/
	public Boolean phoneVerify(String phone);
	/*修改账户仅在用户登录时用来修改登录失败次数和登录失败时间*/
	public Boolean modifyAccount(Account account);
	/*修改账户的商户名称*/
	public Boolean modifyAccountMerchantName(Long merchantId,String merchantName);
	/*修改账户的门店名称*/
	public Boolean modifyAccountShopName(Long shopId,String shopName);
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
	/*根据账户id查询账户状态*/
	public String queryAccountStatusById(Long accountId);
}
