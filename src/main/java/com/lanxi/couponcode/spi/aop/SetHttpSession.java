package com.lanxi.couponcode.spi.aop;

import com.lanxi.couponcode.spi.consts.annotations.SetUtf8;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by yangyuanjian on 2017/11/6.
 */
@Aspect
public class SetHttpSession {
    @Pointcut
    public void setReqUtf8(){};
    @Pointcut
    public void setResUtf8(){};
    @Pointcut
    public void setRestExcelFile(){};
    @Pointcut
    public void setResJsonp(){};

    @Around("")
    public Object applySetReqToControllerService(){
        return null;
    };
}
