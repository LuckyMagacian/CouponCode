package com.lanxi.couponcode.test;

import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.Account;
import com.lanxi.couponcode.impl.entity.Merchant;
import com.lanxi.couponcode.impl.entity.Shop;
import com.lanxi.couponcode.impl.service.AccountServiceImpl;
import com.lanxi.couponcode.impl.service.CodeOperateRecordServiceImpl;
import com.lanxi.couponcode.impl.service.CouponCodeServiceImpl;
import com.lanxi.couponcode.impl.service.LoginServiceImpl;
import com.lanxi.couponcode.impl.service.MerchantServiceImpl;
import com.lanxi.couponcode.impl.service.ShopServiceImpl;
import com.lanxi.couponcode.spi.consts.enums.AccountType;
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
    	merchant.setMerchantName("蓝喜");
    	//System.out.println(serviceImpl.queryPossessShopByMerchantId(0, null,1L, null, null, null));
//    	File file=new File("E:/aa.jpg");
//    	File file2=new File("E:/bb.jpg");
//    	File file3=new File("E:/cc.jpg");
    	//System.out.println(serviceImpl.fillInInformation(merchant, file, file2, file3, null, null, null));
    	//System.out.println(serviceImpl.businessLicensePicUpLoad(merchant, file3, null, null, null));
    	//System.out.println(serviceImpl.changeMerchanStatus(1L, null, null, null,"freeze"));
    	//System.out.println(shopServiceImpl.freezeAllShop(1L, null, null, null));
    }
    @Test
    public void test3() {
    	MerchantServiceImpl serviceImpl =ac.getBean(MerchantServiceImpl.class);
    	Merchant merchant=new Merchant();
    	merchant.setMerchantId(1L);
    	System.out.println(serviceImpl.changeMerchanStatus(1L, null, null, null, "freeze"));
    	//merchant.setMerchantName("擦了");
//    	Page<Shop>pageObj=new Page<Shop>(1,10);
//    	System.out.println(serviceImpl.queryPossessShopByMerchantId(pageObj, 1L,null,null,null));
    }
    @Test
    public void test4() {
    	
    	LoginServiceImpl serviceImpl=ac.getBean(LoginServiceImpl.class);
    	//System.out.println(serviceImpl.merchantAccount(AccountType.admin, "aa", "13165978091", "bb", 1L, 1L));
    	//System.out.println(serviceImpl.delAccount(930337119628374016L,null));
//    	Page<Account>pageObj=new Page<Account>(1,10);
//    	System.out.println(serviceImpl.queryAccounts("13165978091", null, null, null, pageObj, null));
    	//System.out.println(serviceImpl.queryAccountInfo(930337119628374016L,null));
    	System.out.println(serviceImpl.login("13165978091","123456789", "a"));
    	
    }
    @Test
    public void test5() {
    	ShopServiceImpl serviceImpl=ac.getBean(ShopServiceImpl.class);
    	System.out.println(serviceImpl.importShops(new File("F:/aa.xlsx"), 1L, null));
    	//System.out.println(serviceImpl.addShop("蓝喜","杭州","aa", null, 1L, null));
    	
    }
    
   
}
