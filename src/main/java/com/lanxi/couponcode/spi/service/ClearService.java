package com.lanxi.couponcode.spi.service;

import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.annotations.HiddenArg;
import com.lanxi.couponcode.spi.consts.enums.ClearStatus;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by yangyuanjian on 2017/11/9.
 */
public interface ClearService {

    RetMessage<File> merchantClearExport(String timeStart,
                                         String timeStop,
                                         ClearStatus status,
                                         @HiddenArg Long operaterId);
    RetMessage<String> clearQuery(String timeStart,
                                  String timeEnd,
                                  String merchantName,
                                  ClearStatus status,
                                  Long operaterId);

    RetMessage<String> clearQuery(String timeStart,
                                  String timeEnd,
                                  ClearStatus status,
                                  Long merchantId,
                                  Long operaterId);

    RetMessage<String> clear(String clearTime,
                             BigDecimal clearCost,
                             Long[] recordIds,
                             Long operaterId);

    RetMessage<String> clear(String timeStart,
                             String timeEnd,
                             Long merchantId,
                             Long operaterId);

    RetMessage<String> clear(String timeStart,
                             String timeEnd,
                             Long[] merchantIds,
                             Long operaterId);

}
