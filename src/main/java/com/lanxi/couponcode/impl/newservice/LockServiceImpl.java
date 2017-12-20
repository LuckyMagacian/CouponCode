package com.lanxi.couponcode.impl.newservice;

import com.lanxi.couponcode.spi.assist.RedisKeyAssist;
import com.lanxi.couponcode.impl.entity.*;
import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.util.utils.LoggerUtil;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by yangyuanjian on 11/28/2017.
 */
@Service
@EasyLog (LoggerUtil.LogLevel.INFO)
public class LockServiceImpl implements LockService {
    @Resource
    private RedisService redisService;
    @Resource
    private RedisEnhancedService redisEnhancedService;
    private static final long LOCK_TIME = 10000L;

    private Function<Object, String> getKey = o -> {
        String key = null;
        if (o == null)
            return null;
        if (o instanceof Merchant) {
            return RedisKeyAssist.getMerchantKey() + ((Merchant) o).getMerchantId();
        }
        if (o instanceof Shop) {
            return RedisKeyAssist.getShopKey() + ((Shop) o).getMerchantId() + ((Shop) o).getShopId();
        }
        if (o instanceof Commodity) {
            return RedisKeyAssist.getCommodityKey() + ((Commodity) o).getMerchantId() + ((Commodity) o).getCommodityId();
        }
        if (o instanceof Account) {
            return RedisKeyAssist.getAccountKey() + ((Account) o).getMerchantId() + ((Account) o).getShopId() + ((Account) o).getAccountId();
        }
        if (o instanceof ClearDailyRecord) {
            return RedisKeyAssist.getDailyRecordKey() + ((ClearDailyRecord) o).getMerchantId() + ((ClearDailyRecord) o).getRecordId();
        }
//        if(o instanceof Enterprise){
//            return RedisKeyAssist.getEnterpriseKey()+((Enterprise) o).getMerchantId();
//        }
        if (o instanceof Order) {
            return RedisKeyAssist.getOrderKey() + ((Order) o).getMerchantId() + ((Order) o).getOrderId();
        }
        if (o instanceof Request) {
            return RedisKeyAssist.getRequestKey() + ((Request) o).getMerchantId() + ((Request) o).getRquestId();
        }
        if (o instanceof CouponCode) {
            return RedisKeyAssist.getCodeKey() + ((CouponCode) o).getMerchantId() + ((CouponCode) o).getCodeId();
        }
        return null;
    };


    @Override
    public Boolean lock(Object obj) {
        String key = getKey.apply(obj);
        try {
            return key == null ? null : redisService.setNx(key, obj.hashCode() + "", LOCK_TIME);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Boolean> lock(List list) {
        List<Boolean> locks = new ArrayList<>();
        List<String> keys = (List<String>) list.stream().map(getKey).collect(Collectors.toList());
        try {
            Pipeline pipeline = redisService.pipeline();
            for (int i = 0; i < keys.size(); i++) {
                pipeline.set(keys.get(i), list.get(i).hashCode() + "", "NX", "PX", (int) LOCK_TIME);
            }
            Response<List<Object>> res = pipeline.exec();
            for (int i = 0, j = 0; i < keys.size(); i++) {
                if (keys.get(i) == null)
                    locks.add(null);
                else
                    locks.add((Boolean) res.get().get(j++));
            }
            return locks;
        } catch (Throwable e) {
            return null;
        }
    }

    @Override
    public Boolean unlock(Object obj) {
        String key = getKey.apply(obj);
        try {
            return key == null ? null : redisService.del(key);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Boolean> unlock(List list) {
        List<Boolean> locks = new ArrayList<>();
        List<String> keys = (List<String>) list.stream().map(getKey).collect(Collectors.toList());
        try {
            Pipeline pipeline = redisService.pipeline();
            for (int i = 0; i < keys.size(); i++) {
                if (keys != null)
                    pipeline.del(keys.get(i));
            }
            Response<List<Object>> res = pipeline.exec();
            for (int i = 0, j = 0; i < keys.size(); i++) {
                if (keys.get(i) == null)
                    locks.add(null);
                else
                    locks.add((Boolean) res.get().get(j++));
            }
            return locks;
        } catch (Throwable e) {
            return null;
        }
    }

//    private Boolean getLock(String key) {
//        return redisService.exists(key);
//    }
}
