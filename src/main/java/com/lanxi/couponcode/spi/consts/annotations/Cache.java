package com.lanxi.couponcode.spi.consts.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yangyuanjian on 2017/11/17.
 */
@Retention (RetentionPolicy.RUNTIME)
@Target ({ElementType.METHOD})
public @interface Cache {
    /**
     * 不作为缓存Key一部分的参数名
     */
    String[] exclusionArgs() default "";
}

