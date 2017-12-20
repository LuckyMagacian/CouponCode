package com.lanxi.couponcode.impl.newservice;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.Merchant;
import com.lanxi.couponcode.spi.consts.enums.MerchantStatus;

import java.io.File;
import java.util.List;

/**
 * 商户操作
 * 		添加商户
 * 		修改商户
 * 		分页商户查询无查询条件
 * 		根据条件查询商户
 * 		查询商户详情
 * 		查询拥有门店
 * 		更改商户状态
 * 		商户信息填写
 * @author wuxiaobo
 *
 */
 public interface MerchantService {
	/*添加商户*/
	 Boolean addMerchant(Merchant merchant);
	/*修改商户*/
	 Boolean updateMerchantById(Merchant merchant);
	/*分页获取商户无查询条件*/
	 List<Merchant> getAllMerchant(Page<Merchant> pageObj);
	/*根据条件获取商户*/
	 List<Merchant> getMerchantByCondition(Page<Merchant> pageObj, EntityWrapper<Merchant> wrapper);
	/*查询商户详情*/
	 Merchant queryMerchantParticularsById(Long merchantId);
//	/*更改商户状态*/
//	 Boolean changeMerchanStatus(Long merchantId,
//			Long operaterId, 
//			String operaterInfo,
//			String operaterPhone, 
//			String merchantStatus);
	/*冻结商户*/
	 Boolean freezeMerchant(Merchant merchant);
	/*开启商户*/
	 Boolean unFreezeMerchant(Merchant merchant);
	/*商户信息填写*/
	 Boolean fillInInformation(Merchant merchant);
	/*商户组织机构代码证上传*/
	 Boolean organizingInstitutionBarCodePicUpLoad(Merchant merchant,
                                                         File file);
	/*商户工商营业执照上传*/
	 Boolean businessLicensePicUpLoad(Merchant merchant,
                                            File file);
	/*商户其他证明材料上传*/
	 Boolean otherPicUpLoad(Merchant merchant,
                                  File file);
	/*根据商户id查询商户的状态*/
	 String queryMerchantStatusByid(Long merchantId,
                                          Long operaterId);
	/*导出*/
	 File merchantExport(String merchantName, MerchantStatus merchantStatus, String timeStart,
                               String timeStop, Long operaterId);
	/**判断商户名称是否可用
	 * true可用 
	 * false 不可用*/
	 Boolean isRepeat(String merchantName);
	/**
	 * 判断商户名称是否可用
	 * @param merchantName
	 * @param merchantId
	 * @return true可用 false 不可用
	 * 
	 */
	 Boolean isRepeat(String merchantName,Long merchantId);
	 List<Merchant> queryAll();
}
