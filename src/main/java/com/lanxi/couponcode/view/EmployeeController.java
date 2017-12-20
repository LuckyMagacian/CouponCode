package com.lanxi.couponcode.view;

import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.couponcode.spi.consts.annotations.LoginCheck;
import com.lanxi.couponcode.spi.consts.annotations.SetUtf8;
import com.lanxi.couponcode.spi.consts.enums.VerificationType;
import com.lanxi.couponcode.spi.service.CouponService;
import com.lanxi.util.utils.LoggerUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.lanxi.couponcode.spi.assist.ArgAssist.*;

/**
 * 员工端
 * Created by yangyuanjian on 2017/11/20.
 */
@Controller
@RequestMapping ("employee")
@EasyLog (LoggerUtil.LogLevel.INFO)
public class EmployeeController {

    @Resource (name = "codeControllerServiceRef")
    private CouponService codeService;

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "verifyCode", produces = "application/json;charset=utf-8")
    public String verifyCode(HttpServletRequest req, HttpServletResponse res) {
        String codIdStr = getArg.apply(req, "codeId");
        String operaterIdStr = getArg.apply(req, "operaterId");
        String codeStr = getArg.apply(req, "code");
        String verifyTypeStr = getArg.apply(req, "verificationType");

        Long codeId = parseArg(codIdStr, Long.class);
        Long operaterId = parseArg(operaterIdStr, Long.class);
        Long code = parseArg(codeStr, Long.class);
        VerificationType verifyType = toVerificationType.apply(verifyTypeStr);
        if (codeId == null)
            return codeService.verificateCode(code, null, operaterId, verifyType).toJson();
        else
            return codeService.verificateCode(codeId, operaterId, verifyType).toJson();
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "queryCode", produces = "application/json;charset=utf-8")
    public String queryCode(HttpServletRequest req, HttpServletResponse res) {
//        String codIdStr=getArg.apply(req,"codeId");
        String operaterIdStr = getArg.apply(req, "operaterId");
        String codeStr = getArg.apply(req, "code");


//        Long codeId=parseArg(codIdStr,Long.class);
        Long operaterId = parseArg(operaterIdStr, Long.class);
        Long code = parseArg(codeStr, Long.class);

//        if(codeId==null)
        return codeService.couponCodeInfo(null, code, operaterId).toJson();
//        else
//            return codeService.couponCodeInfo(codeId,operaterId).toJson();
    }

}
