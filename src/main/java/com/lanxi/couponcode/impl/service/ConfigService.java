package com.lanxi.couponcode.impl.service;

/**
 * 配置管理接口
 * @author yangyuanjian
 */
public interface ConfigService {
	public String getValue(String fileName,String keyName);
	public String getValue(String keyName);
	public void reload();
}
