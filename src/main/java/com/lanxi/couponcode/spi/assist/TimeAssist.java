package com.lanxi.couponcode.spi.assist;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
 * Created by yangyuanjian on 2017/11/22.
 */
public interface TimeAssist {

    Pattern pattern = Pattern.compile("[0-2]{1}[0-9]{3}[0-1]{1}[0-9]{1}[0-3]{1}[0-9]{1}[0-2]{1}[0-9]{1}([0-5]{1}[0-9]{1}){2}");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    static Boolean isyyyyMMddHHmmss(String time) {
        if (time == null)
            return false;
        return pattern.asPredicate().test(time);
    }


    static String timeFixZero(String time, int targetTotalSize) {
        if (time == null)
            return null;
        time = time.trim();
        while (time.length() < targetTotalSize)
            time += "0";
        return time;
    }

    static String timeFiexNine(String time, int targetTotalSize) {
        if (time == null)
            return null;
        time = time.trim();
        while (time.length() < targetTotalSize)
            time += "9";
        return time;
    }

    static String timeFixZero(String time) {
        return timeFixZero(time, 14);
    }

    static String timeFixNine(String time) {
        return timeFiexNine(time, 14);
    }

    /**
     * 判断字符串时间time是否是过去的时间
     *
     * @param time
     * @return true->是|false 否,若time不符合yyyyMMddHHmmss 也将返回false
     */
    static Boolean isFormer(String time) {
        if (!isyyyyMMddHHmmss(time))
            return false;
        int compareResult = LocalDateTime.now().format(formatter).compareTo(time);
        if (compareResult >= 0)
            return true;
        return false;

    }

    static Boolean isFuture(String time) {
        Boolean result = isFormer(time);
        if (result == null)
            return result;
        return !result;
    }

    static String addMillisecond(String time, Long millisecond) {
        if (!isyyyyMMddHHmmss(time))
            return null;
        if (millisecond == null)
            return time;
        //获取当前时区的instant
        Instant instant = LocalDateTime.parse(time, formatter).toInstant(OffsetDateTime.now().getOffset());
        //加上毫秒数
        Instant newInstant = instant.plusMillis(millisecond);
        //格式化
        return LocalDateTime.ofInstant(newInstant, ZoneId.systemDefault()).format(formatter);
    }

    static String addSecond(String time, Long second) {
        return addMillisecond(time, second * 1000);
    }

    static String getNow() {
        return LocalDateTime.now().format(formatter);
    }

    static String getTodayBegin() {
        return getNow().substring(0, 8) + "000000";
    }

    static String getLastMonth() {
        return LocalDateTime.now().minusMonths(1).format(formatter).substring(0, 6);
    }

    static String getToodayEnd() {
        return getNow().substring(0, 8) + "999999";
    }

    static String getTenSecondLater() {
        return LocalDateTime.now().plusSeconds(10).format(formatter);
    }

    static LocalDateTime parseToDateTime(String time) {
        if (!isyyyyMMddHHmmss(time))
            return null;
        return LocalDateTime.parse(time, formatter);
    }

    static String formatToyyyyMMddHHmmss(LocalDateTime dateTime) {
        return dateTime.format(formatter);
    }
}
