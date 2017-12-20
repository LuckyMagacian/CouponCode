package com.lanxi.couponcode.impl.newservice;

/**
 * 配置管理接口
 * @author yangyuanjian
 */
public interface ConfigService {
	String getValue(String fileName, String keyName);
	String getValue(String keyName);
	void reload();
}
