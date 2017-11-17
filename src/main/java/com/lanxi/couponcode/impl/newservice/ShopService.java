package com.lanxi.couponcode.impl.newservice;

import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.Shop;
import com.lanxi.couponcode.spi.consts.enums.ShopStatus;

import java.io.File;
import java.util.List;

/**
 * 门店操作类
 * 		冻结所有门店
 * 		修改门店状态
 * 		根据所属商户id查询门店
 * 		单个添加门店
 * @author wuxiaobo
 *
 */
public interface ShopService {
//	/*根据所属商户id查询门店*/
//	public List<Shop> queryPossessShopByMerchantId(Integer page,Integer size,Long merchantId,Long operaterId,
//	String operaterInfo,String operaterPhone);
	/*冻结所有门店*/
	public Boolean freezeAllShop(Long merchantId, Long operaterId, String operaterInfo, String operaterPhone);
	/*单个添加门店*/
	public Boolean addShop(String shopName, String shopAddress, String minuteShopAddress, String serviceTel, Long merchantId,
                           Long operaterId);
	/*导入门店*/
	public List<String> importShops(File file, Long merchantId, Long operaterId);
	/*冻结门店*/
	public Boolean freezeShop(Long shopId, Long operaterId);
	/*开启门店*/
	public Boolean unfreezeShop(Long shopId, Long operaterId);
	/*查询门店分页*/
	public List<Shop> queryShop(String shopName, ShopStatus status, String shopAddress, Page<Shop> pageObj,
                                Long merchantId, Long operaterId);
	/*查询门店*/
	public List<Shop> queryShop(String shopName, ShopStatus status, String shopAddress, Long merchantId,
                                Long operaterId);
	/*修改门店*/
	public Boolean modifyShop(String shopName, String shopAddress, String minuteShopAddress, String serviceTel, Long shopId, Long operaterId);
	/*导出*/
	public File queryShopsExport(String shopName, String shopAddress, ShopStatus status, Long merchantId,
                                 Long operaterId);
	
}
