package com.lanxi.couponcode.spi.consts.enums;

import java.io.Serializable;

/**
 * Created by yangyuanjian on 2017/11/20.
 */
public enum  InvoiceStatus implements Gettype,Serializable{
    unposted(1),posted(2);
    private String value;
    private InvoiceStatus(int value) {
        this.value=value+"";
    }
    @Override
    public String toString() {
        return value;
    }

    public static InvoiceStatus getType(int value) {
        return getType(value+"");
    }

    public static InvoiceStatus getType(String value) {
        return Gettype.getType(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
