package com.lanxi.couponcode.test;

import com.lanxi.couponcode.impl.entity.Merchant;
import com.lanxi.couponcode.impl.service.CodeOperateRecordServiceImpl;
import com.lanxi.couponcode.impl.service.MerchantServiceImpl;
import com.lanxi.couponcode.impl.service.ShopServiceImpl;
import com.lanxi.couponcode.spi.consts.enums.MerchantStatus;
import com.lanxi.util.entity.LogFactory;
import com.lanxi.util.utils.LoggerUtil;
import com.lanxi.util.utils.LoggerUtil.LogLevel;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSpring {

    ApplicationContext ac;
    @Before
    public void init(){
        ac=new ClassPathXmlApplicationContext("xml/spring-mvc.xml");
        LoggerUtil.setLogLevel(LogLevel.DEBUG);
        LoggerUtil.init();
    }
//    @Test
//    public void test1(){
//        CodeOperateRecordServiceImpl service=ac.getBean(CodeOperateRecordServiceImpl.class);
//        System.out.println(service);
//    }
    @Test
    public void test2() {
    	MerchantServiceImpl serviceImpl =ac.getBean(MerchantServiceImpl.class);
    	//ShopServiceImpl shopServiceImpl=ac.getBean(ShopServiceImpl.class);
    	Merchant merchant=new Merchant();
    	merchant.setMerchantId(1L);
    	merchant.setMerchantName("11111");
    	//System.out.println(serviceImpl.queryPossessShopByMerchantId(0, null,1L, null, null, null));
//    	File file=new File("E:/aa.jpg");
//    	File file2=new File("E:/bb.jpg");
//    	File file3=new File("E:/cc.jpg");
    	//System.out.println(serviceImpl.fillInInformation(merchant, file, file2, file3, null, null, null));
    	//System.out.println(serviceImpl.businessLicensePicUpLoad(merchant, file3, null, null, null));
    	System.out.println(serviceImpl.changeMerchanStatus(1L, null, null, null,"freeze"));
    	//System.out.println(shopServiceImpl.freezeAllShop(1L, null, null, null));
    }
    @Test
    public void test3() {
    	MerchantServiceImpl serviceImpl =ac.getBean(MerchantServiceImpl.class);
    	Merchant merchant=new Merchant();
    	merchant.setMerchantId(1L);
    	merchant.setMerchantName("擦了");
    	System.out.println(serviceImpl.updateMerchantById(merchant, null, null, null));
    }
}
