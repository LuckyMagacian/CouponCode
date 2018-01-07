package com.lanxi.couponcode.impl.newservice;

import com.lanxi.couponcode.spi.service.RedisService;

import javax.annotation.Resource;

/**
 * Created by yangyuanjian on 2017/11/14.
 */
@Deprecated
public class RedisEnhancedServiceImplOld {

    @Resource(name="redisService")
    private RedisService redis;

    public interface LockJob {
        Boolean job();
    }

    public Boolean workLockJob(LockJob job, String key, String locker) {
        Boolean lock = lock(key, locker);
        try {
            if (lock == null)
                return null;
            if (lock)
                return job.job();
            return false;
        } catch (Exception e) {
            return null;
        } finally {
            if (lock != null && lock)
                unlock(key, locker);
        }
    }

    public Boolean workLockJob(LockJob job, String mapName, String key, String locker) {
        Boolean lock = lock(mapName, key, locker);
        try {
            if (lock == null)
                return null;
            if (lock)
                return job.job();
            return false;
        } catch (Exception e) {
            return null;
        } finally {
            if (lock != null && lock)
                unlock(mapName, key, locker);
        }
    }

    /**
     * @param key
     * @param locker
     * @return true 锁定成功
     * false 锁定失败
     * null 对象不存在或发生异常
     */
    public Boolean lock(String key, String locker) {
        if (!redis.exists(key))
            return null;

        String value = redis.get(key);
        Boolean result;
        if (value == null) {
            result = redis.set(key, locker, null);
        } else if (value.equals(locker)) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    /**
     * @param mapName
     * @param key
     * @param locker
     * @return true 锁定成功
     * false 锁定失败
     * null 对象不存在或发生异常
     */
    public Boolean lock(String mapName, String key, String locker) {
        if (!redis.hexists(mapName, key))
            return null;

        Boolean result;
        String value = redis.hget(mapName, key);
        if (value == null) {
            result = redis.hset(mapName, key, locker);
        } else if (value.equals(locker)) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    /**
     * @param key
     * @param locker
     * @return true 锁定成功
     * false 锁定失败
     * null 对象不存在或发生异常
     */
    public Boolean lockForce(String key, String locker) {
        if (!redis.exists(key))
            return null;

        Boolean result;
        result = redis.set(key, locker, null);
        return result;
    }

    /**
     * @param mapName
     * @param key
     * @param locker
     * @return true 锁定成功
     * false 锁定失败
     * null 对象不存在或发生异常
     */
    public Boolean lockForce(String mapName, String key, String locker) {
        if (!redis.hexists(mapName, key))
            return null;

        Boolean result;
        result = redis.hset(mapName, key, locker);
        return result;
    }

    /**
     * @param key
     * @param unlocker
     * @return true 解锁成功
     * false 解锁失败
     * null 对象不存在或发生异常
     */
    public Boolean unlock(String key, String unlocker) {
        if (!redis.exists(key))
            return null;

        String value = redis.get(key);
        Boolean result;
        if (value == null) {
            result = true;
        } else if (value.equals(unlocker)) {
            result = redis.set(key, (String) null, null);
        } else {
            result = false;
        }
        return result;
    }

    /**
     * @param mapName
     * @param key
     * @param unlocker
     * @return true 解锁成功
     * false 解锁失败
     * null 对象不存在或发生异常
     */
    public Boolean unlock(String mapName, String key, String unlocker) {
        if (!redis.hexists(mapName, key))
            return null;

        String value = redis.hget(mapName, key);
        Boolean result;
        if (value == null) {
            result = true;
        } else if (value.equals(unlocker)) {
            result = redis.hset(mapName, key, (String) null);
        } else {
            result = false;
        }
        return result;
    }

    /**
     * @param key
     * @param unlocker
     * @return true 解锁成功
     * false 解锁失败
     * null 对象不存在或发生异常
     */
    public Boolean unlockForce(String key, String unlocker) {
        if (!redis.exists(key))
            return null;

        Boolean result;
        result = redis.set(key, (String) null, null);
        return result;
    }

    /**
     * @param mapName
     * @param key
     * @param unlocker
     * @return true 解锁成功
     * false 解锁失败
     * null 对象不存在或发生异常
     */
    public Boolean unlockForce(String mapName, String key, String unlocker) {
        if (!redis.hexists(mapName, key))
            return null;

        Boolean result;
        result = redis.hset(mapName, key, (String) null);
        return result;
    }

    public Long generateId(String key) {
        Long reuslt = redis.incr(key);
        return reuslt;
    }

    public Long generatedId(String mapName, String key) {
        Long reuslt = redis.hincr(mapName, key);
        return reuslt;
    }
}
