package com.lanxi.couponcode.impl.newservice;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.Merchant;
import com.lanxi.couponcode.impl.entity.Shop;
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
	public Boolean addMerchant(Merchant merchant);
	/*修改商户*/
	public Boolean updateMerchantById(Merchant merchant);
	/*分页获取商户无查询条件*/
	public List<Merchant> getAllMerchant(Page<Merchant> pageObj);
	/*根据条件获取商户*/
	public List<Merchant> getMerchantByCondition(Page<Merchant> pageObj, EntityWrapper<Merchant> wrapper);
	/*查询商户详情*/
	public Merchant queryMerchantParticularsById(Long merchantId);
//	/*更改商户状态*/
//	public Boolean changeMerchanStatus(Long merchantId,
//			Long operaterId, 
//			String operaterInfo,
//			String operaterPhone, 
//			String merchantStatus);
	/*冻结商户*/
	public Boolean freezeMerchant(Merchant merchant);
	/*开启商户*/
	public Boolean unFreezeMerchant(Merchant merchant);
	/*商户信息填写*/
	public Boolean fillInInformation(Merchant merchant, 
			File organizingInstitutionBarCodePicFile, 
			File businessLicensePicFile,
			File otherPicFile);
	/*商户组织机构代码证上传*/
	public Boolean organizingInstitutionBarCodePicUpLoad(Merchant merchant, 
			File file);
	/*商户工商营业执照上传*/
	public Boolean businessLicensePicUpLoad(Merchant merchant, 
			File file);
	/*商户其他证明材料上传*/
	public Boolean otherPicUpLoad(Merchant merchant,
			File file);
	/*根据商户id查询商户的状态*/
	public String queryMerchantStatusByid(Long merchantId,
			Long operaterId);
	/*导出*/
	
	
	

	
}
