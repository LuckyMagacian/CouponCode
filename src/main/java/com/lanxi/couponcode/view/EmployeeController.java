package com.lanxi.couponcode.view;

import com.lanxi.couponcode.spi.consts.annotations.SetUtf8;
import com.lanxi.couponcode.spi.service.CouponService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.lanxi.couponcode.spi.assist.ArgAssist.getArg;
import static com.lanxi.couponcode.spi.assist.ArgAssist.parseArg;

/**
 * 员工端
 * Created by yangyuanjian on 2017/11/20.
 */
@Controller
@RequestMapping("employee")
public class EmployeeController {

    @Resource
    private CouponService codeService;

    @RequestMapping
    public void pic1(){

    }
    @RequestMapping
    public void pic2(){

    }


    @SetUtf8
    @ResponseBody
    @RequestMapping(value = "verifyCode",produces = "application/json;charset=utf-8")
    public String verifyCode(HttpServletRequest req, HttpServletResponse res){
        String codIdStr=getArg.apply(req,"codeId");
        String operaterIdStr=getArg.apply(req,"operaterId");
        String codeStr=getArg.apply(req,"code");


        Long codeId=parseArg(codIdStr,Long.class);
        Long operaterId=parseArg(operaterIdStr,Long.class);
        Long code=parseArg(codeStr,Long.class);

        if(codeId==null)
            return codeService.verificateCode(code,null,operaterId).toJson();
        else
            return codeService.verificateCode(codeId,operaterId).toJson();
    }
    @SetUtf8
    @ResponseBody
    @RequestMapping(value = "queryCode",produces = "application/json;charset=utf-8")
    public String queryCode(HttpServletRequest req,HttpServletResponse res){
//        String codIdStr=getArg.apply(req,"codeId");
        String operaterIdStr=getArg.apply(req,"operaterId");
        String codeStr=getArg.apply(req,"code");


//        Long codeId=parseArg(codIdStr,Long.class);
        Long operaterId=parseArg(operaterIdStr,Long.class);
        Long code=parseArg(codeStr,Long.class);

//        if(codeId==null)
            return codeService.couponCodeInfo(code,null,operaterId).toJson();
//        else
//            return codeService.couponCodeInfo(codeId,operaterId).toJson();
    }

}
