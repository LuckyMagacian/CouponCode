package com.lanxi.couponcode.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.lanxi.couponcode.spi.consts.annotations.SetUtf8;
import com.lanxi.couponcode.spi.consts.enums.AccountStatus;
import com.lanxi.couponcode.spi.consts.enums.AccountType;
import com.lanxi.couponcode.spi.consts.enums.MerchantStatus;
import com.lanxi.couponcode.spi.service.AccountService;
import com.lanxi.couponcode.spi.service.MerchantService;
import com.lanxi.couponcode.spi.service.ShopService;
import com.lanxi.util.entity.LogFactory;

/**
 * 管理员端
 * Created by yangyuanjian on 2017/11/20.
 */
@Controller
public class AdminController {
	@Resource
	private AccountService accountService;
	@Resource
	private MerchantService merchantService;
	@Resource
	private ShopService shopService;
	/*添加账户*/
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
		String shopIdStr=req.getParameter("shopId");
		String shopName=req.getParameter("shopName");
		Long shopId=Long.valueOf(shopIdStr);
		Long operaterId = Long.valueOf(operaterIdStr);
		Long merchantId = Long.valueOf(merchantIdStr);
		AccountType type=AccountType.getType(typeStr);
		return accountService.addAccount(type, userName, phone, merchantName, merchantId,shopId,shopName, operaterId).toJson();
	}
	/*账户查询*/
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
		String merchantIdStr=req.getParameter("merchantId");
		Long merchantId = Long.valueOf(merchantIdStr);
		String shopIdStr=req.getParameter("shopId");
		String shopName=req.getParameter("shopName");
		Long shopId=Long.valueOf(shopIdStr);
		return accountService.queryAccounts(merchantId,shopId,shopName,phone, merchantName, type, status, pageNum, pageSize, operaterId).toJson();
	}
	/*冻结账户*/
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
	/*开启账户*/
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
	/*删除账户*/
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
	/*查询商户详情*/
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "", produces = "application/json;charset=utf-8")
	public String queryMerchantInfo(HttpServletRequest req,HttpServletResponse res) {
		String operaterIdStr=req.getParameter("operaterId");
		String merchantIdStr=req.getParameter("merchantId");
		Long operaterId = Long.valueOf(operaterIdStr);
		Long merchantId=Long.valueOf(merchantIdStr);
		return merchantService.queryMerchantInfo(operaterId, merchantId).toJson();
	}
	/*查询商户下的所有门店*/
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "", produces = "application/json;charset=utf-8")
	public String querySubordinateShops(HttpServletRequest req,HttpServletResponse res) {
		String operaterIdStr=req.getParameter("operaterId");
		String merchantIdStr=req.getParameter("merchantId");
		Long operaterId = Long.valueOf(operaterIdStr);
		Long merchantId=Long.valueOf(merchantIdStr);
		String pageNumStr=req.getParameter("pageNum");
		String pageSizeStr=req.getParameter("pageSize");
		Integer pageNum=Integer.valueOf(pageNumStr);
		Integer pageSize=Integer.valueOf(pageSizeStr);
		return shopService.queryShops(merchantId, operaterId, pageNum, pageSize).toJson();
	}
	/*添加商户*/
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
	/*修改商户*/
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
	/*商户查询*/
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
	/*冻结商户*/
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
	/*开启商户*/
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
	/*导出商户*/
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
}
