package com.lanxi.couponcode.test;

import com.lanxi.couponcode.impl.service.RedisCodeService;
import com.lanxi.couponcode.impl.service.RedisCodeServiceImpl;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by yangyuanjian on 2017/11/1.
 */
public class TestRedisCodeService {
    RedisCodeService service;

    @Before
    public void init(){
        service=new RedisCodeServiceImpl();
    }

}
