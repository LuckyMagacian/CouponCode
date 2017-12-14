package com.lanxi.couponcode.impl;

import com.lanxi.util.utils.LoggerUtil;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by yangyuanjian on 12/4/2017.
 */
public class mainclass {
    public static void main(String[] args){
        LoggerUtil.init();
        new ClassPathXmlApplicationContext("xml/spring-mvc.xml");
        while(true){
            try {
                Thread.sleep(24*3600*1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
