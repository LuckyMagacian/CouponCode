package com.lanxi.couponcode.spi.service;

import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.annotations.HiddenArg;
import com.lanxi.couponcode.spi.consts.annotations.RealReturnType;
import com.lanxi.couponcode.spi.consts.enums.CommodityType;
import com.lanxi.couponcode.spi.consts.enums.OperateType;
import com.lanxi.couponcode.spi.consts.enums.RequestOperateType;
import com.lanxi.couponcode.spi.consts.enums.RequestStatus;

import java.math.BigDecimal;

/**
 * Created by yangyuanjian on 2017/11/9.
 */
public interface RequestService {
    @RealReturnType("List<")
    RetMessage<String> queryRequests(String timeStart,
                                      String timeStop,
                                      String commodityName,
                                      String merchantName,
                                      RequestOperateType type,
                                      RequestStatus status,
                                      @HiddenArg Long merchantId,
                                      @HiddenArg Integer pageNum,
                                      @HiddenArg Integer pageSize);
    RetMessage<Boolean> agreeRequest(@HiddenArg Long requestId,
                        @HiddenArg Long operaterId);
    RetMessage<Boolean> disagreeRequest(@HiddenArg Long requestId,
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
    @RealReturnType("List<OperateRequest>")
    RetMessage<String> queryCommodityRequest(String commodityName,
                                 CommodityType type,
                                 OperateType operateType,
                                 RequestStatus status,
                                 String timeStart,
                                 String timeEnd,
                                 @HiddenArg Integer pageNum,
                                 @HiddenArg Integer pageSize,
                                 @HiddenArg Long operaterId);

}
