package com.lanxi.couponcode.impl.service;

import java.util.List;

import com.lanxi.couponcode.impl.entity.Merchant;

/**
 * 商户操作
 * 		添加商户
 * 		商户查询等
 * @author wuxiaobo
 *
 */


public interface MerchantService {
	/*添加商户*/
	public boolean addMerchant(Merchant merchant,Long merchantId,Long operaterId,String operaterInfo,String operaterPhone);
	/*修改商户*/
	public boolean updateMerchantById(Merchant merchant,Long merchantId,Long operaterId,String operaterInfo,String operaterPhone);
	/*获取所有商户*/
	public List<Merchant> getAllMerchant();
}
