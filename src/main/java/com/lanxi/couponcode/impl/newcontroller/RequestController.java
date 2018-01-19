package com.lanxi.couponcode.impl.newcontroller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.lanxi.couponcode.impl.assist.FillAssist;
import com.lanxi.couponcode.spi.assist.ArgAssist;
import com.lanxi.couponcode.spi.assist.TimeAssist;
import com.lanxi.couponcode.impl.entity.*;
import com.lanxi.couponcode.impl.newservice.*;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.impl.config.HiddenMap;
import com.lanxi.couponcode.spi.consts.annotations.CheckArg;
import com.lanxi.couponcode.spi.consts.annotations.Comment;
import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.couponcode.spi.consts.enums.*;
import com.lanxi.couponcode.spi.defaultInterfaces.ToJson;
import com.lanxi.couponcode.spi.service.RedisService;
import com.lanxi.util.entity.LogFactory;
import com.lanxi.util.utils.BeanUtil;
import com.lanxi.util.utils.LoggerUtil;
import com.lanxi.util.utils.TimeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.test.annotation.Commit;

import static com.lanxi.couponcode.impl.assist.PredicateAssist.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by yangyuanjian on 2017/11/20.
 */
@CheckArg
@Controller ("requestControllerService")
@EasyLog (LoggerUtil.LogLevel.INFO)
public class RequestController implements com.lanxi.couponcode.spi.service.RequestService {
    @Resource
    private RequestService       requestService;
    @Resource
    private RedisService         redisService;
    @Resource
    private RedisEnhancedService redisEnhancedService;
    @Resource
    private CommodityService     commodityService;
    @Resource
    private AccountService       accountService;
    @Resource
    private MerchantService      merchantService;
    @Resource
    private OperateRecordService recordService;
    @Resource
    private OperateRecordService operateRecordService;
    @Resource
    private LockService          lockService;

    private List<Request> queryRequestsHidden ( String timeStart,
                                                String timeStop,
                                                String commodityName,
                                                String merchantName,
                                                RequestOperateType type,
                                                RequestStatus status,
                                                CommodityType commodityType,
                                                Long merchantId,
                                                Long commodityId,
                                                Page<Request> page ) {
        EntityWrapper<Request> wrapper = new EntityWrapper<>( );
        wrapper.orderBy("request_time",false);
        if (timeStart != null && ! timeStart.isEmpty( )) {
            while (timeStart.length( ) < 14)
                timeStart += "0";
            wrapper.ge("request_time", timeStart);
        }
        if (timeStop != null && ! timeStop.isEmpty( )) {
            while (timeStop.length( ) < 14)
                timeStop += "9";
            wrapper.le("request_time", timeStop);
        }
        if (commodityName != null)
            wrapper.like("commodity_name", commodityName);
        if (merchantName != null)
            wrapper.like("merchant_name", merchantName);
        if (type != null)
            wrapper.eq("type", type.getValue( ));
        if (status != null)
            wrapper.eq("status", status.getValue( ));
        if (commodityType != null)
            wrapper.eq("commodity_type", commodityType.getValue( ));
        if (commodityId != null)
            wrapper.eq("commodity_id", commodityId);
        Optional.ofNullable(merchantId).ifPresent(e -> wrapper.eq("merchant_id", e));
        return requestService.queryRequestInfos(wrapper, page);
    }

    private Request makeRequest ( Account account, Merchant merchant, Commodity commodity ) {
        //组装请求
        Request request = new Request( );
        request.setRequestId(IdWorker.getId( ));
        request.setRequestTime(TimeUtil.getDateTime( ));
        request.setRequesterId(account.getAccountId( ));
        request.setRequesterName(account.getUserName( ));
        request.setRequesterPhone(account.getPhone( ));
        //        request.setType(RequestOperateType.createCommodity);
        request.setStatus(RequestStatus.submit);
        request.setCommodityId(commodity.getCommodityId( ));
        request.setCommodityType(CommodityType.getType(commodity.getType( )));
        request.setCommodityName(commodity.getCommodityName( ));
        request.setMerchantId(merchant.getMerchantId( ));
        request.setMerchantName(merchant.getMerchantName( ));
        request.setCommodityInfo(FillAssist.keepEntityField(HiddenMap.REQUEST_COMMODITY_ADMIN.keySet( ), BeanUtil.deepCopy(commodity)).toJson( ));
        return request;
    }

    @Override
    public RetMessage<String> queryRequests ( String timeStart,
                                              String timeStop,
                                              String commodityName,
                                              String merchantName,
                                              RequestOperateType type,
                                              RequestStatus status,
                                              CommodityType commodityType,
                                              Long commodityId,
                                              Integer pageNum,
                                              Integer pageSize,
                                              Long operaterId ) {
        Account    account = accountService.queryAccountById(operaterId);
        RetMessage message = checkAccount.apply(account, OperateType.queryRequest);
        if (message != null)
            return message;
        Page<Request> page = new Page<>(pageNum, pageSize);
        List<Request> list = queryRequestsHidden(timeStart, timeStop, commodityName, merchantName, type, status, commodityType, null, commodityId, page);
        FillAssist.returnDeal.accept(HiddenMap.ADMIN_REQUEST, list);
        Map<String, Object> map = new HashMap<>( );
        map.put("page", page);
        map.put("list", list);
        if (list != null)
            return new RetMessage<String>(RetCodeEnum.success, "查询成功", ToJson.toJson(map));
        else
            return new RetMessage<String>(RetCodeEnum.fail, "查询失败", null);
    }

    @Override
    public RetMessage<String> queryRequests ( String timeStart,
                                              String timeStop,
                                              String commodityName,
                                              RequestOperateType type,
                                              RequestStatus status,
                                              CommodityType commodityType,
                                              Long merchantId,
                                              Integer pageNum,
                                              Integer pageSize,
                                              Long operaterId ) {
        Account account = accountService.queryAccountById(operaterId);
        return queryRequests(timeStart, timeStop, commodityName, account.getMerchantName( ), type, status, commodityType, account.getMerchantId( ), pageNum, pageSize, operaterId);
    }


    @Override
    public RetMessage<Boolean> agreeRequest ( Long requestId,
                                              Long operaterId,
                                              @Comment ("该参数在审核通过时,传入的是销售价格") String reason ) {
        Account    account = accountService.queryAccountById(operaterId);
        RetMessage message = checkAccount.apply(account, OperateType.passRequest);
        if (message != null)
            return message;
        Request request = requestService.queryRequestInfo(requestId);
        Boolean lock    = null;
        try {
//            lock = lockService.lock(request);
//            if (lock == null || ! lock)
//                return new RetMessage<>(RetCodeEnum.fail, "请求不存在或正被处理,拒绝失败!", null);
            message = checkRequest.apply(request, OperateType.passRequest);
            if (message != null)
                return message;
            Merchant merchant = merchantService.queryMerchantParticularsById(request.getMerchantId( ));
            message = checkMerchant.apply(merchant, OperateType.passRequest);
            if (message != null)
                return message;
            String    commodityJson = request.getCommodityInfo( );
            Commodity commodity     = JSON.parseObject(commodityJson, Commodity.class);
            commodity.setType(JSON.parseObject(commodityJson).getString("type"));
            if (! RequestOperateType.createCommodity.equals(request.getType( ))) {
                Long commodityId = commodity.getCommodityId( );
                if (commodityId == null)
                    return new RetMessage<>(RetCodeEnum.fail, "非添加操作,商品编号不能为空!", null);
                message = checkCommodity.apply(commodityService.queryCommodity(commodityId), OperateType.getType(request.getType( )));
                if (message != null)
                    return message;
            } else {
                commodity.setCommodityId(IdWorker.getId( ));
                request.setCommodityId(commodity.getCommodityId( ));
            }

            if (RequestOperateType.createCommodity.equals(request.getType( )) || RequestOperateType.shelveCommodity.equals(request.getType( ))) {
                @Comment ("调整销售价以通过添加申请时传入的值为准") Object obj = null;
                if (reason != null)
                    commodity.setSellPrice(new BigDecimal(reason == null || reason.isEmpty( ) ? "0" : reason));
                if (commodity.getSellPrice( ) == null)
                    return new RetMessage<>(RetCodeEnum.fail, "销售价格异常!", null);
            }
            //配置请求
            request.setStatus(RequestStatus.pass);
            request.setCheckerId(account.getAccountId( ));
            request.setCheckerName(account.getUserName( ));
            request.setCheckerPhone(account.getPhone( ));
            request.setCheckTime(TimeUtil.getDateTime( ));
            request.setCommodityName(commodity.getCommodityName( ));
            request.setReason(reason);
            //TODO 通过时将reason置为null
            request.setReason(null);
            //更新售价到请求的商品信息里
            request.setCommodityInfo(FillAssist.keepEntityField(HiddenMap.REQUEST_COMMODITY_ADMIN.keySet(),BeanUtil.deepCopy(commodity)).toJson());
            Boolean result = requestService.passRequest(request);


            if (result == null)
                return new RetMessage<>(RetCodeEnum.fail, "通过时异常!", null);
            if (result) {
                OperateRecord record = new OperateRecord( );
                record.setRecordId(IdWorker.getId( ));
                record.setOperaterId(account.getAccountId( ));
                record.setAccountType(account.getAccountType( ));
                record.setPhone(account.getPhone( ));
                record.setName(account.getUserName( ));
                record.setTargetType(OperateTargetType.request);
                record.setType(OperateType.passRequest);
                record.setOperateTime(TimeAssist.getNow( ));
                record.setMerchantId(account.getMerchantId( ));
                record.setShopId(account.getShopId( ));
                record.setOperateResult("success");
                record.setMerchantName(account.getMerchantName( ));
                record.setShopName(account.getShopName( ));
                //                record.setDescription("通过申请[" + request.getRequestId() + "]");
                record.setDescription("通过申请");
                operateRecordService.addRecord(record);

                Account adder = accountService.queryAccountById(request.getRequesterId( ));
                record = new OperateRecord( );
                record.setRecordId(IdWorker.getId( ));
                record.setOperaterId(adder.getAccountId( ));
                record.setAccountType(adder.getAccountType( ));
                record.setPhone(adder.getPhone( ));
                record.setName(adder.getUserName( ));
                record.setTargetType(OperateTargetType.commodity);
                record.setOperateTime(TimeAssist.getNow( ));
                record.setOperateResult("success");

                boolean flag = false;
                commodity.setRemark(commodity.getRemark( ) + "," + request.getRequesterId( ));
                switch (RequestOperateType.getType(request.getType( ))) {
                    case createCommodity: {
                        commodity.setRemark(request.getRequesterId( ) + "");
                        record.setType(OperateType.createCommodity);
                        record.setDescription("添加商品");
                        //                        record.setDescription("添加商品[" + commodity.getCommodityId() + "]");
                        flag = commodityService.addCommodity(commodity);
                    }
                    ;
                    break;
                    case modifyCommodity: {
                        record.setType(OperateType.modifyCommodity);
                        record.setDescription("修改商品");
                        //                        record.setDescription("修改商品[" + commodity.getCommodityId() + "]");
                        flag = commodityService.modifyCommodity(commodity);
                    }
                    ;
                    break;
                    case unshelveCommodity: {
                        record.setType(OperateType.unshelveCommodity);
                        record.setDescription("下架商品");
                        //                        record.setDescription("下架商品[" + commodity.getCommodityId() + "]");
                        flag = commodityService.unshelveCommodity(commodity);
                    }
                    ;
                    break;
                    case shelveCommodity: {
                        record.setType(OperateType.shelveCommodity);
                        record.setDescription("上架商品");
                        //                        record.setDescription("上架商品[" + commodity.getCommodityId() + "]");
                        flag = commodityService.shelveCommodity(commodity);
                    }
                    ;
                    break;
                    case deleteCommodity: {
                        record.setType(OperateType.deleteCommodity);
                        record.setDescription("删除商品");
                        //                        record.setDescription("删除商品[" + commodity.getCommodityId() + "]");
                        flag = commodityService.delCommodity(commodity);
                    }
                    case cancelCommodity:
                        throw new UnsupportedOperationException("不支持注销商品");
                    default:
                        ;
                }
                if (flag) {
                    //                    record.setDescription("success");
                    recordService.addRecord(record);
                    return new RetMessage<>(RetCodeEnum.success, "通过成功,操作成功!", null);
                } else {
                    //                    record.setDescription("fail");
                    return new RetMessage<>(RetCodeEnum.success, "通过成功!操作失败", null);
                }
            }
            return new RetMessage<>(RetCodeEnum.fail, "通过失败!", null);
            //            return new RetMessage<>(RetCodeEnum.exception,"no thing to do!",null);
        } catch (Exception e) {
            LogFactory.info(this, "通过请求异常!", e);
            return new RetMessage<>(RetCodeEnum.error, "系统异常!", null);
        } finally {
//            if (lock != null)
//                lockService.unlock(request);
        }
    }

    @Override
    public RetMessage<Boolean> disagreeRequest ( Long requestId,
                                                 Long operaterId,
                                                 String reason ) {
        Account    account = accountService.queryAccountById(operaterId);
        RetMessage message = checkAccount.apply(account, OperateType.rejectRequest);
        if (message != null)
            return message;
        Request request = requestService.queryRequestInfo(requestId);
        Commodity commodity=commodityService.queryCommodity(request.getCommodityId());
        if(CommodityStatus.shelvedWait.equals(commodity.getStatus())){
            commodity.setStatus(CommodityStatus.unshelved);
        }
        if(CommodityStatus.unshelvedWait.equals(commodity.getStatus())){
            commodity.setStatus(CommodityStatus.shelved);
        }
        commodity.updateById();
        // 不加锁
        Boolean lock    = commodity.updateById();
        try {
//            lock = lockService.lock(request);
//            lock=true;
            if (lock == null || ! lock)
                return new RetMessage<>(RetCodeEnum.fail, "请求不存在或正被处理,拒绝失败!", null);
            message = checkRequest.apply(request, OperateType.rejectRequest);
            if (message != null)
                return message;
            Merchant merchant = merchantService.queryMerchantParticularsById(request.getMerchantId( ));
            message = checkMerchant.apply(merchant, OperateType.rejectRequest);
            if (message != null)
                return message;
            //配置请求
            request.setStatus(RequestStatus.reject);
            request.setCheckerId(account.getAccountId( ));
            request.setCheckerName(account.getUserName( ));
            request.setCheckerPhone(account.getPhone( ));
            request.setCheckTime(TimeUtil.getDateTime( ));
            request.setReason(reason);
            Boolean result = requestService.rejectRequest(request);
            if (result == null)
                return new RetMessage<>(RetCodeEnum.fail, "拒绝时异常!", null);
            if (result) {
                OperateRecord record = new OperateRecord( );
                record.setRecordId(IdWorker.getId( ));
                record.setOperaterId(account.getAccountId( ));
                record.setAccountType(account.getAccountType( ));
                record.setPhone(account.getPhone( ));
                record.setName(account.getUserName( ));
                record.setTargetType(OperateTargetType.request);
                record.setType(OperateType.rejectRequest);
                record.setOperateTime(TimeAssist.getNow( ));
                record.setOperateResult("success");
                record.setMerchantName(account.getMerchantName( ));
                record.setShopName(account.getShopName( ));
                record.setDescription("驳回申请");
                //                record.setDescription("驳回申请[" + request.getRequestId() + "]");
                operateRecordService.addRecord(record);
                return new RetMessage<>(RetCodeEnum.success, "拒绝成功!", null);
            }
            return new RetMessage<>(RetCodeEnum.fail, "拒绝失败!", null);
            //            return new RetMessage<>(RetCodeEnum.exception,"no thing to do!",null);
        } catch (Exception e) {
            LogFactory.info(this, "驳回请求异常!", e);
            return new RetMessage<>(RetCodeEnum.error, "系统异常!", null);
        } finally {
            if (lock != null && lock)
                lockService.unlock(request);
        }

    }

    @Override
    public RetMessage<Boolean> requestAddCommodity ( String commodityName,
                                                     CommodityType commodityType,
                                                     BigDecimal facePrice,
                                                     BigDecimal costPrice,
                                                     BigDecimal sellPrice,
                                                     Integer lifeTime,
                                                     String useDistription,
                                                     Long operaterId,
                                                     @Deprecated Long merchantId ) {

        Account    account = accountService.queryAccountById(operaterId);
        RetMessage message = checkAccount.apply(account, OperateType.requestAddCommodity);
        if (message != null)
            return message;
        Merchant merchant = merchantService.queryMerchantParticularsById(account.getMerchantId( ));
        message = checkMerchant.apply(merchant, OperateType.requestAddCommodity);
        if (message != null)
            return message;
        //组装商品
        Commodity commodity = new Commodity( );
        commodity.setAddId(IdWorker.getId( ));
        commodity.setAddName(commodityName);
        commodity.setType(commodityType);
        commodity.setFacePrice(facePrice);
        commodity.setSellPrice(sellPrice);
        commodity.setCostPrice(costPrice);
        commodity.setStatus(CommodityStatus.unshelved);
        commodity.setLifeTime(lifeTime);

        commodity.setMerchantId(merchant.getMerchantId( ));
        commodity.setMerchantName(merchant.getMerchantName( ));

        commodity.setDescription(useDistription);
        commodity.setUseDetail(useDistription);

        commodity.setAddId(account.getAccountId( ));
        commodity.setAddTime(TimeUtil.getDateTime( ));
        commodity.setAddName(account.getUserName( ));
        //组装请求
        Request request = makeRequest(account, merchant, commodity);
        request.setType(RequestOperateType.createCommodity);
        Boolean result = requestService.addRequest(request);
        if (result == null)
            return new RetMessage<>(RetCodeEnum.success, "添加时异常!", null);
        if (result) {
            OperateRecord record = new OperateRecord( );
            record.setRecordId(IdWorker.getId( ));
            record.setOperaterId(account.getAccountId( ));
            record.setAccountType(account.getAccountType( ));
            record.setPhone(account.getPhone( ));
            record.setName(account.getUserName( ));
            record.setTargetType(OperateTargetType.request);
            record.setType(OperateType.requestAddCommodity);
            record.setOperateTime(TimeAssist.getNow( ));
            record.setMerchantId(account.getMerchantId( ));
            record.setShopId(account.getShopId( ));
            record.setMerchantName(account.getMerchantName( ));
            record.setShopName(account.getShopName( ));
            recordService.addRecord(record);
            return new RetMessage<>(RetCodeEnum.success, "添加成功!", null);
        } else
            return new RetMessage<>(RetCodeEnum.success, "添加失败!", null);
    }

    @Override
    public RetMessage<Boolean> requestModifyCommodity ( BigDecimal costPrice,
                                                        BigDecimal facePrice,
                                                        BigDecimal sellPrice,
                                                        Integer lifeTime,
                                                        Long commodityId,
                                                        Long operaterId ) {
        Account    account = accountService.queryAccountById(operaterId);
        RetMessage message = checkAccount.apply(account, OperateType.requestModifyCommodity);
        if (message != null)
            return message;
        Merchant merchant = merchantService.queryMerchantParticularsById(account.getMerchantId( ));
        message = checkMerchant.apply(merchant, OperateType.requestModifyCommodity);
        if (message != null)
            return message;
        //修改商品
        Commodity commodity = commodityService.queryCommodity(commodityId);
        message = checkCommodity.apply(commodityService.queryCommodity(commodityId), OperateType.requestModifyCommodity);
        if (message != null)
            return message;
        Optional.ofNullable(costPrice).ifPresent(e -> commodity.setCostPrice(e));
        Optional.ofNullable(facePrice).ifPresent(e -> commodity.setFacePrice(e));
        Optional.ofNullable(sellPrice).ifPresent(e -> commodity.setSellPrice(e));
        Optional.ofNullable(lifeTime).ifPresent(e -> commodity.setLifeTime(e));
        //组装请求
        Request request = makeRequest(account, merchant, commodity);
        request.setType(RequestOperateType.modifyCommodity);
        Boolean result = requestService.addRequest(request);
        if (result == null)
            return new RetMessage<>(RetCodeEnum.success, "添加时异常!", null);
        if (result) {
            OperateRecord record = new OperateRecord( );
            record.setRecordId(IdWorker.getId( ));
            record.setOperaterId(account.getAccountId( ));
            record.setAccountType(account.getAccountType( ));
            record.setPhone(account.getPhone( ));
            record.setName(account.getUserName( ));
            record.setTargetType(OperateTargetType.request);
            record.setMerchantId(account.getMerchantId( ));
            record.setShopId(account.getShopId( ));
            record.setMerchantName(account.getMerchantName( ));
            record.setShopName(account.getShopName( ));
            record.setType(OperateType.requestModifyCommodity);
            record.setOperateTime(TimeAssist.getNow( ));
            recordService.addRecord(record);
            return new RetMessage<>(RetCodeEnum.success, "添加成功!", null);
        } else
            return new RetMessage<>(RetCodeEnum.success, "添加失败!", null);
    }

    @Override
    public RetMessage<Boolean> requestShelveCommodity ( Long commodityId,
                                                        Long operaterId ) {
        Account    account = accountService.queryAccountById(operaterId);
        RetMessage message = checkAccount.apply(account, OperateType.requestShelveCommodity);
        if (message != null)
            return message;
        Merchant merchant = merchantService.queryMerchantParticularsById(account.getMerchantId( ));
        message = checkMerchant.apply(merchant, OperateType.requestShelveCommodity);
        if (message != null)
            return message;
        //修改商品
        Commodity commodity = commodityService.queryCommodity(commodityId);
        message = checkCommodity.apply(commodityService.queryCommodity(commodityId), OperateType.requestShelveCommodity);
        if (message != null)
            return message;
        commodity.setStatus(CommodityStatus.shelved);
        //组装请求
        Request request = makeRequest(account, merchant, commodity);
        //todo 新增上架待审核
        commodity.setStatus(CommodityStatus.shelvedWait);
        commodityService.modifyCommodity(commodity);
        request.setType(RequestOperateType.shelveCommodity);
        Boolean result = requestService.addRequest(request);
        if (result == null)
            return new RetMessage<>(RetCodeEnum.success, "添加时异常!", null);
        if (result) {
            OperateRecord record = new OperateRecord( );
            record.setRecordId(IdWorker.getId( ));
            record.setOperaterId(account.getAccountId( ));
            record.setAccountType(account.getAccountType( ));
            record.setPhone(account.getPhone( ));
            record.setName(account.getUserName( ));
            record.setMerchantId(account.getMerchantId( ));
            record.setShopId(account.getShopId( ));
            record.setMerchantName(account.getMerchantName( ));
            record.setShopName(account.getShopName( ));
            record.setTargetType(OperateTargetType.request);
            record.setType(OperateType.requestShelveCommodity);
            record.setOperateTime(TimeAssist.getNow( ));
            return new RetMessage<>(RetCodeEnum.success, "添加成功!", null);
        } else
            return new RetMessage<>(RetCodeEnum.success, "添加失败!", null);
    }

    @Override
    public RetMessage<Boolean> requestUnshelveCommodity ( Long commodityId,
                                                          Long operaterId ) {
        Account    account = accountService.queryAccountById(operaterId);
        RetMessage message = checkAccount.apply(account, OperateType.requestUnshelveCommodity);
        if (message != null)
            return message;
        Merchant merchant = merchantService.queryMerchantParticularsById(account.getMerchantId( ));
        message = checkMerchant.apply(merchant, OperateType.requestUnshelveCommodity);
        if (message != null)
            return message;
        //修改商品
        Commodity commodity = commodityService.queryCommodity(commodityId);
        message = checkCommodity.apply(commodityService.queryCommodity(commodityId), OperateType.requestUnshelveCommodity);
        if (message != null)
            return message;
        //todo 新增下架待审核
        commodity.setStatus(CommodityStatus.unshelvedWait);
        commodityService.modifyCommodity(commodity);
        Request request = makeRequest(account, merchant, commodity);
        request.setType(RequestOperateType.unshelveCommodity);
        Boolean result = requestService.addRequest(request);
        if (result == null)
            return new RetMessage<>(RetCodeEnum.success, "添加时异常!", null);
        if (result) {
            OperateRecord record = new OperateRecord( );
            record.setRecordId(IdWorker.getId( ));
            record.setOperaterId(account.getAccountId( ));
            record.setAccountType(account.getAccountType( ));
            record.setPhone(account.getPhone( ));
            record.setName(account.getUserName( ));
            record.setMerchantId(account.getMerchantId( ));
            record.setShopId(account.getShopId( ));
            record.setMerchantName(account.getMerchantName( ));
            record.setShopName(account.getShopName( ));
            record.setTargetType(OperateTargetType.request);
            record.setType(OperateType.requestUnshelveCommodity);
            record.setOperateTime(TimeAssist.getNow( ));
            return new RetMessage<>(RetCodeEnum.success, "添加成功!", null);
        } else
            return new RetMessage<>(RetCodeEnum.success, "添加失败!", null);
    }

    @Override
    public RetMessage<Boolean> requestDelCommodity ( Long commodityId, Long operaterId ) {
        Account    account = accountService.queryAccountById(operaterId);
        RetMessage message = checkAccount.apply(account, OperateType.requestDelCommodity);
        if (message != null)
            return message;
        Merchant merchant = merchantService.queryMerchantParticularsById(account.getMerchantId( ));
        message = checkMerchant.apply(merchant, OperateType.requestDelCommodity);
        if (message != null)
            return message;
        //修改商品
        Commodity commodity = commodityService.queryCommodity(commodityId);
        message = checkCommodity.apply(commodityService.queryCommodity(commodityId), OperateType.requestDelCommodity);
        if (message != null)
            return message;
        Request request = makeRequest(account, merchant, commodity);
        request.setType(RequestOperateType.deleteCommodity);
        Boolean result = requestService.addRequest(request);
        if (result == null)
            return new RetMessage<>(RetCodeEnum.success, "添加时异常!", null);
        if (result) {
            OperateRecord record = new OperateRecord( );
            record.setRecordId(IdWorker.getId( ));
            record.setOperaterId(account.getAccountId( ));
            record.setAccountType(account.getAccountType( ));
            record.setPhone(account.getPhone( ));
            record.setName(account.getUserName( ));
            record.setTargetType(OperateTargetType.request);
            record.setType(OperateType.requestDelCommodity);
            record.setMerchantId(account.getMerchantId( ));
            record.setShopId(account.getShopId( ));
            record.setMerchantName(account.getMerchantName( ));
            record.setShopName(account.getShopName( ));
            record.setOperateTime(TimeAssist.getNow( ));
            return new RetMessage<>(RetCodeEnum.success, "添加成功!", null);
        } else
            return new RetMessage<>(RetCodeEnum.success, "添加失败!", null);
    }


    @Override
    public RetMessage<String> queryCommodityRequest ( @Comment("实际传入的为商品编号") String commodityName,
                                                      CommodityType type,
                                                      RequestOperateType operateType,
                                                      RequestStatus status,
                                                      String timeStart,
                                                      String timeEnd,
                                                      Integer pageNum,
                                                      Integer pageSize,
                                                      Long operaterId ) {
        Account    account = accountService.queryAccountById(operaterId);
        RetMessage message = checkAccount.apply(account, OperateType.queryRequest);
        if (message != null)
            return message;
        Merchant merchant = merchantService.queryMerchantParticularsById(account.getMerchantId( ));
        message = checkMerchant.apply(merchant, OperateType.queryRequest);
        if (message != null)
            return message;
        Page<Request> page = new Page<>(pageNum, pageSize);
        List<Request> list = queryRequestsHidden(timeStart, timeEnd, commodityName, merchant.getMerchantName( ), operateType, status, type, merchant.getMerchantId( ), null, page);
        FillAssist.returnDeal.accept(FillAssist.getMap.apply(AccountType.merchantManager, Request.class), list);
        Map<String, Object> map = new HashMap<>( );
        map.put("page", page);
        map.put("list", list);
        if (list != null)
            return new RetMessage<String>(RetCodeEnum.success, "查询成功", ToJson.toJson(map));
        else
            return new RetMessage<String>(RetCodeEnum.fail, "查询失败", null);
    }

    @Override
    public RetMessage<String> queryRequest ( Long requestId, Long operatorId ) {
        Account    account = accountService.queryAccountById(operatorId);
        RetMessage message = checkAccount.apply(account, OperateType.queryRequest);
        if (message != null)
            return message;
        Request request = requestService.queryRequestInfo(requestId);
        if (isAdmin.test(account))
            FillAssist.returnDeal.accept(FillAssist.getMap.apply(AccountType.admin, Request.class), request);
        else if (isMerchantManager.test(account))
            FillAssist.returnDeal.accept(FillAssist.getMap.apply(AccountType.merchantManager, Request.class), request);
        return new RetMessage<>(RetCodeEnum.success, "查询成功!", request == null ? null : request.toJson( ));
    }
}
