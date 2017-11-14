package com.lanxi.couponcode.impl.service;

import com.lanxi.couponcode.impl.entity.Account;
/**
 * 账户操作接口
 * @author wuxiaobo
 *
 */
public interface AccountService {
	/*添加账户*/
	public Boolean addAccount(Account account,Long merchantId,Long operaterId);
}
