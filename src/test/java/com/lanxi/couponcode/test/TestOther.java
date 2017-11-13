package com.lanxi.couponcode.test;

import org.junit.Test;

import com.lanxi.util.utils.ExcelUtil;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by yangyuanjian on 2017/11/9.
 */
public class TestOther {

    @Test
    public void test1(){
        int[] i=new int[]{1,2,3};
        System.out.println(i.getClass());
        System.out.println(i instanceof Serializable);
        System.out.println(Array.class.isAssignableFrom(i.getClass()));
        System.out.println(Serializable.class.isAssignableFrom(ArrayList.class));
    }
}
