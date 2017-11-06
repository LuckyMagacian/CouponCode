package com.lanxi.couponcode.spi.consts.future;

import com.lanxi.couponcode.spi.consts.enums.Gettype;

/**
 * <strong>商品类型枚举</strong>
 * 
 * @author yangyuanjian
 *
 */
public enum CommdityType {
	unknow(-1);
	private String value;
	private CommdityType(int value) {
		this.value=value+"";
	}
	public static CommdityType getType(int value) {
		return getType(value+"");
	}

	
	public static CommdityType getType(String value) {
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
