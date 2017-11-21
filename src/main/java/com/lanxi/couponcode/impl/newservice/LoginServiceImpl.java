package com.lanxi.couponcode.impl.newservice;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import com.lanxi.couponcode.impl.entity.Account;

import com.lanxi.util.entity.LogFactory;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;

import java.util.Date;

/**
 * 登录接口实现类
 * 
 * @author Administrator
 *
 */
@Service("loginService")
public class LoginServiceImpl implements LoginService {
	@Resource
	private DaoService dao;

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

	@Override
	public Boolean changePassword(Account account) {

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
			
			throw new RuntimeException("重置密码时发生异常",e);
			
		}
		return result;
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

}
