package com.lanxi.couponcode.impl.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.lanxi.couponcode.spi.abstractentity.AbstractOrder;
import com.lanxi.couponcode.spi.consts.enums.CommodityType;

/**
 * Created by yangyuanjian on 2017/11/22.
 */
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
	/*订单编号*/
	@TableId("order_id")
	private Long orderId;
	/*订单发起方*/
	@TableField("initiator")
	private String initiator;
	/*是否需要下发"0"需要"1"不需要*/
	private String NeedSend;
	/*绑定手机号码*/
	@TableField("phone")
	private String phone;
	/*交易序号*/
	@TableField("transaction_num")
	private String transactionNum;
	/*请求类型*/
	@TableField("request_type")
	private String requestType;
	/*商品所属商户id*/
	@TableField("merchant_id")
	private Long merchantId;
	/*商品类型*/
	@TableField("commodity_type")
	private CommodityType commodityType;
	/*商品id*/
	@TableField("commodity_id")
	private Long commodityId;
	/*商品数量*/
	@TableField("commodity_num")
	private Integer commodityNum;
	/*订单发生时间*/
	@TableField("create_time")
	private String createTime;
	/*商品单价*/
	@TableField("sell_price")
	private BigDecimal sellPrice;
	/*交易总额*/
	@TableField("total_transaction")
	private BigDecimal totalTransaction;
	/*备注*/
	@TableField("remark")
	private String remark;
	
	public String getNeedSend() {
		return NeedSend;
	}
	public void setNeedSend(String needSend) {
		NeedSend = needSend;
	}
	public String getCommodityType() {
		return commodityType==null?null:commodityType.getValue();
	}
	public void setCommodityType(CommodityType commodityType) {
		this.commodityType = commodityType;
	}
	public void setCommodityType(String commodityType) {
		this.commodityType = CommodityType.getType(commodityType);
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getInitiator() {
		return initiator;
	}
	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getTransactionNum() {
		return transactionNum;
	}
	public void setTransactionNum(String transactionNum) {
		this.transactionNum = transactionNum;
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
	public Long getCommodityId() {
		return commodityId;
	}
	public void setCommodityId(Long commodityId) {
		this.commodityId = commodityId;
	}
	public Integer getCommodityNum() {
		return commodityNum;
	}
	public void setCommodityNum(Integer commodityNum) {
		this.commodityNum = commodityNum;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public BigDecimal getSellPrice() {
		return sellPrice;
	}
	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}
	public BigDecimal getTotalTransaction() {
		return totalTransaction;
	}
	public void setTotalTransaction(BigDecimal totalTransaction) {
		this.totalTransaction = totalTransaction;
	}
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Override
	protected Serializable pkVal() {
		
		return this.orderId;
	}
	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", initiator=" + initiator + ", NeedSend=" + NeedSend + ", phone=" + phone
				+ ", transactionNum=" + transactionNum + ", requestType=" + requestType + ", merchantId=" + merchantId
				+ ", commodityType=" + commodityType + ", commodityId=" + commodityId + ", commodityNum=" + commodityNum
				+ ", createTime=" + createTime + ", sellPrice=" + sellPrice + ", totalTransaction=" + totalTransaction
				+ ", remark=" + remark + "]";
	}
	
	
}
