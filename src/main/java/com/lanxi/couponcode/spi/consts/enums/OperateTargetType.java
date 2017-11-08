package com.lanxi.couponcode.spi.consts.enums;

import java.io.Serializable;

/**
 * Created by yangyuanjian on 2017/11/2.
 */
public enum OperateTargetType  implements Serializable {
    merchant(1),shop(2),merchantManager(3),shopManager(4),employee(5),commodity(6),code(7),account(8),unknown(9);
    private String value;
    private OperateTargetType(int value) {
        this.value=value+"";
    }
    @Override
    public String toString() {
        return value;
    }

    public static OperateTargetType getType(int value) {
        return getType(value+"");
    }


    public static OperateTargetType getType(String value) {
        return Gettype.getType(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
