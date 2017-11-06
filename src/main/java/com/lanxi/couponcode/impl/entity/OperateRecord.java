package com.lanxi.couponcode.impl.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lanxi.couponcode.spi.abstractentity.AbstractOperateRecord;
import com.lanxi.couponcode.spi.consts.enums.OperateTargetType;

import java.io.Serializable;

/**
 * 操作记录类
 * Created by yangyuanjian on 2017/11/2.
 */
@TableName("operate_record")
public class OperateRecord extends AbstractOperateRecord{
    /**记录编号*/
    @TableId("record_id")
    private Long recordId;
    /**操作者编号*/
    @TableField("operater_id")
    private Long operaterId;
    /**操作员手机号码*/
    @TableField("operater_phone")
    private String operaterPhone;
    /**目标类型{@link OperateTargetType}*/
    @TableField("target_type")
    private OperateTargetType targetType;
    /**操作描述*/
    @TableField("description")
    private String description;
    /**操作时间*/
    @TableField("operate_time")
    private String operateTime;
    /**操作结果*/
    @TableField("operate_result")
    private String operateResult;
    /**备注*/
    @TableField("remark")
    private String remark;
    /**操作员详细信息*/
    @TableField("operater_info")
    private String operaterInfo;
    /**操作目标信息*/
    @TableField("target_info")
    private String targetInfo;
    /**操作目标结果信息*/
    @TableField("target_result_info")
    private String targetResultInfo;
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
                ", operaterPhone='" + operaterPhone + '\'' +
                ", targetType=" + targetType +
                ", description='" + description + '\'' +
                ", operateTime='" + operateTime + '\'' +
                ", operateResult='" + operateResult + '\'' +
                ", remark='" + remark + '\'' +
                ", operaterInfo='" + operaterInfo + '\'' +
                ", targetInfo='" + targetInfo + '\'' +
                ", targetResultInfo='" + targetResultInfo + '\'' +
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

    public String getOperaterPhone() {
        return operaterPhone;
    }

    public void setOperaterPhone(String operaterPhone) {
        this.operaterPhone = operaterPhone;
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

    public String getTargetResultInfo() {
        return targetResultInfo;
    }

    public void setTargetResultInfo(String targetResultInfo) {
        this.targetResultInfo = targetResultInfo;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }
}
