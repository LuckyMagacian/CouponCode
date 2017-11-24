package com.lanxi.couponcode.spi.service;

import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.annotations.RealReturnType;
import com.lanxi.couponcode.spi.consts.enums.CommodityType;

/**
 * Created by yangyuanjian on 2017/11/9.
 */
public interface OrderService {
	
	RetMessage<Boolean> addOrder(String phone,
			CommodityType commodityType,
			Long commodityId,
			Integer commodityNum,
			String remark,
			String initiator,
			String requestType,
			String transactionNum,
			String NeedSend);
	
	RetMessage<String> queryOrderInfo(String createTime,
			String transactionNum,
			String phone,
			String remark);
	@RealReturnType("List<Order>")
	RetMessage<String> queryOrders(String timeStart,
			String timeStop,
			String phone,
			Integer pageNum,
            Integer pageSize,
            String remark);
}
