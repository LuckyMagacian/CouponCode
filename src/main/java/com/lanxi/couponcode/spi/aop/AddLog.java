package com.lanxi.couponcode.spi.aop;

import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.util.entity.LogFactory;
import com.lanxi.util.utils.LoggerUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by yangyuanjian on 2017/11/10.
 */
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE+10)
public class AddLog {

    @Pointcut ("@within(com.lanxi.couponcode.spi.consts.annotations.EasyLog)")
    public void easyLogClass() {}
    @Pointcut ("@annotation(com.lanxi.couponcode.spi.consts.annotations.EasyLog)")
    public void easyLogMethod(){}

    @Around ("easyLogClass()||easyLogMethod()")
    public <T> T logDefault(ProceedingJoinPoint joinPoint) {
        LogFactory.debug(this,"---------------------------aop addlog is working---------------------------");
        AopJob<T> job = (clazz, method, args, point) -> {
            T t = null;
            try {
                LoggerUtil.LogLevel level = getLogLevel(method);
                List<Object> argList=Arrays.asList(args);
                HttpServletRequest req= (HttpServletRequest) argList.parallelStream().filter(e->e instanceof HttpServletRequest).findAny().orElse(null);
                if(req!=null){
                    LogFactory.debug(this.getClass(), "try invoke  class:[" + clazz.getName() + "] method:[" + method + "] arg:[" + req.getParameterMap() + "]");
                }
                else{
                    LogFactory.debug(this.getClass(), "try invoke  class:[" + clazz.getName() + "] method:[" + method + "] arg:[" + argList + "]");
                }
                t = (T) joinPoint.proceed(args);
                //若是集合类型则记录返回的数量,否则记录返回结果
                if (t instanceof Collection) {
                    Collection collection = (Collection) t;
                    log(clazz, "class:[" + clazz.getName() + "] method:[" + method.getName() + "] resultSize:[" + collection.size() + "] arg:[" + Arrays.asList(args) + "]", level);
                    LogFactory.debug(clazz, "class:[" + clazz.getName() + "] method:[" + method.getName() + "] result:[" + collection + "] arg:[" + Arrays.asList(args) + "]");
                    return t;
                } else if (t instanceof Map) {
                    Map map = (Map) t;
                    log(clazz, "class:[" + clazz.getName() + "] method:[" + method.getName() + "] resultSize:[" + map.size() + "] arg:[" + Arrays.asList(args) + "]", level);
                    LogFactory.debug(clazz, "class:[" + clazz.getName() + "] method:[" + method.getName() + "] result:[" + map + "] arg:[" + Arrays.asList(args) + "]");
                    return t;
                } else {
                    log(clazz, "invoke  class:[" + clazz.getName() + "] method:[" + method.getName() + "] arg:[" + Arrays.asList(args) + "] result:[" + t + "]", level);
                    return t;
                }
            } catch (Throwable throwable) {
                //记录异常
                LogFactory.error(clazz, "occured exception ["+throwable+"]!class:[" + clazz.getName() + "] method[" + method + "]  arg:[" + Arrays.asList(args) + "]", throwable);
                if(t!=null)
                    return t;
                else
                    return (T)new RetMessage(RetCodeEnum.error,"添加日志时发生异常!",null);
            }
        };
        return AopJob.workJob(job, joinPoint);
    }

    public void log(Class clazz, String mesage, LoggerUtil.LogLevel level) {
        log(clazz, mesage, null, level);
    }

    public void log(Class clazz, String message, Throwable e, LoggerUtil.LogLevel level) {
        switch (level) {
            case DEBUG:
                logDebug(clazz, message, e);
                break;
            case INFO:
                logInfo(clazz, message, e);
                break;
            case WARN:
                logWarn(clazz, message, e);
                break;
            case ERROR:
                logError(clazz, message, e);
                break;
            default:
                logInfo(clazz, message, e);
                break;
        }
    }

    public void logDebug(Class clazz, String message, Throwable e) {
        if (null == e)
            LogFactory.debug(clazz, message);
        else
            LogFactory.debug(clazz, message, e);
    }

    public void logInfo(Class clazz, String message, Throwable e) {
        if (null == e)
            LogFactory.info(clazz, message);
        else
            LogFactory.info(clazz, message, e);
    }

    public void logWarn(Class clazz, String message, Throwable e) {
        if (null == e)
            LogFactory.warn(clazz, message);
        else
            LogFactory.warn(clazz, message, e);
    }

    public void logError(Class clazz, String message, Throwable e) {
        if (null == e)
            LogFactory.error(clazz, message);
        else
            LogFactory.error(clazz, message, e);
    }


    /**
     * 获取方法上的日志等级
     *
     * @param method
     * @return
     */
    private LoggerUtil.LogLevel getLogLevel(Method method) {
        Annotation level = method.getAnnotation(EasyLog.class);
        if (level == null) {
            return getLogLevel(method.getDeclaringClass());
        } else
            return ((EasyLog) level).value();

    }

    /**
     * 获取类上的日志等级
     *
     * @param clazz
     * @return
     */
    private LoggerUtil.LogLevel getLogLevel(Class clazz) {
        Annotation level = clazz.getAnnotation(EasyLog.class);
        if (level == null)
            return LoggerUtil.LogLevel.DEBUG;
        else
            return ((EasyLog) level).value();
    }

    //    @Around("easyLog()")
//    public <T> T logDefault(ProceedingJoinPoint joinPoint){
//        T t=null;
//        //获取代理方法参数值
//        Object[] args=joinPoint.getArgs();
//        //获取代理目标类
//        Class clazz= joinPoint.getTarget().getClass();
//        Method method=null;
//        try {
//            //获取代理方法对象
//            method=((MethodSignature)joinPoint.getSignature()).getMethod();
//            LoggerUtil.LogLevel level=getLogLevel(method);
////            Field methodInvocation=((MethodInvocationProceedingJoinPoint)joinPoint).getClass().getDeclaredField("methodInvocation");
////            methodInvocation.setAccessible(true);
////            method=((ReflectiveMethodInvocation)methodInvocation.get(joinPoint)).getMethod();
//            //记录参数
//            log(clazz,"try invoke \nclass:["+clazz.getName()+"]\nmethod:["+method+"]\narg:["+ Arrays.asList(args)+"]",level);
//            //调用方法
//            t= (T) joinPoint.proceed(args);
//            //若是集合类型则记录返回的数量,否则记录返回结果
//            if(t instanceof Collection){
//                Collection collection= (Collection) t;
//                log(clazz,"class:["+clazz.getName()+"]\nmethod:["+method.getName()+"]\nresultSize:["+collection.size()+"]\narg:["+ Arrays.asList(args)+"]",level);
//                LogFactory.debug(clazz,"class:["+clazz.getName()+"]\nmethod:["+method.getName()+"]\nresult:["+collection+"]\narg:["+ Arrays.asList(args)+"]");
//                return t;
//            }else if(t instanceof Map){
//                Map map= (Map) t;
//                log(clazz,"class:["+clazz.getName()+"]\nmethod:["+method.getName()+"]\nresultSize:["+map.size()+"]\narg:["+ Arrays.asList(args)+"]",level);
//                LogFactory.debug(clazz,"class:["+clazz.getName()+"]\nmethod:["+method.getName()+"]\nresult:["+map+"]\narg:["+ Arrays.asList(args)+"]");
//                return t;
//            }else{
//                log(clazz,"class:["+clazz.getName()+"]\nmethod:["+method.getName()+"]\nresult:["+t+"]\narg:["+ Arrays.asList(args)+"]",level);
//                return t;
//            }
//        } catch (Throwable e) {
//            //记录异常
//            LogFactory.error(clazz,"class:["+clazz.getName()+"]\nmethod["+method+"] occured exception !\narg:["+ Arrays.asList(args)+"]",e);
//            return t;
//        }
//    }


//    /**
//     * 获取日志等级
//     * @param type
//     * @return
//     */
//    private LoggerUtil.LogLevel getLogLevel(GenericArrayType type){
//        if(type==null)
//            throw new IllegalArgumentException("arg: type can't be null !");
//        LoggerUtil.LogLevel level=null;
//        Object obj=type;
//        if(obj instanceof Class){
//            level=getLogLevel((Class) obj);
//        }
//        if(obj instanceof Method){
//            level=getLogLevel((Method)obj);
//        }
//        return level==null? LoggerUtil.LogLevel.INFO:level;
//    }

}
