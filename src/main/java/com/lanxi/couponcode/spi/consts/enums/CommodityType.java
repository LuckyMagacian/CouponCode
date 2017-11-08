package com.lanxi.couponcode.spi.consts.enums;

import java.io.Serializable;

/**
 * <strong>商品类型枚举</strong>
 * 
 * @author yangyuanjian
 *
 */
public enum CommodityType  implements Serializable {
	eleCoupon(1),voucher(2);
	private String value;
	private CommodityType(int value) {
		this.value=value+"";
	}
	public static CommodityType getType(int value) {
		return getType(value+"");
	}

	
	public static CommodityType getType(String value) {
		return Gettype.getType(value);
	}
	
	@Override
	public String toString() {
		return value;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
