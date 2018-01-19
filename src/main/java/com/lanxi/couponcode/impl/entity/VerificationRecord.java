package com.lanxi.couponcode.impl.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lanxi.couponcode.spi.abstractentity.AbstractVerificationRecord;
import com.lanxi.couponcode.spi.consts.enums.ClearStatus;
import com.lanxi.couponcode.spi.consts.enums.VerificationType;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 串码核销记录类
 * Created by yangyuanjian on 2017/11/2.
 */
@TableName ("verification_record")
public class VerificationRecord extends AbstractVerificationRecord {
    /**
     * 记录编号
     */
    @TableId ("record_id")
    private Long             recordId;
    /**
     * 串码
     */
    @TableField ("code")
    private Long             code;
    /**
     * 操作者编号
     */
    @TableField ("operater_id")
    private Long             operaterId;
    /**
     * 操作者手机号码
     */
    @TableField ("operater_phone")
    private String           operaterPhone;
    /**
     * 商品编号
     */
    @TableField ("commodity_id")
    private Long             commodityId;
    /**
     * 商品名称
     */
    @TableField ("commodity_name")
    private String           commodityName;
    /**
     * 核销时间
     */
    @TableField ("verficate_time")
    private String           verficateTime;
    /**
     * 商户编号
     */
    @TableField ("merchant_id")
    private Long             merchantId;
    /**
     * 商户名称
     */
    @TableField ("merchant_name")
    private String           merchantName;
    /**
     * 门店编号
     */
    @TableField ("shop_id")
    private Long             shopId;
    /**
     * 门店名称
     */
    @TableField ("shop_name")
    private String           shopName;
    /**
     * 验证方式
     */
    @TableField (value = "verification_type")
    private VerificationType verificationType;
    /**
     * 清算状态
     */
    @TableField ("clear_status")
    private ClearStatus      clearStatus;

    //    /**商品详情*/
    //    @TableField("commodity_info")
    //    private String commodityInfo;
    /**
     * 门店详情
     */
    @TableField ("shop_info")
    private String     shopInfo;
    /**
     * 商品类型
     */
    @TableField ("commodity_type")
    private String     commodityType;
    /**
     * 面值
     */
    @TableField ("face_price")
    private BigDecimal facePrice;
    /**
     * 面值
     */
    @TableField ("cost_price")
    private BigDecimal costPrice;

    @Override
    protected Serializable pkVal() {
        return this.recordId;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Long getOperaterId() {
        return operaterId;
    }

    public void setOperaterId(Long operaterId) {
        this.operaterId = operaterId;
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

    public String getVerficateTime() {
        return verficateTime;
    }

    public void setVerficateTime(String verficateTime) {
        this.verficateTime = verficateTime;
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

    public String getOperaterPhone() {
        return operaterPhone;
    }

    public void setOperaterPhone(String operaterPhone) {
        this.operaterPhone = operaterPhone;
    }

    //    public String getCommodityInfo() {
    //        return commodityInfo;
    //    }
    //
    //    public void setCommodityInfo(String commodityInfo) {
    //        this.commodityInfo = commodityInfo;
    //    }

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

    public String getVerificationType() {
        return verificationType == null ? null : verificationType.getValue();
    }

    public void setVerificationType(VerificationType verificationType) {
        this.verificationType = verificationType;
    }

    public void setVerificationType(String verificationType) {
        this.verificationType = VerificationType.getType(verificationType);
    }

    public String getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(String shopInfo) {
        this.shopInfo = shopInfo;
    }

    public String getClearStatus() {
        return clearStatus == null ? null : clearStatus.toString();
    }

    public void setClearStatus(ClearStatus clearStatus) {
        this.clearStatus = clearStatus;
    }

    public void setClearStatus(String clearStatus) {
        this.clearStatus = ClearStatus.getType(clearStatus);
    }

    public String getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(String commodityType) {
        this.commodityType = commodityType;
    }

    public BigDecimal getFacePrice() {
        return facePrice;
    }

    public void setFacePrice(BigDecimal facePrice) {
        this.facePrice = facePrice;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    @Override public String toString() {
        final StringBuffer sb = new StringBuffer("VerificationRecord{");
        sb.append("recordId=").append(recordId);
        sb.append(", code=").append(code);
        sb.append(", operaterId=").append(operaterId);
        sb.append(", operaterPhone='").append(operaterPhone).append('\'');
        sb.append(", commodityId=").append(commodityId);
        sb.append(", commodityName='").append(commodityName).append('\'');
        sb.append(", verficateTime='").append(verficateTime).append('\'');
        sb.append(", merchantId=").append(merchantId);
        sb.append(", merchantName='").append(merchantName).append('\'');
        sb.append(", shopId=").append(shopId);
        sb.append(", shopName='").append(shopName).append('\'');
        sb.append(", verificationType=").append(verificationType);
        sb.append(", clearStatus=").append(clearStatus);
        sb.append(", shopInfo='").append(shopInfo).append('\'');
        sb.append(", commodityType='").append(commodityType).append('\'');
        sb.append(", facePrice=").append(facePrice);
        sb.append(", costPrice=").append(costPrice);
        sb.append('}');
        return sb.toString();
    }
}
