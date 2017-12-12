package com.lanxi.couponcode.impl.newservice;

import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.couponcode.spi.consts.enums.LockResult;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.util.utils.LoggerUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import static com.lanxi.couponcode.spi.assist.TimeAssist.*;
import static com.lanxi.couponcode.spi.consts.enums.LockResult.*;
import java.io.Serializable;

/**
 * Created by yangyuanjian on 2017/11/21.
 */
@Service("redisEnhancedService")
@EasyLog (LoggerUtil.LogLevel.INFO)
public class RedisEnhancedServiceImpl implements RedisEnhancedService{
    @Resource
    private RedisService redisService;

    private LockResult doLockCommon(LockJob<Serializable> job,String key){
        return doLockCommon(job,null,key);
    }

    private LockResult doLockCommon(LockJob<Serializable> job,String mapName,String key){
        Boolean check;
        //获取存在状况
        if(mapName==null)
            check=redisService.exists(key);
        else
            check=redisService.hexists(mapName,key);
        //判断存在性
        if(check==null)
            return exception;
        if(!check)
            return none;
        //执行lambda表达式
        Serializable jobResult=job.job();
        //若返回的是LockResult类型,直接返回任务结果,否则根据返回的内容来返回对应的加锁状态
        if(jobResult instanceof LockResult)
            return (LockResult) jobResult;
        else{
            if (jobResult == null) {
                return exception;
            }
            Boolean lockResult= (Boolean) jobResult;
            if (lockResult)
                return success;
            else
                return fail;
        }
    }

    public interface LockJob<T extends Serializable>{
        T job();
    }


    /**
     * 执行分布式锁redis操作
     * @param job 操作内容
     * @param key 需要锁定的Key
     * @param locker 锁定者,该参数可以为Null,若为null,则采用计时锁定的方式
     * @param <T> 返回的内容,仅仅当锁定成功后会有返回
     * @return {@link RetMessage}{@link LockResult},success==success,faill==fail,warning==occupy,exception==none,error==exception
     */
    public <T extends Serializable> RetMessage<T> workLockJob(LockJob<T> job, String key, String locker){
        return workLockJob(job,null,key,locker);
    }
    /**
     * 执行分布式锁redis操作
     * @param job 操作内容
     * @param mapName 需要锁定的map名称
     * @param key 需要锁定的Key
     * @param locker 锁定者,该参数可以为Null,若为null,则采用计时锁定的方式
     * @param <T> 返回的内容,仅仅当锁定成功后会有返回
     * @return {@link RetMessage}{@link LockResult},success==success,faill==fail,warning==occupy,exception==none,error==exception
     */
    public <T extends Serializable> RetMessage<T> workLockJob(LockJob<T> job, String mapName, String key, String locker){
        LockResult result=null;
        RetMessage<T> retMessage=new RetMessage<>();
        try {
            //判断是否是map操作
            if(mapName==null) {
                    result = lock(key, locker);
            }else{
                //判断是否是延时锁
                if (locker == null)
                    result = hlock(mapName,key);
                else
                    result = hlock(mapName,key,locker);
            }
            if(success.equals(result)){
                T t=job.job();
                retMessage.setAll(RetCodeEnum.success,null,t);
            }
            if(fail.equals(result))
                retMessage.setAll(RetCodeEnum.fail,null,null);
            if(occupy.equals(result))
                retMessage.setAll(RetCodeEnum.warning,null,null);
            if(none.equals(result))
                retMessage.setAll(RetCodeEnum.exception,null,null);
            if(exception.equals(result))
                retMessage.setAll(RetCodeEnum.error,null,null);
        } catch (Exception e) {
            retMessage.setAll(RetCodeEnum.error,null,null);
        }finally {
            //若锁成功,释放锁
            if(success.equals(result))
                if(mapName==null)
                    unlock(key,locker);
                else
                    hunlock(mapName,key);
            return retMessage;
        }
    }


    @Override
    public LockResult lock(String key, String locker) {
        LockJob<Serializable> job=()->{
            //获取旧锁
            String oldLocker= redisService.get(key);
            if(oldLocker==null||oldLocker.equals(locker))
                return redisService.set(key,locker,null);
            else
                return occupy;
        };
        return doLockCommon(job,key);
    }

    @Override
    public LockResult hlock(String mapName, String key, String locker) {
        LockJob<Serializable> job=()->{
            //获取旧锁
            String oldLocker= redisService.hget(mapName,key);
            if(oldLocker==null)
                return redisService.hset(mapName,key,locker);
            else if(oldLocker.equals(locker))
                return true;
            else {
                //判断旧锁是否是延时锁,且判断有效期
                if(isFormer(oldLocker)){
                    return redisService.hset(mapName,key,locker);
                }else{
                    return occupy;
                }
            }
        };
        return doLockCommon(job,mapName,key);
    }

    @Override
    public LockResult hlock(String mapName, String key) {
        LockJob<Serializable> job=()->{
            String oldLocker= redisService.hget(mapName,key);
            if(oldLocker==null){
                return redisService.hset(mapName,key,getTenSecondLater());
            }else{
                //判断旧锁是否是延时锁,且判断有效期
                if(isFormer(oldLocker)){
                    return redisService.hset(mapName,key,getTenSecondLater());
                }else{
                    return occupy;
                }
            }
        };
        return doLockCommon(job,key);
    }

    @Override
    public LockResult lockForce(String key, String locker) {
        LockJob<Serializable> job=()->redisService.set(key,locker,null);
        return doLockCommon(job,key);
    }

    @Override
    public LockResult hlockForce(String mapName, String key, String locker) {
        LockJob<Serializable> job=()->redisService.hset(mapName,key,locker);
        return doLockCommon(job,key);
    }

    @Override
    public LockResult hlockForce(String mapName, String key) {
        LockJob<Serializable> job=()->redisService.hset(mapName,key,getTenSecondLater());
        return doLockCommon(job,key);
    }

    @Override
    public LockResult unlock(String key, String unlocker) {
        LockJob<Serializable> job=()->{
            //获取旧锁
            String oldLocker= redisService.get(key);
            if(oldLocker==null)
                return true;
            if(oldLocker.equals(unlocker))
                return redisService.set(key,(String)null,null);
            else
                return occupy;
        };
        return doLockCommon(job,key);
    }

    @Override
    public LockResult hunlock(String mapName, String key, String unlocker) {
        LockJob<Serializable> job=()->{
            //获取旧锁
            String oldLocker= redisService.hget(mapName,key);
            if(oldLocker==null)
                return true;
            else if(oldLocker.equals(unlocker))
                return redisService.hset(mapName,key,(String)null);
            else {
                //判断旧锁是否是延时锁,且判断有效期
                if(isFormer(oldLocker)){
                    return redisService.hset(mapName,key,(String)null);
                }else{
                    return occupy;
                }
            }
        };
        return doLockCommon(job,mapName,key);
    }

    @Override
    public LockResult hunlock(String mapName, String key) {
        LockJob<Serializable> job=()->{
            //获取旧锁
            String oldLocker= redisService.hget(mapName,key);
            if(oldLocker==null)
                return true;
            else {
                //判断旧锁是否是延时锁,且判断有效期
                if(isFormer(oldLocker)){
                    return redisService.hset(mapName,key,(String)null);
                }else{
                    return occupy;
                }
            }
        };
        return doLockCommon(job,mapName,key);
    }

    @Override
    public LockResult unlockForce(String key) {
        LockJob<Serializable> job=()->redisService.set(key,(String)null,null);
        return doLockCommon(job,key);
    }

    @Override
    public LockResult hunlockForce(String mapName, String key) {
        LockJob<Serializable> job=()->redisService.hset(mapName,key,(String)null);
        return doLockCommon(job,key);
    }
}
