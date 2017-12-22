package com.lanxi.couponcode.impl.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lanxi.couponcode.spi.abstractentity.AbstractOrder;
import com.lanxi.couponcode.spi.consts.enums.CommodityType;
import com.lanxi.couponcode.spi.consts.enums.OrderStatus;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by yangyuanjian on 2017/11/22.
 */
@TableName("code_order")
public class Order extends AbstractOrder{
    /**
     * 1 设计报文
     *  1.1 报文消息头
     *  1.2 报文消息体
     *
     * 2 设计订单类
     *  2.1 来源{发起方,绑定手机号码,交易序号,请求类型}[报文序号,测试标记]
     *  2.2 商品相关的信息{merchantId,commodityId,商品数量}
     *  2.3 返回给来源的信息{数量,单价,串码,过期时间,返回交易总额}
     *  2.4 订单发生时的商品信息
     *
     * 3 查询订单
     *  3.1 查询详情
     *  3.2 批量查询
     */
	/**订单编号*/
	@TableId("order_id")
	private Long orderId;
	/**订单发起机构*/
	@TableField("src")
	private String src;
	/**绑定手机号*/
	@TableField("phone")
	private String phone;
	/**清算日期*/
	@TableField("chk_date")
	private String chkDate;
	/**是否需要下发"0"需要"1"不需要*/
	@TableField("need_send")
	private String needSend;
	/**订单状态"0"表示未完成  "1"表示已完成  "2"表示部分成功*/
	@TableField("order_status")
	private OrderStatus orderStatus;
	/**串码过期时间*/
	@TableField("end_time")
	private String endTime;
	/**串码*/
	@TableField("code")
	private String code;
	/**交易序号*/
	@TableField("msg_id")
	private String msgId;
	/**请求类型*/
	@TableField("request_type")
	private String requestType;
	/**商品所属商户id*/
	@TableField("merchant_id")
	private Long merchantId;
	/**商品类型*/
	@TableField("commodity_type")
	private CommodityType type;
	/**商品id*/
	@TableField("commodity_id")
	private Long skuCode;
	/**商品数量*/
	@TableField("count")
	private Integer count;
	/**交易日期*/
	@TableField("work_date")
	private String workDate;
	/**订单创建时间*/
	@TableField("create_time")
	private String createTime;
	/**串码生成时间*/
	@TableField("code_create_time")
	private String codeCreateTime;
	/**交易时间*/
	@TableField("work_time")
	private String workTime;
	/**商品单价*/
	@TableField("amt")
	private BigDecimal amt;
	/**交易总额*/
	@TableField("total_amt")
	private BigDecimal totalAmt;
	/**交易成功笔数*/
	@TableField("success_num")
	private Integer successNum;
	/**商品名称*/
	@TableField("commodity_name")
	private String commodityName;
	/**商户名称*/
	@TableField("merchant_name")
	private String merchantName;
	/**平台流水号*/
	@TableField("serial_num")
	private String serialNum;
	/**备注*/
	@TableField("remark")
	private String remark;
	@Override
	protected Serializable pkVal() {
		
		return this.orderId;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getChkDate() {
		return chkDate;
	}

	public void setChkDate(String chkDate) {
		this.chkDate = chkDate;
	}

	public String getNeedSend() {
		return needSend;
	}

	public void setNeedSend(String needSend) {
		this.needSend = needSend;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public Long getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(Long skuCode) {
		this.skuCode = skuCode;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getWorkDate() {
		return workDate;
	}

	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}

	public String getWorkTime() {
		return workTime;
	}

	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}

	public BigDecimal getAmt() {
		return amt;
	}

	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}

	public BigDecimal getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(BigDecimal totalAmt) {
		this.totalAmt = totalAmt;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCodeCreateTime() {
		return codeCreateTime;
	}

	public void setCodeCreateTime(String codeCreateTime) {
		this.codeCreateTime = codeCreateTime;
	}


	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public String getType() {
		return type == null ? null : type.toString();
	}

	public void setType(CommodityType Type) {
		this.type = Type;
	}

	public void setType(String Type) {
		this.type = CommodityType.getType(Type);
	}


	public OrderStatus getOrderStatusEnum(){return orderStatus;}

	public String getOrderStatus() {
		return orderStatus==null?null:orderStatus.getValue();
	}


	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = OrderStatus.getType(orderStatus);
	}
	
	public Integer getSuccessNum() {
		return successNum;
	}

	public void setSuccessNum(Integer successNum) {
		this.successNum = successNum;
	}


	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	@Override
	public String toString() {
		return "Order{" +
				"orderId=" + orderId +
				", src='" + src + '\'' +
				", phone='" + phone + '\'' +
				", chkDate='" + chkDate + '\'' +
				", needSend='" + needSend + '\'' +
				", orderStatus=" + orderStatus +
				", endTime='" + endTime + '\'' +
				", code='" + code + '\'' +
				", msgId='" + msgId + '\'' +
				", requestType='" + requestType + '\'' +
				", merchantId=" + merchantId +
				", type=" + type +
				", skuCode=" + skuCode +
				", count=" + count +
				", workDate='" + workDate + '\'' +
				", createTime='" + createTime + '\'' +
				", codeCreateTime='" + codeCreateTime + '\'' +
				", workTime='" + workTime + '\'' +
				", amt=" + amt +
				", totalAmt=" + totalAmt +
				", successNum=" + successNum +
				", commodityName='" + commodityName + '\'' +
				", merchantName='" + merchantName + '\'' +
				", serialNum='" + serialNum + '\'' +
				", remark='" + remark + '\'' +
				'}';
	}
}

