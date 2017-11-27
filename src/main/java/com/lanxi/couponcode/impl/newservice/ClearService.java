package com.lanxi.couponcode.impl.newservice;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.ClearDailyRecord;
import com.lanxi.couponcode.impl.entity.ClearRecord;

import java.util.List;

/**
 * Created by yangyuanjian on 2017/11/22.
 */
public interface ClearService {
    Boolean addClearDailyRecord(ClearDailyRecord record);
    Boolean addClearRecord(ClearRecord record);

    ClearDailyRecord queryDailyRecordInfo(Long recordId);
    ClearRecord queryClearRecordInfo(Long recordId);

    List<ClearDailyRecord> queryDailyRecords(Wrapper<ClearDailyRecord> wrapper, Page<ClearDailyRecord> page);
    List<ClearDailyRecord> queryDailyRecords(Long[] recordIds);
    List<ClearRecord> queryClearRecords(Wrapper<ClearRecord> wrapper,Page<ClearRecord> page);

}
