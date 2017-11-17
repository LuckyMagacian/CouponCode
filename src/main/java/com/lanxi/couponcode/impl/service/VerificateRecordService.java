package com.lanxi.couponcode.impl.service;

import com.lanxi.couponcode.impl.entity.VerificationRecord;

import java.util.List;

/**
 * 串码核销记录服务接口<br>
 * Created by yangyuanjian on 2017/11/3.<br>
 */
@Deprecated
public interface VerificateRecordService {
//    /**根据串码查询核销记录,允许串码模糊*/
//    List<VerificationRecord> queryVerificateRecord(Integer page, Integer size, Long merchantId, Long code);
//    /**管理员模糊查询串码核销情况*/
public List<VerificationRecord> queryVerificateRecords(Integer page,    Integer size,           String startTime,   String endTime,
                                                       Long merchantId, Long commodityId,       String shopName,    String operaterPhone,
                                                       Long code,       String commodityName,   String merchantName,String operaterId,
                                                       Long recordId,boolean isManager);
}
