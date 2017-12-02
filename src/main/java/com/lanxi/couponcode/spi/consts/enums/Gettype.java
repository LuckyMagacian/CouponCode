package com.lanxi.couponcode.spi.consts.enums;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.stream.Stream;
/**
 * 仅包含一个static 的getType方法<br>
 * 仅用于枚举类中获取value值对应的枚举量<br>
 * @author yangyuanjian
 *
 */
public interface Gettype{
	/**
	 * 枚举类专用的方法,非枚举类中调用会出现{@link IllegalStateException}<br>
	 * 用于根据枚举了的value值来获取对应的枚举量<br>
	 * @param value	枚举量的值<br>
	 * @return 对应的枚举量
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Enum<?>> T getType(String value) {
		if(value==null)
			return null;
		Class<?> clazz=null;
		try {
			String className=Thread.currentThread().getStackTrace()[2].getClassName();
			clazz=Class.forName(className);
			if(!Enum.class.isAssignableFrom(clazz))
				throw new IllegalStateException("method : \"getType\" can only be invoked in class which extends enum !");
			clazz.getDeclaredField("value");
			T[] all=(T[]) clazz.getMethod("values").invoke(null);
			Optional<T> opt=Stream.of(all).filter(
						(e)->e.toString().equals(value)
					).findAny();
			return opt.equals(Optional.empty())?null:opt.get();
		}
		catch (NoSuchFieldException e) {
			throw new IllegalArgumentException("枚举类:"+clazz.getName()+"中不包含value字段!");
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}	
	}

	default boolean equals(String value){
		Class<?> clazz=null;
		try {
			if(value==null)
				return false;
			clazz=this.getClass();
			if(!Enum.class.isAssignableFrom(clazz))
				throw new IllegalStateException("method : \"getType\" can only be invoked in class which extends enum !");
			Field field=clazz.getDeclaredField("value");
			field.setAccessible(true);
			Object obj=field.get(this);
			if(obj!=null)
				return obj.equals(value);
				return value.equals(obj);
		}
		catch (NoSuchFieldException e) {
			throw new IllegalArgumentException("枚举类:"+clazz.getName()+"中不包含value字段!");
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


}
