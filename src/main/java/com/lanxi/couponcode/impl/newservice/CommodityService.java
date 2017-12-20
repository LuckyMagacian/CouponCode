package com.lanxi.couponcode.impl.newservice;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.Commodity;

import java.util.List;

/**
 * Created by yangyuanjian on 2017/11/17.
 */
public interface CommodityService {
    Boolean addCommodity(Commodity commodity);
    Commodity queryCommodity(Long commodityId);
    Commodity queryCommodity(Long commodityId, Long merchantId);
    List<Commodity> queryCommodities(Wrapper<Commodity> wrapper, Page<Commodity> page);
    Boolean delCommodity(Commodity commodity);
    Boolean shelveCommodity(Commodity commodity);
    Boolean unshelveCommodity(Commodity commodity);
    Boolean modifyCommodity(Commodity commodity);
    List<Commodity> queryAll();
}
