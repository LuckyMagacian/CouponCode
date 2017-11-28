package com.lanxi.couponcode.spi.service;

import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.annotations.RealReturnType;
import com.lanxi.couponcode.spi.consts.enums.CommodityType;

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
			String CHKDate);
	
	RetMessage<String> queryOrderInfo(String OrgWorkDate,
			String OrgMsgID,
			String Phone,
			String Remark);
	@RealReturnType("List<Order>")
	RetMessage<String> queryOrders(String StartDate,
			String EndDate,
			String Phone,
            String Remark);
}
