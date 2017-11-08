package com.lanxi.couponcode.spi.consts.enums;

import java.io.Serializable;

/**
 * Created by yangyuanjian on 2017/11/7.
 */
public enum RetCodeEnum  implements Serializable {
    error(9999),exception(8999),fail(1999),success(0000),unknown(4999);
    private String value;
    private RetCodeEnum(int value) {
        this.value=value+"";
    }
    @Override
    public String toString() {
        return value;
    }

    public static RetCodeEnum getType(int value) {
        return getType(value+"");
    }


    public static RetCodeEnum getType(String value) {
        return Gettype.getType(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
