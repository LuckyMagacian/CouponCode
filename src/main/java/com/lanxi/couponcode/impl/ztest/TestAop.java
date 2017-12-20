package com.lanxi.couponcode.impl.ztest;

import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.util.utils.LoggerUtil;
import org.springframework.stereotype.Service;

/**
 * Created by yangyuanjian on 2017/11/13.
 */
@Service
@EasyLog (LoggerUtil.LogLevel.INFO)
public class TestAop {
    public void sayHello() {
        System.out.println("hello!");
    }

    public String sayHello(String name) {
        return "hello," + name;
    }

    public String say(String name) {
        return name + " !";
    }
}
