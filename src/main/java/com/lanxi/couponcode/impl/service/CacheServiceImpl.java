package com.lanxi.couponcode.impl.service;

import com.lanxi.common.interfaces.RedisCacheServiceInterface;
import com.lanxi.couponcode.impl.config.ConstConfig;
import com.lanxi.couponcode.spi.consts.enums.IdType;
import com.lanxi.couponcode.spi.consts.enums.LockStatus;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.lanxi.couponcode.impl.config.ConstConfig.ID_DEFAULT_VALUE;
import static com.lanxi.couponcode.impl.config.FunName.ARTIFCAT;
import static com.lanxi.couponcode.impl.config.FunName.CODE_VALUE;
import static com.lanxi.couponcode.impl.config.FunName.MERCHANT_COUDE_VAR;
/**
 * <strong>基于redis的id生成器</strong>
 * 
 * @author yangyuanjian
 *
 */
@Deprecated
public class CacheServiceImpl implements RedisCacheServiceInterface {

	private RedisCacheServiceInterface redis;

	private ConfigService config;

	/**
	 * 删除redis中的数据
	 * @param key
	 * @return 
	 * 			false->键不存在或删除失败
	 * 			true ->删除成功
	 */
	public boolean delKey(String key) {
		Jedis conn = null;
		try {
			conn=redis.getRedisConn();
			long temp=conn.del(key);
			return temp==1; 
		} catch (Exception e) {
			throw new RuntimeException("delKey 时发生异常", e);
		} finally {
			conn.close();
		}
	}
	/**
	 * 删除hmap中的数据
	 * @param key map名称
	 * @param fields 对应的键的名称
	 * @return 
	 * 			false->键不存在或删除失败
	 * 			true ->删除成功
	 */
	public boolean delKey(String key,String... fields) {
		Jedis conn = null;
		try {
			conn=redis.getRedisConn();
			long temp=conn.hdel(key, fields);
			return temp==1; 
		} catch (Exception e) {
			throw new RuntimeException("delKeys 时发生异常", e);
		} finally {
			conn.close();
		}
	}
	
	
	public Map<String, String> getStrings(String...keys){
		Jedis conn = null;
		try {
			conn=redis.getRedisConn();
			Map<String, Response<String>> map=new HashMap<>();
			Map<String, String> result=new HashMap<>();
			Pipeline pipeline=conn.pipelined();
			Stream.of(keys).map(e->pipeline.get(e));
			pipeline.sync();
			map.entrySet().forEach(e->result.put(e.getKey(), e.getValue().get()));
			return result;
		} catch (Exception e) {
			throw new RuntimeException("gets 时发生异常", e);
		} finally {
			conn.close();
		}
	}
	
	public Map<String, byte[]> getBytes(byte[]...keys){
		Jedis conn = null;
		try {
			conn=redis.getRedisConn();
			Map<String, Response<byte[]>> map=new HashMap<>();
			Map<String, byte[]> result=new HashMap<>();
			Pipeline pipeline=conn.pipelined();
			Stream.of(keys).map(e->pipeline.get(e));
			pipeline.sync();
			map.entrySet().forEach(e->result.put(e.getKey(), e.getValue().get()));
			return result;
		} catch (Exception e) {
			throw new RuntimeException("gets 时发生异常", e);
		} finally {
			conn.close();
		}
	}
	
	
//	public <T> T getString(String key,Class<T> clazz) {
//		return (T) JSON.parseObject(redis.get(key),clazz);
//	}

	@Override
	public Long delete(byte[] p0) {
		return redis.delete(p0);
	}

	@Override
	public Long delete(String p0) {
		return redis.delete(p0);
	}

	@Override
	public byte[] get(byte[] p0) {
		return redis.get(p0);
	}

	@Override
	public String get(String p0) {
		return redis.get(p0);
	}

	@Override
	public List<byte[]> getMap(byte[] p0, byte[]... p1) {
		return redis.getMap(p0, p1);
	}

	@Override
	public List<String> getMap(String p0, String... p1) {
		return redis.getMap(p0, p1);
	}

	@Override
	public byte[] getMapOne(byte[] p0, byte[] p1) {
		return redis.getMapOne(p0, p1);
	}

	@Override
	public String getMapOne(String p0, String p1) {
		return redis.getMapOne(p0, p1);
	}

	@Override
	public Jedis getRedisConn() {
		return redis.getRedisConn();
	}

	@Override
	public Boolean has(byte[] p0) {
		return redis.has(p0);
	}

	@Override
	public Boolean has(String p0) {
		return redis.has(p0);
	}

	@Override
	public String set(byte[] p0, byte[] p1) {
		return redis.set(p0,p1);
	}

	@Override
	public String set(byte[] p0, byte[] p1, long p2) {
		return redis.set(p0,p1,p2);
	}

	@Override
	public String set(String p0, String p1) {
		return redis.set(p0,p1);
	}

	@Override
	public String set(String p0, String p1, long p2) {
		return redis.set(p0,p1,p2);
	}

	@Override
	public Long setKeyLife(byte[] p0, long p1) {
		return redis.setKeyLife(p0, p1);
	}

	@Override
	public Long setKeyLife(String p0, long p1) {
		return redis.setKeyLife(p0, p1);
	}

	@Override
	public String setMap(byte[] p0, Map<byte[], byte[]> p1) {
		return redis.setMap(p0, p1);
	}

	@Override
	public String setMap(String p0, Map<String, String> p1) {
		return redis.setMap(p0, p1);
	}

	@Override
	public Long setMapOne(byte[] p0, byte[] p1, byte[] p2) {
		return redis.setMapOne(p0, p1,p2);
	}

	@Override
	public Long setMapOne(String p0, String p1, String p2) {
		return redis.setMapOne(p0, p1,p2);
	}
	
}
