package com.lanxi.couponcode.spi.service;

import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.enums.ClearStatus;
import com.lanxi.couponcode.spi.consts.enums.InvoiceStatus;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by yangyuanjian on 2017/11/9.
 */
public interface ClearService {
    /**管理员专用*/
    RetMessage<String> queryDailyRecords(String merchantName,
                                         String timeStart,
                                         String timeStop,
                                         ClearStatus clearStatus,
                                         Integer pageNum,
                                         Integer pageSize,
                                         Long operaterId);
    /**商户用*/
    RetMessage<String> queryDailyRecords(String timeStart,
                                         String timeStop,
                                         ClearStatus clearStatus,
                                         Integer pageNum,
                                         Integer pageSize,
                                         Long operaterId);

    RetMessage<String> queryDailyRecordInfo(Long recordId, Long operaterId);


    /**管理员用*/
    RetMessage<String> queryClearRecords(String merchantName,
                                         String timeStart,
                                         String timeStop,
                                         ClearStatus clearStatus,
                                         InvoiceStatus invoiceStatus,
                                         Integer pageNum,
                                         Integer pageSize,
                                         Long operaterId);

    RetMessage<File> exportClearRecords(String merchantName,
                                        String timeStart,
                                        String timeStop,
                                        ClearStatus clearStatus,
                                        InvoiceStatus invoiceStatus,
                                        Long operaterId);

    /**商户用*/
    RetMessage<String> queryClearRecords(String timeStart,
                                         String timeStop,
                                         ClearStatus clearStatus,
                                         InvoiceStatus invoiceStatus,
                                         Integer pageNum,
                                         Integer pageSize,
                                         Long operaterId);

    RetMessage<File> exportClearRecords(String timeStart,
                                        String timeStop,
                                        ClearStatus clearStatus,
                                        InvoiceStatus invoiceStatus,
                                        Long operaterId);

    RetMessage<String> queryRecordInfo(Long recordId, Long operaterId);

    /**管理员用*/
    RetMessage<String> clear(Long[] dailyRecordIds,
                             Long operaterId);

    RetMessage<String> clear(String clearTime, BigDecimal factTotal, String remark, Long recordId, Long operaterId);

    RetMessage<String> addInvoiceInfo(String taxNum,String logisticsCompany,String orderNum,String postTime,Long recordId,Long operaterId);

    /**管理员专用*/
    RetMessage<String> statsticDailyRecords(String merchantName,
                                         String timeStart,
                                         String timeStop,
                                         ClearStatus clearStatus,
                                         Integer pageNum,
                                         Integer pageSize,
                                         Long operaterId);
    /**管理员专用*/
    RetMessage<File> exportStatsticDailyRecords(String merchantName,
                                            String timeStart,
                                            String timeStop,
                                            ClearStatus clearStatus,
                                            Long operaterId);


    /**商户用*/
    RetMessage<String> statsticDailyRecords(String timeStart,
                                         String timeStop,
                                         ClearStatus clearStatus,
                                         Integer pageNum,
                                         Integer pageSize,
                                         Long operaterId);
    /**管理员专用*/
    RetMessage<File> exportStatsticDailyRecords(String timeStart,
                                                String timeStop,
                                                ClearStatus clearStatus,
                                                Long operaterId);

    RetMessage<File> exoirtDailyRecords(String timeStart,
                                        String timeStop,
                                        ClearStatus clearStatus,
                                        InvoiceStatus invoiceStatus,
                                        Long operaterId);

    RetMessage<File> exoirtDailyRecords(String merchantName,
                                        String timeStart,
                                        String timeStop,
                                        ClearStatus clearStatus,
                                        InvoiceStatus invoiceStatus,
                                        Long operaterId);
}
