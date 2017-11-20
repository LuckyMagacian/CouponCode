package com.lanxi.couponcode.impl.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lanxi.couponcode.spi.abstractentity.AbstractRequest;
import com.lanxi.couponcode.spi.consts.enums.CommodityType;
import com.lanxi.couponcode.spi.consts.enums.RequestOperateType;
import com.lanxi.couponcode.spi.consts.enums.RequestStatus;

import java.io.Serializable;

/**
 * Created by yangyuanjian on 2017/11/20.
 */
@TableName("commodity_request")
public class Request extends AbstractRequest{
    /**请求编号*/
    @TableId("request_id")
    private Long rquestId;
    /**请求时间*/
    @TableField("request_time")
    private String requestTime;
    /**请求者发起者编号*/
    @TableField("requester_id")
    private Long requesterId;
    /**请求发起者姓名*/
    @TableField("requester_name")
    private String requesterName;
    /**请求发起者手机号码*/
    @TableField("requester_phone")
    private String requesterPhone;
    /**请求类型*/
    @TableField("type")
    private RequestOperateType type;
    /**请求参数-商品信息,仅在添加商品时使用*/
    @TableField("commodity_info")
    private String commodityInfo;
    /**请求状态*/
    @TableField("status")
    private RequestStatus status;
    /**审核者编号*/
    @TableField("checker_id")
    private Long checkerId;
    /**审核者姓名*/
    @TableField("checker_name")
    private String checkerName;
    /**审核者手机号码*/
    @TableField("checker_phone")
    private String checkerPhone;
    /**审核时间*/
    @TableField("check_time")
    private String checkTime;
    /**关联商品编号*/
    @TableField("commodity_id")
    private Long commodityId;
    /**关联商品名称*/
    @TableField("commodity_name")
    private String commodityName;
    /**关联商品类型*/
    @TableField("commodity_type")
    private CommodityType commodityType;
    /**关联商户编号*/
    @TableField("merchant_id")
    private Long merchantId;
    /**关联商户名称*/
    @TableField("merchant_name")
    private String merchantName;
    /**请求发起时商品信息*/
    @TableField("commodity_info_before")
    private String commodityInfoBefore;
    /**请求处理后商品信息*/
    @TableField("commodity_info_after")
    private String commodityInfoAfter;



    @Override
    protected Serializable pkVal() {
        return this.requesterId;
    }

    @Override
    public String toString() {
        return "Request{" +
                "rquestId=" + rquestId +
                ", requestTime='" + requestTime + '\'' +
                ", requesterId=" + requesterId +
                ", requesterName='" + requesterName + '\'' +
                ", requesterPhone='" + requesterPhone + '\'' +
                ", type=" + type +
                ", commodityInfo='" + commodityInfo + '\'' +
                ", status=" + status +
                ", checkerId=" + checkerId +
                ", checkerName='" + checkerName + '\'' +
                ", checkerPhone='" + checkerPhone + '\'' +
                ", checkTime='" + checkTime + '\'' +
                ", commodityId=" + commodityId +
                ", commodityName='" + commodityName + '\'' +
                ", commodityType=" + commodityType +
                ", merchantId=" + merchantId +
                ", merchantName='" + merchantName + '\'' +
                ", commodityInfoBefore='" + commodityInfoBefore + '\'' +
                ", commodityInfoAfter='" + commodityInfoAfter + '\'' +
                '}';
    }

    public Long getRquestId() {
        return rquestId;
    }

    public void setRquestId(Long rquestId) {
        this.rquestId = rquestId;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public Long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getRequesterPhone() {
        return requesterPhone;
    }

    public String getCommodityInfo() {
        return commodityInfo;
    }

    public void setCommodityInfo(String commodityInfo) {
        this.commodityInfo = commodityInfo;
    }

    public void setRequesterPhone(String requesterPhone) {
        this.requesterPhone = requesterPhone;
    }

    public String getType() {
        return type.getValue();
    }

    public void setType(RequestOperateType type) {
        this.type = type;
    }

    public void setType(String type) {
        this.type = RequestOperateType.getType(type);
    }

    public String getStatus() {
        return status.getValue();
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = RequestStatus.getType(status);
    }

    public Long getCheckerId() {
        return checkerId;
    }

    public void setCheckerId(Long checkerId) {
        this.checkerId = checkerId;
    }

    public String getCheckerName() {
        return checkerName;
    }

    public void setCheckerName(String checkerName) {
        this.checkerName = checkerName;
    }

    public String getCheckerPhone() {
        return checkerPhone;
    }

    public void setCheckerPhone(String checkerPhone) {
        this.checkerPhone = checkerPhone;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public Long getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Long commodityId) {
        this.commodityId = commodityId;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
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

    public String getCommodityInfoBefore() {
        return commodityInfoBefore;
    }

    public void setCommodityInfoBefore(String commodityInfoBefore) {
        this.commodityInfoBefore = commodityInfoBefore;
    }

    public String getCommodityInfoAfter() {
        return commodityInfoAfter;
    }

    public void setCommodityInfoAfter(String commodityInfoAfter) {
        this.commodityInfoAfter = commodityInfoAfter;
    }

    public CommodityType getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(CommodityType commodityType) {
        this.commodityType = commodityType;
    }
}
