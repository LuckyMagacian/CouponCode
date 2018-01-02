package com.lanxi.couponcode.impl.assist;


import com.lanxi.couponcode.spi.config.HiddenMap;
import com.lanxi.couponcode.spi.config.HiddenMapExcel;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * Created by yangyuanjian on 12/12/2017.
 */
public interface ExcelAssist {
    //    private Workbook workbook;
    //
    //    private Boolean readSkipFirstLine  = null;
    //    private Boolean writeSkipFirstLine = null;
    //
    //    private Supplier<Object> checkSkip = () -> {
    //        throw new IllegalStateException("must set readSkipFirstLine and writeSkipFirstLine otherwise may result wrong data !");
    //    };
    //    //    @Comment("0 is first number in poi !")
    //    //    public List<String> readRow(int sheetNum,int rowNum,int colstart,int colend){
    //    //    };
    //    //    @Comment("0 is first number in poi !")
    //    //    public List<String> readCol(int sheetNum,int colNum,int rowstart,int rowend){
    //    //
    //    //    }
    //
    //    public void writeList(OutputStream out, List<Object> list, Map<String, String> headMap) {
    //        ExcelUtil.exportExcelFile(list, headMap, out);
    //    }
    //
    //    public void writeList(File file, List<Object> list, Map<String, String> headMap) {
    //        try {
    //            FileOutputStream fout = new FileOutputStream(file);
    //            ExcelUtil.exportExcelFile(list, headMap, fout);
    //        } catch (FileNotFoundException e) {
    //            e.printStackTrace();
    //        }
    //    }

    static <T> List<List<String>> toStringList(List<T> list,Class clazz, BiFunction<Class, String, String> map) {
//        if (list == null || list.isEmpty())
//            throw new IllegalArgumentException("arg : list can't be null or empty !");
        List<List<String>> data     = new ArrayList<>();
        List<String>       headName = new ArrayList<>();
        Field[]            fields   = clazz.getDeclaredFields();
        //配置excel表头
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();
            if (Modifier.isStatic(fields[i].getModifiers()))
                continue;
            if(map.apply(clazz,fieldName)==null)
                continue;
            String showName = map.apply(clazz, fieldName);
            headName.add(showName);
        }
        if(list==null||list.isEmpty())
            return data;
        data.add(headName);
        //配置数据
        for (int i = 0; i < list.size(); i++) {
            data.add(objToStrings(list.get(i),map));
        }
        return data;
    }

//    static <T> List<List<String>> toStringList(List<T> list) {
//        return toStringList(list, HiddenMap.getAllFieldCN);
//    }

    static <T> List<String> objToStrings(T obj) {
        return objToStrings(obj, HiddenMapExcel.getEnumCN);
    }
    /**导出excel用,仅仅保留map.class中包含的属性*/
    static <T> List<String> objToStrings(T obj, BiFunction<Class, String, String> map) {
        Field[]      fields = obj.getClass().getDeclaredFields();
        List<String> values = new ArrayList<>();
        Stream.of(fields)
              .filter(f -> map.apply(obj.getClass(), f.getName()) != null)
              .filter(f -> !Modifier.isStatic(f.getModifiers()))
              .peek(f -> f.setAccessible(true))
              .forEach(f -> {
                  try {
                      Object value = f.get(obj);
                      if (value == null)
                          value = "";
                      if (value.getClass().isEnum()) {
                          value = HiddenMapExcel.getEnumCN.apply(value.getClass(), value.toString());
                      }
                      values.add(value == null ? "" : value + "");
                  } catch (IllegalAccessException e) {
                      e.printStackTrace();
                  }
              });
        return values;
    }
}
