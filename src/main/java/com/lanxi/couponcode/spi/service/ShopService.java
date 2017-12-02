package com.lanxi.couponcode.spi.service;

import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.annotations.HiddenArg;
import com.lanxi.couponcode.spi.consts.annotations.RealReturnType;
import com.lanxi.couponcode.spi.consts.enums.ShopStatus;

import java.io.File;

/**
 * Created by yangyuanjian on 2017/11/9.
 */
public interface ShopService {
    RetMessage<Boolean> addShop(String shopName,
                                String shopAddress,
                                String minuteShopAddress,
                                String serviceTel,
                                @HiddenArg Long merchantId,
                                @HiddenArg Long operaterId);

    RetMessage<Boolean> importShops(File file,
                        @HiddenArg Long merchantId,
                        @HiddenArg Long operaterId);

    RetMessage<Boolean> freezeShop(@HiddenArg Long shopId,
                       @HiddenArg Long operaterId);

    RetMessage<Boolean> unfreezeShop(@HiddenArg Long shopId,
                         @HiddenArg Long operaterId);

    @RealReturnType("List<Shop>")
    RetMessage<String> queryShop(String merchantName,
                     String shopName,
                     ShopStatus status,
                     String shopAddress,
                     @HiddenArg Integer pageNum,
                     @HiddenArg Integer pageSize,
                     @HiddenArg Long operaterId);

    RetMessage<String> queryShop(String merchantName,
                     String shopName,
                     ShopStatus status,
                     String shopAddress,
                     @HiddenArg Long merchantId,
                     @HiddenArg Long operaterId);

    RetMessage<Boolean> modifyShop(String shopName,
    					String shopAddress,
    					String minuteShopAddress,
    					String serviceTel,
    					@HiddenArg Long shopId,
    					@HiddenArg Long operaterId);
  
    RetMessage<File> queryShopsExport(String shopName,
    				String shopAddress,
    				ShopStatus status,
    				@HiddenArg Integer pageNum,
    				@HiddenArg Integer pageSize,
    				@HiddenArg Long merchantId,
    				@HiddenArg Long operaterId);
    RetMessage<File> downloadExcelTemplate(@HiddenArg Long operaterId);
    RetMessage<String> queryShops(@HiddenArg Long merchantId,
    		@HiddenArg Long operaterId,
			@HiddenArg Integer pageNum,
			@HiddenArg Integer pageSize);
}
