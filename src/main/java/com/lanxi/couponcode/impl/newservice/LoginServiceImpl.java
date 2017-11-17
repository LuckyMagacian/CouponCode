package com.lanxi.couponcode.impl.newservice;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.impl.entity.Account;
import com.lanxi.couponcode.impl.entity.OperateRecord;
import com.lanxi.couponcode.spi.consts.enums.AccountStatus;
import com.lanxi.couponcode.spi.consts.enums.OperateTargetType;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.util.entity.LogFactory;
import com.lanxi.util.utils.TimeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service("loginService")
public class LoginServiceImpl implements LoginService{
	@Resource
	private DaoService dao;
	@Override
	public RetMessage<Account> login(String phone, String password, String validateCode) {
		String locker="phone["+phone+"]\n"+
			    ",password["+password+"]\n"+
                ",validateCode["+validateCode+"]\n";
		LogFactory.info(this,"尝试登录账户\n"+locker);
		RetMessage<Account>retMessage=null;
		Account account=null;
		OperateRecord record=null;
		try {
			retMessage=new RetMessage<Account>();
			
			if(phone!=null&&!phone.isEmpty()&&password!=null&&!password.isEmpty()) {
				//先判断phone对应的账户是否存在
				LogFactory.info(this,"判断["+phone+"]"+"所对应的用户是否存在,\n"+locker);
				Account account2=new Account();
				account2.setPhone(phone);
				account=dao.getAccountDao().selectOne(account2);
				if (account!=null) {
					//用户存在    先判断登录失败次数 如果小于三则继续进行判断  如果登录失败次数+1 记录当前时间
					//判断账户上次登录失败时间跟当前时间的时间差 如果大于15分钟直接进入密码判断                                  
					if(localdateLtDate(account.getLoginFailureTime())) {
						//距离上次登录失败大于15分钟
						if(password.equals(account.getPassword())) {
							if(account.getStatus().equals(AccountStatus.freeze.getValue())) {
								LogFactory.debug(this,"账户已被冻结\n"+locker);
								retMessage.setDetail(null);
								retMessage.setRetCode(RetCodeEnum.exception.getValue());
								retMessage.setRetMessage("您的账户已被冻结");
							}else if (account.getStatus().equals(AccountStatus.deleted.getValue())) {
								LogFactory.debug(this,"已被删除的账户\n"+locker);
								retMessage.setDetail(null);
								retMessage.setRetCode(RetCodeEnum.exception.getValue());
								retMessage.setRetMessage("您的账户不存在");
							}else {
								LogFactory.debug(this,"登录成功\n"+locker);
								LogFactory.info(this,"尝试插入登录操作记录\n"+locker);
								record=new OperateRecord();
								record.setRecordId(IdWorker.getId());
								record.setPhone(phone);
								record.setTargetType(OperateTargetType.account);
								record.setDescription("账户登录");
								record.setOperateTime(TimeUtil.getDateTime());
								record.setTargetInfo(account.toJson());
								account2.setLoginFailureNum(0);
								account2.setLoginFailureTime("20171114033028");
								account2.setAccountId(account.getAccountId());
								LogFactory.info(this,"登录成功把失败登录次数和失败登录时间清零,/n"+locker);
								dao.getAccountDao().updateById(account2);
								retMessage.setDetail(account);
								retMessage.setRetCode(RetCodeEnum.success.getValue());
								retMessage.setRetMessage("登录成功");
								boolean flag=record.insert();
					            LogFactory.info(this,"添加登录账户操作记录["+record+"]结果["+flag+"],\n"+locker); 
							}
						}else {
							LogFactory.debug(this,"登录失败把失败登录次数+1和失败登录时间更新");
							account2.setLoginFailureNum(account.getLoginFailureNum()+1);
							account2.setLoginFailureTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
							account2.setAccountId(account.getAccountId());
							dao.getAccountDao().updateById(account2);
							LogFactory.debug(this,"账号或密码错误\n"+locker);
							retMessage.setDetail(null);
							retMessage.setRetCode(RetCodeEnum.fail.getValue());
							retMessage.setRetMessage("账号或者密码错误");
						}
						
					}else {
						//距离上次登录失败小于15分钟
						if(account.getLoginFailureNum()>2) {
							retMessage.setDetail(null);
							retMessage.setRetCode(RetCodeEnum.fail.getValue());
							retMessage.setRetMessage("密码输入错误次数三次15分钟内不能重新登录");
						}else {
							if(password.equals(account.getPassword())) {
								if(account.getStatus().equals(AccountStatus.freeze.getValue())) {
									LogFactory.debug(this,"账户已被冻结\n"+locker);
									retMessage.setDetail(null);
									retMessage.setRetCode(RetCodeEnum.exception.getValue());
									retMessage.setRetMessage("您的账户已被冻结");
									
								}else if (account.getStatus().equals(AccountStatus.deleted.getValue())) {
									LogFactory.debug(this,"已被删除的账户\n"+locker);
									retMessage.setDetail(null);
									retMessage.setRetCode(RetCodeEnum.exception.getValue());
									retMessage.setRetMessage("您的账户不存在");
									
								}else {
									LogFactory.debug(this,"登录成功\n"+locker);
									LogFactory.info(this,"尝试插入登录操作记录\n"+locker);
									record=new OperateRecord();
									record.setRecordId(IdWorker.getId());
									record.setPhone(phone);
									record.setTargetType(OperateTargetType.account);
									record.setDescription("账户登录");
									record.setOperateTime(TimeUtil.getDateTime());
									record.setTargetInfo(account.toJson());
									retMessage.setDetail(account);
									retMessage.setRetCode(RetCodeEnum.success.getValue());
									retMessage.setRetMessage("登录成功");
									boolean flag=record.insert();
						            LogFactory.info(this,"添加登录账户操作记录["+record+"]结果["+flag+"],\n"+locker); 
								}
								account2.setLoginFailureNum(0);
								account2.setLoginFailureTime("0");
								account2.setAccountId(account.getAccountId());
								LogFactory.info(this,"把失败登录次数和失败登录时间清零,/n"+locker);
								dao.getAccountDao().updateById(account2);
							}else {
								LogFactory.debug(this,"登录失败把失败登录次数+1和失败登录时间更新");
								account2.setLoginFailureNum(account.getLoginFailureNum()+1);
								account2.setLoginFailureTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
								account2.setAccountId(account.getAccountId());
								dao.getAccountDao().updateById(account2);
								LogFactory.debug(this,"账号或密码错误\n"+locker);
								retMessage.setDetail(null);
								retMessage.setRetCode(RetCodeEnum.fail.getValue());
								retMessage.setRetMessage("账号或者密码错误");
							}
						}
					}
				}
		
			}else {
				retMessage.setDetail(null);
				retMessage.setRetMessage("账户和密码不能为空");
				retMessage.setRetCode(RetCodeEnum.fail.getValue());
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this,"登录账户时发生异常\n"+locker,e);
			retMessage.setDetail(null);
			retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("登录账户时发生异常");
		}
		// TODO Auto-generated method stub
		return retMessage;
	}

	@Override
	public Boolean logout(Long accountId) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RetMessage<Boolean> forgetPassword(String phone, String validateCode, String newPassword, String newRepeat,
			Long accountId) {
		String locker="phone["+phone+"]\n"+
			    ",validateCode["+validateCode+"]\n"+
                ",newPassword["+newPassword+"]\n"+
                 ",newRepeat["+newRepeat+"]\n"+
                 ",accountId["+accountId+"]\n";
		LogFactory.info(this,"尝试重置账户密码\n"+locker);
		Boolean result=false;
		Account account=null;
		OperateRecord record=null;
		RetMessage<Boolean> retMessage=new RetMessage<Boolean>();
		try {
			if (!newPassword.equals(newRepeat)) {
				result=false;
				retMessage.setDetail(result);
				retMessage.setRetCode(RetCodeEnum.fail.getValue());
				LogFactory.debug(this,"两次密码不一致\n"+locker);
				retMessage.setRetMessage("两次密码不一致");
			}else {
				account=new Account();
				EntityWrapper<Account> wrapper=new EntityWrapper<Account>();
				wrapper.eq("phone",phone);
				account.setPassword(newPassword);
				LogFactory.info(this,"条件修饰结果["+wrapper+"]");
				Integer var=dao.getAccountDao().update(account, wrapper);
				if (var<0) {
					result=false;
					LogFactory.debug(this,"重置密码失败\n"+locker);
					retMessage.setDetail(result);
					retMessage.setRetCode(RetCodeEnum.unknown.getValue());
					retMessage.setRetMessage("重置密码失败");
				}else if(var>0){
					result=true;
					LogFactory.debug(this,"重置密码成功\n"+locker);
					retMessage.setDetail(result);
					retMessage.setRetCode(RetCodeEnum.success.getValue());
					retMessage.setRetMessage("重置密码成功");
					LogFactory.info(this,"尝试插入重置密码操作记录\n"+locker);
					record=new OperateRecord();
					record.setRecordId(IdWorker.getId());
					record.setPhone(phone);
					record.setTargetType(OperateTargetType.account);
					record.setDescription("重置账户密码");
					record.setOperateTime(TimeUtil.getDateTime());
					record.setTargetInfo(account.toJson());
					boolean flag=record.insert();
		            LogFactory.info(this,"添加重置账户密码操作记录["+record+"]结果["+flag+"],\n"+locker); 
				}
			}
		} catch (Exception e) {
			LogFactory.error(this,"重置密码时发生异常\n"+locker,e);
			retMessage.setDetail(null);
			retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("重置密码时发生异常");
			// TODO: handle exception
		}
		// TODO Auto-generated method stub
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> changePassword(String oldPasswd, String newPasswd, String newRepeat, Long accountId) {
		String locker="oldPasswd["+oldPasswd+"]\n"+
                ",newPasswd["+newPasswd+"]\n"+
                 ",newRepeat["+newRepeat+"]\n"+
                 ",accountId["+accountId+"]\n";
		LogFactory.info(this,"尝试修改账户密码\n"+locker);
		Boolean result=false;
		Account account=null;
		OperateRecord record=null;
		RetMessage<Boolean> retMessage=new RetMessage<Boolean>();
		try {
			//先判断输入的旧密码是否与新密码相同
			if(!oldPasswd.equals(newPasswd)) {
				//判断两次密码是否一致
				if (!newPasswd.equals(newRepeat)) {
					result=false;
					retMessage.setDetail(result);
					retMessage.setRetCode(RetCodeEnum.fail.getValue());
					LogFactory.debug(this,"两次密码不一致\n"+locker);
					retMessage.setRetMessage("两次密码不一致");
				}else {
					//判断旧密码是否正确
					if (oldPasswd.equals(dao.getAccountDao().selectById(accountId).getPassword())) {
						account=new Account();
						account.setPassword(newPasswd);
						account.setAccountId(accountId);
						Integer var=dao.getAccountDao().updateById(account);
						if (var<0) {
							result=false;
							LogFactory.debug(this,"修改密码失败\n"+locker);
							retMessage.setDetail(result);
							retMessage.setRetCode(RetCodeEnum.unknown.getValue());
							retMessage.setRetMessage("修改密码失败");
						}else if(var>0){
							result=true;
							LogFactory.debug(this,"修改密码成功\n"+locker);
							retMessage.setDetail(result);
							retMessage.setRetCode(RetCodeEnum.success.getValue());
							retMessage.setRetMessage("修改密码成功");
							record=new OperateRecord();
							record.setRecordId(IdWorker.getId());
							record.setOperaterId(accountId);
							record.setTargetType(OperateTargetType.account);
							record.setDescription("修改账户密码");
							record.setOperateTime(TimeUtil.getDateTime());
							record.setTargetInfo(account.toJson());
							boolean flag=record.insert();
				            LogFactory.info(this,"添加修改账户密码操作记录["+record+"]结果["+flag+"],\n"+locker); 
						}
					}else {
						result=false;
						LogFactory.debug(this,"输入的旧密码错误\n"+locker);
						retMessage.setDetail(result);
						retMessage.setRetCode(RetCodeEnum.fail.getValue());
						retMessage.setRetMessage("旧密码输入错误");
						}
				}
			}else {
				LogFactory.debug(this,"新旧密码不能相同\n"+locker);
				result=false;
				retMessage.setDetail(result);
				retMessage.setRetCode(RetCodeEnum.fail.getValue());
				retMessage.setRetMessage("新旧密码不能相同");
			}
		
		} catch (Exception e) {
			LogFactory.error(this,"重置密码时发生异常\n"+locker,e);
			retMessage.setDetail(null);
			retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("重置密码时发生异常");
			// TODO: handle exception
		}
		// TODO Auto-generated method stub
		return retMessage;
	}
//判断给定时间跟当前时间的时间查是否大于15分钟
	public boolean localdateLtDate(String date) throws Exception{
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
        Date date1=sdf.parse(date);
        Date now=sdf.parse(sdf.format(new Date()));
        if(now.getTime()-date1.getTime()>15*60*1000){
            return true;
        }
        else{
            return false;
        }
    }

	

}
