package com.lanxi.couponcode.test;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.lanxi.couponcode.impl.entity.*;
import com.lanxi.couponcode.impl.newcontroller.LoginController;
import com.lanxi.couponcode.impl.newcontroller.OperateRecordController;
import com.lanxi.couponcode.impl.newservice.AccountService;
import com.lanxi.couponcode.impl.newservice.CommodityService;
import com.lanxi.couponcode.impl.newservice.DaoService;
import com.lanxi.couponcode.impl.newservice.OperateRecordService;
import com.lanxi.couponcode.impl.ztest.TestAop;
import com.lanxi.couponcode.spi.aop.AddLog;
import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.couponcode.spi.consts.enums.AccountType;
import com.lanxi.couponcode.spi.service.LoginService;
import com.lanxi.couponcode.spi.service.MerchantService;
import com.lanxi.util.entity.MyClassLoader;
import com.lanxi.util.utils.ExcelUtil;
import com.lanxi.util.utils.LoggerUtil;
import com.lanxi.util.utils.LoggerUtil.LogLevel;
import com.lanxi.util.utils.SpringUtil;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:xml/spring-mvc.xml")
public class TestMybatis {
	@Resource
	ApplicationContext ac;
	@Resource
	SqlSessionFactory ssf;
	@Resource
	DaoService daoService;
	@Before
	public void init() {
		LoggerUtil.setLogLevel(LogLevel.INFO);
		LoggerUtil.init();
	}
	@Test
	public void test2(){
		System.out.println(ac.getClass());
		System.out.println(((ConfigurableApplicationContext)ac).getBeanFactory());
		DefaultListableBeanFactory dlbf;
		System.out.println();
	}
	@Test
	public void test() throws Throwable{
//		SqlSession session=ssf.openSession();
//		System.out.println(session.selectList("select user_id from users"));;
//		User user=new User();
////		user.setUserId("10086");
//		user.setName("chinaMobile");
//		user.insert();
		Object obj=null;
		try {
			obj=ac.getBean("10086");
		} catch (Exception e) {
		}
		System.out.println(ac.getClass());
		System.err.println("obj1:"+obj);
		Map<String,Object> map=new HashMap<>();
		new MyClassLoader().loadClasspathClasses();
		Field[] fields=new MyClassLoader().loadClasspathClass("User.class").getDeclaredFields();
		Stream.of(fields).filter(e->e.getType().equals(String.class)).forEach(e->map.put(e.getName(),"嘿嘿嘿"));
		System.out.println(SpringUtil.registerBean("10086",new MyClassLoader().loadClasspathClass("User.class"),null));
		System.out.println("obj2:"+SpringUtil.getBean("10086"));
		System.out.println(SpringUtil.unregisterBean("10086"));
	}

	@Test
	public void test3(){
		CodeAlgorithm codeAlgorithm=new CodeAlgorithm();
        codeAlgorithm.setMerchantId(IdWorker.getId());
        codeAlgorithm.insert();
    }
    @Test
	public void test4() throws NoSuchMethodException {
//		System.out.println(codeOperateRecordService.queryCodeOperateRecord(
//				1,10,null,null,null,null,null,null,
//				null,null,null,null,null,true
//		).getClass());
		System.out.println(daoService.getCouponCodeDao().selectList(new EntityWrapper<CouponCode>().eq("code",123456L)));
	}

	@Test
	public void testAop(){
		TestAop test=ac.getBean(TestAop.class);
		test.sayHello();
	}
    @Test
    public void test5() throws IOException {
		File file=new File("testList.xls");
		if(!file.exists())
			file.createNewFile();
		List<Account> accounts=ac.getBean(AccountService.class).queryAccounts(new EntityWrapper<Account>(),null);
		ExcelUtil.exportExcelFile(accounts,null,new FileOutputStream(file));
    }	



    @Test
	public void test6(){
		System.out.println(daoService.getMerchantDao().insert((Merchant)TestSpring.fillEntity.apply(new Merchant())));
	}
    @Test
	public void test7(){
		System.out.println(ac.getBean(LoginService.class).logout(938692005297397761L));
	}
	@Test
	public void test8() {
		Commodity commodity=ac.getBean(CommodityService.class).queryCommodity(940127998683226112L);
		commodity.setCostPrice(new BigDecimal(5.2));
		System.out.println(commodity.updateById());
		System.out.println(ac.getBean(com.lanxi.couponcode.spi.service.CommodityService.class).modifyCommodity(
				new BigDecimal(1.2),null,null,null,940127998683226112L,938694848231235589L
		));
	}


}
