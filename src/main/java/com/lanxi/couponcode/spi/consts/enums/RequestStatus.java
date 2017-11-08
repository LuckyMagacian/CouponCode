package com.lanxi.couponcode.spi.consts.enums;

import java.io.Serializable;

/**
 * <strong>请求状态</strong>
 * submit 1 已提交<br>
 * pass 2 处理完成通过<br>
 * reject 3 处理完成拒绝<br>
 * test 4 测试<br>
 * @author yangyuanjian
 *
 */
public enum RequestStatus  implements Serializable {
	submit(1),pass(2),reject(3),test(4),cancellation(9);
	private String value;
	private RequestStatus(int value) {
		this.value=value+"";
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
	
	public static RequestStatus getType(int value) {
		return getType(value+"");
	}

	public static RequestStatus getType(String value) {
		return Gettype.getType(value);
	}
	
}
