package com.lanxi.couponcode.test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.junit.Test;

import com.lanxi.couponcode.spi.entity.User;
import com.lanxi.util.utils.RandomUtil;

public class TestEntity {
	@Test
	public void test1() {
		User user=new User();
		user.setName("10086");
//		System.out.println(user.getFieldValue("name")+"");
//		String name=user.getFieldValue("name");
//		System.out.println(name);
	}
	@Test
	public void test3() throws Throwable {
		MethodType type=MethodType.methodType(String.class,int.class,int.class);
		MethodHandle handle=MethodHandles.lookup().findVirtual(String.class, "substring", type);
		System.out.println(handle.invoke("123456",1,5));
		
	}
	
	@Test
	public void test4() throws Throwable {
		User user=new User();
		user.setName("10086");
		MethodType type=MethodType.methodType(String.class);
		System.out.println(type);
		MethodHandle handle=MethodHandles.lookup().findVirtual(User.class, "getName", type);
		System.out.println(handle.type());
		handle.bindTo(user);
		System.out.println(handle.invoke(user));
		System.out.println(handle.invokeExact(user));
		
	}
	
	public static void main(String[] args) {
		System.out.println(RandomUtil.getRandomChar());
	}
	@Test
	public void test5(){
		BiMap<Long,Long> map= HashBiMap.create();
		long i=0;
		while(i<100000000)
			map.put(i, IdWorker.getId());
	}
}
