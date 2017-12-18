package com.lanxi.couponcode.impl.newcontroller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.*;
import com.lanxi.couponcode.impl.newservice.*;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.impl.entity.Account;
import com.lanxi.couponcode.impl.entity.VerificationRecord;
import com.lanxi.couponcode.spi.assist.TimeAssist;
import com.lanxi.couponcode.spi.consts.annotations.CheckArg;
import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.couponcode.spi.consts.annotations.HiddenArg;
import com.lanxi.couponcode.spi.consts.enums.AccountType;
import com.lanxi.couponcode.spi.consts.enums.OperateType;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.couponcode.spi.consts.enums.VerificationType;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.lanxi.couponcode.impl.assist.PredicateAssist.*;

/**
 * Created by yangyuanjian on 2017/11/16.
 */
@CheckArg
@Controller("verificationRecordControllerService")
@EasyLog (LoggerUtil.LogLevel.INFO)
public class VerificationRecordController implements com.lanxi.couponcode.spi.service.VerificationRecordService{
    @Resource
    private VerificationRecordService verificationRecordService;
    @Resource
    private AccountService accountService;
    @Resource
    private MerchantService merchantService;
    @Resource
    private CommodityService commodityService;
    @Resource
    private CodeService codeService;
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
        Account account=accountService.queryAccountById(operaterId);
        RetMessage message=checkAccount.apply(account, OperateType.queryVerifyRecordAll);
        if(message!=null)
            return message;

        Page<VerificationRecord> page=new Page<>(pageNum,pageSize);
        EntityWrapper<VerificationRecord> wrapper=new EntityWrapper<>();
        if(code!=null)
            wrapper.eq("code",code);
        if (timeStart != null && !timeStart.isEmpty()) {
            while (timeStart.length() < 14)
                timeStart += "0";
            wrapper.ge("create_time", timeStart);
        }
        if (timeStop != null && !timeStop.isEmpty()) {
            while (timeStop.length() < 14)
                timeStop += "9";
            wrapper.le("create_time", timeStop);
        }
        if(merchantName!=null)
            wrapper.like("merchant_name",merchantName);
        if(shopName!=null)
            wrapper.like("shopName",shopName);
        if(commodityName!=null)
            wrapper.like("commodity_name",commodityName);
        if(phone!=null)
            wrapper.like("operater_phone",phone);
        if(type!=null)
            wrapper.eq("verification_type",type.getValue());
        List<VerificationRecord> list=verificationRecordService.queryVerificationRecords(wrapper,page);
        if(list==null)
            return  new RetMessage<>(RetCodeEnum.fail,"查询失败!",null);
        //需要分页信息
        Map<String,Object> map=new HashMap<>();
        map.put("page",page);
        map.put("list",list);
        return new RetMessage<>(RetCodeEnum.success,"查询成功",  ToJson.toJson(map));
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
        Account account=accountService.queryAccountById(operaterId);
        RetMessage message=checkAccount.apply(account,OperateType.queryVerifyRecordList);
        if(message!=null)
            return message;
        Page<VerificationRecord> page=new Page<>(pageNum,pageSize);
        EntityWrapper<VerificationRecord> wrapper=new EntityWrapper<>();
        if(code!=null)
            wrapper.eq("code",code);
        if (timeStart != null && !timeStart.isEmpty()) {
            while (timeStart.length() < 14)
                timeStart += "0";
            wrapper.ge("create_time", timeStart);
        }
        if (timeStop != null && !timeStop.isEmpty()) {
            while (timeStop.length() < 14)
                timeStop += "9";
            wrapper.le("create_time", timeStop);
        }
        if(shopName!=null)
            wrapper.like("shopName",shopName);
        if(commodityName!=null)
            wrapper.like("commodity_name",commodityName);
        if(phone!=null)
            wrapper.like("operater_phone",phone);
        if(type!=null)
            wrapper.eq("verification_type",type.getValue());
        wrapper.eq("merchant_id",account.getMerchantId());
        List<VerificationRecord> list=verificationRecordService.queryVerificationRecords(wrapper,page);
        if(list==null)
            return  new RetMessage<>(RetCodeEnum.fail,"查询失败!",null);
        //需要分页信息
        Map<String,Object> map=new HashMap<>();
        map.put("page",page);
        map.put("list",list);
        return new RetMessage<>(RetCodeEnum.success,"查询成功",  ToJson.toJson(map));
    }

    @Override
    public RetMessage<String> queryVerificationRecordInfo(Long recordId, Long operaterId) {
        Account account=accountService.queryAccountById(operaterId);
        VerificationRecord record=verificationRecordService.queryVerificationRecordInfo(recordId);
        if(notAdmin.test(account)&&diffMerchant.test(account,record))
            return new RetMessage<>(RetCodeEnum.fail,"记录不存在!!",null);
        if(record==null)
            return new RetMessage<>(RetCodeEnum.fail,"不存在!",null);
        else
            return new RetMessage<>(RetCodeEnum.success,"查询成功!",record.toString());
    }

    @Override
    public RetMessage<File> exportVerificationRecords(Long code, String timeStart, String timeStop, String merchantName, String shopName, String commodityName, String phone, VerificationType type, Long operaterId) {
        Account account=accountService.queryAccountById(operaterId);
        RetMessage message=checkAccount.apply(account, OperateType.queryVerifyRecordAll);
        if(message!=null)
            return message;

        EntityWrapper<VerificationRecord> wrapper=new EntityWrapper<>();
        if(code!=null)
            wrapper.eq("code",code);
        if (timeStart != null && !timeStart.isEmpty()) {
            while (timeStart.length() < 14)
                timeStart += "0";
            wrapper.ge("create_time", timeStart);
        }
        if (timeStop != null && !timeStop.isEmpty()) {
            while (timeStop.length() < 14)
                timeStop += "9";
            wrapper.le("create_time", timeStop);
        }
        if(merchantName!=null)
            wrapper.like("merchant_name",merchantName);
        if(shopName!=null)
            wrapper.like("shopName",shopName);
        if(commodityName!=null)
            wrapper.like("commodity_name",commodityName);
        if(phone!=null)
            wrapper.like("operater_phone",phone);
        if(type!=null)
            wrapper.eq("verification_type",type.getValue());
        List<VerificationRecord> list=verificationRecordService.queryVerificationRecords(wrapper,null);
        File file=new File("核销记录导出"+ TimeAssist.getNow()+".xls");
        try {
            ExcelUtil.exportExcelFile(list,null,new FileOutputStream(file));
            return new RetMessage<>(RetCodeEnum.success,"操作成功!",file);
        } catch (FileNotFoundException e) {
            LogFactory.error(this,"导出核销记录时发生异常!",e);
            return new RetMessage<>(RetCodeEnum.error,"操作失败!",null);
        }
    }

    @Override
    public RetMessage<File> exportShopVerificationRecords(String timeStart, String timeStop, String shopName, Long code, String commodityName, String phone, VerificationType type, Long operaterId) {
        Account account=accountService.queryAccountById(operaterId);
        RetMessage message=checkAccount.apply(account, OperateType.queryVerifyRecordList);
        if(message!=null)
            return message;
        EntityWrapper<VerificationRecord> wrapper=new EntityWrapper<>();
        wrapper.eq("merchant_id",account.getMerchantId());
        if(code!=null)
            wrapper.eq("code",code);
        if (timeStart != null && !timeStart.isEmpty()) {
            while (timeStart.length() < 14)
                timeStart += "0";
            wrapper.ge("create_time", timeStart);
        }
        if (timeStop != null && !timeStop.isEmpty()) {
            while (timeStop.length() < 14)
                timeStop += "9";
            wrapper.le("create_time", timeStop);
        }
        if(shopName!=null)
            wrapper.like("shopName",shopName);
        if(commodityName!=null)
            wrapper.like("commodity_name",commodityName);
        if(phone!=null)
            wrapper.like("operater_phone",phone);
        if(type!=null)
            wrapper.eq("verification_type",type.getValue());
        List<VerificationRecord> list=verificationRecordService.queryVerificationRecords(wrapper,null);
        File file=new File("核销记录导出"+ TimeAssist.getNow()+".xls");
        try {
            ExcelUtil.exportExcelFile(list,null,new FileOutputStream(file));
            return new RetMessage<>(RetCodeEnum.success,"操作成功!",file);
        } catch (FileNotFoundException e) {
            LogFactory.error(this,"导出核销记录时发生异常!",e);
            return new RetMessage<>(RetCodeEnum.error,"操作失败!",null);
        }
    }

    public RetMessage<String> queryVerifyRecordsAndStatstis(Long accountId,Long operaterId){
        Account account=accountService.queryAccountById(operaterId);
        EntityWrapper<VerificationRecord> wrapper=new EntityWrapper<>();
        List<VerificationRecord> records=verificationRecordService.queryVerificationRecords(new EntityWrapper<VerificationRecord>(),null);
        Stream<VerificationRecord> stream=records.parallelStream();
        if(accountId!=null){
            stream.filter(e->e.getOperaterId().equals(accountId)).collect(Collectors.toList());
        }
        if(AccountType.merchantManager.equals(account.getAccountType())){
            stream.filter(e->e.getMerchantId().equals(account.getMerchantId()));
        }
        if(AccountType.shopManager.equals(account.getAccountType())){
            stream.filter(e->e.getShopId().equals(account.getShopId()));
        }
        records=stream.collect(Collectors.toList());
        VerificationRecord sum=new VerificationRecord();
        records.parallelStream().forEach(e->{
            CouponCode code=codeService.queryCode(e.getMerchantId(),e.getCode()).orElse(null);

        });
        return null;
    }
}
