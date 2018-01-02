package com.lanxi.couponcode.spi.aop;

import com.lanxi.common.interfaces.RedisCacheServiceInterface;
import com.lanxi.couponcode.spi.assist.ArgAssist;
import com.lanxi.couponcode.spi.assist.PredicateAssist;
import com.lanxi.couponcode.spi.assist.RedisKeyAssist;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Created by yangyuanjian on 12/7/2017.
 */
@Component
@Order (Ordered.HIGHEST_PRECEDENCE + 100)
@Aspect
public class CheckLogin {
    @Resource
    private RedisCacheServiceInterface redis;

    @Pointcut ("@annotation(com.lanxi.couponcode.spi.consts.annotations.LoginCheck)")
    private void check() {
    }

    @Around ("check()")
    public String checkLogin(ProceedingJoinPoint joinPoint) {
        LogFactory.debug(this, "---------------------------aop checklogin is working---------------------------");
        AopJob<String> job = (clazz, method, args, point) -> {
            String t = null;
            RetMessage message = new RetMessage();
            LogFactory.debug(this.getClass(), "try checklogin  for class:[" + clazz.getName() + "] method:[" + method + "] arg:[" + Arrays.asList(args) + "]");
            try {
                HttpServletRequest req = (HttpServletRequest) Stream.of(args).filter(e -> e instanceof HttpServletRequest).findAny().orElse(null);
                if (req == null)
                    throw new IllegalArgumentException("annotation @LoginCheck must apply on a method which with arg type HttpServletRequest");
                String operaterIdStr = ArgAssist.getArg.apply(req,"operaterId");
                if (operaterIdStr == null)
                    operaterIdStr = ArgAssist.getArg.apply(req,"accountId");
                String token = ArgAssist.getArg.apply(req,"token");
                if (!PredicateAssist.isId.test(operaterIdStr)) {
                    message.setAll(RetCodeEnum.fail, "illegalargument operaterId [" + operaterIdStr + "]!", null);
                } else if (token == null || token.isEmpty()) {
                    message.setAll(RetCodeEnum.fail, "token can't be empty !", null);
                } else {
                    if (!checkLogin(token, operaterIdStr)) {
                        message.setAll(RetCodeEnum.unknown, "not login !", null);
                    } else {
                        t = (String) joinPoint.proceed(args);
                        return t;
                    }
                }
                return message.toJson();
            } catch (Throwable throwable) {
                //记录异常
                LogFactory.error(this, "exception occurred in [" + CheckLogin.class.getName() + "] [checkLogin] when invoke class:[" + clazz.getName() + "],method:[" + method.getName() + "],args:[" + (args == null ? null : Arrays.asList(args)) + "]", throwable);
                message.setAll(RetCodeEnum.error, "校验登录时发生异常!", null);
                return message.toJson();
            }
        };
        return AopJob.workJob(job, joinPoint);
    }

    public boolean checkLogin(String token, String operaterId) {
        String key = RedisKeyAssist.getLoginKey(Long.parseLong(operaterId));
        String cacheToken = redis.get(key);
        if (cacheToken == null || !token.equals(cacheToken)) {
            return false;
        } else {
            redis.setKeyLife(key, 3 * 60 * 60 * 1000);
            return true;
        }
    }
}
