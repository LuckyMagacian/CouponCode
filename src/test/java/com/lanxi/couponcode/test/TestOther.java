package com.lanxi.couponcode.test;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.lanxi.couponcode.impl.assist.CheckAssist;
import com.lanxi.couponcode.impl.assist.SerializeAssist;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

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


    @Test
    public void test3(){
        RetMessage<Boolean> m1=new RetMessage<>(RetCodeEnum.success,"布尔",false);
        RetMessage<String> m2=new RetMessage<>(RetCodeEnum.success,"字符串","true");
        RetMessage<Boolean> m3=new RetMessage<>(RetCodeEnum.fail,"布尔null",null);
        RetMessage<String> m4=new RetMessage<>(RetCodeEnum.fail,"字符串null",null);

        String s1=m1.toJson();
        String s2=m2.toJson();
        String s3=m3.toJson();
        String s4=m4.toJson();

        System.out.println(s1);
        System.out.println(s2);
        System.out.println(s3);
        System.out.println(s4);

        System.out.println(JSONObject.parseObject(s1,RetMessage.class).getDetail().getClass());
        System.out.println(JSONObject.parseObject(s2,RetMessage.class).getDetail().getClass());
        System.out.println((RetMessage<String>)JSONObject.parseObject(s3,RetMessage.class));
        System.out.println(JSONObject.parseObject(s4,RetMessage.class).getDetail());

    }

    @Test
    public void test4(){
        String pageNum="123";
        String pageSize="2333";

        String time1="20161226";
        String time2="201711251040";

        String addr=" 这zzzz啊啊啊1-213(3幢)!";
        System.out.println(CheckAssist.chineseAssicNumOnly(addr));
    }
    @Test
    public void test5(){
        System.out.println(IdWorker.getId());
    }

    @Test
    public void test6(){
        String str="012346";
        List<String> list=new ArrayList<>();
        while(list.size()<10)
            list.add(list.size()+"");

        System.out.println((List)SerializeAssist.unserialize(SerializeAssist.serialize((Serializable) list)));

    }
}
