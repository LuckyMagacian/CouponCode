package com.lanxi.couponcode.impl.newcontroller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.Account;
import com.lanxi.couponcode.impl.entity.ClearDailyRecord;
import com.lanxi.couponcode.impl.entity.ClearRecord;
import com.lanxi.couponcode.impl.newservice.*;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.enums.ClearStatus;
import com.lanxi.couponcode.spi.consts.enums.InvoiceStatus;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;

import static com.lanxi.couponcode.impl.assist.CheckAssist.*;
import static com.lanxi.couponcode.impl.assist.TimeAssist.*;
import javax.annotation.Resource;
import javax.sound.midi.VoiceStatus;
import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by yangyuanjian on 2017/11/23.
 */
public class ClearController implements com.lanxi.couponcode.spi.service.ClearService{
    @Resource
    private RedisService redisService;
    @Resource
    private RedisEnhancedService redisEnhancedService;
    @Resource
    private ClearService clearService;
    @Resource
    private MerchantService merchantService;
    @Resource
    private CommodityService commodityService;
    @Resource
    private AccountService accountService;


    @Override
    public RetMessage<String> queryDailyRecords(String merchantName,
                                                String timeStart,
                                                String timeStop,
                                                ClearStatus clearStatus,
                                                Integer pageNum,
                                                Integer pageSize,
                                                Long operaterId) {
        RetMessage<String> message=new RetMessage<>();
        //TODO 查询
        Account account=null;

        EntityWrapper<ClearDailyRecord> wrapper=new EntityWrapper<>();
        notNullAndEmpty(merchantName).ifPresent(e->wrapper.like("merchant_name",e));
        notNullAndEmpty(timeStart).ifPresent(e->wrapper.ge("record_time",timeFixZero(timeStart)));
        notNullAndEmpty(timeStop).ifPresent(e->wrapper.le("record_time",timeFixNine(timeStop)));
        notNUll(clearStatus).ifPresent(e->wrapper.eq("clear_status",clearStatus));

        Page<ClearDailyRecord> page=new Page<>(pageNum,pageSize);
        List<ClearDailyRecord> list=clearService.queryDailyRecords(wrapper,page);
        Map<String,Object> map=new HashMap<>();
        map.put("page",page);
        map.put("detail",list);
        message.setAll(RetCodeEnum.success,"查询成功!", nullOrJson(map));
        return message;
    }

    @Override
    public RetMessage<String> queryDailyRecords(String timeStart,
                                                String timeStop,
                                                ClearStatus clearStatus,
                                                Integer pageNum,
                                                Integer pageSize,
                                                Long operaterId) {
        RetMessage<String> message=new RetMessage<>();
        //TODO 查询
        Account account=null;
        EntityWrapper<ClearDailyRecord> wrapper=new EntityWrapper<>();
        wrapper.eq("merchant_id",account.getMerchantId());
        notNullAndEmpty(timeStart).ifPresent(e->wrapper.ge("record_time",timeFixZero(timeStart)));
        notNullAndEmpty(timeStop).ifPresent(e->wrapper.le("record_time",timeFixNine(timeStop)));
        notNUll(clearStatus).ifPresent(e->wrapper.eq("clear_status",clearStatus));
        Page<ClearDailyRecord> page=new Page<>(pageNum,pageSize);
        List<ClearDailyRecord> list=clearService.queryDailyRecords(wrapper,page);
        Map<String,Object> map=new HashMap<>();
        map.put("page",page);
        map.put("detail",list);
        message.setAll(RetCodeEnum.success,"查询成功!", nullOrJson(map));
        return message;
    }

    @Override
    public RetMessage<String> queryDailyRecordInfo(Long recordId,
                                                   Long operaterId) {
        RetMessage<String> message=new RetMessage<>();
        //TODO 查询
        Account account=null;
        ClearDailyRecord record=clearService.queryDailyRecordInfo(recordId);
        message.setAll(RetCodeEnum.success,"查询成功!", nullOrJson(record));
        return message;
    }

    @Override
    public RetMessage<String> queryClearRecords(String merchantName,
                                                String timeStart,
                                                String timeStop,
                                                ClearStatus clearStatus,
                                                InvoiceStatus invoiceStatus,
                                                Integer pageNum,
                                                Integer pageSize,
                                                Long operaterId) {
        RetMessage<String> message=new RetMessage<>();
        //TODO 查询
        Account account=null;

        EntityWrapper<ClearRecord> wrapper=new EntityWrapper<>();
        notNullAndEmpty(merchantName).ifPresent(e->wrapper.like("merchant_name",e));
        notNullAndEmpty(timeStart).ifPresent(e->wrapper.ge("record_time",timeFixZero(timeStart)));
        notNullAndEmpty(timeStop).ifPresent(e->wrapper.le("record_time",timeFixNine(timeStop)));
        notNUll(clearStatus).ifPresent(e->wrapper.eq("clear_status",clearStatus));
        notNUll(invoiceStatus).ifPresent(e->wrapper.eq("invoice_status",invoiceStatus));
        Page<ClearRecord> page=new Page<>(pageNum,pageSize);
        List<ClearRecord> list=clearService.queryClearRecords(wrapper,page);
        Map<String,Object> map=new HashMap<>();
        map.put("page",page);
        map.put("detail",list);
        message.setAll(RetCodeEnum.success,"查询成功!", nullOrJson(map));
        return message;



    }

    @Override
    public RetMessage<File> exportClearRecords(String merchantName,
                                               String timeStart,
                                               String timeStop,
                                               ClearStatus clearStatus,
                                               InvoiceStatus invoiceStatus,
                                               Long operaterId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RetMessage<String> queryClearRecords(String timeStart,
                                                String timeStop,
                                                ClearStatus clearStatus,
                                                InvoiceStatus invoiceStatus,
                                                Integer pageNum,
                                                Integer pageSize,
                                                Long operaterId) {
        RetMessage<String> message=new RetMessage<>();
        //TODO 查询
        Account account=null;

        EntityWrapper<ClearRecord> wrapper=new EntityWrapper<>();
        wrapper.eq("merchant_id",account.getMerchantId());
        notNullAndEmpty(timeStart).ifPresent(e->wrapper.ge("record_time",timeFixZero(timeStart)));
        notNullAndEmpty(timeStop).ifPresent(e->wrapper.le("record_time",timeFixNine(timeStop)));
        notNUll(clearStatus).ifPresent(e->wrapper.eq("clear_status",clearStatus));
        notNUll(invoiceStatus).ifPresent(e->wrapper.eq("invoice_status",invoiceStatus));
        Page<ClearRecord> page=new Page<>(pageNum,pageSize);
        List<ClearRecord> list=clearService.queryClearRecords(wrapper,page);
        Map<String,Object> map=new HashMap<>();
        map.put("page",page);
        map.put("detail",list);
        message.setAll(RetCodeEnum.success,"查询成功!", nullOrJson(map));
        return message;
    }

    @Override
    public RetMessage<File> exportClearRecords(String timeStart,
                                               String timeStop,
                                               ClearStatus clearStatus,
                                               InvoiceStatus invoiceStatus,
                                               Long operaterId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RetMessage<String> queryRecordInfo(Long recordId,
                                              Long operaterId) {
        RetMessage<String> message=new RetMessage<>();
        //TODO 查询
        Account account=null;
        ClearRecord record=clearService.queryClearRecordInfo(recordId);
        message.setAll(RetCodeEnum.success,"查询成功!", nullOrJson(record));
        return message;
    }

    @Override
    public RetMessage<String> clear(Long[] dailyRecordIds,
                                    Long operaterId) {
        RetMessage<String> message=new RetMessage<>();
        //TODO 查询
        Account account=null;
        List<ClearDailyRecord> list=clearService.queryDailyRecords(dailyRecordIds);
        message.setAll(RetCodeEnum.success,"查询成功!", nullOrJson(list));
        return message;
    }
}
