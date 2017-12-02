package com.lanxi.couponcode.spi.abstractentity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.lanxi.couponcode.spi.defaultInterfaces.CommonDefaultMethodOfEntity;

import java.io.Serializable;

/**
 * 抽象企业类
 *
 * @author yangyuanjian
 */
public abstract class AbstractEnterprise extends Model<AbstractEnterprise> implements Serializable, CommonDefaultMethodOfEntity {
    /**
     * 商户编号
     */
    @TableId ("merchant_id")
    protected Long merchantId;
    /**
     * 企业全称
     */
    @TableField ("enterprise_full_name")
    protected String enterpriseFullName;
    /**
     * 证件号码
     */
    @TableField ("identification_number")
    protected String identificationNumber;
    /**
     * 记录日期
     */
    @TableField ("log_time")
    private String logTime;
    /**
     * 创建者编号
     */
    @TableField ("logger_id")
    private Long loggerId;
    /**
     * 备注
     */
    @TableField ("remark")
    private String remark;

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(long merchantId) {
        this.merchantId = merchantId;
    }

    public String getEnterpriseFullName() {
        return enterpriseFullName;
    }

    public void setEnterpriseFullName(String enterpriseFullName) {
        this.enterpriseFullName = enterpriseFullName;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public Long getLoggerId() {
        return loggerId;
    }

    public void setLoggerId(Long loggerId) {
        this.loggerId = loggerId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "AbstractEnterprise{" +
                "merchantId='" + merchantId + '\'' +
                ", enterpriseFullName='" + enterpriseFullName + '\'' +
                ", identificationNumber='" + identificationNumber + '\'' +
                ", logTime='" + logTime + '\'' +
                ", loggerId='" + loggerId + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
