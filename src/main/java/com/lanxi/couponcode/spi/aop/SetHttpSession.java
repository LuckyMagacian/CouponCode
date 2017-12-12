package com.lanxi.couponcode.spi.aop;

import com.lanxi.couponcode.spi.assist.ReflectAssist;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.util.entity.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by yangyuanjian on 2017/11/6.
 */
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SetHttpSession {
    @Pointcut("@annotation(com.lanxi.couponcode.spi.consts.annotations.SetUtf8)")
    public void setUtf8(){};

    @Around("setUtf8()")
    public <T> T applySetReqToControllerService(ProceedingJoinPoint joinPoint){
        LogFactory.debug(this,"---------------------------aop sethttpsession is working---------------------------");
        //获取代理方法参数值
        Object[] args=joinPoint.getArgs();
        Class clazz= joinPoint.getTarget().getClass();
        Method method= ReflectAssist.getTargetMethod(joinPoint);
        LogFactory.debug(this.getClass(), "try sethttpsession for class:[" + clazz.getName() + "] method:[" + method + "] arg:[" + Arrays.asList(args) + "]");

        RetMessage message=new RetMessage();
        T t=null;
        try {
            for(Object each:args){
                if(each instanceof HttpServletRequest){
                    HttpServletRequest request= (HttpServletRequest) each;
                    request.setCharacterEncoding("utf-8");

                }else if(each instanceof HttpServletResponse){
                    HttpServletResponse response= (HttpServletResponse) each;
                    response.setCharacterEncoding("utf-8");
                    response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
                    response.setHeader("Access-Control-Max-Age", "3600"); //设置过期时间
                    response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, client_id, uuid, Authorization");
                    response.setHeader("Access-Control-Allow-Origin", "*");
                }
            }
            LogFactory.debug(clazz,"set  class:["+clazz.getName()+"]method:["+method.getName()+"] charset utf-8 !");
            return (T) joinPoint.proceed(args);
        }catch (Throwable throwable){
            LogFactory.error(this, "exception occurred in ["+this.getClass().getName()+"] [checkLogin] when invoke class:["+clazz.getName()+"],method:["+method.getName()+"],args:["+(args==null?null: Arrays.asList(args))+"]");
            message.setAll(RetCodeEnum.error,"设置消息头时发生异常!",null);
            return (T) message;
        }
    }
}
