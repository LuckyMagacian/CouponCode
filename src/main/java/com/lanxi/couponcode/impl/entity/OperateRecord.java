package com.lanxi.couponcode.impl.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lanxi.couponcode.spi.abstractentity.AbstractOperateRecord;
import com.lanxi.couponcode.spi.consts.enums.AccountType;
import com.lanxi.couponcode.spi.consts.enums.OperateTargetType;
import com.lanxi.couponcode.spi.consts.enums.OperateType;

import javax.annotation.MatchesPattern;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 操作记录类
 * Created by yangyuanjian on 2017/11/2.
 */
@TableName("operate_record")
public class OperateRecord extends AbstractOperateRecord{
 //--------------------------------------------自身信息-------------------------------------------
    /**记录编号*/
    @TableId("record_id")
    @NotNull
    private Long recordId;
    /**操作者编号*/
    @NotNull
    @TableField("operater_id")
    private Long operaterId;
    /**操作员账户类型*/
    @NotNull
    @TableField("account_type")
    private AccountType accountType;
    /**操作员手机号码*/
    @TableField("phone")
    @Pattern(regexp = "[0-9]{11}")
    private String phone;
    /**操作员姓名*/
    @TableField("name")
    private String name;
    /**目标类型{@link OperateTargetType}*/
    @NotNull
    @TableField("target_type")
    private OperateTargetType targetType;
    /**操作类型{@link OperateType}*/
    @NotNull
    @TableField("type")
    private OperateType type;
    /**操作时间*/
    @NotNull
    @TableField("operate_time")
    private String operateTime;
    /**操作结果*/
    @TableField("operate_result")
    private String operateResult;
    /**操作描述*/
    @TableField("description")
    private String description;
    /**备注*/
    @TableField("remark")
    private String remark;

//-----------------------------------------------------冗余信息----------------------------------------
    /**商户id*/
    @NotNull
    @TableField("merchant_id")
    private Long merchantId;
    /**商户名称*/
    @TableField("merchant_name")
    private String merchantName;
    /**门店id*/
    @NotNull
    @TableField("shop_id")
    private Long shopId;
    /**门店名称*/
    @TableField("shop_name")
    private String shopName;

 //----------------------------------------------------附属信息--------------------------------------------

    /**操作员详细信息*/
    @TableField("operater_info")
    private String operaterInfo;
    /**操作目标信息*/
    @TableField("target_info")
    private String targetInfo;
    /**更多附属信息*/
    @TableField("more_info")
    private String moreInfo;



    @Override
    protected Serializable pkVal() {
        return this.recordId;
    }



    @Override
    public String toString() {
        return "OperateRecord{" +
                "recordId=" + recordId +
                ", operaterId=" + operaterId +
                ", accountType=" + accountType +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", targetType=" + targetType +
                ", type=" + type +
                ", operateTime='" + operateTime + '\'' +
                ", operateResult='" + operateResult + '\'' +
                ", description='" + description + '\'' +
                ", remark='" + remark + '\'' +
                ", merchantId=" + merchantId +
                ", merchantName='" + merchantName + '\'' +
                ", shopId=" + shopId +
                ", shopName='" + shopName + '\'' +
                ", operaterInfo='" + operaterInfo + '\'' +
                ", targetInfo='" + targetInfo + '\'' +
                ", moreInfo='" + moreInfo + '\'' +
                '}';
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getOperaterId() {
        return operaterId;
    }

    public void setOperaterId(Long operaterId) {
        this.operaterId = operaterId;
    }

    public String getTargetType() {
        return targetType==null?null:targetType.getValue();
    }

    public void setTargetType(OperateTargetType targetType) {
        this.targetType = targetType;
    }
    public void setTargetType(String targetType) {
        this.targetType = OperateTargetType.getType(targetType);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }

    public String getOperateResult() {
        return operateResult;
    }

    public void setOperateResult(String operateResult) {
        this.operateResult = operateResult;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOperaterInfo() {
        return operaterInfo;
    }

    public void setOperaterInfo(String operaterInfo) {
        this.operaterInfo = operaterInfo;
    }

    public String getTargetInfo() {
        return targetInfo;
    }

    public void setTargetInfo(String targetInfo) {
        this.targetInfo = targetInfo;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type.toString();
    }

    public void setType(OperateType type) {
        this.type = type;
    }
    public void setType(String type) {
        this.type = OperateType.getType(type);
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

    public String getAccountType() {
        return accountType.getValue();
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = AccountType.getType(accountType);
    }
}
