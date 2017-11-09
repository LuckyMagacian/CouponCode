package com.lanxi.couponcode.spi.service;

import com.lanxi.couponcode.impl.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.annotations.HiddenArg;
import com.lanxi.couponcode.spi.consts.annotations.RealReturnType;
import com.lanxi.couponcode.spi.consts.enums.CommodityStatus;
import com.lanxi.couponcode.spi.consts.enums.CommodityType;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by yangyuanjian on 2017/11/7.
 */
public interface CommodityService {
    RetMessage<Boolean> addCommodity(String commodityName,
                                     CommodityType commodityType,
                                     BigDecimal facePrice,
                                     BigDecimal costPrice,
                                     BigDecimal sellPrice,
                                     Integer lifeTime,
                                     String merchantName,
                                     @HiddenArg Long merchantId,
                                     @HiddenArg Long operaterId);

    RetMessage<Boolean> modifyCommodity(BigDecimal costPrice,
                            BigDecimal facePrice,
                            BigDecimal sellPrice,
                            Integer lifeTime,
                            @HiddenArg Long commodityId,
                            @HiddenArg Long operaterId);

    RetMessage<Boolean> shelveCommodity(@HiddenArg Long commodityId,
                            @HiddenArg Long operaterId);
    RetMessage<Boolean> unshelveCommodity(@HiddenArg Long commodityId,
                              @HiddenArg Long operaterId);
    RetMessage<Boolean> delCommodity(@HiddenArg Long commodity,
                         @HiddenArg Long operaterId);
    @RealReturnType("List<Commodity>")
    RetMessage<String> queryCommodities(String merchantName,
                            String commodityName,
                            CommodityType commodityType,
                            CommodityStatus commodityStatus,
                            String timeStart,
                            String timeEnd,
                            @HiddenArg Integer pageNum,
                            @HiddenArg Integer pageSize,
                            @HiddenArg Long operaterId);

    RetMessage<File> queryCommoditiesExport(String merchantName,
                                String commodityName,
                                CommodityType commodityType,
                                CommodityStatus commodityStatus,
                                String timeStart,
                                String timeEnd,
                                @HiddenArg Long operaterId);
    @RealReturnType("List<Commodity>")
    RetMessage<String> merchantQueryCommodities(String commodityName,
                                    CommodityType type,
                                    CommodityStatus status,
                                    @HiddenArg Integer pageNum,
                                    @HiddenArg Integer pageSize,
                                    @HiddenArg Long merchantId,
                                    @HiddenArg Long operaterId);


    RetMessage<File> merchantQueryCommoditiesExport(String commodityName,
                                    CommodityType type,
                                    CommodityStatus status,
                                    @HiddenArg Long merchantId,
                                    @HiddenArg Long operaterId);

    

}
