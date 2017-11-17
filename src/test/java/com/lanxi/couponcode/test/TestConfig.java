package com.lanxi.couponcode.test;

import com.lanxi.util.utils.LoggerUtil;
import com.lanxi.util.utils.LoggerUtil.LogLevel;
import org.junit.Test;

public class TestConfig {
	@Test
	public void test1() {

	}
	
	@Test
	public void test2() {
		LoggerUtil.setLogLevel(LogLevel.DEBUG);
		LoggerUtil.init();
	}
	
}
