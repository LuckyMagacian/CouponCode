package com.lanxi.couponcode.impl.controller;

import com.lanxi.couponcode.impl.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.enums.AccountType;
import com.lanxi.couponcode.spi.consts.enums.OperateTargetType;
import com.lanxi.couponcode.spi.consts.enums.OperateType;

/**
 * Created by yangyuanjian on 2017/11/10.
 */
public class OperaterRecordServiceImpl implements com.lanxi.couponcode.spi.service.OperateRecordService{
    @Override
    public RetMessage<String> queryOperateRecord(OperateType type, OperateTargetType targetType, String merchantName, String shopName, String timeStart, String timeStop, AccountType accountType, String name, String phone, Long merchantId, Long shopId, Long operaterId, Long recordOperaterId) {
        RetMessage<String> retMessage=new RetMessage<>();
        try {

        } catch (Exception e) {

        }finally {
            return  retMessage;
        }
    }

    @Override
    public RetMessage<String> queryMerchantOperaterRecord(OperateType type, String shopName, String timeStart, String timeEnd, AccountType accountType, String operaterName, String operaterPhone, Long querierId) {
        RetMessage<String> retMessage=new RetMessage<>();
        try {

        } catch (Exception e) {

        }finally {
            return  retMessage;
        }
    }

    @Override
    public RetMessage<Boolean> commodityShelveRequest(Long commodityId, Long operaterId) {
        RetMessage<Boolean> retMessage=new RetMessage<>();
        try {

        } catch (Exception e) {

        }finally {
            return  retMessage;
        }
    }

    @Override
    public RetMessage<Boolean> commodityUnshelveRequest(Long commodityId, Long operaterId) {
        RetMessage<Boolean> retMessage=new RetMessage<>();
        try {

        } catch (Exception e) {

        }finally {
            return  retMessage;
        }
    }

    @Override
    public RetMessage<Boolean> commodityDelRequest(Long commodityId, Long operaterId) {
        RetMessage<Boolean> retMessage=new RetMessage<>();
        try {

        } catch (Exception e) {

        }finally {
            return  retMessage;
        }
    }

    @Override
    public RetMessage<Boolean> commodityModifyRequest(Integer costPrice, Integer lifeTime, String useDistription, Long commodityId, Long operaterId) {
        RetMessage<Boolean> retMessage=new RetMessage<>();
        try {

        } catch (Exception e) {

        }finally {
            return  retMessage;
        }
    }
}
