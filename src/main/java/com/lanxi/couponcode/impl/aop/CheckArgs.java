package com.lanxi.couponcode.impl.aop;

import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.impl.newservice.DaoService;
import com.lanxi.util.entity.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangyuanjian on 2017/11/10.
 */
@Aspect
@Component
public class CheckArgs {
    @Resource
    private DaoService dao;

    public  RetMessage checkParams(ProceedingJoinPoint joinPoint){
        Serializable t=null;
        //获取代理方法参数值
        Object[] args=joinPoint.getArgs();
        //获取代理目标类
        Class clazz= joinPoint.getTarget().getClass();
        RetMessage<Serializable> message=new RetMessage();
        Method method=null;
        try {
            //获取代理方法对象
            method = ((MethodSignature) joinPoint.getSignature()).getMethod();
//            Field methodInvocation=((MethodInvocationProceedingJoinPoint)joinPoint).getClass().getDeclaredField("methodInvocation");
//            methodInvocation.setAccessible(true);
//            method=((ReflectiveMethodInvocation)methodInvocation.get(joinPoint)).getMethod();
            //记录参数
            LogFactory.info(clazz, "校验:\n[" + clazz.getName() + "]\n的\n[" + method + "]\n方法\n的参数,参数:\n[" + Arrays.asList(args) + "]");
            Map<String,Object> map=new HashMap<>();
            Parameter[]  parameters=method.getParameters();
            for(int i=0;i<parameters.length;i++){
                String paraName=parameters[i].getName();
                Object value=args[i];
                switch (paraName){
                    case "accountId":{};
                    case "operaterId":{};break;
                    case "merchantId":{};break;
                    case "commodityId":{};break;
                    case "shopId":{};break;
                    case "requestId":{};break;
                    case "couponCodeId":{};break;
                    case "operateRecordId":{};break;
                    case "orderId":{};break;
                    case "verificationRecordId":{};break;
                }

            }







            //调用方法
            t = (Serializable) joinPoint.proceed(args);
            message.setDetail(t);
            return message;
        }catch (Throwable e){
            return message;
        }
    }
}
