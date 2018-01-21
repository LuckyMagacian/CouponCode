package com.lanxi.couponcode.impl.newservice;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.ShopDailyVerifyStatsitc;

import javax.annotation.Resource;
import java.util.List;

public class ShopVerifyStatsticServiceImpl implements ShopVerifyStatsticService {
    @Resource
    private DaoService dao;

    @Override
    public ShopDailyVerifyStatsitc queryInfo(Long recordId) {
        return dao.getShopDailyVerifyStatsicDao().selectById(recordId);
    }

    @Override
    public List<ShopDailyVerifyStatsitc> queryList(Wrapper<ShopDailyVerifyStatsitc> wrapper, Page<ShopDailyVerifyStatsitc> page) {
        return page==null?dao.getShopDailyVerifyStatsicDao().selectList(wrapper):dao.getShopDailyVerifyStatsicDao().selectPage(page,wrapper);
    }
}
