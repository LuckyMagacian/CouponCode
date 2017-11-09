package com.lanxi.couponcode.spi.service;

import com.lanxi.couponcode.spi.consts.annotations.HiddenArg;
import com.lanxi.couponcode.spi.consts.annotations.RealReturnType;
import com.lanxi.couponcode.spi.consts.enums.AccountType;
import com.lanxi.couponcode.spi.consts.enums.OperateTargetType;
import com.lanxi.couponcode.spi.consts.enums.OperateType;

/**
 * Created by yangyuanjian on 2017/11/9.
 */
public interface OperateRecordService {

    @RealReturnType("List<OperaterRecord>")
    String queryOperateRecord(OperateType type,
                              OperateTargetType targetType,
                              String merchantName,
                              String shopName,
                              String timeStart,
                              String timeStop,
                              AccountType accountType,
                              String name,
                              String phone,
                              @HiddenArg Long merchantId,
                              @HiddenArg Long shopId,
                              @HiddenArg Long operaterId,
                              @HiddenArg Long recordOperaterId);

    String queryMerchantOperaterRecord(OperateType type,
                                       String shopName,
                                       String timeStart,
                                       String timeEnd,
                                       AccountType accountType,
                                       String operaterName,
                                       String operaterPhone,
                                       @HiddenArg Long querierId);

    Boolean commodityShelveRequest(@HiddenArg Long commodityId,
                                   @HiddenArg Long operaterId);

    Boolean commodityUnshelveRequest(@HiddenArg Long commodityId,
                                     @HiddenArg Long operaterId);

    Boolean commodityDelRequest(@HiddenArg Long commodityId,
                                @HiddenArg Long operaterId);

    Boolean commodityModifyRequest(Integer costPrice,
                                   Integer lifeTime,
                                   String useDistription,
                                   @HiddenArg Long commodityId,
                                   @HiddenArg Long operaterId);

}
