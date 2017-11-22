package com.lanxi.couponcode.view;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.lanxi.couponcode.spi.consts.annotations.SetUtf8;
import com.lanxi.couponcode.spi.consts.enums.AccountStatus;
import com.lanxi.couponcode.spi.consts.enums.AccountType;
import com.lanxi.couponcode.spi.service.AccountService;

/**
 * 管理员端
 * Created by yangyuanjian on 2017/11/20.
 */
@Controller
public class AdminController {
	@Resource
	private AccountService accountService;
	
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
}
