package com.lanxi.couponcode.impl.newservice;

import java.util.List;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.Order;

/**
 * 
 * @author wuxiaobo
 *
 */
public interface OrderService {
	/*创建订单*/
	public Boolean createOrder(Order order);
	/*查询订单详情*/
	public Order queryOrderInfo(String createTime,String transactionNum,String phone,String remark);
	/*批量查询订单*/
	public List<Order> queryOrders(EntityWrapper<Order> wrapper,Page<Order> pageObj);
	/*通过订单id获取订单详情*/
	public Order queryOrderInfoById(Long orderId);
}
