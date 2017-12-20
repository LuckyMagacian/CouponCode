package com.lanxi.couponcode.spi.aop;

import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.util.entity.LogFactory;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Created by yangyuanjian on 12/15/2017.
 */
@Aspect
@Component
public class DealException {
    @Pointcut ("execution(public * com.lanxi.couponcode.view.*Controller.*(..))")
    private void codeView() {
    }

    @Pointcut ("execution(public * com.lanxi.couponcode.impl.newcontroller.*Controller.*(..))")
    private void codeImpl() {
    }

    @AfterThrowing (value = "codeView()", throwing = "e")
    public String dealExceptionView(Throwable e) {
        LogFactory.error(DealException.class, "发现异常!", e);
        return new RetMessage(RetCodeEnum.error, "发生异常!", null).toJson();
    }

    @AfterThrowing (value = "codeImpl()", throwing = "e")
    public RetMessage dealExceptionImpl(Throwable e) {
        LogFactory.error(DealException.class, "发现异常!", e);
        return new RetMessage(RetCodeEnum.error, "发生异常!", null);
    }
}
