package com.lanxi.couponcode.view;

import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.couponcode.spi.consts.annotations.LoginCheck;
import com.lanxi.couponcode.spi.consts.annotations.SetUtf8;
import com.lanxi.couponcode.spi.consts.enums.AccountType;
import com.lanxi.couponcode.spi.service.AccountService;
import com.lanxi.util.utils.LoggerUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.lanxi.couponcode.spi.assist.ArgAssist.getArg;
import static com.lanxi.couponcode.spi.assist.ArgAssist.parseArg;

/**
 * 门店管理端 Created by yangyuanjian on 2017/11/20.
 */
@Controller("shopView")
@RequestMapping("shop")
@EasyLog (LoggerUtil.LogLevel.INFO)
public class ShopController {
	@Resource(name="accountControllerServiceRef")
	private AccountService accountService;

	/* 门店管理员添加账户 */
	@SetUtf8
	@LoginCheck
	@ResponseBody
	@RequestMapping(value = "addAccount", produces = "application/json;charset=utf-8")
	public String addAccount(HttpServletRequest req, HttpServletResponse res) {
		String userName = getArg.apply(req, "userName");
		String phone = getArg.apply(req, "phone");
		String operaterIdStr = getArg.apply(req, "operaterId");
		String typeStr = getArg.apply(req, "type");
		Long operaterId = parseArg(operaterIdStr, Long.class);
		AccountType type = AccountType.getType(typeStr);
		return accountService.shopAddAccount(type, userName, phone, operaterId).toJson();
	}

	/* 门店管理员查询账户 */
	@SetUtf8
	@LoginCheck
	@ResponseBody
	@RequestMapping(value = "queryAccounts", produces = "application/json;charset=utf-8")
	public String queryAccounts(HttpServletRequest req, HttpServletResponse res) {
		String operaterIdStr = getArg.apply(req, "operaterId");
		Long operaterId = parseArg(operaterIdStr, Long.class);
		String pageNumStr = getArg.apply(req, "pageNum");
		String pageSizeStr = getArg.apply(req, "pageSize");
		Integer pageNum = parseArg(pageNumStr, Integer.class);
		Integer pageSize = parseArg(pageSizeStr, Integer.class);
		String shopIdStr = getArg.apply(req, "shopId");
		Long shopId = parseArg(shopIdStr, Long.class);
		return accountService.queryShopAccounts(shopId, operaterId, pageNum, pageSize).toJson();
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
	public String deleteAccount(HttpServletRequest req, HttpServletResponse res) {
		String operaterIdStr = getArg.apply(req, "operaterId");
		Long operaterId = parseArg(operaterIdStr, Long.class);
		String accountIdStr = getArg.apply(req, "accountId");
		Long accountId = parseArg(accountIdStr, Long.class);
		return accountService.delAccount(accountId, operaterId).toJson();
	}
}
