package com.lanxi.couponcode.impl.newcontroller;
import com.alibaba.fastjson.JSON;
import com.lanxi.couponcode.impl.entity.Account;
import com.lanxi.couponcode.impl.newservice.AccountService;
import com.lanxi.couponcode.impl.newservice.RedisEnhancedService;
import com.lanxi.couponcode.impl.newservice.RedisService;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.enums.AccountStatus;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.util.entity.LogFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
/**
 * 
 * @author wuxiaobo
 *
 */
@Controller
public class LoginController implements com.lanxi.couponcode.spi.service.LoginService{
	@Resource
	private AccountService accountService;
	@Resource
	private RedisService redisService;
	@Resource
	private RedisEnhancedService redisEnhancedService;
	@Override
	public RetMessage<String> login(String phone, String password, String validateCode) {
		RetMessage<String> retMessage=new RetMessage<String>();
		Account account=null;
		//TODO 验证码
		try {
			if(phone!=null&&phone.isEmpty()&&password!=null&&password.isEmpty()) {
				Account account2=new Account();
				account2.setPhone(phone);
				account=accountService.login(account2, validateCode);
				if (account!=null) {
					if(accountService.localdateLtDate(account.getLoginFailureTime())) {
						//距离上次登录失败大于15分钟
						if(password.equals(account.getPassword())) {
							if(AccountStatus.freeze.getValue().equals(account.getStatus())) {
								LogFactory.debug(this,"账户已被冻结\n");
								retMessage.setDetail(null);
								retMessage.setRetCode(RetCodeEnum.exception.getValue());
								retMessage.setRetMessage("您的账户已被冻结");
							}else if (AccountStatus.deleted.getValue().equals(account.getStatus())) {
								LogFactory.debug(this,"已被删除的账户\n");
								retMessage.setDetail(null);
								retMessage.setRetCode(RetCodeEnum.exception.getValue());
								retMessage.setRetMessage("您的账户不存在");
							}else {
								LogFactory.debug(this,"登录成功\n");
								account.setLoginFailureNum(0);
								account.setLoginFailureTime("20171114033028");
								LogFactory.info(this,"登录成功把失败登录次数和失败登录时间清零,/n");
								accountService.modifyAccount(account);
								String result=JSON.toJSONString(account);
								retMessage.setDetail(result);
								retMessage.setRetCode(RetCodeEnum.success.getValue());
								retMessage.setRetMessage("登录成功");
								//TODO 插入操作记录
							}
						}else {
							LogFactory.debug(this,"登录失败把失败登录次数+1和失败登录时间更新");
							account.setLoginFailureNum(account.getLoginFailureNum()+1);
							account.setLoginFailureTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
							accountService.modifyAccount(account);
							LogFactory.debug(this,"账号或密码错误\n");
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
									LogFactory.debug(this,"账户已被冻结\n");
									retMessage.setDetail(null);
									retMessage.setRetCode(RetCodeEnum.exception.getValue());
									retMessage.setRetMessage("您的账户已被冻结");
								}else if (account.getStatus().equals(AccountStatus.deleted.getValue())) {
									LogFactory.debug(this,"已被删除的账户\n");
									retMessage.setDetail(null);
									retMessage.setRetCode(RetCodeEnum.exception.getValue());
									retMessage.setRetMessage("您的账户不存在");
								}else {
									LogFactory.debug(this,"登录成功\n");
									String result=JSON.toJSONString(account);
									retMessage.setDetail(result);
									retMessage.setRetCode(RetCodeEnum.success.getValue());
									retMessage.setRetMessage("登录成功");
									account.setLoginFailureNum(0);
									account.setLoginFailureTime("20171114033028");
									account.setAccountId(account.getAccountId());
									LogFactory.info(this,"把失败登录次数和失败登录时间清零,/n");
									accountService.modifyAccount(account);
								}
								
							}else {
								LogFactory.debug(this,"登录失败把失败登录次数+1和失败登录时间更新");
								account.setLoginFailureNum(account.getLoginFailureNum()+1);
								account.setLoginFailureTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
								account.setAccountId(account.getAccountId());
								//TODO 插入操作记录
								accountService.modifyAccount(account);
								LogFactory.debug(this,"账号或密码错误\n");
								retMessage.setDetail(null);
								retMessage.setRetCode(RetCodeEnum.fail.getValue());
								retMessage.setRetMessage("账号或者密码错误");
							}
						}
					}
				}else {
					retMessage.setDetail(null);
					retMessage.setRetMessage("此账户不存在");
					retMessage.setRetCode(RetCodeEnum.warning.getValue());
				}
			}else {
				retMessage.setDetail(null);
				retMessage.setRetMessage("账户和密码不能为空");
				retMessage.setRetCode(RetCodeEnum.fail.getValue());
			}
		} catch (Exception e) {
			LogFactory.error(this, "登录时出现异常",e);
    		retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("登录时发生异常");
			retMessage.setDetail(null);
		}
		
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> logout(Long accountId) {
		RetMessage<Boolean> retMessage=new RetMessage<Boolean>();
		Boolean result=false;
		try {
			result=accountService.logout(accountId);
			if(result) {
				retMessage.setRetCode(RetCodeEnum.success.getValue());
				retMessage.setRetMessage("退出登录成功");
			}
			if(!result) {
				retMessage.setRetCode(RetCodeEnum.exception.getValue());
				retMessage.setRetMessage("退出登录失败");
			}
			retMessage.setDetail(result);
		} catch (Exception e) {
			LogFactory.error(this,"退出登录时发生异常",e);
			retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("退出登录时发生异常");
			retMessage.setDetail(result);
			
		}
		
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> forgetPassword(String phone, String validateCode, String newPassword, String newRepeat,
			Long accountId) {
		RetMessage<Boolean> retMessage=new RetMessage<Boolean>();
		Boolean result=false;
		//TODO 手机验证码校验
		try {
			if (!newPassword.equals(newRepeat)) {
				result=false;
				retMessage.setDetail(result);
				retMessage.setRetCode(RetCodeEnum.fail.getValue());
				LogFactory.debug(this,"两次密码不一致\n");
				retMessage.setRetMessage("两次密码不一致");
			}else {
				Account account=new Account();
				account.setPhone(phone);
				account.setPassword(newPassword);
				result=accountService.forgetPassword(validateCode, account, accountId);
				if (result) {
					retMessage.setRetCode(RetCodeEnum.success);
					retMessage.setRetMessage("重置密码成功");
					retMessage.setDetail(result);
					//TODO 插入操作记录
				}else {
					retMessage.setRetCode(RetCodeEnum.exception);
					retMessage.setRetMessage("重置密码失败");
					retMessage.setDetail(result);
				}
			}
			
		} catch (Exception e) {
			
			LogFactory.error(this, "重置密码时出现异常",e);
    		retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("重置密码时发生异常");
			retMessage.setDetail(result);
		}
		
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> changePassword(String oldPasswd, String newPasswd, String newRepeat, Long accountId) {
	
		RetMessage<Boolean> retMessage=new RetMessage<Boolean>();
		Boolean result=false;
		try {
			//先判断输入的旧密码是否与新密码相同
			if(!oldPasswd.equals(newPasswd)) {
				//判断两次密码是否一致
				if (!newPasswd.equals(newRepeat)) {
					result=false;
					retMessage.setDetail(result);
					retMessage.setRetCode(RetCodeEnum.fail.getValue());
					LogFactory.debug(this,"两次密码不一致\n");
					retMessage.setRetMessage("两次密码不一致");
				}else {
					Account account=new Account();
					account.setAccountId(accountId);
					if (oldPasswd.equals(accountService.queryAccountInfo(account).getPassword())) {
						account.setPassword(newPasswd);
						result=accountService.changePassword(account);
						if (result) {
							retMessage.setAll(RetCodeEnum.success,"修改密码成功",result);
							//TODO 插入操作记录
						}else {
							retMessage.setAll(RetCodeEnum.exception,"修改密码失败",result);
						}
						
					}else {
						result=false;
						retMessage.setAll(RetCodeEnum.fail,"旧密码错误",result);
						}
					}
				}else {
					result=false;
					retMessage.setAll(RetCodeEnum.fail,"两次密码输入不一致", result);
				}
			
		} catch (Exception e) {
			LogFactory.error(this, "修改密码时出现异常",e);
    		retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("修改密码时发生异常");
			retMessage.setDetail(result);
		}
		return retMessage;
	}

}
