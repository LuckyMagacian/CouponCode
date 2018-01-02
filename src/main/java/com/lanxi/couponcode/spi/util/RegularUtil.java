package com.lanxi.couponcode.spi.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularUtil {
    /**
     * 验证输入是否是手机号
     *
     * @param str
     * @return
     */
    public static boolean isPhone(String str) {
        if (str.length() == 0) {
            return false;
        }
        Pattern pattern = Pattern.compile("^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher isPhone = pattern.matcher(str);
        if (isPhone.matches()) {
            return true;
        }
        return false;
    }
    public static Boolean isEmail(String str){
        if(str==null||str.length()==0)
            return false;
        Pattern pattern=Pattern.compile("^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$");
        Matcher isEmail=pattern.matcher(str);
        if (isEmail.matches())
            return true;
        else
            return false;
    }
    /**
     * 验证输入是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (str.length() == 0) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        }
        return false;
    }
}
