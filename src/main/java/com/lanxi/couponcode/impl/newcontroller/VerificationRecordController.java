package com.lanxi.couponcode.impl.newcontroller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.newservice.AccountService;
import com.lanxi.couponcode.impl.newservice.CommodityService;
import com.lanxi.couponcode.impl.newservice.MerchantService;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.impl.entity.Account;
import com.lanxi.couponcode.impl.entity.VerificationRecord;
import com.lanxi.couponcode.impl.newservice.VerificationRecordService;
import com.lanxi.couponcode.spi.consts.annotations.HiddenArg;
import com.lanxi.couponcode.spi.consts.enums.OperateType;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.couponcode.spi.consts.enums.VerificationType;
import org.springframework.stereotype.Controller;

import static com.lanxi.couponcode.spi.assist.PredicateAssist.*;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangyuanjian on 2017/11/16.
 */
@Controller("verificationRecordControllerService")
public class VerificationRecordController implements com.lanxi.couponcode.spi.service.VerificationRecordService{
    @Resource
    private VerificationRecordService verificationRecordService;
    @Resource
    private AccountService accountService;
    @Resource
    private MerchantService merchantService;
    @Resource
    private CommodityService commodityService;
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
            wrapper.eq("verification_type",type);
        List<VerificationRecord> list=verificationRecordService.queryVerificationRecords(wrapper,page);
        if(list==null)
            return  new RetMessage<>(RetCodeEnum.fail,"查询失败!",null);
        //需要分页信息
        Map<String,Object> map=new HashMap<>();
        map.put("page",page);
        map.put("list",list);
        return new RetMessage<>(RetCodeEnum.success,"查询成功", JSON.toJSONString(map));
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
            wrapper.eq("verification_type",type);
        wrapper.eq("merchant_id",account.getMerchantId());
        List<VerificationRecord> list=verificationRecordService.queryVerificationRecords(wrapper,page);
        if(list==null)
            return  new RetMessage<>(RetCodeEnum.fail,"查询失败!",null);
        //需要分页信息
        Map<String,Object> map=new HashMap<>();
        map.put("page",page);
        map.put("list",list);
        return new RetMessage<>(RetCodeEnum.success,"查询成功", JSON.toJSONString(map));
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
}
