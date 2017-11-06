package com.lanxi.couponcode.impl.service;

import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * redis串码生成接口
 * Created by yangyuanjian on 2017/10/31.
 */
public interface RedisCodeService {
    public long getCodeVar(Long merchantId);
    public boolean addCode(Long merchantId,Long code);
    public boolean checkCodeExist(Long merchantId,Long code);
    public boolean lockCode(Long merchantId,Long code,String locker);
    public boolean lockCodeForce(Long merchantId,Long code,String locker);
    public boolean unlockCode(Long merchantId,Long code,String unlocker);
    public boolean unlockCodeForce(Long merchantId,Long code,String unlocker);
    public boolean delCode(Long merchantId,String... codes);
    public boolean updateCodeVar(Long merchantId,Long codeVar);
    public long getCode(Long merchantId);
    public Jedis getRedisConn();
}
