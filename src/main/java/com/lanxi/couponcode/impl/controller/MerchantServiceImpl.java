package com.lanxi.couponcode.impl.controller;

import com.alibaba.fastjson.JSON;
import com.lanxi.couponcode.impl.assist.RetMessage;
import com.lanxi.couponcode.impl.entity.Merchant;
import com.lanxi.couponcode.impl.service.MerchantService;
import com.lanxi.couponcode.spi.consts.enums.MerchantStatus;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.util.entity.LogFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

/**
 * Created by yangyuanjian on 2017/11/13.
 */
public class MerchantServiceImpl implements com.lanxi.couponcode.spi.service.MerchantService{
	@Resource
	private MerchantService merchantService;
    @Override
    public RetMessage<Boolean> addMerchant(String merchantName, String workAddress, Long operaterId) {
    	RetMessage<Boolean> retMessage=new RetMessage<Boolean>();
    	Boolean result=false;
    	try {
    		Merchant merchant=new Merchant();
    		merchant.setMerchantName(merchantName);
    		merchant.setWorkAddress(workAddress);
			result=merchantService.addMerchant(merchant, operaterId, null, null);
			if(result) {
				retMessage.setRetCode(RetCodeEnum.success+"");
				retMessage.setRetMessage("添加商户成功");
			}
			if(!result) {
				retMessage.setRetCode(RetCodeEnum.exception+"");
				retMessage.setRetMessage("添加商户失败");
			}
			retMessage.setDetail(result);
			
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this,"添加商户时发生异常",e);
		}
        return retMessage;
    }

    @Override
    public RetMessage<Boolean> modifyMerchant(String merchantName, String workAddress, Long operaterId, Long merchantId) {
    	RetMessage<Boolean> retMessage=new RetMessage<Boolean>();
    	Boolean result=false;
    	try {
    		Merchant merchant=new Merchant();
    		merchant.setMerchantName(merchantName);
    		merchant.setWorkAddress(workAddress);
    		merchant.setMerchantId(merchantId);
			result=merchantService.updateMerchantById(merchant, operaterId, null, null);
			if(result) {
				retMessage.setRetCode(RetCodeEnum.success+"");
				retMessage.setRetMessage("修改商户成功");
			}
			if(!result) {
				retMessage.setRetCode(RetCodeEnum.exception+"");
				retMessage.setRetMessage("修改商户失败");
			}
			retMessage.setDetail(result);
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this,"修改商户时发生异常",e);
		}
    	return retMessage;
    }

    @Override
    public RetMessage<String> queryMerchants(String merchantName, MerchantStatus merchantStatus, String timeStart, String timeStop, Integer pageNum, Integer pageSize, Long operaterId) {
    	RetMessage<String> retMessage=new RetMessage<String>();
    	List<Merchant>merchants=null;
    	try {
			merchants=new ArrayList<Merchant>();
			merchants=merchantService.getMerchantByCondition(pageNum, pageSize, operaterId, null, null, timeStart, timeStop, merchantStatus, merchantName);
			String result=JSON.toJSONString(merchants);
			retMessage.setDetail(result);
			if(merchants!=null&&merchants.size()>0) {
				retMessage.setRetCode(RetCodeEnum.success+"");
				retMessage.setRetMessage("查询完毕");
			}else {
				retMessage.setRetCode(RetCodeEnum.exception+"");
				retMessage.setRetMessage("没有查询到任何数据");
			}
    	} catch (Exception e) {
    		LogFactory.error(this, "查询数据是出现异常",e);
			// TODO: handle exception
		}
    	return retMessage;
    }

    @Override
    public RetMessage<File> queryMerchantsExport(String merchantName, MerchantStatus merchantStatus, String timeStart, String timeStop, Long operaterId) {
    	RetMessage<File> retMessage=new RetMessage<File>();
    	try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
    	return null;
    }

    @Override
    public RetMessage<Boolean> enableMerchant(Long merchantId, Long operaterId) {
    	RetMessage<Boolean> retMessage=new RetMessage<Boolean>();
    	Boolean result=false;
    	try {
			result=merchantService.changeMerchanStatus(merchantId, operaterId,null, null,"normal");
			if(result) {
				retMessage.setRetCode(RetCodeEnum.success+"");
				retMessage.setRetMessage("开启商户成功");
			}else {
				retMessage.setRetCode(RetCodeEnum.exception+"");
				retMessage.setRetMessage("开启商户失败");
			}
			retMessage.setDetail(result);
			return retMessage;
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this,"开启商户时发生异常");
			return retMessage;
		}
    }

    @Override
    public RetMessage<Boolean> disableMerchant(Long merchantId, Long operaterId) {
    	RetMessage<Boolean> retMessage=new RetMessage<Boolean>();
    	Boolean result=false;
    	try {
			result=merchantService.changeMerchanStatus(merchantId, operaterId,null, null,"freeze");
			if(result) {
				retMessage.setRetCode(RetCodeEnum.success+"");
				retMessage.setRetMessage("冻结商户成功");
			}else {
				retMessage.setRetCode(RetCodeEnum.exception+"");
				retMessage.setRetMessage("冻结商户失败");
			}
			retMessage.setDetail(result);
			return retMessage;
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this,"冻结商户时发生异常");
			return retMessage;
		}
    }

    @Override
    public RetMessage<Boolean> inputMerchantInfo(String merchantName, String serviceDistription, String workAddress,
    		String businessLicenseNum, String organizingInstitutionBarCode, String enterpriseLegalRepresentativeName, 
    		String contactsName, String contactPhone, String serviceTel, String contactEmail, 
    		File organizingInstitutionBarCodePic, File businessLicensePic, File otherFile, Long operaterId,
    		Long merchantId) {
    	RetMessage<Boolean> retMessage=new RetMessage<Boolean>();
    	Boolean result=false;
    	try {
    		Merchant merchant=new Merchant();
    		merchant.setMerchantName(merchantName);
    		merchant.setWorkAddress(workAddress);
    		merchant.setCharterCode(businessLicenseNum);
    		merchant.setOraganizingCode(organizingInstitutionBarCode);
    		merchant.setPrincipal(enterpriseLegalRepresentativeName);
    		merchant.setLinkMan(contactsName);
    		merchant.setLinkManPhone(contactPhone);
    		merchant.setServiceTel(serviceTel);
    		merchant.setEmail(contactEmail);
    		merchant.setMerchantId(merchantId);
			result=merchantService.fillInInformation(merchant, organizingInstitutionBarCodePic, businessLicensePic, otherFile, null, operaterId, null);
			if(result) {
				
				retMessage.setRetCode(RetCodeEnum.success+"");
				retMessage.setRetMessage("商户详细信息提交成功");
			}else {
				retMessage.setRetCode(RetCodeEnum.exception+"");
				retMessage.setRetMessage("商户详细信息提交失败");
			}
			retMessage.setDetail(result);
    	} catch (Exception e) {
			// TODO: handle exception
    		LogFactory.error(this,"商户详细信息提交时发生异常",e);
		}
    	return retMessage;
    }

    @Override
    public RetMessage<Boolean> modifyMerchantInfo(String merchantName, String serviceDistription, String workAddress, String businessLicenseNum, String organizingInstitutionBarCode, String enterpriseLegalRepresentativeName, String contactsName, String contactPhone, String serviceTel, String contactEmail, File organizingInstitutionBarCodePic, File businessLicensePic, File otherFile, Long operaterId, Long merchantId) {
    	RetMessage<Boolean> retMessage=new RetMessage<Boolean>();
    	Boolean result=false;
    	try {
    		Merchant merchant=new Merchant();
    		merchant.setMerchantName(merchantName);
    		merchant.setWorkAddress(workAddress);
    		merchant.setCharterCode(businessLicenseNum);
    		merchant.setOraganizingCode(organizingInstitutionBarCode);
    		merchant.setPrincipal(enterpriseLegalRepresentativeName);
    		merchant.setLinkMan(contactsName);
    		merchant.setLinkManPhone(contactPhone);
    		merchant.setServiceTel(serviceTel);
    		merchant.setEmail(contactEmail);
    		merchant.setMerchantId(merchantId);
			result=merchantService.fillInInformation(merchant, organizingInstitutionBarCodePic, businessLicensePic, otherFile, null, operaterId, null);
			if(result) {
				retMessage.setRetCode(RetCodeEnum.success+"");
				retMessage.setRetMessage("商户详细信息修改成功");
			}else {
				retMessage.setRetCode(RetCodeEnum.exception+"");
				retMessage.setRetMessage("商户详细信息修改失败");
			}
			retMessage.setDetail(result);
    	} catch (Exception e) {
			// TODO: handle exception
    		LogFactory.error(this,"商户详细信息修改时发生异常",e);
		}
    	return retMessage;
    }
}
