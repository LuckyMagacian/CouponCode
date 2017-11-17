package com.lanxi.couponcode.spi.service;

import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.annotations.HiddenArg;
import com.lanxi.couponcode.spi.consts.annotations.RealReturnType;
import com.lanxi.couponcode.spi.consts.enums.AccountType;
import com.lanxi.couponcode.spi.consts.enums.OperateTargetType;
import com.lanxi.couponcode.spi.consts.enums.OperateType;

/**
 * Created by yangyuanjian on 2017/11/9.
 */
public interface OperateRecordService {

    @RealReturnType ("List<OperaterRecord>")
    RetMessage<String> queryOperateRecord(OperateType type,
                                          OperateTargetType targetType,
                                          String merchantName,
                                          String shopName,
                                          String timeStart,
                                          String timeStop,
                                          AccountType accountType,
                                          String name,
                                          String phone,
                                          @HiddenArg Integer pageNum,
                                          @HiddenArg Integer pageSize,
                                          @HiddenArg Long operaterId);


    RetMessage<String> queryMerchantOperateRecord(OperateType type,
                                                  OperateTargetType targetType,
                                                  String shopName,
                                                  String timeStart,
                                                  String timeEnd,
                                                  AccountType accountType,
                                                  String name,
                                                  String phone,
                                                  @HiddenArg Integer pageNum,
                                                  @HiddenArg Integer pageSize,
                                                  @HiddenArg Long operaterId);

    RetMessage<String> queryShopMerchantOperateRecord(OperateType type,
                                                      OperateTargetType targetType,
                                                      String timeStart,
                                                      String timeEnd,
                                                      AccountType accountType,
                                                      String name,
                                                      String phone,
                                                      @HiddenArg Integer pageNum,
                                                      @HiddenArg Integer pageSize,
                                                      @HiddenArg Long operaterId);

    RetMessage<String> queryOperateRecordInfo(Long recordId, Long operaterId);

//    RetMessage<Boolean> commodityShelveRequest(@HiddenArg Long commodityId,
//                                   @HiddenArg Long operaterId);
//
//    RetMessage<Boolean> commodityUnshelveRequest(@HiddenArg Long commodityId,
//                                     @HiddenArg Long operaterId);
//
//    RetMessage<Boolean> commodityDelRequest(@HiddenArg Long commodityId,
//                                @HiddenArg Long operaterId);
//
//    RetMessage<Boolean> commodityModifyRequest(Integer costPrice,
//                                   Integer lifeTime,
//                                   String useDistription,
//                                   @HiddenArg Long commodityId,
//                                   @HiddenArg Long operaterId);

}
