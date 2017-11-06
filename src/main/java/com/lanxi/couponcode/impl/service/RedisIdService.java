package com.lanxi.couponcode.impl.service;

import com.lanxi.couponcode.spi.consts.enums.IdType;

/**
 * redis唯一id生成接口
 * Created by yangyuanjian on 2017/10/31.
 */
@Deprecated
public interface RedisIdService {
    public  long generateId(Long merchantId, IdType idType);
}
