package com.lanxi.couponcode.impl.newservice;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.ClearDailyRecord;
import com.lanxi.couponcode.impl.entity.ClearRecord;
import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.couponcode.spi.consts.enums.ClearStatus;
import com.lanxi.util.utils.LoggerUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yangyuanjian on 2017/11/22.
 */
@Service ("clearService")
@EasyLog (LoggerUtil.LogLevel.INFO)
public class ClearServiceImpl implements ClearService {

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
    public List<ClearDailyRecord> queryDailyRecords(EntityWrapper<ClearDailyRecord> wrapper, Page<ClearDailyRecord> page) {
        if (page == null)
            return daoService.getClearDailyRecordDao().selectList(wrapper);
        else
            return daoService.getClearDailyRecordDao().selectPage(page, wrapper);
    }

    @Override
    public List<ClearDailyRecord> queryDailyRecords(Long[] recordIds) {
        return daoService.getClearDailyRecordDao().selectBatchIds(Arrays.asList(recordIds));
    }

    @Override
    public List<ClearRecord> queryClearRecords(EntityWrapper<ClearRecord> wrapper, Page<ClearRecord> page) {
        if (page == null)
            return daoService.getClearRecordDao().selectList(wrapper);
        else
            return daoService.getClearRecordDao().selectPage(page, wrapper);
    }

    @Override
    public Boolean clearDailyRecord(ClearDailyRecord record) {
        record.setClearStatus(ClearStatus.cleard);
        return record.updateById();
    }
}
