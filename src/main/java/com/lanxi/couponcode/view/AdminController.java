package com.lanxi.couponcode.view;

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

/**
 * 管理员端
 * Created by yangyuanjian on 2017/11/20.
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
//    private BiFunction<HttpServletRequest,String,String> getArg=(r,n)->r.getParameter(n);



	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "", produces = "application/json;charset=utf-8")
	public String addAccount(HttpServletRequest req,HttpServletResponse res) {
		String userName=req.getParameter("userName");
		String merchantName=req.getParameter("merchantName");
		String phone=req.getParameter("phone");
		String operaterIdStr = req.getParameter("operaterId");
		String merchantIdStr = req.getParameter("merchantId");
		String typeStr=req.getParameter("type");
		Long operaterId = Long.valueOf(operaterIdStr);
		Long merchantId = Long.valueOf(merchantIdStr);
		AccountType type=AccountType.getType(typeStr);
		return accountService.addAccount(type, userName, phone, merchantName, merchantId, operaterId).toJson();
	}
	
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "", produces = "application/json;charset=utf-8")
	public String merchantAddAccount(HttpServletRequest req,HttpServletResponse res) {
		String userName=req.getParameter("userName");
		String shopName=req.getParameter("shopName");
		String phone=req.getParameter("phone");
		String operaterIdStr = req.getParameter("operaterId");
		String shopIdStr = req.getParameter("shopId");
		String typeStr=req.getParameter("type");
		Long operaterId = Long.valueOf(operaterIdStr);
		Long shopId = Long.valueOf(shopIdStr);
		AccountType type=AccountType.getType(typeStr);
		return accountService.merchantAddAccount(type, userName, phone, shopName, shopId, operaterId).toJson();
	}
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "", produces = "application/json;charset=utf-8")
	public String queryAccounts(HttpServletRequest req,HttpServletResponse res) {
		String phone=req.getParameter("phone");
		String merchantName=req.getParameter("merchantName");
		String typeStr=req.getParameter("type");
		AccountType type=AccountType.getType(typeStr);
		String operaterIdStr = req.getParameter("operaterId");
		Long operaterId = Long.valueOf(operaterIdStr);
		String statusStr=req.getParameter("status");
		AccountStatus status=AccountStatus.getType(statusStr);
		String pageNumStr=req.getParameter("pageNum");
		String pageSizeStr=req.getParameter("pageSize");
		Integer pageNum=Integer.valueOf(pageNumStr);
		Integer pageSize=Integer.valueOf(pageSizeStr);
		return accountService.queryAccounts(phone, merchantName, type, status, pageNum, pageSize, operaterId).toJson();
	}
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "", produces = "application/json;charset=utf-8")
	public String merchantQueryAccounts(HttpServletRequest req,HttpServletResponse res) {
		String phone=req.getParameter("phone");
		String shopName=req.getParameter("shopName");
		String typeStr=req.getParameter("type");
		AccountType type=AccountType.getType(typeStr);
		String operaterIdStr = req.getParameter("operaterId");
		Long operaterId = Long.valueOf(operaterIdStr);
		String statusStr=req.getParameter("status");
		AccountStatus status=AccountStatus.getType(statusStr);
		String pageNumStr=req.getParameter("pageNum");
		String pageSizeStr=req.getParameter("pageSize");
		Integer pageNum=Integer.valueOf(pageNumStr);
		Integer pageSize=Integer.valueOf(pageSizeStr);
		return accountService.merchantQueryAccounts(phone, shopName, type, status, pageNum, pageSize, operaterId).toJson();
	}
	
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "", produces = "application/json;charset=utf-8")
	public String queryShopAccounts(HttpServletRequest req,HttpServletResponse res) {
		String shopIdStr=req.getParameter("shopId");
		Long shopId=Long.valueOf(shopIdStr);
		String pageNumStr=req.getParameter("pageNum");
		String pageSizeStr=req.getParameter("pageSize");
		Integer pageNum=Integer.valueOf(pageNumStr);
		Integer pageSize=Integer.valueOf(pageSizeStr);
		String operaterIdStr = req.getParameter("operaterId");
		Long operaterId = Long.valueOf(operaterIdStr);
		return accountService.queryShopAccounts(shopId, operaterId, pageNum, pageSize).toJson();
	}
	
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "", produces = "application/json;charset=utf-8")
	public String freezeAccount(HttpServletRequest req,HttpServletResponse res) {
		String operaterIdStr = req.getParameter("operaterId");
		Long operaterId = Long.valueOf(operaterIdStr);
		String accountIdStr=req.getParameter("accountId");
		Long accountId=Long.valueOf(accountIdStr);
		return accountService.freezeAccount(accountId, operaterId).toJson();
	}
	
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "", produces = "application/json;charset=utf-8")
	public String unFreezeAccount(HttpServletRequest req,HttpServletResponse res) {
		String operaterIdStr = req.getParameter("operaterId");
		Long operaterId = Long.valueOf(operaterIdStr);
		String accountIdStr=req.getParameter("accountId");
		Long accountId=Long.valueOf(accountIdStr);
		return accountService.unfreezeAccount(accountId, operaterId).toJson();
	}
	
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "", produces = "application/json;charset=utf-8")
	public String delAccount(HttpServletRequest req,HttpServletResponse res) {
		String operaterIdStr = req.getParameter("operaterId");
		Long operaterId = Long.valueOf(operaterIdStr);
		String accountIdStr=req.getParameter("accountId");
		Long accountId=Long.valueOf(accountIdStr);
		return accountService.delAccount(accountId, operaterId).toJson();
	}

    @SetUtf8
    @ResponseBody
    @RequestMapping(value = "queryCodes", produces = "application/json;charset=utf-8")
    public String queryCodes(HttpServletRequest req,HttpServletResponse res){
	    String timeStart=getArg.apply(req,"timeStart");
	    String timeEnd=getArg.apply(req,"timeStop");
        String merchantName=getArg.apply(req,"merchantName");
        String commodityName=getArg.apply(req,"commodityName");

        String codeStr=getArg.apply(req,"code");
        String codeIdStr=getArg.apply(req,"codeId");
        String pageNumStr=getArg.apply(req,"pageNum");
        String pageSizeStr=getArg.apply(req,"pageSize");
        String operaterIdStr=getArg.apply(req,"operaterId");

        Long code= parseArg(codeStr,Long.class);
        Long codeId=parseArg(codeIdStr,Long.class);
        Long operaterId=parseArg(operaterIdStr,Long.class);

        Integer pageNum=parseArg(pageNumStr,Integer.class);
        Integer pageSize=parseArg(pageSizeStr,Integer.class);

		return codeService.queryCodes(timeStart,timeEnd,merchantName,commodityName,code,codeId,pageNum,pageSize,operaterId).toJson();
	}

    @SetUtf8
    @ResponseBody
    @RequestMapping(value = "queryDailyRecords", produces = "application/json;charset=utf-8")
	public String queryDailyRecords(HttpServletRequest req,HttpServletResponse res){
        String timeStart=getArg.apply(req,"timeStart");
        String timeEnd=getArg.apply(req,"timeStop");
        String merchantName=getArg.apply(req,"merchantName");
        String clearStatusStr=getArg.apply(req,"clearStatus");
        String pageNumStr=getArg.apply(req,"pageNum");
        String pageSizeStr=getArg.apply(req,"pageSize");
        String operaterIdStr=getArg.apply(req,"operaterId");

        ClearStatus clearStatus=ClearStatus.getType(clearStatusStr);
        Long operaterId=parseArg(operaterIdStr,Long.class);
        Integer pageNum=parseArg(pageNumStr,Integer.class);
        Integer pageSize=parseArg(pageSizeStr,Integer.class);

        return clearService.queryDailyRecords(merchantName,timeStart,timeEnd,clearStatus,pageNum,pageSize,operaterId).toJson();
    }

    @SetUtf8
    @ResponseBody
    @RequestMapping(value = "queryDailyRecord", produces = "application/json;charset=utf-8")
    public String queryDailyRecord(HttpServletRequest req,HttpServletResponse res){
        String recordIdStr=getArg.apply(req,"recordId");
        String operaterIdStr=getArg.apply(req,"operaterId");

        Long recordId=parseArg(recordIdStr,Long.class);
        Long operaterId=parseArg(operaterIdStr,Long.class);

        return clearService.queryDailyRecordInfo(recordId,operaterId).toJson();
    }

    @SetUtf8
    @ResponseBody
    @RequestMapping(value = "queryClearRecords", produces = "application/json;charset=utf-8")
    public String queryClearRecords(HttpServletRequest req,HttpServletResponse res){
        String timeStart=getArg.apply(req,"timeStart");
        String timeEnd=getArg.apply(req,"timeStop");
        String merchantName=getArg.apply(req,"merchantName");
        String clearStatusStr=getArg.apply(req,"clearStatus");
        String invoiceStatusStr=getArg.apply(req,"invoiceStatus");
        String pageNumStr=getArg.apply(req,"pageNum");
        String pageSizeStr=getArg.apply(req,"pageSize");
        String operaterIdStr=getArg.apply(req,"operaterId");

        ClearStatus clearStatus=ClearStatus.getType(clearStatusStr);
        Long operaterId=parseArg(operaterIdStr,Long.class);
        Integer pageNum=parseArg(pageNumStr,Integer.class);
        Integer pageSize=parseArg(pageSizeStr,Integer.class);
        InvoiceStatus invoiceStatus=InvoiceStatus.getType(invoiceStatusStr);
        return clearService.queryClearRecords(merchantName,timeStart,timeEnd,clearStatus,invoiceStatus,pageNum,pageSize,operaterId).toJson();
    }

    @SetUtf8
    @ResponseBody
    @RequestMapping(value = "queryClearRecord", produces = "application/json;charset=utf-8")
    public String queryClearRecord(HttpServletRequest req,HttpServletResponse res){
        String recordIdStr=getArg.apply(req,"recordId");
        String operaterIdStr=getArg.apply(req,"operaterId");
        Long recordId=parseArg(recordIdStr,Long.class);
        Long operaterId=parseArg(operaterIdStr,Long.class);
        return clearService.queryRecordInfo(recordId,operaterId).toJson();
    }

    @SetUtf8
    @ResponseBody
    @RequestMapping(value = "clear", produces = "application/json;charset=utf-8")
    public String clear(HttpServletRequest req,HttpServletResponse res){
        String recordIdsStr=getArg.apply(req,"recordIds");
        Long[] recordIds= (Long[]) Stream.of(recordIdsStr.split(",")).map(e->parseArg(e,Long.class)).collect(Collectors.toList()).toArray();
        String operaterIdStr=getArg.apply(req,"operaterId");
        Long operaterId=parseArg(operaterIdStr,Long.class);

        return clearService.clear(recordIds,operaterId).toJson();
    }

    @SetUtf8
    @ResponseBody
    @RequestMapping(value = "queryOperateRecords", produces = "application/json;charset=utf-8")
    public String queryOperateRecords(HttpServletRequest req,HttpServletResponse res){
        String typeStr=getArg.apply(req,"type");
        String targetTypeStr=getArg.apply(req,"targetType");
        String accountTypeStr=getArg.apply(req,"accountType");
        String shopName=getArg.apply(req,"shopName");
        String timeStart=getArg.apply(req,"timeStart");
        String timeEnd=getArg.apply(req,"timeStop");
        String merchantName=getArg.apply(req,"merchantName");
        String name=getArg.apply(req,"name");
        String phone=getArg.apply(req,"phone");
        String pageNumStr=getArg.apply(req,"pageNum");
        String pageSizeStr=getArg.apply(req,"pageSize");
        String operaterIdStr=getArg.apply(req,"operaterId");

        OperateType type=OperateType.getType(typeStr);
        OperateTargetType targetType=OperateTargetType.getType(targetTypeStr);
        AccountType accountType= AccountType.getType(accountTypeStr);
        Long operaterId=parseArg(operaterIdStr,Long.class);
        Integer pageNum=parseArg(pageNumStr,Integer.class);
        Integer pageSize=parseArg(pageSizeStr,Integer.class);
        return operateRecordService.queryOperateRecord(type,targetType,merchantName,shopName,timeStart,timeEnd,accountType,name,phone,pageNum,pageSize,operaterId).toJson();
    }
    @SetUtf8
    @ResponseBody
    @RequestMapping(value = "queryOperateRecord", produces = "application/json;charset=utf-8")
    public String queryOperateRecord(HttpServletRequest req,HttpServletResponse res){
        String recordIdStr=getArg.apply(req,"recordId");
        String operaterIdStr=getArg.apply(req,"operaterId");
        Long recordId=parseArg(recordIdStr,Long.class);
        Long operaterId=parseArg(operaterIdStr,Long.class);
        return operateRecordService.queryOperateRecordInfo(recordId,operaterId).toJson();
    }

    @SetUtf8
    @ResponseBody
    @RequestMapping(value = "queryRequests", produces = "application/json;charset=utf-8")
    public String queryRequests(HttpServletRequest req,HttpServletResponse res){
        String typeStr=getArg.apply(req,"operateType");
        String statusStr=getArg.apply(req,"status");
        String commodityTypeStr=getArg.apply(req,"commodityType");
        String shopName=getArg.apply(req,"shopName");
        String timeStart=getArg.apply(req,"timeStart");
        String timeEnd=getArg.apply(req,"timeStop");
        String commodityName=getArg.apply(req,"commodityName");
        String merchantName=getArg.apply(req,"merchantName");
        String pageNumStr=getArg.apply(req,"pageNum");
        String pageSizeStr=getArg.apply(req,"pageSize");
        String operaterIdStr=getArg.apply(req,"operaterId");

        RequestOperateType operateType=RequestOperateType.getType(typeStr);
        RequestStatus status=RequestStatus.getType(statusStr);
        CommodityType commodityType=CommodityType.getType(commodityTypeStr);
        Long operaterId=parseArg(operaterIdStr,Long.class);
        Integer pageNum=parseArg(pageNumStr,Integer.class);
        Integer pageSize=parseArg(pageSizeStr,Integer.class);
        return requestService.queryRequests(timeStart,timeEnd,commodityName,merchantName,operateType,status,commodityType,null,pageNum,pageSize,operaterId).toJson();
    }

    @SetUtf8
    @ResponseBody
    @RequestMapping(value = "passRequest", produces = "application/json;charset=utf-8")
    public String passRequest(HttpServletRequest req,HttpServletResponse res){
        String requestIdStr=getArg.apply(req,"requestId");
        String operaterIdStr=getArg.apply(req,"operaterId");
        String reason=getArg.apply(req,"reason");
        Long requestId=parseArg(requestIdStr,Long.class);
        Long operaterId=parseArg(operaterIdStr,Long.class);
        return requestService.agreeRequest(requestId,operaterId,reason).toJson();
    }

    @SetUtf8
    @ResponseBody
    @RequestMapping(value = "rejectRequest", produces = "application/json;charset=utf-8")
    public String rejectRequest(HttpServletRequest req,HttpServletResponse res){
        String requestIdStr=getArg.apply(req,"requestId");
        String operaterIdStr=getArg.apply(req,"operaterId");
        String reason=getArg.apply(req,"reason");
        Long requestId=parseArg(requestIdStr,Long.class);
        Long operaterId=parseArg(operaterIdStr,Long.class);
        return requestService.disagreeRequest(requestId,operaterId,reason).toJson();
    }

    @SetUtf8
    @ResponseBody
    @RequestMapping(value = "queryVerifyRecords", produces = "application/json;charset=utf-8")
    public String queryVerifyRecords(HttpServletRequest req,HttpServletResponse res){
        String codeStr=getArg.apply(req,"code");
        String timeStart=getArg.apply(req,"timeStart");
        String timeEnd=getArg.apply(req,"timeStop");
        String shopName=getArg.apply(req,"shopName");
        String merchantName=getArg.apply(req,"merchantName");
        String phone=getArg.apply(req,"phone");
        String verificationTypeString=getArg.apply(req,"verificationType");
        String pageNumStr=getArg.apply(req,"pageNum");
        String pageSizeStr=getArg.apply(req,"pageSize");
        String operaterIdStr=getArg.apply(req,"operaterId");

        VerificationType type=toVerificationType.apply(verificationTypeString);
        Integer pageNum=toIntArg.apply(pageNumStr);
        Integer pageSize=toIntArg.apply(pageSizeStr);
        return null;
    }
}
