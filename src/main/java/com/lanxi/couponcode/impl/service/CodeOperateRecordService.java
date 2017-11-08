package com.lanxi.couponcode.impl.service;

import com.lanxi.couponcode.impl.entity.OperateRecord;

import java.util.List;

/**
 * 串码操作记录服务接口<br>
 * Created by yangyuanjian on 2017/11/3.<br>
 */
public interface CodeOperateRecordService {
    public List<OperateRecord> queryCodeOperateRecord(Integer page, Integer size, String startTime, String endTime,
                                                      Long merchantId, Long commodityId, String shopName, String operaterPhone,
                                                      Long code, String commodityName, String merchantName, String operaterId,
                                                      Long recordId, boolean isManager);
}
