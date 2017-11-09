package com.lanxi.couponcode.spi.service;

import com.lanxi.couponcode.spi.consts.annotations.HiddenArg;
import com.lanxi.couponcode.spi.consts.annotations.RealReturnType;

import java.io.File;

/**
 * Created by yangyuanjian on 2017/11/9.
 */
public interface StatsticService {
    @RealReturnType("Map<String,Object>")
    String verificationStatstics(String merchantName,
                                 String timeStart,
                                 String timeStop,
                                 @HiddenArg Long operaterId);

    File verificationStatsticsExport(String merchantName,
                                     String timeStart,
                                     String timeStop,
                                     @HiddenArg Long operaterId);

}
