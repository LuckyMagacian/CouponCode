package com.lanxi.couponcode.test;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.lanxi.couponcode.spi.consts.enums.AccountLevel;
import com.lanxi.couponcode.spi.consts.enums.AccountStatus;
import com.lanxi.couponcode.spi.consts.enums.RequestOperateType;
import org.junit.Test;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.*;
import java.lang.reflect.InvocationTargetException;

public class TestEnum {
	@Test
	public void test() {
//		System.out.println(Gettype.getType("1"));
		System.out.println(RequestOperateType.getType("1").name());
		System.out.println(AccountStatus.getType(1).name());
	}
	
	@Test
	public void testSerializable() throws IOException {
		Kryo kryo=new Kryo();
		kryo.setReferences(false);
		kryo.setRegistrationRequired(false);
		kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
//		kryo.register(Temp2.class);
		File file=new File("testKryo.txt");
		if(!file.exists())
			file.createNewFile();
		Output output=new Output(new FileOutputStream(new File("testKryo.txt")));
		Temp temp=new Temp();
		temp.setName("kryo");
		kryo.writeClassAndObject(output, temp);
		output.flush();
		output.close();
	}
	
	@Test
	public void testSerializable2() throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Kryo kryo=new Kryo();
		kryo.setReferences(false);
		kryo.setRegistrationRequired(true);
		kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
//		kryo.register(Temp.class);
		kryo.register(Temp2.class);
		
//		if(!file.exists())
//			file.createNewFile();
		Input input=new Input(new FileInputStream(new File("testKryo.txt")));
		Registration obj=kryo.readClass(input);
		System.out.println(obj.getType());
		System.out.println(kryo.readObject(input, obj.getType()));
		
	}
	
	@Test
	public void test3() {
		RequestOperateType[] values=RequestOperateType.values();
		for(RequestOperateType each:values) {
			System.out.println(" * "+each.name()+" "+each.getValue()+"<br>");
		}
	}
	
	public static class Temp2 implements Serializable{
		
	}
	
	public static class Temp implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 7994692158206995352L;
		String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return "Temp [name=" + name + "]";
		}
		
	}

	@Test
	public void test4(){
		AccountLevel level=AccountLevel.highest;
		System.out.println(level.equals(AccountLevel.highest));
		System.out.println(level.equals(AccountLevel.highest.getValue()));
	}
}
