package com.lanxi.couponcode.impl.newservice;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.Commodity;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by yangyuanjian on 2017/11/17.
 */
public class CommodityServiceImpl implements CommodityService{
    @Resource
    private DaoService daoService;
    @Resource
    private RedisService redisService;
    @Resource
    private RedisEnhancedService redisEnhancedService;

    @Override
    public Boolean addCommodity(Commodity commodity) {
        return commodity.insert();
    }

    @Override
    public Commodity queryCommodity(Long commodityId) {
        return null;
    }

    @Override
    public List<Commodity> queryCommodities(Wrapper<Commodity> wrapper, Page<Commodity> page) {
        return null;
    }

    @Override
    public Boolean delCommodity(Commodity commodity) {
        return null;
    }

    @Override
    public Boolean shelveCommodity(Commodity commodity) {
        return null;
    }

    @Override
    public Boolean unshelveCommodity(Commodity commodity) {
        return null;
    }

    @Override
    public Boolean freezeCommodity(Commodity commodity) {
        return null;
    }

    @Override
    public Boolean unfreezeCommodity(Commodity commodity) {
        return null;
    }

    @Override
    public Boolean modifyCommodity(Commodity commodity) {
        return null;
    }
}
