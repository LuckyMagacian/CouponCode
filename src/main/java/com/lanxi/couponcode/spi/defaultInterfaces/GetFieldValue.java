package com.lanxi.couponcode.spi.defaultInterfaces;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;

public interface GetFieldValue {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	default <T> T getFieldValue(String fieldName) {
		 try {
			 if(this instanceof Map) {
				 return (T)((Map) this).get(fieldName);
			 }
			 if(this instanceof Entry) {
				 return (T) ((Entry) this).getValue();
			 }
			 Class<?> clazz=this.getClass();
		 	 Method method=clazz.getMethod("get"+(char)(fieldName.charAt(0)-32)+fieldName.substring(1));
		 	 method.setAccessible(true);
			 return (T) method.invoke(this);
		 }  catch (IllegalArgumentException e2) {
			throw e2;
			} catch (IllegalAccessException e3) {
				throw new IllegalStateException("have no access to get "+fieldName);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				throw new IllegalArgumentException("no such method \"get"+(char)(fieldName.charAt(0)-32)+fieldName.substring(1)+"\"");
			} catch (SecurityException e) {
				throw new IllegalArgumentException("field \""+fieldName+"\" is not visiable !");
			} catch (InvocationTargetException e) {
				throw new IllegalArgumentException("method \"get"+(fieldName.charAt(0)-'a'-'A')+fieldName.substring(1)+"\"'s target is illegal !");
			}
	 }
}
