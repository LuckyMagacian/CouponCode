package com.lanxi.couponcode.spi.consts.future;

import com.lanxi.couponcode.spi.consts.enums.Gettype;

/**
 * <strong>商户类型枚举</strong>
 * 未使用
 * @author yangyuanjian
 *
 */
public enum MerchantType {
	unknow(-1);
	private String value;
	private MerchantType(int value) {
		this.value=value+"";
	}
	public static MerchantType getType(int value) {
		return getType(value+"");
	}
	
	public static MerchantType getType(String value) {
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
