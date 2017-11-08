package com.lanxi.couponcode.spi.service;

import com.lanxi.couponcode.spi.consts.annotations.HiddenArg;
import com.lanxi.couponcode.spi.consts.enums.CommodityType;

import java.math.BigDecimal;

/**
 * Created by yangyuanjian on 2017/11/7.
 */
public interface CommodityService {
    Boolean addCommodity(String commodityName,
                         CommodityType commodityType,
                         BigDecimal facePrice,
                         BigDecimal costPrice,
                         BigDecimal sellPrice,
                         Integer lifeTime,
                         String merchantName,
                         @HiddenArg Long merchantId,
                         @HiddenArg Long operaterId);

    Boolean modifyCommodity(BigDecimal costPrice,
                            BigDecimal facePrice,
                            BigDecimal sellPrice,
                            Integer lifeTime,
                            @HiddenArg Long operaterId,
                            @HiddenArg Long commodityId);

    Boolean shelveCommodity(@HiddenArg Long commodityId,
                            @HiddenArg Long operaterId);
    Boolean unshelveCommodity(@HiddenArg Long commodityId,
                              @HiddenArg Long operaterId);
    Boolean delCommodity(@HiddenArg Long commodity,
                         @HiddenArg Long operaterId);

}
