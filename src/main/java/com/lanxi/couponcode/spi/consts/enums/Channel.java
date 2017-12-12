package com.lanxi.couponcode.spi.consts.enums;

/**
 * Created by yangyuanjian on 12/9/2017.
 */
public enum  Channel {
    buy(1),adminMake(0);
    Integer channel;

    Channel(Integer channel) {
        this.channel = channel;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }
}
