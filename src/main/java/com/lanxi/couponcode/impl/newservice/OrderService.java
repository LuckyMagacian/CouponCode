package com.lanxi.couponcode.impl.newservice;


import java.util.List;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.Order;

/**
 * @author wuxiaobo
 */
public interface OrderService {
    /*创建订单*/
    Boolean createOrder(Order order);

    /*查询订单详情*/
    Order queryOrderInfo(EntityWrapper<Order> wrapper);

    /*批量查询订单*/
    List<Order> queryOrders(EntityWrapper<Order> wrapper);

    /*通过订单id获取订单详情*/
    Order queryOrderInfoById(Long orderId);

    /*根据订单的id修改订单的状态并存入串码*/
    Boolean changeOrderStatus(Order order);

    /**
     * 根据交易序号和交易日期判断交易序号是否重复
     *
     * @param MsgID    交易序号
     * @param WorkDate 交易日期
     * @return true重复        false不重复
     */
    Boolean isRepetition(String MsgID, String WorkDate, String SRC);

    List<Order> queryOrders(EntityWrapper<Order> wrapper, Page<Order> pageObj);

    List<Order> orderExport(EntityWrapper<Order> wrapper);
}
