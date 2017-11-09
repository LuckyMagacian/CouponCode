package com.lanxi.couponcode.spi.service;

import com.lanxi.couponcode.spi.consts.annotations.HiddenArg;
import com.lanxi.couponcode.spi.consts.annotations.RealReturnType;
import com.lanxi.couponcode.spi.consts.enums.VerificationType;

/**
 * Created by yangyuanjian on 2017/11/9.
 */
public interface VerificationRecordService {
    @RealReturnType("List<VerificationRecord>")
    String queryVerificationRecords(Long code,
                                    String timeStart,
                                    String timeStop,
                                    String shopName,
                                    String workerPhone,
                                    VerificationType type,
                                    String commodityName,
                                    @HiddenArg Integer pageNum,
                                    @HiddenArg Integer pageSize,
                                    @HiddenArg Long operaterId);

    /**需要包含统计信息*/
    @RealReturnType("Map<String,Object>")
    String queryShopVerificationRecords(String timeStart,
                                        String timeStop,
                                        String shopName,
                                        @HiddenArg Long shopId,
                                        @HiddenArg Long operaterId);

}
