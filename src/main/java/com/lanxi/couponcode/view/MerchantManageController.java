package com.lanxi.couponcode.view;

import com.lanxi.couponcode.spi.consts.annotations.SetUtf8;

import com.lanxi.couponcode.spi.consts.enums.MerchantStatus;
import com.lanxi.couponcode.spi.service.MerchantService;
import com.lanxi.util.entity.LogFactory;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 商户管理端 Created by yangyuanjian on 2017/11/20.
 */
@Controller
public class MerchantManageController {
	@Resource
	private MerchantService merchantService;

	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "", produces = "application/json;charset=utf-8")
	public String addMerchant(HttpServletRequest req, HttpServletResponse res) {
		String merchantName = req.getParameter("merchantName");
		String workAddress = req.getParameter("workAddress");
		String detailAddress = req.getParameter("minuteWorkAddress");
		String operaterIdStr = req.getParameter("operaterId");
		Long operaterId = Long.valueOf(operaterIdStr);
		return merchantService.addMerchant(merchantName, workAddress, detailAddress, operaterId).toJson();
	}

	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "", produces = "application/json;charset=utf-8")
	public String modifyMerchant(HttpServletRequest req, HttpServletResponse res) {
		String merchantName = req.getParameter("merchantName");
		String workAddress = req.getParameter("workAddress");
		String detailAddress = req.getParameter("minuteWorkAddress");
		String operaterIdStr = req.getParameter("operaterId");
		String merchantIdStr = req.getParameter("merchantId");
		Long operaterId = Long.valueOf(operaterIdStr);
		Long merchantId = Long.valueOf(merchantIdStr);
		return merchantService.modifyMerchant(merchantName, workAddress, detailAddress, operaterId, merchantId)
				.toJson();

	}

	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "", produces = "application/json;charset=utf-8")
	public String queryMerchant(HttpServletRequest req, HttpServletResponse res) {
		String merchantName = req.getParameter("merchantName");
		String merchantStatusStr = req.getParameter("merchantStatus");
		String timeStop = req.getParameter("timeStop");
		String timeStart = req.getParameter("timeStart");
		String pageNumStr = req.getParameter("pageNum");
		String pageSizeStr = req.getParameter("pageSize");
		String operaterIdStr = req.getParameter("operaterId");
		Integer pageNum = Integer.valueOf(pageNumStr);
		Integer pageSize = Integer.valueOf(pageSizeStr);
		Long operaterId = Long.valueOf(operaterIdStr);
		MerchantStatus merchantStatus = MerchantStatus.getType(merchantStatusStr);
		return merchantService
				.queryMerchants(merchantName, merchantStatus, timeStart, timeStop, pageNum, pageSize, operaterId)
				.toJson();

	}

	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "", produces = "application/json;charset=utf-8")
	public String freezeMerchant(HttpServletRequest req, HttpServletResponse res) {
		String operaterIdStr = req.getParameter("operaterId");
		String merchantIdStr = req.getParameter("merchantId");
		Long operaterId = Long.valueOf(operaterIdStr);
		Long merchantId = Long.valueOf(merchantIdStr);
		return merchantService.disableMerchant(merchantId, operaterId).toJson();
	}

	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "", produces = "application/json;charset=utf-8")
	public String unfreezeMerchant(HttpServletRequest req, HttpServletResponse res) {
		String operaterIdStr = req.getParameter("operaterId");
		String merchantIdStr = req.getParameter("merchantId");
		Long operaterId = Long.valueOf(operaterIdStr);
		Long merchantId = Long.valueOf(merchantIdStr);
		return merchantService.enableMerchant(merchantId, operaterId).toJson();
	}

	@RequestMapping(value = "")
	public void queryMerchantsExport(HttpServletRequest req, HttpServletResponse res) {
		String merchantName = req.getParameter("merchantName");
		String timeStart = req.getParameter("timeStart");
		String timeStop = req.getParameter("timeStop");
		String operaterIdStr = req.getParameter("operaterId");
		String merchantStatusStr = req.getParameter("merchantStatus");
		Long operaterId = Long.valueOf(operaterIdStr);
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
	
}
