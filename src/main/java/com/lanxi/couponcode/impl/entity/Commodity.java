package com.lanxi.couponcode.impl.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lanxi.couponcode.spi.abstractentity.AbstractCommodity;
import com.lanxi.couponcode.spi.consts.enums.CommodityStatus;
import com.lanxi.couponcode.spi.consts.enums.CommodityType;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by yangyuanjian on 2017/11/7.
 */
@TableName("commodity")
public class Commodity extends AbstractCommodity{
    /**商品编号*/
    @TableId("commodity_id")
    private Long commodityId;
    /**商品名称*/
    @TableField("commodity_name")
    private String commodityName;
    /**商品类型*/
    @TableField("type")
    private CommodityType type;
    /**商品面值*/
    @TableField("face_price")
    private BigDecimal facePrice;
    /**商品销售价*/
    @TableField("sell_price")
    private BigDecimal sellPrice;
    /**商品成本价*/
    @TableField("cost_price")
    private BigDecimal costPrice;
    /**商品有效期*/
    @TableField("life_time")
    private Integer lifeTime;
    /**商品状态*/
    @TableField("status")
    private CommodityStatus status;
    /**剩余库存*/
    @TableField("less_num")
    private Integer lessNum;
    /**库存警告值*/
    @TableField("warnning_num")
    private Integer warnningNum;

    /**商户编号*/
    @TableField("merchant_id")
    private Long merchantId;
    /**商户名称*/
    @TableField("merchant_name")
    private String merchantName;

    /**商品描述*/
    @TableField("description")
    private String description;
    /**使用详情*/
    @TableField("use_detail")
    private String useDetail;

    /**添加者编号*/
    @TableField("add_id")
    private Long addId;
    /**添加时间*/
    @TableField("add_time")
    private String addTime;
    /**添加者姓名*/
    @TableField("add_name")
    private String addName;


//    /**请求编号*/
//    @TableField("request_id")
//    private Long requestId;
//    /**请求时间*/
//    @TableField("request_time")
//    private String requestTime;
//    /**请求添加者编号*/
//    @TableField("requester_id")
//    private Long requesterId;
//    /**请求者添加者姓名*/
//    @TableField("requester_name")
//    private String requesterName;
//
//    /**审核者编号*/
//    @TableField("check_id")
//    private Long checkId;
//    /**审核时间*/
//    @TableField("check_time")
//    private String checkTime;
//    /**审核者姓名*/
//    @TableField("check_name")
//    private String checkName;

    /**备注--用作存储请求id*/
    @TableField("remark")
    private String remark;

    @Override
    public String toString() {
        return "Commodity{" +
                "commodityId=" + commodityId +
                ", commodityName='" + commodityName + '\'' +
                ", type=" + type +
                ", facePrice=" + facePrice +
                ", sellPrice=" + sellPrice +
                ", costPrice=" + costPrice +
                ", lifeTime=" + lifeTime +
                ", status=" + status +
                ", lessNum=" + lessNum +
                ", warnningNum=" + warnningNum +
                ", merchantId=" + merchantId +
                ", merchantName='" + merchantName + '\'' +
                ", description='" + description + '\'' +
                ", useDetail='" + useDetail + '\'' +
                ", addId=" + addId +
                ", addTime='" + addTime + '\'' +
                ", addName='" + addName + '\'' +
//                ", requestId=" + requestId +
//                ", requestTime='" + requestTime + '\'' +
//                ", requesterId=" + requesterId +
//                ", requesterName='" + requesterName + '\'' +
//                ", checkId=" + checkId +
//                ", checkTime='" + checkTime + '\'' +
//                ", checkName='" + checkName + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }

    @Override
    protected Serializable pkVal() {
        return null;
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

    public String getType() {
        return type==null?null:type.toString();
    }

    public void setType(CommodityType type) {
        this.type = type;
    }

    public void setType(String type) {
        this.type = CommodityType.getType(type);
    }

    public BigDecimal getFacePrice() {
        return facePrice;
    }

    public void setFacePrice(BigDecimal facePrice) {
        this.facePrice = facePrice;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public Integer getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(Integer lifeTime) {
        this.lifeTime = lifeTime;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUseDetail() {
        return useDetail;
    }

    public void setUseDetail(String useDetail) {
        this.useDetail = useDetail;
    }

    public Long getAddId() {
        return addId;
    }

    public void setAddId(Long addId) {
        this.addId = addId;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getAddName() {
        return addName;
    }

    public void setAddName(String addName) {
        this.addName = addName;
    }

//    public Long getRequestId() {
//        return requestId;
//    }
//
//    public void setRequestId(Long requestId) {
//        this.requestId = requestId;
//    }
//
//    public String getRequestTime() {
//        return requestTime;
//    }
//
//    public void setRequestTime(String requestTime) {
//        this.requestTime = requestTime;
//    }
//
//    public Long getCheckId() {
//        return checkId;
//    }
//
//    public void setCheckId(Long checkId) {
//        this.checkId = checkId;
//    }
//
//    public String getCheckTime() {
//        return checkTime;
//    }
//
//    public void setCheckTime(String checkTime) {
//        this.checkTime = checkTime;
//    }
//
//    public String getCheckName() {
//        return checkName;
//    }
//
//    public void setCheckName(String checkName) {
//        this.checkName = checkName;
//    }



    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status==null?null:status.toString();
    }

    public void setStatus(CommodityStatus status) {
        this.status = status;
    }
    public void setStatus(String status) {
        this.status = CommodityStatus.getType(status);
    }

//    public String getRequesterName() {
//        return requesterName;
//    }
//
//    public void setRequesterName(String requesterName) {
//        this.requesterName = requesterName;
//    }
//
//    public Long getRequesterId() {
//        return requesterId;
//    }
//
//    public void setRequesterId(Long requesterId) {
//        this.requesterId = requesterId;
//    }

    public Integer getLessNum() {
        return lessNum;
    }

    public void setLessNum(Integer lessNum) {
        this.lessNum = lessNum;
    }

    public Integer getWarnningNum() {
        return warnningNum;
    }

    public void setWarnningNum(Integer warnningNum) {
        this.warnningNum = warnningNum;
    }
}
