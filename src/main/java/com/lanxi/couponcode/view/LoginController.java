package com.lanxi.couponcode.view;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.lanxi.couponcode.spi.assist.ArgAssist.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.annotations.SetUtf8;
import com.lanxi.couponcode.spi.service.LoginService;

@Controller ("loginView")
public class LoginController {
    @Resource (name = "loginControllerService")
    private LoginService loginService;

    @SetUtf8
    @ResponseBody
    @RequestMapping (value = "login", produces = "application/json;charset=utf-8")
    public String login(HttpServletRequest req, HttpServletResponse res) {
        String phone = getArg.apply(req, "phone");
        String password = getArg.apply(req, "password");
        String validateCode = getArg.apply(req, "validateCode");
        RetMessage<String> retMessage = loginService.login(phone, password, validateCode);
        return retMessage.toJson();
    }

    @SetUtf8
    @ResponseBody
    @RequestMapping (value = "logout", produces = "application/json;charset=utf-8")
    public String logout(HttpServletRequest req, HttpServletResponse res) {
        String accountIdStr = getArg.apply(req, "accountId");
        Long accountId = toLongArg.apply(accountIdStr);
        return loginService.logout(accountId).toJson();
    }

    @SetUtf8
    @ResponseBody
    @RequestMapping (value = "forgetPassword", produces = "application/json;charset=utf-8")
    public String forgetPassword(HttpServletRequest req, HttpServletResponse res) {
        String phone = getArg.apply(req, "phone");
        String validateCode = getArg.apply(req, "validateCode");
        String newPassword = getArg.apply(req, "newPassword");
        String newRepeat = getArg.apply(req, "newRepeat");
        String accountIdStr = getArg.apply(req, "accountId");
        Long accountId = toLongArg.apply(accountIdStr);
        return loginService.forgetPassword(phone, validateCode, newPassword, newRepeat, accountId).toJson();
    }

    @SetUtf8
    @ResponseBody
    @RequestMapping (value = "changePassword", produces = "application/json;charset=utf-8")
    public String changePassword(HttpServletRequest req, HttpServletResponse res) {
        String oldPasswd = getArg.apply(req, "oldPasswd");
        String newPasswd = getArg.apply(req, "newPasswd");
        String newRepeat = getArg.apply(req, "newRepeat");
        String accountIdStr = getArg.apply(req, "accountId");
        Long accountId = toLongArg.apply(accountIdStr);
        return loginService.changePassword(oldPasswd, newPasswd, newRepeat, accountId).toJson();
    }
}
