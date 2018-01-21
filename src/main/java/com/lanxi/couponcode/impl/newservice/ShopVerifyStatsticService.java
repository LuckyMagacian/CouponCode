package com.lanxi.couponcode.impl.newservice;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.ShopDailyVerifyStatsitc;

import java.util.List;

public interface ShopVerifyStatsticService {
    ShopDailyVerifyStatsitc queryInfo(Long recordId);
    List<ShopDailyVerifyStatsitc> queryList(Wrapper<ShopDailyVerifyStatsitc> wrapper, Page<ShopDailyVerifyStatsitc> page);
}
