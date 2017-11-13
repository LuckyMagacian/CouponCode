package com.lanxi.couponcode.impl.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
	/*商户信息填写*/
	public Boolean fillInInformation(Merchant merchant,MultipartFile organizingInstitutionBarCodePicFile,MultipartFile businessLicensePicFile,
			MultipartFile otherPicFile,Long accountId,Long operaterId,String operaterInfo);
	/*商户组织机构代码证上传*/
	public Boolean organizingInstitutionBarCodePicUpLoad(Merchant merchant,MultipartFile file,Long accountId,Long operaterId,String operaterInfo);
	/*商户工商营业执照上传*/
	public Boolean businessLicensePicUpLoad(Merchant merchant,MultipartFile file,Long accountId,Long operaterId,String operaterInfo);
	/*商户其他证明材料上传*/
	public Boolean otherPicUpLoad(Merchant merchant,MultipartFile file,Long accountId,Long operaterId,String operaterInfo);
	/*导出*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
