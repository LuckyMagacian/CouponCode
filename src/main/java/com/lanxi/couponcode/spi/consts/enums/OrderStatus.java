package com.lanxi.couponcode.spi.consts.enums;

import java.io.Serializable;

/**
 * unfinish 0 未完成
 * finish   1 完成
 * portionfinish 2 部分完成
 *
 * @author wuxiaobo
 */
public enum OrderStatus implements Serializable, Gettype {
    unfinish(0), finish(1), portionfinish(2);
    private String value;

    OrderStatus(int value) {
        this.value = value + "";
    }

    @Override
    public String toString() {
        return value;
    }

    public static OrderStatus getType(int value) {
        return getType(value + "");
    }


    public static OrderStatus getType(String value) {
        return Gettype.getType(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
