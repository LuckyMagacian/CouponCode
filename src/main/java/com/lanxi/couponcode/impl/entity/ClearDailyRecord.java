package com.lanxi.couponcode.impl.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lanxi.couponcode.impl.assist.CommodityClearRecord;
import com.lanxi.couponcode.spi.consts.enums.ClearStatus;
import com.lanxi.couponcode.spi.defaultInterfaces.CommonDefaultMethodOfEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangyuanjian on 2017/11/22.
 */
@TableName("clearDailyRecord")
public class ClearDailyRecord extends Model<ClearDailyRecord> implements CommonDefaultMethodOfEntity{
    /**记录编号*/
    @TableId("record_id")
    private Long recordId;
    /**记录时间*/
    @TableField("record_time")
    private String recordTime;
//    private List<Long> commodityIds;
//    private List<String> commodityNames;
    /**应结算总额*/
    @TableField("show_total")
    private BigDecimal showTotal;
    /**商户编号*/
    @TableField("merchant_id")
    private Long merchantId;
    /**商户名称*/
    @TableField("merchant_name")
    private String merchantName;
    /**核销数量*/
    @TableField("verificate_um")
    private Integer verificateNum;
    /**销毁数量*/
    @TableField("cancelation_num")
    private Integer cancelationNum;
    /**过期数量*/
    @TableField("overtime_num")
    private Integer overtimeNum;
    /**核销成本*/
    @TableField("verificate_cost")
    private BigDecimal verificateCost;
    /**销毁成本*/
    @TableField("cancelation_cost")
    private BigDecimal cancelationCost;
    /**过期成本*/
    @TableField("overtime_cost")
    private BigDecimal overtimeCost;
    /**结算状态*/
    @TableField("clear_status")
    private ClearStatus clearStatus;
    /**清算时间*/
    @TableField("clear_time")
    private String clearTime;
    /**商品清算记录列表*/
    @TableField("commodity_clear_records")
    private List<CommodityClearRecord> commodityClearRecords;



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

    public BigDecimal getShowTotal() {
        return showTotal;
    }

    public void setShowTotal(BigDecimal showTotal) {
        this.showTotal = showTotal;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
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

    public String getClearStatus() {
        return clearStatus.getValue();
    }


    public void setClearStatus(ClearStatus clearStatus) {
        this.clearStatus = clearStatus;
    }

    public void setClearStatus(String clearStatus) {
        this.clearStatus = ClearStatus.getType(clearStatus);
    }

    public String getClearTime() {
        return clearTime;
    }

    public void setClearTime(String clearTime) {
        this.clearTime = clearTime;
    }

    public List<CommodityClearRecord> getCommodityClearRecordsList() {
        return commodityClearRecords;
    }

    public String getCommodityClearRecords() {
        return JSON.toJSONString(commodityClearRecords);
    }

    public void setCommodityClearRecords(List<CommodityClearRecord> cmommodityClearRecords) {
        this.commodityClearRecords = cmommodityClearRecords;
    }
    public void setCommodityClearRecords(String commodityClearRecords) {
        this.commodityClearRecords = JSONArray.parseArray(commodityClearRecords,CommodityClearRecord.class);
    }


    @Override
    protected Serializable pkVal() {
        return recordId;
    }

}
