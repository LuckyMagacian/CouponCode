package com.lanxi.couponcode.impl.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.assist.RetMessage;
import com.lanxi.couponcode.impl.entity.Account;
import com.lanxi.couponcode.impl.entity.CouponCode;
import com.lanxi.couponcode.impl.service.CouponCodeService;
import com.lanxi.couponcode.impl.service.DaoService;
import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.couponcode.spi.consts.enums.AccountType;
import com.lanxi.couponcode.spi.consts.enums.CouponCodeStatus;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.util.utils.LoggerUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * 串码控制<br>
 * Created by yangyuanjian on 2017/11/10.
 */
@EasyLog(LoggerUtil.LogLevel.INFO)
@Deprecated
public class CouponCodeServiceIml implements com.lanxi.couponcode.spi.service.CouponService{
    @Resource
    private CouponCodeService codeService;
    @Resource
    private DaoService daoService;
    @Override
    public RetMessage<String> queryCodes(String timeStart, String timeEnd, String merchantName, String commodityName, Long code, Long codeId, Integer pageNum, Integer pageSize, Long operaterId) {
        RetMessage<String> retMessage=new RetMessage<>();
        try {
            Optional<Account> account=Optional.of(daoService.getAccountDao().selectById(operaterId));
            //查询者必须存在!
            if(!account.isPresent()){
                retMessage.setRetCode(RetCodeEnum.warning);
                retMessage.setRetMessage("操作者不存在!");
            }
            //若查询者存在
            account.ifPresent(e->{
                if(!AccountType.admin.equals(account.get().getType())){
                    retMessage.setRetCode(RetCodeEnum.warning);
                    retMessage.setRetMessage("操作者不是管理员!");
                }else {
                    List<CouponCode> codes = codeService.queryCouponCode(timeStart, timeEnd, merchantName, commodityName, code, codeId, null, null, null, null, null, true, new Page(pageNum, pageSize));
                    retMessage.setRetCode(RetCodeEnum.success);
                    retMessage.setRetMessage("查询成功!");
                    retMessage.setDetail(JSON.toJSONString(codes));
                }
            });
        } catch (Exception e) {
            retMessage.setRetCode(RetCodeEnum.exception);
            retMessage.setRetMessage("查询时发生异常!");
        }finally {
            return  retMessage;
        }
    }

    @Override
    public RetMessage<File> queryCodesExport(String timeStart, String timeEnd, String merchantName, String commodityName, Long code, Long codeId, Long operaterId) {
        RetMessage<File> retMessage=new RetMessage<>();
        try {

        } catch (Exception e) {

        }finally {
            return  retMessage;
        }
    }

    @Override
    public RetMessage<Boolean> destroyCode(Long codeId,Long operaterId) {
        RetMessage<Boolean> retMessage=new RetMessage<>();
        try {
            Account account=daoService.getAccountDao().selectById(operaterId);
            CouponCode code=daoService.getCouponCodeDao().selectById(codeId);
            if(account==null){
                retMessage.setCodeAndMessage(RetCodeEnum.fail,"操作者不存在!");
            }else if (code==null){
                retMessage.setCodeAndMessage(RetCodeEnum.fail,"串码不存在!");
            }else if((!account.getMerchantId().equals(code.getMerchantId()))){
                retMessage.setCodeAndMessage(RetCodeEnum.fail,"串码商户与操作者商户不一致!");
            }else if(CouponCodeStatus.undestroyed.equals(code.getCodeStatus())){
                retMessage.setCodeAndMessage(RetCodeEnum.fail,"串码已被使用,无法销毁!");
            } else{
                Boolean result= codeService.destroyCode(codeId,account.getAccountId(),account.toJson(),account.getPhone());
                if(result){
                    retMessage.setAll(RetCodeEnum.success,"销毁串码成功!",null);
                }else{
                    retMessage.setAll(RetCodeEnum.fail,"销毁串码失败!",null);
                }
            }
        } catch (Exception e) {
            retMessage.setCodeAndMessage(RetCodeEnum.exception,"销毁串码时发生异常!");
        }finally {
            return  retMessage;
        }
    }

    @Override
    public RetMessage<Boolean> destroyCode(Long code, Long merchantId, Long operaterId) {
        RetMessage<Boolean> retMessage=new RetMessage<>();
        try {
            CouponCode couponCode=codeService.queryCouponCode(merchantId,code);
            Account account=daoService.getAccountDao().selectById(operaterId);
            if(couponCode==null){
                retMessage.setCodeAndMessage(RetCodeEnum.fail,"串码不存在!");
            }else if(account==null){
                retMessage.setCodeAndMessage(RetCodeEnum.fail,"操作者不存在!");
            }else if(!account.getMerchantId().equals(merchantId)){
                retMessage.setCodeAndMessage(RetCodeEnum.fail,"串码商户与操作者商户不一致!");
            }else{
                retMessage=destroyCode(couponCode.getCodeId(),operaterId);
            }
        } catch (Exception e) {

        }finally {
            return  retMessage;
        }
    }

    @Override
    public RetMessage<Boolean> verificateCode(Long codeId,Long operaterId) {
        RetMessage<Boolean> retMessage=new RetMessage<>();
        try {

        } catch (Exception e) {

        }finally {
            return  retMessage;
        }
    }

    @Override
    public RetMessage<Boolean> verificateCode(Long code, Long accountId, Long operaterId) {
        RetMessage<Boolean> retMessage=new RetMessage<>();
        try {

        } catch (Exception e) {

        }finally {
            return  retMessage;
        }
    }

    @Override
    public RetMessage<Boolean> postoneCode(Long codeId, Long operaterId) {
        RetMessage<Boolean> retMessage=new RetMessage<>();
        try {

        } catch (Exception e) {

        }finally {
            return  retMessage;
        }
    }

    @Override
    public RetMessage<Boolean> generateCode(Long merchantId, Long commodityId, String reason, Integer channel) {
        RetMessage<Boolean> retMessage=new RetMessage<>();
        try {

        } catch (Exception e) {

        }finally {
            return  retMessage;
        }
    }

    @Override
    public RetMessage<String> couponCodeInfo(Long codeId, Long operaterId) {
        RetMessage<String> retMessage=new RetMessage<>();
        try {

        } catch (Exception e) {

        }finally {
            return  retMessage;
        }
    }

    @Override
    public RetMessage<String> couponCodeInfo(Long merchantId, Long code, Long operaterId) {
        RetMessage<String> retMessage=new RetMessage<>();
        try {

        } catch (Exception e) {

        }finally {
            return  retMessage;
        }
    }
}
