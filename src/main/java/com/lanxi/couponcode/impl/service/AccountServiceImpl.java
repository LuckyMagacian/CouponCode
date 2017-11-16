package com.lanxi.couponcode.impl.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.lanxi.couponcode.impl.entity.Account;
import com.lanxi.couponcode.impl.entity.OperateRecord;
import com.lanxi.couponcode.spi.consts.enums.AccountStatus;
import com.lanxi.couponcode.spi.consts.enums.AccountType;
import com.lanxi.couponcode.spi.consts.enums.OperateTargetType;
import com.lanxi.util.entity.LogFactory;
import com.lanxi.util.utils.TimeUtil;
@Service("accountService")
public class AccountServiceImpl implements AccountService{
	@Resource
	private DaoService dao;
	@Override
	public Boolean addAccount(AccountType accountType, String name, String phone, String merchantName, Long merchantId,
			Long operaterId) {
		String locker="accountType["+accountType+"]\n"+
			    ",name["+name+"]\n"+
                ",phone["+phone+"]\n"+
                ",merchantName["+merchantName+"]\n"+
                ",merchantId["+merchantId+"]\n"+
                ",operaterId["+operaterId+"]\n";
		LogFactory.info(this, "尝试添加商户账户,\n"+locker);
		OperateRecord record=null;
		boolean result=false;
		Account account=null;
		try {
			if(accountType!=null&&name!=null&&!name.isEmpty()&&phone!=null&&!phone.isEmpty()&&merchantName!=null) {
				account=new Account();
				account.setAccountId(IdWorker.getId());
				account.setAccountType(accountType);
				account.setLoginFailureNum(0);
				account.setLoginFailureTime("20171114033028");
				account.setUserName(name);
				account.setPhone(phone);
				account.setStatus(AccountStatus.normal);
				account.setMerchantName(merchantName);
				account.setMerchantId(merchantId);
				account.setAddTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
				account.setRequesterId(operaterId);
				Integer var=dao.getAccountDao().insert(account);
				if(var<0) {
					result=false;
					LogFactory.warn(this,"添加商户账户失败\n"+locker);
				}else if (var>0) {
					result=true;
					LogFactory.info(this,"添加商户账户成功\n"+locker);
					record=new OperateRecord();
					record.setRecordId(IdWorker.getId());
					record.setOperaterId(operaterId);
					record.setTargetType(OperateTargetType.account);
					record.setDescription("添加商户账户");
					record.setOperateTime(TimeUtil.getDateTime());
					record.setTargetInfo(account.toJson());
					LogFactory.info(this,"尝试添加商户账户操作记录\n"+locker);
					boolean flag=record.insert();
		            LogFactory.info(this,"添加商户账户操作记录["+record+"]结果["+flag+"],\n"+locker); 
				}
			}
		
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this,"添加商户账户时发生异常\n"+locker,e);
		}
		// TODO Auto-generated method stub
		return result;
	}
	@Override
	public Boolean merchantAccount(AccountType accountType, String name, String phone, String shopName, Long shopId,
			Long operaterId) {
		String locker="accountType["+accountType+"]\n"+
			    ",name["+name+"]\n"+
                ",phone["+phone+"]\n"+
                ",shopName["+shopName+"]\n"+
                ",shopId["+shopId+"]\n"+
                ",operaterId["+operaterId+"]\n";
		LogFactory.info(this, "尝试添加门店账户,\n"+locker);
		OperateRecord record=null;
		boolean result=false;
		Account account=null;
		try {
			if(accountType!=null&&name!=null&&!name.isEmpty()&&phone!=null&&!phone.isEmpty()&&shopName!=null) {
				account=new Account();
				account.setAccountId(IdWorker.getId());
				account.setAccountType(accountType);
				account.setUserName(name);
				account.setLoginFailureNum(0);
				account.setLoginFailureTime("0");
				account.setPhone(phone);
				account.setStatus(AccountStatus.normal);
				account.setShopName(shopName);
				account.setShopId(shopId);
				account.setAddTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
				account.setRequesterId(operaterId);
				Integer var=dao.getAccountDao().insert(account);
				if(var<0) {
					result=false;
					LogFactory.warn(this,"添加门店账户失败\n"+locker);
				}else if (var>0) {
					result=true;
					LogFactory.info(this,"添加门店账户成功\n"+locker);
					record=new OperateRecord();
					record.setRecordId(IdWorker.getId());
					record.setOperaterId(operaterId);
					record.setTargetType(OperateTargetType.account);
					record.setDescription("添加门店账户");
					record.setOperateTime(TimeUtil.getDateTime());
					record.setTargetInfo(account.toJson());
					LogFactory.info(this,"尝试添加门店账户操作记录\n"+locker);
					boolean flag=record.insert();
		            LogFactory.info(this,"添加门店账户操作记录["+record+"]结果["+flag+"],\n"+locker); 
				}
			}
		
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this,"添加门店账户时发生异常\n"+locker,e);
		}
		// TODO Auto-generated method stub
		return result;
	}
	
	@Override
	public Boolean freezeAccount(Long accountId, Long operaterId) {
		String locker="accountId["+accountId+"]\n"+
				",operaterId["+operaterId+"]";
		LogFactory.info(this,"尝试冻结账号\n"+locker);
		Boolean result=false;
		Account account=null;
		OperateRecord record=null;
		try {
			if(accountId!=null) {
				account=new Account();
				account.setAccountId(accountId);
				account.setStatus(AccountStatus.freeze);
				Integer var=dao.getAccountDao().updateById(account);
				if(var<0) {
					result=false;
					LogFactory.warn(this,"冻结账户失败\n"+locker);
				}else if (var>0) {
					result=true;
					LogFactory.info(this,"冻结账户成功\n"+locker);
					LogFactory.info(this,"尝试添加冻结账户操作记录\n"+locker);
					record=new OperateRecord();
					record=new OperateRecord();
					record.setRecordId(IdWorker.getId());
					record.setOperaterId(operaterId);
					record.setTargetType(OperateTargetType.account);
					record.setDescription("冻结账户");
					record.setOperateTime(TimeUtil.getDateTime());
					record.setTargetInfo(account.toJson());
					
					boolean flag=record.insert();
		            LogFactory.info(this,"添加冻结账户操作记录["+record+"]结果["+flag+"],\n"+locker); 
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this,"冻结账户时发生异常\n"+locker,e);
		}
		
		// TODO Auto-generated method stub
		return result;
	}
	@Override
	public Boolean unfreezeAccount(Long accountId, Long operaterId) {
		String locker="accountId["+accountId+"]\n"+
				",operaterId["+operaterId+"]\n";
		LogFactory.info(this,"尝试开启账户\n"+locker);
		Boolean result=false;
		Account account=null;
		OperateRecord record=null;
		try {
			if(accountId!=null) {
				account=new Account();
				account.setAccountId(accountId);
				account.setStatus(AccountStatus.normal);
				Integer var=dao.getAccountDao().updateById(account);
				if(var<0) {
					result=false;
					LogFactory.warn(this,"开启账户失败\n"+locker);
				}else if (var>0) {
					result=true;
					LogFactory.info(this,"开启账户成功\n"+locker);
					LogFactory.info(this,"尝试开启冻结账户操作记录\n"+locker);
					record=new OperateRecord();
					record=new OperateRecord();
					record.setRecordId(IdWorker.getId());
					record.setOperaterId(operaterId);
					record.setTargetType(OperateTargetType.account);
					record.setDescription("开启账户");
					record.setOperateTime(TimeUtil.getDateTime());
					record.setTargetInfo(account.toJson());
					
					boolean flag=record.insert();
		            LogFactory.info(this,"添加开启账户操作记录["+record+"]结果["+flag+"],\n"+locker); 
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this,"开启账户时发生异常\n"+locker,e);
		}
		
		// TODO Auto-generated method stub
		return result;
	}
	@Override
	public Boolean delAccount(Long accountId, Long operaterId) {
		String locker="accountId["+accountId+"]\n"+
				",operaterId["+operaterId+"]\n";
		LogFactory.info(this,"尝试删除账号\n"+locker);
		Boolean result=false;
		Account account=null;
		OperateRecord record=null;
		try {
			if(accountId!=null) {
				account=new Account();
				account.setAccountId(accountId);
				account.setStatus(AccountStatus.deleted);
				Integer var=dao.getAccountDao().updateById(account);
				if(var<0) {
					result=false;
					LogFactory.warn(this,"删除账户失败\n"+locker);
				}else if (var>0) {
					result=true;
					LogFactory.info(this,"删除账户成功\n"+locker);
					LogFactory.info(this,"尝试添加删除账户操作记录\n"+locker);
					record=new OperateRecord();
					record.setRecordId(IdWorker.getId());
					record.setOperaterId(operaterId);
					record.setTargetType(OperateTargetType.account);
					record.setDescription("删除账户");
					record.setOperateTime(TimeUtil.getDateTime());
					record.setTargetInfo(account.toJson());
					
					boolean flag=record.insert();
		            LogFactory.info(this,"添加删除账户操作记录["+record+"]结果["+flag+"],\n"+locker); 
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this,"删除账户时发生异常\n"+locker,e);
		}
		
		// TODO Auto-generated method stub
		return result;
	}
	@Override
	public List<Account> queryAccounts(String phone, String merchantName, AccountType type, AccountStatus status,
			Page<Account> pageObj, Long operaterId) {
		String locker="phone["+phone+"]\n"+
				",merchantName["+merchantName+"]\n"+
				",AccountType["+type+"]\n"+
				",AccountStatus["+status+"]\n"+
				",operaterId["+operaterId+"]\n";
		LogFactory.info(this,"尝试获取账户信息/n"+locker);
		List<Account>list=null;
		try {
			//组装查询条件
			EntityWrapper<Account> wrapper=new EntityWrapper<Account>();
			if (pageObj!=null) {
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
				 LogFactory.info(this, "条件装饰结果["+wrapper+"]\n"+locker);
				 list=dao.getAccountDao().selectPage(pageObj, wrapper);
				 LogFactory.debug(this,"查询到的结果["+list+"]\n"+locker);
				 LogFactory.info(this,"查询到的总记录数["+list.size()+"]\n"+locker);
			}
			return list;
			 
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this,"获取账户信息时发生异常\n"+locker,e);
			return list;
		}
	}
	@Override
	public List<Account> merchantQueryAccounts(String phone, String shopName, AccountType type, AccountStatus status,
			Page<Account> pageObj, Long operaterId) {
		String locker="phone["+phone+"]\n"+
				",shopName["+shopName+"]\n"+
				",AccountType["+type+"]\n"+
				",AccountStatus["+status+"]\n"+
				",operaterId["+operaterId+"]\n";
		LogFactory.info(this,"尝试获取账户信息\n"+locker);
		List<Account>list=null;
		try {
			//组装查询条件
			EntityWrapper<Account> wrapper=new EntityWrapper<Account>();
			if (pageObj!=null) {
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
				 LogFactory.info(this, "条件装饰结果["+wrapper+"]\n"+locker);
				 list=dao.getAccountDao().selectPage(pageObj, wrapper);
				 LogFactory.debug(this,"查询到的结果["+list+"]\n"+locker);
				 LogFactory.info(this,"查询到的总记录数["+list.size()+"]\n"+locker);
			}
			return list;
			 
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this,"获取账户信息时发生异常\n"+locker,e);
			return list;
		}
	}
	@Override
	public List<Account> queryShopAccounts(Long shopId, Long operaterId) {
		String locker="shopId["+shopId+"]\n"+
				",operaterId["+operaterId+"]";
		LogFactory.info(this,"尝试获取账户信息\n"+locker);
		List<Account>list=null;
		try {
			//组装查询条件
			EntityWrapper<Account> wrapper=new EntityWrapper<Account>();
				if (shopId!=null) {
					wrapper.eq("shop_id",shopId);
				}
				 LogFactory.info(this, "条件装饰结果["+wrapper+"]\n"+locker);
				 list=dao.getAccountDao().selectList(wrapper);
				 LogFactory.debug(this,"查询到的结果["+list+"]\n"+locker);
				 LogFactory.info(this,"查询到的总记录数["+list.size()+"]\n"+locker);
				 return list;
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this,"获取账户信息时发生异常\n"+locker,e);
			return list;
		}
	}
	@Override
	public Account queryAccountInfo(Long accountId, Long operaterId) {
		String locker="accountId["+accountId+"]\n"+
				",operaterId["+operaterId+"]";
		LogFactory.info(this,"尝试根据账户id获取账户信息\n"+locker);
		Account account=null;
		try {
			if(accountId!=null) {
				account=dao.getAccountDao().selectById(accountId);
				LogFactory.debug(this,"查询结果["+account+"]\n"+locker);
			}
			return account;
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this,"根据账户id获取账户信息时发生异常\n"+locker);
			return account;
		}
	
	}
	@Override
	public Boolean phoneVerify(String phone, Long operaterId) {
		String locker="phone["+phone+"]\n"+
				",operaterId["+operaterId+"]";
		LogFactory.info(this,"查询手机号码是否已注册\n"+locker);
		Boolean result=false;
		List<Account> list=null;
		try {
			EntityWrapper<Account> wrapper=new EntityWrapper<Account>();
			if(phone!=null&&!phone.isEmpty()) {
				wrapper.eq("phone", phone);
			}
			list=dao.getAccountDao().selectList(wrapper);
			if(list.size()>0) {
				LogFactory.debug(this,"此手机号码已经被注册过\n"+locker);
				result=false;
			}else if (list.size()==0) {
				LogFactory.debug(this,"此手机号码可用\n"+locker);
				result=true;
			}
			
		} catch (Exception e) {
			LogFactory.error(this,"查询手机号码是否已注册时发生异常\n"+locker,e);
			result=false;
			// TODO: handle exception
		}
		// TODO Auto-generated method stub
		return result;
	}
	

}
