package com.lanxi.couponcode.spi.aop;

import com.lanxi.couponcode.spi.consts.annotations.Trim;
import com.lanxi.util.entity.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.Parameter;
import java.util.Arrays;

/**
 * Created by yangyuanjian on 2017/11/24.
 */

public class DoTrim {
    @Pointcut ("@args(com.lanxi.couponcode.spi.consts.annotations.Trim)")
    private void trim() {
    }

    private <T> T doTrim(ProceedingJoinPoint joinPoint) {
        LogFactory.debug(this, "---------------------------aop dotrim is working---------------------------");
        AopJob<T> job = (clazz, method, args, point) -> {
            T t = null;
            try {
                Parameter[] params = method.getParameters();
                for (int i = 0; i < params.length; i++) {
                    Parameter current = params[i];
                    if (current.getAnnotation(Trim.class) != null) {
                        Object arg = args[i];
                        if (arg instanceof String) {
                            arg = ((String) arg).trim();
                            args[i] = arg;
                        }
                    }
                }
                t = (T) joinPoint.proceed(args);
                return t;
            } catch (Throwable e) {
                LogFactory.error(this, "exception occurred in [" + DoTrim.class.getName() + "] [doTrim] when invoke class:[" + clazz.getName() + "],method:[" + method.getName() + "],args:[" + args == null ? null : Arrays.asList(args) + "]");
                return t;
            }
        };
        return AopJob.workJob(job, joinPoint);
    }
}
