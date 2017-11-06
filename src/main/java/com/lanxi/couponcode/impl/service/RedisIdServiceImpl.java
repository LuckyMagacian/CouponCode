package com.lanxi.couponcode.impl.service;

import com.lanxi.common.interfaces.RedisCacheServiceInterface;
import com.lanxi.couponcode.spi.consts.enums.IdType;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;

import static com.lanxi.couponcode.impl.config.ConstConfig.ARTIFCAT;
import static com.lanxi.couponcode.impl.config.ConstConfig.ID_DEFAULT_VALUE;

/**
 * redis唯一id生成接口实现类
 * Created by yangyuanjian on 2017/10/31.
 */
@Deprecated
public class RedisIdServiceImpl implements RedisIdService{
//    @Resource
//    private RedisCacheServiceInterface redis;
    @Resource
    private RedisCodeService redis;
    @Override
    public long generateId(Long merchantId, IdType idType) {
        Jedis conn=null;
        String key=ARTIFCAT+idType;
        try {
            conn=redis.getRedisConn();
            long currentId=conn.hincrBy(key,merchantId+"",1);
            if(currentId<ID_DEFAULT_VALUE) {
                conn.hset(key,merchantId+"",ID_DEFAULT_VALUE+"");
                currentId=conn.hincrBy(key,merchantId+"",1);
            }
            return currentId;
        } catch (Exception e) {
            throw new RuntimeException("生产商户["+merchantId+"],id类型["+idType+"]的Id时发生异常", e);
        } finally {
            if(conn!=null)
                conn.close();
        }
    }
    public boolean addId(String merchantId,IdType idType,String id){
        return false;
    }
    public boolean lockId(String merchantId,IdType idType,String id){
        return false;
    }
    public boolean unlockId(String merchantId,IdType idType,String id){
        return false;
    }
}
