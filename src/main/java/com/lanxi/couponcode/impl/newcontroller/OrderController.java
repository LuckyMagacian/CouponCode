package com.lanxi.couponcode.impl.newcontroller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.lanxi.couponcode.impl.entity.Commodity;
import com.lanxi.couponcode.impl.entity.CouponCode;
import com.lanxi.couponcode.impl.entity.Merchant;
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
import com.lanxi.couponcode.spi.consts.enums.OperateType;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.couponcode.spi.service.CouponService;
import com.lanxi.util.entity.LogFactory;
import com.lanxi.util.utils.LoggerUtil;
import static com.lanxi.couponcode.impl.assist.PredicateAssist.*;

/**
 * 
 * @author wuxiaobo
 *
 */
@EasyLog(LoggerUtil.LogLevel.INFO)
@Controller("orderControllerService")
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
	@Resource
	private CouponService couponService;

	@Override
	public RetMessage<String> addOrder(String Phone, CommodityType Type, Long SkuCode, Integer Count, String Remark,
			String SRC, String requestType, String MsgID, String NeedSend, String WorkDate, String WorkTime,
			String CHKDate) {
		RetMessage<String> retMessage = null;
		try {
			retMessage = new RetMessage<String>();
			if (SkuCode != null) {
				Commodity commodity = commodityService.queryCommodity(SkuCode);
				Merchant mer = merchantService.queryMerchantParticularsById(commodity.getMerchantId());
				RetMessage message = checkMerchant.apply(mer, OperateType.createCouponCode);
				if (notNull.test(message))
					return message;
				if (CommodityStatus.shelved.equals(commodity.getStatus())) {
					// 判断交易序号是否重复
					if (orderService.isRepetition(MsgID, WorkDate)) {
						retMessage.setAll(RetCodeEnum.exception, "交易序号重复", null);
					} else {
						Order order = new Order();
						order.setSkuCode(SkuCode);
						order.setCount(Count);
						order.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
						order.setType(Type);
						order.setPhone(Phone);
						order.setCHKDate(CHKDate);
						order.setRemark(Remark);
						order.setSRC(SRC);
						order.setOrderStatus("0");
						order.setWorkDate(WorkDate);
						order.setWorkTime(WorkTime);
						order.setMerchantId(commodityService.queryCommodity(SkuCode).getMerchantId());
						order.setOrderId(IdWorker.getId());
						order.setRequestType(requestType);
						order.setAmt(commodity.getSellPrice());
						order.setMsgID(MsgID);
						order.setNeedSend(NeedSend);
						Boolean b = orderService.createOrder(order);
						if (b) {
							// 调用接口生成串码
							StringBuilder stringBuilder = new StringBuilder();
							// 当count==1 直接添加>1则for循环
							if (Count > 1) {
								int m = 0;
								for (int i = 0; i < Count; i++) {
									// 循环调用生成串码的接口结果以"|"为分隔添加到stringBuilder中
									RetMessage<String> retMessage2 = couponService
											.generateCode(commodity.getCommodityId(), SkuCode, "客户购买", null);

									if (RetCodeEnum.success.equals(retMessage2.getRetCode())) {
										CouponCode couponCode = JSON.parseObject(retMessage2.getDetail(),
												CouponCode.class);
										stringBuilder.append(String.valueOf(couponCode.getCode()) + "|");
										order.setEndTime(couponCode.getOverTime());
										m++;
									} else {
										RetMessage<String> retMessage3 = null;
										for (int n = 0; n < 8; n++) {
											retMessage3 = couponService.generateCode(commodity.getCommodityId(),
													SkuCode, "客户购买", null);
											if (RetCodeEnum.success.equals(retMessage3.getRetCode())) {
												CouponCode couponCode = JSON.parseObject(retMessage3.getDetail(),
														CouponCode.class);
												stringBuilder.append(String.valueOf(couponCode.getCode()) + "|");
												order.setEndTime(couponCode.getOverTime());
												m++;
												break;
											}
										}

									}
								}
								if (m == 0) {
									retMessage.setAll(RetCodeEnum.exception, "串码生成失败", null);
									return retMessage;
								} else if (m == 1) {
									order.setCode(stringBuilder.toString().substring(0,
											stringBuilder.toString().lastIndexOf("|")));
									order.setOrderStatus("2");
									order.setSuccessNum(m);
									order.setTotalAmt(commodity.getSellPrice().multiply(
											new BigDecimal(m)));
									Boolean result = orderService.changeOrderStatus(order);
									if (result) {
										String s = JSON.toJSONString(order);
										retMessage.setAll(RetCodeEnum.success, "串码生成部分成功", s);
										return retMessage;
									} else {
										retMessage.setAll(RetCodeEnum.exception, "串码生成失败", null);
										return retMessage;
									}

								} else if (m < 8) {
									order.setCode(stringBuilder.toString());
									order.setOrderStatus("2");
									order.setSuccessNum(m);
									order.setTotalAmt(commodity.getSellPrice().multiply(
											new BigDecimal(m)));
									Boolean result = orderService.changeOrderStatus(order);
									if (result) {
										String s = JSON.toJSONString(order);
										retMessage.setAll(RetCodeEnum.success, "串码生成部分成功", s);
										return retMessage;
									} else {
										retMessage.setAll(RetCodeEnum.exception, "串码生成失败", null);
										return retMessage;
									}

								}

							} else if (Count == 1) {

								// 调用一次生成串码接口
								RetMessage<String> retMessage2 = couponService.generateCode(commodity.getCommodityId(),
										SkuCode, "客户购买", null);
								// 判断串码生成是否成功
								if (RetCodeEnum.success.equals(retMessage2.getRetCode())) {
									CouponCode couponCode = JSON.parseObject(retMessage2.getDetail(), CouponCode.class);
									stringBuilder.append(String.valueOf(couponCode.getCode()));
									order.setEndTime(couponCode.getOverTime());
								} else {
									// 当当前次生成串码失败则再次生成 若连续失败8次则为失败
									RetMessage<String> retMessage3 = null;
									for (int i = 0; i < 8; i++) {
										retMessage3 = couponService.generateCode(commodity.getCommodityId(), SkuCode,
												"客户购买", null);

										if (RetCodeEnum.success.equals(retMessage3.getRetCode())) {

											break;
										}
									}
									if (RetCodeEnum.success.equals(retMessage3.getRetCode())) {
										CouponCode couponCode = JSON.parseObject(retMessage2.getDetail(),
												CouponCode.class);
										stringBuilder.append(String.valueOf(couponCode.getCode()));
										order.setEndTime(couponCode.getOverTime());
									} else {
										retMessage.setAll(RetCodeEnum.exception, "订单生成失败", null);
										return retMessage;
									}

								}
							}
							order.setCode(stringBuilder.toString());
							order.setSuccessNum(Count);
							order.setOrderStatus("1");
							Boolean result = orderService.changeOrderStatus(order);
							if (result) {
								String s = JSON.toJSONString(order);
								retMessage.setAll(RetCodeEnum.success, "订单生成成功", s);
							} else {
								retMessage.setAll(RetCodeEnum.exception, "订单生成失败", null);
							}
						} else {
							retMessage.setAll(RetCodeEnum.exception, "订单生成失败", null);
						}
					}

				} else {
					retMessage.setAll(RetCodeEnum.fail, "此商品不是上架状态", null);
				}

			}
		} catch (Exception e) {
			LogFactory.error(this, "生成订单时发生异常", e);
			retMessage.setAll(RetCodeEnum.error, "生成订单失败", null);
		}

		return retMessage;
	}

	@Override
	public RetMessage<String> queryOrderInfo(String OrgWorkDate, String OrgMsgID, String Phone, String Remark) {
		RetMessage<String> retMessage = null;
		try {
			retMessage = new RetMessage<String>();
			if (OrgWorkDate != null && !OrgWorkDate.isEmpty() && OrgMsgID != null && !OrgMsgID.isEmpty()
					&& Remark != null && !Remark.isEmpty()) {
				EntityWrapper<Order> wrapper = new EntityWrapper<Order>();
				wrapper.eq("msg_id", OrgMsgID);
				if (Phone != null && !Phone.isEmpty()) {
					wrapper.eq("phone", Phone);
				}
				wrapper.like("remark", Remark);
				wrapper.eq("work_date", OrgWorkDate);
				Order order = orderService.queryOrderInfo(wrapper);
				if (order == null) {
					retMessage.setAll(RetCodeEnum.success, "没有查询到任何订单相关信息", null);
				} else {
					String result = order.toJson();
					retMessage.setAll(RetCodeEnum.success, "订单查询成功", result);
				}
			} else {
				retMessage.setAll(RetCodeEnum.fail, "信息提供不完整", null);
			}

		} catch (Exception e) {
			retMessage.setAll(RetCodeEnum.error, "没有查询到订单相关信息", null);
		}
		return retMessage;
	}

	@Override
	public RetMessage<String> queryOrders(String StartDate, String EndDate, String Phone, String Remark) {
		RetMessage<String> retMessage = null;
		try {
			retMessage = new RetMessage<String>();
			if (StartDate != null && !StartDate.isEmpty() && EndDate != null && !EndDate.isEmpty() && Phone != null
					&& !Phone.isEmpty() && Remark != null && !Remark.isEmpty()) {
				List<Order> orders = null;
				EntityWrapper<Order> wrapper = new EntityWrapper<Order>();
				wrapper.eq("phone", Phone);
				wrapper.like("remark", Remark);
				wrapper.ge("start_date", StartDate);
				wrapper.le("start_date", EndDate);
				orders = orderService.queryOrders(wrapper);
				if (orders.size() < 1) {
					retMessage.setAll(RetCodeEnum.success, "没有查询到任何历史订单", null);
				} else {
					retMessage.setAll(RetCodeEnum.success, "查询历史订单成功", JSON.toJSONString(orders));
				}

			} else {
				retMessage.setAll(RetCodeEnum.fail, "信息提供不完整", null);
			}
		} catch (Exception e) {
			retMessage.setAll(RetCodeEnum.error, "查询发生异常", null);
		}
		return retMessage;
	}

}
