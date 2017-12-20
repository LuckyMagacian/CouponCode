package com.lanxi.couponcode.test;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * Created by yangyuanjian on 2017/10/31.
 */
public class TestRedis {
    private Jedis conn = null;

    @Before
    public void init() {
        conn = new Jedis("192.168.17.186");
    }


    @Test
    public void test1() {
//        System.out.println(conn.del("test"));
//        System.out.println(conn.hincrBy("test","10086",1));
        System.out.println(conn.hget("test", "10010"));
        conn.close();
    }
}
