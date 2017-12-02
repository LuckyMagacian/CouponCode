package com.lanxi.couponcode.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lanxi.couponcode.impl.entity.Account;
import com.lanxi.couponcode.impl.entity.Request;
import com.lanxi.couponcode.spi.consts.enums.*;
import com.lanxi.couponcode.spi.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.lanxi.couponcode.spi.consts.annotations.SetUtf8;

import static com.lanxi.couponcode.spi.assist.ArgAssist.*;
import java.math.BigDecimal;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.lanxi.couponcode.spi.consts.enums.AccountStatus;
import com.lanxi.couponcode.spi.consts.enums.AccountType;
import com.lanxi.couponcode.spi.consts.enums.MerchantStatus;
import com.lanxi.couponcode.spi.service.AccountService;
import com.lanxi.couponcode.spi.service.MerchantService;
import com.lanxi.couponcode.spi.service.ShopService;
import com.lanxi.util.entity.LogFactory;

/**
 * 管理员端 Created by yangyuanjian on 2017/11/20.
 */
@Controller
@RequestMapping("admin")
public class AdminController {
	@Resource
	private AccountService accountService;
	@Resource
	private CouponService codeService;
	@Resource
	private ClearService clearService;
	@Resource
	private OperateRecordService operateRecordService;
	@Resource
	private RequestService requestService;

	@Resource
	private VerificationRecordService verificationRecordService;
	// private BiFunction<HttpServletRequest,String,String>
	// getArg=(r,n)->r.getParameter(n);

	@Resource
	private MerchantService merchantService;
	@Resource
	private ShopService shopService;

	/* admin添加账户 */
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "addAccount", produces = "application/json;charset=utf-8")
	public String addAccount(HttpServletRequest req, HttpServletResponse res) {
		String userName = getArg.apply(req, "userName");
		String merchantName = getArg.apply(req, "merchantName");
		String phone = getArg.apply(req, "phone");
		String operaterIdStr = getArg.apply(req, "operaterId");
		String merchantIdStr = getArg.apply(req, "merchantId");
		String typeStr = getArg.apply(req, "type");
		Long operaterId = parseArg(operaterIdStr, Long.class);
		Long merchantId = parseArg(merchantIdStr, Long.class);
		AccountType type = AccountType.getType(typeStr);
		return accountService.adminAddAccount(type, merchantName, userName, phone, operaterId, merchantId).toJson();
	}

	/* admin账户查询 */
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "queryAccounts", produces = "application/json;charset=utf-8")
	public String queryAccounts(HttpServletRequest req, HttpServletResponse res) {
		String phone = getArg.apply(req, "phone");
		String merchantName = getArg.apply(req, "merchantName");
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
		return accountService.queryAccounts(shopName, phone, merchantName, type, status, pageNum, pageSize, operaterId)
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
	public String delAccount(HttpServletRequest req, HttpServletResponse res) {
		String operaterIdStr = getArg.apply(req, "operaterId");
		Long operaterId = parseArg(operaterIdStr, Long.class);
		String accountIdStr = getArg.apply(req, "accountId");
		Long accountId = parseArg(accountIdStr, Long.class);
		return accountService.delAccount(accountId, operaterId).toJson();
	}

	/* 查询商户详情 */
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "queryMerchantInfo", produces = "application/json;charset=utf-8")
	public String queryMerchantInfo(HttpServletRequest req, HttpServletResponse res) {
		String operaterIdStr = getArg.apply(req, "operaterId");
		String merchantIdStr = getArg.apply(req, "merchantId");
		Long operaterId = parseArg(operaterIdStr, Long.class);
		Long merchantId = parseArg(merchantIdStr, Long.class);
		return merchantService.queryMerchantInfo(operaterId, merchantId).toJson();
	}

	/* 查询商户下的所有门店 */
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "querySubordinateShops", produces = "application/json;charset=utf-8")
	public String querySubordinateShops(HttpServletRequest req, HttpServletResponse res) {
		String operaterIdStr = getArg.apply(req, "operaterId");
		String merchantIdStr = getArg.apply(req, "merchantId");
		Long operaterId = parseArg(operaterIdStr, Long.class);
		Long merchantId = parseArg(merchantIdStr, Long.class);
		String pageNumStr = getArg.apply(req, "pageNum");
		String pageSizeStr = getArg.apply(req, "pageSize");
		Integer pageNum = parseArg(pageNumStr, Integer.class);
		Integer pageSize = parseArg(pageSizeStr, Integer.class);
		return shopService.queryShops(merchantId, operaterId, pageNum, pageSize).toJson();
	}

	/* 添加商户 */
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "addMerchant", produces = "application/json;charset=utf-8")
	public String addMerchant(HttpServletRequest req, HttpServletResponse res) {
		String merchantName = getArg.apply(req, "merchantName");
		String workAddress = getArg.apply(req, "workAddress");
		String minuteWorkAddress = getArg.apply(req, "minuteWorkAddress");
		String operaterIdStr = getArg.apply(req, "operaterId");
		Long operaterId = parseArg(operaterIdStr, Long.class);
		return merchantService.addMerchant(merchantName, workAddress, minuteWorkAddress, operaterId).toJson();
	}

	/* 修改商户 */
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "modifyMerchant", produces = "application/json;charset=utf-8")
	public String modifyMerchant(HttpServletRequest req, HttpServletResponse res) {
		String merchantName = getArg.apply(req, "merchantName");
		String workAddress = getArg.apply(req, "workAddress");
		String minuteWorkAddress = getArg.apply(req, "minuteWorkAddress");
		String operaterIdStr = getArg.apply(req, "operaterId");
		String merchantIdStr = getArg.apply(req, "merchantId");
		Long operaterId = parseArg(operaterIdStr, Long.class);
		Long merchantId = parseArg(merchantIdStr, Long.class);
		return merchantService.modifyMerchant(merchantName, workAddress, minuteWorkAddress, operaterId, merchantId)
				.toJson();
	}

	/* 商户查询 */
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "queryMerchant", produces = "application/json;charset=utf-8")
	public String queryMerchant(HttpServletRequest req, HttpServletResponse res) {
		String merchantName = getArg.apply(req, "merchantName");
		String merchantStatusStr = getArg.apply(req, "merchantStatus");
		String timeStop = getArg.apply(req, "timeStop");
		String timeStart = getArg.apply(req, "timeStart");
		String pageNumStr = getArg.apply(req, "pageNum");
		String pageSizeStr = getArg.apply(req, "pageSize");
		String operaterIdStr = getArg.apply(req, "operaterId");
		Integer pageNum = parseArg(pageNumStr, Integer.class);
		Integer pageSize = parseArg(pageSizeStr, Integer.class);
		Long operaterId = parseArg(operaterIdStr, Long.class);
		MerchantStatus merchantStatus = MerchantStatus.getType(merchantStatusStr);
		return merchantService
				.queryMerchants(merchantName, merchantStatus, timeStart, timeStop, pageNum, pageSize, operaterId)
				.toJson();

	}

	/* 冻结商户 */
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "freezeMerchant", produces = "application/json;charset=utf-8")
	public String freezeMerchant(HttpServletRequest req, HttpServletResponse res) {
		String operaterIdStr = getArg.apply(req, "operaterId");
		String merchantIdStr = getArg.apply(req, "merchantId");
		Long operaterId = parseArg(operaterIdStr, Long.class);
		Long merchantId = parseArg(merchantIdStr, Long.class);
		return merchantService.disableMerchant(merchantId, operaterId).toJson();
	}

	/* 开启商户 */
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "unfreezeMerchant", produces = "application/json;charset=utf-8")
	public String unfreezeMerchant(HttpServletRequest req, HttpServletResponse res) {
		String operaterIdStr = getArg.apply(req, "operaterId");
		String merchantIdStr = getArg.apply(req, "merchantId");
		Long operaterId = parseArg(operaterIdStr, Long.class);
		Long merchantId = parseArg(merchantIdStr, Long.class);
		return merchantService.enableMerchant(merchantId, operaterId).toJson();
	}

	/* 导出商户 */
	@RequestMapping(value = "queryMerchantsExport")
	public void queryMerchantsExport(HttpServletRequest req, HttpServletResponse res) {
		String merchantName = getArg.apply(req, "merchantName");
		String timeStart = getArg.apply(req, "timeStart");
		String timeStop = getArg.apply(req, "timeStop");
		String operaterIdStr = getArg.apply(req, "operaterId");
		String merchantStatusStr = getArg.apply(req, "merchantStatus");
		Long operaterId = parseArg(operaterIdStr, Long.class);
		MerchantStatus merchantStatus = MerchantStatus.getType(merchantStatusStr);
		File file = (File) merchantService
				.queryMerchantsExport(merchantName, merchantStatus, timeStart, timeStop, operaterId).getDetail();
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

	// -------------------------------------------------------------------------------------------------
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "queryCodes", produces = "application/json;charset=utf-8")
	public String queryCodes(HttpServletRequest req, HttpServletResponse res) {
		String timeStart = getArg.apply(req, "timeStart");
		String timeEnd = getArg.apply(req, "timeStop");
		String merchantName = getArg.apply(req, "merchantName");
		String commodityName = getArg.apply(req, "commodityName");

		String codeStr = getArg.apply(req, "code");
		String codeIdStr = getArg.apply(req, "codeId");
		String pageNumStr = getArg.apply(req, "pageNum");
		String pageSizeStr = getArg.apply(req, "pageSize");
		String operaterIdStr = getArg.apply(req, "operaterId");

		Long code = parseArg(codeStr, Long.class);
		Long codeId = parseArg(codeIdStr, Long.class);
		Long operaterId = parseArg(operaterIdStr, Long.class);

		Integer pageNum = parseArg(pageNumStr, Integer.class);
		Integer pageSize = parseArg(pageSizeStr, Integer.class);

		return codeService.queryCodes(timeStart, timeEnd, merchantName, commodityName, code, codeId, pageNum, pageSize,
				operaterId).toJson();
	}

	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "queryDailyRecords", produces = "application/json;charset=utf-8")
	public String queryDailyRecords(HttpServletRequest req, HttpServletResponse res) {
		String timeStart = getArg.apply(req, "timeStart");
		String timeEnd = getArg.apply(req, "timeStop");
		String merchantName = getArg.apply(req, "merchantName");
		String clearStatusStr = getArg.apply(req, "clearStatus");
		String pageNumStr = getArg.apply(req, "pageNum");
		String pageSizeStr = getArg.apply(req, "pageSize");
		String operaterIdStr = getArg.apply(req, "operaterId");

		ClearStatus clearStatus = ClearStatus.getType(clearStatusStr);
		Long operaterId = parseArg(operaterIdStr, Long.class);
		Integer pageNum = parseArg(pageNumStr, Integer.class);
		Integer pageSize = parseArg(pageSizeStr, Integer.class);

		return clearService
				.queryDailyRecords(merchantName, timeStart, timeEnd, clearStatus, pageNum, pageSize, operaterId)
				.toJson();
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
		String merchantName = getArg.apply(req, "merchantName");
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
		return clearService.queryClearRecords(merchantName, timeStart, timeEnd, clearStatus, invoiceStatus, pageNum,
				pageSize, operaterId).toJson();
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
	@RequestMapping(value = "clear", produces = "application/json;charset=utf-8")
	public String clear(HttpServletRequest req, HttpServletResponse res) {
		String recordIdsStr = getArg.apply(req, "recordIds");
		Long[] recordIds = (Long[]) Stream.of(recordIdsStr.split(",")).map(e -> parseArg(e, Long.class))
				.collect(Collectors.toList()).toArray();
		String operaterIdStr = getArg.apply(req, "operaterId");
		Long operaterId = parseArg(operaterIdStr, Long.class);

		return clearService.clear(recordIds, operaterId).toJson();
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
		String merchantName = getArg.apply(req, "merchantName");
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
		return operateRecordService.queryOperateRecord(type, targetType, merchantName, shopName, timeStart, timeEnd,
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
	@RequestMapping(value = "queryRequests", produces = "application/json;charset=utf-8")
	public String queryRequests(HttpServletRequest req, HttpServletResponse res) {
		String typeStr = getArg.apply(req, "operateType");
		String statusStr = getArg.apply(req, "status");
		String commodityTypeStr = getArg.apply(req, "commodityType");
		String shopName = getArg.apply(req, "shopName");
		String timeStart = getArg.apply(req, "timeStart");
		String timeEnd = getArg.apply(req, "timeStop");
		String commodityName = getArg.apply(req, "commodityName");
		String merchantName = getArg.apply(req, "merchantName");
		String pageNumStr = getArg.apply(req, "pageNum");
		String pageSizeStr = getArg.apply(req, "pageSize");
		String operaterIdStr = getArg.apply(req, "operaterId");

		RequestOperateType operateType = RequestOperateType.getType(typeStr);
		RequestStatus status = RequestStatus.getType(statusStr);
		CommodityType commodityType = CommodityType.getType(commodityTypeStr);
		Long operaterId = parseArg(operaterIdStr, Long.class);
		Integer pageNum = parseArg(pageNumStr, Integer.class);
		Integer pageSize = parseArg(pageSizeStr, Integer.class);
		return requestService.queryRequests(timeStart, timeEnd, commodityName, merchantName, operateType, status,
				commodityType, null, pageNum, pageSize, operaterId).toJson();
	}

	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "passRequest", produces = "application/json;charset=utf-8")
	public String passRequest(HttpServletRequest req, HttpServletResponse res) {
		String requestIdStr = getArg.apply(req, "requestId");
		String operaterIdStr = getArg.apply(req, "operaterId");
		String reason = getArg.apply(req, "reason");
		Long requestId = parseArg(requestIdStr, Long.class);
		Long operaterId = parseArg(operaterIdStr, Long.class);
		return requestService.agreeRequest(requestId, operaterId, reason).toJson();
	}

	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "rejectRequest", produces = "application/json;charset=utf-8")
	public String rejectRequest(HttpServletRequest req, HttpServletResponse res) {
		String requestIdStr = getArg.apply(req, "requestId");
		String operaterIdStr = getArg.apply(req, "operaterId");
		String reason = getArg.apply(req, "reason");
		Long requestId = parseArg(requestIdStr, Long.class);
		Long operaterId = parseArg(operaterIdStr, Long.class);
		return requestService.disagreeRequest(requestId, operaterId, reason).toJson();
	}

	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "queryVerifyRecords", produces = "application/json;charset=utf-8")
	public String queryVerifyRecords(HttpServletRequest req, HttpServletResponse res) {
		String codeStr = getArg.apply(req, "code");
		String timeStart = getArg.apply(req, "timeStart");
		String timeEnd = getArg.apply(req, "timeStop");
		String shopName = getArg.apply(req, "shopName");
		String merchantName = getArg.apply(req, "merchantName");
		String phone = getArg.apply(req, "phone");
		String verificationTypeString = getArg.apply(req, "verificationType");
		String pageNumStr = getArg.apply(req, "pageNum");
		String pageSizeStr = getArg.apply(req, "pageSize");
		String operaterIdStr = getArg.apply(req, "operaterId");

		VerificationType type = toVerificationType.apply(verificationTypeString);
		Integer pageNum = toIntArg.apply(pageNumStr);
		Integer pageSize = toIntArg.apply(pageSizeStr);
		return null;
	}

}
