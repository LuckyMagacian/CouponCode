package com.lanxi.couponcode.impl.newservice;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.Account;
import com.lanxi.util.entity.LogFactory;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
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
		synchronized (account) {
			LogFactory.info(this, "尝试冻结账号\n");
			Boolean result = false;
			try {
				if (account != null) {
					Integer var = dao.getAccountDao().updateById(account);
					if (var < 0) {
						result = false;
						LogFactory.warn(this, "冻结账户失败\n");
					} else if (var > 0) {
						result = true;
						LogFactory.info(this, "冻结账户成功\n");
					}
				} else {
					result = false;
					LogFactory.warn(this, "信息为空");
				}
			} catch (Exception e) {
				LogFactory.error(this, "冻结账户时发生异常\n", e);
			}
			return result;
		}
	}
	@Override
	public Boolean unfreezeAccount(Account account) {
		
		synchronized (account) {
			LogFactory.info(this, "尝试开启账户\n");
			Boolean result = false;
			try {
				if (account != null) {
					Integer var = dao.getAccountDao().updateById(account);
					if (var < 0) {
						result = false;
						LogFactory.warn(this, "开启账户失败\n");
					} else if (var > 0) {
						result = true;
						LogFactory.info(this, "开启账户成功\n");
					}
				} else {
					result = false;
					LogFactory.warn(this, "信息为空");
				}
			} catch (Exception e) {
				LogFactory.error(this, "开启账户时发生异常\n", e);
			}
			return result;
		}
	}
	@Override
	public Boolean delAccount(Account account) {
		
		synchronized (account) {
			LogFactory.info(this, "尝试删除账号\n");
			Boolean result = false;
			try {
				if (account != null) {
					Integer var = dao.getAccountDao().updateById(account);
					if (var < 0) {
						result = false;
						LogFactory.warn(this, "删除账户失败\n");
					} else if (var > 0) {
						result = true;
						LogFactory.info(this, "删除账户成功\n");
					}
				} else {
					result = false;
					LogFactory.warn(this, "信息为空");
				}
			} catch (Exception e) {
				LogFactory.error(this, "删除账户时发生异常\n", e);
			}
			return result;
		}
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
	@Override
	public Boolean modifyAccount(Account account) {
		synchronized (account) {
			return account.updateById();
		}
	}
	@Override
	public Account login(Account account, String validateCode) {
		LogFactory.info(this, "尝试登录账户\n");
		Account account2=null;
		try {
			if (account!=null) {
				account2 = dao.getAccountDao().selectOne(account);
				if (account2 != null) {
					return account2;
				} else {
					LogFactory.info(this, "此账户不存在");
				}
			} else {
				LogFactory.warn(this, "账户名和密码不能为空");
			}
		} catch (Exception e) {
			LogFactory.error(this, "登录账户时发生异常\n", e);
		}
		return account2;
	}

	@Override
	public Boolean logout(Long accountId) {

		return null;
	}

	@Override
	public Boolean forgetPassword( String validateCode, Account account, 
			Long accountId) {
		synchronized (account) {
			LogFactory.info(this, "尝试重置账户密码\n");
			Boolean result = false;
			try {

				EntityWrapper<Account> wrapper = new EntityWrapper<Account>();
				wrapper.eq("phone", account.getPhone());
				LogFactory.info(this, "条件修饰结果[" + wrapper + "]");
				Integer var = dao.getAccountDao().update(account, wrapper);
				if (var < 0) {
					result = false;
					LogFactory.debug(this, "重置密码失败\n");

				} else if (var > 0) {
					result = true;
					LogFactory.debug(this, "重置密码成功\n");

				}

			} catch (Exception e) {
				LogFactory.error(this, "重置密码时发生异常\n");

			}
			return result;
		}
	}

	@Override
	public Boolean changePassword(Account account) {

		synchronized (account) {
			LogFactory.info(this, "尝试修改账户密码\n");
			Boolean result = false;
			try {
				Integer var = dao.getAccountDao().updateById(account);
				if (var < 0) {
					result = false;
					LogFactory.debug(this, "修改密码失败\n");

				} else if (var > 0) {
					result = true;
					LogFactory.debug(this, "修改密码成功\n");

				}

			} catch (Exception e) {
				LogFactory.error(this, "重置密码时发生异常\n", e);

				throw new RuntimeException("重置密码时发生异常", e);

			}
			return result;
		}
	}

	// 判断给定时间跟当前时间的时间差是否大于15分钟
	public Boolean localdateLtDate(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			Date date1 = sdf.parse(date);
			Date now = sdf.parse(sdf.format(new Date()));
			if (now.getTime() - date1.getTime() > 15 * 60 * 1000) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			LogFactory.info(this, "判断时间差时发生异常");
			return false;
		}

	}
	@Override
	public String queryAccountStatusById(Long accountId) {
		LogFactory.info(this,"尝试通过账户id获取账户状态");
		String accountStatus=null;
		try {
			Account account=dao.getAccountDao().selectById(accountId);
			if (account!=null) {
				accountStatus=account.getStatus();
			}else {
				LogFactory.info(this,"此账户不存在");
			}
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this,"通过账户id获取账户状态时发生异常",e);
		}
		return accountStatus;
	}

}
