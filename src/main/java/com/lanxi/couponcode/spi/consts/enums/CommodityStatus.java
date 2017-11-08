package com.lanxi.couponcode.spi.consts.enums;

import java.io.Serializable;

/**
 * <strong>商品状态枚举</strong>
 * shelved 1 上架<br>
 * unshelved 2 下架<br>
 * deleted 3 删除<br>
 * test 4 测试<br>
 * @author yangyuanjian
 *
 */
public enum CommodityStatus implements Serializable {
	shelved(1),unshelved(2),deleted(3),test(4),cancellation(9);
	private String value;
	private CommodityStatus(int value) {
		this.value=value+"";
	}
	@Override
	public String toString() {
		return value;
	}
	
	public static CommodityStatus getType(int value) {
		return getType(value+"");
	}

	
	public static CommodityStatus getType(String value) {
		return Gettype.getType(value);
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
