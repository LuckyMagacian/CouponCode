package com.lanxi.couponcode.test;

import com.lanxi.couponcode.impl.newservice.MerchantServiceImpl;
import com.lanxi.util.utils.LoggerUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSpring {

	    ApplicationContext ac;
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
	    
}

