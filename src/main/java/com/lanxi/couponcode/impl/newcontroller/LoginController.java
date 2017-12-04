package com.lanxi.couponcode.impl.newcontroller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.lanxi.couponcode.impl.assist.RedisKeyAssist;
import com.lanxi.couponcode.impl.assist.TimeAssist;
import com.lanxi.couponcode.impl.entity.Account;
import com.lanxi.couponcode.impl.entity.OperateRecord;
import com.lanxi.couponcode.impl.newservice.AccountService;
import com.lanxi.couponcode.impl.newservice.ConfigService;
import com.lanxi.couponcode.impl.newservice.OperateRecordService;
import com.lanxi.couponcode.impl.newservice.RedisEnhancedService;
import com.lanxi.couponcode.impl.newservice.RedisService;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.enums.AccountStatus;
import com.lanxi.couponcode.spi.consts.enums.OperateTargetType;
import com.lanxi.couponcode.spi.consts.enums.OperateType;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.util.entity.LogFactory;
import com.lanxi.util.utils.SignUtil;
import static com.lanxi.couponcode.impl.assist.PredicateAssist.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;

/**
 * 
 * @author wuxiaobo
 *
 */
@Controller("loginControllerService")
public class LoginController implements com.lanxi.couponcode.spi.service.LoginService {
	@Resource
	private AccountService accountService;
	@Resource
	private RedisService redisService;
	@Resource
	private RedisEnhancedService redisEnhancedService;
	@Resource
	private ConfigService configService;
	@Resource
	private OperateRecordService operateRecordService;

	@Override
	public RetMessage<String> login(String phone, String password, String validateCode) {
		Account account = null;
		// TODO 验证码
		try {
			if (phone != null && phone.isEmpty() && password != null && password.isEmpty()) {
				Account account2 = new Account();
				account2.setPhone(phone);
				account = accountService.login(account2, validateCode);
				if (account != null) {
					if (accountService.localdateLtDate(account.getLoginFailureTime())) {
						// 距离上次登录失败大于15分钟
						//account.setPassword(SignUtil.md5LowerCase("123456","utf-8"));
						if (SignUtil.md5LowerCase(password,"utf-8").equals(account.getPassword())) {
							if (AccountStatus.freeze.getValue().equals(account.getStatus())) {
								LogFactory.debug(this, "账户已被冻结\n");
								return new RetMessage<>(RetCodeEnum.fail, "您的账户已被冻结", null);
							} else if (AccountStatus.deleted.getValue().equals(account.getStatus())) {
								LogFactory.debug(this, "已被删除的账户\n");
								return new RetMessage<>(RetCodeEnum.fail, "您的账户不存在", null);
							} else {
								LogFactory.debug(this, "登录成功\n");
								account.setLoginFailureNum(0);
								account.setLoginFailureTime("20171114033028");
								LogFactory.info(this, "登录成功把失败登录次数和失败登录时间清零,/n");
								accountService.modifyAccount(account);
								String result = JSON.toJSONString(account);
								Boolean boolean1 = redisService.set(RedisKeyAssist.getLoginKey(account.getAccountId()),
										SignUtil.md5LowerCase(TimeAssist.getNow() + account.getAccountId(), "utf-8"),
										Long.valueOf(configService.getValue("parameter", "accountLifeDefaultValue")));
								if (boolean1) {
									return new RetMessage<>(RetCodeEnum.success, "登录成功", result);
								} else {
									return new RetMessage<>(RetCodeEnum.error, "登录异常", null);
								}
							}
						} else {
							LogFactory.debug(this, "登录失败把失败登录次数+1和失败登录时间更新");
							account.setLoginFailureNum(account.getLoginFailureNum() + 1);
							account.setLoginFailureTime(
									LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
							accountService.modifyAccount(account);
							LogFactory.debug(this, "账号或密码错误\n");
							return new RetMessage<>(RetCodeEnum.fail, "账号或者密码错误", null);
						}
					} else {
						// 距离上次登录失败小于15分钟
						if (account.getLoginFailureNum() > 2) {
							return new RetMessage<>(RetCodeEnum.fail, "密码输入错误次数三次15分钟内不能重新登录", null);
						} else {
							if (SignUtil.md5LowerCase(password,"utf-8").equals(account.getPassword())) {
								if (account.getStatus().equals(AccountStatus.freeze.getValue())) {
									LogFactory.debug(this, "账户已被冻结\n");
									return new RetMessage<>(RetCodeEnum.fail, "您的账户已被冻结", null);
								} else if (account.getStatus().equals(AccountStatus.deleted.getValue())) {
									LogFactory.debug(this, "已被删除的账户\n");
									return new RetMessage<>(RetCodeEnum.fail, "账户不存在", null);
								} else {
									LogFactory.debug(this, "登录成功\n");
									String result = JSON.toJSONString(account);
									account.setLoginFailureNum(0);
									account.setLoginFailureTime("20171114033028");
									account.setAccountId(account.getAccountId());
									LogFactory.info(this, "把失败登录次数和失败登录时间清零,/n");
									accountService.modifyAccount(account);
									Boolean boolean1 = redisService.set(
											RedisKeyAssist.getLoginKey(account.getAccountId()),
											SignUtil.md5LowerCase(TimeAssist.getNow() + account.getAccountId(),
													"utf-8"),
											Long.valueOf(
													configService.getValue("parameter", "accountLifeDefaultValue")));
									if (boolean1) {
										return new RetMessage<>(RetCodeEnum.success, "登录成功", result);
									} else {
										return new RetMessage<>(RetCodeEnum.error, "登录异常", null);
									}
								}
							} else {
								LogFactory.debug(this, "登录失败把失败登录次数+1和失败登录时间更新");
								account.setLoginFailureNum(account.getLoginFailureNum() + 1);
								account.setLoginFailureTime(
										LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
								account.setAccountId(account.getAccountId());
								accountService.modifyAccount(account);
								LogFactory.debug(this, "账号或密码错误\n");
								return new RetMessage<>(RetCodeEnum.fail, "账号或者密码错误", null);
							}
						}
					}
				} else {
					return new RetMessage<>(RetCodeEnum.fail, "此账户不存在", null);
				}
			} else {
				return new RetMessage<>(RetCodeEnum.fail, "账户和密码不能为空", null);
			}
		} catch (Exception e) {
			LogFactory.error(this, "登录时出现异常", e);
			return new RetMessage<>(RetCodeEnum.error, "登录时发生异常", null);
		}
	}

	@Override
	public RetMessage<Boolean> logout(Long accountId) {
		Boolean result = false;
		try {
			String loginKey = redisService.get(RedisKeyAssist.getLoginKey(accountId));
			if (loginKey != null) {
				result = redisService.del(loginKey);
			} else {
				result = true;
			}
			if (result) {
				return new RetMessage<Boolean>(RetCodeEnum.success, "退出登录成功", true);
			} else {
				return new RetMessage<Boolean>(RetCodeEnum.exception, "退出登录失败", false);
			}
		} catch (Exception e) {
			LogFactory.error(this, "退出登录时发生异常", e);
			return new RetMessage<Boolean>(RetCodeEnum.error, "退出登录时发生异常", false);
		}
	}

	@Override
	public RetMessage<Boolean> forgetPassword(String phone, String validateCode, String newPassword, String newRepeat,
			Long accountId) {
		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result = false;
		// TODO 手机验证码校验
		try {
			if (!newPassword.equals(newRepeat)) {
				result = false;
				LogFactory.debug(this, "两次密码不一致\n");
				retMessage.setAll(RetCodeEnum.fail, "两次密码不一致", false);
			} else {
				Account account = new Account();
				account.setPhone(phone);
				Account a = accountService.queryAccountInfo(account);
				RetMessage message = checkAccount.apply(a, OperateType.changePassword);
				if (notNull.test(message))
					return message;
				account.setPassword(SignUtil.md5LowerCase(newPassword,"utf-8"));
				result = accountService.forgetPassword(validateCode, account, accountId);
				if (result) {
					retMessage.setAll(RetCodeEnum.success, "重置密码成功", result);
					OperateRecord record = new OperateRecord();
					record.setRecordId(IdWorker.getId());
					record.setOperaterId(a.getAccountId());
					record.setAccountType(a.getAccountType());
					record.setPhone(a.getPhone());
					record.setName(a.getUserName());
					record.setTargetType(OperateTargetType.account);
					record.setType(OperateType.changePassword);
					record.setOperateTime(TimeAssist.getNow());
					record.setOperateResult("success");
					record.setDescription("修改密码[" + a.getAccountId() + "]");
					operateRecordService.addRecord(record);
				} else {
					retMessage.setAll(RetCodeEnum.exception, "重置密码失败", result);
				}
			}

		} catch (Exception e) {
			LogFactory.error(this, "重置密码时出现异常", e);
			retMessage.setAll(RetCodeEnum.error, "重置密码时发生异常", result);
		}
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> changePassword(String oldPasswd, String newPasswd, String newRepeat, Long accountId) {

		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result = false;
		try {
			// 先判断输入的旧密码是否与新密码相同
			if (!oldPasswd.equals(newPasswd)) {
				// 判断两次密码是否一致
				if (!newPasswd.equals(newRepeat)) {
					result = false;
					LogFactory.debug(this, "两次密码不一致\n");
					retMessage.setAll(RetCodeEnum.fail, "两次密码不一致", false);
				} else {
					Account account = accountService.queryAccountById(accountId);
					RetMessage message = checkAccount.apply(account, OperateType.changePassword);
					if (notNull.test(message))
						return message;
					if (SignUtil.md5LowerCase(oldPasswd,"utf-8").equals(account.getPassword())) {
						account.setPassword(SignUtil.md5LowerCase(newPasswd,"utf-8"));
						result = accountService.changePassword(account);
						if (result) {
							retMessage.setAll(RetCodeEnum.success, "修改密码成功", result);
							OperateRecord record = new OperateRecord();
							record.setRecordId(IdWorker.getId());
							record.setOperaterId(accountId);
							record.setAccountType(account.getAccountType());
							record.setPhone(account.getPhone());
							record.setName(account.getUserName());
							record.setTargetType(OperateTargetType.merchant);
							record.setType(OperateType.changePassword);
							record.setOperateTime(TimeAssist.getNow());
							record.setOperateResult("success");
							record.setDescription("修改密码[" + accountId + "]");
							operateRecordService.addRecord(record);
						} else {
							retMessage.setAll(RetCodeEnum.exception, "修改密码失败", result);
						}

					} else {
						result = false;
						retMessage.setAll(RetCodeEnum.fail, "旧密码错误", result);
					}
				}
			} else {
				result = false;
				retMessage.setAll(RetCodeEnum.fail, "两次密码输入不一致", result);
			}

		} catch (Exception e) {
			LogFactory.error(this, "修改密码时出现异常", e);
			retMessage.setAll(RetCodeEnum.error, "修改密码时发生异常", result);
		}
		return retMessage;
	}

}
