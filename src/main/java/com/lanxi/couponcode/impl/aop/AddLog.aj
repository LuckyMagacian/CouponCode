package com.lanxi.couponcode.impl.aop;

import com.lanxi.couponcode.impl.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.util.entity.LogFactory;
import com.lanxi.util.utils.AopUtil;
import org.aopalliance.intercept.Joinpoint;
import org.apache.ibatis.logging.LogException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.aop.framework.AopContext;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * Created by yangyuanjian on 2017/11/10.
 */
@Aspect
public class AddLog {
    @Pointcut("")
    public void debugAll(){}

    public void debugService(){}
    public void infoService(){};

    @Around("")
    public void logVoid(ProceedingJoinPoint joinPoint){
        Object[] args=joinPoint.getArgs();
        Class clazz= joinPoint.getTarget().getClass();
        String methodName=joinPoint.getSignature().getName();
        try {
            LogFactory.info(clazz,"尝试调用["+clazz.getName()+"]的["+methodName+"]方法,参数["+ Arrays.asList(args)+"]");

            LogFactory.info(clazz,"["+clazz.getName()+"]的["+methodName+"]方法执行完成!参数["+ Arrays.asList(args)+"]");
        } catch (Throwable e) {
            LogFactory.error(clazz,"调用["+clazz.getName()+"]的["+methodName+"]方法时,发生异常!参数["+ Arrays.asList(args)+"]",e);
        }
    }

    @Around("")
    public <T> T logReturn(ProceedingJoinPoint joinPoint){
        T t=null;
        Object[] args=joinPoint.getArgs();
        Class clazz= joinPoint.getTarget().getClass();
        String methodName=joinPoint.getSignature().getName();
        try {
            LogFactory.info(clazz,"尝试调用["+clazz.getName()+"]的["+methodName+"]方法,参数["+ Arrays.asList(args)+"]");
            t= (T) joinPoint.proceed(args);
            if(t instanceof Collection){
                Collection collection= (Collection) t;
                LogFactory.info(clazz,"["+clazz.getName()+"]的["+methodName+"]方法的返回结果个数为["+collection.size()+"],参数["+ Arrays.asList(args)+"]");
                LogFactory.debug(clazz,"["+clazz.getName()+"]的["+methodName+"]方法的返回结果为["+t+"],参数["+ Arrays.asList(args)+"]");
                return t;
            }else if(t instanceof Map){
                Map map= (Map) t;
                LogFactory.info(clazz,"["+clazz.getName()+"]的["+methodName+"]方法的返回结果个数为["+map.size()+"],参数["+ Arrays.asList(args)+"]");
                LogFactory.debug(clazz,"["+clazz.getName()+"]的["+methodName+"]方法的返回结果为["+t+"],参数["+ Arrays.asList(args)+"]");
                return t;
            }else{
                LogFactory.info(clazz,"["+clazz.getName()+"]的["+methodName+"]方法的返回结果为["+t+"],参数["+ Arrays.asList(args)+"]");
                return t;
            }
        } catch (Throwable e) {
            LogFactory.error(clazz,"调用["+clazz.getName()+"]的["+methodName+"]方法时,发生异常!参数["+ Arrays.asList(args)+"]",e);
            return t;
        }
    }
}
