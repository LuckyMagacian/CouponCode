package com.lanxi.couponcode.impl.newservice;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.ClearDailyRecord;
import com.lanxi.couponcode.impl.entity.ClearRecord;
import com.lanxi.couponcode.impl.entity.CouponCode;
import com.lanxi.couponcode.spi.consts.enums.ClearStatus;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yangyuanjian on 2017/11/22.
 */
public class ClearServiceImpl implements ClearService{

    @Resource
    private DaoService daoService;


    @Override
    public Boolean addClearDailyRecord(ClearDailyRecord record) {
        return record.insert();
    }

    @Override
    public Boolean addClearRecord(ClearRecord record) {
        return record.insert();
    }

    @Override
    public ClearDailyRecord queryDailyRecordInfo(Long recordId) {
        return daoService.getClearDailyRecordDao().selectById(recordId);
    }

    @Override
    public ClearRecord queryClearRecordInfo(Long recordId) {
        return daoService.getClearRecordDao().selectById(recordId);
    }

    @Override
    public List<ClearDailyRecord> queryDailyRecords(Wrapper<ClearDailyRecord> wrapper, Page<ClearDailyRecord> page) {
        if(page==null)
            return daoService.getClearDailyRecordDao().selectList(wrapper);
        else
            return daoService.getClearDailyRecordDao().selectPage(page,wrapper);
    }

    @Override
    public List<ClearDailyRecord> queryDailyRecords(Long[] recordIds) {
        return daoService.getClearDailyRecordDao().selectBatchIds(Arrays.asList(recordIds));
    }

    @Override
    public List<ClearRecord> queryClearRecords(Wrapper<ClearRecord> wrapper, Page<ClearRecord> page) {
        if(page==null)
            return daoService.getClearRecordDao().selectList(wrapper);
        else
            return daoService.getClearRecordDao().selectPage(page,wrapper);
    }

    @Override
    public Boolean clearDailyRecord(ClearDailyRecord record) {
        record.setClearStatus(ClearStatus.cleard);
        return record.updateById();
    }
}
