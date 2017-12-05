package com.lanxi.couponcode.impl.newcontroller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.lanxi.couponcode.spi.assist.TimeAssist;
import com.lanxi.couponcode.impl.entity.*;
import com.lanxi.couponcode.impl.newservice.*;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.enums.*;
import com.lanxi.util.entity.LogFactory;
import org.springframework.stereotype.Controller;

import static com.lanxi.couponcode.spi.assist.CheckAssist.*;
import static com.lanxi.couponcode.spi.assist.TimeAssist.*;
import static com.lanxi.couponcode.impl.assist.PredicateAssist.*;
import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by yangyuanjian on 2017/11/23.
 */
@Controller("clearControllerService")
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
    @Resource
    private LockService lockService;
    @Resource
    private OperateRecordService operateRecordService;
    @Override
    public RetMessage<String> queryDailyRecords(String merchantName,
                                                String timeStart,
                                                String timeStop,
                                                ClearStatus clearStatus,
                                                Integer pageNum,
                                                Integer pageSize,
                                                Long operaterId) {
        Account account=accountService.queryAccountById(operaterId);
        RetMessage<String> message=checkAccount.apply(account, OperateType.queryDailyRecord);
        if(message!=null)
            return message;
        EntityWrapper<ClearDailyRecord> wrapper=new EntityWrapper<>();
        notNullAndEmpty(merchantName).ifPresent(e->wrapper.like("merchant_name",e));
        notNullAndEmpty(timeStart).ifPresent(e->wrapper.ge("record_time",timeFixZero(timeStart)));
        notNullAndEmpty(timeStop).ifPresent(e->wrapper.le("record_time",timeFixNine(timeStop)));
        notNUll(clearStatus).ifPresent(e->wrapper.eq("clear_status",clearStatus));

        Page<ClearDailyRecord> page=new Page<>(pageNum,pageSize);
        List<ClearDailyRecord> list=clearService.queryDailyRecords(wrapper,page);
        page.setRecords(null);
        Map<String,Object> map=new HashMap<>();
        map.put("page",page);
        map.put("detail",list);
        return new RetMessage<>(RetCodeEnum.success,"查询成功!",nullOrJson(map));
    }

    @Override
    public RetMessage<String> queryDailyRecords(String timeStart,
                                                String timeStop,
                                                ClearStatus clearStatus,
                                                Integer pageNum,
                                                Integer pageSize,
                                                Long operaterId) {
        Account account=accountService.queryAccountById(operaterId);
        RetMessage<String> message=checkAccount.apply(account,OperateType.queryDailyRecord);
        if(message!=null) {
            return message;
        }
        EntityWrapper<ClearDailyRecord> wrapper=new EntityWrapper<>();
        wrapper.eq("merchant_id",account.getMerchantId());
        notNullAndEmpty(timeStart).ifPresent(e->wrapper.ge("record_time",timeFixZero(timeStart)));
        notNullAndEmpty(timeStop).ifPresent(e->wrapper.le("record_time",timeFixNine(timeStop)));
        notNUll(clearStatus).ifPresent(e->wrapper.eq("clear_status",clearStatus));
        Page<ClearDailyRecord> page=new Page<>(pageNum,pageSize);
        List<ClearDailyRecord> list=clearService.queryDailyRecords(wrapper,page);
        Map<String,Object> map=new HashMap<>();
        page.setRecords(null);
        map.put("page",page);
        map.put("detail",list);
        return new RetMessage<>(RetCodeEnum.success,"查询成功!", nullOrJson(map));
    }

    @Override
    public RetMessage<String> queryDailyRecordInfo(Long recordId,
                                                   Long operaterId) {
        Account account=accountService.queryAccountById(operaterId);
        RetMessage<String> message=checkAccount.apply(account,OperateType.queryDailyRecord);
        if(message!=null) {
            return message;
        }
        ClearDailyRecord record=clearService.queryDailyRecordInfo(recordId);
        return new RetMessage<>(RetCodeEnum.success,"查询成功!", nullOrJson(record));
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
        Account account=accountService.queryAccountById(operaterId);
        RetMessage<String> message=checkAccount.apply(account,OperateType.queryClearRecord);
        if(message!=null) {
            return message;
        }
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
        return new RetMessage<>(RetCodeEnum.success,"查询成功!", nullOrJson(map));



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
        Account account=accountService.queryAccountById(operaterId);
        RetMessage<String> message=checkAccount.apply(account,OperateType.queryClearRecord);
        if(message!=null) {
            return message;
        }

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
        message=new RetMessage<>();
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
        Account account=accountService.queryAccountById(operaterId);
        RetMessage<String> message=checkAccount.apply(account,OperateType.queryClearRecord);
        if(message!=null) {
            return message;
        }
        ClearRecord record=clearService.queryClearRecordInfo(recordId);
        message=new RetMessage<>();
        message.setAll(RetCodeEnum.success,"查询成功!", nullOrJson(record));
        return message;
    }

    @Override
    public RetMessage<String> clear(Long[] dailyRecordIds,
                                    Long operaterId) {
        //校验权限
        Account account=accountService.queryAccountById(operaterId);
        RetMessage<String> message=checkAccount.apply(account,OperateType.createClearRecord);
        if(message!=null) {
            return message;
        }
        //查询list并排除重复的
        List<ClearDailyRecord> list=clearService.queryDailyRecords(dailyRecordIds).stream().distinct().collect(Collectors.toList());
        try {
            //redis加锁
            List<Boolean> lock=lockService.lock(list);
            if(lock.parallelStream().filter(e->e==null||e==false).findAny().isPresent()){
                return new RetMessage<>(RetCodeEnum.fail,"部分日清算记录可能正在被处理!",null);
            }
            //更新数据库
            lock=list.stream().map(clearService::clearDailyRecord).collect(Collectors.toList());
            //映射处理结果
            Map<Long,Boolean> clearResult=new HashMap<>();
            for(int i=0;i<dailyRecordIds.length;i++)
                clearResult.put(dailyRecordIds[i],lock.get(i));
            //根据商户号分组
            Map<Long,List<ClearDailyRecord>> merchantDailyRecord=list.stream().collect(Collectors.groupingBy(e->e.getMerchantId()));
            //分组处理,插入数据库
            merchantDailyRecord.entrySet().forEach(e->{
                Long merchantId=e.getKey();
                Merchant merchant=merchantService.queryMerchantParticularsById(merchantId);
                List<ClearDailyRecord> records=e.getValue();
                BigDecimal showTotal=records.stream().map(f->f.getVerificateCost()).reduce(new BigDecimal(0),(a,b)->a.add(b));
                ClearRecord record=new ClearRecord();
                record.setRecordId(IdWorker.getId());
                record.setMerchantId(merchantId);
                record.setMerchantName(merchant.getMerchantName());
                record.setDailyRecordIds(records.stream().map(f->f.getRecordId()).collect(Collectors.toList()));
                record.setOperaterId(operaterId);
                record.setOperaterName(account.getUserName());
                record.setCreateTime(TimeAssist.getNow());
                record.insert();
            });
//            OperateRecord operateRecord=new OperateRecord();
            OperateRecord record=new OperateRecord();
            record.setRecordId(IdWorker.getId());
            record.setOperaterId(account.getAccountId());
            record.setAccountType(account.getAccountType());
            record.setPhone(account.getPhone());
            record.setName(account.getUserName());
            record.setTargetType(OperateTargetType.clearRecord);
            record.setType(OperateType.createClearRecord);
            record.setOperateTime(TimeAssist.getNow());
            record.setOperateResult("success");
            record.setDescription("结算["+ Arrays.asList(dailyRecordIds)+"]");
            operateRecordService.addRecord(record);

            if(lock.parallelStream().filter(e->e==null||e==false).findAny().isPresent()){
                Map map=new HashMap();
                for(int i=0;i<dailyRecordIds.length;i++)
                    map.put(dailyRecordIds[i],lock);
                return new RetMessage<>(RetCodeEnum.success,"清算部分成功!",JSON.toJSONString(map));
            }
            return new RetMessage<>(RetCodeEnum.success,"清算成功!",null);
        } catch (Exception e) {
            LogFactory.info(this,"清算异常!",e);
            return new RetMessage<>(RetCodeEnum.error,"发生异常!",null);
        }finally {
            lockService.unlock(list);
        }
    }
}
