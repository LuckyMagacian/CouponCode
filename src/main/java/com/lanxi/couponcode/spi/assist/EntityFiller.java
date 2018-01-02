package com.lanxi.couponcode.spi.assist;

/**
 * Created by yangyuanjian on 12/28/2017.
 */
public interface EntityFiller {
    <T> T fill(T t);
    <T> T fill(T t,EntityMapper<T> mapper);
}
