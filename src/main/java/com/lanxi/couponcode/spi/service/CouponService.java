package com.lanxi.couponcode.spi.service;

import com.lanxi.couponcode.impl.entity.CouponCode;
import com.lanxi.couponcode.spi.consts.annotations.HiddenArg;
import com.lanxi.couponcode.spi.consts.annotations.RealReturnType;

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
                       @HiddenArg Long     accountId
    );

    Boolean destroyCode( @HiddenArg Long codeId,
                         @HiddenArg Long accountId
    );
    Boolean postoneCode(@HiddenArg Long codeId,
                        @HiddenArg Long accountId);
    Boolean generateCode(Long merchantId,
                         Long commodityId,
                         String reason,
                         @HiddenArg Integer channel);




}
