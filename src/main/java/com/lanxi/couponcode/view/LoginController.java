package com.lanxi.couponcode.view;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lanxi.couponcode.spi.consts.annotations.SetUtf8;
import com.lanxi.couponcode.spi.service.LoginService;

@Controller
public class LoginController {
	@Resource
	private LoginService loginService;
	
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "", produces = "application/json;charset=utf-8")
	public String login(HttpServletRequest req,HttpServletResponse res) {
		
		return null;
	}
	
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "", produces = "application/json;charset=utf-8")
	public String logout(HttpServletRequest req,HttpServletResponse res) {
		
		return null;
	}
	
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "", produces = "application/json;charset=utf-8")
	public String forgetPassword(HttpServletRequest req,HttpServletResponse res) {
		
		return null;
	}
	
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "", produces = "application/json;charset=utf-8")
	public String changePassword(HttpServletRequest req,HttpServletResponse res) {
		
		return null;
	}
}
