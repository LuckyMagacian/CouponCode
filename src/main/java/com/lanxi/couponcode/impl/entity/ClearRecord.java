package com.lanxi.couponcode.impl.entity;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lanxi.couponcode.spi.consts.enums.ClearStatus;
import com.lanxi.couponcode.spi.consts.enums.InvoiceStatus;
import com.lanxi.couponcode.spi.defaultInterfaces.CommonDefaultMethodOfEntity;
import com.lanxi.couponcode.spi.defaultInterfaces.ToJson;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 清算记录类
 * Created by yangyuanjian on 2017/11/20.
 */
@TableName ("clear_record")
public class ClearRecord extends Model<ClearRecord> implements CommonDefaultMethodOfEntity {
    /**
     * 记录编号
     */
    @TableId ("record_id")
    private Long recordId;
    /**
     * 商户编号
     */
    @TableField ("merchant_id")
    private Long merchantId;
    /**
     * 商户名称
     */
    @TableField ("merchant_name")
    private String merchantName;
    /**
     * 核销记录列表
     */
    @TableField ("daily_record_ids")
    private List<Long> dailyRecordIds;
    /**
     * 清算时间起
     */
    @TableField ("time_start")
    private String timeStart;
    /**
     * 清算时间止
     */
    @TableField ("time_stop")
    private String timeStop;
    /**
     * 应结算金额
     */
    @TableField ("show_total")
    private BigDecimal showTotal;
    /**
     * 实际结算金额
     */
    @TableField ("fact_total")
    private BigDecimal factTotal;
    /**
     * 清算时间
     */
    @TableField ("clear_time")
    private String clearTime;
    /**
     * 操作者编号
     */
    @TableField ("operater_id")
    private Long operaterId;
    /**
     * 操作者姓名
     */
    @TableField ("operater_name")
    private String operaterName;
    /**
     * 税号
     */
    @TableField ("tax_num")
    private String taxNum;
    /**
     * 物流公司
     */
    @TableField ("logistics_company")
    private String logisticsCompany;
    /**
     * 运单号
     */
    @TableField ("order_num")
    private String orderNum;
    /**
     * 寄出时间
     */
    @TableField ("post_time")
    private String postTime;
    /**
     * 发票状态{@link InvoiceStatus}
     */
    @TableField ("invoice_status")
    private InvoiceStatus invoiceStatus;
    /**
     * 创建时间
     */
    @TableField ("create_time")
    private String createTime;
    /**
     * 结算状态
     */
    @TableField ("clear_status")
    private ClearStatus clearStatus;

    @TableField("remark")
    private String remark;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public String getDailyRecordIds() {
        if(dailyRecordIds!=null&&!dailyRecordIds.isEmpty()&&!dailyRecordIds.equals("{}"))
            return ToJson.toJson(dailyRecordIds);
        else return null;
    }

    public List<Long> getDailyRecordIdsList() {
        return dailyRecordIds;
    }

    public void setDailyRecordIds(List<Long> dailyRecordIds) {
        this.dailyRecordIds = dailyRecordIds;
    }

    public void setDailyRecordIds(String dailyRecordIds) {
        if(dailyRecordIds!=null&&!dailyRecordIds.isEmpty()&&!dailyRecordIds.equals("{}"))
            this.dailyRecordIds = JSONArray.parseArray(dailyRecordIds, Long.class);
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

    public String getOperaterName() {
        return operaterName;
    }

    public void setOperaterName(String operaterName) {
        this.operaterName = operaterName;
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

    public String getInvoiceStatus() {
        return invoiceStatus == null ? null : invoiceStatus.getValue();
    }

    public void setInvoiceStatus(InvoiceStatus invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = InvoiceStatus.getType(invoiceStatus);
    }

    public ClearStatus getClearStatusEnum() {
        return clearStatus;
    }

    public String getClearStatus() {
        return clearStatus == null ? null : clearStatus.getValue();
    }

    public void setClearStatus(ClearStatus clearStatus) {
        this.clearStatus = clearStatus;
    }

    public void setClearStatus(String clearStatus) {
        this.clearStatus = ClearStatus.getType(clearStatus);
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    protected Serializable pkVal() {
        return this.recordId;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ClearRecord{");
        sb.append("recordId=").append(recordId);
        sb.append(", merchantId=").append(merchantId);
        sb.append(", merchantName='").append(merchantName).append('\'');
        sb.append(", dailyRecordIds=").append(dailyRecordIds);
        sb.append(", timeStart='").append(timeStart).append('\'');
        sb.append(", timeStop='").append(timeStop).append('\'');
        sb.append(", showTotal=").append(showTotal);
        sb.append(", factTotal=").append(factTotal);
        sb.append(", clearTime='").append(clearTime).append('\'');
        sb.append(", operaterId=").append(operaterId);
        sb.append(", operaterName='").append(operaterName).append('\'');
        sb.append(", taxNum='").append(taxNum).append('\'');
        sb.append(", logisticsCompany='").append(logisticsCompany).append('\'');
        sb.append(", orderNum='").append(orderNum).append('\'');
        sb.append(", postTime='").append(postTime).append('\'');
        sb.append(", invoiceStatus=").append(invoiceStatus);
        sb.append(", createTime='").append(createTime).append('\'');
        sb.append(", clearStatus=").append(clearStatus);
        sb.append(", remark='").append(remark).append('\'');
        sb.append(", dailyRecordIdsList=").append(getDailyRecordIdsList());
        sb.append(", clearStatusEnum=").append(getClearStatusEnum());
        sb.append('}');
        return sb.toString();
    }
}
