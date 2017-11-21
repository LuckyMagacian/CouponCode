package com.lanxi.couponcode.view;

import com.lanxi.couponcode.spi.consts.annotations.SetUtf8;
import com.lanxi.couponcode.spi.service.MerchantService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 商户管理端
 * Created by yangyuanjian on 2017/11/20.
 */
public class MerchantManageController {
    @Resource
    private MerchantService merchantService;

    @SetUtf8
    @ResponseBody
    @RequestMapping(value = "",produces="application/json;charset=utf-8")
    public String addMerchant(HttpServletRequest req, HttpServletResponse res){
        String merchantName=req.getParameter("");
        String workAddress = null;
        String detailAddress = null;
        String operaterIdStr = null;
        Long operaterId=Long.valueOf(operaterIdStr);
       return  merchantService.addMerchant(merchantName,workAddress,detailAddress,operaterId).toJson();
    }
}
