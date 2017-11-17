package com.lanxi.couponcode.impl.newservice;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.OperateRecord;
import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.util.utils.LoggerUtil;
import com.sun.org.apache.regexp.internal.RE;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by yangyuanjian on 2017/11/16.
 */
@EasyLog(LoggerUtil.LogLevel.INFO)
public class OperateRecordServiceImpl implements OperateRecordService{
    @Resource
    private DaoService daoService;
    @Resource
    private RedisService redisService;
    @Resource
    private RedisEnhancedService redisEnhancedService;

    @Override
    public Boolean addRecord(OperateRecord record) {
        return record.insert();
    }

    @Override
    public OperateRecord queryRecordInfo(Long recordId) {
        OperateRecord record= daoService.getOperateRecordDao().selectById(recordId);
        return record;
    }

    @Override
    public List<OperateRecord> queryRecords(Wrapper<OperateRecord> wrapper, Page<OperateRecord> page) {
        List<OperateRecord> list;
        if(page==null)
            list = daoService.getOperateRecordDao().selectList(wrapper);
        else
            list =daoService.getOperateRecordDao().selectPage(page,wrapper);
        return list;
    }
}
