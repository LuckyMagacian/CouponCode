package com.lanxi.couponcode.impl.newservice;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.Order;
import com.lanxi.couponcode.spi.consts.enums.OrderStatus;
@Service("orderService")
public class OrderServiceImpl implements OrderService{
	@Resource
	private DaoService dao;
	@Override
	public Boolean createOrder(Order order) {
		return order.insert();
		
	}
	
	@Override
	public Order queryOrderInfo(EntityWrapper<Order> wrapper) {
		
		List<Order> orders=dao.getOrderDao().selectList(wrapper);
		if (orders.size()>1) {
//			for(int i=0;i<orders.size();i++) {
//				if (orders.get(i).getOrderStatus().equals(OrderStatus.finish.getValue())) {
//					return orders.get(i);
//				}
//			}
			return null;
		}else if (orders.size()==0) {
			return null;
		}else 
			return orders.get(0);
	}

	@Override
	public List<Order> queryOrders(EntityWrapper<Order> wrapper) {
			return dao.getOrderDao().selectList(wrapper);
	}

	@Override
	public Order queryOrderInfoById(Long orderId) {
		return dao.getOrderDao().selectById(orderId);
	}

	@Override
	public Boolean changeOrderStatus(Order order) {
		return order.updateById();
	}

	@Override
	public Boolean isRepetition(String MsgID, String WorkDate) {
		if (MsgID!=null&&!MsgID.isEmpty()&&WorkDate!=null&&!WorkDate.isEmpty()) {
			Order order=new Order();
			order.setMsgID(MsgID);
			order.setWorkDate(WorkDate);
			Order order2=dao.getOrderDao().selectOne(order);
			if (order2.getOrderId()==null||order2.getOrderId()==0L) {
				return false;
			}else {
				return true;
			}
		}else {
			return true;
		}
		
	}

	

}
