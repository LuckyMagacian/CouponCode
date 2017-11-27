package com.lanxi.couponcode.impl.newcontroller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.lanxi.couponcode.impl.entity.CommodityClearRecord;
import com.lanxi.couponcode.impl.assist.TimeAssist;
import com.lanxi.couponcode.impl.entity.ClearDailyRecord;
import com.lanxi.couponcode.impl.entity.Commodity;
import com.lanxi.couponcode.impl.entity.CouponCode;
import com.lanxi.couponcode.impl.entity.Merchant;
import com.lanxi.couponcode.impl.newservice.*;
import com.lanxi.couponcode.spi.consts.enums.ClearStatus;
import com.lanxi.couponcode.spi.consts.enums.CouponCodeStatus;
import com.lanxi.couponcode.spi.service.QuartzService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 定时任务控制器
 * Created by yangyuanjian on 2017/11/23.
 */
@Controller
public class QuartzController implements QuartzService{
    @Resource
    private RedisService redisService;
    @Resource
    private RedisEnhancedService redisEnhancedService;
    @Resource
    private CommodityService commodityService;
    @Resource
    private CodeService codeService;
    @Resource
    private ClearService clearService;
    @Resource
    private MerchantService merchantService;



    @Override
    public void codeOverTime() {
        EntityWrapper<CouponCode> wrapper=new EntityWrapper<>();
        wrapper.lt("over_time", TimeAssist.getTodayBegin());
        wrapper.eq("code_status",CouponCodeStatus.undestroyed);
        List<CouponCode> list=codeService.queryCodes(wrapper,null);
        list.parallelStream().forEach(codeService::overTimeCode);
    }
    /**清算状态为未清算的,过期时间或核销时间或*/
    @Override
    public void addClearDailyRecord() {
        EntityWrapper<CouponCode> wrapper=new EntityWrapper<>();
        wrapper.eq("clear_status", ClearStatus.uncleared);
        wrapper.isNotNull("final_time");
        wrapper.between("final_time",TimeAssist.getTodayBegin(),TimeAssist.getToodayEnd());
        List codeStatus=new ArrayList(Arrays.asList(CouponCodeStatus.values()));
        codeStatus.remove(CouponCodeStatus.undestroyed);
        codeStatus.remove(CouponCodeStatus.test);
        wrapper.in("code_status",codeStatus);
        //查找所有清算状态为未清算的,串码状态不是未核销及测试的串码
        List<CouponCode> list=codeService.queryCodes(wrapper,null);
        //按商户id分组
        Map<Long,List<CouponCode>> mapByMerchant=list.stream().collect(Collectors.groupingBy(e->e.getMerchantId()));
        //遍历map
        mapByMerchant.entrySet().parallelStream().forEach(e->{

            Long merchantId=e.getKey();
            Merchant merchant=merchantService.queryMerchantParticularsById(merchantId);
            //商户日商品结算记录列表
            List<CommodityClearRecord> commodityClearRecords=new ArrayList<>();
            List<CouponCode> merchantCodes=e.getValue();
            //按商品分组
            Map<Long,List<CouponCode>> mapByCommodity=merchantCodes.parallelStream().collect(Collectors.groupingBy(one->one.getCommodityId()));
            //遍历map,构建单个商品的日结算记录并添加到comodityClearRecords中
            mapByCommodity.entrySet().parallelStream().forEach(f->{
                Long commodityId=f.getKey();
                Commodity commodity=commodityService.queryCommodity(commodityId);
                List<CouponCode> commodityCodes=f.getValue();
                //按状态分组
                Map<CouponCodeStatus,List<CouponCode>> statusCodes=commodityCodes.parallelStream().collect(Collectors.groupingBy(g->CouponCodeStatus.getType(g.getCodeStatus())));
                //创建商户商品日计算记录
                CommodityClearRecord record=new CommodityClearRecord();
                record.setRecordId(IdWorker.getId());
                record.setRecordTime(TimeAssist.getNow());
                //配置商品及商户信息
                record.setCommodityId(commodity.getCommodityId());
                record.setCommodityName(commodity.getCommodityName());
                record.setCommodityType(commodity.getType());
                record.setMerchantId(merchant.getMerchantId());
                record.setMerchantName(merchant.getMerchantName());
                //根据串码状态配置串码数量
                Function<CouponCodeStatus,Integer> statusToSize=(s)-> statusCodes.get(s).size();
                record.setVerificateNum(statusToSize.apply(CouponCodeStatus.destroyed));
                record.setCancelationNum(statusToSize.apply(CouponCodeStatus.cancellation));
                record.setOvertimeNum(statusToSize.apply(CouponCodeStatus.overtime));
                //根据串码状态配置串码成本
                BigDecimal start=new BigDecimal(0);
                Function<CouponCodeStatus,BigDecimal> statusToCost=(s)->commodity.getCostPrice().multiply(new BigDecimal(statusToSize.apply(s)));
                record.setVerificateCost(statusToCost.apply(CouponCodeStatus.destroyed));
                record.setCancelationCost(statusToCost.apply(CouponCodeStatus.cancellation));
                record.setOvertimeCost(statusToCost.apply(CouponCodeStatus.overtime));
                //添加到商户日结算记录中
                commodityClearRecords.add(record);
            });


            //创建商户日结算记录
            ClearDailyRecord dailyRecord=new ClearDailyRecord();
            dailyRecord.setRecordId(IdWorker.getId());
            dailyRecord.setRecordTime(TimeAssist.getNow());
            //配置商户信息
            dailyRecord.setMerchantId(merchant.getMerchantId());
            dailyRecord.setMerchantName(merchant.getMerchantName());
            //配置结算信息
            //转换函数->输入串码状态,输出对应的总数
            Function<CouponCodeStatus,Integer> statusToSumNum=(s)->
                    commodityClearRecords.parallelStream().map(g->{
                        if(CouponCodeStatus.destroyed.equals(s))
                            return g.getVerificateNum();
                        if(CouponCodeStatus.cancellation.equals(s))
                            return g.getCancelationNum();
                        if(CouponCodeStatus.overtime.equals(s))
                            return g.getOvertimeNum();
                        return 0;
                    }).reduce(0,Integer::sum);
            dailyRecord.setVerificateNum(statusToSumNum.apply(CouponCodeStatus.destroyed));
            dailyRecord.setCancelationNum(statusToSumNum.apply(CouponCodeStatus.cancellation));
            dailyRecord.setOvertimeNum(statusToSumNum.apply(CouponCodeStatus.overtime));
            //转换函数->输入串码状态,输出对应的总成本
            Function<CouponCodeStatus,BigDecimal> statusToSumCost=(s)->
                commodityClearRecords.parallelStream().map(g->{
                    if(CouponCodeStatus.destroyed.equals(s))
                        return g.getVerificateCost();
                    if(CouponCodeStatus.cancellation.equals(s))
                        return g.getCancelationCost();
                    if(CouponCodeStatus.overtime.equals(s))
                        return g.getOvertimeCost();
                    return new BigDecimal(0);
                }).reduce(new BigDecimal(0),(a,b)->a.add(b));
            dailyRecord.setVerificateCost(statusToSumCost.apply(CouponCodeStatus.destroyed));
            dailyRecord.setShowTotal(dailyRecord.getVerificateCost());
            dailyRecord.setCancelationCost(statusToSumCost.apply(CouponCodeStatus.cancellation));
            dailyRecord.setOvertimeCost(statusToSumCost.apply(CouponCodeStatus.overtime));
            dailyRecord.setClearStatus(ClearStatus.uncleared);
            //插入到日结算表,若成功,所有该日结算记录相关的串码都更新为已结算
            boolean result=clearService.addClearDailyRecord(dailyRecord);
            if(result){
                merchantCodes.parallelStream().forEach(one->{
                    one.setClearStatus(ClearStatus.cleard);
                    one.setClearTime(TimeAssist.getNow());
                    one.updateById();
                });
            }
        });
    }
}
