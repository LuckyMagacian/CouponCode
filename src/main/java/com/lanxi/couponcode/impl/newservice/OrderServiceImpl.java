package com.lanxi.couponcode.impl.newservice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.util.entity.LogFactory;
import com.lanxi.util.utils.ExcelUtil;
import com.lanxi.util.utils.LoggerUtil;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.lanxi.couponcode.impl.entity.Order;
import com.lanxi.couponcode.spi.consts.enums.OrderStatus;
@Service("orderService")
@EasyLog (LoggerUtil.LogLevel.INFO)
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
	public Boolean isRepetition(String MsgID, String WorkDate,String SRC) {
		
			EntityWrapper<Order>wrapper=new EntityWrapper<>();
		
				wrapper.eq("msg_id",MsgID);
		
			wrapper.eq("work_date",WorkDate);
		
			wrapper.eq("src",SRC);
			List<Order>list=dao.getOrderDao().selectList(wrapper);
			if (list!=null&&list.size()>0) {
				return true;
			}else {
				return false;
			}
	}

	@Override
	public List<Order> queryOrders(EntityWrapper<Order> wrapper, Page<Order> pageObj) {
		try {
			return dao.getOrderDao().selectPage(pageObj, wrapper);
			
		} catch (Exception e) {
			LogFactory.error(this,"批量查询门店时发生异常",e);
			return null;
		}
	}

	@Override
	public List<Order> orderExport(EntityWrapper<Order> wrapper) {
		try {
			return dao.getOrderDao().selectList(wrapper);
			
		} catch (Exception e) {
			LogFactory.error(this,"导出订单时发生异常",e);
			return null;
		}
		
	}
	

}
