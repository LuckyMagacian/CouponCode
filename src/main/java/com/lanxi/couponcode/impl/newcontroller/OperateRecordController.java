package com.lanxi.couponcode.impl.newcontroller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.Account;
import com.lanxi.couponcode.impl.entity.CouponCode;
import com.lanxi.couponcode.impl.entity.OperateRecord;
import com.lanxi.couponcode.impl.newservice.AccountService;
import com.lanxi.couponcode.impl.newservice.OperateRecordService;
import com.lanxi.couponcode.impl.newservice.RedisEnhancedService;
import com.lanxi.couponcode.spi.service.RedisService;
import com.lanxi.couponcode.impl.assist.FillAssist;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.impl.config.HiddenMap;
import com.lanxi.couponcode.spi.consts.annotations.CheckArg;
import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.couponcode.spi.consts.enums.AccountType;
import com.lanxi.couponcode.spi.consts.enums.OperateTargetType;
import com.lanxi.couponcode.spi.consts.enums.OperateType;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.couponcode.spi.defaultInterfaces.ToJson;
import com.lanxi.util.utils.LoggerUtil;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lanxi.couponcode.impl.assist.PredicateAssist.*;

/**
 * Created by yangyuanjian on 2017/11/16.
 */
@CheckArg
@EasyLog (LoggerUtil.LogLevel.INFO)
@Controller ("operateRecordControllerService")
public class OperateRecordController implements com.lanxi.couponcode.spi.service.OperateRecordService {
    @Resource
    private OperateRecordService recordService;
    @Resource
    private RedisService redisService;
    @Resource
    private RedisEnhancedService redisEnhancedService;
    @Resource
    private AccountService accountService;

    @Override
    public RetMessage<String> queryOperateRecord(OperateType type,
                                                 OperateTargetType targetType,
                                                 String merchantName,
                                                 String shopName,
                                                 String timeStart,
                                                 String timeStop,
                                                 AccountType accountType,
                                                 String name,
                                                 String phone,
                                                 Integer pageNum,
                                                 Integer pageSize,
                                                 Long operaterId) {
        //-----------------------------------------------------------------校验--------------------------------------------------------------
        Account account = accountService.queryAccountById(operaterId);
        RetMessage message = checkAccount.apply(account, OperateType.queryOperateRecordList);
        if (message != null)
            return message;
        //-----------------------------------------------------------------执行--------------------------------------------------------------
        Page<OperateRecord> page = new Page<>(pageNum, pageSize);
        EntityWrapper<OperateRecord> wrapper = new EntityWrapper<>();
        wrapper.orderBy("operate_time",false);
        if (type != null)
            wrapper.eq("operate_type", type.getValue());
        if (targetType != null)
            wrapper.eq("target_type", targetType.getValue());
        if (merchantName != null)
            wrapper.like("merchant_name", merchantName);
        if (shopName != null)
            wrapper.like("shop_name", shopName);
        if (timeStart != null && !timeStart.isEmpty()) {
            while (timeStart.length() < 14)
                timeStart += "0";
            wrapper.ge("operate_time", timeStart);
        }
        if (timeStop != null && !timeStop.isEmpty()) {
            while (timeStop.length() < 14)
                timeStop += "9";
            wrapper.le("operate_time", timeStop);
        }
        if (accountType != null)
            wrapper.eq("account_type", accountType.getValue());
        if (name != null)
            wrapper.like("name", name);
        if (phone != null)
            wrapper.like("phone", phone);
        wrapper.ne("account_type",accountType.shopEmployee.getValue());
        wrapper.ne("account_type",AccountType.shopManager.getValue());
        List<OperateRecord> list = recordService.queryRecords(wrapper, page);
        FillAssist.returnDeal.accept(HiddenMap.ADMIN_OPERATE,list);
        //-----------------------------------------------------------------返回--------------------------------------------------------------
        //需要分页信息
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("list", list);
        if (list != null)
            return new RetMessage<>(RetCodeEnum.success, "查询成功!", ToJson.toJson(map));
        else
            return new RetMessage<>(RetCodeEnum.fail, "查询失败!", null);
    }

    @Override
    public RetMessage<String> queryMerchantOperateRecord(OperateType type,
                                                         OperateTargetType targetType,
                                                         String shopName,
                                                         String timeStart,
                                                         String timeEnd,
                                                         AccountType accountType,
                                                         String name,
                                                         String phone,
                                                         Integer pageNum,
                                                         Integer pageSize,
                                                         Long operaterId) {
        //-----------------------------------------------------------------校验--------------------------------------------------------------
        Account account = accountService.queryAccountById(operaterId);
        RetMessage message = checkAccount.apply(account, OperateType.queryOperateRecordList);
        if (message != null)
            return message;
        //-----------------------------------------------------------------执行--------------------------------------------------------------
        Page<OperateRecord> page = new Page<>(pageNum, pageSize);
        EntityWrapper<OperateRecord> wrapper = new EntityWrapper<>();
        wrapper.orderBy("operate_time",false);
        if (type != null)
            wrapper.eq("operate_type", type.getValue());
        if (targetType != null)
            wrapper.eq("operate_target_type", targetType.getValue());
        if (shopName != null)
            wrapper.like("shop_name", shopName);
        if (timeStart != null && !timeStart.isEmpty()) {
            while (timeStart.length() < 14)
                timeStart += "0";
            wrapper.ge("operate_time", timeStart);
        }
        if (timeEnd != null && !timeEnd.isEmpty()) {
            while (timeEnd.length() < 14)
                timeEnd += "9";
            wrapper.le("operate_time", timeEnd);
        }
        if (accountType != null)
            wrapper.eq("account_type", accountType.getValue());
        if (name != null)
            wrapper.like("name", name);
        if (phone != null)
            wrapper.like("phone", phone);
        wrapper.eq("merchant_id", account.getMerchantId());
        wrapper.ne("operate_type",OperateType.destroyCouponCode.getValue());
        List<OperateRecord> list = recordService.queryRecords(wrapper, page);
        FillAssist.returnDeal.accept(FillAssist.getMap.apply(AccountType.merchantManager,OperateRecord.class), list);
        //-----------------------------------------------------------------返回--------------------------------------------------------------
        //需要分页信息
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("list", list);

        if (list == null)
            return new RetMessage<>(RetCodeEnum.fail, "查询失败!", null);
        else
            return new RetMessage<>(RetCodeEnum.success, "查询成功!", ToJson.toJson(map));
    }

    @Override
    public RetMessage<String> queryShopMerchantOperateRecord(OperateType type,
                                                             OperateTargetType targetType,
                                                             String timeStart,
                                                             String timeEnd,
                                                             AccountType accountType,
                                                             String name, String phone,
                                                             Integer pageNum,
                                                             Integer pageSize,
                                                             Long operaterId) {
        Account account = accountService.queryAccountById(operaterId);
        RetMessage message = checkAccount.apply(account, OperateType.queryOperateRecord);
        if (message != null)
            return message;
        Page<OperateRecord> page = new Page<>(pageNum, pageSize);
        EntityWrapper<OperateRecord> wrapper = new EntityWrapper<>();
        wrapper.orderBy("operate_time",false);
        if (type != null)
            wrapper.eq("operate_type", type.getValue());
        if (targetType != null)
            wrapper.eq("operate_target_type", targetType.getValue());
        if (timeStart != null && !timeStart.isEmpty()) {
            while (timeStart.length() < 14)
                timeStart += "0";
            wrapper.ge("operate_time", timeStart);
        }
        if (timeEnd != null && !timeEnd.isEmpty()) {
            while (timeEnd.length() < 14)
                timeEnd += "9";
            wrapper.le("operate_time", timeEnd);
        }
        if (accountType != null)
            wrapper.eq("account_type", accountType.getValue());
        if (name != null)
            wrapper.like("name", name);
        if (phone != null)
            wrapper.like("phone", phone);
        wrapper.eq("merchant_id", account.getMerchantId());
        wrapper.eq("shop_id", account.getShopId());
        List<OperateRecord> list = recordService.queryRecords(wrapper, page);
        FillAssist.returnDeal.accept(FillAssist.getMap.apply(AccountType.merchantManager,OperateRecord.class), list);
        //需要分页信息
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("list", list);
        if (list == null)
            return new RetMessage<>(RetCodeEnum.fail, "查询失败!", null);
        else
            return new RetMessage<>(RetCodeEnum.success, "查询成功!", ToJson.toJson(map));
    }

    @Override
    public RetMessage<String> queryOperateRecordInfo(Long recordId, Long operaterId) {
        Account account = accountService.queryAccountById(operaterId);
        RetMessage message = checkAccount.apply(account, OperateType.queryOperateRecord);
        if (message != null)
            return message;
        OperateRecord record = recordService.queryRecordInfo(recordId);
        if(isAdmin.test(account))
            FillAssist.returnDeal.accept(FillAssist.getMap.apply(AccountType.admin,OperateRecord.class), record);
        else if(isMerchantManager.test(account))
            FillAssist.returnDeal.accept(FillAssist.getMap.apply(AccountType.merchantManager,OperateRecord.class), record);
        else
            FillAssist.returnDeal.accept(FillAssist.getMap.apply(AccountType.shopManager,CouponCode.class), record);
        if (record == null)
            return new RetMessage<>(RetCodeEnum.fail, "查询失败!", null);
        else
            return new RetMessage<>(RetCodeEnum.success, "查询成功!", record.toJson());
    }
}
