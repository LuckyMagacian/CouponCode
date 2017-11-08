package com.lanxi.couponcode.test;

import com.lanxi.couponcode.impl.service.CodeOperateRecordServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSpring {

    ApplicationContext ac;
    @Before
    public void init(){
        ac=new ClassPathXmlApplicationContext("xml/spring-mvc.xml");
    }
    @Test
    public void test1(){
        CodeOperateRecordServiceImpl service=ac.getBean(CodeOperateRecordServiceImpl.class);
        System.out.println(service);
    }
}
