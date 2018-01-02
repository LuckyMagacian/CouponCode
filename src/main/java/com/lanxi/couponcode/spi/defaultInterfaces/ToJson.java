package com.lanxi.couponcode.spi.defaultInterfaces;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;

/**
 * Created by yangyuanjian on 12/7/2017.
 */
public interface ToJson {
    /**
     * 给定一个对象,返回其json状态
     */
    Function<Object, Object> jsonDeal = e -> {
        if (e == null) {
            return "";
        } else if (e instanceof ToJson) {
            return ((ToJson) e).toJsonObject();
        } else if (e instanceof Collection) {
            return collectionToJsonArray((Collection) e);
        } else if (e instanceof Map) {
            return mapToJsonObject((Map) e);
        } else if (e instanceof Number || e instanceof Character || e.getClass().isEnum() || e instanceof Boolean || e instanceof CharSequence) {
            return e.toString();
        } else if (e instanceof Pagination) {
            return paginationToJsonObject((Pagination) e);
        } else {
            return objToJsonObject(e);
        }
    };

    default String toJson() {
        return toJson(this);
    }
    default String toJson(Collection<String> c){
        return toJson(this,c);
    }

    default JSON toJsonObject() {
        return toJsonObject(this);
    }
    default JSON toJsonObject(Collection<String> c){
        return toJsonObject(this,c);
    }


    static JSONArray collectionToJsonArray(Collection collection) {
        if (collection == null)
            return null;
        JSONArray jsonArray = new JSONArray();
        collection.stream().forEach(e -> {
            if (e != null)
                jsonArray.add(jsonDeal.apply(e));
        });
        return jsonArray;
    }

    static JSONObject paginationToJsonObject(Pagination page) {
        JSONObject jobj = new JSONObject(new LinkedHashMap<>());
        jobj.put("total", page.getTotal());
        jobj.put("size", page.getSize());
        jobj.put("current", page.getCurrent());
        jobj.put("pages", page.getPages());
        return jobj;
    }

    static JSONObject mapToJsonObject(Map map) {
        return mapToJsonObject(map,null);
    }
    static JSONObject mapToJsonObject(Map map,Collection<String> c) {
        JSONObject jobj = new JSONObject();
        map.entrySet()
           .stream()
           .filter(e->c==null||c.contains(((Map.Entry) e).getKey().toString()))
           .forEach(e -> {
            Map.Entry entry = (Map.Entry) e;
            Object    key   = entry.getKey();
            Object    value = entry.getValue();
            if (value == null)
                return;
            key = jsonDeal.apply(key == null ? "null" : key);
            value = jsonDeal.apply(value);
            jobj.put(key.toString(), value);
        });
        return jobj;
    }

    static JSONObject objToJsonObject(Object obj) {
        return objToJsonObject(obj,null);
    }
    static JSONObject objToJsonObject(Object obj,Collection<String> c) {
        Field[]    fields = obj.getClass().getDeclaredFields();
        JSONObject jobj   = new JSONObject(new LinkedHashMap<>());
        Arrays.asList(fields)
              .stream()
              .filter(e -> !Modifier.isStatic(e.getModifiers()))
              .filter(e -> c==null||c.contains(e.getName()))
              .sorted(Comparator.comparing(Field::getName))
              .forEach(e -> {
                  try {
                      e.setAccessible(true);
                      String name  = e.getName();
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

    static JSON toJsonObject(Object obj) {
        return toJsonObject(obj,null);
    }
    static JSON toJsonObject(Object obj,Collection<String> c){
        if (obj == null) {
            return new JSONObject(new LinkedHashMap<>());
        } else if (obj instanceof Collection) {
            return collectionToJsonArray((Collection) obj);
        } else if (obj instanceof Map) {
            return mapToJsonObject((Map) obj,c);
        } else if (obj instanceof Pagination) {
            return paginationToJsonObject((Pagination) obj);
        } else {
            return objToJsonObject(obj,c);
        }
    }


    static String toJson(Object obj) {
        return toJson(obj,null);
    }
    static String toJson(Object obj,Collection<String> c){
        return toJsonObject(obj,c).toJSONString();
    }

}
