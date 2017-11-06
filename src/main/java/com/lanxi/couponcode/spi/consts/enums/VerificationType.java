package com.lanxi.couponcode.spi.consts.enums;

/**
 * Created by yangyuanjian on 2017/11/2.
 */
public enum VerificationType {
    input(1),scan(2),unknown(3);
    private String value;
    private VerificationType(int value) {
        this.value=value+"";
    }
    @Override
    public String toString() {
        return value;
    }

    public static VerificationType getType(int value) {
        return getType(value+"");
    }


    public static VerificationType getType(String value) {
        return Gettype.getType(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
