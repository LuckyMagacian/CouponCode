package com.lanxi.couponcode.impl.newservice;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.Order;
@Service("orderService")
public class OrderServiceImpl implements OrderService{
	@Resource
	private DaoService dao;
	@Override
	public Boolean createOrder(Order order) {
		return order.insert();
		
	}
	
	@Override
	public Order queryOrderInfo(String createTime, String transactionNum, String phone,String remark) {
		EntityWrapper<Order> wrapper=new EntityWrapper<Order>();
		if (transactionNum!=null) {
			wrapper.eq("transaction_num",transactionNum);
		}
		if (phone!=null&&!phone.isEmpty()) {
			wrapper.eq("phone",phone);
		}
		if (remark!=null&&!remark.isEmpty()) {
			wrapper.eq("remark", remark);
		}
		if (createTime!=null&&!createTime.isEmpty()) {
			String timeStop=createTime;
				while (createTime.length() < 14) {
					createTime += "0";
				}
				wrapper.ge("create_time", createTime);
				while (timeStop.length() < 14) {
					timeStop += "9";
				}	
				wrapper.le("create_time", timeStop);
		}
		return dao.getOrderDao().selectList(wrapper).get(0);
	}

	@Override
	public List<Order> queryOrders(EntityWrapper<Order> wrapper,Page<Order> pageObj) {
		
		if (pageObj!=null) {
			return dao.getOrderDao().selectPage(pageObj, wrapper);
		}else {
			return dao.getOrderDao().selectList(wrapper);
		}
		
	}

	@Override
	public Order queryOrderInfoById(Long orderId) {
		return dao.getOrderDao().selectById(orderId);
	}

	

}
