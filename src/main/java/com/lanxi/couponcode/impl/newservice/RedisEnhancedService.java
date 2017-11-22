package com.lanxi.couponcode.impl.newservice;

import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.enums.LockResult;

import java.io.Serializable;

/**
 * Created by yangyuanjian on 2017/11/14.
 */
public interface RedisEnhancedService {
    Long LOCK_TIME_SHORT=1L;
    Long LOCK_TIME_MIDIUM=5L;
    Long LOCK_TIME_LONG=10L;
    /**
     *
     * @param key
     * @param locker
     * @return {@link LockResult}
     */
    LockResult lock(String key, String locker);
//    LockResult lock(String key);
    /**
     *
     * @param mapName
     * @param key
     * @param locker
     * @return true 锁定成功
     *         false 锁定失败
     *         null 对象不存在或发生异常
     */
    LockResult hlock(String mapName,String key,String locker);
    LockResult hlock(String mapName,String key);
    /**
     *
     * @param key
     * @param locker
     * @return {@link LockResult}
     */
    LockResult lockForce(String key,String locker);
//    LockResult lockForce(String key);
    /**
     *
     * @param mapName
     * @param key
     * @param locker
     * @return {@link LockResult}
     */
    LockResult hlockForce(String mapName,String key,String locker);
    LockResult hlockForce(String mapName,String key);
    /**
     *
     * @param key
     * @param unlocker
     * @return {@link LockResult}
     */
    LockResult unlock(String key,String unlocker);
//    LockResult unlock(String key);
    /**
     *
     * @param mapName
     * @param key
     * @param unlocker
     * @return true 解锁成功
     *         false 解锁失败
     *         null 对象不存在或发生异常
     */
    LockResult hunlock(String mapName,String key,String unlocker);
    LockResult hunlock(String mapName,String key);
    /**
     *
     * @param key
     * @return {@link LockResult}
     */
    LockResult unlockForce(String key);
//    LockResult unlockForce(String key);
    /**
     *
     * @param mapName
     * @param key
     * @return {@link LockResult}
     */
    LockResult hunlockForce(String mapName,String key);
    /**本方法返回的错误代码等级仅仅用于区分 锁定资源的操作结果*/
    <T extends Serializable> RetMessage<T> workLockJob(RedisEnhancedServiceImpl.LockJob<T> job, String key, String locker);
    /**本方法返回的错误代码等级仅仅用于区分 锁定资源的操作结果*/
    <T extends Serializable> RetMessage<T> workLockJob(RedisEnhancedServiceImpl.LockJob<T> job, String mapName, String key, String locker);
}
