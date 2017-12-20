package com.lanxi.couponcode.test;

import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.lanxi.util.utils.RandomUtil;
import org.junit.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class TestEntity {

    @Test
    public void test3() throws Throwable {
        MethodType type = MethodType.methodType(String.class, int.class, int.class);
        MethodHandle handle = MethodHandles.lookup().findVirtual(String.class, "substring", type);
        System.out.println(handle.invoke("123456", 1, 5));

    }

    @Test
    public void test4() throws Throwable {


    }

    public static void main(String[] args) {
        System.out.println(RandomUtil.getRandomChar());
    }

    @Test
    public void test5() {
        BiMap<Long, Long> map = HashBiMap.create();
        long i = 0;
        while (i < 100000000)
            map.put(i++, IdWorker.getId());
    }
}
