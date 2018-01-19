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
import com.lanxi.couponcode.spi.consts.annotations.CheckArg;
import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.couponcode.spi.consts.enums.*;
import com.lanxi.couponcode.spi.defaultInterfaces.ToJson;
import com.lanxi.couponcode.spi.service.RedisService;
import com.lanxi.util.utils.ExcelUtil;
import com.lanxi.util.utils.LoggerUtil;
import com.lanxi.util.utils.TimeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.lanxi.couponcode.impl.assist.PredicateAssist.*;

/**
 * Created by yangyuanjian on 2017/11/20.
 */
//商品不允许直接操作,只能通过request间接操作

@EasyLog (LoggerUtil.LogLevel.INFO)
@Service ("commodityControllerService")
public class CommodityController implements com.lanxi.couponcode.spi.service.CommodityService {
    @Resource
    private DaoService           daoService;
    @Resource
    private AccountService       accountService;
    @Resource
    private RedisEnhancedService redisEnhancedService;
    @Resource
    private RedisService         redisService;
    @Resource
    private CommodityService     commodityService;
    @Resource
    private OperateRecordService operateRecordService;
    @Resource
    private MerchantService      merchantService;

    @CheckArg
    @Override
    public RetMessage<Boolean> addCommodity ( String commodityName,
                                              CommodityType commodityType,
                                              BigDecimal facePrice,
                                              BigDecimal costPrice,
                                              BigDecimal sellPrice,
                                              Integer lifeTime,
                                              @Deprecated String merchantName,
                                              String useDestription,
                                              @Deprecated Long merchantId,
                                              Long operaterId ) {
        Account    account = accountService.queryAccountById(operaterId);
        RetMessage message = checkAccount.apply(account, OperateType.createCommodity);
        if (notNull.test(message))
            return message;
        if (merchantId == null && isMerchantManager.test(account)) {
            merchantId = account.getMerchantId( );
        }
        Merchant merchant = merchantService.queryMerchantParticularsById(merchantId);
        message = checkMerchant.apply(merchant, OperateType.createCommodity);
        if (notNull.test(message))
            return message;
        Commodity commodity = new Commodity( );
        commodity.setCommodityId(IdWorker.getId( ));
        commodity.setCommodityName(commodityName);
        commodity.setType(commodityType);
        commodity.setFacePrice(facePrice);
        commodity.setSellPrice(sellPrice);
        commodity.setCostPrice(costPrice);
        commodity.setLifeTime(lifeTime);
        commodity.setStatus(CommodityStatus.unshelved);

        commodity.setMerchantId(merchant.getMerchantId( ));
        commodity.setMerchantName(merchant.getMerchantName( ));
        //库存
        commodity.setLessNum(null);
        commodity.setWarnningNum(null);

        commodity.setAddId(account.getAccountId( ));
        commodity.setAddTime(TimeUtil.getDateTime( ));
        commodity.setAddName(account.getUserName( ));
        //描述
        commodity.setDescription(useDestription);
        commodity.setUseDetail(useDestription);

        //        //请求
        //        commodity.setRequestId(null);
        //        commodity.setRequestTime(null);
        //        commodity.setRequesterId(null);
        //        commodity.setRequesterName(null);
        //
        //        //审核
        //        commodity.setCheckId(null);
        //        commodity.setCheckName(null);
        //        commodity.setCheckTime(null);
        //备注
        commodity.setRemark(null);
        Boolean result = commodityService.addCommodity(commodity);
        if (result == null)
            return new RetMessage<>(RetCodeEnum.fail, "添加时异常!", null);
        if (result) {
            OperateRecord record = new OperateRecord( );
            record.setRecordId(IdWorker.getId( ));
            record.setOperaterId(account.getAccountId( ));
            record.setAccountType(account.getAccountType( ));
            record.setPhone(account.getPhone( ));
            record.setName(account.getUserName( ));
            record.setTargetType(OperateTargetType.commodity);
            record.setOperateTime(TimeAssist.getNow( ));
            record.setType(OperateType.createCommodity);
            record.setDescription("管理员添加商品");
            //            record.setDescription("管理员添加商品[" + commodity.getCommodityId() + "]");
            record.setOperateResult("success");
            record.setMerchantName(account.getMerchantName( ));
            record.setShopName(account.getShopName( ));
            record.setMerchantId(account.getMerchantId( ));
            record.setShopId(account.getShopId( ));
            operateRecordService.addRecord(record);
            return new RetMessage<>(RetCodeEnum.success, "添加成功!", null);
        } else
            return new RetMessage<>(RetCodeEnum.fail, "添加失败!", null);
    }

    @CheckArg
    @Override
    public RetMessage<Boolean> modifyCommodity ( BigDecimal costPrice,
                                                 BigDecimal facePrice,
                                                 BigDecimal sellPrice,
                                                 Integer lifeTime,
                                                 String useDetail,
                                                 Long commodityId,
                                                 Long operaterId ) {
        Account    account = accountService.queryAccountById(operaterId);
        RetMessage message = checkAccount.apply(account, OperateType.modifyCommodity);
        if (message != null)
            return message;

        Commodity commodity = commodityService.queryCommodity(commodityId);
        if (commodity == null || CommodityStatus.deleted.equals(commodity.getStatus( )))
            return new RetMessage<>(RetCodeEnum.fail, "商品不存在!", null);
        Merchant m=merchantService.queryMerchantParticularsById(commodity.getMerchantId());
        message = checkMerchant.apply(m, OperateType.exportClearRecord);
        if (notNull.test(message))
            return message;
        if (! CommodityStatus.unshelved.equals(commodity.getStatus( ))) {
            return new RetMessage<>(RetCodeEnum.fail, "非下架状态!无法修改!", null);
        }
        if (notNull.test(costPrice))
            commodity.setCostPrice(costPrice);
        if (notNull.test(facePrice))
            commodity.setFacePrice(facePrice);
        if (notNull.test(sellPrice))
            commodity.setSellPrice(sellPrice);
        if (notNull.test(lifeTime))
            commodity.setLifeTime(lifeTime);
        if (notNull.test(useDetail)) {
            commodity.setUseDetail(useDetail);
            commodity.setDescription(useDetail);
        }
        Boolean result = commodityService.modifyCommodity(commodity);
        if (result == null)
            return new RetMessage<>(RetCodeEnum.fail, "修改时异常!", null);
        if (result) {
            OperateRecord record = new OperateRecord( );
            record.setRecordId(IdWorker.getId( ));
            record.setOperaterId(account.getAccountId( ));
            record.setAccountType(account.getAccountType( ));
            record.setPhone(account.getPhone( ));
            record.setName(account.getUserName( ));
            record.setTargetType(OperateTargetType.commodity);
            record.setOperateTime(TimeAssist.getNow( ));
            record.setType(OperateType.modifyCommodity);
            record.setMerchantId(account.getMerchantId( ));
            record.setShopId(account.getShopId( ));
            record.setMerchantName(account.getMerchantName( ));
            record.setShopName(account.getShopName( ));
            record.setDescription("管理员修改商品");
            //            record.setDescription("管理员修改商品[" + commodity.getCommodityId() + "]");
            record.setOperateResult("success");
            operateRecordService.addRecord(record);
            return new RetMessage<>(RetCodeEnum.success, "修改成功!", null);
        } else
            return new RetMessage<>(RetCodeEnum.fail, "修改失败!", null);
    }

    @CheckArg
    @Override
    public RetMessage<Boolean> shelveCommodity ( Long commodityId,
                                                 BigDecimal sellPrice,
                                                 Long operaterId ) {
        Account    account = accountService.queryAccountById(operaterId);
        RetMessage message = checkAccount.apply(account, OperateType.shelveCommodity);
        if (message != null)
            return message;
        Commodity commodity = commodityService.queryCommodity(commodityId);
        if (commodity == null || CommodityStatus.deleted.equals(commodity.getStatus( ))) {
            return new RetMessage<>(RetCodeEnum.fail, "不存在!", null);
        }
        if (! CommodityStatus.unshelved.equals(commodity.getStatus( ))) {
            return new RetMessage<>(RetCodeEnum.fail, "非下架状态!无法上架!", null);
        }
        if(sellPrice!=null)
            commodity.setSellPrice(sellPrice);
        if(commodity.getSellPrice()==null)
            return new RetMessage<>(RetCodeEnum.fail,"销售价格异常!",null);
        Boolean result = commodityService.shelveCommodity(commodity);
        if (result == null)
            return new RetMessage<>(RetCodeEnum.fail, "上架时异常!", null);
        if (result) {
            OperateRecord record = new OperateRecord( );
            record.setRecordId(IdWorker.getId( ));
            record.setOperaterId(account.getAccountId( ));
            record.setAccountType(account.getAccountType( ));
            record.setPhone(account.getPhone( ));
            record.setName(account.getUserName( ));
            record.setTargetType(OperateTargetType.commodity);
            record.setOperateTime(TimeAssist.getNow( ));
            record.setType(OperateType.unshelveCommodity);
            record.setMerchantId(account.getMerchantId( ));
            record.setShopId(account.getShopId( ));
            record.setMerchantName(account.getMerchantName( ));
            record.setShopName(account.getShopName( ));
            record.setDescription("管理员上架商品售价" + sellPrice);
            //            record.setDescription("管理员上架商品[" + commodity.getCommodityId() + "]");
            record.setOperateResult("success");
            operateRecordService.addRecord(record);
            return new RetMessage<>(RetCodeEnum.success, "上架成功!", null);
        } else
            return new RetMessage<>(RetCodeEnum.fail, "上架失败!", null);
    }

    @CheckArg
    @Override
    public RetMessage<Boolean> unshelveCommodity ( Long commodityId,
                                                   Long operaterId ) {
        Account    account = accountService.queryAccountById(operaterId);
        RetMessage message = checkAccount.apply(account, OperateType.unshelveCommodity);
        if (message != null)
            return message;
        Commodity commodity = commodityService.queryCommodity(commodityId);
        if (commodity == null || CommodityStatus.deleted.equals(commodity.getStatus( ))) {
            return new RetMessage<>(RetCodeEnum.fail, "不存在!", null);
        }
        if (! CommodityStatus.shelved.equals(commodity.getStatus( ))) {
            return new RetMessage<>(RetCodeEnum.fail, "非上架状态!无法上架!", null);
        }
        Boolean result = commodityService.unshelveCommodity(commodity);
        if (result == null)
            return new RetMessage<>(RetCodeEnum.fail, "下架时异常!", null);
        if (result) {
            OperateRecord record = new OperateRecord( );
            record.setRecordId(IdWorker.getId( ));
            record.setOperaterId(account.getAccountId( ));
            record.setAccountType(account.getAccountType( ));
            record.setPhone(account.getPhone( ));
            record.setName(account.getUserName( ));
            record.setTargetType(OperateTargetType.commodity);
            record.setOperateTime(TimeAssist.getNow( ));
            record.setType(OperateType.shelveCommodity);
            record.setMerchantId(account.getMerchantId( ));
            record.setShopId(account.getShopId( ));
            record.setDescription("管理员下架商品");
            //            record.setDescription("管理员下架商品[" + commodity.getCommodityId() + "]");
            record.setOperateResult("success");
            operateRecordService.addRecord(record);
            return new RetMessage<>(RetCodeEnum.success, "下架成功!", null);
        } else
            return new RetMessage<>(RetCodeEnum.fail, "下架失败!", null);
    }

    @CheckArg
    @Override
    public RetMessage<Boolean> delCommodity ( Long commodityId,
                                              Long operaterId ) {
        Account    account = accountService.queryAccountById(operaterId);
        RetMessage message = checkAccount.apply(account, OperateType.deleteCommodity);
        if (message != null)
            return message;
        Merchant m=merchantService.queryMerchantParticularsById(account.getMerchantId());
        message = checkMerchant.apply(m, OperateType.deleteCommodity);
        if (notNull.test(message))
            return message;
        Commodity commodity = commodityService.queryCommodity(commodityId);
        if (commodity == null || CommodityStatus.deleted.equals(commodity.getStatus( ))) {
            return new RetMessage<>(RetCodeEnum.fail, "不存在!", null);
        }
        if (! CommodityStatus.unshelved.equals(commodity.getStatus( ))) {
            return new RetMessage<>(RetCodeEnum.fail, "非下架状态!无法删除!", null);
        }
        Boolean result = commodityService.delCommodity(commodity);
        if (result == null)
            return new RetMessage<>(RetCodeEnum.fail, "删除时异常!", null);
        if (result) {
            OperateRecord record = new OperateRecord( );
            record.setRecordId(IdWorker.getId( ));
            record.setOperaterId(account.getAccountId( ));
            record.setAccountType(account.getAccountType( ));
            record.setPhone(account.getPhone( ));
            record.setName(account.getUserName( ));
            record.setTargetType(OperateTargetType.commodity);
            record.setOperateTime(TimeAssist.getNow( ));
            record.setType(OperateType.deleteCommodity);
            record.setMerchantId(account.getMerchantId( ));
            record.setShopId(account.getShopId( ));
            record.setMerchantName(account.getMerchantName( ));
            record.setShopName(account.getShopName( ));
            record.setDescription("管理员删除商品");
            //            record.setDescription("管理员删除商品[" + commodity.getCommodityId() + "]");
            record.setOperateResult("success");
            operateRecordService.addRecord(record);
            return new RetMessage<>(RetCodeEnum.success, "删除成功!", null);
        } else
            return new RetMessage<>(RetCodeEnum.fail, "删除失败!", null);
    }

    @CheckArg
    @Override
    public RetMessage<String> queryCommodities ( String merchantName,
                                                 String commodityName,
                                                 CommodityType commodityType,
                                                 CommodityStatus commodityStatus,
                                                 String timeStart,
                                                 String timeEnd,
                                                 Long commodityId,
                                                 Integer pageNum,
                                                 Integer pageSize,
                                                 Long operaterId ) {
        Account    account = accountService.queryAccountById(operaterId);
        RetMessage message = checkAccount.apply(account, OperateType.queryCommodity);
        if (message != null)
            return message;
        Page<Commodity> page = new Page<>(pageNum, pageSize);
        List<Commodity> list = queryCommoditiesHidden(merchantName, commodityName, commodityType, commodityStatus, timeStart, timeEnd, commodityId, page);
        FillAssist.returnDeal.accept(FillAssist.getMap.apply(AccountType.admin, Commodity.class), list);
        //需要分页信息
        Map<String, Object> map = new HashMap<>( );
        map.put("page", page);
        map.put("list", list);
        if (list != null)
            return new RetMessage<String>(RetCodeEnum.success, "查询成功", ToJson.toJson(map));
        else
            return new RetMessage<String>(RetCodeEnum.fail, "查询失败", null);
    }

    @CheckArg
    @Override
    public RetMessage<File> queryCommoditiesExport ( String merchantName,
                                                     String commodityName,
                                                     CommodityType commodityType,
                                                     CommodityStatus commodityStatus,
                                                     String timeStart,
                                                     String timeEnd,
                                                     Long commodityId,
                                                     Long operaterId ) {
        Account    account = accountService.queryAccountById(operaterId);
        RetMessage message = checkAccount.apply(account, OperateType.exportCommodity);
        if (message != null)
            return message;
        List<Commodity> list = queryCommoditiesHidden(merchantName, commodityName, commodityType, commodityStatus, timeStart, timeEnd, commodityId, null);
        Map<String, String> map  = new HashMap<>( );
        File                file = new File("商品导出" + TimeAssist.getNow( ) + ".xls");
        FileDelete.add(file);
        try {
            ExcelUtil.exportExcelFile(ExcelAssist.toStringList(list, Commodity.class, HiddenMap.getAdminFieldCN), new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace( );
        }
        if (file != null)
            return new RetMessage<File>(RetCodeEnum.success, "导出成功", file);
        else
            return new RetMessage<File>(RetCodeEnum.fail, "导出失败", file);
    }

    @CheckArg
    @Override
    public RetMessage<String> merchantQueryCommodities ( String commodityName,
                                                         CommodityType type,
                                                         CommodityStatus status,
                                                         Integer pageNum,
                                                         Integer pageSize,
                                                         Long commodityId,
                                                         Long operaterId ) {
        Account    account = accountService.queryAccountById(operaterId);
        RetMessage message = checkAccount.apply(account, OperateType.queryCommodity);
        if (message != null)
            return message;
        Merchant merchant = merchantService.queryMerchantParticularsById(account.getMerchantId( ));
        Page<Commodity> page = new Page<>(pageNum, pageSize);
        List<Commodity> list = queryCommoditiesHidden(merchant.getMerchantName( ), commodityName, type, status, null, null, commodityId, page);
        Map<String,String> map1=FillAssist.getMap.apply(AccountType.merchantManager, Commodity.class);
        FillAssist.returnDeal.accept(map1, list);
        //需要分页信息
        Map<String, Object> map = new HashMap<>( );
        map.put("page", page);
        map.put("list", list);
        if (list != null)
            return new RetMessage<String>(RetCodeEnum.success, "查询成功", ToJson.toJson(map));
        else
            return new RetMessage<String>(RetCodeEnum.fail, "查询失败", null);
    }

    @CheckArg
    @Override
    public RetMessage<File> merchantQueryCommoditiesExport ( String commodityName,
                                                             CommodityType type,
                                                             CommodityStatus status,
                                                             Long commodityId,
                                                             Long operaterId ) {
        Account    account = accountService.queryAccountById(operaterId);
        RetMessage message = checkAccount.apply(account, OperateType.exportCommodity);
        if (message != null)
            return message;
        Merchant merchant = merchantService.queryMerchantParticularsById(account.getMerchantId( ));
        message = checkMerchant.apply(merchant, OperateType.exportClearRecord);
        if (notNull.test(message))
            return message;
        List<Commodity> list = queryCommoditiesHidden(merchant.getMerchantName( ), commodityName, type, status, null, null, commodityId, null);
        Map<String, String> map  = new HashMap<>( );
        File                file = new File("商品查询导出" + TimeAssist.getNow( ) + ".xls");
        FileDelete.add(file);
        try {
            ExcelUtil.exportExcelFile(ExcelAssist.toStringList(list, Commodity.class, HiddenMap.getMerchantManagerFieldCN), new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace( );
            file = null;
        }
        if (file != null)
            return new RetMessage<File>(RetCodeEnum.success, "导出成功", file);
        else
            return new RetMessage<File>(RetCodeEnum.fail, "导出失败", file);
    }

    private List<Commodity> queryCommoditiesHidden ( String merchantName,
                                                     String commodityName,
                                                     CommodityType commodityType,
                                                     CommodityStatus commodityStatus,
                                                     String timeStart,
                                                     String timeEnd,
                                                     Long commodityId,
                                                     Page<Commodity> page ) {
        EntityWrapper<Commodity> wrapper = new EntityWrapper<>( );
        wrapper.orderBy("add_time",false);
        if (timeStart != null && ! timeStart.isEmpty( )) {
            while (timeStart.length( ) < 14)
                timeStart += "0";
            wrapper.ge("add_time", timeStart);
        }
        if (timeEnd != null && ! timeEnd.isEmpty( )) {
            while (timeEnd.length( ) < 14)
                timeEnd += "9";
            wrapper.le("add_time", timeEnd);
        }
        if (merchantName != null)
            wrapper.like("merchant_name", merchantName);
        if (commodityName != null)
            wrapper.like("commodity_name", commodityName);
        if (commodityType != null)
            wrapper.eq("type", commodityType.getValue( ));
        if (commodityStatus != null)
            wrapper.eq("status", commodityStatus.getValue( ));
        Optional.ofNullable(commodityId).ifPresent(e -> wrapper.eq("commodity_id", e));
        wrapper.ne("status", CommodityStatus.deleted.getValue( ));
        return commodityService.queryCommodities(wrapper, page);
    }

    @CheckArg
    @Override
    public RetMessage<Serializable> queryAllCommodityIds ( Long operaterId ) {
        Account account = accountService.queryAccountById(operaterId);
        if (isAdmin.negate( ).test(account) && isMerchantManager.negate( ).test(account))
            return new RetMessage<>(RetCodeEnum.fail, "非管理员商户管理员无权操作!", null);
        Map<Long, String> map = new HashMap<>( );
        commodityService.queryAll( )
                        .parallelStream( )
                        .filter(e -> {
                            if (isMerchantManager.test(account))
                               return e.getMerchantId( ).equals(account.getMerchantId( ));
                            else
                                return true;
                        })
                        .filter(e->! CommodityStatus.deleted.equals(e.getStatus( )))
                        .forEach(e -> map.put(e.getCommodityId( ), e.getCommodityName( )));
        return new RetMessage<>(RetCodeEnum.success, "查询成功!", (Serializable) map);
    }

    @CheckArg
    @Override
    public RetMessage<Serializable> queryAllCommodityIds (Long merchantId,CommodityStatus status, Long operaterId) {
        Account account = accountService.queryAccountById(operaterId);
        if (isAdmin.negate( ).test(account)&&isMerchantManager.negate().test(account))
            return new RetMessage<>(RetCodeEnum.fail, "非管理员商户管理员无权操作!", null);
        Map<Long, String> map = new HashMap<>( );
        commodityService.queryAll( )
                        .parallelStream( )
                        .filter(e->{
                            if(isMerchantManager.test(account)){
                                return e.getMerchantId().equals(account.getMerchantId());
                            }else{
                                return true;
                            }
                        })
                        .filter(e-> merchantId==null||e.getMerchantId().equals(merchantId))
                        .filter(e ->status==null|| status.equals( e.getStatus( )))
                        .filter(e->!CommodityStatus.deleted.equals(e.getStatus()))
                        .forEach(e -> map.put(e.getCommodityId( ), e.getCommodityName( )));
        return new RetMessage<>(RetCodeEnum.success, "查询成功!", (Serializable) map);
    }

    @Override
    public RetMessage<String> queryCommodity ( Long commodityId, Long operaterId ) {
        Account   account   = accountService.queryAccountById(operaterId);
        Commodity commodity = commodityService.queryCommodity(commodityId);
        if (isAdmin.test(account))
            FillAssist.returnDeal.accept(FillAssist.getMap.apply(AccountType.merchantManager, Commodity.class), commodity);
        else if (isMerchantManager.test(account))
            FillAssist.returnDeal.accept(FillAssist.getMap.apply(AccountType.merchantManager, Commodity.class), commodity);
        else
            FillAssist.returnDeal.accept(FillAssist.getMap.apply(AccountType.shopManager, Commodity.class), commodity);

        return new RetMessage<>(RetCodeEnum.success, "查询成功!", commodity.toJson( ));
    }
}
