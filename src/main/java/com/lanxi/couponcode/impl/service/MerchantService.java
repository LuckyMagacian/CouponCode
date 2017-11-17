package com.lanxi.couponcode.impl.service;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.Merchant;
import com.lanxi.couponcode.impl.entity.Shop;
import com.lanxi.couponcode.spi.consts.enums.MerchantStatus;

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
	public Boolean addMerchant(Merchant merchant,Long operaterId,String operaterInfo,
			String operaterPhone);
	/*修改商户*/
	public Boolean updateMerchantById(Merchant merchant,Long operaterId,String operaterInfo,
			String operaterPhone);
	/*分页获取商户无查询条件*/
	public List<Merchant> getAllMerchant(Page<Merchant> pageObj, Long operaterId, 
			String operaterInfo,String operaterPhone);
	/*根据条件获取商户*/
	public List<Merchant> getMerchantByCondition(Page<Merchant> pageObj, Long operaterId, 
			String operaterInfo,String operaterPhone,String startTime,String endTime,MerchantStatus merchantStatus,
			String merchantName);
	/*查询商户详情*/
	public Merchant queryMerchantParticularsById(Long merchantId,Long operaterId,String operaterInfo,
			String operaterPhone);
	/*查询拥有门店*/
	public List<Shop> queryPossessShopByMerchantId(Page<Shop> pageObj,Long merchantId,Long operaterId,String operaterInfo,
			String operaterPhone);
	/*更改商户状态*/
	public Boolean changeMerchanStatus(Long merchantId,Long operaterId,String operaterInfo,
			String operaterPhone,String merchantStatus);	
	/*商户信息填写*/
	public Boolean fillInInformation(Merchant merchant,File organizingInstitutionBarCodePicFile,File businessLicensePicFile,
			File otherPicFile,Long accountId,Long operaterId,String operaterInfo);
	/*商户组织机构代码证上传*/
	public Boolean organizingInstitutionBarCodePicUpLoad(Merchant merchant,File file,Long accountId,Long operaterId,String operaterInfo);
	/*商户工商营业执照上传*/
	public Boolean businessLicensePicUpLoad(Merchant merchant,File file,Long accountId,Long operaterId,String operaterInfo);
	/*商户其他证明材料上传*/
	public Boolean otherPicUpLoad(Merchant merchant,File file,Long accountId,Long operaterId,String operaterInfo);
	/*根据商户id查询商户的状态*/
	public String queryMerchantStatusByid(Long merchantId,Long operaterId);
	/*导出*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
