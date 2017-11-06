package com.lanxi.couponcode.test;

import org.junit.Test;

import com.lanxi.couponcode.impl.service.ConfigServiceImpl;

import com.lanxi.util.utils.LoggerUtil;
import com.lanxi.util.utils.LoggerUtil.LogLevel;

public class TestConfig {
	@Test
	public void test1() {

	}
	
	@Test
	public void test2() {
		LoggerUtil.setLogLevel(LogLevel.DEBUG);
		LoggerUtil.init();
		System.out.println(new ConfigServiceImpl().getValue("redis","url"));
	}
	
}
