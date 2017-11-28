package com.lanxi.couponcode.impl.newservice;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.Commodity;
import com.lanxi.couponcode.spi.consts.enums.CommodityStatus;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

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
        return daoService.getCommodityDao().selectById(commodityId);
    }

    @Override
    public Commodity queryCommodity(Long commodityId, Long merchantId) {
        EntityWrapper<Commodity> wrapper=new EntityWrapper<>();
        wrapper.eq("commodity_id",commodityId);
        wrapper.eq("merchant_id",merchantId);
        List<Commodity> list=queryCommodities(wrapper,null);
        if(list==null||list.isEmpty())
            return null;
        if(list.size()>1)
            return null;
        return list.get(0);
    }

    @Override
    public List<Commodity> queryCommodities(Wrapper<Commodity> wrapper, Page<Commodity> page) {
        if(page==null)
            return daoService.getCommodityDao().selectList(wrapper);
        else
            return daoService.getCommodityDao().selectPage(page,wrapper);
    }

    @Override
    public Boolean delCommodity(Commodity commodity) {
        synchronized (commodity){
            commodity.setStatus(CommodityStatus.deleted);
            return commodity.updateById();
        }
    }

    @Override
    public Boolean shelveCommodity(Commodity commodity) {
        synchronized (commodity){
            commodity.setStatus(CommodityStatus.shelved);
            return  commodity.updateById();
        }
    }

    @Override
    public Boolean unshelveCommodity(Commodity commodity) {
        synchronized (commodity){
            commodity.setStatus(CommodityStatus.unshelved);
            return  commodity.updateById();
        }
    }

    @Override
    public Boolean modifyCommodity(Commodity commodity) {
        synchronized (commodity){
            return commodity.updateById();
        }
    }
}
