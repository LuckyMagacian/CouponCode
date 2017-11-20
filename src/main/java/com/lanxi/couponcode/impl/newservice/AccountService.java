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
}
