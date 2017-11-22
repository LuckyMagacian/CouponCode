package com.lanxi.couponcode.test;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.*;
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

    @Test
    public void test2() throws IOException {
        MockHttpServletRequest request=new MockHttpServletRequest();
        InputStream is= request.getInputStream();
        int temp=-1;
        File file=new File("file.name");
        file.createNewFile();
        FileOutputStream fout=new FileOutputStream(file);
        while((temp=is.read())!=-1){
            fout.write(temp);
        }

    }
}
