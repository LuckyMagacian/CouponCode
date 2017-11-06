package com.lanxi.couponcode.spi.consts.enums;

/**
 * Created by yangyuanjian on 2017/10/31.
 */
public enum IdType {
    MERCHANT("MERCHANT-ID-"),COMMODITY("MERCHANT-COMMODITY-ID-"),
    SHOP("MERCHANT-SHOP-ID-"),USER("MERCHANT-USER-ID-"),
    CLEAR("MERCHANT-CLEAR-ID-"),OPERATE("MERCHANT-OPERATE-ID-");
    private String value;
    private IdType(String value){
        this.value=value;
    }
    @Override
    public String toString() {
        return value;
    }

    public static IdType getType(String value) {
        return Gettype.getType(value);
    }
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
