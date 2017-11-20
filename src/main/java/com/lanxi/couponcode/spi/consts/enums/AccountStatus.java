package com.lanxi.couponcode.spi.consts.enums;

import java.io.Serializable;

/**
 * <strong>用户账户状态</strong><br>
 * normal 1正常<br>
 * freeze 2冻结<br>
 * deleted 3删除<br>
 * test	4 测试<br>
 * @author yangyuanjian
 *
 */
public enum AccountStatus implements Serializable,Gettype{
	normal(1),freeze(2),deleted(3),test(4),cancellation(9);
	private String value;
	private AccountStatus(int value) {
		this.value=value+"";
	}
	@Override
	public String toString() {
		return value;
	}
	
	public static AccountStatus getType(int value) {
		return getType(value+"");
	}

	
	public static AccountStatus getType(String value) {
		return Gettype.getType(value);
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
