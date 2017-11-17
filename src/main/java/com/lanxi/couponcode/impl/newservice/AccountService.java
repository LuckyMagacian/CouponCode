package com.lanxi.couponcode.impl.newservice;

import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.Account;
import com.lanxi.couponcode.spi.consts.enums.AccountStatus;
import com.lanxi.couponcode.spi.consts.enums.AccountType;

import java.util.List;

/**
 * 账户操作接口
 * @author wuxiaobo
 *
 */
public interface AccountService {
	/*添加账户*/
	public Boolean addAccount(AccountType accountType, String name, String phone, String merchantName, Long merchantId, Long operaterId);
	/*商户添加账户*/
	public Boolean merchantAccount(AccountType accountType, String name, String phone, String shopName, Long shopId, Long operaterId);
	/*冻结账户*/
	public Boolean freezeAccount(Long accountId, Long operaterId);
	/*开启账户*/
	public Boolean unfreezeAccount(Long accountId, Long operaterId);
	/*删除账户*/
	public Boolean delAccount(Long accountId, Long operaterId);
	/*账户查询*/
	public List<Account> queryAccounts(String phone, String merchantName, AccountType type, AccountStatus status,
                                       Page<Account> pageObj, Long operaterId);
	public List<Account> merchantQueryAccounts(String phone, String shopName, AccountType type, AccountStatus status,
                                               Page<Account> pageObj, Long operaterId);

	public List<Account> queryShopAccounts(Long shopId, Long operaterId);
	/*账户详情查询*/
	public Account queryAccountInfo(Long accountId, Long operaterId);
	/*手机号码验证*/
	public Boolean phoneVerify(String phone, Long operaterId);
}
