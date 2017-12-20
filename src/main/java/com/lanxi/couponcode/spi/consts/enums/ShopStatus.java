package com.lanxi.couponcode.spi.consts.enums;

import java.io.Serializable;

/**
 * <strong>店铺状态</strong>
 * normal 1 正常<br>
 * freeze 2 冻结<br>
 * deleted 3 删除<br>
 * test 4 测试<br>
 *
 * @author yangyuanjian
 */
public enum ShopStatus implements Serializable, Gettype {
    normal(1), freeze(2), deleted(3), test(4), cancellation(9);
    private String value;

    private ShopStatus(int value) {
        this.value = value + "";
    }

    @Override
    public String toString() {
        return value;
    }

    public static ShopStatus getType(int value) {
        return getType(value + "");
    }


    public static ShopStatus getType(String value) {
        return Gettype.getType(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
