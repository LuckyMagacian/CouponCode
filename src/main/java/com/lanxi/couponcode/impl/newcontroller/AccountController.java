package com.lanxi.couponcode.impl.newcontroller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.impl.config.ConstConfig;
import com.lanxi.couponcode.impl.entity.Account;
import com.lanxi.couponcode.impl.newservice.AccountService;
import com.lanxi.couponcode.spi.consts.enums.AccountStatus;
import com.lanxi.couponcode.spi.consts.enums.AccountType;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.util.entity.LogFactory;

import javax.annotation.Resource;
import java.util.List;

/**
 * 
 * @author wuxiaobo
 *
 */
public class AccountController implements com.lanxi.couponcode.spi.service.AccountService{
	@Resource
	private AccountService accountService;
	@Override
	public RetMessage<Boolean> addAccount(AccountType type, String name, String phone, String merchantName,
			Long merchantId, Long operaterId) {
		RetMessage<Boolean> retMessage=new RetMessage<Boolean>();
		Boolean result=false;
		try {
			result=accountService.addAccount(type, name, phone, merchantName, merchantId, operaterId);
			if(result) {
				retMessage.setRetCode(RetCodeEnum.success.getValue());
				retMessage.setRetMessage("添加账户成功");
			}
			if(!result) {
				retMessage.setRetCode(RetCodeEnum.exception.getValue());
				retMessage.setRetMessage("添加账户失败");
			}
			retMessage.setDetail(result);
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this,"添加账户时发生异常",e);
			retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("添加账户时发生异常");
			retMessage.setDetail(result);
		}
		// TODO Auto-generated method stub
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> merchantAddAccount(AccountType type, String name, String phone, String shopName,
			Long shopId, Long operaterId) {
		RetMessage<Boolean> retMessage=new RetMessage<Boolean>();
		Boolean result=false;
		try {
			result=accountService.merchantAccount(type, name, phone, shopName, shopId, operaterId);
			if(result) {
				retMessage.setRetCode(RetCodeEnum.success.getValue());
				retMessage.setRetMessage("添加账户成功");
			}
			if(!result) {
				retMessage.setRetCode(RetCodeEnum.exception.getValue());
				retMessage.setRetMessage("添加账户失败");
			}
			retMessage.setDetail(result);
			
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this,"添加账户时发生异常",e);
			retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("添加账户时发生异常");
			retMessage.setDetail(result);
		}
		// TODO Auto-generated method stub
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> freezeAccount(Long accountId, Long operaterId) {
		RetMessage<Boolean> retMessage=new RetMessage<Boolean>();
		Boolean result=false;
		try {
			result=accountService.freezeAccount(accountId, operaterId);
			if(result) {
				retMessage.setRetCode(RetCodeEnum.success.getValue());
				retMessage.setRetMessage("冻结账户成功");
			}
			if(!result) {
				retMessage.setRetCode(RetCodeEnum.exception.getValue());
				retMessage.setRetMessage("冻结账户失败");
			}
			retMessage.setDetail(result);
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this,"冻结账户时发生异常",e);
			retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("冻结账户时发生异常");
			retMessage.setDetail(result);
		}
		// TODO Auto-generated method stub
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> unfreezeAccount(Long accountId, Long operaterId) {
		
		RetMessage<Boolean> retMessage=new RetMessage<Boolean>();
		Boolean result=false;
		try {
			result=accountService.unfreezeAccount(accountId, operaterId);
			if(result) {
				retMessage.setRetCode(RetCodeEnum.success.getValue());
				retMessage.setRetMessage("开启账户成功");
			}
			if(!result) {
				retMessage.setRetCode(RetCodeEnum.exception.getValue());
				retMessage.setRetMessage("开启账户失败");
			}
			retMessage.setDetail(result);
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this,"开启账户时发生异常",e);
			retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("开启账户时发生异常");
			retMessage.setDetail(result);
		}
		// TODO Auto-generated method stub
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> delAccount(Long accountId, Long operaterId) {
		RetMessage<Boolean> retMessage=new RetMessage<Boolean>();
		Boolean result=false;
		try {
			result=accountService.delAccount(accountId, operaterId);
			if(result) {
				retMessage.setRetCode(RetCodeEnum.success.getValue());
				retMessage.setRetMessage("删除账户成功");
			}
			if(!result) {
				retMessage.setRetCode(RetCodeEnum.exception.getValue());
				retMessage.setRetMessage("删除账户失败");
			}
			retMessage.setDetail(result);
		} catch (Exception e) {
			LogFactory.error(this,"删除账户时发生异常",e);
			retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("删除账户时发生异常");
			retMessage.setDetail(result);
			// TODO: handle exception
		}
		// TODO Auto-generated method stub
		return retMessage;
	}

	@Override
	public RetMessage<String> queryAccounts(String phone, String merchantName, AccountType type, AccountStatus status,
			Integer pageNum, Integer pageSize, Long operaterId) {
		RetMessage<String> retMessage=new RetMessage<String>();
		List<Account> accounts=null;
		try {
			if(pageNum!=null) {
				pageSize=pageSize==null?ConstConfig.DEFAULT_PAGE_SIZE:pageSize;
			}
			Page<Account> pageObj=new Page<Account>(pageNum,pageSize);
			accounts=accountService.queryAccounts(phone, merchantName, type, status, pageObj, operaterId);
			if(accounts!=null&&accounts.size()>0) {
				retMessage.setRetCode(RetCodeEnum.success.getValue());
				retMessage.setRetMessage("查询完毕");
			}else {
				retMessage.setRetCode(RetCodeEnum.exception.getValue());
				retMessage.setRetMessage("没有查询到任何数据");
			}
			String result=JSON.toJSONString(accounts);
			retMessage.setDetail(result);
		} catch (Exception e) {
			LogFactory.error(this, "查询数据时出现异常",e);
    		retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("查询数据时发生异常");
			retMessage.setDetail(null);
			// TODO: handle exception
		}
		// TODO Auto-generated method stub
		return retMessage;
	}

	@Override
	public RetMessage<String> merchantQueryAccounts(String phone, String shopName, AccountType type,
			AccountStatus status, Integer pageNum, Integer pageSize, Long operaterId) {
		RetMessage<String> retMessage=new RetMessage<String>();
		List<Account> accounts=null;
		try {
			if(pageNum!=null) {
				pageSize=pageSize==null?ConstConfig.DEFAULT_PAGE_SIZE:pageSize;
			}
			Page<Account> pageObj=new Page<>(pageNum,pageSize);
			accounts=accountService.merchantQueryAccounts(phone, shopName, type, status, pageObj, operaterId);
			if(accounts!=null&&accounts.size()>0) {
				retMessage.setRetCode(RetCodeEnum.success.getValue());
				retMessage.setRetMessage("查询完毕");
			}else {
				retMessage.setRetCode(RetCodeEnum.exception.getValue());
				retMessage.setRetMessage("没有查询到任何数据");
			}
			String result=JSON.toJSONString(accounts);
			retMessage.setDetail(result);
		} catch (Exception e) {
			LogFactory.error(this, "查询数据时出现异常",e);
    		retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("查询数据时发生异常");
			retMessage.setDetail(null);
			// TODO: handle exception
		}
		// TODO Auto-generated method stub
		return retMessage;
	}

	@Override
	public RetMessage<String> queryAccountInfo(Long accountId, Long operaterId) {
		RetMessage<String> retMessage=new RetMessage<String>();
		Account account=null;
		try {
			account=accountService.queryAccountInfo(accountId, operaterId);
			if(account!=null) {
				retMessage.setRetCode(RetCodeEnum.success.getValue());
				retMessage.setRetMessage("查询账户信息完毕");
			}else {
				retMessage.setRetCode(RetCodeEnum.exception.getValue());
				retMessage.setRetMessage("没有查询到任何数据");
			}
			String result=JSON.toJSONString(account);
			retMessage.setDetail(result);
		} catch (Exception e) {
			LogFactory.error(this, "查询账户信息时出现异常",e);
    		retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("查询账户信息时发生异常");
			retMessage.setDetail(null);
			// TODO: handle exception
		}
		// TODO Auto-generated method stub
		return retMessage;
	}

	@Override
	public RetMessage<String> queryShopAccounts(Long shopId, Long operaterId) {
		RetMessage<String> retMessage=new RetMessage<String>();
		List<Account> accounts=null;
		try {
			accounts=accountService.queryShopAccounts(shopId, operaterId);
			if(accounts!=null&&accounts.size()>0) {
				retMessage.setRetCode(RetCodeEnum.success.getValue());
				retMessage.setRetMessage("查询完毕");
			}else {
				retMessage.setRetCode(RetCodeEnum.exception.getValue());
				retMessage.setRetMessage("没有查询到任何数据");
			}
			String result=JSON.toJSONString(accounts);
			retMessage.setDetail(result);
		} catch (Exception e) {
			LogFactory.error(this, "查询数据时出现异常",e);
    		retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("查询数据时发生异常");
			retMessage.setDetail(null);
			// TODO: handle exception
		}
		// TODO Auto-generated method stub
		return retMessage;
	}

}
