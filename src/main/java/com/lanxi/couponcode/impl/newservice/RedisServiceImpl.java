package com.lanxi.couponcode.impl.newservice;

import com.lanxi.couponcode.impl.newservice.ConfigService;
import com.lanxi.util.entity.LogFactory;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import redis.clients.jedis.*;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * redis服务类<br>
 * Created by yangyuanjian on 2017/11/14.<br>
 */
@Order(-10000)
@Service("redisService")
public class RedisServiceImpl implements RedisService{
    /**job接口,用于真正执行redis操作*/
    @FunctionalInterface
    private interface Job<T>{
        T job(Jedis conn);
    };
    /**work方法,封装redis连接的获取及销毁,执行job*/
    private <T> T work(Job<T> job){
        Jedis jedis=generateConn();
        T t=job.job(jedis);
        destroyConn(jedis);
        return t;
    };

    /**配置文件服务类*/
    @Resource
    private ConfigService config;
    /**
     * redis连接池
     */
    private JedisPool pool;
    /**
     * redis默认端口号
     */
    private static final int REDIS_DEFAULT_PORT=6379;

    /**redis的成功字符串*/
    private static final String REDIS_SUCCESS="OK";

    /**redis set时表示仅仅set已存在的key时的状态*/
    private static final String EXISTED="XX";

    /**redis set时 表示仅仅set未存在的值时的状态*/
    private static final String NOT_EXISTED="NX";

    /**redis set时 表示生命周期用秒来作为计时单位*/
    private static final String SECONDS="EX";

    /**redis set时 表示生命周期用毫秒来作为计时单位*/
    private static final String MILLI_SECONDS="PX";

    public RedisServiceImpl(){

    }

    public RedisServiceImpl(ConfigService configService){
        this.config=configService;
    }

    /**
     * 初始化redis连接池
     * 由于redis连接无法通过dubbo获取,改为本地redis连接池提供,
     */
    @Lazy
    public void redisInit(){
        String url=config.getValue("redis","url");
        int port=config.getValue("redis","port")==null?REDIS_DEFAULT_PORT:Integer.parseInt(config.getValue("redis","port"));
        String password=config.getValue("redis","password");
        JedisPoolConfig poolConfig=new JedisPoolConfig();
//		config.setMaxWaitMillis(1000);
        poolConfig.setMaxTotal(Integer.parseInt(config.getValue("redis","maxTotal")));
        poolConfig.setMaxIdle(Integer.parseInt(config.getValue("redis","maxIdle")));
        poolConfig.setMinIdle(Integer.parseInt(config.getValue("redis","minIdle")));
        poolConfig.setMaxWaitMillis(Integer.parseInt(config.getValue("redis","maxWaitMillis")));
        poolConfig.setTestOnBorrow(Boolean.parseBoolean(config.getValue("redis","testOnBorrow")));
        poolConfig.setBlockWhenExhausted(Boolean.parseBoolean(config.getValue("redis","blockWhenExhausted")));
        poolConfig.setTestWhileIdle(Boolean.parseBoolean(config.getValue("redis","testWhileIdle")));
        if(password!=null&&!password.isEmpty()){
            LogFactory.info(this, "init redis pool by password !");
            pool=new JedisPool(poolConfig, url, port, 10000, password);
        }
        else
            pool=new JedisPool(poolConfig, url, port);
    }




    @Override
    public Jedis generateConn() {
        return pool.getResource();
    }

    @Override
    public Boolean destroyConn(Jedis jedis) {
        if(jedis==null)
            return true;
        try {
            jedis.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean set(String key, String value, Long life) {
        if(life==null)
            return REDIS_SUCCESS.equals(work(e->e.set(key,value)));
        return REDIS_SUCCESS.equals(work(e->e.psetex(key,life,value)));
    }

    @Override
    public Boolean setEx(String key, String value, Long life) {

        return REDIS_SUCCESS.equals(work(e->e.set(key,value,EXISTED,MILLI_SECONDS,life)));
    }

    @Override
    public Boolean setNx(String key, String value, Long life) {
        return REDIS_SUCCESS.equals(work(e->e.set(key,value,NOT_EXISTED,MILLI_SECONDS,life)));
    }

    @Override
    public Boolean exists(String key) {
        return work(e->e.exists(key));
    }

    @Override
    public Boolean exists(String... keys) {
        return keys.length==work(e->e.exists(keys));
    }

    @Override
    public String get(String key) {
        return work(e->e.get(key));
    }

    @Override
    public Collection<String> get(String... keys) {
        return work(e->e.mget(keys));
    }

    @Override
    public Boolean del(String key) {
        return work(e->e.del(key))>0;
    }

    @Override
    public Boolean del(String... keys) {
        return work(e->e.del(keys))>0;
    }

    @Override
    public Boolean expire(String key, Long life) {
        return work(e->e.pexpire(key,life))>0;
    }

    @Override
    public Long incr(String key) {
        return work(e->e.incr(key));
    }

    @Override
    public Long incrBy(String key, long step) {
        return work(e->e.incrBy(key,step));
    }

    @Override
    public Long decr(String key) {
        return work(e->e.decr(key));
    }

    @Override
    public Long decrBy(String key, long step) {
        return work(e->e.decrBy(key,step));
    }

    @Override
    public Boolean hset(String mapName, String key, String value) {
        return work(e->e.hset(mapName,key,value))>0;
    }

    @Override
    public Boolean hset(String mapName, Map<String, String> value) {
        if(value==null||value.isEmpty())
            return true;
        return REDIS_SUCCESS.equals(work(e->e.hmset(mapName,value)));
    }

    @Override
    public String hget(String mapName, String key) {
        return work(e->e.hget(mapName,key));
    }

    @Override
    public Map<String, String> hget(String mapName) {
        return work(e->e.hgetAll(mapName));
    }

    @Override
    public Collection<String> hget(String mapName, String... keys) {
        return work(e->e.hmget(mapName,keys));
    }

    @Override
    public Boolean hsetNx(String mapName, String key, String value) {
        return work(e->e.hsetnx(mapName,key,value))>0;
    }

    @Override
    public Boolean hsetEx(String mapName, String key, String value) {
        String now=work(e->e.hget(mapName,key));
        if(hexists(mapName,key)){
            return work(e->e.hsetnx(mapName,key,value))>0;
        }else{
            return false;
        }
    }

    @Override
    public Boolean hexists(String mapName, String key) {
        return work(e->e.hexists(mapName,key));
    }

    @Override
    public Long hlength(String mapName) {
        return work(e->e.hlen(mapName));
    }

    @Override
    public Collection<String> hkeys(String mapName) {
        return work(e->e.hkeys(mapName));
    }

    @Override
    public Collection<String> hvals(String mapName) {
        return work(e->e.hvals(mapName));
    }

    @Override
    public Long hincr(String mapName, String key) {
        return work(e->e.hincrBy(mapName,key,1));
    }

    @Override
    public Long hincrBy(String mapName, String key, Long step) {
        return work(e->e.hincrBy(mapName,key,step));
    }

    @Override
    public Long hdecr(String mapName, String key) {
        return work(e->e.hincrBy(mapName,key,-1));
    }

    @Override
    public Long hdecrBy(String mapName, String key, Long step) {
        return work(e->e.hincrBy(mapName,key,-step));
    }

    @Override
    public Boolean hdel(String mapName, String key) {
        return work(e->e.hdel(mapName,key))>0;
    }

    @Override
    public Boolean hdel(String mapName, String... keys) {
        return work(e->e.hdel(mapName,keys))>0;
    }

    @Override
    public Boolean lpush(String listName, String value) {
        return work(e->e.lpush(listName,value))>0;
    }

    @Override
    public Boolean rpush(String listName, String value) {
        return work(e->e.rpush(listName,value))>0;
    }

    @Override
    public String lpop(String listName) {
        return work(e->e.lpop(listName));
    }

    @Override
    public String rpop(String listName) {
        return work(e->e.rpop(listName));
    }

    @Override
    public Long llength(String listName) {
        return work(e->e.llen(listName));
    }

    @Override
    public Boolean sadd(String setName, String... values) {
        return work(e->e.sadd(setName,values))==values.length;
    }

    @Override
    public Collection<String> smembers(String setName) {
        return work(e->e.smembers(setName));
    }

    @Override
    public Boolean sremove(String setName,String... members) {
        return work(e->e.srem(setName,members))==members.length;
    }

//    @Override
//    public Long ssize(String setName) {
//        return work(e->e.s);
//    }

    @Override
    public Boolean sismember(String setName, String member) {
        return work(e->e.sismember(setName,member));
    }

    public ConfigService getConfig() {
        return config;
    }

    public void setConfig(ConfigService config) {
        this.config = config;
        redisInit();
    }
}
