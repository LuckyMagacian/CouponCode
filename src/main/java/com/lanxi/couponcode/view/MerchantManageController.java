package com.lanxi.couponcode.view;

import com.lanxi.couponcode.spi.consts.annotations.SetUtf8;

import com.lanxi.couponcode.spi.consts.enums.*;
import com.lanxi.couponcode.spi.service.*;
import com.lanxi.util.entity.LogFactory;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static com.lanxi.couponcode.spi.assist.ArgAssist.*;

/**
 * 商户管理端 Created by yangyuanjian on 2017/11/20.
 */
@Controller
@RequestMapping("merchantManager")
public class MerchantManageController {
	@Resource
	private MerchantService merchantService;
	@Resource
	private CouponService codeService;
	@Resource
	private ClearService clearService;
	@Resource
	private ShopService shopService;
	@Resource
	private OperateRecordService operateRecordService;
	@Resource
	private RequestService requestService;
	@Resource
	private VerificationRecordService verificationRecordService;
	@Resource
	private AccountService accountService;

	/* 完善商户详细信息 */
	@SetUtf8
	@RequestMapping(value = "inputMerchantInfo", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String inputMerchantInfo(HttpServletRequest req, HttpServletResponse res) {
		String merchantName = getArg.apply(req, "merchantName");
		String serveExplain = getArg.apply(req, "serveExplain");
		String workAddress = getArg.apply(req, "workAddress");
		String minuteWorkAddress = getArg.apply(req, "minuteWorkAddress");
		String charterCode = getArg.apply(req, "charterCode");
		String oraganizingCode = getArg.apply(req, "oraganizingCode");
		String principal = getArg.apply(req, "principal");
		String linkMan = getArg.apply(req, "linkMan");
		String linkManPhone = getArg.apply(req, "linkManPhone");
		String serviceTel = getArg.apply(req, "serviceTel");
		String email = getArg.apply(req, "email");
		String operaterIdStr = getArg.apply(req, "operaterId");
		String merchantIdStr = getArg.apply(req, "merchantId");
		Long operaterId = parseArg(operaterIdStr, Long.class);
		Long merchantId = parseArg(merchantIdStr, Long.class);
		return merchantService
				.inputMerchantInfo(merchantName, serveExplain, workAddress, minuteWorkAddress, charterCode,
						oraganizingCode, principal, linkMan, linkManPhone, serviceTel, email, operaterId, merchantId)
				.toJson();
	}

	/* 完善商户组织机构代码证 */
	@SetUtf8
	@RequestMapping(value = "organizingInstitutionBarCodePicUpLoad", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String organizingInstitutionBarCodePicUpLoad(HttpServletRequest req, HttpServletResponse res) {
		String operaterIdStr = getArg.apply(req, "operaterId");
		String merchantIdStr = getArg.apply(req, "merchantId");
		Long operaterId = parseArg(operaterIdStr, Long.class);
		Long merchantId = parseArg(merchantIdStr, Long.class);
		String result = null;
		try {
			File file = new File("file");
			InputStream is = req.getInputStream();
			OutputStream os = new FileOutputStream(file);
			byte temp[] = new byte[1024];
			int size = -1;
			while ((size = is.read(temp)) != -1) {
				os.write(temp, 0, size);
			}
			os.close();
			result = merchantService.organizingInstitutionBarCodePicUpLoad(file, operaterId, merchantId).toJson();
		} catch (Exception e) {
			LogFactory.error(this, "上传组织机构代码证时发生异常");

		}
		return result;
	}

	/* 完善商户营业执照 */
	@SetUtf8
	@RequestMapping(value = "businessLicensePicUpLoad", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String businessLicensePicUpLoad(HttpServletRequest req, HttpServletResponse res) {
		String operaterIdStr = getArg.apply(req, "operaterId");
		String merchantIdStr = getArg.apply(req, "merchantId");
		Long operaterId = parseArg(operaterIdStr, Long.class);
		Long merchantId = parseArg(merchantIdStr, Long.class);
		String result = null;
		try {
			File file = new File("file");
			InputStream is = req.getInputStream();
			OutputStream os = new FileOutputStream(file);
			byte temp[] = new byte[1024];
			int size = -1;
			while ((size = is.read(temp)) != -1) {
				os.write(temp, 0, size);
			}
			os.close();
			result = merchantService.businessLicensePicUpLoad(file, operaterId, merchantId).toJson();
		} catch (Exception e) {
			LogFactory.error(this, "上传组织机构代码证时发生异常");

		}
		return result;
	}

	/* 完善商户其他证明资料 */
	@SetUtf8
	@RequestMapping(value = "otherPicUpLoad", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String otherPicUpLoad(HttpServletRequest req, HttpServletResponse res) {
		String operaterIdStr = getArg.apply(req, "operaterId");
		String merchantIdStr = getArg.apply(req, "merchantId");
		Long operaterId = parseArg(operaterIdStr, Long.class);
		Long merchantId = parseArg(merchantIdStr, Long.class);
		String result = null;
		try {
			File file = new File("file");
			InputStream is = req.getInputStream();
			OutputStream os = new FileOutputStream(file);
			byte temp[] = new byte[1024];
			int size = -1;
			while ((size = is.read(temp)) != -1) {
				os.write(temp, 0, size);
			}
			os.close();
			result = merchantService.otherPicUpLoad(file, operaterId, merchantId).toJson();
		} catch (Exception e) {
			LogFactory.error(this, "上传组织机构代码证时发生异常");

		}
		return result;
	}

	/* 修改商户详细信息 */
	@SetUtf8
	@RequestMapping(value = "modifyMerchantInfo", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String modifyMerchantInfo(HttpServletRequest req, HttpServletResponse res) {
		String merchantName = getArg.apply(req, "merchantName");
		String serveExplain = getArg.apply(req, "serveExplain");
		String workAddress = getArg.apply(req, "workAddress");
		String minuteWorkAddress = getArg.apply(req, "minuteWorkAddress");
		String charterCode = getArg.apply(req, "charterCode");
		String oraganizingCode = getArg.apply(req, "oraganizingCode");
		String principal = getArg.apply(req, "principal");
		String linkMan = getArg.apply(req, "linkMan");
		String linkManPhone = getArg.apply(req, "linkManPhone");
		String serviceTel = getArg.apply(req, "serviceTel");
		String email = getArg.apply(req, "email");
		String operaterIdStr = getArg.apply(req, "operaterId");
		String merchantIdStr = getArg.apply(req, "merchantId");
		Long operaterId = parseArg(operaterIdStr, Long.class);
		Long merchantId = parseArg(merchantIdStr, Long.class);
		return merchantService
				.modifyMerchantInfo(merchantName, serveExplain, workAddress, minuteWorkAddress, charterCode,
						oraganizingCode, principal, linkMan, linkManPhone, serviceTel, email, operaterId, merchantId)
				.toJson();
	}

	/* 修改商户组织机构代码证 */
	@SetUtf8
	@RequestMapping(value = "modifyOrganizingInstitutionBarCodePic", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String modifyOrganizingInstitutionBarCodePic(HttpServletRequest req, HttpServletResponse res) {
		String operaterIdStr = getArg.apply(req, "operaterId");
		String merchantIdStr = getArg.apply(req, "merchantId");
		Long operaterId = parseArg(operaterIdStr, Long.class);
		Long merchantId = parseArg(merchantIdStr, Long.class);
		String result = null;
		try {
			File file = new File("file");
			InputStream is = req.getInputStream();
			OutputStream os = new FileOutputStream(file);
			byte temp[] = new byte[1024];
			int size = -1;
			while ((size = is.read(temp)) != -1) {
				os.write(temp, 0, size);
			}
			os.close();
			result = merchantService.modifyOrganizingInstitutionBarCodePic(file, operaterId, merchantId).toJson();
		} catch (Exception e) {
			LogFactory.error(this, "上传组织机构代码证时发生异常");

		}
		return result;
	}

	/* 修改商户营业执照 */
	@SetUtf8
	@RequestMapping(value = "modifyBusinessLicensePic", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String modifyBusinessLicensePic(HttpServletRequest req, HttpServletResponse res) {
		String operaterIdStr = getArg.apply(req, "operaterId");
		String merchantIdStr = getArg.apply(req, "merchantId");
		Long operaterId = parseArg(operaterIdStr, Long.class);
		Long merchantId = parseArg(merchantIdStr, Long.class);
		String result = null;
		try {
			File file = new File("file");
			InputStream is = req.getInputStream();
			OutputStream os = new FileOutputStream(file);
			byte temp[] = new byte[1024];
			int size = -1;
			while ((size = is.read(temp)) != -1) {
				os.write(temp, 0, size);
			}
			os.close();
			result = merchantService.modifyBusinessLicensePic(file, operaterId, merchantId).toJson();
		} catch (Exception e) {
			LogFactory.error(this, "上传组织机构代码证时发生异常");

		}
		return result;
	}

	/* 修改商户其他证明资料 */
	@SetUtf8
	@RequestMapping(value = "modifyOtherPic", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String modifyOtherPic(HttpServletRequest req, HttpServletResponse res) {
		String operaterIdStr = getArg.apply(req, "operaterId");
		String merchantIdStr = getArg.apply(req, "merchantId");
		Long operaterId = parseArg(operaterIdStr, Long.class);
		Long merchantId = parseArg(merchantIdStr, Long.class);
		String result = null;
		try {
			File file = new File("file");
			InputStream is = req.getInputStream();
			OutputStream os = new FileOutputStream(file);
			byte temp[] = new byte[1024];
			int size = -1;
			while ((size = is.read(temp)) != -1) {
				os.write(temp, 0, size);
			}
			os.close();
			result = merchantService.modifyOtherPic(file, operaterId, merchantId).toJson();
		} catch (Exception e) {
			LogFactory.error(this, "上传组织机构代码证时发生异常");

		}
		return result;
	}

	/* 冻结门店 */
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "freezeShop", produces = "application/json;charset=utf-8")
	public String freezeShop(HttpServletRequest req, HttpServletResponse res) {
		String shopIdStr = getArg.apply(req, "shopId");
		String operaterIdStr = getArg.apply(req, "operaterId");
		Long operaterId = toLongArg.apply(operaterIdStr);
		Long shopId = toLongArg.apply(shopIdStr);
		return shopService.freezeShop(shopId, operaterId).toJson();
	}

	/* 开启门店 */
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "unfreezeShop", produces = "application/json;charset=utf-8")
	public String unfreezeShop(HttpServletRequest req, HttpServletResponse res) {
		String shopIdStr = getArg.apply(req, "shopId");
		String operaterIdStr = getArg.apply(req, "operaterId");
		Long operaterId = toLongArg.apply(operaterIdStr);
		Long shopId = toLongArg.apply(shopIdStr);
		return shopService.unfreezeShop(shopId, operaterId).toJson();
	}

	/* 修改门店 */
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "modifyShop", produces = "application/json;charset=utf-8")
	public String modifyShop(HttpServletRequest req, HttpServletResponse res) {
		String shopName = getArg.apply(req, "shopName");
		String shopAddress = getArg.apply(req, "shopAddress");
		String minuteShopAddress = getArg.apply(req, "minuteShopAddress");
		String serviceTel = getArg.apply(req, "serviceTel");
		String operaterIdStr = getArg.apply(req, "operaterId");
		String merchantIdStr = getArg.apply(req, "merchantId");
		Long operaterId = toLongArg.apply(operaterIdStr);
		Long merchantId = toLongArg.apply(merchantIdStr);
		return shopService.modifyShop(shopName, shopAddress, minuteShopAddress, serviceTel, merchantId, operaterId)
				.toJson();
	}

	/* 查询门店 */
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "queryShop", produces = "application/json;charset=utf-8")
	public String queryShop(HttpServletRequest req, HttpServletResponse res) {
		String merchantName = getArg.apply(req, "merchantName");
		String shopName = getArg.apply(req, "shopName");
		String shopAddress = getArg.apply(req, "shopAddress");
		String shopStatusStr = getArg.apply(req, "shopStatus");
		String pageNumStr = getArg.apply(req, "pageNum");
		String pageSizeStr = getArg.apply(req, "pageSize");
		String operaterIdStr = getArg.apply(req, "operaterId");
		ShopStatus shopStatus = ShopStatus.getType(shopStatusStr);
		Integer pageNum = toIntArg.apply(pageNumStr);
		Integer pageSize = toIntArg.apply(pageSizeStr);
		Long operaterId = toLongArg.apply(operaterIdStr);
		return shopService
				.queryShop(merchantName, shopName, shopStatus, shopAddress, pageNum, pageSize, operaterId)
				.toJson();

	}

	/* 导出 */
	@ResponseBody
	@RequestMapping(value = "queryShopsExport")
	public void queryShopsExport(HttpServletRequest req, HttpServletResponse res) {
		String shopName = getArg.apply(req, "shopName");
		String shopAddress = getArg.apply(req, "shopAddress");
		String shopStatusStr = getArg.apply(req, "shopStatus");
		String pageNumStr = getArg.apply(req, "pageNum");
		String pageSizeStr = getArg.apply(req, "pageSize");
		String operaterIdStr = getArg.apply(req, "operaterId");
		String merchantIdStr = getArg.apply(req, "merchantId");
		ShopStatus shopStatus = ShopStatus.getType(shopStatusStr);
		Integer pageNum = toIntArg.apply(pageNumStr);
		Integer pageSize = toIntArg.apply(pageSizeStr);
		Long operaterId = toLongArg.apply(operaterIdStr);
		Long merchantId = toLongArg.apply(merchantIdStr);
		File file = (File) shopService
				.queryShopsExport(shopName, shopAddress, shopStatus, pageNum, pageSize, merchantId, operaterId)
				.getDetail();
		try {
			if (file != null) {
				InputStream is = new FileInputStream(file);
				OutputStream os = res.getOutputStream();
				byte temp[] = new byte[1024];
				int size = -1;
				while ((size = is.read(temp)) != -1) {
					os.write(temp, 0, size);
				}
				is.close();
				os.close();
			}
		} catch (Exception e) {
			LogFactory.error(this, "导出Excel文件时发生异常", e);
		}
	}

	/* 商户负责人添加门店管理员和核销员账户 */
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "addAccount", produces = "application/json;charset=utf-8")
	public String addAccount(HttpServletRequest req, HttpServletResponse res) {
		String userName = getArg.apply(req, "userName");
		String phone = getArg.apply(req, "phone");
		String operaterIdStr = getArg.apply(req, "operaterId");
		String typeStr = getArg.apply(req, "type");
		String shopIdStr = getArg.apply(req, "shopId");
		String shopName = getArg.apply(req, "shopName");
		Long shopId = parseArg(shopIdStr, Long.class);
		Long operaterId = parseArg(operaterIdStr, Long.class);
		AccountType type = AccountType.getType(typeStr);
		return accountService.merchantAddAccount(type, userName, phone, shopName, shopId, operaterId).toJson();
	}

	/* 商户管理员查询账户 */
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "queryAccounts", produces = "application/json;charset=utf-8")
	public String queryAccounts(HttpServletRequest req, HttpServletResponse res) {
		String phone = getArg.apply(req, "phone");
		String typeStr = getArg.apply(req, "type");
		AccountType type = AccountType.getType(typeStr);
		String operaterIdStr = getArg.apply(req, "operaterId");
		Long operaterId = parseArg(operaterIdStr, Long.class);
		String statusStr = getArg.apply(req, "status");
		AccountStatus status = AccountStatus.getType(statusStr);
		String pageNumStr = getArg.apply(req, "pageNum");
		String pageSizeStr = getArg.apply(req, "pageSize");
		Integer pageNum = parseArg(pageNumStr, Integer.class);
		Integer pageSize = parseArg(pageSizeStr, Integer.class);
		String shopName = getArg.apply(req, "shopName");
		return accountService.merchantQueryAccounts(phone, shopName, type, status, pageNum, pageSize, operaterId)
				.toJson();
	}

	/* 冻结账户 */
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "freezeAccount", produces = "application/json;charset=utf-8")
	public String freezeAccount(HttpServletRequest req, HttpServletResponse res) {
		String operaterIdStr = getArg.apply(req, "operaterId");
		Long operaterId = parseArg(operaterIdStr, Long.class);
		String accountIdStr = getArg.apply(req, "accountId");
		Long accountId = parseArg(accountIdStr, Long.class);
		return accountService.freezeAccount(accountId, operaterId).toJson();
	}

	/* 开启账户 */
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "unfreezeAccount", produces = "application/json;charset=utf-8")
	public String unfreezeAccount(HttpServletRequest req, HttpServletResponse res) {
		String operaterIdStr = getArg.apply(req, "operaterId");
		Long operaterId = parseArg(operaterIdStr, Long.class);
		String accountIdStr = getArg.apply(req, "accountId");
		Long accountId = parseArg(accountIdStr, Long.class);
		return accountService.unfreezeAccount(accountId, operaterId).toJson();
	}

	/* 删除账户 */
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "delAccount", produces = "application/json;charset=utf-8")
	public String deleteAccount(HttpServletRequest req, HttpServletResponse res) {
		String operaterIdStr = getArg.apply(req, "operaterId");
		Long operaterId = parseArg(operaterIdStr, Long.class);
		String accountIdStr = getArg.apply(req, "accountId");
		Long accountId = parseArg(accountIdStr, Long.class);
		return accountService.delAccount(accountId, operaterId).toJson();
	}

	// -------------------------------------------------------------------------------------------------------------------------------
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "destroyCode", produces = "application/json;charset=utf-8")
	public String destroyCode(HttpServletRequest req, HttpServletResponse res) {
		String codIdStr = getArg.apply(req, "codeId");
		String operaterIdStr = getArg.apply(req, "operaterId");
		String codeStr = getArg.apply(req, "code");

		Long codeId = parseArg(codIdStr, Long.class);
		Long operaterId = parseArg(operaterIdStr, Long.class);
		Long code = parseArg(codeStr, Long.class);

		if (codeId == null)
			return codeService.destroyCode(code, null, operaterId).toJson();
		else
			return codeService.destroyCode(codeId, operaterId).toJson();
	}

	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "postoneCode", produces = "application/json;charset=utf-8")
	public String postoneCode(HttpServletRequest req, HttpServletResponse res) {
		String codIdStr = getArg.apply(req, "codeId");
		String operaterIdStr = getArg.apply(req, "operaterId");
		Long codeId = parseArg(codIdStr, Long.class);
		Long operaterId = parseArg(operaterIdStr, Long.class);
		return codeService.postoneCode(codeId, operaterId).toJson();
	}

	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "queryCode", produces = "application/json;charset=utf-8")
	public String queryCode(HttpServletRequest req, HttpServletResponse res) {
		String codIdStr = getArg.apply(req, "codeId");
		String operaterIdStr = getArg.apply(req, "operaterId");
		String codeStr = getArg.apply(req, "code");
		Long codeId = parseArg(codIdStr, Long.class);
		Long operaterId = parseArg(operaterIdStr, Long.class);
		Long code = parseArg(codeStr, Long.class);
		if (codeId == null)
			return codeService.couponCodeInfo(code, null, operaterId).toJson();
		else
			return codeService.couponCodeInfo(codeId, operaterId).toJson();
	}

	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "queryDailyRecords", produces = "application/json;charset=utf-8")
	public String queryDailyRecords(HttpServletRequest req, HttpServletResponse res) {
		String timeStart = getArg.apply(req, "timeStart");
		String timeEnd = getArg.apply(req, "timeStop");
		String clearStatusStr = getArg.apply(req, "clearStatus");
		String pageNumStr = getArg.apply(req, "pageNum");
		String pageSizeStr = getArg.apply(req, "pageSize");
		String operaterIdStr = getArg.apply(req, "operaterId");

		ClearStatus clearStatus = ClearStatus.getType(clearStatusStr);
		Long operaterId = parseArg(operaterIdStr, Long.class);
		Integer pageNum = parseArg(pageNumStr, Integer.class);
		Integer pageSize = parseArg(pageSizeStr, Integer.class);

		return clearService.queryDailyRecords(timeStart, timeEnd, clearStatus, pageNum, pageSize, operaterId).toJson();
	}

	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "queryDailyRecord", produces = "application/json;charset=utf-8")
	public String queryDailyRecord(HttpServletRequest req, HttpServletResponse res) {
		String recordIdStr = getArg.apply(req, "recordId");
		String operaterIdStr = getArg.apply(req, "operaterId");
		Long recordId = parseArg(recordIdStr, Long.class);
		Long operaterId = parseArg(operaterIdStr, Long.class);
		return clearService.queryDailyRecordInfo(recordId, operaterId).toJson();
	}

	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "queryClearRecords", produces = "application/json;charset=utf-8")
	public String queryClearRecords(HttpServletRequest req, HttpServletResponse res) {
		String timeStart = getArg.apply(req, "timeStart");
		String timeEnd = getArg.apply(req, "timeStop");
		String clearStatusStr = getArg.apply(req, "clearStatus");
		String invoiceStatusStr = getArg.apply(req, "invoiceStatus");
		String pageNumStr = getArg.apply(req, "pageNum");
		String pageSizeStr = getArg.apply(req, "pageSize");
		String operaterIdStr = getArg.apply(req, "operaterId");

		ClearStatus clearStatus = ClearStatus.getType(clearStatusStr);
		Long operaterId = parseArg(operaterIdStr, Long.class);
		Integer pageNum = parseArg(pageNumStr, Integer.class);
		Integer pageSize = parseArg(pageSizeStr, Integer.class);
		InvoiceStatus invoiceStatus = InvoiceStatus.getType(invoiceStatusStr);
		return clearService
				.queryClearRecords(timeStart, timeEnd, clearStatus, invoiceStatus, pageNum, pageSize, operaterId)
				.toJson();
	}

	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "queryClearRecord", produces = "application/json;charset=utf-8")
	public String queryClearRecord(HttpServletRequest req, HttpServletResponse res) {
		String recordIdStr = getArg.apply(req, "recordId");
		String operaterIdStr = getArg.apply(req, "operaterId");
		Long recordId = parseArg(recordIdStr, Long.class);
		Long operaterId = parseArg(operaterIdStr, Long.class);
		return clearService.queryRecordInfo(recordId, operaterId).toJson();
	}

	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "queryOperateRecords", produces = "application/json;charset=utf-8")
	public String queryOperateRecords(HttpServletRequest req, HttpServletResponse res) {
		String typeStr = getArg.apply(req, "type");
		String targetTypeStr = getArg.apply(req, "targetType");
		String accountTypeStr = getArg.apply(req, "accountType");
		String shopName = getArg.apply(req, "shopName");
		String timeStart = getArg.apply(req, "timeStart");
		String timeEnd = getArg.apply(req, "timeStop");
		String name = getArg.apply(req, "name");
		String phone = getArg.apply(req, "phone");
		String pageNumStr = getArg.apply(req, "pageNum");
		String pageSizeStr = getArg.apply(req, "pageSize");
		String operaterIdStr = getArg.apply(req, "operaterId");

		OperateType type = OperateType.getType(typeStr);
		OperateTargetType targetType = OperateTargetType.getType(targetTypeStr);
		AccountType accountType = AccountType.getType(accountTypeStr);
		Long operaterId = parseArg(operaterIdStr, Long.class);
		Integer pageNum = parseArg(pageNumStr, Integer.class);
		Integer pageSize = parseArg(pageSizeStr, Integer.class);
		return operateRecordService.queryMerchantOperateRecord(type, targetType, shopName, timeStart, timeEnd,
				accountType, name, phone, pageNum, pageSize, operaterId).toJson();
	}

	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "queryOperateRecord", produces = "application/json;charset=utf-8")
	public String queryOperateRecord(HttpServletRequest req, HttpServletResponse res) {
		String recordIdStr = getArg.apply(req, "recordId");
		String operaterIdStr = getArg.apply(req, "operaterId");
		Long recordId = parseArg(recordIdStr, Long.class);
		Long operaterId = parseArg(operaterIdStr, Long.class);
		return operateRecordService.queryOperateRecordInfo(recordId, operaterId).toJson();
	}


	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "addShop", produces = "application/json;charset=utf-8")
	public String addShop(HttpServletRequest req, HttpServletResponse res) {
		String shopName = getArg.apply(req, "shopName");
		String shopAddress = getArg.apply(req, "shopAddress");
		String minuteShopAddress = getArg.apply(req, "minuteShopAddress");
		String serviceTel = getArg.apply(req, "serviceTel");
		String operaterIdStr = getArg.apply(req, "operaterId");
		String merchantIdStr = getArg.apply(req, "merchantId");
		Long operaterId = toLongArg.apply(operaterIdStr);
		Long merchantId = toLongArg.apply(merchantIdStr);
		return shopService.addShop(shopName, shopAddress, minuteShopAddress, serviceTel, merchantId, operaterId)
				.toJson();
	}

	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "importShops", produces = "application/json;charset=utf-8")
	public String importShops(HttpServletRequest req, HttpServletResponse res) {
		String operaterIdStr = getArg.apply(req, "operaterId");
		String merchantIdStr = getArg.apply(req, "merchantId");
		Long operaterId = toLongArg.apply(operaterIdStr);
		Long merchantId = toLongArg.apply(merchantIdStr);
		String result = null;
		try {
			InputStream is = req.getInputStream();
			File file = new File("file");
			OutputStream os = new FileOutputStream(file);
			byte temp[] = new byte[1024];
			int size = -1;
			while ((size = is.read(temp)) != -1) {
				os.write(temp, 0, size);
			}
			os.close();
			result = shopService.importShops(file, merchantId, operaterId).toJson();
		} catch (Exception e) {
			LogFactory.error(this, "批量导入门店的时候发生异常", e);
		}
		return result;
	}
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "queryVerifyRecords", produces = "application/json;charset=utf-8")
	public String queryVerifyRecords(final HttpServletRequest req, final HttpServletResponse res){
		String codeStr = getArg.apply(req, "code");
		String timeStart = getArg.apply(req, "timeStart");
		String timeEnd = getArg.apply(req, "timeStop");
		String shopName = getArg.apply(req, "shopName");
		String phone = getArg.apply(req, "phone");
		String verificationTypeString = getArg.apply(req, "verificationType");
		String pageNumStr = getArg.apply(req, "pageNum");
		String pageSizeStr = getArg.apply(req, "pageSize");
		String operaterIdStr = getArg.apply(req, "operaterId");
        String commodityName=getArg.apply(req,"commodityName");

		Long code = toLongArg.apply(codeStr);
		VerificationType type = toVerificationType.apply(verificationTypeString);
		Integer pageNum = toIntArg.apply(pageNumStr);
		Integer pageSize = toIntArg.apply(pageSizeStr);
        Long operaterId = toLongArg.apply(operaterIdStr);
		return verificationRecordService.queryShopVerificationRecords(timeStart,timeEnd,shopName,code,commodityName,phone,type,pageNum,pageSize,operaterId).toJson();
	}

    @SetUtf8
    @ResponseBody
    @RequestMapping(value = "queryVerifyRecord", produces = "application/json;charset=utf-8")
    public String queryVerifyRecord(final HttpServletRequest req, final HttpServletResponse res){
        String recordIdStr=getArg.apply(req,"recordId");
        String operaterIdStr=getArg.apply(req,"operaterId");
        Long recordId=parseArg(recordIdStr,Long.class);
        Long operaterId=parseArg(operaterIdStr,Long.class);
        return verificationRecordService.queryVerificationRecordInfo(recordId,operaterId).toJson();
    }
	@ResponseBody
	@RequestMapping(value = "downloadExcelTemplate")
	public void downloadExcelTemplate(HttpServletRequest req, HttpServletResponse res) {
		String operaterIdStr = getArg.apply(req, "operaterId");
		Long operaterId = toLongArg.apply(operaterIdStr);
		File file = (File) shopService.downloadExcelTemplate(operaterId).getDetail();
		try {
			if (file != null) {
				InputStream is = new FileInputStream(file);
				OutputStream os = res.getOutputStream();
				byte temp[] = new byte[1024];
				int size = -1;
				while ((size = is.read(temp)) != -1) {
					os.write(temp, 0, size);
				}
				is.close();
				os.close();
			}

		} catch (Exception e) {
			LogFactory.error(this, "下载Excel模板时发生异常", e);
		}
	}

}
