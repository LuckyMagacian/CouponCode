package com.lanxi.couponcode.impl.assist;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by yangyuanjian on 2017/11/22.
 */
public class TimeAssist {

    private static final Pattern pattern=Pattern.compile("[0-2]{1}[0-9]{3}[0-1]{1}[0-9]{1}[0-3]{1}[0-9]{1}[0-2]{1}[0-9]{1}([0-5]{1}[0-9]{1}){2}");
    private static final DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    public static Boolean isyyyyMMddHHmmss(String time){
        if(time==null)
            return false;
        return pattern.asPredicate().test(time);
    }

    /**
     * 判断字符串时间time是否是过去的时间
     * @param time
     * @return true->是|false 否,若time不符合yyyyMMddHHmmss 也将返回false
     */
    public static Boolean isFormer(String time){
        if(!isyyyyMMddHHmmss(time))
            return false;
        int compareResult=LocalDateTime.now().format(formatter).compareTo(time);
        if(compareResult>=0)
            return true;
        return false;

    }

    public static Boolean isFuture(String time){
        Boolean result=isFormer(time);
        if(result==null)
            return result;
        return !result;
    }

    public static String addMillisecond(String time,Long millisecond){
        if(!isyyyyMMddHHmmss(time))
            return null;
        if(millisecond==null)
            return time;
        //获取当前时区的instant
        Instant instant=LocalDateTime.parse(time,formatter).toInstant(OffsetDateTime.now().getOffset());
        //加上毫秒数
        Instant newInstant=instant.plusMillis(millisecond);
        //格式化
        return LocalDateTime.ofInstant(newInstant, ZoneId.systemDefault()).format(formatter);
    }

    public static String addSecond(String time,Long second){
        return addMillisecond(time,second*1000);
    }

    public static String getNow(){
        return LocalDateTime.now().format(formatter);
    }

    public static String getTenSecondLater(){
        return LocalDateTime.now().plusSeconds(10).format(formatter);
    }

    @Test
    public void test(){
        String time="20171122aa00";
        System.out.println(addMillisecond(time,1000L));
    }
}
