package com.lanxi.couponcode.impl.newcontroller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.lanxi.couponcode.impl.assist.ExcelAssist;
import com.lanxi.couponcode.impl.entity.*;
import com.lanxi.couponcode.impl.newservice.*;
import com.lanxi.couponcode.impl.assist.FillAssist;
import com.lanxi.couponcode.spi.assist.FileDelete;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.assist.TimeAssist;
import com.lanxi.couponcode.impl.config.HiddenMap;
import com.lanxi.couponcode.spi.consts.enums.*;
import com.lanxi.couponcode.spi.defaultInterfaces.ToJson;
import com.lanxi.couponcode.spi.service.RedisService;
import com.lanxi.util.entity.LogFactory;
import com.lanxi.util.utils.ExcelUtil;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import static com.lanxi.couponcode.impl.assist.PredicateAssist.*;
import static com.lanxi.couponcode.impl.assist.PredicateAssist.notNull;
import static com.lanxi.couponcode.spi.assist.CheckAssist.*;
import static com.lanxi.couponcode.spi.assist.TimeAssist.timeFixNine;
import static com.lanxi.couponcode.spi.assist.TimeAssist.timeFixZero;

//import com.lanxi.couponcode.impl.assist.PredicateAssist;

/**
 * Created by yangyuanjian on 2017/11/23.
 */
@Controller ("clearControllerService")
public class ClearController implements com.lanxi.couponcode.spi.service.ClearService {

    @Resource
    private RedisService         redisService;
    @Resource
    private RedisEnhancedService redisEnhancedService;
    @Resource
    private ClearService         clearService;
    @Resource
    private MerchantService      merchantService;
    @Resource
    private CommodityService     commodityService;
    @Resource
    private AccountService       accountService;
    @Resource
    private LockService          lockService;
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
        Account account = accountService.queryAccountById(operaterId);
        RetMessage<String> message = checkAccount.apply(account, OperateType.queryDailyRecord);
        if (message != null)
            return message;
        EntityWrapper<ClearDailyRecord> wrapper = new EntityWrapper<>();
        wrapper.orderBy("record_time",false);
        notNullAndEmpty(merchantName).ifPresent(e -> wrapper.like("merchant_name", e));
        notNullAndEmpty(timeStart).ifPresent(e -> wrapper.ge("record_time", timeFixZero(timeStart)));
        notNullAndEmpty(timeStop).ifPresent(e -> wrapper.le("record_time", timeFixNine(timeStop)));
        notNUll(clearStatus).ifPresent(e -> wrapper.eq("clear_status", clearStatus.getValue()));

        Page<ClearDailyRecord> page = new Page<>(pageNum, pageSize);
        List<ClearDailyRecord> list = clearService.queryDailyRecords(wrapper, page);
        FillAssist.returnDeal.accept(HiddenMap.ADMIN_DAILY_CLEAR,list);
        page.setRecords(null);
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("list", list);
        return new RetMessage<>(RetCodeEnum.success, "查询成功!", nullOrJson(map));
    }

    @Override
    public RetMessage<String> queryDailyRecords(String timeStart,
                                                String timeStop,
                                                ClearStatus clearStatus,
                                                Integer pageNum,
                                                Integer pageSize,
                                                Long operaterId) {
        Account account = accountService.queryAccountById(operaterId);
        RetMessage<String> message = checkAccount.apply(account, OperateType.queryDailyRecord);
        if (message != null) {
            return message;
        }
        EntityWrapper<ClearDailyRecord> wrapper = new EntityWrapper<>();
        wrapper.eq("merchant_id", account.getMerchantId());
        wrapper.orderBy("record_time",false);
        notNullAndEmpty(timeStart).ifPresent(e -> wrapper.ge("record_time", timeFixZero(timeStart)));
        notNullAndEmpty(timeStop).ifPresent(e -> wrapper.le("record_time", timeFixNine(timeStop)));
        notNUll(clearStatus).ifPresent(e -> wrapper.eq("clear_status", clearStatus.getValue()));
        Page<ClearDailyRecord> page = new Page<>(pageNum, pageSize);
        List<ClearDailyRecord> list = clearService.queryDailyRecords(wrapper, page);
        FillAssist.returnDeal.accept(FillAssist.getMap.apply(AccountType.merchantManager,ClearDailyRecord.class), list);
        Map<String, Object> map = new HashMap<>();
        page.setRecords(null);
        map.put("page", page);
        map.put("list", list);
        return new RetMessage<>(RetCodeEnum.success, "查询成功!", nullOrJson(map));
    }

    @Override
    public RetMessage<String> queryDailyRecordInfo(Long recordId,
                                                   Long operaterId) {
        Account account = accountService.queryAccountById(operaterId);
        RetMessage<String> message = checkAccount.apply(account, OperateType.queryDailyRecord);
        if (message != null) {
            return message;
        }
        ClearDailyRecord record = clearService.queryDailyRecordInfo(recordId);
        FillAssist.returnDeal.accept(FillAssist.getMap.apply(AccountType.merchantManager,ClearDailyRecord.class), record);
        FillAssist.returnDeal.accept(HiddenMap.ADMIN_DAILY_CLEAR,record);
        return new RetMessage<>(RetCodeEnum.success, "查询成功!", nullOrJson(record));
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
        Account account = accountService.queryAccountById(operaterId);
        RetMessage<String> message = checkAccount.apply(account, OperateType.queryClearRecord);
        if (message != null) {
            return message;
        }
        EntityWrapper<ClearRecord> wrapper = new EntityWrapper<>();
        wrapper.orderBy("clear_time",false);
        notNullAndEmpty(merchantName).ifPresent(e -> wrapper.like("merchant_name", e));
        notNullAndEmpty(timeStart).ifPresent(e -> wrapper.ge("create_time", timeFixZero(timeStart)));
        notNullAndEmpty(timeStop).ifPresent(e -> wrapper.le("create_time", timeFixNine(timeStop)));
        notNUll(clearStatus).ifPresent(e -> wrapper.eq("clear_status", clearStatus.getValue()));
        notNUll(invoiceStatus).ifPresent(e -> wrapper.eq("invoice_status", invoiceStatus.getValue()));
        Page<ClearRecord> page = new Page<>(pageNum, pageSize);
        List<ClearRecord> list = clearService.queryClearRecords(wrapper, page);
        FillAssist.returnDeal.accept(HiddenMap.ADMIN_CLEAR_RECORD,list);
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("list", list);
        return new RetMessage<>(RetCodeEnum.success, "查询成功!", nullOrJson(map));


    }

    @Override
    public RetMessage<File> exportClearRecords(String merchantName,
                                               String timeStart,
                                               String timeStop,
                                               ClearStatus clearStatus,
                                               InvoiceStatus invoiceStatus,
                                               Long operaterId) {
        Account account = accountService.queryAccountById(operaterId);
        RetMessage message = checkAccount.apply(account, OperateType.exportClearRecord);
        if (message != null)
            return message;
        EntityWrapper<ClearRecord> wrapper = new EntityWrapper<>();
        wrapper.orderBy("clear_time",false);
        notNullAndEmpty(merchantName).ifPresent(e -> wrapper.eq("merchant_name", merchantName));
        notNUll(timeStart).ifPresent(e -> wrapper.ge("create_time", TimeAssist.timeFixZero(timeStart)));
        notNUll(timeStop).ifPresent(e -> wrapper.le("create_time", TimeAssist.timeFixNine(e)));
        notNUll(clearStatus).ifPresent(e -> wrapper.eq("clear_status", clearStatus.getValue()));
        notNUll(invoiceStatus).ifPresent(e -> wrapper.eq("invoice_status", invoiceStatus.getValue()));
        List<ClearRecord> records = clearService.queryClearRecords(wrapper, null);
//        FillAssist.returnDeal.accept(HiddenMap.ADMIN_REQUEST,records);
        try {
            File file = new File("结算记录导出" + TimeAssist.getNow() + ".xls");
            FileDelete.add(file);
            ExcelUtil.exportExcelFile(ExcelAssist.toStringList(records,ClearRecord.class,HiddenMap.getAdminFieldCN), new FileOutputStream(file));
//            ExcelUtil.exportExcelFile(records, null, new FileOutputStream(file));
            return new RetMessage<>(RetCodeEnum.success, "操作成功!", file);
        } catch (FileNotFoundException e) {
            LogFactory.error(this, "导出结算记录时发生异常!", e);
            return new RetMessage<>(RetCodeEnum.error, "操作失败!", null);
        }
    }

    @Override
    public RetMessage<String> queryClearRecords(String timeStart,
                                                String timeStop,
                                                ClearStatus clearStatus,
                                                InvoiceStatus invoiceStatus,
                                                Integer pageNum,
                                                Integer pageSize,
                                                Long operaterId) {
        Account account = accountService.queryAccountById(operaterId);
        RetMessage<String> message = checkAccount.apply(account, OperateType.queryClearRecord);
        if (message != null) {
            return message;
        }

        EntityWrapper<ClearRecord> wrapper = new EntityWrapper<>();
        wrapper.orderBy("clear_time",false);
        wrapper.eq("merchant_id", account.getMerchantId());
        notNullAndEmpty(timeStart).ifPresent(e -> wrapper.ge("create_time", timeFixZero(timeStart)));
        notNullAndEmpty(timeStop).ifPresent(e -> wrapper.le("create_time", timeFixNine(timeStop)));
        notNUll(clearStatus).ifPresent(e -> wrapper.eq("clear_status", clearStatus.getValue()));
        notNUll(invoiceStatus).ifPresent(e -> wrapper.eq("invoice_status", invoiceStatus.getValue()));
        Page<ClearRecord> page = new Page<>(pageNum, pageSize);
        List<ClearRecord> list = clearService.queryClearRecords(wrapper, page);
        FillAssist.returnDeal.accept(FillAssist.getMap.apply(AccountType.merchantManager,ClearRecord.class), list);
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("list", list);
        message = new RetMessage<>();
        message.setAll(RetCodeEnum.success, "查询成功!", nullOrJson(map));
        return message;
    }

    @Override
    public RetMessage<File> exportClearRecords(String timeStart,
                                               String timeStop,
                                               ClearStatus clearStatus,
                                               InvoiceStatus invoiceStatus,
                                               Long operaterId) {
        Account account = accountService.queryAccountById(operaterId);
        RetMessage message = checkAccount.apply(account, OperateType.exportClearRecord);
        if (message != null)
            return message;
        Merchant m=merchantService.queryMerchantParticularsById(account.getMerchantId());
         message = checkMerchant.apply(m, OperateType.exportClearRecord);
        if (notNull.test(message))
            return message;
        EntityWrapper<ClearRecord> wrapper = new EntityWrapper<>();
        wrapper.orderBy("clear_time",false);
        wrapper.eq("merchant_id", account.getMerchantId());
        notNUll(timeStart).ifPresent(e -> wrapper.ge("create_time", TimeAssist.timeFixZero(timeStart)));
        notNUll(timeStop).ifPresent(e -> wrapper.le("create_time", TimeAssist.timeFixNine(e)));
        notNUll(clearStatus).ifPresent(e -> wrapper.eq("clear_status", clearStatus.getValue()));
        notNUll(invoiceStatus).ifPresent(e -> wrapper.eq("invoice_status", invoiceStatus.getValue()));
        List<ClearRecord> records = clearService.queryClearRecords(wrapper, null);
        try {
            File file = new File("结算记录导出" + TimeAssist.getNow() + ".xls");
            FileDelete.add(file);
            ExcelUtil.exportExcelFile(ExcelAssist.toStringList(records,ClearRecord.class ,HiddenMap.getMerchantManagerFieldCN), new FileOutputStream(file));
            return new RetMessage<>(RetCodeEnum.success, "操作成功!", file);
        } catch (FileNotFoundException e) {
            LogFactory.info(this, "导出结算记录时发生异常!", e);
            return new RetMessage<>(RetCodeEnum.fail, "操作失败!", null);
        }
    }

    @Override
    public RetMessage<String> queryRecordInfo(Long recordId,
                                              Long operaterId) {
        Account account = accountService.queryAccountById(operaterId);
        RetMessage<String> message = checkAccount.apply(account, OperateType.queryClearRecord);
        if (message != null) {
            return message;
        }
        ClearRecord record = clearService.queryClearRecordInfo(recordId);
        if(isAdmin.test(account))
            FillAssist.returnDeal.accept(FillAssist.getMap.apply(AccountType.admin,ClearRecord.class), record);
        else if(isMerchantManager.test(account))
            FillAssist.returnDeal.accept(FillAssist.getMap.apply(AccountType.merchantManager,ClearRecord.class), record);
        message = new RetMessage<>();
        message.setAll(RetCodeEnum.success, "查询成功!", nullOrJson(record));
        return message;
    }

    @Override
    public RetMessage<String> clear(Long[] dailyRecordIds,
                                    Long operaterId) {
        //校验权限
        Account account = accountService.queryAccountById(operaterId);
        RetMessage<String> message = checkAccount.apply(account, OperateType.createClearRecord);
        if (message != null) {
            return message;
        }
        //查询list并排除重复的
        List<ClearDailyRecord> list = clearService.queryDailyRecords(dailyRecordIds).stream().distinct().collect(Collectors.toList());
        try {
            //redis加锁
            List<Boolean> lock = lockService.lock(list);
            if (lock.parallelStream().filter(e -> e == null || e == false).findAny().isPresent()) {
                return new RetMessage<>(RetCodeEnum.fail, "部分日清算记录可能正在被处理!", null);
            }
            //更新数据库
            lock = list.stream().map(clearService::clearDailyRecord).collect(Collectors.toList());
            //映射处理结果
            Map<Long, Boolean> clearResult = new HashMap<>();
            for (int i = 0; i < dailyRecordIds.length; i++)
                clearResult.put(dailyRecordIds[i], lock.get(i));
            //根据商户号分组
            Map<Long, List<ClearDailyRecord>> merchantDailyRecord = list.stream().collect(Collectors.groupingBy(e -> e.getMerchantId()));
            //分组处理,插入数据库
            merchantDailyRecord.entrySet().forEach(e -> {
                Long merchantId = e.getKey();
                Merchant merchant = merchantService.queryMerchantParticularsById(merchantId);
                List<ClearDailyRecord> records = e.getValue();
                BigDecimal showTotal = records.stream().map(f -> f.getVerificateCost()).reduce(new BigDecimal(0), (a, b) -> a.add(b));
                ClearRecord record = new ClearRecord();
                record.setRecordId(IdWorker.getId());
                record.setMerchantId(merchantId);
                record.setMerchantName(merchant.getMerchantName());
                record.setDailyRecordIds(records.stream().map(f -> f.getRecordId()).collect(Collectors.toList()));
                record.setOperaterId(operaterId);
                record.setOperaterName(account.getUserName());
                record.setCreateTime(TimeAssist.getNow());
                record.insert();
            });
//            OperateRecord operateRecord=new OperateRecord();
            OperateRecord record = new OperateRecord();
            record.setRecordId(IdWorker.getId());
            record.setOperaterId(account.getAccountId());
            record.setAccountType(account.getAccountType());
            record.setPhone(account.getPhone());
            record.setName(account.getUserName());
            record.setTargetType(OperateTargetType.clearRecord);
            record.setType(OperateType.createClearRecord);
            record.setOperateTime(TimeAssist.getNow());
            record.setOperateResult("success");
            record.setMerchantId(account.getMerchantId());
            record.setShopId(account.getShopId());
            record.setMerchantName(account.getMerchantName());
            record.setShopName(account.getShopName());
//            record.setDescription("结算[" + Arrays.asList(dailyRecordIds) + "]");
            record.setDescription("结算");
            operateRecordService.addRecord(record);

            if (lock.parallelStream().filter(e -> e == null || e == false).findAny().isPresent()) {
                Map map = new HashMap();
                for (int i = 0; i < dailyRecordIds.length; i++)
                    map.put(dailyRecordIds[i], lock);
                return new RetMessage<>(RetCodeEnum.success, "清算部分成功!", ToJson.toJson(map));
            }
            return new RetMessage<>(RetCodeEnum.success, "清算成功!", null);
        } catch (Exception e) {
            LogFactory.info(this, "清算异常!", e);
            return new RetMessage<>(RetCodeEnum.error, "发生异常!", null);
        } finally {
            lockService.unlock(list);
        }
    }

    @Override
    public RetMessage<String> clear(String clearTime, BigDecimal factTotal, String remark, Long recordId, Long operaterId) {
        ClearRecord record = clearService.queryClearRecordInfo(recordId);
        if (!ClearStatus.uncleared.equals(record.getClearStatusEnum()))
            return new RetMessage<>(RetCodeEnum.fail, "非未结算状态,无法结算!", null);
        Account account = accountService.queryAccountById(operaterId);
        RetMessage message = checkAccount.apply(account, OperateType.modifyClearRecord);
        if (message != null)
            return message;
        record.setFactTotal(factTotal);
        record.setOperaterId(account.getAccountId());
        record.setOperaterName(account.getUserName());
        record.setClearStatus(ClearStatus.cleard);
        record.setClearTime(TimeAssist.timeFixZero(clearTime));
        record.setRemark(remark);
        boolean result = record.updateById();
        if (result) {
            OperateRecord operateRecord = new OperateRecord();
            operateRecord.setRecordId(IdWorker.getId());
            operateRecord.setOperaterId(account.getAccountId());
            operateRecord.setAccountType(account.getAccountType());
            operateRecord.setPhone(account.getPhone());
            operateRecord.setName(account.getUserName());
            operateRecord.setTargetType(OperateTargetType.clearRecord);
            operateRecord.setType(OperateType.modifyClearRecord);
            operateRecord.setOperateTime(TimeAssist.getNow());
            operateRecord.setOperateResult(result ? "success" : "fail");
            operateRecord.setDescription("添加结算信息");
            operateRecord.setMerchantId(account.getMerchantId());
            operateRecord.setShopId(account.getShopId());
            operateRecord.setMerchantName(account.getMerchantName());
            operateRecord.setShopName(account.getShopName());
            operateRecord.insert();
            return new RetMessage<>(RetCodeEnum.success, "操作成功!", null);
        } else {
            return new RetMessage<>(RetCodeEnum.fail, "操作失败!", null);
        }
    }

    @Override
    public RetMessage<String> addInvoiceInfo(String taxNum, String logisticsCompany, String orderNum, String postTime, Long recordId, Long operaterId) {
        Account account = accountService.queryAccountById(operaterId);
        RetMessage message = checkAccount.apply(account, OperateType.modifyClearRecord);
        if (message != null)
            return message;
        ClearRecord record = clearService.queryClearRecordInfo(recordId);
        if (!ClearStatus.cleard.equals(record.getClearStatus()))
            return new RetMessage<>(RetCodeEnum.fail, "未结算!", null);
        if (!InvoiceStatus.unposted.equals(record.getInvoiceStatus()))
            return new RetMessage<>(RetCodeEnum.fail, "已录入!", null);
        record.setTaxNum(taxNum);
        record.setOrderNum(orderNum);
        record.setPostTime(postTime);
        record.setLogisticsCompany(logisticsCompany);
        boolean result = record.updateById();
        if (result) {
            OperateRecord operateRecord = new OperateRecord();
            operateRecord.setRecordId(IdWorker.getId());
            operateRecord.setOperaterId(account.getAccountId());
            operateRecord.setAccountType(account.getAccountType());
            operateRecord.setPhone(account.getPhone());
            operateRecord.setName(account.getUserName());
            operateRecord.setTargetType(OperateTargetType.clearRecord);
            operateRecord.setType(OperateType.modifyClearRecord);
            operateRecord.setOperateTime(TimeAssist.getNow());
            operateRecord.setOperateResult(result ? "success" : "fail");
            operateRecord.setMerchantId(account.getMerchantId());
            operateRecord.setShopId(account.getShopId());
            operateRecord.setMerchantName(account.getMerchantName());
            operateRecord.setShopName(account.getShopName());
            operateRecord.insert();

            return new RetMessage<>(RetCodeEnum.success, "操作成功!", null);
        } else
            return new RetMessage<>(RetCodeEnum.fail, "操作失败!", null);
    }

    @Override
    public RetMessage<String> statsticDailyRecords(String merchantName, String timeStart, String timeStop, ClearStatus clearStatus, Integer pageNum, Integer pageSize, Long operaterId) {
        Account account = accountService.queryAccountById(operaterId);
        RetMessage<String> message = checkAccount.apply(account, OperateType.queryDailyRecord);
        if (message != null)
            return message;
        EntityWrapper<ClearDailyRecord> wrapper = new EntityWrapper<>();
        wrapper.orderBy("record_time",false);
        notNullAndEmpty(merchantName).ifPresent(e -> wrapper.like("merchant_name", e));
        notNullAndEmpty(timeStart).ifPresent(e -> wrapper.ge("record_time", timeFixZero(timeStart)));
        notNullAndEmpty(timeStop).ifPresent(e -> wrapper.le("record_time", timeFixNine(timeStop)));
        notNUll(clearStatus).ifPresent(e -> wrapper.eq("clear_status", clearStatus.getValue()));
        BinaryOperator<Integer> addInteger = (a, b) -> {
            a = a == null ? 0 : a;
            b = b == null ? 0 : b;
            return a + b;
        };
        BinaryOperator<BigDecimal> addDecimal = (a, b) -> {
            a = a == null ? new BigDecimal(0) : a;
            b = b == null ? new BigDecimal(0) : b;
            return a.add(b);
        };
        Page<ClearDailyRecord> page = new Page<>(pageNum, pageSize);
        List<ClearDailyRecord> list = clearService.queryDailyRecords(wrapper, null);
        FillAssist.returnDeal.accept(HiddenMap.ADMIN_DAILY_CLEAR,list);
        Map<String, Object> sum = new HashMap<>();
        sum.put("verificateNum", list.parallelStream().map(ClearDailyRecord::getVerificateNum).reduce(0, addInteger));
        sum.put("cancelationNum", list.parallelStream().map(ClearDailyRecord::getCancelationNum).reduce(0, addInteger));
        sum.put("overtimeNum", list.parallelStream().map(ClearDailyRecord::getOvertimeNum).reduce(0, addInteger));
        sum.put("exchangeNum", (Integer) sum.get("verificateNum") + (Integer) sum.get("cancelationNum") + (Integer) sum.get("overtimeNum"));
        sum.put("verificateCost", list.stream().map(ClearDailyRecord::getVerificateCost).reduce(new BigDecimal(0), addDecimal));
        sum.put("cancelationCost", list.stream().map(ClearDailyRecord::getCancelationCost).reduce(new BigDecimal(0), addDecimal));
        sum.put("overtimeCost", list.stream().map(ClearDailyRecord::getOvertimeCost).reduce(new BigDecimal(0), addDecimal));
        sum.put("exchangeCost", ((BigDecimal) sum.get("verificateCost")).add((BigDecimal) sum.get("cancelationCost")).add(((BigDecimal) sum.get("overtimeCost"))));
        list = clearService.queryDailyRecords(wrapper, page);
        page.setRecords(null);
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("list", list);
        map.put("sum", sum);
        return new RetMessage<>(RetCodeEnum.success, "查询成功!", nullOrJson(map));
    }

    @Override
    public RetMessage<File> exportStatsticDailyRecords(String merchantName, String timeStart, String timeStop, ClearStatus clearStatus, Long operaterId) {
        Account account = accountService.queryAccountById(operaterId);
        RetMessage message = checkAccount.apply(account, OperateType.queryDailyRecord);
        if (message != null)
            return message;
        EntityWrapper<ClearDailyRecord> wrapper = new EntityWrapper<>();
        wrapper.orderBy("record_time",false);
        notNullAndEmpty(merchantName).ifPresent(e -> wrapper.like("merchant_name", e));
        notNullAndEmpty(timeStart).ifPresent(e -> wrapper.ge("record_time", timeFixZero(timeStart)));
        notNullAndEmpty(timeStop).ifPresent(e -> wrapper.le("record_time", timeFixNine(timeStop)));
        notNUll(clearStatus).ifPresent(e -> wrapper.eq("clear_status", clearStatus.getValue()));
        BinaryOperator<Integer> addInteger = (a, b) -> {
            a = a == null ? 0 : a;
            b = b == null ? 0 : b;
            return a + b;
        };
        BinaryOperator<BigDecimal> addDecimal = (a, b) -> {
            a = a == null ? new BigDecimal(0) : a;
            b = b == null ? new BigDecimal(0) : b;
            return a.add(b);
        };
        List<ClearDailyRecord> list = clearService.queryDailyRecords(wrapper, null);
        ClearDailyRecord sum = new ClearDailyRecord();
        sum.setVerificateNum(list.parallelStream().map(ClearDailyRecord::getVerificateNum).reduce(0, addInteger));
        sum.setCancelationNum(list.parallelStream().map(ClearDailyRecord::getCancelationNum).reduce(0, addInteger));
        sum.setOvertimeNum(list.parallelStream().map(ClearDailyRecord::getOvertimeNum).reduce(0, addInteger));

        sum.setVerificateCost(list.stream().map(ClearDailyRecord::getVerificateCost).reduce(new BigDecimal(0), addDecimal));
        sum.setCancelationCost(list.stream().map(ClearDailyRecord::getCancelationCost).reduce(new BigDecimal(0), addDecimal));
        sum.setOvertimeCost(list.stream().map(ClearDailyRecord::getOvertimeCost).reduce(new BigDecimal(0), addDecimal));

        list.add(new ClearDailyRecord());
        list.add(sum);
        File file = new File("日结算记录统计导出" + TimeAssist.getNow() + ".xls");
        FileDelete.add(file);
        try {
            ExcelUtil.exportExcelFile(ExcelAssist.toStringList(list,ClearDailyRecord.class,HiddenMap.getAdminFieldCN), new FileOutputStream(file));
            return new RetMessage<>(RetCodeEnum.success, "操作成功!", file);
        } catch (FileNotFoundException e) {
            LogFactory.error(this, "日结算记录导统计出时发生异常!", e);
            return new RetMessage<>(RetCodeEnum.error, "操作失败!", null);
        }

    }

    @Override
    public RetMessage<String> statsticDailyRecords(String timeStart, String timeStop, ClearStatus clearStatus, Integer pageNum, Integer pageSize, Long operaterId) {
        Account account = accountService.queryAccountById(operaterId);
        RetMessage<String> message = checkAccount.apply(account, OperateType.queryDailyRecord);
        if (message != null)
            return message;
        Merchant m=merchantService.queryMerchantParticularsById(account.getMerchantId());
        message = checkMerchant.apply(m, OperateType.queryDailyRecord);
        if (notNull.test(message))
            return message;
        EntityWrapper<ClearDailyRecord> wrapper = new EntityWrapper<>();
        wrapper.orderBy("record_time",false);
        wrapper.eq("merchant_id", account.getMerchantId());
        notNullAndEmpty(timeStart).ifPresent(e -> wrapper.ge("record_time", timeFixZero(timeStart)));
        notNullAndEmpty(timeStop).ifPresent(e -> wrapper.le("record_time", timeFixNine(timeStop)));
        notNUll(clearStatus).ifPresent(e -> wrapper.eq("clear_status", clearStatus.getValue()));
        BinaryOperator<Integer> addInteger = (a, b) -> {
            a = a == null ? 0 : a;
            b = b == null ? 0 : b;
            return a + b;
        };
        BinaryOperator<BigDecimal> addDecimal = (a, b) -> {
            a = a == null ? new BigDecimal(0) : a;
            b = b == null ? new BigDecimal(0) : b;
            return a.add(b);
        };
        Page<ClearDailyRecord> page = new Page<>(pageNum, pageSize);
        List<ClearDailyRecord> list = clearService.queryDailyRecords(wrapper, null);
        Map<String, Object> sum = new HashMap<>();
        sum.put("verificateNum", list.parallelStream().map(ClearDailyRecord::getVerificateNum).reduce(0, addInteger));
        sum.put("cancelationNum", list.parallelStream().map(ClearDailyRecord::getCancelationNum).reduce(0, addInteger));
        sum.put("overtimeNum", list.parallelStream().map(ClearDailyRecord::getOvertimeNum).reduce(0, addInteger));
        sum.put("exchangeNum", (Integer) sum.get("verificateNum") + (Integer) sum.get("cancelationNum") + (Integer) sum.get("overtimeNum"));
        sum.put("verificateCost", list.stream().map(ClearDailyRecord::getVerificateCost).reduce(new BigDecimal(0), addDecimal));
        sum.put("cancelationCost", list.stream().map(ClearDailyRecord::getCancelationCost).reduce(new BigDecimal(0), addDecimal));
        sum.put("overtimeCost", list.stream().map(ClearDailyRecord::getOvertimeCost).reduce(new BigDecimal(0), addDecimal));
        sum.put("exchangeCost", ((BigDecimal) sum.get("verificateCost")).add((BigDecimal) sum.get("cancelationCost")).add(((BigDecimal) sum.get("overtimeCost"))));
        list = clearService.queryDailyRecords(wrapper, page);
        FillAssist.returnDeal.accept(FillAssist.getMap.apply(AccountType.merchantManager,ClearDailyRecord.class), list);
        page.setRecords(null);
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("list", list);
        map.put("sum", sum);
        return new RetMessage<>(RetCodeEnum.success, "查询成功!", nullOrJson(map));
    }

    @Override
    public RetMessage<File> exportStatsticDailyRecords(String timeStart, String timeStop, ClearStatus clearStatus, Long operaterId) {
        Account account = accountService.queryAccountById(operaterId);
        RetMessage message = checkAccount.apply(account, OperateType.queryDailyRecord);
        if (message != null)
            return message;
        Merchant m=merchantService.queryMerchantParticularsById(account.getMerchantId());
        message = checkMerchant.apply(m, OperateType.exportDailyRecord);
        if (notNull.test(message))
            return message;
        EntityWrapper<ClearDailyRecord> wrapper = new EntityWrapper<>();
        wrapper.eq("merchant_id", account.getMerchantId());
        wrapper.orderBy("record_time",false);
        notNullAndEmpty(timeStart).ifPresent(e -> wrapper.ge("record_time", timeFixZero(timeStart)));
        notNullAndEmpty(timeStop).ifPresent(e -> wrapper.le("record_time", timeFixNine(timeStop)));
        notNUll(clearStatus).ifPresent(e -> wrapper.eq("clear_status", clearStatus.getValue()));
        BinaryOperator<Integer> addInteger = (a, b) -> {
            a = a == null ? 0 : a;
            b = b == null ? 0 : b;
            return a + b;
        };
        BinaryOperator<BigDecimal> addDecimal = (a, b) -> {
            a = a == null ? new BigDecimal(0) : a;
            b = b == null ? new BigDecimal(0) : b;
            return a.add(b);
        };
        List<ClearDailyRecord> list = clearService.queryDailyRecords(wrapper, null);
        ClearDailyRecord sum = new ClearDailyRecord();
        sum.setVerificateNum(list.parallelStream().map(ClearDailyRecord::getVerificateNum).reduce(0, addInteger));
        sum.setCancelationNum(list.parallelStream().map(ClearDailyRecord::getCancelationNum).reduce(0, addInteger));
        sum.setOvertimeNum(list.parallelStream().map(ClearDailyRecord::getOvertimeNum).reduce(0, addInteger));

        sum.setVerificateCost(list.stream().map(ClearDailyRecord::getVerificateCost).reduce(new BigDecimal(0), addDecimal));
        sum.setCancelationCost(list.stream().map(ClearDailyRecord::getCancelationCost).reduce(new BigDecimal(0), addDecimal));
        sum.setOvertimeCost(list.stream().map(ClearDailyRecord::getOvertimeCost).reduce(new BigDecimal(0), addDecimal));

        list.add(new ClearDailyRecord());
        list.add(sum);
        File file = new File("日结算记录统计" + TimeAssist.getNow() + ".xls");
        FileDelete.add(file);
        try {
            ExcelUtil.exportExcelFile(ExcelAssist.toStringList(list,ClearDailyRecord.class ,HiddenMap.getMerchantManagerFieldCN), new FileOutputStream(file));
            return new RetMessage<>(RetCodeEnum.success, "操作成功!", file);
        } catch (FileNotFoundException e) {
            LogFactory.error(this, "日结算记录统计时发生异常!", e);
            return new RetMessage<>(RetCodeEnum.error, "操作失败!", null);
        }

    }

    @Override
    public RetMessage<File> exoirtDailyRecords(String timeStart, String timeStop, ClearStatus clearStatus, InvoiceStatus invoiceStatus, Long operaterId) {
        Account account = accountService.queryAccountById(operaterId);
        RetMessage message = checkAccount.apply(account, OperateType.queryDailyRecord);
        if (message != null)
            return message;
        Merchant m=merchantService.queryMerchantParticularsById(account.getMerchantId());
        message = checkMerchant.apply(m, OperateType.exportDailyRecord);
        if (notNull.test(message))
            return message;
        EntityWrapper<ClearDailyRecord> wrapper = new EntityWrapper<>();
        wrapper.eq("merchant_id", account.getMerchantId());
        notNullAndEmpty(timeStart).ifPresent(e -> wrapper.ge("record_time", timeFixZero(timeStart)));
        notNullAndEmpty(timeStop).ifPresent(e -> wrapper.le("record_time", timeFixNine(timeStop)));
        notNUll(clearStatus).ifPresent(e -> wrapper.eq("clear_status", clearStatus.getValue()));
        wrapper.orderBy("record_time",false);
        List<ClearDailyRecord> list = clearService.queryDailyRecords(wrapper, null);
        File file = new File("日结算记录导出" + TimeAssist.getNow() + ".xls");
        FileDelete.add(file);
        try {
            ExcelUtil.exportExcelFile(ExcelAssist.toStringList(list,ClearDailyRecord.class ,HiddenMap.getMerchantManagerFieldCN), new FileOutputStream(file));
            return new RetMessage<>(RetCodeEnum.success, "操作成功!", file);
        } catch (FileNotFoundException e) {
            LogFactory.error(this, "日结算记录导出时发生异常!", e);
            return new RetMessage<>(RetCodeEnum.error, "操作失败!", null);
        }
    }

    @Override
    public RetMessage<File> exoirtDailyRecords(String merchantName, String timeStart, String timeStop, ClearStatus clearStatus, InvoiceStatus invoiceStatus, Long operaterId) {
        Account account = accountService.queryAccountById(operaterId);
        RetMessage message = checkAccount.apply(account, OperateType.queryDailyRecord);
        if (message != null)
            return message;
        EntityWrapper<ClearDailyRecord> wrapper = new EntityWrapper<>();
        notNullAndEmpty(merchantName).ifPresent(e -> wrapper.like("merchant_name", e));
        notNullAndEmpty(timeStart).ifPresent(e -> wrapper.ge("record_time", timeFixZero(timeStart)));
        notNullAndEmpty(timeStop).ifPresent(e -> wrapper.le("record_time", timeFixNine(timeStop)));
        notNUll(clearStatus).ifPresent(e -> wrapper.eq("clear_status", clearStatus.getValue()));
        wrapper.orderBy("record_time",false);
        List<ClearDailyRecord> list = clearService.queryDailyRecords(wrapper, null);
        File file = new File("日结算记录导出" + TimeAssist.getNow() + ".xls");
        FileDelete.add(file);
        try {
            ExcelUtil.exportExcelFile(ExcelAssist.toStringList(list,ClearDailyRecord.class,HiddenMap.getAdminFieldCN), new FileOutputStream(file));
            return new RetMessage<>(RetCodeEnum.success, "操作成功!", file);
        } catch (FileNotFoundException e) {
            LogFactory.error(this, "日结算记录导出时发生异常!", e);
            return new RetMessage<>(RetCodeEnum.error, "操作失败!", null);
        }
    }


//    private RetMessage<String> clear(String startMonth,String endMonth,Long merchantId1,Long operaterId){
//        Account account=accountService.queryAccountById(operaterId);
//        RetMessage<String> message=checkAccount.apply(account, OperateType.createClearRecord);
//        if(message!=null) {
//            return message;
//        }
//        //查询list并排除重复的
//        EntityWrapper<ClearDailyRecord> wrapper=new EntityWrapper<>();
//        wrapper.ge("record_time",TimeAssist.timeFixZero(startMonth));
//        wrapper.le("record_time",TimeAssist.timeFixNine(endMonth));
//        wrapper.eq("merchant_id",merchantId1);
//        List<ClearDailyRecord> records=clearService.queryDailyRecords(wrapper,null);
//        Map<Long,List<ClearDailyRecord>> merchantRecords=records.stream().collect(Collectors.groupingBy(ClearDailyRecord::getMerchantId));
//        merchantRecords.entrySet().forEach(e->{
//            Long merchantId=e.getKey();
//            Merchant merchant=merchantService.queryMerchantParticularsById(merchantId);
//            List<ClearDailyRecord> records2=e.getValue();
//            BigDecimal showTotal=records2.stream().map(f->f.getVerificateCost()).reduce(new BigDecimal(0),(a,b)->a.add(b));
//            ClearRecord record=new ClearRecord();
//            record.setRecordId(IdWorker.getId());
//            record.setMerchantId(merchantId);
//            record.setMerchantName(merchant.getMerchantName());
//            record.setDailyRecordIds(records2.stream().map(f->f.getRecordId()).collect(Collectors.toList()));
//            record.setOperaterId(operaterId);
//            record.setOperaterName(account.getUserName());
//            record.setCreateTime(TimeAssist.getNow());
//            record.setTimeStart(startMonth);
//            record.setTimeStart(endMonth);
//            record.insert();
//        });
//        OperateRecord record=new OperateRecord();
//        record.setRecordId(IdWorker.getId());
//        record.setOperaterId(account.getAccountId());
//        record.setAccountType(account.getAccountType());
//        record.setPhone(account.getPhone());
//        record.setName(account.getUserName());
//        record.setTargetType(OperateTargetType.clearRecord);
//        record.setType(OperateType.createClearRecord);
//        record.setOperateTime(TimeAssist.getNow());
//        record.setOperateResult("success");
//        record.setDescription("结算["+ records.stream().map(ClearDailyRecord::getRecordId).collect(Collectors.toList())+"]");
//        operateRecordService.addRecord(record);
//        return new RetMessage<>(RetCodeEnum.success,"生成结算记录成功!",null);
//    }


}
