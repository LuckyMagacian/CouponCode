package com.lanxi.couponcode.impl.newcontroller;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.lanxi.couponcode.spi.consts.enums.CommodityType;

public class Order {
	/*订单编号*/
	@TableId("order_id")
	private Long orderId;
	/*订单发起机构*/
	private String SRC;
	/*绑定手机号*/
	private String Phone;
	/*是否需要下发"0"需要"1"不需要*/
	private String NeedSend;
	/*交易序号*/
	private String MsgID;
	/*请求类型*/
	@TableField("request_type")
	private String requestType;
	/*商品所属商户id*/
	@TableField("merchant_id")
	private Long merchantId;
	/*商品类型*/
	@TableField("commodity_type")
	private CommodityType Type;
	/*商品id*/
	@TableField("commodity_id")
	private Long SkuCode;
	/*商品数量*/
	@TableField("count")
	private Integer Count;
	/*交易日期*/
	@TableField("work_date")
	private String WorkDate;
	/*交易时间*/
	private String WorkTime;
	/*商品单价*/
	@TableField("amt")
	private BigDecimal amt;
	/*交易总额*/
	@TableField("total_amt")
	private BigDecimal TotalAmt;
}
