package com.lanxi.couponcode.test.entity;

import com.lanxi.util.entity.MyClassLoader;
import org.junit.Test;

import java.io.File;

/**
 * Created by yangyuanjian on 2017/10/26.
 */
public class TestPatch {

    @Test
    public void test1()throws Throwable{
        MyClassLoader loader=new MyClassLoader();
        File file=new File(TestPatch.class.getClassLoader().getResource("").getPath()+"patch/AccountManage.class");
        System.out.println(TestPatch.class.getClassLoader().getResource("").getPath());
        if(file.exists()){
            Class clazz=loader.loadPathClass(TestPatch.class.getClassLoader().getResource("").getPath()+"patch/AccountManage.class");
            System.out.println(clazz.getName());
        }
    }
}
