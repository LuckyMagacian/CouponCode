package com.lanxi.couponcode.spi.service;

import com.lanxi.couponcode.spi.consts.annotations.HiddenArg;
import com.lanxi.couponcode.spi.consts.enums.ClearStatus;

import java.io.File;

/**
 * Created by yangyuanjian on 2017/11/9.
 */
public interface ClearService {

    File merchantClearExport(String timeStart,
                             String timeStop,
                             ClearStatus status,
                             @HiddenArg Long operaterId);


}
