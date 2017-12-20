package com.lanxi.couponcode.spi.consts.enums;

import java.io.Serializable;

/**
 * Created by yangyuanjian on 2017/11/7.
 */
public enum ClearStatus implements Serializable, Gettype {
    cleard(1), uncleared(2), all(3), waitsure(4);
    private String value;

    ClearStatus(int value) {
        this.value = value + "";
    }

    @Override
    public String toString() {
        return value;
    }

    public static ClearStatus getType(int value) {
        return getType(value + "");
    }


    public static ClearStatus getType(String value) {
        return Gettype.getType(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
