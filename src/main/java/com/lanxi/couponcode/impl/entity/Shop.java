package com.lanxi.couponcode.impl.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lanxi.couponcode.spi.abstractentity.AbstractShop;
import com.lanxi.couponcode.spi.consts.enums.MerchantStatus;
import com.lanxi.couponcode.spi.consts.enums.ShopStatus;
/**
 * 门店实体类
 * @author wuxiaobo
 *
 */
@TableName("shop")
public class Shop extends AbstractShop{
	/*门店编号*/
	@TableId("shop_id")
	private Long shopId;
	/*所属商户id*/
	@TableField("merchant_id")
	private Long merchantId;
	/*门店名称*/
	@TableField("shop_name")
	private String shopName;
	/*创建时间*/
	@TableField("create_time")
	private String createTime;
	/*所对应商户的状态*/
	@TableField("merchant_status")
	private MerchantStatus merchantStatus;
	/*门店地址*/
	@TableField("shop_address")
	private String shopAddress;
	/*门店详细地址*/
	@TableField("minute_shop_address")
	private String minuteShopAddress;
	/*门店状态*/
	@TableField("shop_status")
	private ShopStatus shopStatus;
	/*客服电话*/
	@TableField("servicetel")
	private String servicetel;
	@Override
	protected Serializable pkVal() {
		// TODO Auto-generated method stub
		return this.shopId;
	}
	public Long getShopId() {
		return shopId;
	}
	
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
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
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getMerchantStatus() {
		return merchantStatus==null?null:merchantStatus.getValue();
	}
	public void setMerchantStatus(MerchantStatus merchantStatus) {
		this.merchantStatus = merchantStatus;
	}
	public void setMerchantStatus(String merchantStatus) {
		this.merchantStatus = MerchantStatus.getType(merchantStatus);
	}
	public String getShopAddress() {
		return shopAddress;
	}
	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}
	public String getMinuteShopAddress() {
		return minuteShopAddress;
	}
	public void setMinuteShopAddress(String minuteShopAddress) {
		this.minuteShopAddress = minuteShopAddress;
	}
	public String getShopStatus() {
		
		return shopStatus==null?null:shopStatus.getValue();
	}
	public void setShopStatus(ShopStatus shopStatus) {
		this.shopStatus = shopStatus;
	}
	public void setShopStatus(String shopStatus) {
		this.shopStatus=ShopStatus.getType(shopStatus);
	}
	public String getServicetel() {
		return servicetel;
	}
	public void setServicetel(String servicetel) {
		this.servicetel = servicetel;
	}
	@Override
	public String toString() {
		return "Shop [shopId=" + shopId + ", merchantId=" + merchantId + ", shopName=" + shopName + ", createTime="
				+ createTime + ", merchantStatus=" + merchantStatus + ", shopAddress=" + shopAddress
				+ ", minuteShopAddress=" + minuteShopAddress + ", shorStatus=" + shopStatus + ", servicetel="
				+ servicetel + "]";
	}
	
	
}
