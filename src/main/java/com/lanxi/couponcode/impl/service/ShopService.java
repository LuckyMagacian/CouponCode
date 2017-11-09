package com.lanxi.couponcode.impl.service;

import java.util.List;

import com.lanxi.couponcode.impl.entity.Shop;

/**
 * 门店操作类
 * 		冻结所有门店
 * 		修改门店状态
 * 		根据所属商户id查询门店
 * @author wuxiaobo
 *
 */
public interface ShopService {
//	/*根据所属商户id查询门店*/
//	public List<Shop> queryPossessShopByMerchantId(Integer page,Integer size,Long merchantId,Long operaterId,String operaterInfo,
//			String operaterPhone);
	public Boolean freezeAllShop(Long merchantId,Long operaterId,String operaterInfo,
			String operaterPhone);
	
}
