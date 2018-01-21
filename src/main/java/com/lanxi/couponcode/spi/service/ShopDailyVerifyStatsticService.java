package com.lanxi.couponcode.spi.service;

import com.lanxi.couponcode.spi.assist.RetMessage;

import java.io.File;

public interface ShopDailyVerifyStatsticService{
    RetMessage<String> queryStatsticsInfo(Long recordId,Long operaterId);
    RetMessage<String> queryStatsticsList(String shopName,Long shopId,String timeStart,String timeStop,Integer pageNum,Integer pageSize,Long operaterId);
    RetMessage<File> exportStatsticsList(String shopName, Long shopId, String timeStart, String timeStop, Long operaterId);
}
