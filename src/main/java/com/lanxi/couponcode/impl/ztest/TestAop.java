package com.lanxi.couponcode.impl.ztest;

import org.springframework.stereotype.Service;

/**
 * Created by yangyuanjian on 2017/11/13.
 */
@Service
public class TestAop {
    public void sayHello(){
        System.out.println("hello!");
    }

    public String sayHello(String name){
        return "hello,"+name;
    }

}
