package com.lanxi.couponcode.impl.controller;

import com.lanxi.couponcode.impl.assist.RetMessage;
import com.lanxi.couponcode.impl.service.CouponCodeService;

import javax.annotation.Resource;
import java.io.File;

/**
 * Created by yangyuanjian on 2017/11/10.
 */
public class CouponCodeServiceIml implements com.lanxi.couponcode.spi.service.CouponService{
    @Resource
    private CouponCodeService codeService;
    @Override
    public RetMessage<String> queryCodes(String timeStart, String timeEnd, String merchantName, String commodityName, Long code, Long codeId, Integer pageNum, Integer pageSize, Long operaterId) {
        RetMessage<String> retMessage=new RetMessage<>();
        try {

        } catch (Exception e) {

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
    public RetMessage<Boolean> destroyCode(Long codeId) {
        RetMessage<Boolean> retMessage=new RetMessage<>();
        try {

        } catch (Exception e) {

        }finally {
            return  retMessage;
        }
    }

    @Override
    public RetMessage<Boolean> destroyCode(Long code, Long accountId, Long operaterId) {
        RetMessage<Boolean> retMessage=new RetMessage<>();
        try {

        } catch (Exception e) {

        }finally {
            return  retMessage;
        }
    }

    @Override
    public RetMessage<Boolean> verificateCode(Long codeId) {
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
    public RetMessage<Boolean> postoneCode(Long codeId, Long accountId) {
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
