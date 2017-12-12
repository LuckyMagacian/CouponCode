package com.lanxi.couponcode.test;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lanxi.couponcode.impl.entity.*;
import com.lanxi.couponcode.impl.newservice.DaoService;
import com.lanxi.couponcode.impl.newservice.MerchantServiceImpl;
import com.lanxi.couponcode.spi.consts.enums.Gettype;
import com.lanxi.util.utils.BeanUtil;
import com.lanxi.util.utils.LoggerUtil;
import com.lanxi.util.utils.RandomUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Stream;

public class TestSpring {
	    ApplicationContext ac;
	    public final static Function<Object,Object> fillEntity=e->{
	        Field[] fields=e.getClass().getDeclaredFields();
            Stream.of(fields).parallel().filter(f->!Modifier.isStatic(f.getModifiers())).forEach(f->{
                try {
                    f.setAccessible(true);
                    Class clazz=f.getType();
                    if(clazz.equals(String.class)){
                        f.set(e, RandomUtil.getRandomChar(2));
                    }else if(clazz.equals(int.class)||clazz.equals(Integer.class)){
                        f.set(e,new Random().nextInt(100000));
                    }else if(clazz.equals(long.class)||clazz.equals(Long.class)){
                        f.set(e,new Random().nextLong());
                    }else if(clazz.equals(double.class)||clazz.equals(Double.class)){
                        f.set(e,RandomUtil.getRandomDouble());
                    }else if(clazz.equals(BigDecimal.class)){
                        f.set(e,new BigDecimal(RandomUtil.getRandomDouble()));
                    }else if(clazz.isEnum()){
                        Object[] values= (Object[]) clazz.getMethod("values").invoke(null);
                        f.set(e,values[new Random().nextInt(values.length-1)]);
                    }else if(clazz.equals(float.class)||clazz.equals(Float.class)){
                        f.set(e,new Random().nextFloat());
                    }else if(clazz.equals(char.class)||clazz.equals(Character.class)){
                        f.set(e,RandomUtil.getRandomChar());
                    }else if(clazz.equals(AtomicLong.class)){
                        f.set(e,new AtomicLong(new Random().nextLong()));
                    }else
                        f.set(e,null);
                }catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (NoSuchMethodException e1) {
                    e1.printStackTrace();
                } catch (InvocationTargetException e1) {
                    e1.printStackTrace();
                }
            });
            return e;
        };


	    @Before
	    public void init(){
	    	LoggerUtil.setLogLevel(LoggerUtil.LogLevel.DEBUG);
	    	LoggerUtil.init();
	        ac=new ClassPathXmlApplicationContext("xml/spring-mvc.xml");
	        
	    }
//	    @Test
//	    public void test1(){
//	        CodeOperateRecordServiceImpl service=ac.getBean (CodeOperateRecordServiceImpl.class);
//	        System.out.println(service);
//	    }
	    @Test
	    public void test2() {
	    	MerchantServiceImpl serviceImpl =ac.getBean (MerchantServiceImpl.class);
	    	//ShopServiceImpl shopServiceImpl=ac.getBean (ShopServiceImpl.class);
	    	
	    
	    	System.out.println(serviceImpl.queryMerchantParticularsById(1L));
	    	//System.out.println (serviceImpl.queryPossessShopByMerchantId(0, null,1L, null,  null, null));
//	    	File file=new File("E:/aa.jpg");
//	    	File file2=new File("E:.jpg");
//	    	File file3=new File("E:/cc.jpg");
	    	//System.out.println(serviceImpl.fillInInformation (merchant, file, file2, file3, null, null, null));
	    	//System.out.println (serviceImpl.businessLicensePicUpLoad(merchant, file3, null,  null, null));
	    	//System.out.println(serviceImpl.changeMerchanStatus (1L, null, null, null,"freeze"));
	    	//System.out.println(shopServiceImpl.freezeAllShop(1L,  null, null, null));
	    }
	    @Test
        public void test3(){
            System.out.println(((Model)fillEntity.apply(new Account())).insert());
        }
    @Test
    public void test6(){
        System.out.println(ac.getBean(DaoService.class).getMerchantDao().insert((Merchant)TestSpring.fillEntity.apply(new Merchant())));
    }
}

