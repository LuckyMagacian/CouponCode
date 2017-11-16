package com.lanxi.couponcode.impl.controller;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.lanxi.couponcode.impl.assist.RetMessage;
import com.lanxi.couponcode.impl.entity.Account;
import com.lanxi.couponcode.impl.service.LoginService;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.util.entity.LogFactory;

public class LoginServiceImpl implements com.lanxi.couponcode.spi.service.LoginService{
	@Resource
	private LoginService loginService;
	@Override
	public RetMessage<String> login(String phone, String password, String validateCode) {
		RetMessage<String> retMessage=new RetMessage<String>();
		RetMessage<Account> retMessage2=null;
		try {
			if(phone!=null&&phone.isEmpty()&&password!=null&&password.isEmpty()) {
				retMessage2=loginService.login(phone, password, validateCode);
				if (retMessage2!=null) {
					retMessage.setRetCode(retMessage2.getRetCode());
					retMessage.setRetMessage(retMessage2.getRetMessage());
					String result=JSON.toJSONString(retMessage2.getDetail());
					retMessage.setDetail(result);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this, "登录时出现异常",e);
    		retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("登录时发生异常");
			retMessage.setDetail(null);
		}
		// TODO Auto-generated method stub
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> logout(Long accountId) {
		RetMessage<Boolean> retMessage=new RetMessage<Boolean>();
		Boolean result=false;
		try {
			result=loginService.logout(accountId);
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
			// TODO: handle exception
		}
		// TODO Auto-generated method stub
		return retMessage;
	}

	@Override
	public RetMessage<String> forgetPassword(String phone, String validateCode, String newPassword, String newRepeat,
			Long accountId) {
		RetMessage<String> retMessage=new RetMessage<String>();
		RetMessage<Boolean> retMessage2=null;
		try {
			retMessage2=loginService.forgetPassword(phone, validateCode, newPassword, newRepeat, accountId);
			if (retMessage2!=null) {
				retMessage.setRetCode(retMessage2.getRetCode());
				retMessage.setRetMessage(retMessage2.getRetMessage());
				String result=JSON.toJSONString(retMessage2);
				retMessage.setDetail(result);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this, "重置密码时出现异常",e);
    		retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("重置密码时发生异常");
			retMessage.setDetail(null);
		}
		// TODO Auto-generated method stub
		return retMessage;
	}

	@Override
	public RetMessage<String> changePassword(String oldPasswd, String newPasswd, String newRepeat, Long accountId) {
		RetMessage<String> retMessage=new RetMessage<String>();
		RetMessage<Boolean> retMessage2=null;
		try {
			retMessage2=loginService.changePassword(oldPasswd, newPasswd, newRepeat, accountId);
			if (retMessage2!=null) {
				retMessage.setRetCode(retMessage2.getRetCode());
				retMessage.setRetMessage(retMessage2.getRetMessage());
				String result=JSON.toJSONString(retMessage2);
				retMessage.setDetail(result);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this, "修改密码时出现异常",e);
    		retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("修改密码时发生异常");
			retMessage.setDetail(null);
		}
		// TODO Auto-generated method stub
		return retMessage;
	}

}
