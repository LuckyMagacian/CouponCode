package com.lanxi.couponcode.test;

import com.google.common.collect.HashBiMap;
import com.lanxi.couponcode.impl.entity.CodeAlgorithm;
import com.lanxi.couponcode.impl.service.RedisCodeServiceImpl;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by yangyuanjian on 2017/10/31.
 */
public class TestCodeServce {


    @Test
    public  void test1(){
//        CodeServiceImpl codeService=new CodeServiceImpl();
//        codeService.test1();
    }
//    @Test
//    public void test2(){
//        RedisCodeServiceImpl codeService=new RedisCodeServiceImpl();
//        CodeAlgorithm algorithm=codeService.testGetCodeAlgorithm("test");
//        HashBiMap<Long,Long> biMap=HashBiMap.create();
//
//        AtomicLong i=new AtomicLong(1);
//        final Optional<AtomicLong> opt=Optional.of(i);
//        Thread thread=new Thread(()->{
//            while (true){
//            try {
//                Thread.sleep(1000);
//                System.out.println(opt.get().get());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }}
//        });
//        thread.start();
//        while (i.getAndAdd(1)<10000000000L){
//            biMap.put(i.get(),algorithm.testGetCode(i.get()));
//        }
//    }

}
