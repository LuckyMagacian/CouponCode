package com.lanxi.couponcode.impl.newservice;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.VerificationRecord;
import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.couponcode.spi.service.RedisService;
import com.lanxi.util.utils.LoggerUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by yangyuanjian on 2017/11/16.
 */
@Service ("verificationRecordService")
@EasyLog (LoggerUtil.LogLevel.INFO)
public class VerificationRecordServiceImpl implements VerificationRecordService {
    @Resource
    private DaoService           daoService;
    @Resource(name="redisService")
    private RedisService         redisService;
    @Resource
    private RedisEnhancedService redisEnhancedService;

    @Override
    public Boolean addVerificationRecord(VerificationRecord record) {
        return record.insert();
    }

    @Override
    public VerificationRecord queryVerificationRecordInfo(Long recordId) {
        VerificationRecord record = daoService.getVerificationRecordDao().selectById(recordId);
        return record;
    }

    @Override
    public List<VerificationRecord> queryVerificationRecords(Wrapper<VerificationRecord> wrapper, Page<VerificationRecord> page) {
        if (page == null)
            return daoService.getVerificationRecordDao().selectList(wrapper);
        else
            return daoService.getVerificationRecordDao().selectPage(page, wrapper);
    }
}
