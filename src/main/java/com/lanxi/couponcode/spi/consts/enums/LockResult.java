package com.lanxi.couponcode.spi.consts.enums;

import java.io.Serializable;

/**
 * redis分布式锁操作结果<br>
 * success 锁成功<br>
 * fail 锁失败<br>
 * occupy 被占<br>
 * none 锁定对象在redis中不存在<br>
 * exception 锁定时发生异常<br>
 * Created by yangyuanjian on 2017/10/31.
 */
public enum LockResult  implements Serializable ,Gettype{
    success(0),fail(1),occupy(2),none(3),exception(4);
    private String value;
    private LockResult(int value) {
        this.value=value+"";
    }
    @Override
    public String toString() {
        return value;
    }

    public static LockResult getType(int value) {
        return getType(value+"");
    }


    public static LockResult getType(String value) {
        return Gettype.getType(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
