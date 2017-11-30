package com.lanxi.couponcode.spi.consts.enums;

import java.io.Serializable;

/**
 * 操作目标枚举
 * Created by yangyuanjian on 2017/11/2.
 */
public enum OperateTargetType  implements Serializable,Gettype {
    shop(1),merchantManager(2),shopManager(3),employee(4),commodity(5),code(6),account(7),merchant(8),statstic(9),request(10),dailyRecord(11),clearRecord(12);
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
