package com.lanxi.couponcode.spi.defaultInterfaces;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by yangyuanjian on 12/7/2017.
 */
public interface ToJson {
    Function<Object, Object> jsonDeal = e -> {
        if (e instanceof ToJson) {
            return ((ToJson) e).toJsonObject();
        } else if (e instanceof Collection) {
            return collectionToJsonArray((Collection) e);
        } else if (e instanceof Map) {
            return mapToJsonObject((Map) e);
        } else if (e instanceof Number || e instanceof Character || e.getClass().isEnum() || e instanceof Boolean || e instanceof CharSequence) {
            return e.toString();
        } else {
            return objToJsonObject(e);
        }
    };

    default String toJson() {
        return toJson(this);
    }

    default JSON toJsonObject() {
        return toJsonObject(this);
    }

//    static JSONObject toJsonObject2(Object obj){
//        if(obj==null)
//            return null;
//        if (obj instanceof ToJson) {
//            return ((ToJson) obj).toJsonObject();
//        } else if (obj instanceof Map) {
//            JSONObject jobj = new JSONObject();
//            ((Map) obj).entrySet().stream().forEach(e -> {
//                Map.Entry entry = (Map.Entry) e;
//                Object key = entry.getKey();
//                Object value = entry.getValue();
//                key = jsonDeal.apply(key);
//                value = jsonDeal.apply(value);
//                jobj.put(key.toString(), value);
//            });
//            return jobj;
//        } else {
//            return JSONObject.parseObject(JSON.toJSONString(obj));
//        }
//    }

//    static JSONObject toJsonObject(Object obj){
//        return objToJsonObject(obj);
//        Field[] fields = obj.getClass().getDeclaredFields();
//        JSONObject jobj = new JSONObject(new LinkedHashMap<>());
//        Arrays  .asList(fields)
//                .stream()
//                .sorted(Comparator.comparing(Field::getName))
//                .filter(e -> !Modifier.isStatic(e.getModifiers()))
//                .forEach(e -> {
//                    try {
//                        e.setAccessible(true);
//                        String name = e.getName();
//                        Object value = e.get(obj);
//                        if(value==null)
//                            return ;
//                        if (value instanceof Map) {
//                            jobj.put(name, toJsonObject(value));
//                        } else if (value instanceof Collection) {
//                            jobj.put(name, toJsonArray(value));
//                        } else if (value instanceof Number || value instanceof Character || value.getClass().isEnum() || value instanceof Boolean) {
//                            jobj.put(name, value.toString());
//                        } else if (value instanceof CharSequence) {
//                            jobj.put(name, value);
//                        } else {
//                            jobj.put(name, toJsonObject(value));
//                        }
//                    } catch (IllegalAccessException e1) {
//                        e1.printStackTrace();
//                    }
//                });
//        return jobj;
//    }

    static JSONArray collectionToJsonArray(Collection collection) {
        if (collection == null)
            return null;
        JSONArray jsonArray = new JSONArray();
        collection.stream().forEach(e -> jsonArray.add(jsonDeal.apply(e)));
        return jsonArray;
    }

    static JSONObject mapToJsonObject(Map map) {
        JSONObject jobj = new JSONObject();
        map.entrySet().stream().forEach(e -> {
            Map.Entry entry = (Map.Entry) e;
            Object key = entry.getKey();
            Object value = entry.getValue();
            key = jsonDeal.apply(key);
            value = jsonDeal.apply(value);
            jobj.put(key.toString(), value);
        });
        return jobj;
    }

    static JSONObject objToJsonObject(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        JSONObject jobj = new JSONObject(new LinkedHashMap<>());
        Arrays.asList(fields)
                .stream()
                .sorted(Comparator.comparing(Field::getName))
                .filter(e -> !Modifier.isStatic(e.getModifiers()))
                .forEach(e -> {
                    try {
                        e.setAccessible(true);
                        String name = e.getName();
                        Object value = e.get(obj);
                        if (value == null)
                            return;
                        if (value instanceof Map) {
                            jobj.put(name, mapToJsonObject((Map) value));
                        } else if (value instanceof Collection) {
                            jobj.put(name, collectionToJsonArray((Collection) value));
                        } else if (value instanceof Number || value instanceof Character || value.getClass().isEnum() || value instanceof Boolean || value instanceof CharSequence) {
                            jobj.put(name, value.toString());
                        } else if (!value.getClass().getSuperclass().equals(Object.class)) {
                            jobj.put(name, objToJsonObject(value));
                        } else
                            jobj.put(name, obj.toString());
                    } catch (IllegalAccessException e1) {
                        e1.printStackTrace();
                    }
                });
        return jobj;
    }

//    static JSONArray toJsonArray(Object obj) {
//        if(obj==null)
//            return null;
//        if (obj instanceof Collection) {
//            Collection collection = (Collection) obj;
//            JSONArray jsonArray = new JSONArray();
//            collection.stream().forEach(e -> jsonArray.add(jsonDeal.apply(e)));
//            return jsonArray;
//        }
//        throw new IllegalArgumentException("method toJsonArray can only accept collection as arg !");
//    }

    static JSON toJsonObject(Object obj) {
        if (obj instanceof Collection) {
            return collectionToJsonArray((Collection) obj);
        } else if (obj instanceof Map) {
            return mapToJsonObject((Map) obj);
        } else {
            return objToJsonObject(obj);
        }
    }

    static String toJson(Object obj) {
        return toJsonObject(obj).toJSONString();
    }
}
