package com.lanxi.couponcode.spi.consts.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * 注释注解
 * Created by yangyuanjian on 12/9/2017.
 */
@Retention (RetentionPolicy.RUNTIME)
@Target ({
        TYPE,
        FIELD,
        METHOD,
        PARAMETER,
        CONSTRUCTOR,
        LOCAL_VARIABLE,
        ANNOTATION_TYPE,
        PACKAGE,
        TYPE_PARAMETER,
        TYPE_USE
})
public @interface Comment {
    String value();
}
