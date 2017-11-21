package com.lanxi.couponcode.impl.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lanxi.couponcode.spi.consts.enums.InvoiceStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 清算记录类
 * Created by yangyuanjian on 2017/11/20.
 */
@TableName("clear_record")
public class ClearRecord extends Model<ClearRecord>{
    /**记录编号*/
    @TableId("record_id")
    private Long recordId;
    /**商户编号*/
    @TableField("merchant_id")
    private Long merchantId;
    /**商户名称*/
    @TableField("merchant_name")
    private String merchantName;
    /**核销记录列表*/
    @TableField("verification_recordIds")
    private List<Long> verificationRecordIds;
    /**清算时间起*/
    @TableField("time_start")
    private String timeStart;
    /**清算时间止*/
    @TableField("time_stop")
    private String timeStop;
    /**应结算金额*/
    @TableField("show_total")
    private BigDecimal showTotal;
    /**实际结算金额*/
    @TableField("fact_total")
    private BigDecimal factTotal;
    /**清算时间*/
    @TableField("clear_time")
    private String clearTime;
    /**操作者编号*/
    @TableField("operater_id")
    private Long operaterId;
    /**操作者姓名*/
    @TableField("operater_name")
    private Long operaterName;
    /**清算参数*/
    @TableField("args")
    private Map<String,Object> args;
    /**税号*/
    @TableField("tax_num")
    private String taxNum;
    /**物流公司*/
    @TableField("logistics_company")
    private String logisticsCompany;
    /**运单号*/
    @TableField("order_num")
    private String orderNum;
    /**寄出时间*/
    @TableField("post_time")
    private String postTime;
    /**发票状态{@link InvoiceStatus}*/
    @TableField("invoice_status")
    private InvoiceStatus invoiceStatus;

    @Override
    public String toString() {
        return "ClearRecord{" +
                "recordId=" + recordId +
                ", merchantId=" + merchantId +
                ", merchantName='" + merchantName + '\'' +
                ", verificationRecordIds=" + verificationRecordIds +
                ", timeStart='" + timeStart + '\'' +
                ", timeStop='" + timeStop + '\'' +
                ", showTotal=" + showTotal +
                ", factTotal=" + factTotal +
                ", clearTime='" + clearTime + '\'' +
                ", operaterId=" + operaterId +
                ", operaterName=" + operaterName +
                ", args=" + args +
                ", taxNum='" + taxNum + '\'' +
                ", logisticsCompany='" + logisticsCompany + '\'' +
                ", orderNum='" + orderNum + '\'' +
                ", postTime='" + postTime + '\'' +
                ", invoiceStatus=" + invoiceStatus +
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

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public List<Long> getVerificationRecordIds() {
        return verificationRecordIds;
    }

    public void setVerificationRecordIds(List<Long> verificationRecordIds) {
        this.verificationRecordIds = verificationRecordIds;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeStop() {
        return timeStop;
    }

    public void setTimeStop(String timeStop) {
        this.timeStop = timeStop;
    }

    public BigDecimal getShowTotal() {
        return showTotal;
    }

    public void setShowTotal(BigDecimal showTotal) {
        this.showTotal = showTotal;
    }

    public BigDecimal getFactTotal() {
        return factTotal;
    }

    public void setFactTotal(BigDecimal factTotal) {
        this.factTotal = factTotal;
    }

    public String getClearTime() {
        return clearTime;
    }

    public void setClearTime(String clearTime) {
        this.clearTime = clearTime;
    }

    public Long getOperaterId() {
        return operaterId;
    }

    public void setOperaterId(Long operaterId) {
        this.operaterId = operaterId;
    }

    public Long getOperaterName() {
        return operaterName;
    }

    public void setOperaterName(Long operaterName) {
        this.operaterName = operaterName;
    }

    public Map<String, Object> getArgs() {
        return args;
    }

    public void setArgs(Map<String, Object> args) {
        this.args = args;
    }

    public String getTaxNum() {
        return taxNum;
    }

    public void setTaxNum(String taxNum) {
        this.taxNum = taxNum;
    }

    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    public void setLogisticsCompany(String logisticsCompany) {
        this.logisticsCompany = logisticsCompany;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public InvoiceStatus getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(InvoiceStatus invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    @Override
    protected Serializable pkVal() {
        return this.recordId;
    }
}
