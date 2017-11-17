package com.lanxi.couponcode.test;

import com.alibaba.fastjson.JSON;
import com.lanxi.couponcode.impl.newservice.RedisServiceImpl;
import com.lanxi.couponcode.impl.service.ConfigServiceImpl;
import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.util.utils.LoggerUtil;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.lanxi.couponcode.spi.consts.enums.AccountStatus;
import com.lanxi.couponcode.spi.consts.enums.ShopStatus;
import com.lanxi.util.utils.ExcelUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by yangyuanjian on 2017/11/9.
 */
public class TestOther {

    @Test
    public void test1(){
        int[] i=new int[]{1,2,3};
        System.out.println(i.getClass());
        System.out.println(i instanceof Serializable);
        System.out.println(Array.class.isAssignableFrom(i.getClass()));
        System.out.println(Serializable.class.isAssignableFrom(ArrayList.class));
    }
    @Test
<<<<<<< HEAD
    public void test2(){
        JSON.toJSONString("");
    }
    @Test
    public void test3() throws NoSuchFieldException, IllegalAccessException {
        ConfigServiceImpl configService=new ConfigServiceImpl();
        configService.reload();
        Field field=RedisServiceImpl.class.getDeclaredField("config");
        RedisServiceImpl redisService=new RedisServiceImpl();
        field.setAccessible(true);
        field.set(redisService,configService);
        redisService.redisInit();
        System.out.println(redisService.set("移动","10086",1000L));
        System.out.println(redisService.exists("移动"));
    }

    void test5(){

    }

    @Test
    @EasyLog(LoggerUtil.LogLevel.DEBUG)
    public void test4() throws NoSuchMethodException {
        Method method=TestOther.class.getDeclaredMethod("test5");
        Object obj=method;
        if(obj instanceof Method){
            Annotation level=method.getAnnotation(EasyLog.class);
            if(level!=null){
                EasyLog log= (EasyLog) level;
                System.out.println(log.value());
            }
        }
        System.out.println(method.toString());
        System.out.println(method.getDeclaringClass());


//        System.out.println(method.gettype);
    }
=======
    public void test2() {
    	System.out.println(JSON.toJSONString(null));
    }
  
>>>>>>> 3c441174f60e273d953f4f9ec9389353e9dfc74a
}
