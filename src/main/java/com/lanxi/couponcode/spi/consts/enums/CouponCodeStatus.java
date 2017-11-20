package com.lanxi.couponcode.spi.consts.enums;

import java.io.Serializable;

/**
 * <strong>串码状态枚举</strong>
 * destroyed 1 已核销<br>
 * undestroyed 2 未核销<br>
 * overtime	3 过期<br>
 * test 4 测试<br>
 * @author yangyuanjian
 *
 */
public enum CouponCodeStatus  implements Serializable,Gettype {
	destroyed(1),undestroyed(2),overtime(3),test(4),cancellation(9);
	private String value;
	private CouponCodeStatus(int value) {
		this.value=value+"";
	}
	@Override
	public String toString() {
		return value;
	}

	public static CouponCodeStatus getType(int value) {
		return getType(value+"");
	}

	
	public static CouponCodeStatus getType(String value) {
		return Gettype.getType(value);
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
