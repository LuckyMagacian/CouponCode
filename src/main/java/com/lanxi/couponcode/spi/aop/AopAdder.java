package com.lanxi.couponcode.spi.aop;

/**
 * Created by yangyuanjian on 12/7/2017.
 */
public interface AopAdder {
    default void addAop(Class aopClass){
        AopOrder.addAop(aopClass);
    }
}
