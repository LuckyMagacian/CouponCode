package com.lanxi.couponcode.test.entity;

import com.lanxi.couponcode.spi.defaultInterfaces.ToJson;

import java.math.BigDecimal;

/**
 * Created by yangyuanjian on 12/7/2017.
 */
public class Cc implements ToJson{
    private Long a;
    private BigDecimal b;
    private Double c;

    public Long getA() {
        return a;
    }

    public void setA(Long a) {
        this.a = a;
    }

    public BigDecimal getB() {
        return b;
    }

    public void setB(BigDecimal b) {
        this.b = b;
    }

    public Double getC() {
        return c;
    }

    public void setC(Double c) {
        this.c = c;
    }
}
