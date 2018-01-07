package com.lanxi.couponcode.impl.assist;

import com.lanxi.couponcode.impl.entity.*;
import com.lanxi.couponcode.impl.config.HiddenMap;
import com.lanxi.couponcode.spi.consts.enums.AccountType;
import com.lanxi.util.utils.RandomUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by yangyuanjian on 12/18/2017.
 */
public abstract class FillAssist {
    private static final Map<String, Map<String, String>> maps = new HashMap<>();

    /**
     * 私有化的构造方法-不允许继承与实例化
     */
    private FillAssist() {
    }

    //    /**
    //     * 实体类属性映射
    //     * class配置对应的类型
    //     * field配置要映射的字段
    //     * 第一层?配置当对应字段的值符合匹配
    //     * string配置条件表达式
    //     * 第二层?配置替换值
    //     */
    //    public static final Map<Class<?>, Map<Field, Map<?, Map<String, ?>>>> FIELDS_VALUE_MAPPE = new HashMap<>();


    /**
     * 根据list里的字段名,自动填充字段
     */
    public static final <T> T fillEntityField(Collection<String> l, T o) {
        Field[] fields = o.getClass().getDeclaredFields();
        Stream.of(fields)
              .parallel()
              .filter(f -> !Modifier.isStatic(f.getModifiers()))
              .filter(f -> l != null && l.contains(f.getName()))
              .forEach(f -> {
                  try {
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
    }

    public static final <T> Collection<T> fillEntitiesField(Collection<String> cs, Collection<T> co) {
        return co.parallelStream()
                 .map(o -> fillEntityField(cs, o))
                 .collect(Collectors.toList());
    }

    /**
     * 根据字字段类型自动填充字段-全部
     */
    public static final <T> T fillEntityFieldAll(T o) {
        Field[] fields = o.getClass().getDeclaredFields();
        return fillEntityField(Stream.of(fields).map(f -> f.getName()).collect(Collectors.toList()),
                               o);
    }

    public static final <T> Collection<T> fillEntitiesFieldAll(Collection<T> co) {
        Class   clazz  = co.iterator().next().getClass();
        Field[] fields = clazz.getDeclaredFields();
        return fillEntitiesField(Stream.of(fields).map(f -> f.getName()).collect(Collectors.toList()),
                                 co);
    }

    /**
     * 根据字段名清除对应的字段的值
     * l为null时不清除任何字段,否则清除l中包含的字段
     */
    public static final <T> T clearEntityField(Collection<String> l, T o) {
        Field[] fields = o.getClass().getDeclaredFields();
        //转流->并行->过滤为集合中的字段->去除静态字段->设置对应字段值为null->返回o
        Stream.of(fields)
              .parallel()
              .filter(f -> !Modifier.isStatic(f.getModifiers()))
              .filter(f -> l != null && l.contains(f.getName()))
              .forEach(f -> {
                  try {
                      f.setAccessible(true);
                      f.set(o, null);
                  } catch (IllegalAccessException e) {
                      e.printStackTrace();
                  }
              });
        return o;
    }

    public static final <T> Collection<T> clearEntitiesField(Collection<String> cs, Collection<T> co) {
        return co.parallelStream()
                 .map(o -> clearEntityField(cs, o))
                 .collect(Collectors.toList());
    }


    /**
     * 根据字段名保留对应的字段的值
     */
    public static final <T> T keepEntityField(Collection<String> l, T o) {
        Field[] fields = o.getClass().getDeclaredFields();
        //获取所有字段,并移除需要保留的字段
        List<String> clearFields = Stream.of(fields)
                                         .map(f -> f.getName())
                                         .collect(Collectors.toList());
        //清除不需要保留的字段
        clearFields.removeAll(l);
        return clearEntityField(clearFields, o);
    }

    public static final <T> Collection<T> keepEntitiesField(Collection<String> cl, Collection<T> co) {
        return co.parallelStream()
                 .map(o -> keepEntityField(cl, o))
                 .collect(Collectors.toList());
    }


    public static final BiFunction<AccountType, Class, Map<String, String>> getMap     = (a, c) -> {
        if (maps.isEmpty()) {
            HiddenMap.init();
            List<Field> list = Stream.of(HiddenMap.class.getDeclaredFields())
                                     .filter(f->Map.class.isAssignableFrom(f.getType()))
                                     .peek(e -> e.setAccessible(true))
                                     .collect(Collectors.toList());
            list.stream().forEach(f -> {
                try {
                    maps.put(f.getName(), (Map<String, String>) f.get(HiddenMap.class));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        }
        StringBuffer name = new StringBuffer();
        switch (a) {
            case admin:
                name.append("ADMIN_");
                break;
            case merchantManager:
                name.append("MERCHANTMANAGER_");
                break;
            case shopManager:
                name.append("SHOPMANAGER_");
                break;
            case shopEmployee:
                name.append("EMPLOYEE_");
                break;
            default:
                throw new IllegalArgumentException("illeagal arg a !");
        }
        if(c.equals(Account.class))
            name.append("ACCOUNT");
        else if(c.equals(ClearDailyRecord.class))
            name.append("DAILY_CLEAR");
        else if(c.equals(ClearRecord.class))
            name.append("CLEAR_RECORD");
        else if(c.equals(CodeAlgorithm.class))
            name.append("CODE_ALGORITHM");
        else if(c.equals(Commodity.class))
            name.append("COMMODITY");
        else if(c.equals(CouponCode.class))
            name.append("CODE");
        else if(c.equals(Merchant.class))
            name.append("MERCHANT");
        else if(c.equals(OperateRecord.class))
            name.append("OPERATE");
        else if(c.equals(Order.class))
            name.append("ORDER");
        else if(c.equals(Request.class))
            name.append("REQUEST");
        else if(c.equals(Shop.class))
            name.append("SHOP");
        else if(c.equals(VerificationRecord.class))
            name.append("VERIFY");
        else
            throw new IllegalArgumentException("illeagal argument c !");
        return maps.get(name.toString());
    };
    public static final BiConsumer<Map<String, String>, Object>             returnDeal = (m, o) -> {
        if (o == null || m == null)
            return;
        if (o instanceof Collection) {
            if (((Collection) o).isEmpty())
                return;
            keepEntitiesField(m.keySet(), (Collection<? extends Object>) o);
        } else {
            keepEntityField(m.keySet(), o);
        }
    };
}
