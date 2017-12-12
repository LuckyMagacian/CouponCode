package com.lanxi.couponcode.impl.newservice;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yangyuanjian on 2017/11/23.
 */
public interface CacheService {
//    Boolean addMethodCache(String className,String methodName, List<Object> args, String value,Long lifeCycle);
//    Boolean addCache(String key,String value,Long lifeCycle);
//    Boolean addCache(String mapName,String fieldName,String value,Long lifeCycle);
//
//    Boolean addCache(String mapName,String fieldName,byte[] value,Long lifeCycle);
//    Boolean addCache(String key,byte[] value,Long lifeCycle);
//
//    String getCache(String key);
//    byte[] getCacheBytes(String key);
//    String getCache(String mapName,String fieldName);
//    byte[] getCacheBytes(String mapName,String fieldName);
//    String getMethodCache(String className,String methodName,List<Object> args);
//    Boolean delCache(String keyName);
//    Boolean delCache(String mapName,String fieldName);
//    Boolean delMethodCache(String className,String methodName);
//    Boolean delMethodCache(String className,String methodName,List<Object> args);

    //1缓存方法结果,Key为类名,方法名,参数,
    boolean addCache(String key, Serializable value, Long lifecycle);
    boolean addCache(String className, String methodName, List<Object> args, Serializable value, Long lifecycle);
    //获取缓存,key为类名,方法名,参数
    Serializable getCache(String className, String methodName, List<Object> args);
    //获取缓存,key
    Serializable getCache(String key);
    //删除缓存,key为类名,方法名,参数名
    boolean delCache(String className, String methodName, List<Object> args);
    //删除缓存,key为类名,方法名
    boolean delCache(String className, String methodName);
    //删除缓存,key为类名,
    boolean delCache(String className);
    //删除所有缓存
    boolean delAllCache();
}
