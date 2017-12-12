package com.lanxi.couponcode.view;

import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.couponcode.spi.consts.annotations.LoginCheck;
import com.lanxi.couponcode.spi.consts.annotations.SetUtf8;
import com.lanxi.couponcode.spi.consts.enums.AccountType;
import com.lanxi.couponcode.spi.consts.enums.OperateTargetType;
import com.lanxi.couponcode.spi.consts.enums.OperateType;
import com.lanxi.couponcode.spi.consts.enums.VerificationType;
import com.lanxi.couponcode.spi.service.CommodityService;
import com.lanxi.couponcode.spi.service.CouponService;
import com.lanxi.couponcode.spi.service.OperateRecordService;
import com.lanxi.util.utils.LoggerUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.lanxi.couponcode.spi.assist.ArgAssist.*;

/**
 * Created by yangyuanjian on 11/30/2017.
 */
@Controller
@RequestMapping("shopManager")
@EasyLog (LoggerUtil.LogLevel.INFO)
public class ShopManagerController {
    @Resource(name="codeControllerService")
    private CouponService codeService;
    @Resource(name = "operateRecordControllerService")
    private OperateRecordService operateRecordService;
    @Resource(name="commodityControllerService")
    private CommodityService commodityService;


    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping(value = "verifyCode",produces = "application/json;charset=utf-8")
    public String verifyCode(HttpServletRequest req,HttpServletResponse res){
        String codIdStr=getArg.apply(req,"codeId");
        String operaterIdStr=getArg.apply(req,"operaterId");
        String codeStr=getArg.apply(req,"code");
        String verifyTypeStr=getArg.apply(req,"verificationType");

        Long codeId=parseArg(codIdStr,Long.class);
        Long operaterId=parseArg(operaterIdStr,Long.class);
        Long code=parseArg(codeStr,Long.class);
        VerificationType verifyType=toVerificationType.apply(verifyTypeStr);
        if(codeId==null)
            return codeService.verificateCode(code,null,operaterId,verifyType).toJson();
        else
            return codeService.verificateCode(codeId,operaterId,verifyType).toJson();
    }
    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping(value = "queryCode",produces = "application/json;charset=utf-8")
    public String queryCode(HttpServletRequest req,HttpServletResponse res){
        String codIdStr=getArg.apply(req,"codeId");
        String operaterIdStr=getArg.apply(req,"operaterId");
        String codeStr=getArg.apply(req,"code");


        Long codeId=parseArg(codIdStr,Long.class);
        Long operaterId=parseArg(operaterIdStr,Long.class);
        Long code=parseArg(codeStr,Long.class);

        if(codeId==null)
            return codeService.couponCodeInfo(null,code,operaterId).toJson();
        else
            return codeService.couponCodeInfo(codeId,operaterId).toJson();
    }


    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping(value = "queryOperateRecords", produces = "application/json;charset=utf-8")
    public String queryOperateRecords(HttpServletRequest req,HttpServletResponse res){
        String typeStr=getArg.apply(req,"type");
        String targetTypeStr=getArg.apply(req,"targetType");
        String accountTypeStr=getArg.apply(req,"accountType");
        String timeStart=getArg.apply(req,"timeStart");
        String timeEnd=getArg.apply(req,"timeStop");
        String name=getArg.apply(req,"name");
        String phone=getArg.apply(req,"phone");
        String pageNumStr=getArg.apply(req,"pageNum");
        String pageSizeStr=getArg.apply(req,"pageSize");
        String operaterIdStr=getArg.apply(req,"operaterId");

        OperateType type= OperateType.getType(typeStr);
        OperateTargetType targetType= OperateTargetType.getType(targetTypeStr);
        AccountType accountType= AccountType.getType(accountTypeStr);
        Long operaterId=parseArg(operaterIdStr,Long.class);
        Integer pageNum=parseArg(pageNumStr,Integer.class);
        Integer pageSize=parseArg(pageSizeStr,Integer.class);
        return operateRecordService.queryShopMerchantOperateRecord(type,targetType,timeStart,timeEnd,accountType,name,phone,pageNum,pageSize,operaterId).toJson();
    }
    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping(value = "queryOperateRecord", produces = "application/json;charset=utf-8")
    public String queryOperateRecord(HttpServletRequest req,HttpServletResponse res){
        String recordIdStr=getArg.apply(req,"recordId");
        String operaterIdStr=getArg.apply(req,"operaterId");
        Long recordId=parseArg(recordIdStr,Long.class);
        Long operaterId=parseArg(operaterIdStr,Long.class);
        return operateRecordService.queryOperateRecordInfo(recordId,operaterId).toJson();
    }
    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping(value = "queryCommodity", produces = "application/json;charset=utf-8")
    public String queryCommodity(HttpServletRequest req,HttpServletResponse res){
        String commodityIdStr=getArg.apply(req,"commodityId");
        String operaterIdStr=getArg.apply(req,"operaterId");
        Long operaterId = toLongArg.apply(operaterIdStr);
        Long commodityId=toLongArg.apply(commodityIdStr);
        return commodityService.queryCommodity(commodityId,operaterId).toJson();
    }

}
