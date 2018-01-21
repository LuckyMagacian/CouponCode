package com.lanxi.couponcode.impl.newcontroller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.assist.ExcelAssist;
import com.lanxi.couponcode.impl.assist.FillAssist;
import com.lanxi.couponcode.impl.config.HiddenMap;
import com.lanxi.couponcode.impl.dao.ShopDailyVerifyStatsicDao;
import com.lanxi.couponcode.impl.entity.Account;
import com.lanxi.couponcode.impl.entity.CouponCode;
import com.lanxi.couponcode.impl.entity.ShopDailyVerifyStatsitc;
import com.lanxi.couponcode.impl.newservice.AccountService;
import com.lanxi.couponcode.impl.newservice.ShopVerifyStatsticService;
import com.lanxi.couponcode.spi.assist.FileDelete;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.assist.TimeAssist;
import com.lanxi.couponcode.spi.consts.enums.OperateType;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.couponcode.spi.defaultInterfaces.ToJson;
import com.lanxi.couponcode.spi.service.ShopDailyVerifyStatsticService;
import com.lanxi.util.entity.LogFactory;
import com.lanxi.util.utils.ExcelUtil;

import static com.lanxi.couponcode.impl.assist.PredicateAssist.*;
import static com.lanxi.couponcode.spi.assist.CheckAssist.notNullAndEmpty;
import static com.lanxi.couponcode.spi.assist.TimeAssist.timeFixNine;
import static com.lanxi.couponcode.spi.assist.TimeAssist.timeFixZero;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ShopDailyVerifyStatsticController implements ShopDailyVerifyStatsticService {
    @Resource
    private ShopVerifyStatsticService shopVerifyStatsticService;
    @Resource
    private AccountService accountService;
    @Override
    public RetMessage<String> queryStatsticsInfo(Long recordId, Long operaterId) {
        Account account=accountService.queryAccountById(operaterId);
        RetMessage<String> message=null;
        message=checkAccount.apply(account, OperateType.queryVerifyRecordList);
        if(message!=null)
            return message;
        ShopDailyVerifyStatsitc record=shopVerifyStatsticService.queryInfo(recordId);
        return new RetMessage<String>(RetCodeEnum.success,"查询成功!",record.toJson());
    }

    @Override
    public RetMessage<String> queryStatsticsList(String shopName, Long shopId, String timeStart, String timeStop, Integer pageNum, Integer pageSize, Long operaterId) {
        Account account=accountService.queryAccountById(operaterId);
        RetMessage<String> message=null;
        message=checkAccount.apply(account, OperateType.queryVerifyRecordList);
        if(message!=null)
            return message;
        EntityWrapper<ShopDailyVerifyStatsitc> wrapper=new EntityWrapper<>();
        if(shopName!=null&&shopName.isEmpty()){
            wrapper.like("shop_name",shopName);
        }
        if(shopId!=null){
            wrapper.eq("shop_id",shopId);
        }
        notNullAndEmpty(timeStart).ifPresent(e -> wrapper.ge("record_time", timeFixZero(timeStart)));
        notNullAndEmpty(timeStop).ifPresent(e -> wrapper.le("record_time", timeFixNine(timeStop)));
        Page<ShopDailyVerifyStatsitc> page       = new Page<>(pageNum, pageSize);
        List<ShopDailyVerifyStatsitc> records    = shopVerifyStatsticService.queryList(wrapper,page);
        FillAssist.keepEntitiesField(HiddenMap.MERCHANTMANAGER_SHOP_VERIFY.keySet(),records);
        List<ShopDailyVerifyStatsitc> allRecords =shopVerifyStatsticService.queryList(wrapper, null);

        Map<String,Object> total=new HashMap<>();
        Integer sumNum=allRecords.stream().map(e->e.getVerifyNum()).reduce(Integer::sum).orElse(0);
        BigDecimal sumCost=allRecords.stream().map(e->e.getVerifyCostSum()).reduce(new BigDecimal(0),(a,b)->a.add(b));
        total.put("sumNum",sumNum);
        total.put("sumCost",sumCost);
        total.put("list",records);
        return new RetMessage<String>(RetCodeEnum.success, "查询成功!", ToJson.toJson(total));
    }

    @Override
    public RetMessage<File> exportStatsticsList(String shopName, Long shopId, String timeStart, String timeStop, Long operaterId) {
        Account account=accountService.queryAccountById(operaterId);
        RetMessage<File> message=null;
        message=checkAccount.apply(account, OperateType.queryVerifyRecordList);
        if(message!=null)
            return message;
        EntityWrapper<ShopDailyVerifyStatsitc> wrapper=new EntityWrapper<>();
        if(shopName!=null&&shopName.isEmpty()){
            wrapper.like("shop_name",shopName);
        }
        if(shopId!=null){
            wrapper.eq("shop_id",shopId);
        }
        notNullAndEmpty(timeStart).ifPresent(e -> wrapper.ge("record_time", timeFixZero(timeStart)));
        notNullAndEmpty(timeStop).ifPresent(e -> wrapper.le("record_time", timeFixNine(timeStop)));
        List<ShopDailyVerifyStatsitc> allRecords =shopVerifyStatsticService.queryList(wrapper, null);
        FillAssist.keepEntitiesField(HiddenMap.MERCHANTMANAGER_SHOP_VERIFY.keySet(),allRecords);

        Integer sumNum=allRecords.stream().map(e->e.getVerifyNum()).reduce(Integer::sum).orElse(0);
        BigDecimal sumCost=allRecords.stream().map(e->e.getVerifyCostSum()).reduce(new BigDecimal(0),(a,b)->a.add(b));

        File file = new File("门店核销记录导出" + TimeAssist.getNow() + ".xls");
        FileDelete.add(file);
        try{
           List<List<String>> stringList=ExcelAssist.toStringList(allRecords, ShopDailyVerifyStatsitc.class, HiddenMap.getMerchantManagerFieldCN);
           stringList.add(new ArrayList<>());
           //添加分行
           List<String> sum=new ArrayList<>();
           //添加合计信息
           sum.add("合计");
           sum.add(sumNum+"");
           sum.add(sumCost+"");
           stringList.add(sum);
           ExcelUtil.exportExcelFile(stringList, new FileOutputStream(file));
            return new RetMessage<File>(RetCodeEnum.success, "导出成功!", file);
        }catch(FileNotFoundException e){
            LogFactory.error(this, "门店核销记录导出时发生异常!", e);
            return new RetMessage<>(RetCodeEnum.error, "导出失败!", null);
        }
    }
}
