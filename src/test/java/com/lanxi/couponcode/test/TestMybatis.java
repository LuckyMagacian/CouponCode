package com.lanxi.couponcode.test;

import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.lanxi.couponcode.impl.entity.CodeAlgorithm;
import com.lanxi.util.entity.MyClassLoader;
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
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:xml/spring-mvc.xml")
public class TestMybatis {
	@Resource
	ApplicationContext ac;
	@Resource
	SqlSessionFactory ssf;
	@Before
	public void init() {
		LoggerUtil.setLogLevel(LogLevel.DEBUG);
		LoggerUtil.init();
		SpringUtil.setAc(ac);
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
}
