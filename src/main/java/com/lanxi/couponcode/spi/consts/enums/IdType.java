package com.lanxi.couponcode.spi.consts.enums;

import java.io.Serializable;

/**
 * Created by yangyuanjian on 2017/10/31.
 */
@Deprecated
public enum IdType implements Serializable, Gettype {
    MERCHANT("MERCHANT-ID-"), COMMODITY("MERCHANT-COMMODITY-ID-"),
    SHOP("MERCHANT-SHOP-ID-"), USER("MERCHANT-USER-ID-"),
    CLEAR("MERCHANT-CLEAR-ID-"), OPERATE("MERCHANT-OPERATE-ID-");
    private String value;

    IdType(String value) {
        this.value = value;
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
