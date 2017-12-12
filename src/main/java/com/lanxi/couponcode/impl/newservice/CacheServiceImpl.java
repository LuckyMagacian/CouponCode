package com.lanxi.couponcode.impl.newservice;

import com.lanxi.couponcode.spi.assist.RedisKeyAssist;
import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.util.utils.LoggerUtil;
import org.springframework.stereotype.Service;
import static com.lanxi.couponcode.spi.assist.SerializeAssist.*;
import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * Created by yangyuanjian on 2017/11/23.
 */
@Service("cacheService")
@EasyLog (LoggerUtil.LogLevel.INFO)
public class CacheServiceImpl implements CacheService{

    @Resource
    private RedisService redisService;

    @Override
    public boolean addCache(String key, Serializable value, Long lifecycle) {
        if(value==null)
            return false;
        final byte[] bytes=serialize(value);
        return redisService.set(key,bytes,lifecycle);
    }

    @Override
    public boolean addCache(String className, String methodName, List<Object> args, Serializable value, Long lifecycle) {
        final String key=RedisKeyAssist.getMethodCacheKey(className,methodName,args);
        return addCache(key,value,lifecycle);
    }

    @Override
    public Serializable getCache(String className, String methodName, List<Object> args) {
        final String key=RedisKeyAssist.getMethodCacheKey(className,methodName,args);
        return getCache(key);
    }

    @Override
    public Serializable getCache(String key) {
        byte[] bytes = redisService.getBytes(key);
        if(bytes==null)
            return null;
        return unserialize(bytes);
    }

    @Override
    public boolean delCache(String className, String methodName, List<Object> args) {
        final String key=RedisKeyAssist.getMethodCacheKey(className,methodName,args);
        return redisService.del(key);
    }

    @Override
    public boolean delCache(String className, String methodName) {
        final String key=RedisKeyAssist.getMethodCacheKey(className,methodName,"*");
        return redisService.delKeyByPattern(key);
    }

    @Override
    public boolean delCache(String className) {
        final String key=RedisKeyAssist.getMethodCacheKey(className,"","*");
        return redisService.delKeyByPattern(key);
    }

    @Override
    public boolean delAllCache() {
        final String key=RedisKeyAssist.getMethodCacheKey("","","*");
        return redisService.delKeyByPattern(key);
    }

//
//    @Override
//    public Boolean addMethodCache(String className, String methodName, List<Object> args, String value, Long lifeCycle) {
//        String key=RedisKeyAssist.getMethodCacheKey(className,methodName,args);
//        return redisService.set(key,value,lifeCycle);
//    }
//
//    @Override
//    public Boolean addCache(String key, String value, Long lifeCycle) {
//        return redisService.set(key,value,lifeCycle);
//    }
//
//    @Override
//    public Boolean addCache(String mapName, String fieldName, String value, Long lifeCycle) {
//        return addCache(mapName+fieldName,value,lifeCycle);
//    }
//
//    @Override
//    public Boolean addCache(String mapName, String fieldName, byte[] value, Long lifeCycle) {
//        return addCache(mapName+fieldName,value,lifeCycle);
//    }
//
//    @Override
//    public Boolean addCache(String key, byte[] value, Long lifeCycle) {
//        return redisService.set(key,value,lifeCycle);
//    }
//
//    @Override
//    public String getCache(String key) {
//        return redisService.get(key);
//    }
//
//    @Override
//    public byte[] getCacheBytes(String key) {
//        return redisService.getBytes(key);
//    }
//
//    @Override
//    public String getCache(String mapName, String fieldName) {
//        return redisService.get(mapName+fieldName);
//    }
//
//    @Override
//    public byte[] getCacheBytes(String mapName, String fieldName) {
//        return redisService.getBytes(mapName+fieldName);
//    }
//
//    @Override
//    public String getMethodCache(String className, String methodName, List<Object> args) {
//        return redisService.get(RedisKeyAssist.getMethodCacheKey(className,methodName,args));
//    }
//
//    @Override
//    public Boolean delCache(String keyName) {
//        return redisService.del(keyName);
//    }
//
//    @Override
//    public Boolean delCache(String mapName, String fieldName) {
//        return redisService.del(mapName+fieldName);
//    }
//
//    @Override
//    public Boolean delMethodCache(String className, String methodName) {
//        return redisService.delKeyByPattern(RedisKeyAssist.getMethodCacheKey(className,methodName,"*"));
//    }
//
//    @Override
//    public Boolean delMethodCache(String className, String methodName, List<Object> args) {
//        return redisService.del(RedisKeyAssist.getMethodCacheKey(className,methodName,args));
//    }


}
