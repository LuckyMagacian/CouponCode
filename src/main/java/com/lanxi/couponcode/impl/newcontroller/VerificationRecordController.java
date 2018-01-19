package com.lanxi.couponcode.impl.newcontroller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.assist.ExcelAssist;
import com.lanxi.couponcode.impl.entity.*;
import com.lanxi.couponcode.impl.newservice.*;
import com.lanxi.couponcode.impl.assist.FillAssist;
import com.lanxi.couponcode.spi.assist.FileDelete;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.impl.entity.Account;
import com.lanxi.couponcode.impl.entity.VerificationRecord;
import com.lanxi.couponcode.spi.assist.TimeAssist;
import com.lanxi.couponcode.impl.config.HiddenMap;
import com.lanxi.couponcode.spi.consts.annotations.CheckArg;
import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.couponcode.spi.consts.annotations.HiddenArg;
import com.lanxi.couponcode.spi.consts.enums.*;
import com.lanxi.couponcode.spi.defaultInterfaces.ToJson;
import com.lanxi.util.entity.LogFactory;
import com.lanxi.util.utils.ExcelUtil;
import com.lanxi.util.utils.LoggerUtil;
import org.springframework.stereotype.Controller;

import static com.lanxi.couponcode.impl.assist.PredicateAssist.*;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by yangyuanjian on 2017/11/16.
 */
@CheckArg
@Controller("verificationRecordControllerService")
@EasyLog(LoggerUtil.LogLevel.INFO)
public class VerificationRecordController implements com.lanxi.couponcode.spi.service.VerificationRecordService {
    @Resource
    private VerificationRecordService verificationRecordService;
    @Resource
    private AccountService            accountService;
    @Resource
    private MerchantService           merchantService;
    @Resource
    private CommodityService          commodityService;
    @Resource
    private CodeService               codeService;

    @Override
    public RetMessage<String> queryVerificationRecords(Long code,
                                                       String timeStart,
                                                       String timeStop,
                                                       String shopName,
                                                       String merchantName,
                                                       String commodityName,
                                                       String phone,
                                                       VerificationType type,
                                                       Integer pageNum,
                                                       Integer pageSize,
                                                       Long operaterId) {
        Account    account = accountService.queryAccountById(operaterId);
        RetMessage message = checkAccount.apply(account, OperateType.queryVerifyRecordAll);
        if (message != null)
            return message;

        Page<VerificationRecord>          page    = new Page<>(pageNum, pageSize);
        EntityWrapper<VerificationRecord> wrapper = new EntityWrapper<>();
        wrapper.orderBy("verficate_time", false);
        if (code != null)
            wrapper.eq("code", code);
        if (timeStart != null && !timeStart.isEmpty()){
            while(timeStart.length() < 14)
                timeStart += "0";
            wrapper.ge("verficate_time", timeStart);
        }
        if (timeStop != null && !timeStop.isEmpty()){
            while(timeStop.length() < 14)
                timeStop += "9";
            wrapper.le("verficate_time", timeStop);
        }
        if (merchantName != null)
            wrapper.like("merchant_name", merchantName);
        if (shopName != null)
            wrapper.like("shopName", shopName);
        if (commodityName != null)
            wrapper.like("commodity_name", commodityName);
        if (phone != null)
            wrapper.like("operater_phone", phone);
        if (type != null)
            wrapper.eq("verification_type", type.getValue());
        List<VerificationRecord> list = verificationRecordService.queryVerificationRecords(wrapper, page);
        FillAssist.returnDeal.accept(HiddenMap.ADMIN_VERIFY, list);
        if (list == null)
            return new RetMessage<>(RetCodeEnum.fail, "查询失败!", null);
        //需要分页信息
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("list", list);
        return new RetMessage<>(RetCodeEnum.success, "查询成功", ToJson.toJson(map));
    }

    @Override
    public RetMessage<String> queryShopVerificationRecords(String timeStart,
                                                           String timeStop,
                                                           String shopName,
                                                           Long code,
                                                           String commodityName,
                                                           String phone,
                                                           VerificationType type,
                                                           @HiddenArg Integer pageNum,
                                                           @HiddenArg Integer pageSize,
                                                           @HiddenArg Long operaterId) {
        Account    account = accountService.queryAccountById(operaterId);
        RetMessage message = checkAccount.apply(account, OperateType.queryVerifyRecordList);
        if (message != null)
            return message;
        Page<VerificationRecord>          page    = new Page<>(pageNum, pageSize);
        EntityWrapper<VerificationRecord> wrapper = new EntityWrapper<>();
        wrapper.orderBy("verficate_time", false);
        if (code != null)
            wrapper.eq("code", code);
        if (timeStart != null && !timeStart.isEmpty()){
            while(timeStart.length() < 14)
                timeStart += "0";
            wrapper.ge("verficate_time", timeStart);
        }
        if (timeStop != null && !timeStop.isEmpty()){
            while(timeStop.length() < 14)
                timeStop += "9";
            wrapper.le("verficate_time", timeStop);
        }
        if (shopName != null)
            wrapper.like("shopName", shopName);
        if (commodityName != null)
            wrapper.like("commodity_name", commodityName);
        if (phone != null)
            wrapper.like("operater_phone", phone);
        if (type != null)
            wrapper.eq("verification_type", type.getValue());
        wrapper.eq("merchant_id", account.getMerchantId());
        List<VerificationRecord> list = verificationRecordService.queryVerificationRecords(wrapper, page);
        FillAssist.returnDeal.accept(FillAssist.getMap.apply(AccountType.merchantManager, VerificationRecord.class), list);
        if (list == null)
            return new RetMessage<>(RetCodeEnum.fail, "查询失败!", null);
        //需要分页信息
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("list", list);
        return new RetMessage<>(RetCodeEnum.success, "查询成功", ToJson.toJson(map));
    }

    @Override
    public RetMessage<String> queryVerificationRecordInfo(Long recordId, Long operaterId) {
        Account            account = accountService.queryAccountById(operaterId);
        VerificationRecord record  = verificationRecordService.queryVerificationRecordInfo(recordId);
        if (isAdmin.test(account))
            FillAssist.returnDeal.accept(FillAssist.getMap.apply(AccountType.admin, VerificationRecord.class), record);
        else if (isMerchantManager.test(account))
            FillAssist.returnDeal.accept(FillAssist.getMap.apply(AccountType.merchantManager, VerificationRecord.class), record);
        if (notAdmin.test(account) && diffMerchant.test(account, record))
            return new RetMessage<>(RetCodeEnum.fail, "记录不存在!!", null);
        if (record == null)
            return new RetMessage<>(RetCodeEnum.fail, "不存在!", null);
        else
            return new RetMessage<>(RetCodeEnum.success, "查询成功!", record.toString());
    }

    @Override
    public RetMessage<File> exportVerificationRecords(Long code, String timeStart, String timeStop, String merchantName, String shopName, String commodityName, String phone, VerificationType type, Long operaterId) {
        Account    account = accountService.queryAccountById(operaterId);
        RetMessage message = checkAccount.apply(account, OperateType.queryVerifyRecordAll);
        if (message != null)
            return message;

        EntityWrapper<VerificationRecord> wrapper = new EntityWrapper<>();
        wrapper.orderBy("verficate_time", false);
        if (code != null)
            wrapper.eq("code", code);
        if (timeStart != null && !timeStart.isEmpty()){
            while(timeStart.length() < 14)
                timeStart += "0";
            wrapper.ge("verficate_time", timeStart);
        }
        if (timeStop != null && !timeStop.isEmpty()){
            while(timeStop.length() < 14)
                timeStop += "9";
            wrapper.le("verficate_time", timeStop);
        }
        if (merchantName != null)
            wrapper.like("merchant_name", merchantName);
        if (shopName != null)
            wrapper.like("shopName", shopName);
        if (commodityName != null)
            wrapper.like("commodity_name", commodityName);
        if (phone != null)
            wrapper.like("operater_phone", phone);
        if (type != null)
            wrapper.eq("verification_type", type.getValue());
        List<VerificationRecord> list = verificationRecordService.queryVerificationRecords(wrapper, null);
        File                     file = new File("核销记录导出"+TimeAssist.getNow()+".xls");

        try{
            ExcelUtil.exportExcelFile(ExcelAssist.toStringList(list, VerificationRecord.class, HiddenMap.getAdminFieldCN), new FileOutputStream(file));
            return new RetMessage<>(RetCodeEnum.success, "操作成功!", file);
        }catch(FileNotFoundException e){
            LogFactory.error(this, "导出核销记录时发生异常!", e);
            return new RetMessage<>(RetCodeEnum.error, "操作失败!", null);
        }
    }

    @Override
    public RetMessage<File> exportShopVerificationRecords(String timeStart, String timeStop, String shopName, Long code, String commodityName, String phone, VerificationType type, Long operaterId) {
        Account    account = accountService.queryAccountById(operaterId);
        RetMessage message = checkAccount.apply(account, OperateType.queryVerifyRecordList);
        if (message != null)
            return message;
        Merchant m = merchantService.queryMerchantParticularsById(account.getMerchantId());
        message = checkMerchant.apply(m, OperateType.exportVerifyRecord);
        if (notNull.test(message))
            return message;
        EntityWrapper<VerificationRecord> wrapper = new EntityWrapper<>();
        wrapper.eq("merchant_id", account.getMerchantId());
        wrapper.orderBy("verficate_time", false);
        if (code != null)
            wrapper.eq("code", code);
        if (timeStart != null && !timeStart.isEmpty()){
            while(timeStart.length() < 14)
                timeStart += "0";
            wrapper.ge("verficate_time", timeStart);
        }
        if (timeStop != null && !timeStop.isEmpty()){
            while(timeStop.length() < 14)
                timeStop += "9";
            wrapper.le("verficate_time", timeStop);
        }
        if (shopName != null)
            wrapper.like("shopName", shopName);
        if (commodityName != null)
            wrapper.like("commodity_name", commodityName);
        if (phone != null)
            wrapper.like("operater_phone", phone);
        if (type != null)
            wrapper.eq("verification_type", type.getValue());
        List<VerificationRecord> list = verificationRecordService.queryVerificationRecords(wrapper, null);
        File                     file = new File("核销记录导出"+TimeAssist.getNow()+".xls");
        FileDelete.add(file);
        try{
            ExcelUtil.exportExcelFile(ExcelAssist.toStringList(list, VerificationRecord.class, HiddenMap.getMerchantManagerFieldCN), new FileOutputStream(file));
            return new RetMessage<>(RetCodeEnum.success, "操作成功!", file);
        }catch(FileNotFoundException e){
            LogFactory.error(this, "导出核销记录时发生异常!", e);
            return new RetMessage<>(RetCodeEnum.error, "操作失败!", null);
        }
    }

    @Override
    public RetMessage<String> queryVerifyRecordsAndStatstis(Long accountId, String operateTime, Long operaterId) {
        Account                           account = accountService.queryAccountById(operaterId);
        EntityWrapper<VerificationRecord> wrapper = new EntityWrapper<>();
        wrapper.orderBy("verficate_time", false);
        List<VerificationRecord>   records = verificationRecordService.queryVerificationRecords(wrapper, null);
        Stream<VerificationRecord> stream  = records.stream();
        if (account != null && AccountType.shopEmployee.equals(account.getAccountType())){
            stream = stream.filter(e -> e.getOperaterId().equals(accountId));
        }
        if (operateTime != null){
            stream = stream.filter(e -> {
                String time  = e.getVerficateTime();
                String start = TimeAssist.timeFixZero(operateTime);
                String end   = TimeAssist.timeFixNine(operateTime);
                return time.compareTo(start) >= 0 && e.getVerficateTime().compareTo(end) <= 0;
            });
        }
        if (AccountType.merchantManager.equals(account.getAccountType())){
            stream = stream.filter(e -> e.getMerchantId().equals(account.getMerchantId()));
        }
        if (AccountType.shopManager.equals(account.getAccountType())){
            stream = stream.filter(e -> {
                    Long recordShopId=e.getShopId();
                    Long accountShopId=account.getShopId();
                    return accountShopId.equals(recordShopId);
            });
        }
        stream = stream.peek(e -> FillAssist.keepEntityField(Arrays.asList(new String[]{"commodityName", "code", "verficateTime","merchantId"}), e));
        records = stream.collect(Collectors.toList());
        FillAssist.returnDeal.accept(FillAssist.getMap.apply(AccountType.shopManager, VerificationRecord.class), records);

        Map<String, Object> map = new HashMap<>();
        map.put("list", records);
        map.put("count", 0);
        map.put("sum", new BigDecimal(0));
        records.stream().forEach(e -> {
            CouponCode code          = codeService.queryCode(e.getMerchantId(), e.getCode()).orElse(null);
            String     commodityInfo = code.getCommodityInfo();
            Commodity  commodity     = JSON.parseObject(commodityInfo, Commodity.class);
            map.put("count", (Integer)map.get("count")+1);
            //TODO 此处将售价为空的记录的售价改为了0 !!!
            map.put("sum", ((BigDecimal)map.get("sum")).add(commodity.getSellPrice()==null?new BigDecimal(0):commodity.getSellPrice()));
        });
        return new RetMessage<>(RetCodeEnum.success, "操作成功!", ToJson.toJson(map));
    }


    @Override
    public RetMessage<String> statisticVerifyRecord(String shopName,String timeStart,String timeStop,Integer pageNum,Integer pageSize,Long merchantId,Long operaterId){
        Account                           account = accountService.queryAccountById(operaterId);

        RetMessage<String> message=null;
        Page<VerificationRecord>          page    = new Page<>(pageNum, pageSize);
        message=checkAccount.apply(account,OperateType.queryDailyRecord);
        if(message!=null)
            return message;
        EntityWrapper<VerificationRecord> wrapper = new EntityWrapper<>();
        wrapper.orderBy("verficate_time", false);
        if(shopName!=null&&!shopName.isEmpty()){
            wrapper.like("shop_name",shopName);
        }
        if (timeStart != null && !timeStart.isEmpty()){
            while(timeStart.length() < 14)
                timeStart += "0";
            wrapper.ge("verficate_time", timeStart);
        }
        if (timeStop != null && !timeStop.isEmpty()){
            while(timeStop.length() < 14)
                timeStop += "9";
            wrapper.le("verficate_time", timeStop);
        }
        if(merchantId!=null){
            wrapper.eq("merchant_id",merchantId);
        }
        List<VerificationRecord>   records = verificationRecordService.queryVerificationRecords(wrapper, page);
        //将系统管理员核销的处理并按门店编号分组
        Map<String,List<VerificationRecord>> map= records.stream().map(r->{
            if(r.getShopId()==null){
                r.setShopName("管理员操作");
            }
            return r;
        }).collect(Collectors.groupingBy(r->r.getShopName()));

        Map<String,Map<String,Object>> result=new HashMap<>();

        int totalNum  = records.size();
        BigDecimal    totalCost =new BigDecimal(0);
        totalCost=records.stream().map(e->e.getCostPrice()==null?new BigDecimal(0):e.getCostPrice()).reduce(totalCost,(a,b)->a.add(b));
        map.entrySet()
           .stream()
           .forEach(e->{
               String shopNameTemp=e.getKey();
               BigDecimal verCost=new BigDecimal(0);
               List<VerificationRecord> list=e.getValue();
               int verNum=list.size();
               verCost=list.stream().map(r->r.getCostPrice()==null?new BigDecimal(0):r.getCostPrice()).reduce(verCost,(a,b)->a.add(b));
               Map<String,Object> tempMap=new HashMap<>();
               tempMap.put("verfyNum",verNum);
               tempMap.put("verfyCost",verCost);
               result.put(shopNameTemp,tempMap);
           });
        Map<String,Object> tempMap=new HashMap<>();
        tempMap.put("totalNum",totalNum);
        tempMap.put("totalCost",totalCost);
        result.put("total",tempMap);
        return new RetMessage<>(RetCodeEnum.success,"查询成功!",records.toString());
    }





    @Override
    public RetMessage<File> exportStatisticVerifyRecord(String shopName,String timeStart,String timeStop,Long merchantId,Long operaterId){
        Account                           account = accountService.queryAccountById(operaterId);

        RetMessage<String> message=null;
        EntityWrapper<VerificationRecord> wrapper = new EntityWrapper<>();
        wrapper.orderBy("verficate_time", false);
        if(shopName!=null&&!shopName.isEmpty()){
            wrapper.like("shop_name",shopName);
        }
        if (timeStart != null && !timeStart.isEmpty()){
            while(timeStart.length() < 14)
                timeStart += "0";
            wrapper.ge("verficate_time", timeStart);
        }
        if (timeStop != null && !timeStop.isEmpty()){
            while(timeStop.length() < 14)
                timeStop += "9";
            wrapper.le("verficate_time", timeStop);
        }
        if(merchantId!=null){
            wrapper.eq("merchant_id",merchantId);
        }
        List<VerificationRecord>   records = verificationRecordService.queryVerificationRecords(wrapper, null);
        //将系统管理员核销的处理并按门店编号分组
        Map<String,List<VerificationRecord>> map= records.stream().map(r->{
            if(r.getShopId()==null){
                r.setShopName("商户管理员核销");
            }
            return r;
        }).collect(Collectors.groupingBy(r->r.getShopName()));

        Map<String,Map<String,Object>> result=new LinkedHashMap<>();

        int totalNum  = records.size();
        BigDecimal    totalCost =new BigDecimal(0);
        totalCost=records.stream().map(e->e.getCostPrice()==null?new BigDecimal(0):e.getCostPrice()).reduce(totalCost,(a,b)->a.add(b));
        map.entrySet()
           .stream()
           .forEach(e->{
               String shopNameTemp=e.getKey();
               BigDecimal verCost=new BigDecimal(0);
               List<VerificationRecord> list=e.getValue();
               int verNum=list.size();
               verCost=list.stream().map(r->r.getCostPrice()==null?new BigDecimal(0):r.getCostPrice()).reduce(verCost,(a,b)->a.add(b));
               Map<String,Object> tempMap=new HashMap<>();
               tempMap.put("verfyNum",verNum);
               tempMap.put("verfyCost",verCost);
               result.put(shopNameTemp,tempMap);
           });
        Map<String,Object> tempMap=new HashMap<>();
        tempMap.put("totalNum",totalNum);
        tempMap.put("totalCost",totalCost);


        File file = new File("商户核销记录统计" + TimeAssist.getNow() + ".xls");
        List<List<String>> llist=new ArrayList<>();
        List<String> head=new ArrayList<>();
        head.add("门店名称");
        head.add("核销总数");
        head.add("核销总成本");
        llist.add(head);
        result.entrySet().stream().forEach(e->{
            String tempSHopName=e.getKey();
            String verNum=e.getValue().get("verfyNum")==null?"0":e.getValue().get("verfyNum")+"";
            String verCost=e.getValue().get("verfyCost")==null?"0":e.getValue().get("verfyCost")+"";
            List<String> tempList=new ArrayList<>();
            tempList.add(tempSHopName);
            tempList.add(verNum);
            tempList.add(verCost);
            llist.add(tempList);
        });
        List<String> sum=new ArrayList<>();
        sum.add("合计");
        sum.add(totalNum+"");
        sum.add(totalCost+"");
        llist.add(sum);
        try{
            ExcelUtil.exportExcelFile(llist,new FileOutputStream(file));
            return new RetMessage<>(RetCodeEnum.success,"操作成功!",file);
        }catch(Exception e){
            LogFactory.error(this, "商户核销记录统计时发生异常!", e);
            return new RetMessage<>(RetCodeEnum.error, "操作失败!", null);
        }
    }
}
