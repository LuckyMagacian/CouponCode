package com.lanxi.couponcode.spi.aop;

import org.springframework.core.Ordered;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by yangyuanjian on 12/7/2017.
 */
public abstract class AopOrder {
    /**私有化默认构造方法,不允许继承该类*/
    private AopOrder(){};
    /**无参的任务*/
    private interface HiddenJobNoArg{
        void doJob();
    };
    /**有参的任务*/
    private interface HiddenJobWithArg<T>{
       void doJob(T t);
    }
    /**保存aop的载入顺序*/
    private static final Map<Class,Integer> aops=new ConcurrentHashMap<>();
    /**addAop方法的调用环境记录*/
    private static final Set<String> envs=new HashSet<>();
    /**spring 排序等级默认起始点*/
    private static int order=-10000;
    /**addAop方法调用环境限制*/
    public static int ENV_SIZE_LIMIT=3;

    /**校验aop类参数是否为null*/
    private static final HiddenJobWithArg<Class> isNull=e->{
        if(e==null)
            throw new IllegalArgumentException("arg : aopClass can't be null !");
    };
    /**校验方法参数是否是class*/
    private static final HiddenJobWithArg<Class> isClass=e->{
        if(!(e instanceof Class))
            throw new IllegalArgumentException("arg : aopClass must be a class !");
    };
    /**添加方法运行环境*/
    private static final HiddenJobNoArg addEnv=()->{
        envs.add(Thread.currentThread().getStackTrace()[3].getClassName());
    };
    /**限制添加aop的环境数量,避免分散添加导致顺序与代码预期不附等问题*/
    private static final HiddenJobNoArg checkEnvLmit=()->{
        if(ENV_SIZE_LIMIT<=envs.size())
            throw new IllegalStateException("addAop can only invoke in "+ENV_SIZE_LIMIT+" class ! Change AopOrder.ENV_SIZE_LIMIT to plus or invoke in "+envs);
    };
    /**校验方法执行环境是否是AopJob*/
    @Deprecated
    private static final HiddenJobNoArg inAopJob=()->{
        String  envClassName=Thread.currentThread().getStackTrace()[3].getClassName();
        try {
            if(!AopJob.class.isAssignableFrom(Class.forName(envClassName)))
                throw new IllegalStateException("method : addAop can only invoke in class AopJob or it's subclass ! current env is :"+envClassName);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("class not found :"+envClassName,e);
        }
    };

    /**添加aop类*/
    public static final synchronized void addAop(Class aopClass){
        isNull.doJob(aopClass);
        isClass.doJob(aopClass);
        checkEnvLmit.doJob();
        addEnv.doJob();
        order++;
        aops.put(aopClass,order);
    }
    /**获取aop类的执行顺序*/
    public static synchronized final int getOrder(Class aopClass){
        isNull.doJob(aopClass);
        isClass.doJob(aopClass);
        if(!aops.containsKey(aopClass)){
            return Ordered.LOWEST_PRECEDENCE;
        }else{
            return aops.entrySet().parallelStream().filter(e->aopClass.equals(e.getKey())).map(e->e.getValue()).findFirst().orElse(Ordered.LOWEST_PRECEDENCE);
        }
    }
    /**获取添加的aop类及其优先级*/
    public static synchronized Map<String,Integer> getAops(){
        final Map<String,Integer> map=new LinkedHashMap<>();
        aops.entrySet().stream().forEach(e->map.put(e.getKey().getName(),e.getValue()));
        return Collections.unmodifiableMap(map);
    }
    /**获取添加aop环境限制数量*/
    public static int getEnvSizeLimit() {
        return ENV_SIZE_LIMIT;
    }
    /**设置添加aop环境限制数量*/
    public static void setEnvSizeLimit(int envSizeLimit) {
        ENV_SIZE_LIMIT = envSizeLimit;
    }
    /**获取addAop已有的调用环境*/
    public static Set<String> getEnvs() {
        return envs;
    }
}
