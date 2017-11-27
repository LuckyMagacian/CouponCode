package com.lanxi.couponcode.impl.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lanxi.couponcode.spi.abstractentity.AbstractCodeAlgorithm;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.JDBCType;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 串码算法封装类
 * Created by yangyuanjian on 2017/11/1.
 */
@TableName ("code_algorithm")
public class CodeAlgorithm extends AbstractCodeAlgorithm {
    /**
     * 数据库中的的串码计数起始值
     */
    private transient static final long DEFAULT_VAR = 500000000L;

    /**
     * 商户编号
     */
    @TableId ("merchant_id")
    private Long merchantId;
    /**
     * 乘方值
     */
    @TableField ("power")
    private Integer power;
    /**
     * 大质数1
     */
    @TableField ("p1")
    private BigDecimal p1;
    /**
     * 大质数2
     */
    @TableField ("p2")
    private BigDecimal p2;
    /**
     * 大质数之积
     */
    @TableField ("n")
    private BigDecimal n;
    /**
     * 自增变量
     */
    @TableField (value = "var")
    private volatile AtomicLong var;

    public CodeAlgorithm() {
//        var = new AtomicLong(DEFAULT_VAR);
    }

    public CodeAlgorithm(long merchantId, int power, long p1, long p2, long var) {
        this.merchantId = merchantId;
        this.power = power;
        this.p1 = new BigDecimal(p1);
        this.p2 = new BigDecimal(p2);
        this.n = this.p1.multiply(this.p2);
        this.var = var < DEFAULT_VAR ? new AtomicLong(DEFAULT_VAR) : new AtomicLong(var);
    }

    //    public long testGetCode(long var){
//        BigDecimal bVar = new BigDecimal(var);
//        BigDecimal code = bVar.pow(power).remainder(this.n);
//        return code.longValue();
//    }
    public long getCode(Long var) {
        if (var==null||var < 0) {
            synchronized (this) {
                var = getAndIncyVar();
                this.updateById();
            }
        }
        BigDecimal bVar = new BigDecimal(var);
        BigDecimal code = bVar.pow(power).remainder(this.n);
        return code.longValue();
    }

    @Override
    protected Serializable pkVal() {
        return this.merchantId;
    }

    @Override
    public String toString() {
        return "CodeAlgorithm{" +
                "merchantId='" + merchantId + '\'' +
                ", power=" + power +
                ", p1=" + p1 +
                ", p2=" + p2 +
                ", n=" + n +
                ", var=" + var +
                '}';
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(long merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public BigDecimal getP1() {
        return p1;
    }

    public void setP1(BigDecimal p1) {
        this.p1 = p1;
    }

    public BigDecimal getP2() {
        return p2;
    }

    public void setP2(BigDecimal p2) {
        this.p2 = p2;
    }

    public BigDecimal getN() {
        return n;
    }

    public void setN(BigDecimal n) {
        this.n = n;
    }

    public Long getVar() {
        return this.var==null?0:this.var.get();
    }

    public long getAndIncyVar() {
        return var.addAndGet(1);
    }

    public void setVar(Long var) {
        this.var = new AtomicLong(var);
    }

    public void setVar(AtomicLong var) {
        this.var = var;
    }

}
