package com.lanxi.couponcode.view;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lanxi.couponcode.impl.entity.MsgOrderInfoHeadBean;
import com.lanxi.couponcode.impl.entity.MsgRechargeBean;
import com.lanxi.couponcode.impl.entity.ProjectParam;
import com.lanxi.couponcode.impl.util.HttpUtil;
import com.lanxi.couponcode.impl.util.RegularUtil;
import com.lanxi.couponcode.impl.util.ValidateUtil;
import com.lanxi.couponcode.impl.util.XmlUtil;
import com.lanxi.couponcode.spi.service.OrderService;
import com.lanxi.util.entity.LogFactory;

/**
 * 
 * @author wuxiaobo
 *
 */
@Controller
public class XmlApiController {
	@Resource
	private OrderService orderService;

	/**
	 * 购买电子券
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST, produces = { "application/xml;charset=GBK" })
	@ResponseBody
	public String PurchaseOfElectronicGoods(HttpServletRequest req, HttpServletResponse res) {
		try {
			/** 获取请求体 */
			String reqXmlStr = HttpUtil.getBodyFromPostReq(req, "GBK");
			if (reqXmlStr == null) {
				LogFactory.error(this, "获取请求体错误");
				Map<String, String> statusMap = new HashMap<String, String>();
				statusMap.put("ResCode", ProjectParam.RESCODE_ERR);
				statusMap.put("ResMsg", "请求参数错误");
				return XmlUtil.getPrepaidRechargeRespXmlStr(new MsgRechargeBean(), statusMap);
			}
			LogFactory.info(this, "请求体" + reqXmlStr);
			/** 根据xml生成MsgRechargeBean */
			MsgRechargeBean msgRechargeBean = XmlUtil.getMsgPrepaidRechargeBeanByXmlStr(reqXmlStr);
			if (null == msgRechargeBean) {
				LogFactory.error(this, "解析post请求body错误, reqXmlStr: " + reqXmlStr);
				Map<String, String> statusMap = new HashMap<String, String>();
				statusMap.put("ResCode", ProjectParam.RESCODE_ERR);
				statusMap.put("ResMsg", "请求参数错误");
				return XmlUtil.getPrepaidRechargeRespXmlStr(new MsgRechargeBean(), statusMap);
			}
			/** 验签 */
			if (!ValidateUtil.validatePrepaidRechargeSign(msgRechargeBean)) {
				Map<String, String> statusMap = new HashMap<String, String>();
				statusMap.put("ResCode", ProjectParam.RESCODE_ERR);
				statusMap.put("ResMsg", "签名验证失败");
				return XmlUtil.getPrepaidRechargeRespXmlStr(msgRechargeBean, statusMap);
			}
			/** 验证手机号 */
			if (msgRechargeBean.getPhone().length() != 11 || !RegularUtil.isPhone(msgRechargeBean.getPhone())) {
				Map<String, String> statusMap = new HashMap<String, String>();
				statusMap.put("ResCode", ProjectParam.RESCODE_ERR);
				statusMap.put("ResMsg", "手机号码格式不正确");
				return XmlUtil.getPrepaidRechargeRespXmlStr(msgRechargeBean, statusMap);
			}
			/** 验证商品数量 */
			if (!RegularUtil.isNumeric(msgRechargeBean.getCount()) || Integer.valueOf(msgRechargeBean.getCount()) < 0) {
				Map<String, String> statusMap = new HashMap<String, String>();
				statusMap.put("ResCode", ProjectParam.RESCODE_ERR);
				statusMap.put("ResMsg", "商品数量不正确");
				return XmlUtil.getPrepaidRechargeRespXmlStr(msgRechargeBean, statusMap);
			}
			/** 验证交易序号 */
			if (msgRechargeBean.getMsgID().length() > 8) {
				Map<String, String> statusMap = new HashMap<String, String>();
				statusMap.put("ResCode", ProjectParam.RESCODE_ERR);
				statusMap.put("ResMsg", "交易序号不能超过8位");
				return XmlUtil.getPrepaidRechargeRespXmlStr(msgRechargeBean, statusMap);
			}
			/** 调用生成串码 */

		} catch (Exception e) {
			LogFactory.error(this, "操作失败", e);
			Map<String, String> statusMap = new HashMap<String, String>();
			statusMap.put("ResCode", ProjectParam.RESCODE_ERR);
			statusMap.put("ResMsg", "操作失败");
			return XmlUtil.getPrepaidRechargeRespXmlStr(new MsgRechargeBean(), statusMap);
		}
		return null;
	}

	/**
	 * 查询订单明细
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST, produces = { "application/xml;charset=GBK" })
	@ResponseBody
	public String enquiryOfOrderDetail(HttpServletRequest req, HttpServletResponse res) {
		try {
			/** 获取请求体 */
			String reqXmlStr = HttpUtil.getBodyFromPostReq(req, "GBK");
			if (reqXmlStr == null) {
				LogFactory.error(this, "获取请求体错误");
				Map<String, String> statusMap = new HashMap<String, String>();
				statusMap.put("ResCode", ProjectParam.RESCODE_ERR);
				statusMap.put("ResMsg", "请求参数错误");
				return XmlUtil.getMsgOrderInfoResXmlStr(new MsgOrderInfoHeadBean(), statusMap);
			}
			LogFactory.info(this, "请求体" + reqXmlStr);
			/** 根据请求体生成对应的MsgOrderInfoHeadBean */
			MsgOrderInfoHeadBean msgOrderInfoHeadBean = XmlUtil.getMsgOrderInfoHeadBeanByXmlStr(reqXmlStr);
			if (msgOrderInfoHeadBean == null) {
				LogFactory.error(this, "解析post请求body错误, reqXmlStr: " + reqXmlStr);
				Map<String, String> statusMap = new HashMap<String, String>();
				statusMap.put("ResCode", ProjectParam.RESCODE_ERR);
				statusMap.put("ResMsg", "请求参数错误");
				return XmlUtil.getMsgOrderInfoResXmlStr(msgOrderInfoHeadBean, statusMap);
			}
			/** 验签 */
			if (!ValidateUtil.validateOrderInfoSign(msgOrderInfoHeadBean)) {
				Map<String, String> statusMap = new HashMap<String, String>();
				statusMap.put("ResCode", ProjectParam.RESCODE_ERR);
				statusMap.put("ResMsg", "签名验证失败");
				return XmlUtil.getMsgOrderInfoResXmlStr(msgOrderInfoHeadBean, statusMap);
			}
			/**调用查询订单详情*/
			orderService.queryOrderInfo(msgOrderInfoHeadBean.getOrgWorkDate(), msgOrderInfoHeadBean.getOrgMsgID(), msgOrderInfoHeadBean.getPhone(), msgOrderInfoHeadBean.getRemark());
			
		} catch (Exception e) {
			LogFactory.error(this, "操作失败", e);
			Map<String, String> statusMap = new HashMap<String, String>();
			statusMap.put("ResCode", ProjectParam.RESCODE_ERR);
			statusMap.put("ResMsg", "操作失败");
			return XmlUtil.getMsgOrderInfoResXmlStr(new MsgOrderInfoHeadBean(), statusMap);
		}
		return null;
	}
	/**
	 * 查询历史订单
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST, produces = { "application/xml;charset=GBK" })
	@ResponseBody
	public String queryHistoryOrder(HttpServletRequest req, HttpServletResponse res) {
		try {
			/** 获取请求体 */
			String reqXmlStr = HttpUtil.getBodyFromPostReq(req, "GBK");
			if (reqXmlStr == null) {
				LogFactory.error(this, "获取请求体错误");
				Map<String, String> statusMap = new HashMap<String, String>();
				statusMap.put("ResCode", ProjectParam.RESCODE_ERR);
				statusMap.put("ResMsg", "请求参数错误");
				return XmlUtil.getMsgOrderResXml(new MsgOrderInfoHeadBean(), statusMap);
			}
			LogFactory.info(this, "请求体" + reqXmlStr);
			/** 根据请求体生成对应的MsgOrderInfoHeadBean */
			MsgOrderInfoHeadBean msgOrderInfoHeadBean = XmlUtil.getMsgOrderInfoHeadBeanByXmlStr(reqXmlStr);
			if (msgOrderInfoHeadBean == null) {
				LogFactory.error(this, "解析post请求body错误, reqXmlStr: " + reqXmlStr);
				Map<String, String> statusMap = new HashMap<String, String>();
				statusMap.put("ResCode", ProjectParam.RESCODE_ERR);
				statusMap.put("ResMsg", "请求参数错误");
				return XmlUtil.getMsgOrderResXml(msgOrderInfoHeadBean, statusMap);
			}
			/** 验签 */
			if (!ValidateUtil.validateOrderInfoSign(msgOrderInfoHeadBean)) {
				Map<String, String> statusMap = new HashMap<String, String>();
				statusMap.put("ResCode", ProjectParam.RESCODE_ERR);
				statusMap.put("ResMsg", "签名验证失败");
				return XmlUtil.getMsgOrderResXml(msgOrderInfoHeadBean, statusMap);
			}
			/**验证手机号*/
			if (!RegularUtil.isPhone(msgOrderInfoHeadBean.getPhone())) {
				Map<String, String> statusMap = new HashMap<String, String>();
				statusMap.put("ResCode", ProjectParam.RESCODE_ERR);
				statusMap.put("ResMsg", "手机号码格式不正确");
				return XmlUtil.getMsgOrderResXml(msgOrderInfoHeadBean, statusMap);
			}
			/**调用生成历史订单*/
		} catch (Exception e) {
			LogFactory.error(this, "操作失败", e);
			Map<String, String> statusMap = new HashMap<String, String>();
			statusMap.put("ResCode", ProjectParam.RESCODE_ERR);
			statusMap.put("ResMsg", "操作失败");
			return XmlUtil.getMsgOrderResXml(new MsgOrderInfoHeadBean(), statusMap);
		}
		return null;
	}
}
