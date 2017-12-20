package com.lanxi.couponcode.spi.assist;

import com.lanxi.util.utils.RandomUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Created by yangyuanjian on 12/18/2017.
 */
public interface FillAssist {
    /**
     * 根据list里的字段名,自动填充字段
     */
    BiFunction<Object, List<String>, Object> fillFieldValue = (o, l) -> {
        Field[] fields = o.getClass().getDeclaredFields();
        Stream.of(fields).parallel().filter(f -> !Modifier.isStatic(f.getModifiers())).forEach(f -> {
            try {
                String name = f.getName();
                if (l != null && !l.contains(name))
                    return;

                f.setAccessible(true);
                Class clazz = f.getType();
                if (clazz.equals(String.class)) {
                    f.set(o, RandomUtil.getRandomChar(2));
                } else if (clazz.equals(int.class) || clazz.equals(Integer.class)) {
                    f.set(o, new Random().nextInt(100000));
                } else if (clazz.equals(long.class) || clazz.equals(Long.class)) {
                    f.set(o, new Random().nextLong());
                } else if (clazz.equals(double.class) || clazz.equals(Double.class)) {
                    f.set(o, RandomUtil.getRandomDouble());
                } else if (clazz.equals(BigDecimal.class)) {
                    f.set(o, new BigDecimal(RandomUtil.getRandomDouble()));
                } else if (clazz.isEnum()) {
                    Object[] values = (Object[]) clazz.getMethod("values").invoke(null);
                    f.set(o, values[new Random().nextInt(values.length - 1)]);
                } else if (clazz.equals(float.class) || clazz.equals(Float.class)) {
                    f.set(o, new Random().nextFloat());
                } else if (clazz.equals(char.class) || clazz.equals(Character.class)) {
                    f.set(o, RandomUtil.getRandomChar());
                } else if (clazz.equals(AtomicLong.class)) {
                    f.set(o, new AtomicLong(new Random().nextLong()));
                } else
                    f.set(o, null);
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            } catch (NoSuchMethodException e1) {
                e1.printStackTrace();
            } catch (InvocationTargetException e1) {
                e1.printStackTrace();
            }
        });
        return o;
    };

    /**
     * 根据字字段类型自动填充字段-全部
     */
    Function<Object, Object> fillEntity = e ->
            fillFieldValue.apply(e, null);

    /**
     * 根据字段名清除对应的字段的值
     */
    BiFunction<Object, List<String>, Object> clearFieldValue = (o, l) -> {
        Field[] fields = o.getClass().getDeclaredFields();
        Stream.of(fields).parallel().filter(f -> !Modifier.isStatic(f.getModifiers())).forEach(f -> {
            String name = f.getName();
            if (l != null && l.contains(name)) {
                f.setAccessible(true);
                try {
                    f.set(o, null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        return o;
    };
    /**
     * 根据字段名保留对应的字段的值
     */
    BiFunction<Object, List<String>, Object> keeyFieldValue = (o, l) -> {
        Field[] fields = o.getClass().getDeclaredFields();
        Stream.of(fields).parallel().filter(f -> !Modifier.isStatic(f.getModifiers())).forEach(f -> {
            String name = f.getName();
            if (l != null && !l.contains(name)) {
                f.setAccessible(true);
                try {
                    f.set(o, null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        return o;
    };


}
