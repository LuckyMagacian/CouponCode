package com.lanxi.couponcode.spi.service;

import com.lanxi.couponcode.impl.entity.CouponCode;
import com.lanxi.couponcode.spi.consts.annotations.HiddenArg;
import com.lanxi.couponcode.spi.consts.annotations.RealReturnType;

import java.io.File;
import java.util.List;

/**
 * Created by yangyuanjian on 2017/11/7.
 */
public interface CouponService {
    @RealReturnType("List<CouponCode>")
    String  queryCodes(String   timeStart,
                       String   timeEnd,
                       String   merchantName,
                       String   commodityName,
                       Long     code,
                       Long     codeId,
                       @HiddenArg Integer  pageNum,
                       @HiddenArg Integer  pageSize,
                       @HiddenArg Long     operaterId
    );

    File queryCodesExport(String   timeStart,
                          String   timeEnd,
                          String   merchantName,
                          String   commodityName,
                          Long     code,
                          Long     codeId,
                          @HiddenArg Long     operaterId);

    Boolean destroyCode(@HiddenArg Long codeId);
    Boolean destroyCode(Long code,
                        @HiddenArg Long accountId,
                        @HiddenArg Long operaterId);

    Boolean verificateCode(@HiddenArg Long codeId);
    Boolean verificateCode(Long code,
                           @HiddenArg Long accountId,
                           @HiddenArg Long operaterId);

    Boolean postoneCode(@HiddenArg Long codeId,
                        @HiddenArg Long accountId);
    Boolean generateCode(Long merchantId,
                         Long commodityId,
                         String reason,
                         @HiddenArg Integer channel);


    @RealReturnType("CouponCode")
    String couponCodeInfo(Long codeId,
                          @HiddenArg Long operaterId);
    @RealReturnType(("CouponCode"))
    String couponCodeInfo(Long merchantId,
                          Long code,
                          @HiddenArg Long operaterId);

}
