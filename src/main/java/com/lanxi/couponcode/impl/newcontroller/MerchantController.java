package com.lanxi.couponcode.impl.newcontroller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.assist.TimeAssist;
import com.lanxi.couponcode.spi.config.ConstConfig;
import com.lanxi.couponcode.impl.entity.Account;
import com.lanxi.couponcode.impl.entity.Merchant;
import com.lanxi.couponcode.impl.entity.OperateRecord;
import com.lanxi.couponcode.impl.newservice.AccountService;
import com.lanxi.couponcode.impl.newservice.MerchantService;
import com.lanxi.couponcode.impl.newservice.OperateRecordService;
import com.lanxi.couponcode.impl.newservice.RedisEnhancedService;
import com.lanxi.couponcode.impl.newservice.RedisService;
import com.lanxi.couponcode.spi.consts.enums.AccountStatus;
import com.lanxi.couponcode.spi.consts.enums.MerchantStatus;
import com.lanxi.couponcode.spi.consts.enums.OperateTargetType;
import com.lanxi.couponcode.spi.consts.enums.OperateType;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.util.entity.LogFactory;
import javax.annotation.Resource;
import static com.lanxi.couponcode.impl.assist.PredicateAssist.*;
import org.springframework.stereotype.Controller;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by yangyuanjian on 2017/11/13.
 */
@Controller("merchantControllerService")
public class MerchantController implements com.lanxi.couponcode.spi.service.MerchantService {
	@Resource
	private AccountService accountService;
	@Resource
	private MerchantService merchantService;
	@Resource
	private RedisService redisService;
	@Resource
	private RedisEnhancedService redisEnhancedService;
	@Resource
	private OperateRecordService operateRecordService;

	@Override
	public RetMessage<Boolean> addMerchant(String merchantName, String workAddress, String minuteWorkAddress,
			Long operaterId) {
		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result = false;
		// TODO 校验
		try {
			Account a = accountService.queryAccountById(operaterId);
			RetMessage message = checkAccount.apply(a, OperateType.createMerchant);
			if (notNull.test(message)) 
				return message;
			Merchant merchant = new Merchant();
			merchant.setMerchantName(merchantName);
			merchant.setWorkAddress(workAddress);
			if (minuteWorkAddress != null && !minuteWorkAddress.isEmpty()) {
				merchant.setMinuteWorkAddress(minuteWorkAddress);
			}
			merchant.setMerchantId(IdWorker.getId());
			merchant.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
			merchant.setMerchantStatus(MerchantStatus.normal);
			merchant.setAddId(operaterId);
			merchant.setAddName(a.getUserName());
			result = merchantService.addMerchant(merchant);
			if (result) {
				retMessage.setRetCode(RetCodeEnum.success.getValue());
				retMessage.setRetMessage("添加商户成功");
				OperateRecord record = new OperateRecord();
				record.setRecordId(IdWorker.getId());
				record.setOperaterId(operaterId);
				record.setAccountType(a.getAccountType());
				record.setPhone(a.getPhone());
				record.setName(a.getUserName());
				record.setTargetType(OperateTargetType.merchant);
				record.setType(OperateType.createMerchant);
				record.setOperateTime(TimeAssist.getNow());
				record.setOperateResult("success");
				record.setDescription("添加商户[" + merchant.getMerchantId() + "]");
				operateRecordService.addRecord(record);
			}
			if (!result) {
				retMessage.setRetCode(RetCodeEnum.exception.getValue());
				retMessage.setRetMessage("添加商户失败");
			}
			retMessage.setDetail(result);
		} catch (Exception e) {
			LogFactory.error(this, "添加商户时发生异常", e);
			retMessage.setAll(RetCodeEnum.error, "添加商户时发生异常", result);
		}
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> modifyMerchant(String merchantName, String workAddress, String minuteWorkAddress,
			Long operaterId, Long merchantId) {
		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result = false;
		// TODO 校验
		try {
			Account a = accountService.queryAccountById(operaterId);
			RetMessage message = checkAccount.apply(a, OperateType.modifyMerchant);
			if (notNull.test(message))
				return message;
			Merchant m = merchantService.queryMerchantParticularsById(merchantId);
			message = checkMerchant.apply(m, OperateType.modifyMerchant);
			if (notNull.test(message))
				return message;
			Merchant merchant = new Merchant();
			merchant.setMerchantId(merchantId);
			if (merchantName != null && !merchantName.isEmpty() && workAddress != null && !workAddress.isEmpty()) {
				merchant.setMerchantName(merchantName);
				merchant.setWorkAddress(workAddress);
			}
			if (minuteWorkAddress != null && !minuteWorkAddress.isEmpty()) {
				merchant.setMinuteWorkAddress(minuteWorkAddress);
			}
			result = merchantService.updateMerchantById(merchant);
			if (result) {
				retMessage.setRetCode(RetCodeEnum.success.getValue());
				retMessage.setRetMessage("修改商户成功");
				if (merchantName != null) {
					accountService.modifyAccountMerchantName(merchantId, merchantName);
				}
				OperateRecord record = new OperateRecord();
				record.setRecordId(IdWorker.getId());
				record.setOperaterId(operaterId);
				record.setAccountType(a.getAccountType());
				record.setPhone(a.getPhone());
				record.setName(a.getUserName());
				record.setTargetType(OperateTargetType.merchant);
				record.setType(OperateType.modifyMerchant);
				record.setOperateTime(TimeAssist.getNow());
				record.setOperateResult("success");
				record.setDescription("修改商户[" + merchantId + "]");
				operateRecordService.addRecord(record);
			}
			if (!result) {
				retMessage.setRetCode(RetCodeEnum.exception.getValue());
				retMessage.setRetMessage("修改商户失败");
			}
			retMessage.setDetail(result);
		} catch (Exception e) {
			LogFactory.error(this, "修改商户时发生异常", e);
			retMessage.setAll(RetCodeEnum.error, "修改商户时发生异常", result);
		}
		return retMessage;
	}

	@Override
	public RetMessage<String> queryMerchants(String merchantName, MerchantStatus merchantStatus, String timeStart,
			String timeStop, Integer pageNum, Integer pageSize, Long operaterId) {
		RetMessage<String> retMessage = new RetMessage<String>();
		List<Merchant> merchants = null;
		// TODO 校验权限
		try {
			Account a = accountService.queryAccountById(operaterId);
			RetMessage message = checkAccount.apply(a, OperateType.queryMerchant);
			if (notNull.test(message))
				return message;
			EntityWrapper<Merchant> wrapper = new EntityWrapper<Merchant>();
			if (pageNum != null) {
				pageSize = pageSize == null ? ConstConfig.DEFAULT_PAGE_SIZE : pageSize;
			}
			if (timeStart != null && !timeStart.isEmpty()) {
				while (timeStart.length() < 14)
					timeStart += "0";
				wrapper.ge("create_time", timeStart);
			}
			if (timeStop != null && !timeStop.isEmpty()) {
				while (timeStop.length() < 14)
					timeStop += "9";
				wrapper.le("create_time", timeStop);
			}
			if (merchantStatus != null) {
				wrapper.eq("merchant_status", merchantStatus);
			}
			if (merchantName != null && !merchantName.isEmpty()) {
				wrapper.eq("merchant_name", merchantName);
			}
			Page<Merchant> pageObj = new Page<Merchant>(pageNum, pageSize);
			merchants = merchantService.getMerchantByCondition(pageObj, wrapper);
			String result = JSON.toJSONString(merchants);
			retMessage.setDetail(result);
			if (merchants != null && merchants.size() > 0) {
				retMessage.setRetCode(RetCodeEnum.success.getValue());
				retMessage.setRetMessage("查询完毕");
			} else {
				retMessage.setRetCode(RetCodeEnum.exception.getValue());
				retMessage.setRetMessage("没有查询到任何数据");
			}
		} catch (Exception e) {
			LogFactory.error(this, "查询数据时出现异常", e);
			retMessage.setAll(RetCodeEnum.error, "查询数据时发生异常", null);
		}
		return retMessage;
	}

	@Override
	public RetMessage<File> queryMerchantsExport(String merchantName, MerchantStatus merchantStatus, String timeStart,
			String timeStop, Long operaterId) {
		RetMessage<File> retMessage = new RetMessage<File>();
		// TODO 校验
		try {
			if (operaterId != null) {
				// 先校验操作者账户状态
				if (AccountStatus.normal.equals(accountService.queryAccountStatusById(operaterId))) {
					
				} else {
					retMessage.setAll(RetCodeEnum.warning, "您的账户状态不正常", null);
				}
			} else {

				retMessage.setAll(RetCodeEnum.fail, "账户id为空", null);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> enableMerchant(Long merchantId, Long operaterId) {
		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result = false;
		// TODO 权限校验
		try {
			Account a = accountService.queryAccountById(operaterId);
			RetMessage message = checkAccount.apply(a, OperateType.unfreezeMerchant);
			if (notNull.test(message))
				return message;
			Merchant m = merchantService.queryMerchantParticularsById(merchantId);
			message = checkMerchant.apply(m, OperateType.unfreezeMerchant);
			if (notNull.test(message))
				return message;
			Merchant merchant = new Merchant();
			merchant.setMerchantId(merchantId);
			merchant.setMerchantStatus(MerchantStatus.normal);
			result = merchantService.unFreezeMerchant(merchant);
			if (result) {
				retMessage.setRetCode(RetCodeEnum.success.getValue());
				retMessage.setRetMessage("开启商户成功");
				OperateRecord record = new OperateRecord();
				record.setRecordId(IdWorker.getId());
				record.setOperaterId(operaterId);
				record.setAccountType(a.getAccountType());
				record.setPhone(a.getPhone());
				record.setName(a.getUserName());
				record.setTargetType(OperateTargetType.merchant);
				record.setType(OperateType.unfreezeMerchant);
				record.setOperateTime(TimeAssist.getNow());
				record.setOperateResult("success");
				record.setDescription("开启商户[" + merchantId + "]");
				operateRecordService.addRecord(record);
			} else {
				retMessage.setRetCode(RetCodeEnum.exception.getValue());
				retMessage.setRetMessage("开启商户失败");
			}
			retMessage.setDetail(result);
		} catch (Exception e) {
			LogFactory.error(this, "开启商户时发生异常");
			retMessage.setAll(RetCodeEnum.error, "开启商户时发生异常", result);
		}
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> disableMerchant(Long merchantId, Long operaterId) {
		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result = false;
		// TODO 权限校验
		try {
			Account a = accountService.queryAccountById(operaterId);
			RetMessage message = checkAccount.apply(a, OperateType.freezeMerchant);
			if (notNull.test(message))
				return message;
			Merchant m = merchantService.queryMerchantParticularsById(merchantId);
			message = checkMerchant.apply(m, OperateType.freezeMerchant);
			if (notNull.test(message))
				return message;
			Merchant merchant = new Merchant();
			merchant.setMerchantId(merchantId);
			merchant.setMerchantStatus(MerchantStatus.freeze);
			result = merchantService.freezeMerchant(merchant);
			if (result) {
				retMessage.setRetCode(RetCodeEnum.success.getValue());
				retMessage.setRetMessage("冻结商户成功");
				OperateRecord record = new OperateRecord();
				record.setRecordId(IdWorker.getId());
				record.setOperaterId(operaterId);
				record.setAccountType(a.getAccountType());
				record.setPhone(a.getPhone());
				record.setName(a.getUserName());
				record.setTargetType(OperateTargetType.merchant);
				record.setType(OperateType.freezeMerchant);
				record.setOperateTime(TimeAssist.getNow());
				record.setOperateResult("success");
				record.setDescription("冻结商户[" + merchantId + "]");
				operateRecordService.addRecord(record);
			} else {
				retMessage.setRetCode(RetCodeEnum.exception.getValue());
				retMessage.setRetMessage("冻结商户失败");
			}
			retMessage.setDetail(result);
		} catch (Exception e) {
			LogFactory.error(this, "冻结商户时发生异常");
			retMessage.setAll(RetCodeEnum.error, "冻结商户时发生异常", result);
		}
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> inputMerchantInfo(String merchantName, String serviceDistription, String workAddress,
			String minuteWorkAddress, String businessLicenseNum, String organizingInstitutionBarCode,
			String enterpriseLegalRepresentativeName, String contactsName, String contactPhone, String serviceTel,
			String contactEmail, Long operaterId, Long merchantId) {
		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result = false;
		// TODO 校验
		try {
			Account a = accountService.queryAccountById(operaterId);
			RetMessage message = checkAccount.apply(a, OperateType.inputMerchantInfo);
			if (notNull.test(message))
				return message;
			Merchant m = merchantService.queryMerchantParticularsById(merchantId);
			message = checkMerchant.apply(m, OperateType.inputMerchantInfo);
			if (notNull.test(message))
				return message;
			Merchant merchant = new Merchant();
			merchant.setMerchantName(merchantName);
			merchant.setWorkAddress(workAddress);
			merchant.setMinuteWorkAddress(minuteWorkAddress);
			merchant.setCharterCode(businessLicenseNum);
			merchant.setOraganizingCode(organizingInstitutionBarCode);
			merchant.setPrincipal(enterpriseLegalRepresentativeName);
			merchant.setLinkMan(contactsName);
			merchant.setLinkManPhone(contactPhone);
			merchant.setServiceTel(serviceTel);
			merchant.setEmail(contactEmail);
			merchant.setMerchantId(merchantId);
			merchant.setServeExplain(serviceDistription);
			result = merchantService.fillInInformation(merchant);
			if (result) {
				retMessage.setRetCode(RetCodeEnum.success.getValue());
				retMessage.setRetMessage("商户详细信息提交成功");
				retMessage.setRetCode(RetCodeEnum.success.getValue());
				retMessage.setRetMessage("冻结商户成功");
				OperateRecord record = new OperateRecord();
				record.setRecordId(IdWorker.getId());
				record.setOperaterId(operaterId);
				record.setAccountType(a.getAccountType());
				record.setPhone(a.getPhone());
				record.setName(a.getUserName());
				record.setTargetType(OperateTargetType.merchant);
				record.setType(OperateType.inputMerchantInfo);
				record.setOperateTime(TimeAssist.getNow());
				record.setOperateResult("success");
				record.setDescription("商户详细信息提交[" + merchantId + "]");
				operateRecordService.addRecord(record);
			} else {
				retMessage.setRetCode(RetCodeEnum.exception.getValue());
				retMessage.setRetMessage("商户详细信息提交失败");
			}
			retMessage.setDetail(result);
		} catch (Exception e) {
			LogFactory.error(this, "商户详细信息提交时发生异常", e);
			retMessage.setAll(RetCodeEnum.error, "商户详细信息提交时发生异常", result);
		}
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> modifyMerchantInfo(String merchantName, String serviceDistription, String workAddress,
			String minuteWorkAddress, String businessLicenseNum, String organizingInstitutionBarCode,
			String enterpriseLegalRepresentativeName, String contactsName, String contactPhone, String serviceTel,
			String contactEmail, Long operaterId, Long merchantId) {
		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result = false;
		// TODO 权限校验
		try {
			Account a = accountService.queryAccountById(operaterId);
			RetMessage message = checkAccount.apply(a, OperateType.inputMerchantInfo);
			if (notNull.test(message))
				return message;
			Merchant m = merchantService.queryMerchantParticularsById(merchantId);
			message = checkMerchant.apply(m, OperateType.inputMerchantInfo);
			if (notNull.test(message))
				return message;
			Merchant merchant = new Merchant();
			merchant.setMerchantName(merchantName);
			merchant.setWorkAddress(workAddress);
			merchant.setMinuteWorkAddress(minuteWorkAddress);
			merchant.setServeExplain(serviceDistription);
			merchant.setCharterCode(businessLicenseNum);
			merchant.setOraganizingCode(organizingInstitutionBarCode);
			merchant.setPrincipal(enterpriseLegalRepresentativeName);
			merchant.setLinkMan(contactsName);
			merchant.setLinkManPhone(contactPhone);
			merchant.setServiceTel(serviceTel);
			merchant.setEmail(contactEmail);
			merchant.setMerchantId(merchantId);
			result = merchantService.fillInInformation(merchant);
			if (result) {
				retMessage.setRetCode(RetCodeEnum.success.getValue());
				retMessage.setRetMessage("商户详细信息修改成功");
				OperateRecord record = new OperateRecord();
				record.setRecordId(IdWorker.getId());
				record.setOperaterId(operaterId);
				record.setAccountType(a.getAccountType());
				record.setPhone(a.getPhone());
				record.setName(a.getUserName());
				record.setTargetType(OperateTargetType.merchant);
				record.setType(OperateType.modifyMerchant);
				record.setOperateTime(TimeAssist.getNow());
				record.setOperateResult("success");
				record.setDescription("商户详细信息修改[" + merchantId + "]");
				operateRecordService.addRecord(record);
			} else {
				retMessage.setRetCode(RetCodeEnum.exception.getValue());
				retMessage.setRetMessage("商户详细信息修改失败");
			}
			retMessage.setDetail(result);

		} catch (Exception e) {
			LogFactory.error(this, "商户详细信息修改时发生异常", e);
			retMessage.setAll(RetCodeEnum.error, "商户详细信息修改时发生异常", result);
		}
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> organizingInstitutionBarCodePicUpLoad(File organizingInstitutionBarCodePicFile,
			Long operaterId, Long merchantId) {
		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result = false;
		Merchant merchant = null;
		try {
			Account a = accountService.queryAccountById(operaterId);
			RetMessage message = checkAccount.apply(a, OperateType.inputMerchantInfo);
			if (notNull.test(message))
				return message;
			Merchant m = merchantService.queryMerchantParticularsById(merchantId);
			message = checkMerchant.apply(m, OperateType.inputMerchantInfo);
			if (notNull.test(message))
				return message;

			merchant = new Merchant();
			merchant.setMerchantId(merchantId);
			result = merchantService.organizingInstitutionBarCodePicUpLoad(merchant,
					organizingInstitutionBarCodePicFile);
			if (result) {
				retMessage.setAll(RetCodeEnum.success, "上传商户组织机构代码证成功", result);
				OperateRecord record = new OperateRecord();
				record.setRecordId(IdWorker.getId());
				record.setOperaterId(operaterId);
				record.setAccountType(a.getAccountType());
				record.setPhone(a.getPhone());
				record.setName(a.getUserName());
				record.setTargetType(OperateTargetType.merchant);
				record.setType(OperateType.inputMerchantInfo);
				record.setOperateTime(TimeAssist.getNow());
				record.setOperateResult("success");
				record.setDescription("商户详细信息提交[" + merchantId + "]");
				operateRecordService.addRecord(record);
			} else {
				retMessage.setAll(RetCodeEnum.exception, "上传商户组织机构代码证失败", result);
			}

		} catch (Exception e) {
			retMessage.setAll(RetCodeEnum.error, "上传商户组织机构代码证时发生异常", result);
		}
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> businessLicensePicUpLoad(File businessLicensePicFile, Long operaterId, Long merchantId) {
		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result = false;
		Merchant merchant = null;
		try {
			Account a = accountService.queryAccountById(operaterId);
			RetMessage message = checkAccount.apply(a, OperateType.inputMerchantInfo);
			if (notNull.test(message))
				return message;
			Merchant m = merchantService.queryMerchantParticularsById(merchantId);
			message = checkMerchant.apply(m, OperateType.inputMerchantInfo);
			if (notNull.test(message))
				return message;

			merchant = new Merchant();
			merchant.setMerchantId(merchantId);
			result = merchantService.businessLicensePicUpLoad(merchant, businessLicensePicFile);
			if (result) {
				retMessage.setAll(RetCodeEnum.success, "上传商户营业执照成功", result);
				OperateRecord record = new OperateRecord();
				record.setRecordId(IdWorker.getId());
				record.setOperaterId(operaterId);
				record.setAccountType(a.getAccountType());
				record.setPhone(a.getPhone());
				record.setName(a.getUserName());
				record.setTargetType(OperateTargetType.merchant);
				record.setType(OperateType.inputMerchantInfo);
				record.setOperateTime(TimeAssist.getNow());
				record.setOperateResult("success");
				record.setDescription("商户详细信息提交[" + merchantId + "]");
				operateRecordService.addRecord(record);
			} else {
				retMessage.setAll(RetCodeEnum.exception, "上传商户营业执照失败", result);
			}

		} catch (Exception e) {
			retMessage.setAll(RetCodeEnum.error, "上传商户营业执照时发生异常", result);
		}
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> otherPicUpLoad(File otherPicFile, Long operaterId, Long merchantId) {
		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result = false;
		Merchant merchant = null;
		try {
			Account a = accountService.queryAccountById(operaterId);
			RetMessage message = checkAccount.apply(a, OperateType.inputMerchantInfo);
			if (notNull.test(message))
				return message;
			Merchant m = merchantService.queryMerchantParticularsById(merchantId);
			message = checkMerchant.apply(m, OperateType.inputMerchantInfo);
			if (notNull.test(message))
				return message;
			merchant = new Merchant();
			merchant.setMerchantId(merchantId);
			result = merchantService.otherPicUpLoad(merchant, otherPicFile);
			if (result) {
				retMessage.setAll(RetCodeEnum.success, "上传其他证明资料成功", result);
				OperateRecord record = new OperateRecord();
				record.setRecordId(IdWorker.getId());
				record.setOperaterId(operaterId);
				record.setAccountType(a.getAccountType());
				record.setPhone(a.getPhone());
				record.setName(a.getUserName());
				record.setTargetType(OperateTargetType.merchant);
				record.setType(OperateType.inputMerchantInfo);
				record.setOperateTime(TimeAssist.getNow());
				record.setOperateResult("success");
				record.setDescription("商户详细信息提交[" + merchantId + "]");
				operateRecordService.addRecord(record);
			} else {
				retMessage.setAll(RetCodeEnum.exception, "上传其他证明资料失败", result);
			}

		} catch (Exception e) {
			retMessage.setAll(RetCodeEnum.error, "上传其他证明资料时发生异常", result);
		}
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> modifyOrganizingInstitutionBarCodePic(File organizingInstitutionBarCodePicFile,
			Long operaterId, Long merchantId) {
		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result = false;
		Merchant merchant = null;
		try {
			Account a = accountService.queryAccountById(operaterId);
			RetMessage message = checkAccount.apply(a, OperateType.inputMerchantInfo);
			if (notNull.test(message))
				return message;
			Merchant m = merchantService.queryMerchantParticularsById(merchantId);
			message = checkMerchant.apply(m, OperateType.inputMerchantInfo);
			if (notNull.test(message))
				return message;

			merchant = new Merchant();
			merchant.setMerchantId(merchantId);
			result = merchantService.organizingInstitutionBarCodePicUpLoad(merchant,
					organizingInstitutionBarCodePicFile);
			if (result) {
				retMessage.setAll(RetCodeEnum.success, "修改商户组织机构代码证成功", result);
				OperateRecord record = new OperateRecord();
				record.setRecordId(IdWorker.getId());
				record.setOperaterId(operaterId);
				record.setAccountType(a.getAccountType());
				record.setPhone(a.getPhone());
				record.setName(a.getUserName());
				record.setTargetType(OperateTargetType.merchant);
				record.setType(OperateType.inputMerchantInfo);
				record.setOperateTime(TimeAssist.getNow());
				record.setOperateResult("success");
				record.setDescription("修改商户组织机构代码证[" + merchantId + "]");
				operateRecordService.addRecord(record);
			} else {
				retMessage.setAll(RetCodeEnum.exception, "修改商户组织机构代码证失败", result);
			}

		} catch (Exception e) {
			retMessage.setAll(RetCodeEnum.error, "修改商户组织机构代码证时发生异常", result);
		}
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> modifyBusinessLicensePic(File businessLicensePicFile, Long operaterId, Long merchantId) {
		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result = false;
		Merchant merchant = null;
		try {
			Account a = accountService.queryAccountById(operaterId);
			RetMessage message = checkAccount.apply(a, OperateType.inputMerchantInfo);
			if (notNull.test(message))
				return message;
			Merchant m = merchantService.queryMerchantParticularsById(merchantId);
			message = checkMerchant.apply(m, OperateType.inputMerchantInfo);
			if (notNull.test(message))
				return message;

			merchant = new Merchant();
			merchant.setMerchantId(merchantId);
			result = merchantService.businessLicensePicUpLoad(merchant, businessLicensePicFile);
			if (result) {
				retMessage.setAll(RetCodeEnum.success, "修改商户营业执照成功", result);
				OperateRecord record = new OperateRecord();
				record.setRecordId(IdWorker.getId());
				record.setOperaterId(operaterId);
				record.setAccountType(a.getAccountType());
				record.setPhone(a.getPhone());
				record.setName(a.getUserName());
				record.setTargetType(OperateTargetType.merchant);
				record.setType(OperateType.inputMerchantInfo);
				record.setOperateTime(TimeAssist.getNow());
				record.setOperateResult("success");
				record.setDescription("修改商户营业执照[" + merchantId + "]");
				operateRecordService.addRecord(record);
			} else {
				retMessage.setAll(RetCodeEnum.exception, "修改商户营业执照失败", result);
			}
		} catch (Exception e) {
			retMessage.setAll(RetCodeEnum.error, "修改商户营业执照时发生异常", result);
		}
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> modifyOtherPic(File otherPicFile, Long operaterId, Long merchantId) {
		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result = false;
		Merchant merchant = null;
		try {
			Account a = accountService.queryAccountById(operaterId);
			RetMessage message = checkAccount.apply(a, OperateType.inputMerchantInfo);
			if (notNull.test(message))
				return message;
			Merchant m = merchantService.queryMerchantParticularsById(merchantId);
			message = checkMerchant.apply(m, OperateType.inputMerchantInfo);
			if (notNull.test(message))
				return message;
			merchant = new Merchant();
			merchant.setMerchantId(merchantId);
			result = merchantService.otherPicUpLoad(merchant, otherPicFile);
			if (result) {
				retMessage.setAll(RetCodeEnum.success, "修改其他证明资料成功", result);
				OperateRecord record = new OperateRecord();
				record.setRecordId(IdWorker.getId());
				record.setOperaterId(operaterId);
				record.setAccountType(a.getAccountType());
				record.setPhone(a.getPhone());
				record.setName(a.getUserName());
				record.setTargetType(OperateTargetType.merchant);
				record.setType(OperateType.inputMerchantInfo);
				record.setOperateTime(TimeAssist.getNow());
				record.setOperateResult("success");
				record.setDescription("修改其他证明资料[" + merchantId + "]");
				operateRecordService.addRecord(record);
			} else {
				retMessage.setAll(RetCodeEnum.exception, "修改其他证明资料失败", result);
			}
		} catch (Exception e) {
			retMessage.setAll(RetCodeEnum.error, "修改其他证明资料时发生异常", result);
		}
		return retMessage;
	}

	@Override
	public RetMessage<String> queryMerchantInfo(Long operaterId, Long merchantId) {
		try {
			Account a=accountService.queryAccountById(operaterId);
			RetMessage message=checkAccount.apply(a, OperateType.queryMerchantInfo);
			if(notNull.test(message))
				return message;
			Merchant merchant=merchantService.queryMerchantParticularsById(merchantId);
			if (merchant!=null) {
				String result=merchant.toJson();
				return new RetMessage<String>(RetCodeEnum.success,"查询商户详情成功",result);
			}
			else
				return new RetMessage<String>(RetCodeEnum.success,"查询商户详情失败",null);
		} catch (Exception e) {
			LogFactory.error(this,"查询商户详情时发生异常",e);
			return new RetMessage<String>(RetCodeEnum.error,"查询商户详情时发生异常",null);
		}
	
	}
}
