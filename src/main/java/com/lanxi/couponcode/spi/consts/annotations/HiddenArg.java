package com.lanxi.couponcode.spi.consts.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 隐含的参数|附加的参数
 * Created by yangyuanjian on 2017/11/7.
 */
@Retention (RetentionPolicy.SOURCE)
@Target(ElementType.PARAMETER)
public @interface HiddenArg {

}
