package com.lanxi.couponcode.spi.defaultInterfaces;

import com.baomidou.mybatisplus.activerecord.Model;
import com.sun.org.apache.xpath.internal.operations.Mod;

import java.lang.reflect.Method;

/**
 * Created by yangyuanjian on 11/29/2017.
 */
public interface ReEquals {
    default boolean equals(Model t){
        try {
            Method method=Model.class.getDeclaredMethod("pkVal");
            method.setAccessible(true);
            return method.invoke(this).equals(method.invoke(t));
        } catch (Exception e) {
            e.printStackTrace();
            return this.equals((Object)t);
        }
    }
}
