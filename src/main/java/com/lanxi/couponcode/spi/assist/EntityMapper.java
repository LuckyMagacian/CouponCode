package com.lanxi.couponcode.spi.assist;

import java.util.Map;

/**
 * Created by yangyuanjian on 12/28/2017.
 */
public interface EntityMapper<T> {
    void init(Map<String,Object> map);
    <V> V get(String fieldName);
}
