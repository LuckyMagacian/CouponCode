package com.lanxi.couponcode.impl.newservice;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.lanxi.couponcode.impl.entity.Account;
import com.lanxi.couponcode.impl.entity.OperateRecord;
import com.lanxi.couponcode.spi.consts.enums.AccountStatus;
import com.lanxi.couponcode.spi.consts.enums.AccountType;
import com.lanxi.couponcode.spi.consts.enums.OperateTargetType;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.util.entity.LogFactory;
import com.lanxi.util.utils.TimeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service("accountService")
public class AccountServiceImpl implements AccountService{
	@Resource
	private DaoService dao;
	@Override
	public Boolean addAccount(Account account) {
		LogFactory.info(this, "尝试添加商户账户,\n");
		boolean result=false;
		try {
				if (account!=null) {
					Integer var=dao.getAccountDao().insert(account);
					if(var<0) {
						result=false;
						LogFactory.warn(this,"添加商户账户失败\n");
					}else if (var>0) {
						result=true;
						LogFactory.info(this,"添加商户账户成功\n");
					}
				}else {
					result=false;
					LogFactory.warn(this,"信息为空");
				}
				
		} catch (Exception e) {
			LogFactory.error(this,"添加商户账户时发生异常\n",e);
		}
		return result;
	}
	@Override
	public Boolean merchantAccount(Account account) {
		LogFactory.info(this, "尝试添加门店账户,\n");
		boolean result=false;
		try {
			if (account!=null) {
				Integer var=dao.getAccountDao().insert(account);
				if(var<0) {
					result=false;
					LogFactory.warn(this,"添加门店账户失败\n");
				}else if (var>0) {
					result=true;
					LogFactory.info(this,"添加门店账户成功\n");
				}
			}else {
				result=false;
				LogFactory.warn(this,"信息为空");
			}	
		} catch (Exception e) {
			LogFactory.error(this,"添加门店账户时发生异常\n",e);
		}
		return result;
	}
	@Override
	public Boolean freezeAccount(Account account) {
		LogFactory.info(this,"尝试冻结账号\n");
		Boolean result=false;
		try {
			if(account!=null) {
				Integer var=dao.getAccountDao().updateById(account);
				if(var<0) {
					result=false;
					LogFactory.warn(this,"冻结账户失败\n");
				}else if (var>0) {
					result=true;
					LogFactory.info(this,"冻结账户成功\n");
				}
			}else {
				result=false;
				LogFactory.warn(this,"信息为空");
			}
		} catch (Exception e) {
			LogFactory.error(this,"冻结账户时发生异常\n",e);
		}
		return result;
	}
	@Override
	public Boolean unfreezeAccount(Account account) {
		
		LogFactory.info(this,"尝试开启账户\n");
		Boolean result=false;
	
		try {
			if(account!=null) {
				Integer var=dao.getAccountDao().updateById(account);
				if(var<0) {
					result=false;
					LogFactory.warn(this,"开启账户失败\n");
				}else if (var>0) {
					result=true;
					LogFactory.info(this,"开启账户成功\n");
				}
			}else {
				result=false;
				LogFactory.warn(this,"信息为空");
			}
		} catch (Exception e) {
			LogFactory.error(this,"开启账户时发生异常\n",e);
		}
		return result;
	}
	@Override
	public Boolean delAccount(Account account) {
		
		LogFactory.info(this,"尝试删除账号\n");
		Boolean result=false;
		try {
			if(account!=null) {
				Integer var=dao.getAccountDao().updateById(account);
				if(var<0) {
					result=false;
					LogFactory.warn(this,"删除账户失败\n");
				}else if (var>0) {
					result=true;
					LogFactory.info(this,"删除账户成功\n");
				}
			}else {
				result=false;
				LogFactory.warn(this,"信息为空");
			}
		} catch (Exception e) {
			LogFactory.error(this,"删除账户时发生异常\n",e);
		}
		return result;
	}
	@Override
	public List<Account> queryAccounts(EntityWrapper<Account> wrapper,
			Page<Account> pageObj) {
		LogFactory.info(this,"尝试获取账户信息/n");
		List<Account>list=null;
		try {
			if (pageObj!=null) {
				 list=dao.getAccountDao().selectPage(pageObj, wrapper);
				 LogFactory.debug(this,"查询到的结果["+list+"]\n");
				 LogFactory.info(this,"查询到的总记录数["+list.size()+"]\n");
			}
			return list;
		} catch (Exception e) {
			LogFactory.error(this,"获取账户信息时发生异常\n",e);
			return list;
		}
	}
	@Override
	public List<Account> merchantQueryAccounts(EntityWrapper<Account> wrapper,
			Page<Account> pageObj) {
		LogFactory.info(this,"尝试获取账户信息\n");
		List<Account>list=null;
		try {
			if (pageObj!=null) {
				 list=dao.getAccountDao().selectPage(pageObj, wrapper);
				 LogFactory.debug(this,"查询到的结果["+list+"]\n");
				 LogFactory.info(this,"查询到的总记录数["+list.size()+"]\n");
			}
			return list;
		} catch (Exception e) {
			LogFactory.error(this,"获取账户信息时发生异常\n",e);
			return list;
		}
	}
	@Override
	public List<Account> queryShopAccounts(EntityWrapper<Account> wrapper,
			Page<Account> pageObj) {
		LogFactory.info(this,"尝试获取账户信息\n");
		List<Account>list=null;
		try {
				 list=dao.getAccountDao().selectList(wrapper);
				 LogFactory.debug(this,"查询到的结果["+list+"]\n");
				 LogFactory.info(this,"查询到的总记录数["+list.size()+"]\n");
				 return list;
		} catch (Exception e) {
			LogFactory.error(this,"获取账户信息时发生异常\n",e);
			return list;
		}
	}
	@Override
	public Account queryAccountInfo(Account account) {
		LogFactory.info(this,"尝试根据账户id获取账户信息\n");
		try {
			if(account!=null) {
				account=dao.getAccountDao().selectOne(account);
				LogFactory.debug(this,"查询结果["+account+"]\n");
			}
			return account;
		} catch (Exception e) {
			LogFactory.error(this,"根据账户id获取账户信息时发生异常\n");
			return account;
		}
	}
	@Override
	public Boolean phoneVerify(String phone) {
		String locker="phone["+phone+"]\n";
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
			
		}
		
		return result;
	}
	

}
