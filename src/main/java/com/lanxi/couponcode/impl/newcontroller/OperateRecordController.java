package com.lanxi.couponcode.impl.newcontroller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.impl.entity.Account;
import com.lanxi.couponcode.impl.entity.OperateRecord;
import com.lanxi.couponcode.impl.newservice.OperateRecordService;
import com.lanxi.couponcode.impl.newservice.RedisEnhancedService;
import com.lanxi.couponcode.impl.newservice.RedisService;
import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.couponcode.spi.consts.enums.AccountType;
import com.lanxi.couponcode.spi.consts.enums.OperateTargetType;
import com.lanxi.couponcode.spi.consts.enums.OperateType;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.util.utils.LoggerUtil;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangyuanjian on 2017/11/16.
 */
@EasyLog(LoggerUtil.LogLevel.INFO)
@Controller
public class OperateRecordController implements com.lanxi.couponcode.spi.service.OperateRecordService {
    @Resource
    private OperateRecordService recordService;
    @Resource
    private RedisService redisService;
    @Resource
    private RedisEnhancedService redisEnhancedService;

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
        //TODO 校验
        Page<OperateRecord> page = new Page<>(pageNum, pageSize);
        EntityWrapper<OperateRecord> wrapper = new EntityWrapper<>();
        if (type != null)
            wrapper.eq("operate_type", type);
        if (targetType != null)
            wrapper.eq("target_type", targetType);
        if (merchantName != null)
            wrapper.like("merchant_name", merchantName);
        if (shopName != null)
            wrapper.like("shop_name", shopName);
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
        if (accountType != null)
            wrapper.eq("account_type", accountType);
        if (name != null)
            wrapper.like("name", name);
        if (phone != null)
            wrapper.like("phone", phone);
        List<OperateRecord> list = recordService.queryRecords(wrapper, page);
        //需要分页信息
        Map<String,Object> map=new HashMap<>();
        map.put("page",page);
        map.put("list",list);

        if (list != null)
            return new RetMessage<>(RetCodeEnum.success, "查询成功!", JSON.toJSONString(map));
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
                                                         String name, String phone,
                                                         Integer pageNum,
                                                         Integer pageSize,
                                                         Long operaterId) {
        //TODO 校验
        //TODO 查询
        Account account = null;

        Page<OperateRecord> page = new Page<>(pageNum, pageSize);
        EntityWrapper<OperateRecord> wrapper = new EntityWrapper<>();
        if (type != null)
            wrapper.eq("operate_type", type);
        if (targetType != null)
            wrapper.eq("operate_target_type", targetType);
        if (shopName != null)
            wrapper.like("shop_name", shopName);
        if (timeStart != null && !timeStart.isEmpty()) {
            while (timeStart.length() < 14)
                timeStart += "0";
            wrapper.ge("create_time", timeStart);
        }
        if (timeEnd != null && !timeEnd.isEmpty()) {
            while (timeEnd.length() < 14)
                timeEnd += "9";
            wrapper.le("create_time", timeEnd);
        }
        if (accountType != null)
            wrapper.eq("account_type", accountType);
        if (name != null)
            wrapper.like("name", name);
        if (phone != null)
            wrapper.like("phone", phone);
        wrapper.eq("merchant_id", account.getMerchantId());
        List<OperateRecord> list = recordService.queryRecords(wrapper, page);
        //需要分页信息
        Map<String,Object> map=new HashMap<>();
        map.put("page",page);
        map.put("list",list);

        if (list == null)
            return new RetMessage<>(RetCodeEnum.fail, "查询失败!", null);
        else
            return new RetMessage<>(RetCodeEnum.success, "查询成功!", JSON.toJSONString(map));
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
        //TODO 校验
        //TODO 查询
        Account account = null;

        Page<OperateRecord> page = new Page<>(pageNum, pageSize);
        EntityWrapper<OperateRecord> wrapper = new EntityWrapper<>();
        if (type != null)
            wrapper.eq("operate_type", type);
        if (targetType != null)
            wrapper.eq("operate_target_type", targetType);
        if (timeStart != null && !timeStart.isEmpty()) {
            while (timeStart.length() < 14)
                timeStart += "0";
            wrapper.ge("create_time", timeStart);
        }
        if (timeEnd != null && !timeEnd.isEmpty()) {
            while (timeEnd.length() < 14)
                timeEnd += "9";
            wrapper.le("create_time", timeEnd);
        }
        if (accountType != null)
            wrapper.eq("account_type", accountType);
        if (name != null)
            wrapper.like("name", name);
        if (phone != null)
            wrapper.like("phone", phone);
        wrapper.eq("merchant_id", account.getMerchantId());
        wrapper.eq("shop_id",account.getShopId());
        List<OperateRecord> list = recordService.queryRecords(wrapper, page);
        //需要分页信息
        Map<String,Object> map=new HashMap<>();
        map.put("page",page);
        map.put("list",list);
        if (list == null)
            return new RetMessage<>(RetCodeEnum.fail, "查询失败!", null);
        else
            return new RetMessage<>(RetCodeEnum.success, "查询成功!", JSON.toJSONString(map));
    }

    @Override
    public RetMessage<String> queryOperateRecordInfo(Long recordId, Long operaterId) {
        //TODO 校验
        OperateRecord record=recordService.queryRecordInfo(recordId);
        if(record==null)
            return new RetMessage<>(RetCodeEnum.fail, "查询失败!", null);
        else
            return new RetMessage<>(RetCodeEnum.success, "查询成功!",record.toJson());
    }
}
