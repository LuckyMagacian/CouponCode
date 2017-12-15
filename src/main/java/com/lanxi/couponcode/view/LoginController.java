package com.lanxi.couponcode.view;

import com.lanxi.couponcode.impl.newservice.RedisServiceImpl;
import com.lanxi.couponcode.spi.assist.RedisKeyAssist;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.couponcode.spi.consts.annotations.LoginCheck;
import com.lanxi.couponcode.spi.consts.annotations.SetUtf8;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.couponcode.spi.service.LoginService;
import com.lanxi.util.utils.LoggerUtil;
import com.lanxi.util.utils.PictureVerifyUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.lanxi.couponcode.spi.assist.ArgAssist.getArg;
import static com.lanxi.couponcode.spi.assist.ArgAssist.toLongArg;

@Controller ("loginView")
@RequestMapping("loginCon")
@EasyLog (LoggerUtil.LogLevel.INFO)
public class LoginController {
    @Resource (name = "loginControllerServiceRef")
    private LoginService loginService;
    @Resource(name = "redisService")
    private RedisServiceImpl redisService;
    private static final long picCodeLife=600*1000L;
    @SetUtf8
    @ResponseBody
    @RequestMapping (value = "login", produces = "application/json;charset=utf-8")
    public String login(HttpServletRequest req, HttpServletResponse res) {
        String phone = getArg.apply(req, "phone");
        String password = getArg.apply(req, "password");
        String validateCode = getArg.apply(req, "validateCode");
        if(validateCode==null){
            return new RetMessage<>(RetCodeEnum.fail,"图片验证码校验不通过!",null).toJson();
        }
        String sessionId=req.getRemoteHost()+req.getRemoteUser();
        String key=RedisKeyAssist.getVerificateCodeKey(sessionId);
        String cacheCode=redisService.get(key);
        if(cacheCode==null||!cacheCode.equals(validateCode)){
            return new RetMessage<>(RetCodeEnum.fail,"图片验证码错误!",null).toJson();
        }else{
            redisService.del(key);
        }
        RetMessage<String> retMessage = loginService.login(phone, password, validateCode);
        return retMessage.toJson();
    }
    @SetUtf8
    @ResponseBody
    @RequestMapping (value = "loginWechat", produces = "application/json;charset=utf-8")
    public String loginWechat(HttpServletRequest req,HttpServletResponse res){
        String phone=getArg.apply(req,"phone");
        String password=getArg.apply(req,"password");
        RetMessage<String> retMessage = loginService.login(phone, password, null);
        return retMessage.toJson();
    }

    @SetUtf8
    @LoginCheck
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
    @LoginCheck
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
    @SetUtf8
    @ResponseBody
    @RequestMapping (value = "sendValidateCode", produces = "application/json;charset=utf-8")
    public String sendValidateCode(HttpServletRequest req, HttpServletResponse res) {
    	String phone=getArg.apply(req, "phone");
    	return loginService.sendValidateCode(phone).toJson();
    }
    @RequestMapping(value = "getPicCode")
    public void getPicValidateCode(HttpServletRequest req,HttpServletResponse res){
        String sessionId=req.getRemoteHost()+req.getRemoteUser();
        String code= PictureVerifyUtil.sendVerifyCode(res);
        String key= RedisKeyAssist.getVerificateCodeKey(sessionId);
        redisService.set(key,code,picCodeLife);
    }
    @SetUtf8
    @ResponseBody
    @RequestMapping (value = "weChatLogin", produces = "application/json;charset=utf-8")
    public String  weChatlogin(HttpServletRequest req, HttpServletResponse res) {
        String phone = getArg.apply(req, "phone");
        String password = getArg.apply(req, "password");
        RetMessage<String> retMessage = loginService.login(phone, password, null);
        return retMessage.toJson();
    }
}
