package com.lanxi.couponcode.spi.service;

import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.annotations.RealReturnType;
import com.lanxi.couponcode.spi.consts.enums.CommodityType;
import com.lanxi.couponcode.spi.consts.enums.OrderStatus;

import java.io.File;

/**
 * Created by yangyuanjian on 2017/11/9.
 */
public interface OrderService {

    RetMessage<String> addOrder(String Phone,
                                CommodityType Type,
                                Long SkuCode,
                                Integer Count,
                                String Remark,
                                String SRC,
                                String requestType,
                                String MsgID,
                                String NeedSend,
                                String WorkDate,
                                String WorkTime,
                                String CHKDate,
                                String SerialNum);

    RetMessage<String> queryOrderInfo(String OrgWorkDate,
                                      String OrgMsgID,
                                      String Phone,
                                      String Remark);

    @RealReturnType ("List<Order>")
    RetMessage<String> queryOrders(String StartDate,
                                   String EndDate,
                                   String Phone,
                                   String Remark);

    RetMessage<String> queryOrders(String StartDate, String EndDate, String Phone, Long orderId, Long SkuCode,
                                   String SRC, OrderStatus orderStatus, Integer pageNum, Integer pageSize, Long operaterId);

    RetMessage<File> orderExport(String StartDate, String EndDate, String Phone, Long orderId, Long SkuCode,
                                 String SRC, OrderStatus orderStatus, Long operaterId);

    RetMessage<String> queryOrderInfo(Long orderId, Long operaterId);
}
