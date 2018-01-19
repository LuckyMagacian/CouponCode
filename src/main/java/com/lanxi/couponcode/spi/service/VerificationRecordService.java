package com.lanxi.couponcode.spi.service;

import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.annotations.HiddenArg;
import com.lanxi.couponcode.spi.consts.annotations.RealReturnType;
import com.lanxi.couponcode.spi.consts.enums.VerificationType;

import java.io.File;

/**
 * Created by yangyuanjian on 2017/11/9.
 */
public interface VerificationRecordService {
    @RealReturnType ("List<VerificationRecord>")
    RetMessage<String> queryVerificationRecords(Long code,
                                                String timeStart,
                                                String timeStop,
                                                String merchantName,
                                                String shopName,
                                                String commodityName,
                                                String phone,
                                                VerificationType type,
                                                @HiddenArg Integer pageNum,
                                                @HiddenArg Integer pageSize,
                                                @HiddenArg Long operaterId);

    /**
     * 需要包含统计信息
     */
    @RealReturnType ("Map<String,Object>")
    RetMessage<String> queryShopVerificationRecords(String timeStart,
                                                    String timeStop,
                                                    String shopName,
                                                    Long code,
                                                    String commodityName,
                                                    String phone,
                                                    VerificationType type,
                                                    @HiddenArg Integer pageNum,
                                                    @HiddenArg Integer pageSize,
                                                    @HiddenArg Long operaterId);

    RetMessage<String> queryVerificationRecordInfo(Long recordId, Long operaterId);

    RetMessage<File> exportVerificationRecords(Long code,
                                               String timeStart,
                                               String timeStop,
                                               String merchantName,
                                               String shopName,
                                               String commodityName,
                                               String phone,
                                               VerificationType type,
                                               @HiddenArg Long operaterId);

    RetMessage<File> exportShopVerificationRecords(String timeStart,
                                                   String timeStop,
                                                   String shopName,
                                                   Long code,
                                                   String commodityName,
                                                   String phone,
                                                   VerificationType type,
                                                   @HiddenArg Long operaterId);

    RetMessage<String> queryVerifyRecordsAndStatstis(Long accountId,String operateTime, Long operaterId);

    RetMessage<String> statisticVerifyRecord(String shopName,String timeStart,String timeStop,Integer pageNum,Integer pageSize,Long merchantId,Long operaterId);
    RetMessage<File> exportStatisticVerifyRecord(String shopName,String timeStart,String timeStop,Long merchantId,Long operaterId);
}
