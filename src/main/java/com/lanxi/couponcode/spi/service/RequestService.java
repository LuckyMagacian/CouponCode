package com.lanxi.couponcode.spi.service;

import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.annotations.HiddenArg;
import com.lanxi.couponcode.spi.consts.annotations.RealReturnType;
import com.lanxi.couponcode.spi.consts.enums.CommodityType;
import com.lanxi.couponcode.spi.consts.enums.RequestOperateType;
import com.lanxi.couponcode.spi.consts.enums.RequestStatus;

import java.math.BigDecimal;

/**
 * Created by yangyuanjian on 2017/11/9.
 */
public interface RequestService {
    @RealReturnType ("List<")
    RetMessage<String> queryRequests(String timeStart,
                                     String timeStop,
                                     String commodityName,
                                     String merchantName,
                                     RequestOperateType type,
                                     RequestStatus status,
                                     CommodityType commodityType,
                                     @HiddenArg Long merchantId,
                                     @HiddenArg Integer pageNum,
                                     @HiddenArg Integer pageSize,
                                     @HiddenArg Long operaterId);

    RetMessage<String> queryRequests(String timeStart,
                                            String timeStop,
                                            String commodityName,
                                            RequestOperateType type,
                                            RequestStatus status,
                                            CommodityType commodityType,
                                            Long merchantId,
                                            Integer pageNum,
                                            Integer pageSize,
                                            Long operaterId);

    RetMessage<Boolean> agreeRequest(@HiddenArg Long requestId,
                                     @HiddenArg Long operaterId,
                                     String reason);

    RetMessage<Boolean> disagreeRequest(@HiddenArg Long requestId,
                                        @HiddenArg Long operaterId,
                                        String reason);

    @RealReturnType ("List<OperateRequest>")
    RetMessage<String> queryCommodityRequest(String commodityName,
                                             CommodityType type,
                                             RequestOperateType operateType,
                                             RequestStatus status,
                                             String timeStart,
                                             String timeEnd,
                                             @HiddenArg Integer pageNum,
                                             @HiddenArg Integer pageSize,
                                             @HiddenArg Long operaterId);


    RetMessage<Boolean> requestAddCommodity(String commodityName,
                                            CommodityType commodityType,
                                            BigDecimal facePrice,
                                            BigDecimal costPrice,
                                            BigDecimal sellPrice,
                                            Integer lifeTime,
                                            String useDistription,
                                            @HiddenArg Long operaterId,
                                            @HiddenArg Long merchantId);

    RetMessage<Boolean> requestModifyCommodity(BigDecimal costPrice,
                                               BigDecimal facePrice,
                                               BigDecimal sellPrice,
                                               Integer lifeTime,
                                               Long commodityId,
                                               Long operaterId);

    RetMessage<Boolean> requestShelveCommodity(Long commodityId,
                                               Long operaterId);

    RetMessage<Boolean> requestUnshelveCommodity(Long commodityId,
                                                 Long operaterId);

    RetMessage<Boolean> requestDelCommodity(Long commodityId,
                                            Long operaterId);

    RetMessage<String> queryRequest(Long requestId, Long operatorId);


}
