package com.lanxi.couponcode.spi.service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * redis服务接口<br>
 * Created by yangyuanjian on 2017/11/14.<br>
 */
public interface RedisService {
    /**
     * 获取连接
     */
    Jedis generateConn();

    /**
     * 销毁连接
     */
    Boolean destroyConn(Jedis jedis);

    Boolean set(String key, String value, Long life);

    Boolean set(String key, byte[] value, Long life);

    Boolean set(byte[] key, byte[] value, Long life);

//    Boolean set(byte[] key,byte[] value,Long life);

    Boolean setEx(String key, String value, Long life);

//    Boolean setEx(byte[] key,byte[] value,Long life);

    Boolean setNx(String key, String value, Long life);

//    Boolean setNx(byte[] key,byte[] value,Long life);

    Boolean exists(String key);

    Boolean exists(String... keys);

    Set<String> keys(String pattern);

    Boolean delKeyByPattern(String pattern);

    String get(String key);

    byte[] getBytes(String key);

    byte[] get(byte[] key);


//    byte[] get(byte[] key);

    Collection<String> get(String... keys);

//    Collection<byte[]> get(byte[]... keys);

    Boolean del(String key);

//    Boolean del(byte[] key);

    Boolean del(String... keys);

//    Boolean del(byte[]... keys);

    Boolean expire(String key, Long life);

    Long incr(String key);

    Long incrBy(String key, long step);

    Long decr(String key);

    Long decrBy(String key, long step);

    //------------------------------------------------------------------------------------------------------------------

    Boolean hset(String mapName, String key, String value);

    Boolean hset(String mapName, Map<String, String> value);

    Boolean hset(byte[] mapName, byte[] key, byte[] value);

    Boolean hset(String mapName, byte[] key, byte[] value);

    Boolean hset(String mapName, String key, byte[] value);



    String hget(String mapName, String key);

    byte[] hgetBytes(String mapName, String key);

    byte[] hget(byte[] mapName, byte[] key);


    Map<String, String> hget(String mapName);

    Collection<String> hget(String mapName, String... keys);

    Boolean hsetNx(String mapName, String key, String value);

    Boolean hsetEx(String mapName, String key, String value);

    Boolean hexists(String mapName, String key);

    Long hlength(String mapName);

    Collection<String> hkeys(String mapName);

    Collection<String> hvals(String mapName);

    Long hincr(String mapName, String key);

    Long hincrBy(String mapName, String key, Long step);

    Long hdecr(String mapName, String key);

    Long hdecrBy(String mapName, String key, Long step);

    Boolean hdel(String mapName, String key);

    Boolean hdel(String mapName, String... keys);
    //------------------------------------------------------------------------------------------------------------------

    Boolean lpush(String listName, String value);

    Boolean rpush(String listName, String value);

    String lpop(String listName);

    String rpop(String listName);

    Long llength(String listName);

    //------------------------------------------------------------------------------------------------------------------

    Boolean sadd(String setName, String... values);

    Collection<String> smembers(String setName);

    Boolean sremove(String setName, String... members);

//    Long ssize(String setName);

    Boolean sismember(String setName, String member);

    //------------------------------------------------------------------------------------------------------------------

//    Boolean zadd(String zsetName,long order,String value);
//
//    Boolean zadd(String zsetName,Map<String,Long> values);
//
//    Boolean zremove(String zsetName,String... members);
//
//    Long zsize(String zsetName);
//
//    Long zgetorder(String zetName,String member);

    //------------------------------------------------------------------------------------------------------------------
    Pipeline pipeline();
}
