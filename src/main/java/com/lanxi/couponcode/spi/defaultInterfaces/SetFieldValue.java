package com.lanxi.couponcode.spi.defaultInterfaces;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;

public interface SetFieldValue {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	default <T> void setFieldValue(String fieldName,T value) {
		 try {
			 if(this instanceof Map) {
				 ((Map) this).put(fieldName, value);
			 }
			 if(this instanceof Entry) {
				 ((Entry) this).setValue(value);
			 }
			 Class<?> clazz=this.getClass();
		 	 Method method=clazz.getMethod("set"+(char)(fieldName.charAt(0)-32)+fieldName.substring(1));
		 	 method.setAccessible(true);
			 method.invoke(this,value);
		 }  catch (IllegalArgumentException e2) {
			throw e2;
			} catch (IllegalAccessException e3) {
				throw new IllegalStateException("have no access to set ["+fieldName+"]:["+value+"]");
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				throw new IllegalArgumentException("no such method \"set"+(char)(fieldName.charAt(0)-32)+fieldName.substring(1)+"\"");
			} catch (SecurityException e) {
				throw new IllegalArgumentException("field \""+fieldName+"\" is not visiable !");
			} catch (InvocationTargetException e) {
				throw new IllegalArgumentException("method \"set"+(fieldName.charAt(0)-'a'-'A')+fieldName.substring(1)+"\"'s target is illegal !");
			}
	 }
}
