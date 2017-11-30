package com.lanxi.couponcode.impl.newcontroller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.lanxi.couponcode.impl.assist.TimeAssist;
import com.lanxi.couponcode.impl.entity.*;
import com.lanxi.couponcode.impl.newservice.*;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.enums.*;
import com.lanxi.util.entity.LogFactory;
import com.lanxi.util.utils.TimeUtil;
import com.sun.org.apache.regexp.internal.RE;
import org.aspectj.apache.bcel.generic.RET;
import org.springframework.stereotype.Controller;
import static com.lanxi.couponcode.impl.assist.PredicateAssist.*;
import javax.activation.CommandInfo;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangyuanjian on 2017/11/20.
 */
@Controller
public class RequestController implements com.lanxi.couponcode.spi.service.RequestService{
    @Resource
    private RequestService requestService;
    @Resource
    private RedisService redisService;
    @Resource
    private RedisEnhancedService redisEnhancedService;
    @Resource
    private CommodityService commodityService;
    @Resource
    private AccountService accountService;
    @Resource
    private MerchantService merchantService;
    @Resource
    private OperateRecordService recordService;
    @Resource
    private OperateRecordService operateRecordService;
    @Resource
    private LockService lockService;

    private List<Request> queryRequestsHidden(String timeStart,
                                              String timeStop,
                                              String commodityName,
                                              String merchantName,
                                              RequestOperateType type,
                                              RequestStatus status,
                                              CommodityType commodityType,
                                              Long merchantId,
                                              Page<Request> page){
        EntityWrapper<Request> wrapper= new EntityWrapper<>();
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
        if(commodityName!=null)
            wrapper.like("commodity_name",commodityName);
        if(merchantName!=null)
            wrapper.like("merchant_name",merchantName);
        if(type!=null)
            wrapper.eq("type",type.getValue());
        if(status!=null)
            wrapper.eq("status",status.getValue());
        if(commodityType!=null)
            wrapper.eq("commodity_type",commodityType.getValue());
        if(merchantId!=null)
            wrapper.eq("merchant_id",merchantId);
        return requestService.queryRequestInfos(wrapper,page);
    }

    private Request makeRequest(Account account,Merchant merchant,Commodity commodity){
        //组装请求
        Request request=new Request();
        request.setRquestId(IdWorker.getId());
        request.setRequestTime(TimeUtil.getDateTime());
        request.setRequesterId(account.getAccountId());
        request.setRequesterName(account.getUserName());
        request.setRequesterPhone(account.getPhone());
//        request.setType(RequestOperateType.createCommodity);
        request.setCommodityInfo(commodity.toJson());
        request.setStatus(RequestStatus.submit);
        request.setCommodityId(commodity.getCommodityId());
        request.setCommodityType(CommodityType.getType(commodity.getType()));
        request.setMerchantId(merchant.getMerchantId());
        request.setMerchantName(merchant.getMerchantName());
        return  request;
    }

    @Override
    public RetMessage<String> queryRequests(String timeStart,
                                            String timeStop,
                                            String commodityName,
                                            String merchantName,
                                            RequestOperateType type,
                                            RequestStatus status,
                                            CommodityType commodityType,
                                            Long merchantId,
                                            Integer pageNum,
                                            Integer pageSize,
                                            Long operaterId) {
        Account account=accountService.queryAccountById(operaterId);
        RetMessage message=checkAccount.apply(account,OperateType.queryRequest);
        if(message!=null)
            return message;
        Page<Request> page=new Page<>(pageNum,pageSize);
        List<Request> list=queryRequestsHidden(timeStart,timeStop,commodityName,merchantName,type,status,commodityType,merchantId,page);
        Map<String,Object> map=new HashMap<>();
        map.put("page",page);
        map.put("list",list);
        if(list!=null)
            return new RetMessage<String>(RetCodeEnum.success,"查询成功", JSON.toJSONString(map));
        else
            return new RetMessage<String>(RetCodeEnum.fail,"查询失败",null);
    }
    @Override
    public RetMessage<String> queryRequests(String timeStart,
                                            String timeStop,
                                            String commodityName,
                                            RequestOperateType type,
                                            RequestStatus status,
                                            CommodityType commodityType,
                                            Long merchantId,
                                            Integer pageNum,
                                            Integer pageSize,
                                            Long operaterId) {
        Account account=accountService.queryAccountById(operaterId);
        return queryRequests(timeStart,timeStop,commodityName, account.getMerchantName(),type,status,commodityType, account.getMerchantId(),pageNum,pageSize,operaterId);
    }


    @Override
    public RetMessage<Boolean> agreeRequest(Long requestId,
                                            Long operaterId,
                                            String reason) {
        Account account=accountService.queryAccountById(operaterId);
        RetMessage message=checkAccount.apply(account,OperateType.passRequest);
        if(message!=null)
            return message;
        Request request=requestService.queryRequestInfo(requestId);
        Boolean lock=null;
        try {
            lock=lockService.lock(request);
            if(lock==null||!lock)
                return new RetMessage<>(RetCodeEnum.fail,"请求不存在或正被处理,拒绝失败!",null);
            message=checkRequest.apply(request,OperateType.passRequest);
            if(message!=null)
                return message;
            Merchant merchant=merchantService.queryMerchantParticularsById(account.getMerchantId());
            message=checkMerchant.apply(merchant,OperateType.passRequest);
            if(message!=null)
                return message;
            String commodityJson=request.getCommodityInfo();
            Commodity commodity=JSON.parseObject(commodityJson,Commodity.class);
            if(!RequestOperateType.createCommodity.equals(request.getType())){
                Long commodityId=commodity.getCommodityId();
                if(commodityId==null)
                    return new RetMessage<>(RetCodeEnum.fail,"非添加操作,商品编号不能为空!",null);
                message=checkCommodity.apply(commodityService.queryCommodity(commodityId),OperateType.getType(request.getType()));
                if(message!=null)
                    return message;
            }
            //配置请求
            request.setStatus(RequestStatus.pass);
            request.setCheckerId(account.getAccountId());
            request.setCheckerName(account.getUserName());
            request.setCheckerPhone(account.getPhone());
            request.setCheckTime(TimeUtil.getDateTime());
            request.setReason(reason);

            Boolean result=requestService.passRequest(request);


            if(result==null)
                return new RetMessage<>(RetCodeEnum.fail,"通过时异常!",null);
            if(result) {
                OperateRecord record = new OperateRecord();
                record.setRecordId(IdWorker.getId());
                record.setOperaterId(account.getAccountId());
                record.setAccountType(account.getAccountType());
                record.setPhone(account.getPhone());
                record.setName(account.getUserName());
                record.setTargetType(OperateTargetType.request);
                record.setType(OperateType.passRequest);
                record.setOperateTime(TimeAssist.getNow());
                record.setOperateResult("success");
                record.setDescription("通过申请[" + request.getRquestId() + "]");
                operateRecordService.addRecord(record);

                Account adder=accountService.queryAccountById(request.getRequesterId());
                record=new OperateRecord();
                record.setRecordId(IdWorker.getId());
                record.setOperaterId(adder.getAccountId());
                record.setAccountType(adder.getAccountType());
                record.setPhone(adder.getPhone());
                record.setName(adder.getUserName());
                record.setTargetType(OperateTargetType.commodity);
                record.setOperateTime(TimeAssist.getNow());
                record.setOperateResult("success");

                boolean flag=false;
                commodity.setRemark(commodity.getRemark()+","+request.getRequesterId());
                switch (RequestOperateType.getType(request.getType())){
                    case createCommodity:{
                        commodity.setRemark(request.getRequesterId()+"");
                        record.setType(OperateType.createCommodity);
                        record.setDescription("添加商品[" + commodity.getCommodityId() + "]");
                        flag=commodityService.addCommodity(commodity);
                    };break;
                    case modifyCommodity:{
                        record.setType(OperateType.modifyCommodity);
                        record.setDescription("修改商品[" + commodity.getCommodityId() + "]");
                        flag=commodityService.modifyCommodity(commodity);
                    };break;
                    case unshelveCommodity:{
                        record.setType(OperateType.unshelveCommodity);
                        record.setDescription("下架商品[" + commodity.getCommodityId() + "]");
                        flag=commodityService.shelveCommodity(commodity);
                    };break;
                    case shelveCommodity:{
                        record.setType(OperateType.shelveCommodity);
                        record.setDescription("上架商品[" + commodity.getCommodityId() + "]");
                        flag=commodityService.unshelveCommodity(commodity);
                    };break;
                    case cancelCommodity:throw new UnsupportedOperationException("不支持注销商品");
                    default:;
                }
                if(flag){
                    record.setDescription("success");
                    recordService.addRecord(record);
                    return new RetMessage<>(RetCodeEnum.success,"通过成功,操作成功!",null);
                }else{
                    record.setDescription("fail");
                    return new RetMessage<>(RetCodeEnum.success,"通过成功!操作失败",null);
                }
            }
            if(!result)
                return new RetMessage<>(RetCodeEnum.fail,"通过失败!",null);
            return new RetMessage<>(RetCodeEnum.exception,"no thing to do!",null);
        } catch (Exception e) {
            LogFactory.info(this,"通过请求异常!",e);
            return new RetMessage<>(RetCodeEnum.error,"系统异常!",null);
        } finally {
            if(lock!=null)
                lockService.unlock(request);
        }
    }

    @Override
    public RetMessage<Boolean> disagreeRequest(Long requestId,
                                               Long operaterId,
                                               String reason) {
        Account account=accountService.queryAccountById(operaterId);
        RetMessage message=checkAccount.apply(account,OperateType.rejectRequest);
        if(message!=null)
            return message;
        Request request=requestService.queryRequestInfo(requestId);
        Boolean lock=null;
        try {
            lock=lockService.lock(request);
            if(lock==null||!lock)
                return new RetMessage<>(RetCodeEnum.fail,"请求不存在或正被处理,拒绝失败!",null);
            message=checkRequest.apply(request,OperateType.rejectRequest);
            if(message!=null)
                return message;
            Merchant merchant=merchantService.queryMerchantParticularsById(account.getMerchantId());
            message=checkMerchant.apply(merchant,OperateType.rejectRequest);
            if(message!=null)
                return message;
            //配置请求
            request.setStatus(RequestStatus.reject);
            request.setCheckerId(account.getAccountId());
            request.setCheckerName(account.getUserName());
            request.setCheckerPhone(account.getPhone());
            request.setCheckTime(TimeUtil.getDateTime());
            request.setReason(reason);
            Boolean result=requestService.rejectRequest(request);
            if(result==null)
                return new RetMessage<>(RetCodeEnum.fail,"拒绝时异常!",null);
            if(result){
                OperateRecord record=new OperateRecord();
                record.setRecordId(IdWorker.getId());
                record.setOperaterId(account.getAccountId());
                record.setAccountType(account.getAccountType());
                record.setPhone(account.getPhone());
                record.setName(account.getUserName());
                record.setTargetType(OperateTargetType.request);
                record.setType(OperateType.rejectRequest);
                record.setOperateTime(TimeAssist.getNow());
                record.setOperateResult("success");
                record.setDescription("驳回申请["+request.getRquestId()+"]");
                operateRecordService.addRecord(record);
                return new RetMessage<>(RetCodeEnum.success,"拒绝成功!",null);
            }
            if(!result)
                return new RetMessage<>(RetCodeEnum.fail,"拒绝失败!",null);
            return new RetMessage<>(RetCodeEnum.exception,"no thing to do!",null);
        } catch (Exception e) {
            LogFactory.info(this,"驳回请求异常!",e);
            return new RetMessage<>(RetCodeEnum.error,"系统异常!",null);
        }finally {
            if(lock!=null&&lock)
                lockService.unlock(request);
        }

    }

    @Override
    public RetMessage<Boolean> requestAddCommodity(String commodityName,
                                                   CommodityType commodityType,
                                                   BigDecimal facePrice,
                                                   BigDecimal costPrice,
                                                   BigDecimal sellPrice,
                                                   Integer lifeTime,
                                                   String useDistription,
                                                   Long operaterId,
                                                   @Deprecated Long merchantId) {

        Account account=accountService.queryAccountById(operaterId);
        RetMessage message=checkAccount.apply(account,OperateType.requestAddCommodity);
        if(message!=null)
            return message;
        Merchant merchant=merchantService.queryMerchantParticularsById(account.getMerchantId());
        message=checkMerchant.apply(merchant,OperateType.requestAddCommodity);
        if(message!=null)
            return message;
        //组装商品
        Commodity commodity=new Commodity();
        commodity.setAddId(IdWorker.getId());
        commodity.setAddName(commodityName);
        commodity.setType(commodityType);
        commodity.setFacePrice(facePrice);
        commodity.setSellPrice(sellPrice);
        commodity.setCostPrice(costPrice);
        commodity.setStatus(CommodityStatus.unshelved);
        commodity.setLifeTime(lifeTime);

        commodity.setMerchantId(merchant.getMerchantId());
        commodity.setMerchantName(merchant.getMerchantName());

        commodity.setDescription(useDistription);
        commodity.setUseDetail(useDistription);

        commodity.setAddId(account.getAccountId());
        commodity.setAddTime(TimeUtil.getDateTime());
        commodity.setAddName(account.getUserName());
        //组装请求
        Request request=makeRequest(account,merchant,commodity);
        request.setType(RequestOperateType.createCommodity);
        Boolean result=requestService.addRequest(request);
        if(result==null)
            return new RetMessage<>(RetCodeEnum.success,"添加时异常!",null);
        if(result){
            OperateRecord record=new OperateRecord();
            record.setRecordId(IdWorker.getId());
            record.setOperaterId(account.getAccountId());
            record.setAccountType(account.getAccountType());
            record.setPhone(account.getPhone());
            record.setName(account.getUserName());
            record.setTargetType(OperateTargetType.request);
            record.setType(OperateType.requestAddCommodity);
            record.setOperateTime(TimeAssist.getNow());
            recordService.addRecord(record);
            return new RetMessage<>(RetCodeEnum.success,"添加成功!",null);
        }
        else
            return new RetMessage<>(RetCodeEnum.success,"添加失败!",null);
    }

    @Override
    public RetMessage<Boolean> requestModifyCommodity(BigDecimal costPrice,
                                                      BigDecimal facePrice,
                                                      BigDecimal sellPrice,
                                                      Integer lifeTime,
                                                      Long commodityId,
                                                      Long operaterId) {
        Account account=accountService.queryAccountById(operaterId);
        RetMessage message=checkAccount.apply(account,OperateType.requestModifyCommodity);
        if(message!=null)
            return message;
        Merchant merchant=merchantService.queryMerchantParticularsById(account.getMerchantId());
        message=checkMerchant.apply(merchant,OperateType.requestModifyCommodity);
        if(message!=null)
            return message;
        //修改商品
        Commodity commodity=commodityService.queryCommodity(commodityId);
        message=checkCommodity.apply(commodityService.queryCommodity(commodityId),OperateType.requestModifyCommodity);
        if(message!=null)
            return message;
        commodity.setCostPrice(costPrice);
        commodity.setFacePrice(facePrice);
        commodity.setSellPrice(sellPrice);
        commodity.setLifeTime(lifeTime);
        //组装请求
        Request request=makeRequest(account,merchant,commodity);
        request.setType(RequestOperateType.modifyCommodity);
        Boolean result=requestService.addRequest(request);
        if(result==null)
            return new RetMessage<>(RetCodeEnum.success,"添加时异常!",null);
        if(result){
            OperateRecord record=new OperateRecord();
            record.setRecordId(IdWorker.getId());
            record.setOperaterId(account.getAccountId());
            record.setAccountType(account.getAccountType());
            record.setPhone(account.getPhone());
            record.setName(account.getUserName());
            record.setTargetType(OperateTargetType.request);
            record.setType(OperateType.requestModifyCommodity);
            record.setOperateTime(TimeAssist.getNow());
            recordService.addRecord(record);
            return new RetMessage<>(RetCodeEnum.success,"添加成功!",null);
        }
        else
            return new RetMessage<>(RetCodeEnum.success,"添加失败!",null);
    }

    @Override
    public RetMessage<Boolean> requestShelveCommodity(Long commodityId,
                                                      Long operaterId) {
        Account account=accountService.queryAccountById(operaterId);
        RetMessage message=checkAccount.apply(account,OperateType.requestShelveCommodity);
        if(message!=null)
            return message;
        Merchant merchant=merchantService.queryMerchantParticularsById(account.getMerchantId());
        message=checkMerchant.apply(merchant,OperateType.requestShelveCommodity);
        if(message!=null)
            return message;
        //修改商品
        Commodity commodity=commodityService.queryCommodity(commodityId);
        message=checkCommodity.apply(commodityService.queryCommodity(commodityId),OperateType.requestShelveCommodity);
        if(message!=null)
            return message;
        commodity.setStatus(CommodityStatus.shelved);
        //组装请求
        Request request=makeRequest(account,merchant,commodity);
        request.setType(RequestOperateType.shelveCommodity);
        Boolean result=requestService.addRequest(request);
        if(result==null)
            return new RetMessage<>(RetCodeEnum.success,"添加时异常!",null);
        if(result){
            OperateRecord record=new OperateRecord();
            record.setRecordId(IdWorker.getId());
            record.setOperaterId(account.getAccountId());
            record.setAccountType(account.getAccountType());
            record.setPhone(account.getPhone());
            record.setName(account.getUserName());
            record.setTargetType(OperateTargetType.request);
            record.setType(OperateType.requestShelveCommodity);
            record.setOperateTime(TimeAssist.getNow());
            return new RetMessage<>(RetCodeEnum.success,"添加成功!",null);
        }
        else
            return new RetMessage<>(RetCodeEnum.success,"添加失败!",null);
    }

    @Override
    public RetMessage<Boolean> requestUnshelveCommodity(Long commodityId,
                                                        Long operaterId) {
        Account account=accountService.queryAccountById(operaterId);
        RetMessage message=checkAccount.apply(account,OperateType.requestUnshelveCommodity);
        if(message!=null)
            return message;
        Merchant merchant=merchantService.queryMerchantParticularsById(account.getMerchantId());
        message=checkMerchant.apply(merchant,OperateType.requestUnshelveCommodity);
        if(message!=null)
            return message;
        //修改商品
        Commodity commodity=commodityService.queryCommodity(commodityId);
        message=checkCommodity.apply(commodityService.queryCommodity(commodityId),OperateType.requestUnshelveCommodity);
        if(message!=null)
            return message;
        Request request=makeRequest(account,merchant,commodity);
        request.setType(RequestOperateType.unshelveCommodity);
        Boolean result=requestService.addRequest(request);
        if(result==null)
            return new RetMessage<>(RetCodeEnum.success,"添加时异常!",null);
        if(result){
            OperateRecord record=new OperateRecord();
            record.setRecordId(IdWorker.getId());
            record.setOperaterId(account.getAccountId());
            record.setAccountType(account.getAccountType());
            record.setPhone(account.getPhone());
            record.setName(account.getUserName());
            record.setTargetType(OperateTargetType.request);
            record.setType(OperateType.requestUnshelveCommodity);
            record.setOperateTime(TimeAssist.getNow());
            return new RetMessage<>(RetCodeEnum.success,"添加成功!",null);
        }
        else
            return new RetMessage<>(RetCodeEnum.success,"添加失败!",null);
    }

    @Override
    @Deprecated
    public RetMessage<String> queryCommodityRequest(String commodityName,
                                                    CommodityType type,
                                                    RequestOperateType operateType,
                                                    RequestStatus status,
                                                    String timeStart,
                                                    String timeEnd,
                                                    Integer pageNum,
                                                    Integer pageSize,
                                                    Long operaterId) {
        Account account=accountService.queryAccountById(operaterId);
        RetMessage message=checkAccount.apply(account,OperateType.queryRequest);
        if(message!=null)
            return message;
        Merchant merchant=merchantService.queryMerchantParticularsById(account.getMerchantId());
        message=checkMerchant.apply(merchant,OperateType.queryRequest);
        if(message!=null)
            return message;
        Page<Request> page=new Page<>(pageNum,pageSize);
        List<Request> list=queryRequestsHidden(timeStart,timeEnd,commodityName,merchant.getMerchantName(),operateType,status,type,merchant.getMerchantId(),page);
        Map<String,Object> map=new HashMap<>();
        map.put("page",page);
        map.put("list",list);
        if(list!=null)
            return new RetMessage<String>(RetCodeEnum.success,"查询成功", JSON.toJSONString(map));
        else
            return new RetMessage<String>(RetCodeEnum.fail,"查询失败",null);
    }
    @Override
    public RetMessage<String> queryRequest(Long requestId,Long operatorId){
        Account account=accountService.queryAccountById(operatorId);
        RetMessage message=checkAccount.apply(account,OperateType.queryRequest);
        if(message!=null)
            return message;
        Request request=requestService.queryRequestInfo(requestId);
        return new RetMessage<>(RetCodeEnum.success,"查询成功!",request==null?null:request.toJson());
    }
}
