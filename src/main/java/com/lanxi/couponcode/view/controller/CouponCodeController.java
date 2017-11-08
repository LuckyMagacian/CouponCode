package com.lanxi.couponcode.view.controller;

import com.lanxi.couponcode.impl.assist.RetMessage;
import com.lanxi.couponcode.impl.entity.Account;
import com.lanxi.couponcode.impl.entity.Commodity;
import com.lanxi.couponcode.impl.service.CouponCodeService;
import com.lanxi.couponcode.impl.service.DaoService;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static com.lanxi.couponcode.impl.config.ConstConfig.*;
/**
 * Created by yangyuanjian on 2017/11/6.
 */
public class CouponCodeController {
    @Resource
    private DaoService daoService;
    @Resource
    private CouponCodeService codeService;
    public String generateCode(HttpServletRequest req, HttpServletResponse res){
        //校验登录状态通过aop校验
        String operaterId=req.getParameter("accountId");
        String commodityId=req.getParameter("commodityId");
        Account account= (Account) daoService.getAccountDao().selectById(operaterId);
        Commodity commodity= (Commodity) daoService.getCommodityDao().selectById(commodityId);
        if(account==null)
            return new RetMessage(RetCodeEnum.fail.toString(),"账户不存在!",null).toJson();
        Long merchantId=account.getMerchantId();
        Long code=codeService.getnerateCode(merchantId,commodity.getCommodityId(),account.getMerchantName(),commodity.getCommodityName(),commodity.toJson(),commodity.getLifeTime());
        if(INVALID_LONG==code)
            return new RetMessage(RetCodeEnum.fail.toString(),"添加串码失败!",null).toJson();
        else
            return new RetMessage(RetCodeEnum.success.toString(),"添加串码成功!",null).toJson();

    }



}
