package com.lanxi.couponcode.spi.service;

/**
 * 定时任务
 * Created by yangyuanjian on 2017/11/23.
 */
public interface QuartzService {
    void codeOverTime();
    void addClearDailyRecord();
    void addClearRecords();
}
