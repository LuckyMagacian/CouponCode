package com.lanxi.couponcode.impl.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lanxi.couponcode.spi.abstractentity.AbstractOrder;
import com.lanxi.couponcode.spi.consts.enums.CommodityType;
import com.lanxi.couponcode.spi.consts.enums.OrderStatus;

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
	private String SRC;
	/**绑定手机号*/
	@TableField("phone")
	private String Phone;
	/**清算日期*/
	@TableField("chk_date")
	private String CHKDate;
	/**是否需要下发"0"需要"1"不需要*/
	@TableField("need_send")
	private String NeedSend;
	/**订单状态"0"表示未完成  "1"表示已完成  "2"表示部分成功*/
	@TableField("order_status")
	private OrderStatus orderStatus;
	/**串码过期时间*/
	@TableField("end_time")
	private String EndTime;
	/**串码*/
	@TableField("code")
	private String Code;
	/**交易序号*/
	@TableField("msg_id")
	private String MsgID;
	/**请求类型*/
	@TableField("request_type")
	private String requestType;
	/**商品所属商户id*/
	@TableField("merchant_id")
	private Long merchantId;
	/**商品类型*/
	@TableField("commodity_type")
	private CommodityType Type;
	/**商品id*/
	@TableField("commodity_id")
	private Long SkuCode;
	/**商品数量*/
	@TableField("count")
	private Integer Count;
	/**交易日期*/
	@TableField("work_date")
	private String WorkDate;
	/**订单创建时间*/
	@TableField("create_time")
	private String createTime;
	/**交易时间*/
	@TableField("work_time")
	private String WorkTime;
	/**商品单价*/
	@TableField("amt")
	private BigDecimal Amt;
	/**交易总额*/
	@TableField("total_amt")
	private BigDecimal TotalAmt;
	/**交易成功笔数*/
	@TableField("success_num")
	private Integer successNum;
	/**备注*/
	@TableField("remark")
	private String Remark;
	@Override
	protected Serializable pkVal() {
		
		return this.orderId;
	}
	
	public String getCHKDate() {
		return CHKDate;
	}

	public void setCHKDate(String cHKDate) {
		CHKDate = cHKDate;
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
	public String getSRC() {
		return SRC;
	}
	public void setSRC(String sRC) {
		SRC = sRC;
	}
	public String getPhone() {
		return Phone;
	}
	public void setPhone(String phone) {
		Phone = phone;
	}
	public String getNeedSend() {
		return NeedSend;
	}
	public void setNeedSend(String needSend) {
		NeedSend = needSend;
	}
	public String getMsgID() {
		return MsgID;
	}
	public void setMsgID(String msgID) {
		MsgID = msgID;
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
	public CommodityType getTypeEnum() {
		return Type;
	}
	public String getType(){return Type.getValue();}
	public void setType(CommodityType type) {
		Type = type;
	}
	public Long getSkuCode() {
		return SkuCode;
	}
	public void setSkuCode(Long skuCode) {
		SkuCode = skuCode;
	}
	public Integer getCount() {
		return Count;
	}
	public void setCount(Integer count) {
		Count = count;
	}
	public String getWorkDate() {
		return WorkDate;
	}
	public void setWorkDate(String workDate) {
		WorkDate = workDate;
	}
	public String getWorkTime() {
		return WorkTime;
	}
	public void setWorkTime(String workTime) {
		WorkTime = workTime;
	}
	public BigDecimal getAmt() {
		return Amt;
	}
	public void setAmt(BigDecimal amt) {
		Amt = amt;
	}
	public BigDecimal getTotalAmt() {
		return TotalAmt;
	}
	public void setTotalAmt(BigDecimal totalAmt) {
		TotalAmt = totalAmt;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}

	
	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}

	public String getEndTime() {
		return EndTime;
	}

	public void setEndTime(String endTime) {
		EndTime = endTime;
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

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", SRC=" + SRC + ", Phone=" + Phone + ", CHKDate=" + CHKDate
				+ ", NeedSend=" + NeedSend + ", orderStatus=" + orderStatus + ", EndTime=" + EndTime + ", Code=" + Code
				+ ", MsgID=" + MsgID + ", requestType=" + requestType + ", merchantId=" + merchantId + ", Type=" + Type
				+ ", SkuCode=" + SkuCode + ", Count=" + Count + ", WorkDate=" + WorkDate + ", createTime=" + createTime
				+ ", WorkTime=" + WorkTime + ", Amt=" + Amt + ", TotalAmt=" + TotalAmt + ", successNum=" + successNum
				+ ", Remark=" + Remark + "]";
	}

	

	

	
}

