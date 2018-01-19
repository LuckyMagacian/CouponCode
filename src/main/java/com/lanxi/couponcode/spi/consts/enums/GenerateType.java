package com.lanxi.couponcode.spi.consts.enums;

import java.io.Serializable;

/**
 * Created by yangyuanjian on 12/11/2017.
 */
public enum GenerateType implements Serializable, Gettype {
    auto(1),hand(0);
    private String value;

    GenerateType(int value) {
        this.value = value + "";
    }

    @Override
    public String toString() {
        return value;
    }

    public static GenerateType getType(int value) {
        return getType(value + "");
    }


    public static GenerateType getType(String value) {
        return Gettype.getType(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
