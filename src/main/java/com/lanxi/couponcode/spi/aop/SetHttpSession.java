package com.lanxi.couponcode.spi.aop;

import com.lanxi.couponcode.spi.consts.annotations.SetUtf8;
import com.lanxi.util.entity.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Created by yangyuanjian on 2017/11/6.
 */
@Aspect
public class SetHttpSession {
    @Pointcut("within(com.lanxi.couponcode.spi.consts.annotations.SetUtf8)")
    public void setUtf8(){};

    @Around("setUtf8()")
    public void applySetReqToControllerService(ProceedingJoinPoint joinPoint){
        //获取代理方法参数值
        Object[] args=joinPoint.getArgs();
        Class clazz= joinPoint.getTarget().getClass();
        Method method=null;
        try {
            for(Object each:args){
                if(each instanceof HttpServletRequest){
                    HttpServletRequest request= (HttpServletRequest) each;
                    request.setCharacterEncoding("utf-8");
                }else if(each instanceof HttpServletResponse){
                    HttpServletResponse response= (HttpServletResponse) each;
                    response.setCharacterEncoding("utf-8");
                }
            }
            LogFactory.info(clazz,"set  class:["+clazz.getName()+"]\nmethod:["+method.getName()+"] charset utf-8 !");
        }catch (Throwable throwable){
            throw new RuntimeException("occured exception when set charset class:["+clazz.getName()+"]\nmethod:["+method.getName()+"]",throwable);
        }
    }
}
