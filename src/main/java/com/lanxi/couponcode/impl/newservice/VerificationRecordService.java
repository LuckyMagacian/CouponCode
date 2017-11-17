package com.lanxi.couponcode.impl.newservice;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.VerificationRecord;

import java.util.List;

/**
 * Created by yangyuanjian on 2017/11/16.
 */
public interface VerificationRecordService {

    Boolean addVerificationRecord(VerificationRecord record);

    VerificationRecord queryVerificationRecordInfo(Long recordId);

    List<VerificationRecord> queryVerificationRecords(Wrapper<VerificationRecord> wrapper, Page<VerificationRecord> page);
}
