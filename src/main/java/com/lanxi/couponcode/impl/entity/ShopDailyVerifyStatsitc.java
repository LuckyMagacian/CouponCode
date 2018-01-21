package com.lanxi.couponcode.impl.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lanxi.couponcode.spi.defaultInterfaces.CommonDefaultMethodOfEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@TableName("shop_daily_verify_statsitc")
public class ShopDailyVerifyStatsitc extends Model<ShopDailyVerifyStatsitc> implements CommonDefaultMethodOfEntity {
    @TableId("record_id")
    private Long recordId;
    @TableField("merchant_id")
    private Long merchantId;
    @TableField("shop_id")
    private Long shopId;
    @TableField("shop_name")
    private String shopName;
    @TableField("trade_time")
    private String tradeTime;
    @TableField("record_time")
    private String recordTime;
    @TableField("verify_num")
    private Integer verifyNum;
    @TableField("verify_cost_num")
    private BigDecimal verifyCostSum;

    @Override
    public String toString() {
        return "ShopDailyVerfyStatsitc{" +
               "recordId=" + recordId +
               ", merchantId=" + merchantId +
               ", shopId=" + shopId +
               ", shopName='" + shopName + '\'' +
               ", tradeTime='" + tradeTime + '\'' +
               ", recordTime='" + recordTime + '\'' +
               ", verifyNum=" + verifyNum +
               ", verifyCostSum=" + verifyCostSum +
               '}';
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public Integer getVerifyNum() {
        return verifyNum;
    }

    public void setVerifyNum(Integer verifyNum) {
        this.verifyNum = verifyNum;
    }

    public BigDecimal getVerifyCostSum() {
        return verifyCostSum;
    }

    public void setVerifyCostSum(BigDecimal verifyCostSum) {
        this.verifyCostSum = verifyCostSum;
    }

    @Override
    protected Serializable pkVal() {
        return this.recordId;
    }
}
