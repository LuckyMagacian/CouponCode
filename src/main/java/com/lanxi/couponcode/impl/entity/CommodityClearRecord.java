package com.lanxi.couponcode.impl.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.lanxi.couponcode.spi.consts.enums.ClearStatus;
import com.lanxi.couponcode.spi.consts.enums.CommodityType;
import com.lanxi.couponcode.spi.defaultInterfaces.CommonDefaultMethodOfEntity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by yangyuanjian on 2017/11/20.
 */
public class CommodityClearRecord implements CommonDefaultMethodOfEntity {
    /**记录编号*/
    private Long recordId;
    /**记录时间*/
    private String recordTime;
    /**商品编号*/
    private Long commodityId;
    /**商户编号*/
    private Long merchantId;
    /**商品名称*/
    private String commodityName;
    /**商户名称*/
    private String merchantName;
    /**商品类型*/
    private CommodityType commodityType;
//    /**兑换数量*/
//    private Integer exchangedNum;
    /**核销数量*/
    private Integer verificateNum;
    /**销毁数量*/
    private Integer cancelationNum;
    /**过期数量*/
    private Integer overtimeNum;
//    /**兑换成本*/
//    private BigDecimal exchangedCost;
    /**核销成本*/
    private BigDecimal verificateCost;
    /**销毁成本*/
    private BigDecimal cancelationCost;
    /**过期成本*/
    private BigDecimal overtimeCost;
    /**清算状态{@link ClearStatus}*/
    private ClearStatus clearStatus;


    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public Long getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Long commodityId) {
        this.commodityId = commodityId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public CommodityType getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(CommodityType commodityType) {
        this.commodityType = commodityType;
    }

    public void setCommodityType(String commodityType) {
        this.commodityType = CommodityType.getType(commodityType);
    }

    public Integer getVerificateNum() {
        return verificateNum;
    }

    public void setVerificateNum(Integer verificateNum) {
        this.verificateNum = verificateNum;
    }

    public Integer getCancelationNum() {
        return cancelationNum;
    }

    public void setCancelationNum(Integer cancelationNum) {
        this.cancelationNum = cancelationNum;
    }

    public Integer getOvertimeNum() {
        return overtimeNum;
    }

    public void setOvertimeNum(Integer overtimeNum) {
        this.overtimeNum = overtimeNum;
    }

    public BigDecimal getVerificateCost() {
        return verificateCost;
    }

    public void setVerificateCost(BigDecimal verificateCost) {
        this.verificateCost = verificateCost;
    }

    public BigDecimal getCancelationCost() {
        return cancelationCost;
    }

    public void setCancelationCost(BigDecimal cancelationCost) {
        this.cancelationCost = cancelationCost;
    }

    public BigDecimal getOvertimeCost() {
        return overtimeCost;
    }

    public void setOvertimeCost(BigDecimal overtimeCost) {
        this.overtimeCost = overtimeCost;
    }

    public ClearStatus getClearStatus() {
        return clearStatus;
    }

    public void setClearStatus(ClearStatus clearStatus) {
        this.clearStatus = clearStatus;
    }
}
