package com.lanxi.couponcode.impl.assist;

import com.alibaba.fastjson.JSONObject;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.util.interfaces.ToJson;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

/**
 * Created by yangyuanjian on 2017/11/23.
 */
public interface CheckAssist {
    /**若参数是null或者空,返回Optional.empty() 否则返回Optional.of(参数)*/
    static Optional<String> notNullAndEmpty(String value) {
        if (value == null || value.trim().isEmpty())
            return Optional.empty();
        return Optional.of(value.trim());
    }
    /**若参数是null,返回Optional.empty() 否则返回Optional.of(参数)*/
    static <T> Optional<T> notNUll(T t) {
        if (t == null)
            return Optional.empty();
        return Optional.of(t);
    }
    /**若参数是null,返回null 否则转换为json字符串并返回*/
    static String nullOrJson(Object obj) {
        if (obj == null)
            return null;
        if (obj instanceof ToJson)
            return ((ToJson) obj).toJson();
        return JSONObject.toJSONString(obj);
    }


    static boolean  checkPage(String pageNum,String pageSize){
        if(pageNum==null||pageSize==null)
            return false;
        final String regex="[1-9]+[0-9]{1,6}";
        return pageNum.matches(regex)&&pageSize.matches(regex);
    }

    static boolean checkTimeArg(String time){
      String regex="(20[0-2][0-9][0-1][0-9][0-3][0-9])([0-2][0-9]([0-5][0-9]){2}){0,1}";
      return time.matches(regex);
    };

    static boolean chineseAssicNumOnly(String string){
        if(string==null||string.trim().isEmpty())
            return false;
//        final String regex="([\u4e00-\u9fa5a-zA-Z])+([\u4e00-\u9fa50-9a-zA-Z]+ *)*(\\( *[\u4e00-\u9fa50-9a-zA-Z]+ *\\))*";
        final String regex="[\\u4e00-\\u9fa50-9a-zA-Z\\(\\)\\-\\[\\] ]+";
        return string.matches(regex);
    }

    static boolean isCode(Long code){
        if(code==null)
            return false;
        final String codeStr=code+"";
        final String regex="[0-9]{10}";
        return codeStr.matches(regex);
    }

    static boolean isId(Long id){
        if(id==null)
            return false;
        final String idStr=id+"";
        final String regex="[0-9]{18}";
        return idStr.matches(regex);
    }

    static boolean isNull(Object obj){
        return obj==null;
    }

}