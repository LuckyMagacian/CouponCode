package com.lanxi.couponcode.spi.service;

import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.annotations.HiddenArg;
import com.lanxi.couponcode.spi.consts.annotations.RealReturnType;
import com.lanxi.couponcode.spi.consts.enums.VerificationType;

import java.io.File;
import java.util.List;

/**
 * Created by yangyuanjian on 2017/11/7.
 */
public interface CouponService {
    @RealReturnType ("List<CouponCode>")
    RetMessage<String> queryCodes(String timeStart,
                                  String timeEnd,
                                  String merchantName,
                                  String commodityName,
                                  Long code,
                                  Long codeId,
                                  @HiddenArg Integer pageNum,
                                  @HiddenArg Integer pageSize,
                                  @HiddenArg Long operaterId
    );

    RetMessage<File> queryCodesExport(String timeStart,
                                      String timeEnd,
                                      String merchantName,
                                      String commodityName,
                                      Long code,
                                      Long codeId,
                                      @HiddenArg Long operaterId);

    RetMessage<Boolean> destroyCode(@HiddenArg Long codeId,
                                    @HiddenArg Long operaterId);

    RetMessage<Boolean> destroyCode(Long code,
                                    @HiddenArg Long merchantId,
                                    @HiddenArg Long operaterId);

    RetMessage<Boolean> verificateCode(@HiddenArg Long codeId,
                                       @HiddenArg Long operaterId,
                                       VerificationType verificationType);

    RetMessage<Boolean> verificateCode(Long code,
                                       @HiddenArg Long merchantId,
                                       @HiddenArg Long operaterId,
                                       VerificationType verificationType);

    RetMessage<Boolean> postoneCode(@HiddenArg Long codeId,
                                    @HiddenArg Long operaterId);

    RetMessage<String> generateCode(Long merchantId,
                                    Long commodityId,
                                    String reason,
                                    @HiddenArg Integer channel);
    RetMessage<String> generateCode(Long merchantId,
                                    Long commodityId,
                                    String reason,
                                    @HiddenArg Long operaterId);

    @RealReturnType ("CouponCode")
    RetMessage<String> couponCodeInfo(Long codeId,
                                      @HiddenArg Long operaterId);

    @RealReturnType (("CouponCode"))
    RetMessage<String> couponCodeInfo(Long merchantId,
                                      Long code,
                                      @HiddenArg Long operaterId);

}
