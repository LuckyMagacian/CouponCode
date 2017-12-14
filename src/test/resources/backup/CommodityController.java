package com.lanxi.couponcode.view;

import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.enums.CommodityStatus;
import com.lanxi.couponcode.spi.consts.enums.CommodityType;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.couponcode.spi.service.CommodityService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;

import static com.lanxi.couponcode.spi.assist.ArgAssist.*;
import static com.lanxi.couponcode.spi.assist.PredicateAssist.isNull;
import static com.lanxi.couponcode.spi.assist.PredicateAssist.notId;

/**
 * Created by yangyuanjian on 12/6/2017.
 */
@Controller
public class CommodityController {

    @Resource(name="commodityControllerService")
    private CommodityService commodityService;

    public String addCommodity(HttpServletRequest req, HttpServletResponse res){
        String commodityName=getArg.apply(req,"commodityName");
        String commodityTypeStr=getArg.apply(req,"commodityType");
        String facePriceStr=getArg.apply(req,"facePrice");
        String costPriceStr=getArg.apply(req,"costPrice");
        String sellPriceStr=getArg.apply(req,"sellPrice");
        String lifeTimeStr=getArg.apply(req,"lifeTime");
        String merchantNameStr=getArg.apply(req,"merchantName");
        String merchantIdStr=getArg.apply(req,"merchantId");
        String operaterIdStr=getArg.apply(req,"operaterId");

        if(isNull.test(merchantIdStr)||notId.test(merchantIdStr)){
            return new RetMessage<String>(RetCodeEnum.fail,"商户编号不能为空!",null).toJson();
        }

        CommodityType commodityType=CommodityType.getType(commodityTypeStr);
        BigDecimal facePrice=parseArg(facePriceStr,BigDecimal.class);
        BigDecimal costPrice=parseArg(costPriceStr,BigDecimal.class);
        BigDecimal sellPrice=parseArg(sellPriceStr,BigDecimal.class);
        Integer lifeTime=parseArg(lifeTimeStr,Integer.class);
        Long merchantId=parseArg(merchantIdStr,Long.class);
        Long operaterId=parseArg(operaterIdStr,Long.class);

        return commodityService.addCommodity(commodityName,commodityType,facePrice,costPrice,sellPrice,lifeTime,merchantNameStr,merchantId,operaterId).toJson();
    }

    public String modifyCommodity(HttpServletRequest req,HttpServletResponse res){
        String facePriceStr=getArg.apply(req,"facePrice");
        String costPriceStr=getArg.apply(req,"costPrice");
        String sellPriceStr=getArg.apply(req,"sellPrice");
        String lifeTimeStr=getArg.apply(req,"lifeTime");
        String commodityIdStr=getArg.apply(req,"commodityId");
        String operaterIdStr=getArg.apply(req,"operaterId");

        BigDecimal facePrice=parseArg(facePriceStr,BigDecimal.class);
        BigDecimal costPrice=parseArg(costPriceStr,BigDecimal.class);
        BigDecimal sellPrice=parseArg(sellPriceStr,BigDecimal.class);
        Integer lifeTime=parseArg(lifeTimeStr,Integer.class);
        Long commodityId=parseArg(commodityIdStr,Long.class);
        Long operaterId=parseArg(operaterIdStr,Long.class);

        return commodityService.modifyCommodity(costPrice,facePrice,sellPrice,lifeTime,commodityId,operaterId).toJson();
    }

    public String shelveCommodity(HttpServletRequest req,HttpServletResponse res){
        String commodityIdStr=getArg.apply(req,"commodityId");
        String operaterIdStr=getArg.apply(req,"operaterId");
        Long commodityId=parseArg(commodityIdStr,Long.class);
        Long operaterId=parseArg(operaterIdStr,Long.class);
        return commodityService.shelveCommodity(commodityId,operaterId).toJson();
    }

    public String unshelveCommodity(HttpServletRequest req,HttpServletResponse res){
        String commodityIdStr=getArg.apply(req,"commodityId");
        String operaterIdStr=getArg.apply(req,"operaterId");
        Long commodityId=parseArg(commodityIdStr,Long.class);
        Long operaterId=parseArg(operaterIdStr,Long.class);
        return commodityService.unshelveCommodity(commodityId,operaterId).toJson();
    }

    public String delCommodity(HttpServletRequest req,HttpServletResponse res){
        String commodityIdStr=getArg.apply(req,"commodityId");
        String operaterIdStr=getArg.apply(req,"operaterId");
        Long commodityId=parseArg(commodityIdStr,Long.class);
        Long operaterId=parseArg(operaterIdStr,Long.class);
        return commodityService.delCommodity(commodityId,operaterId).toJson();
    }

    public String queryCommodities(HttpServletRequest req,HttpServletResponse res){
        String merchantNameStr=getArg.apply(req,"merchantName");
        String commodityName=getArg.apply(req,"commodityName");
        String commodityTypeStr=getArg.apply(req,"commodityType");
        String commodityStatusStr=getArg.apply(req,"commodityStatus");
        String timeStart=getArg.apply(req,"timeStart");
        String timeStop=getArg.apply(req,"timeStop");
        String pageNumStr = getArg.apply(req, "pageNum");
        String pageSizeStr = getArg.apply(req, "pageSize");
        String operaterIdStr=getArg.apply(req,"operaterId");

        CommodityType commodityType=CommodityType.getType(commodityTypeStr);
        CommodityStatus commodityStatus=CommodityStatus.getType(commodityStatusStr);
        Integer pageNum = toIntArg.apply(pageNumStr);
        Integer pageSize = toIntArg.apply(pageSizeStr);
        Long operaterId = toLongArg.apply(operaterIdStr);
        return commodityService.queryCommodities(merchantNameStr,commodityName,commodityType,commodityStatus,timeStart,timeStart,pageNum,pageSize,operaterId).toJson();
    }

    public void exportCommodities(HttpServletRequest req,HttpServletResponse res){
        String merchantNameStr=getArg.apply(req,"merchantName");
        String commodityName=getArg.apply(req,"commodityName");
        String commodityTypeStr=getArg.apply(req,"commodityType");
        String commodityStatusStr=getArg.apply(req,"commodityStatus");
        String timeStart=getArg.apply(req,"timeStart");
        String timeStop=getArg.apply(req,"timeStop");
        String operaterIdStr=getArg.apply(req,"operaterId");

        CommodityType commodityType=CommodityType.getType(commodityTypeStr);
        CommodityStatus commodityStatus=CommodityStatus.getType(commodityStatusStr);
        Long operaterId = toLongArg.apply(operaterIdStr);
        File file=commodityService.queryCommoditiesExport(merchantNameStr,commodityName,commodityType,commodityStatus,timeStart,timeStart,operaterId).getDetail();
        FileInputStream fin=null;
        try {
            fin= new FileInputStream(file);
            byte[] bytes=new byte[1024];
            int readLenth=-1;
            while((readLenth=fin.read(bytes))>0){
                if(readLenth==1024)
                    res.getOutputStream().write(bytes);
                else
                    res.getOutputStream().write(bytes,0,readLenth);
            }
            res.getOutputStream().flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fin!=null)
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public String merchantQueryCommodities(HttpServletRequest req,HttpServletResponse res){
        String commodityName=getArg.apply(req,"commodityName");
        String commodityTypeStr=getArg.apply(req,"commodityType");
        String commodityStatusStr=getArg.apply(req,"commodityStatus");
        String pageNumStr = getArg.apply(req, "pageNum");
        String pageSizeStr = getArg.apply(req, "pageSize");
        String operaterIdStr=getArg.apply(req,"operaterId");

        CommodityType commodityType=CommodityType.getType(commodityTypeStr);
        CommodityStatus commodityStatus=CommodityStatus.getType(commodityStatusStr);
        Integer pageNum = toIntArg.apply(pageNumStr);
        Integer pageSize = toIntArg.apply(pageSizeStr);
        Long operaterId = toLongArg.apply(operaterIdStr);
        return commodityService.merchantQueryCommodities(commodityName,commodityType,commodityStatus,pageNum,pageSize,null,operaterId).toJson();
    }

    public void merchantExportCommodities(HttpServletRequest req,HttpServletResponse res){
        String commodityName=getArg.apply(req,"commodityName");
        String commodityTypeStr=getArg.apply(req,"commodityType");
        String commodityStatusStr=getArg.apply(req,"commodityStatus");
        String operaterIdStr=getArg.apply(req,"operaterId");

        CommodityType commodityType=CommodityType.getType(commodityTypeStr);
        CommodityStatus commodityStatus=CommodityStatus.getType(commodityStatusStr);
        Long operaterId = toLongArg.apply(operaterIdStr);
        File file=commodityService.merchantQueryCommoditiesExport(commodityName,commodityType,commodityStatus,null,operaterId).getDetail();
        FileInputStream fin=null;
        try {
            fin= new FileInputStream(file);
            byte[] bytes=new byte[1024];
            int readLenth=-1;
            while((readLenth=fin.read(bytes))>0){
                if(readLenth==1024)
                    res.getOutputStream().write(bytes);
                else
                    res.getOutputStream().write(bytes,0,readLenth);
            }
            res.getOutputStream().flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fin!=null)
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
