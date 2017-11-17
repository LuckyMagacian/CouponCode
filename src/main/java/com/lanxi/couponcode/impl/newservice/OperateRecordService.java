package com.lanxi.couponcode.impl.newservice;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.OperateRecord;

import java.util.List;

/**
 * Created by yangyuanjian on 2017/11/16.
 */
public interface OperateRecordService {
    Boolean addRecord(OperateRecord record);
    OperateRecord queryRecordInfo(Long recordId);
    List<OperateRecord> queryRecords(Wrapper<OperateRecord> wrapper, Page<OperateRecord> page);
}
