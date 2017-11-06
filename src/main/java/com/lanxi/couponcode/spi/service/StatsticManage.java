package com.lanxi.couponcode.spi.service;

import java.util.Collection;
import java.util.Optional;
//TODO 不好确定
public interface StatsticManage<T> {
	Collection<T> queryCommodities(Object... args);
	Optional<T> queryCommodity(T t);
}
