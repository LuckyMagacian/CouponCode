package com.lanxi.couponcode.impl.newcontroller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.lanxi.couponcode.impl.config.ConstConfig;
import com.lanxi.couponcode.impl.entity.Commodity;
import com.lanxi.couponcode.impl.entity.Order;
import com.lanxi.couponcode.impl.newservice.CommodityService;
import com.lanxi.couponcode.impl.newservice.MerchantService;
import com.lanxi.couponcode.impl.newservice.OrderService;
import com.lanxi.couponcode.impl.newservice.RedisEnhancedService;
import com.lanxi.couponcode.impl.newservice.RedisService;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.couponcode.spi.consts.enums.CommodityStatus;
import com.lanxi.couponcode.spi.consts.enums.CommodityType;
import com.lanxi.couponcode.spi.consts.enums.MerchantStatus;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.util.utils.LoggerUtil;

/**
 * 
 * @author wuxiaobo
 *
 */
@EasyLog(LoggerUtil.LogLevel.INFO)
@Controller
public class OrderController implements com.lanxi.couponcode.spi.service.OrderService {
	@Resource
	private OrderService orderService;
	@Resource
	private RedisService redisService;
	@Resource
	private RedisEnhancedService redisEnhancedService;
	@Resource
	private CommodityService commodityService;
	@Resource
	private MerchantService merchantService;
	@Override
	public RetMessage<Boolean> addOrder(String phone, CommodityType commodityType, Long commodityId,
			Integer commodityNum, String remark,String initiator,String requestType,String transactionNum,String NeedSend) {
		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result=false;
		if (commodityId!=null) {
			Commodity commodity=commodityService.queryCommodity(commodityId);
			if (MerchantStatus.normal.equals(merchantService.queryMerchantStatusByid(commodity.getMerchantId(), null))) {
				if (CommodityStatus.shelved.equals(commodity.getStatus())) {
					Order order=new Order();
					order.setCommodityId(commodityId);
					order.setCommodityNum(commodityNum);
					order.setCommodityType(commodityType);
					order.setPhone(phone);
					order.setRemark(remark);
					order.setInitiator(initiator);
					order.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
					order.setMerchantId(commodityService.queryCommodity(commodityId).getMerchantId());
					order.setOrderId(IdWorker.getId());
					order.setRequestType(requestType);
					order.setSellPrice(commodity.getSellPrice());
					order.setTotalTransaction(commodity.getSellPrice().multiply(new BigDecimal(Double.valueOf(commodityNum.toString()).toString())));
					order.setTransactionNum(transactionNum);
					order.setNeedSend(NeedSend);
					orderService.createOrder(order);
					result=true;
					retMessage.setAll(RetCodeEnum.success,"订单生成成功",result);
				}else {
					retMessage.setAll(RetCodeEnum.fail,"此商品不是上架状态",false);
				}
			}else {
				retMessage.setAll(RetCodeEnum.fail,"此商户不是正常状态",false);
			}
		}
		return retMessage;
	}
	@Override
	public RetMessage<String> queryOrderInfo(String createTime, String transactionNum, String phone, String remark) {
		RetMessage<String> retMessage = new RetMessage<String>();
		if (createTime!=null&&!createTime.isEmpty()&&transactionNum!=null&&!transactionNum.isEmpty()&&remark!=null&&!remark.isEmpty()) {
			Order order=orderService.queryOrderInfo(createTime, transactionNum, phone, remark);
			if (order!=null) {
				String result=order.toJson();
				Map<String,Object>map=new HashMap<String,Object>();
				map.put("Type", order.getCommodityType());
				map.put("SkuCode", order.getCommodityId());
				
				
				retMessage.setAll(RetCodeEnum.success,"订单查询成功",result);
			}else {
				retMessage.setAll(RetCodeEnum.exception,"没有查询到订单相关信息",null);
			}
		}else {
			retMessage.setAll(RetCodeEnum.fail,"信息提供不完整", null);
		}
		return retMessage;
	}

	@Override
	public RetMessage<String> queryOrders(String timeStart, String timeStop, String phone, Integer pageNum,
			Integer pageSize, String remark) {
		RetMessage<String> retMessage = new RetMessage<String>();
			if (timeStart!=null&&!timeStart.isEmpty()&&timeStop!=null&&!timeStop.isEmpty()&&phone!=null&&!phone.isEmpty()&&remark!=null&&!remark.isEmpty()) {
				List<Order> orders=null;
				EntityWrapper<Order> wrapper=new EntityWrapper<Order>();
					wrapper.eq("phone",phone);
					wrapper.eq("remark",remark);
				if (timeStart != null && !timeStart.isEmpty()) 
					while (timeStart.length() < 14) {
						timeStart += "0";
					wrapper.ge("create_time", timeStart);
				}
					while (timeStop.length() < 14) {
						timeStop += "9";
					wrapper.le("create_time", timeStop);
					}
				if (pageNum != null) {
					pageSize = pageSize == null ? ConstConfig.DEFAULT_PAGE_SIZE : pageSize;
					Page<Order> pageObj = new Page<Order>(pageNum, pageSize);
					 orders=orderService.queryOrders(wrapper, pageObj);
					retMessage.setAll(RetCodeEnum.success,"查询历史订单成功",JSON.toJSONString(orders));
				}else {
					 orders=orderService.queryOrders(wrapper, null);
					 retMessage.setAll(RetCodeEnum.success,"查询历史订单成功",JSON.toJSONString(orders));
				}
				
			}else {
				retMessage.setAll(RetCodeEnum.fail,"信息提供不完整",null);
			}
		return retMessage;
	}

}
