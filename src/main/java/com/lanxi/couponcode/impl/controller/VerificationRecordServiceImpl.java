package com.lanxi.couponcode.impl.controller;

import com.lanxi.couponcode.impl.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.enums.VerificationType;

/**
 * Created by yangyuanjian on 2017/11/10.
 */
public class VerificationRecordServiceImpl implements com.lanxi.couponcode.spi.service.VerificationRecordService{
    @Override
    public RetMessage<String> queryVerificationRecords(Long code, String timeStart, String timeStop, String shopName, String workerPhone, VerificationType type, String commodityName, Integer pageNum, Integer pageSize, Long operaterId) {
        RetMessage<String> retMessage=new RetMessage<>();
        try {

        } catch (Exception e) {

        }finally {
            return  retMessage;
        }
    }

    @Override
    public RetMessage<String> queryShopVerificationRecords(String timeStart, String timeStop, String shopName, Long shopId, Long operaterId) {
        RetMessage<String> retMessage=new RetMessage<>();
        try {

        } catch (Exception e) {

        }finally {
            return  retMessage;
        }
    }
}
