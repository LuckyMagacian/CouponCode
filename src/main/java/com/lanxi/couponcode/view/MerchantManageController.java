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
	private OperateRecordService operateRecordService;
	@Resource
	private RequestService requestService;
	@SetUtf8
	@RequestMapping(value = "", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String inputMerchantInfo(HttpServletRequest req, HttpServletResponse res) {
		String merchantName = req.getParameter("merchantName");
		String serveExplain = req.getParameter("serveExplain");
		String workAddress = req.getParameter("workAddress");
		String minuteWorkAddress = req.getParameter("minuteWorkAddress");
		String charterCode = req.getParameter("charterCode");
		String oraganizingCode = req.getParameter("oraganizingCode");
		String principal = req.getParameter("principal");
		String linkMan = req.getParameter("linkMan");
		String linkManPhone = req.getParameter("linkManPhone");
		String serviceTel = req.getParameter("serviceTel");
		String email = req.getParameter("email");
		String operaterIdStr = req.getParameter("operaterId");
		String merchantIdStr = req.getParameter("merchantId");
		Long operaterId = Long.valueOf(operaterIdStr);
		Long merchantId = Long.valueOf(merchantIdStr);
		return merchantService.inputMerchantInfo(merchantName, serveExplain, workAddress, minuteWorkAddress,
				charterCode, oraganizingCode, principal, linkMan,
				linkManPhone, serviceTel, email,operaterId, merchantId).toJson();
		
	}
	@SetUtf8
	@RequestMapping(value = "", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String organizingInstitutionBarCodePicUpLoad(HttpServletRequest req,HttpServletResponse res) {
		String operaterIdStr = req.getParameter("operaterId");
		String merchantIdStr = req.getParameter("merchantId");
		Long operaterId = Long.valueOf(operaterIdStr);
		Long merchantId = Long.valueOf(merchantIdStr);
		String result=null;
		try {
			File file=new File("file");
			InputStream is=req.getInputStream();
			OutputStream os=new FileOutputStream(file);
			byte temp[] = new byte[1024];  
            int size = -1;  
            while ((size = is.read(temp)) != -1) {   
                os.write(temp, 0, size);  
            }  
            os.close();
            result= merchantService.organizingInstitutionBarCodePicUpLoad(file, operaterId, merchantId).toJson();
		} catch (Exception e) {
			LogFactory.error(this,"上传组织机构代码证时发生异常");
			
		}
		return result;
	}
	@SetUtf8
	@RequestMapping(value = "", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String businessLicensePicUpLoad(HttpServletRequest req,HttpServletResponse res) {
		String operaterIdStr = req.getParameter("operaterId");
		String merchantIdStr = req.getParameter("merchantId");
		Long operaterId = Long.valueOf(operaterIdStr);
		Long merchantId = Long.valueOf(merchantIdStr);
		String result=null;
		try {
			File file=new File("file");
			InputStream is=req.getInputStream();
			OutputStream os=new FileOutputStream(file);
			byte temp[] = new byte[1024];  
            int size = -1;  
            while ((size = is.read(temp)) != -1) {   
                os.write(temp, 0, size);  
            }  
            os.close();
            result= merchantService.businessLicensePicUpLoad(file, operaterId, merchantId).toJson();
		} catch (Exception e) {
			LogFactory.error(this,"上传组织机构代码证时发生异常");
			
		}
		return result;
	}
	@SetUtf8
	@RequestMapping(value = "", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String otherPicUpLoad(HttpServletRequest req,HttpServletResponse res) {
		String operaterIdStr = req.getParameter("operaterId");
		String merchantIdStr = req.getParameter("merchantId");
		Long operaterId = Long.valueOf(operaterIdStr);
		Long merchantId = Long.valueOf(merchantIdStr);
		String result=null;
		try {
			File file=new File("file");
			InputStream is=req.getInputStream();
			OutputStream os=new FileOutputStream(file);
			byte temp[] = new byte[1024];  
            int size = -1;  
            while ((size = is.read(temp)) != -1) {   
                os.write(temp, 0, size);  
            }  
            os.close();
            result= merchantService.otherPicUpLoad(file, operaterId, merchantId).toJson();
		} catch (Exception e) {
			LogFactory.error(this,"上传组织机构代码证时发生异常");
			
		}
		return result;
	}
	@SetUtf8
	@RequestMapping(value = "", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String modifyMerchantInfo(HttpServletRequest req, HttpServletResponse res) {
		String merchantName = req.getParameter("merchantName");
		String serveExplain = req.getParameter("serveExplain");
		String workAddress = req.getParameter("workAddress");
		String minuteWorkAddress = req.getParameter("minuteWorkAddress");
		String charterCode = req.getParameter("charterCode");
		String oraganizingCode = req.getParameter("oraganizingCode");
		String principal = req.getParameter("principal");
		String linkMan = req.getParameter("linkMan");
		String linkManPhone = req.getParameter("linkManPhone");
		String serviceTel = req.getParameter("serviceTel");
		String email = req.getParameter("email");
		String operaterIdStr = req.getParameter("operaterId");
		String merchantIdStr = req.getParameter("merchantId");
		Long operaterId = Long.valueOf(operaterIdStr);
		Long merchantId = Long.valueOf(merchantIdStr);
		return merchantService.modifyMerchantInfo(merchantName, serveExplain, workAddress, minuteWorkAddress, charterCode, oraganizingCode, principal, linkMan, linkManPhone, serviceTel, email, operaterId, merchantId).toJson();
		
	}
	@SetUtf8
	@RequestMapping(value = "", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String modifyOrganizingInstitutionBarCodePic(HttpServletRequest req,HttpServletResponse res) {
		String operaterIdStr = req.getParameter("operaterId");
		String merchantIdStr = req.getParameter("merchantId");
		Long operaterId = Long.valueOf(operaterIdStr);
		Long merchantId = Long.valueOf(merchantIdStr);
		String result=null;
		try {
			File file=new File("file");
			InputStream is=req.getInputStream();
			OutputStream os=new FileOutputStream(file);
			byte temp[] = new byte[1024];  
            int size = -1;  
            while ((size = is.read(temp)) != -1) {   
                os.write(temp, 0, size);  
            }  
            os.close();
            result= merchantService.modifyOrganizingInstitutionBarCodePic(file, operaterId, merchantId).toJson();
		} catch (Exception e) {
			LogFactory.error(this,"上传组织机构代码证时发生异常");
			
		}
		return result;
	}
	@SetUtf8
	@RequestMapping(value = "", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String modifyBusinessLicensePic(HttpServletRequest req,HttpServletResponse res) {
		String operaterIdStr = req.getParameter("operaterId");
		String merchantIdStr = req.getParameter("merchantId");
		Long operaterId = Long.valueOf(operaterIdStr);
		Long merchantId = Long.valueOf(merchantIdStr);
		String result=null;
		try {
			File file=new File("file");
			InputStream is=req.getInputStream();
			OutputStream os=new FileOutputStream(file);
			byte temp[] = new byte[1024];  
            int size = -1;  
            while ((size = is.read(temp)) != -1) {   
                os.write(temp, 0, size);  
            }  
            os.close();
            result= merchantService.modifyBusinessLicensePic(file, operaterId, merchantId).toJson();
		} catch (Exception e) {
			LogFactory.error(this,"上传组织机构代码证时发生异常");
			
		}
		return result;
	}
	@SetUtf8
	@RequestMapping(value = "", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String modifyOtherPic(HttpServletRequest req,HttpServletResponse res) {
		String operaterIdStr = req.getParameter("operaterId");
		String merchantIdStr = req.getParameter("merchantId");
		Long operaterId = Long.valueOf(operaterIdStr);
		Long merchantId = Long.valueOf(merchantIdStr);
		String result=null;
		try {
			File file=new File("file");
			InputStream is=req.getInputStream();
			OutputStream os=new FileOutputStream(file);
			byte temp[] = new byte[1024];  
            int size = -1;  
            while ((size = is.read(temp)) != -1) {   
                os.write(temp, 0, size);  
            }  
            os.close();
            result= merchantService.modifyOtherPic(file, operaterId, merchantId).toJson();
		} catch (Exception e) {
			LogFactory.error(this,"上传组织机构代码证时发生异常");
			
		}
		return result;
	}

	//-------------------------------------------------------------------------------------------------------------------------------
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "destroyCode",produces = "application/json;charset=utf-8")
	public String destroyCode(HttpServletRequest req,HttpServletResponse res){
		String codIdStr=getArg.apply(req,"codeId");
		String operaterIdStr=getArg.apply(req,"operaterId");
		String codeStr=getArg.apply(req,"code");


		Long codeId=parseArg(codIdStr,Long.class);
		Long operaterId=parseArg(operaterIdStr,Long.class);
		Long code=parseArg(codeStr,Long.class);

		if(codeId==null)
			return codeService.destroyCode(code,null,operaterId).toJson();
		else
			return codeService.destroyCode(codeId,operaterId).toJson();
	}

	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "postoneCode",produces = "application/json;charset=utf-8")
	public String postoneCode(HttpServletRequest req,HttpServletResponse res){
		String codIdStr=getArg.apply(req,"codeId");
		String operaterIdStr=getArg.apply(req,"operaterId");
		Long codeId=parseArg(codIdStr,Long.class);
		Long operaterId=parseArg(operaterIdStr,Long.class);
		return codeService.postoneCode(codeId,operaterId).toJson();
	}

	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "queryCode",produces = "application/json;charset=utf-8")
	public String queryCode(HttpServletRequest req,HttpServletResponse res){
		String codIdStr=getArg.apply(req,"codeId");
		String operaterIdStr=getArg.apply(req,"operaterId");
		String codeStr=getArg.apply(req,"code");
		Long codeId=parseArg(codIdStr,Long.class);
		Long operaterId=parseArg(operaterIdStr,Long.class);
		Long code=parseArg(codeStr,Long.class);
		if(codeId==null)
			return codeService.couponCodeInfo(code,null,operaterId).toJson();
		else
			return codeService.couponCodeInfo(codeId,operaterId).toJson();
	}

	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "queryDailyRecords", produces = "application/json;charset=utf-8")
	public String queryDailyRecords(HttpServletRequest req,HttpServletResponse res){
		String timeStart=getArg.apply(req,"timeStart");
		String timeEnd=getArg.apply(req,"timeStop");
		String clearStatusStr=getArg.apply(req,"clearStatus");
		String pageNumStr=getArg.apply(req,"pageNum");
		String pageSizeStr=getArg.apply(req,"pageSize");
		String operaterIdStr=getArg.apply(req,"operaterId");

		ClearStatus clearStatus=ClearStatus.getType(clearStatusStr);
		Long operaterId=parseArg(operaterIdStr,Long.class);
		Integer pageNum=parseArg(pageNumStr,Integer.class);
		Integer pageSize=parseArg(pageSizeStr,Integer.class);

		return clearService.queryDailyRecords(timeStart,timeEnd,clearStatus,pageNum,pageSize,operaterId).toJson();
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
		return clearService.queryClearRecords(timeStart,timeEnd,clearStatus,invoiceStatus,pageNum,pageSize,operaterId).toJson();
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
	@RequestMapping(value = "queryOperateRecords", produces = "application/json;charset=utf-8")
	public String queryOperateRecords(HttpServletRequest req,HttpServletResponse res){
		String typeStr=getArg.apply(req,"type");
		String targetTypeStr=getArg.apply(req,"targetType");
		String accountTypeStr=getArg.apply(req,"accountType");
		String shopName=getArg.apply(req,"shopName");
		String timeStart=getArg.apply(req,"timeStart");
		String timeEnd=getArg.apply(req,"timeStop");
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
		return operateRecordService.queryMerchantOperateRecord(type,targetType,shopName,timeStart,timeEnd,accountType,name,phone,pageNum,pageSize,operaterId).toJson();
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
		String pageNumStr=getArg.apply(req,"pageNum");
		String pageSizeStr=getArg.apply(req,"pageSize");
		String operaterIdStr=getArg.apply(req,"operaterId");

		RequestOperateType operateType=RequestOperateType.getType(typeStr);
		RequestStatus status=RequestStatus.getType(statusStr);
		CommodityType commodityType=CommodityType.getType(commodityTypeStr);
		Long operaterId=parseArg(operaterIdStr,Long.class);
		Integer pageNum=parseArg(pageNumStr,Integer.class);
		Integer pageSize=parseArg(pageSizeStr,Integer.class);
		return requestService.queryRequests(timeStart,timeEnd,commodityName,operateType,status,commodityType,null,pageNum,pageSize,operaterId).toJson();
	}

	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "requestAddCommodity", produces = "application/json;charset=utf-8")
	public String requestAddCommodity(HttpServletRequest req,HttpServletResponse res){
		String commodityName=getArg.apply(req,"commodityName");
		String commodityTypeStr=getArg.apply(req,"commodityType");
		String facePriceStr=getArg.apply(req,"facePrice");
		String costPriceStr=getArg.apply(req,"costPrice");
		String sellPriceStr=getArg.apply(req,"sellPrice");
		String lifeTimeStr=getArg.apply(req,"lifeTime");
		String useDistription=getArg.apply(req,"useDistription");
		String operaterIdStr=getArg.apply(req,"operaterId");

		BigDecimal facePrice=toDecimalArg.apply(facePriceStr);
		BigDecimal costPrice=toDecimalArg.apply(costPriceStr);
		BigDecimal sellPrice=toDecimalArg.apply(sellPriceStr);
		Integer lifeTime=toIntArg.apply(lifeTimeStr);
		Long operaterId=toLongArg.apply(operaterIdStr);
		CommodityType commodityType=toCommodityTypeArg.apply(commodityTypeStr);
		return requestService.requestAddCommodity(commodityName,commodityType,facePrice,costPrice,sellPrice,lifeTime,useDistription,operaterId,null).toJson();
	}

	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "requestModifyCommodity", produces = "application/json;charset=utf-8")
	public String requestModifyCommodity(HttpServletRequest req,HttpServletResponse res){
		String facePriceStr=getArg.apply(req,"facePrice");
		String costPriceStr=getArg.apply(req,"costPrice");
		String sellPriceStr=getArg.apply(req,"sellPrice");
		String lifeTimeStr=getArg.apply(req,"lifeTime");
		String commodityIdStr=getArg.apply(req,"commodityId");
		String operaterIdStr=getArg.apply(req,"operaterId");

		BigDecimal facePrice=toDecimalArg.apply(facePriceStr);
		BigDecimal costPrice=toDecimalArg.apply(costPriceStr);
		BigDecimal sellPrice=toDecimalArg.apply(sellPriceStr);
		Integer lifeTime=toIntArg.apply(lifeTimeStr);
		Long operaterId=toLongArg.apply(operaterIdStr);
		Long commodityId=toLongArg.apply(commodityIdStr);
		return requestService.requestModifyCommodity(costPrice,facePrice,sellPrice,lifeTime,commodityId,operaterId).toJson();
	}

	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "requestShelveCommodity", produces = "application/json;charset=utf-8")
	public String requestShelveCommodity(HttpServletRequest req,HttpServletResponse res){
		String commodityIdStr=getArg.apply(req,"commodityId");
		String operaterIdStr=getArg.apply(req,"operaterId");

		Long operaterId=toLongArg.apply(operaterIdStr);
		Long commodityId=toLongArg.apply(commodityIdStr);
		return requestService.requestShelveCommodity(commodityId,operaterId).toJson();
	}

	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "requestUnshelveCommodity", produces = "application/json;charset=utf-8")
	public String requestUnshelveCommodity(HttpServletRequest req,HttpServletResponse res){
		String commodityIdStr=getArg.apply(req,"commodityId");
		String operaterIdStr=getArg.apply(req,"operaterId");

		Long operaterId=toLongArg.apply(operaterIdStr);
		Long commodityId=toLongArg.apply(commodityIdStr);
		return requestService.requestUnshelveCommodity(commodityId,operaterId).toJson();
	}

	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "queryRequest", produces = "application/json;charset=utf-8")
	public String queryRequest(HttpServletRequest req,HttpServletResponse res){
		String operaterIdStr=getArg.apply(req,"operaterId");
		String requestIdStr=getArg.apply(req,"requestId");
		Long operaterId=toLongArg.apply(operaterIdStr);
		Long requestId=toLongArg.apply(requestIdStr);
		return requestService.queryRequest(requestId,operaterId).toJson();
	}


}
