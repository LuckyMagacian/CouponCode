package com.lanxi.couponcode.impl.aop;

import com.lanxi.couponcode.impl.newservice.CacheService;
import com.lanxi.couponcode.impl.newservice.RedisEnhancedService;
import com.lanxi.couponcode.spi.service.RedisService;
import com.lanxi.couponcode.spi.aop.AopJob;
import com.lanxi.couponcode.spi.assist.RedisKeyAssist;
import com.lanxi.couponcode.spi.consts.annotations.Cache;
import com.lanxi.couponcode.spi.consts.annotations.DelCache;
import com.lanxi.util.entity.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by yangyuanjian on 2017/11/17.
 */
@Aspect
@Component
public class DealCache {
    @Resource
    private CacheService cacheService;
    @Resource(name="redisService")
    private RedisService redisService;
    @Resource
    private RedisEnhancedService redisEnhancedService;
    /**
     * 默认缓存时间 1小时
     */
    private static final Long defaultCacheTime = 1L * 60 * 60 * 1000;

    @Pointcut ("@annotation(com.lanxi.couponcode.spi.consts.annotations.Cache)")
    private void cache() {
    }

    @Pointcut ("@annotation(com.lanxi.couponcode.spi.consts.annotations.DelCache)")
    private void delCache() {
    }

    @Around ("cache()")
    public <T> T fromCache(ProceedingJoinPoint joinPoint) {
        LogFactory.debug(this, "---------------------------aop fromcache is working---------------------------");
        AopJob<T> job = (clazz, method, args, point) -> {
            //获取方法参数
            Parameter[] parameters = method.getParameters();
            Annotation cache = method.getAnnotation(Cache.class);
            List<String> exclusions = Arrays.asList(((Cache) cache).exclusionArgs());
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < parameters.length; i++) {
                if (exclusions.contains(parameters[i].getName()))
                    continue;
                builder.append(parameters[i].getName() + "=" + args[i] + "&");
            }
            String key = RedisKeyAssist.getMethodCacheKey(clazz.getName(), method.getName(), builder.substring(0, builder.length() - 1));
            T t = (T) cacheService.getCache(key);
            try {
                if (t == null) {
                    LogFactory.debug(this, "result from database ! class:[" + clazz.getName() + "],method:[" + method.getName() + "],args:[" + (args == null ? null : Arrays.asList(args)) + "]");
                    t = (T) point.proceed(args);
                    cacheService.addCache(key, (Serializable) t, defaultCacheTime);
                }
                return t;
            } catch (Throwable throwable) {

                LogFactory.error(this, "exception occurred in [" + DealCache.class.getName() + "] [fromCache] when invoke class:[" + clazz.getName() + "],method:[" + method.getName() + "],args:[" + (args == null ? null : Arrays.asList(args)) + "]");
                if (t == null) {
                    LogFactory.debug(this, "result from database ! class:[" + clazz.getName() + "],method:[" + method.getName() + "],args:[" + (args == null ? null : Arrays.asList(args)) + "]");
                    try {
                        t = (T) point.proceed(args);
                    } catch (Throwable throwable1) {
                        throwable1.printStackTrace();
                    }
                    cacheService.addCache(key, (Serializable) t, defaultCacheTime);
                }
                return t;
            }
        };
        return AopJob.workJob(job, joinPoint);
    }

    @Around ("delCache()")
    public <T> T removeCache(ProceedingJoinPoint joinPoint) {
        LogFactory.debug(this, "---------------------------aop delcache is working---------------------------");
        AopJob<T> job = (clazz, method, args, point) -> {
            T t = null;
            try {
                //获取@DelCache上的参数值
                Annotation delCache = method.getAnnotation(DelCache.class);
                //获取要删除的缓存的key数组
                String[] keys = ((DelCache) delCache).value();
                //删除对应的redis缓存
                Stream.of(keys).forEach(redisService::delKeyByPattern);
                //调用要执行的方法
                t = (T) joinPoint.proceed(args);
                return t;
            } catch (Throwable e) {
                LogFactory.error(this, "exception occurred in [" + DealCache.class.getName() + "] [removeCache] when invoke class:[" + clazz.getName() + "],method:[" + method.getName() + "],args:[" + args == null ? null : Arrays.asList(args) + "]");
                return t;
            }
        };
        return AopJob.workJob(job, joinPoint);
    }

}
