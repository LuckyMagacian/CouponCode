package com.lanxi.couponcode.spi.consts.enums;

import java.io.Serializable;

/**
 * <strong>用户账户类型</strong><br>
 * admin 1系统管理员<br>
 * merchantManager 2商户管理员<br>
 * shopManager 3店铺管理员<br>
 * shopEmployee 4店铺员工<br>
 * @author yangyuanjian
 *
 */
public enum AccountType implements Serializable,Gettype{
	admin(1),merchantManager(2),shopManager(3),shopEmployee(4),cancellation(9);
	
	private String value;
	
	private AccountType(int value) {
		this(value+"");
	}
	
	private AccountType(String value) {
		this.value=value;
	}

	@Override
	public String toString() {
		return value;
	}
	
	public static AccountType getType(int value) {
		return getType(value+"");
	}

	
	public static AccountType getType(String value) {
		return Gettype.getType(value);
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
