package com.lanxi.couponcode.spi.consts.enums;

import java.io.Serializable;

/**
 * 账户权限等级枚举<br>
 * Created by yangyuanjian on 2017/11/9.
 */
public enum AccountLevel implements Serializable,Gettype{
    lowes(0),low(3),medium(5),high(7),highest(9);
    private String value;
    private AccountLevel(int value) {
        this.value=value+"";
    }
    @Override
    public String toString() {
        return value;
    }

    public static AccountLevel getType(int value) {
        return getType(value+"");
    }


    public static AccountLevel getType(String value) {
        return Gettype.getType(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

//    public boolean equals(AccountLevel level){
//        return super.equals(level);
//    }
//
//    public boolean equals(String level){
//        return getValue().equals(level);
//    }
}
