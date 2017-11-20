package com.lanxi.couponcode.impl.newcontroller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.impl.config.ConstConfig;
import com.lanxi.couponcode.impl.entity.Account;
import com.lanxi.couponcode.impl.newservice.AccountService;
import com.lanxi.couponcode.spi.consts.enums.AccountStatus;
import com.lanxi.couponcode.spi.consts.enums.AccountType;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.util.entity.LogFactory;

import javax.annotation.Resource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 
 * @author wuxiaobo
 *
 */
public class AccountServiceController implements com.lanxi.couponcode.spi.service.AccountService{
	@Resource
	private AccountService accountService;
	@Override
	public RetMessage<Boolean> addAccount(AccountType type, String name, String phone, String merchantName,
			Long merchantId, Long operaterId) {
		RetMessage<Boolean> retMessage=new RetMessage<Boolean>();
		Boolean result=false;
		//TODO 校验
		try {
			if(type!=null&&name!=null&&!name.isEmpty()&&phone!=null&&!phone.isEmpty()&&merchantName!=null) {
				Account account=new Account();
				account=new Account();
				account.setAccountId(IdWorker.getId());
				account.setAccountType(type);
				account.setLoginFailureNum(0);
				account.setLoginFailureTime("20171114033028");
				account.setUserName(name);
				account.setPhone(phone);
				account.setStatus(AccountStatus.normal);
				account.setMerchantName(merchantName);
				account.setMerchantId(merchantId);
				account.setAddTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
				account.setRequesterId(operaterId);
				result=accountService.addAccount(account);
				if(result) {
					
					retMessage.setRetCode(RetCodeEnum.success.getValue());
					retMessage.setRetMessage("添加账户成功");
					//TODO 添加操作记录
				}
				if(!result) {
					
					retMessage.setRetCode(RetCodeEnum.exception.getValue());
					retMessage.setRetMessage("添加账户失败");
				}
				retMessage.setDetail(result);
			}else {
				result=false;
				retMessage.setAll(RetCodeEnum.fail,"信息填写不完整", result);
			}
			
		} catch (Exception e) {
			LogFactory.error(this,"添加账户时发生异常",e);
			retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("添加账户时发生异常");
			retMessage.setDetail(result);
		}
		
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> merchantAddAccount(AccountType type, String name, String phone, String shopName,
			Long shopId, Long operaterId) {
		RetMessage<Boolean> retMessage=new RetMessage<Boolean>();
		Boolean result=false;
		//TODO 权限校验
		try {
			if(type!=null&&name!=null&&!name.isEmpty()&&phone!=null&&!phone.isEmpty()&&shopName!=null) {
				Account account=new Account();
				account.setAccountId(IdWorker.getId());
				account.setAccountType(type);
				account.setUserName(name);
				account.setLoginFailureNum(0);
				account.setLoginFailureTime("20171114033028");
				account.setPhone(phone);
				account.setStatus(AccountStatus.normal);
				account.setShopName(shopName);
				account.setShopId(shopId);
				account.setAddTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
				account.setRequesterId(operaterId);
				result=accountService.merchantAccount(account);
				if(result) {
					retMessage.setRetCode(RetCodeEnum.success.getValue());
					retMessage.setRetMessage("添加账户成功");
					//TODO 添加操作记录
				}
				if(!result) {
					retMessage.setRetCode(RetCodeEnum.exception.getValue());
					retMessage.setRetMessage("添加账户失败");
				}
				retMessage.setDetail(result);
			}else {
				result=false;
				retMessage.setAll(RetCodeEnum.fail,"信息填写不完整", result);
			}
			
		} catch (Exception e) {
			LogFactory.error(this,"添加账户时发生异常",e);
			retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("添加账户时发生异常");
			retMessage.setDetail(result);
		}
		
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> freezeAccount(Long accountId, Long operaterId) {
		RetMessage<Boolean> retMessage=new RetMessage<Boolean>();
		Boolean result=false;
		//TODO 权限校验
		try {
			if (accountId!=null) {
				Account account=new Account();
				account.setAccountId(accountId);
				account.setStatus(AccountStatus.freeze);
				result=accountService.freezeAccount(account);
				if(result) {
					retMessage.setRetCode(RetCodeEnum.success.getValue());
					retMessage.setRetMessage("冻结账户成功");
					//TODO 添加操作记录
				}
				if(!result) {
					retMessage.setRetCode(RetCodeEnum.exception.getValue());
					retMessage.setRetMessage("冻结账户失败");
				}
				retMessage.setDetail(result);
			}else {
				result=false;
				retMessage.setAll(RetCodeEnum.fail,"账户id不能为空",result);
			}
			
		} catch (Exception e) {
			LogFactory.error(this,"冻结账户时发生异常",e);
			retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("冻结账户时发生异常");
			retMessage.setDetail(result);
		}
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> unfreezeAccount(Long accountId, Long operaterId) {
		
		RetMessage<Boolean> retMessage=new RetMessage<Boolean>();
		Boolean result=false;
		//TODO 权限校验
		try {
			if (accountId!=null) {
				Account account=new Account();
				account.setAccountId(accountId);
				account.setStatus(AccountStatus.normal);
				result=accountService.unfreezeAccount(account);
				if(result) {
					retMessage.setRetCode(RetCodeEnum.success.getValue());
					retMessage.setRetMessage("开启账户成功");
					//TODO 添加操作记录
				}
				if(!result) {
					retMessage.setRetCode(RetCodeEnum.exception.getValue());
					retMessage.setRetMessage("开启账户失败");
				}
				retMessage.setDetail(result);
			}else {
				result=false;
				retMessage.setAll(RetCodeEnum.fail,"账户id不能为空",result);
			}
		} catch (Exception e) {
			LogFactory.error(this,"开启账户时发生异常",e);
			retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("开启账户时发生异常");
			retMessage.setDetail(result);
		}
		
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> delAccount(Long accountId, Long operaterId) {
		RetMessage<Boolean> retMessage=new RetMessage<Boolean>();
		Boolean result=false;
		//TODO 校验权限
		try {
			if (accountId!=null) {
				Account account=new Account();
				account.setAccountId(accountId);
				account.setStatus(AccountStatus.deleted);
				result=accountService.delAccount(account);
				if(result) {
					retMessage.setRetCode(RetCodeEnum.success.getValue());
					retMessage.setRetMessage("删除账户成功");
					//TODO 添加操作记录
				}
				if(!result) {
					retMessage.setRetCode(RetCodeEnum.exception.getValue());
					retMessage.setRetMessage("删除账户失败");
				}
				retMessage.setDetail(result);
			}else {
				result=false;
				retMessage.setAll(RetCodeEnum.fail,"账户id不能为空",result);
			}
			
		} catch (Exception e) {
			LogFactory.error(this,"删除账户时发生异常",e);
			retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("删除账户时发生异常");
			retMessage.setDetail(result);
		}
		return retMessage;
	}

	@Override
	public RetMessage<String> queryAccounts(String phone, String merchantName, AccountType type, AccountStatus status,
			Integer pageNum, Integer pageSize, Long operaterId) {
		RetMessage<String> retMessage=new RetMessage<String>();
		List<Account> accounts=null;
		//TODO 校验
		try {
			if(pageNum!=null) {
				pageSize=pageSize==null?ConstConfig.DEFAULT_PAGE_SIZE:pageSize;
			}
			Page<Account> pageObj=new Page<Account>(pageNum,pageSize);
			EntityWrapper<Account> wrapper=new EntityWrapper<Account>();
			if(phone!=null&&!phone.isEmpty()) {
				wrapper.eq("phone",phone);
			}
			if (merchantName!=null&&!merchantName.isEmpty()) {
				wrapper.eq("merchant_name",merchantName);
			}
			if (type!=null) {
				wrapper.eq("account_type",type);
			}
			if (status!=null) {
				wrapper.eq("status",status);
			}
			 LogFactory.info(this, "条件装饰结果["+wrapper+"]\n");
			accounts=accountService.queryAccounts(wrapper, pageObj);
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
			
		}
		
		return retMessage;
	}

	@Override
	public RetMessage<String> merchantQueryAccounts(String phone, String shopName, AccountType type,
			AccountStatus status, Integer pageNum, Integer pageSize, Long operaterId) {
		RetMessage<String> retMessage=new RetMessage<String>();
		List<Account> accounts=null;
		//TODO 校验
		try {
			if(pageNum!=null) {
				pageSize=pageSize==null?ConstConfig.DEFAULT_PAGE_SIZE:pageSize;
			}
			Page<Account> pageObj=new Page<>(pageNum,pageSize);
			EntityWrapper<Account> wrapper=new EntityWrapper<Account>();
			if(phone!=null&&!phone.isEmpty()) {
				wrapper.eq("phone",phone);
			}
			if (shopName!=null&&!shopName.isEmpty()) {
				wrapper.eq("shop_name",shopName);
			}
			if (type!=null) {
				wrapper.eq("account_type",type);
			}
			if (status!=null) {
				wrapper.eq("status",status);
			}
			 LogFactory.info(this, "条件装饰结果["+wrapper+"]\n");
			accounts=accountService.merchantQueryAccounts(wrapper, pageObj);
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
		}
		return retMessage;
	}

	@Override
	public RetMessage<String> queryAccountInfo(Long accountId, Long operaterId) {
		RetMessage<String> retMessage=new RetMessage<String>();
		Account account2=null;
		//TODO 校验
		try {
			if (accountId!=null) {
				Account account=new Account();
				account2=accountService.queryAccountInfo(account);
				if(account2!=null) {
					retMessage.setRetCode(RetCodeEnum.success.getValue());
					retMessage.setRetMessage("查询账户信息完毕");
				}else {
					retMessage.setRetCode(RetCodeEnum.exception.getValue());
					retMessage.setRetMessage("没有查询到任何数据");
				}
				String result=JSON.toJSONString(account2);
				retMessage.setDetail(result);
			}else {
				retMessage.setAll(RetCodeEnum.fail,"账户id为空", null);
			}
		} catch (Exception e) {
			LogFactory.error(this, "查询账户信息时出现异常",e);
    		retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("查询账户信息时发生异常");
			retMessage.setDetail(null);
		}
		return retMessage;
	}

	@Override
	public RetMessage<String> queryShopAccounts(Long shopId, Long operaterId,Integer pageNum,Integer pageSize) {
		RetMessage<String> retMessage=new RetMessage<String>();
		List<Account> accounts=null;
		//TODO 校验
		try {
			
			if (shopId!=null) {
				if(pageNum!=null) {
					pageSize=pageSize==null?ConstConfig.DEFAULT_PAGE_SIZE:pageSize;
				}
				Page<Account> pageObj=new Page<>(pageNum,pageSize);
				EntityWrapper<Account> wrapper=new EntityWrapper<Account>();
				if (shopId!=null) {
					wrapper.eq("shop_id",shopId);
				}
				 LogFactory.info(this, "条件装饰结果["+wrapper+"]\n");
				accounts=accountService.queryShopAccounts(wrapper, pageObj);
				if(accounts!=null&&accounts.size()>0) {
					retMessage.setRetCode(RetCodeEnum.success.getValue());
					retMessage.setRetMessage("查询完毕");
				}else {
					retMessage.setRetCode(RetCodeEnum.exception.getValue());
					retMessage.setRetMessage("没有查询到任何数据");
				}
				String result=JSON.toJSONString(accounts);
				retMessage.setDetail(result);
			}else {
				retMessage.setAll(RetCodeEnum.fail,"门店id为空", null);
			}
		} catch (Exception e) {
			LogFactory.error(this, "查询数据时出现异常",e);
    		retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("查询数据时发生异常");
			retMessage.setDetail(null);
			
		}
		
		return retMessage;
	}

}
