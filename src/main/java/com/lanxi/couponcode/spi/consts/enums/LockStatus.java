package com.lanxi.couponcode.spi.consts.enums;

/**
 * Created by yangyuanjian on 2017/10/31.
 */
public enum LockStatus {
    lock(10),unlock(-10);
    private String value;
    private LockStatus(int value) {
        this.value=value+"";
    }
    @Override
    public String toString() {
        return value;
    }

    public static LockStatus getType(int value) {
        return getType(value+"");
    }


    public static LockStatus getType(String value) {
        return Gettype.getType(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
