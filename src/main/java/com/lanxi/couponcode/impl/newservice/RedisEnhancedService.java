package com.lanxi.couponcode.impl.newservice;

/**
 * Created by yangyuanjian on 2017/11/14.
 */
public interface RedisEnhancedService {
    /**
     *
     * @param key
     * @param locker
     * @return true 锁定成功
     *         false 锁定失败
     *         null 对象不存在或发生异常
     */
    Boolean lock(String key,String locker);
    /**
     *
     * @param mapName
     * @param key
     * @param locker
     * @return true 锁定成功
     *         false 锁定失败
     *         null 对象不存在或发生异常
     */
    Boolean lock(String mapName,String key,String locker);
    /**
     *
     * @param key
     * @param locker
     * @return true 锁定成功
     *         false 锁定失败
     *         null 对象不存在或发生异常
     */
    Boolean lockForce(String key,String locker);
    /**
     *
     * @param mapName
     * @param key
     * @param locker
     * @return true 锁定成功
     *         false 锁定失败
     *         null 对象不存在或发生异常
     */
    Boolean lockForce(String mapName,String key,String locker);
    /**
     *
     * @param key
     * @param unlocker
     * @return true 解锁成功
     *         false 解锁失败
     *         null 对象不存在或发生异常
     */
    Boolean unlock(String key,String unlocker);
    /**
     *
     * @param mapName
     * @param key
     * @param unlocker
     * @return true 解锁成功
     *         false 解锁失败
     *         null 对象不存在或发生异常
     */
    Boolean unlock(String mapName,String key,String unlocker);
    /**
     *
     * @param key
     * @param unlocker
     * @return true 解锁成功
     *         false 解锁失败
     *         null 对象不存在或发生异常
     */
    Boolean unlockForce(String key,String unlocker);
    /**
     *
     * @param mapName
     * @param key
     * @param unlocker
     * @return true 解锁成功
     *         false 解锁失败
     *         null 对象不存在或发生异常
     */
    Boolean unlockForce(String mapName,String key,String unlocker);

    Long generateId(String key);

    Long generatedId(String mapName,String key);

    Boolean workLockJob(RedisEnhancedServiceImpl.LockJob job, String key, String locker);

    Boolean workLockJob(RedisEnhancedServiceImpl.LockJob job, String mapName, String key, String locker);
}
