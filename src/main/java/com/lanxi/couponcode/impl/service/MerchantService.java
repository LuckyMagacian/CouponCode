package com.lanxi.couponcode.impl.service;

import java.util.List;

import com.lanxi.couponcode.impl.entity.Merchant;
import com.lanxi.couponcode.impl.entity.Shop;

/**
 * 商户操作
 * 		添加商户
 * 		修改商户
 * 		分页商户查询无查询条件
 * 		根据条件查询商户
 * 		查询商户详情
 * 		查询拥有门店
 * 		更改商户状态
 * @author wuxiaobo
 *
 */


public interface MerchantService {
	/*添加商户*/
	public Boolean addMerchant(Merchant merchant,Long operaterId,String operaterInfo,
			String operaterPhone);
	/*修改商户*/
	public Boolean updateMerchantById(Merchant merchant,Long operaterId,String operaterInfo,
			String operaterPhone);
	/*分页获取商户无查询条件*/
	public List<Merchant> getAllMerchant(Integer page,Integer size, Long operaterId, 
			String operaterInfo,String operaterPhone);
	/*根据条件获取商户*/
	public List<Merchant> getMerChantByCondition(Integer page,Integer size, Long operaterId, 
			String operaterInfo,String operaterPhone,String startTime,String endTime,String merchantStatus,
			String merchantName);
	/*查询商户详情*/
	public Merchant queryMerchantParticularsById(Long merchantId,Long operaterId,String operaterInfo,
			String operaterPhone);
	/*查询拥有门店*/
	public List<Shop> queryPossessShopByMerchantId(Integer page,Integer size,Long merchantId,Long operaterId,String operaterInfo,
			String operaterPhone);
	/*更改商户状态*/
	public Boolean changeMerchanStatus(Long merchantId,Long operaterId,String operaterInfo,
			String operaterPhone,String merchantStatus);
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
