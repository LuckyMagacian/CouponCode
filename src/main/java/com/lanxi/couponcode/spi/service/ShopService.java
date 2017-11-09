package com.lanxi.couponcode.spi.service;

import com.lanxi.couponcode.spi.consts.annotations.HiddenArg;
import com.lanxi.couponcode.spi.consts.annotations.RealReturnType;
import com.lanxi.couponcode.spi.consts.enums.ShopStatus;

import java.io.File;

/**
 * Created by yangyuanjian on 2017/11/9.
 */
public interface ShopService {
    Boolean addShop(String shopName,
                    String shopAddress,
                    String serviceTel,
                    @HiddenArg Long merchantId,
                    @HiddenArg Long operaterId);

    Boolean importShops(File file,
                        @HiddenArg Long merchantId,
                        @HiddenArg Long operaterId);

    Boolean freezeShop(@HiddenArg Long shopId,
                       @HiddenArg Long operaterId);

    Boolean unfreezeShop(@HiddenArg Long shopId,
                         @HiddenArg Long operaterId);

    @RealReturnType("List<Shop>")
    String queryShop(String merchantName,
                     String shopName,
                     ShopStatus status,
                     String shopAddress,
                     @HiddenArg Integer pageNum,
                     @HiddenArg Integer pageSize,
                     @HiddenArg Long merchantId,
                     @HiddenArg Long operaterId);

    File queryShop(String merchantName,
                     String shopName,
                     ShopStatus status,
                     String shopAddress,
                     @HiddenArg Long merchantId,
                     @HiddenArg Long operaterId);



}
