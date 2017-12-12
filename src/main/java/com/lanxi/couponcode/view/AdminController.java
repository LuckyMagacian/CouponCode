package com.lanxi.couponcode.view;

import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.couponcode.spi.consts.annotations.LoginCheck;
import com.lanxi.couponcode.spi.consts.annotations.SetUtf8;
import com.lanxi.couponcode.spi.consts.enums.*;
import com.lanxi.couponcode.spi.service.*;
import com.lanxi.util.entity.LogFactory;
import com.lanxi.util.utils.LoggerUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.lanxi.couponcode.spi.assist.ArgAssist.*;
import static com.lanxi.couponcode.spi.assist.PredicateAssist.isNull;
import static com.lanxi.couponcode.spi.assist.PredicateAssist.notId;

/**
 * 管理员端 Created by yangyuanjian on 2017/11/20.
 */

@Controller
@RequestMapping ("admin")
@EasyLog (LoggerUtil.LogLevel.INFO)
public class AdminController {
    @Resource(name="accountControllerService")
    private AccountService accountService;
    @Resource(name="codeControllerService")
    private CouponService codeService;
    @Resource(name = "clearControllerService")
    private ClearService clearService;
    @Resource(name = "operateRecordControllerService")
    private OperateRecordService operateRecordService;
    @Resource(name = "requestControllerService")
    private RequestService requestService;
    @Resource(name = "verificationRecordControllerService")
    private VerificationRecordService verificationRecordService;
    @Resource(name = "merchantControllerService")
    private MerchantService merchantService;
    @Resource(name = "shopControllerService")
    private ShopService shopService;
    @Resource(name="commodityControllerService")
    private CommodityService commodityService;
    @Resource(name = "orderControllerService")
    private OrderService orderService;
    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "queryCodes", produces = "application/json;charset=utf-8")
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

        return codeService.queryCodes(timeStart, timeEnd, merchantName, commodityName, code,
                                        codeId, pageNum, pageSize, operaterId).toJson();
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "queryDailyRecords", produces = "application/json;charset=utf-8")
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

        return clearService.queryDailyRecords(merchantName, timeStart, timeEnd, clearStatus, pageNum, pageSize, operaterId).toJson();
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "queryDailyRecord", produces = "application/json;charset=utf-8")
    public String queryDailyRecord(HttpServletRequest req, HttpServletResponse res) {
        String recordIdStr = getArg.apply(req, "recordId");
        String operaterIdStr = getArg.apply(req, "operaterId");

        Long recordId = parseArg(recordIdStr, Long.class);
        Long operaterId = parseArg(operaterIdStr, Long.class);

        return clearService.queryDailyRecordInfo(recordId, operaterId).toJson();
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "queryClearRecords", produces = "application/json;charset=utf-8")
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
        return clearService.queryClearRecords(merchantName, timeStart, timeEnd, clearStatus, invoiceStatus,
                                                pageNum, pageSize, operaterId).toJson();
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "queryClearRecord", produces = "application/json;charset=utf-8")
    public String queryClearRecord(HttpServletRequest req, HttpServletResponse res) {
        String recordIdStr = getArg.apply(req, "recordId");
        String operaterIdStr = getArg.apply(req, "operaterId");
        Long recordId = parseArg(recordIdStr, Long.class);
        Long operaterId = parseArg(operaterIdStr, Long.class);
        return clearService.queryRecordInfo(recordId, operaterId).toJson();
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "clear", produces = "application/json;charset=utf-8")
    public String clear(final HttpServletRequest req, final HttpServletResponse res) {
        String recordIdsStr = getArg.apply(req, "recordIds");
        List<Long> recordIdsOrigin = Stream.of(recordIdsStr.split(","))
                                            .map(e -> parseArg(e, Long.class))
                                            .collect(Collectors.toList());
        Long [] recordIds=new Long[recordIdsOrigin.size()];
        int index=0;
        for(Long each:recordIdsOrigin)
            recordIds[index++]=each;
        String operaterIdStr = getArg.apply(req, "operaterId");
        Long operaterId = parseArg(operaterIdStr, Long.class);
        return clearService.clear(recordIds, operaterId).toJson();
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "queryOperateRecords", produces = "application/json;charset=utf-8")
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
        return operateRecordService.queryOperateRecord(type, targetType, merchantName, shopName, timeStart,
                                                        timeEnd, accountType, name, phone, pageNum, pageSize, operaterId).toJson();
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "queryOperateRecord", produces = "application/json;charset=utf-8")
    public String queryOperateRecord(HttpServletRequest req, HttpServletResponse res) {
        String recordIdStr = getArg.apply(req, "recordId");
        String operaterIdStr = getArg.apply(req, "operaterId");
        Long recordId = parseArg(recordIdStr, Long.class);
        Long operaterId = parseArg(operaterIdStr, Long.class);
        return operateRecordService.queryOperateRecordInfo(recordId, operaterId).toJson();
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "queryRequests", produces = "application/json;charset=utf-8")
    public String queryRequests(HttpServletRequest req, HttpServletResponse res) {
        String typeStr = getArg.apply(req, "operateType");
        String statusStr = getArg.apply(req, "status");
        String commodityTypeStr = getArg.apply(req, "commodityType");
//        String shopName = getArg.apply(req, "shopName");
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
        return requestService.queryRequests(timeStart, timeEnd, commodityName, merchantName, operateType,
                status, commodityType, null, pageNum, pageSize, operaterId).toJson();
    }
    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "queryRequest", produces = "application/json;charset=utf-8")
    public String queryRequest(HttpServletRequest req, HttpServletResponse res) {
        String requestIdStr = getArg.apply(req, "requestId");
        String operaterIdStr = getArg.apply(req, "operaterId");
        Long requestId = parseArg(requestIdStr, Long.class);
        Long operaterId = parseArg(operaterIdStr, Long.class);
        return requestService.queryRequest(requestId, operaterId).toJson();
    }



    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "passRequest", produces = "application/json;charset=utf-8")
    public String passRequest(HttpServletRequest req, HttpServletResponse res) {
        String requestIdStr = getArg.apply(req, "requestId");
        String operaterIdStr = getArg.apply(req, "operaterId");
        String reason = getArg.apply(req, "reason");
        Long requestId = parseArg(requestIdStr, Long.class);
        Long operaterId = parseArg(operaterIdStr, Long.class);
        return requestService.agreeRequest(requestId, operaterId, reason).toJson();
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "rejectRequest", produces = "application/json;charset=utf-8")
    public String rejectRequest(HttpServletRequest req, HttpServletResponse res) {
        String requestIdStr = getArg.apply(req, "requestId");
        String operaterIdStr = getArg.apply(req, "operaterId");
        String reason = getArg.apply(req, "reason");
        Long requestId = parseArg(requestIdStr, Long.class);
        Long operaterId = parseArg(operaterIdStr, Long.class);
        return requestService.disagreeRequest(requestId, operaterId, reason).toJson();
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "queryVerifyRecords", produces = "application/json;charset=utf-8")
    public String queryVerifyRecords(final HttpServletRequest req, final HttpServletResponse res) {
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

        Long code = toLongArg.apply(codeStr);
        VerificationType type = toVerificationType.apply(verificationTypeString);
        Integer pageNum = toIntArg.apply(pageNumStr);
        Integer pageSize = toIntArg.apply(pageSizeStr);
        Long operaterId = toLongArg.apply(operaterIdStr);
        return verificationRecordService.queryVerificationRecords(code, timeStart, timeEnd, merchantName, shopName,
                codeStr, phone, type, pageNum, pageSize, operaterId).toJson();
    }


    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping(value = "queryVerifyRecord", produces = "application/json;charset=utf-8")
    public String queryVerifyRecord(final HttpServletRequest req, final HttpServletResponse res){
        String recordIdStr=getArg.apply(req,"recordId");
        String operaterIdStr=getArg.apply(req,"operaterId");
        Long recordId=parseArg(recordIdStr,Long.class);
        Long operaterId=parseArg(operaterIdStr,Long.class);
        return verificationRecordService.queryVerificationRecordInfo(recordId,operaterId).toJson();
    }

	/* admin添加账户 */
	@SetUtf8
    @LoginCheck
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
    @LoginCheck
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
    @LoginCheck
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
    @LoginCheck
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
    @LoginCheck
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
    @LoginCheck
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
    @LoginCheck
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
    @LoginCheck
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
    @LoginCheck
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
    @LoginCheck
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
    @LoginCheck
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
    @LoginCheck
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
    @LoginCheck
	@RequestMapping(value = "queryMerchantsExport")
	public void queryMerchantsExport(HttpServletRequest req, HttpServletResponse res) {
		String merchantName = getArg.apply(req, "merchantName");
		String timeStart = getArg.apply(req, "timeStart");
		String timeStop = getArg.apply(req, "timeStop");
		String operaterIdStr = getArg.apply(req, "operaterId");
		String merchantStatusStr = getArg.apply(req, "merchantStatus");
		Long operaterId = parseArg(operaterIdStr, Long.class);
		MerchantStatus merchantStatus = MerchantStatus.getType(merchantStatusStr);
		File file = merchantService
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
			file.delete();
		} catch (Exception e) {
			LogFactory.error(this, "导出Excel文件时发生异常", e);
		}
	}
    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping(value = "addCommodity", produces = "application/json;charset=utf-8")
	public String addCommodity(HttpServletRequest req, HttpServletResponse res){
        String commodityName=getArg.apply(req,"commodityName");
        String commodityTypeStr=getArg.apply(req,"commodityType");
        String facePriceStr=getArg.apply(req,"facePrice");
        String costPriceStr=getArg.apply(req,"costPrice");
        String sellPriceStr=getArg.apply(req,"sellPrice");
        String lifeTimeStr=getArg.apply(req,"lifeTime");
        String merchantNameStr=getArg.apply(req,"merchantName");
        String merchantIdStr=getArg.apply(req,"merchantId");
        String operaterIdStr=getArg.apply(req,"operaterId");
        String useDestription=getArg.apply(req,"useDestription");

        if(isNull.test(merchantIdStr)||notId.test(merchantIdStr)){
            return new RetMessage<String>(RetCodeEnum.fail,"商户编号不能为空!",null).toJson();
        }

        CommodityType commodityType= CommodityType.getType(commodityTypeStr);
        BigDecimal facePrice=parseArg(facePriceStr,BigDecimal.class);
        BigDecimal costPrice=parseArg(costPriceStr,BigDecimal.class);
        BigDecimal sellPrice=parseArg(sellPriceStr,BigDecimal.class);
        Integer lifeTime=parseArg(lifeTimeStr,Integer.class);
        Long merchantId=parseArg(merchantIdStr,Long.class);
        Long operaterId=parseArg(operaterIdStr,Long.class);

        return commodityService.addCommodity(commodityName,commodityType,facePrice,costPrice,sellPrice,lifeTime,merchantNameStr,useDestription,merchantId,operaterId).toJson();
    }
    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping(value = "modifyCommodity", produces = "application/json;charset=utf-8")
    public String modifyCommodity(HttpServletRequest req,HttpServletResponse res){
        String facePriceStr=getArg.apply(req,"facePrice");
        String costPriceStr=getArg.apply(req,"costPrice");
        String sellPriceStr=getArg.apply(req,"sellPrice");
        String lifeTimeStr=getArg.apply(req,"lifeTime");
        String commodityIdStr=getArg.apply(req,"commodityId");
        String operaterIdStr=getArg.apply(req,"operaterId");

        BigDecimal facePrice=parseArg(facePriceStr,BigDecimal.class);
        BigDecimal costPrice=parseArg(costPriceStr,BigDecimal.class);
        BigDecimal sellPrice=parseArg(sellPriceStr,BigDecimal.class);
        Integer lifeTime=parseArg(lifeTimeStr,Integer.class);
        Long commodityId=parseArg(commodityIdStr,Long.class);
        Long operaterId=parseArg(operaterIdStr,Long.class);

        return commodityService.modifyCommodity(costPrice,facePrice,sellPrice,lifeTime,commodityId,operaterId).toJson();
    }
    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping(value = "shelveCommodity", produces = "application/json;charset=utf-8")
    public String shelveCommodity(HttpServletRequest req,HttpServletResponse res){
        String commodityIdStr=getArg.apply(req,"commodityId");
        String operaterIdStr=getArg.apply(req,"operaterId");
        Long commodityId=parseArg(commodityIdStr,Long.class);
        Long operaterId=parseArg(operaterIdStr,Long.class);
        return commodityService.shelveCommodity(commodityId,operaterId).toJson();
    }
    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping(value = "unshelveCommodity", produces = "application/json;charset=utf-8")
    public String unshelveCommodity(HttpServletRequest req,HttpServletResponse res){
        String commodityIdStr=getArg.apply(req,"commodityId");
        String operaterIdStr=getArg.apply(req,"operaterId");
        Long commodityId=parseArg(commodityIdStr,Long.class);
        Long operaterId=parseArg(operaterIdStr,Long.class);
        return commodityService.unshelveCommodity(commodityId,operaterId).toJson();
    }
    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping(value = "delCommodity", produces = "application/json;charset=utf-8")
    public String delCommodity(HttpServletRequest req,HttpServletResponse res){
        String commodityIdStr=getArg.apply(req,"commodityId");
        String operaterIdStr=getArg.apply(req,"operaterId");
        Long commodityId=parseArg(commodityIdStr,Long.class);
        Long operaterId=parseArg(operaterIdStr,Long.class);
        return commodityService.delCommodity(commodityId,operaterId).toJson();
    }
    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping(value = "queryCommodities", produces = "application/json;charset=utf-8")
    public String queryCommodities(HttpServletRequest req,HttpServletResponse res){
        String merchantNameStr=getArg.apply(req,"merchantName");
        String commodityName=getArg.apply(req,"commodityName");
        String commodityTypeStr=getArg.apply(req,"commodityType");
        String commodityStatusStr=getArg.apply(req,"commodityStatus");
        String timeStart=getArg.apply(req,"timeStart");
        String timeStop=getArg.apply(req,"timeStop");
        String pageNumStr = getArg.apply(req, "pageNum");
        String pageSizeStr = getArg.apply(req, "pageSize");
        String operaterIdStr=getArg.apply(req,"operaterId");

        CommodityType commodityType= CommodityType.getType(commodityTypeStr);
        CommodityStatus commodityStatus= CommodityStatus.getType(commodityStatusStr);
        Integer pageNum = toIntArg.apply(pageNumStr);
        Integer pageSize = toIntArg.apply(pageSizeStr);
        Long operaterId = toLongArg.apply(operaterIdStr);
        return commodityService.queryCommodities(merchantNameStr,commodityName,commodityType,commodityStatus,timeStart,timeStart,pageNum,pageSize,operaterId).toJson();
    }
    @LoginCheck
    @RequestMapping(value = "exportCommodities", produces = "application/json;charset=utf-8")
    public void exportCommodities(HttpServletRequest req,HttpServletResponse res){
        String merchantNameStr=getArg.apply(req,"merchantName");
        String commodityName=getArg.apply(req,"commodityName");
        String commodityTypeStr=getArg.apply(req,"commodityType");
        String commodityStatusStr=getArg.apply(req,"commodityStatus");
        String timeStart=getArg.apply(req,"timeStart");
        String timeStop=getArg.apply(req,"timeStop");
        String operaterIdStr=getArg.apply(req,"operaterId");

        CommodityType commodityType= CommodityType.getType(commodityTypeStr);
        CommodityStatus commodityStatus= CommodityStatus.getType(commodityStatusStr);
        Long operaterId = toLongArg.apply(operaterIdStr);
        File file=commodityService.queryCommoditiesExport(merchantNameStr,commodityName,commodityType,commodityStatus,timeStart,timeStart,operaterId).getDetail();
        FileInputStream fin=null;
        try {
            fin= new FileInputStream(file);
            byte[] bytes=new byte[1024];
            int readLenth=-1;
            while((readLenth=fin.read(bytes))>0){
                if(readLenth==1024)
                    res.getOutputStream().write(bytes);
                else
                    res.getOutputStream().write(bytes,0,readLenth);
            }
            res.getOutputStream().flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fin!=null)
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
 	@SetUtf8
    @LoginCheck
    @ResponseBody
	@RequestMapping(value = "queryAllMerchant", produces = "application/json;charset=utf-8")
    public String queryAllMerchant(HttpServletRequest req,HttpServletResponse res) {
    	String operaterIdStr=getArg.apply(req,"operaterId");
    	Long operaterId = toLongArg.apply(operaterIdStr);
    	return merchantService.queryAllMerchant(operaterId).toJson();
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping(value = "queryCommodity", produces = "application/json;charset=utf-8")
    public String queryCommodity(HttpServletRequest req,HttpServletResponse res){
        String commodityIdStr=getArg.apply(req,"commodityId");
        String operaterIdStr=getArg.apply(req,"operaterId");
        Long operaterId = toLongArg.apply(operaterIdStr);
        Long commodityId=toLongArg.apply(commodityIdStr);
        return commodityService.queryCommodity(commodityId,operaterId).toJson();
    }
    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping(value = "queryAllCommodityIds", produces = "application/json;charset=utf-8")
    public String queryAllCommodityIds(HttpServletRequest req,HttpServletResponse res){
        String operaterIdStr=getArg.apply(req,"operaterId");
        Long operaterId = toLongArg.apply(operaterIdStr);
        return commodityService.queryAllCommodityIds(operaterId).toJson();
    }

	@SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping(value = "adminQueryShop", produces = "application/json;charset=utf-8")
    public String adminQueryShop(HttpServletRequest req,HttpServletResponse res) {
    	String operaterIdStr=getArg.apply(req,"operaterId");
    	Long operaterId = toLongArg.apply(operaterIdStr);
    	String merchantName=getArg.apply(req, "merchantName");
    	String shopName=getArg.apply(req, "shopName");
    	String shopStatusStr=getArg.apply(req, "shopStatus");
    	 String pageNumStr = getArg.apply(req, "pageNum");
         String pageSizeStr = getArg.apply(req, "pageSize");
         Integer pageNum = toIntArg.apply(pageNumStr);
         Integer pageSize = toIntArg.apply(pageSizeStr);
         ShopStatus shopStatus=ShopStatus.getType(shopStatusStr);
    	return shopService.adminQueryShop(merchantName, shopName, shopStatus, operaterId, pageNum, pageSize).toJson();
    }
	@SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping(value = "queryOrderInfo", produces = "application/json;charset=utf-8")
	public String queryOrderInfo(HttpServletRequest req,HttpServletResponse res) {
		String orderIdStr=getArg.apply(req,"orderId");
		String operaterIdStr=getArg.apply(req, "operaterId");
		Long orderId=toLongArg.apply(orderIdStr);
		Long operaterId=toLongArg.apply(operaterIdStr);
		return orderService.queryOrderInfo(orderId, operaterId).toJson();
	}
	@SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping(value = "queryOrders", produces = "application/json;charset=utf-8")
	public String queryOrders(HttpServletRequest req,HttpServletResponse res) {
		String StartDate=getArg.apply(req,"StartDate");
		String EndDate=getArg.apply(req,"EndDate");
		String Phone=getArg.apply(req,"Phone");
		String orderIdStr=getArg.apply(req, "orderId");
		String SkuCodeStr=getArg.apply(req,"SkuCode");
		String SRC=getArg.apply(req,"SRC");
		String orderStatusStr=getArg.apply(req, "orderStatus");
		String pageNumStr=getArg.apply(req,"pageNum");
		String pageSizeStr=getArg.apply(req, "pageSize");
		String operaterIdStr=getArg.apply(req, "operaterId");
		Long orderId=toLongArg.apply(orderIdStr);
		Long SkuCode=toLongArg.apply(SkuCodeStr);
		OrderStatus orderStatus=OrderStatus.getType(orderStatusStr);
		Integer pageNum=toIntArg.apply(pageNumStr);
		Integer pageSize=toIntArg.apply(pageSizeStr);
		Long operaterId=toLongArg.apply(operaterIdStr);
		return orderService.queryOrders(StartDate, EndDate, Phone,
				orderId, SkuCode, SRC, orderStatus, pageNum, pageSize, operaterId).toJson();
		
		}
	
    @LoginCheck
    @ResponseBody
    @RequestMapping(value = "queryOrderExport", produces = "application/json;charset=utf-8")
	public void queryOrderExport(HttpServletRequest req,HttpServletResponse res) {
    	String StartDate=getArg.apply(req,"StartDate");
		String EndDate=getArg.apply(req,"EndDate");
		String Phone=getArg.apply(req,"Phone");
		String orderIdStr=getArg.apply(req, "orderId");
		String SkuCodeStr=getArg.apply(req,"SkuCode");
		String SRC=getArg.apply(req,"SRC");
		String orderStatusStr=getArg.apply(req, "orderStatus");
		String operaterIdStr=getArg.apply(req, "operaterId");
		Long orderId=toLongArg.apply(orderIdStr);
		Long SkuCode=toLongArg.apply(SkuCodeStr);
		OrderStatus orderStatus=OrderStatus.getType(orderStatusStr);
		Long operaterId=toLongArg.apply(operaterIdStr);
		try {
			File file=orderService.orderExport(StartDate, EndDate, Phone, orderId,
					SkuCode, SRC, orderStatus, operaterId).getDetail();
			OutputStream os = res.getOutputStream();
			 InputStream is=new FileInputStream(file);
			 int temp=0;
			 byte[] by=new byte[1024];
			 while((temp=is.read(by))!=-1) {
				 os.write(by,0,temp);
			 }
			 is.close();
			 os.flush();
			 file.delete();
		} catch (Exception e) {
			LogFactory.error(this,"导出门店时发生异常",e);
		}
		
	}
}
