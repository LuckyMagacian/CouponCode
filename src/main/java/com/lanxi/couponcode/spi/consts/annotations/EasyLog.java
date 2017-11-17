package com.lanxi.couponcode.spi.consts.annotations;

import com.lanxi.util.utils.LoggerUtil;

import java.lang.annotation.*;

/**
 * Created by yangyuanjian on 2017/11/14.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface EasyLog {
    LoggerUtil.LogLevel value();
}
