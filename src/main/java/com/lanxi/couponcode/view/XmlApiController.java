package com.lanxi.couponcode.view;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.config.ProjectParam;
import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.couponcode.spi.consts.enums.CommodityType;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.couponcode.spi.service.OrderService;
import com.lanxi.couponcode.spi.util.*;
import com.lanxi.util.entity.LogFactory;
import com.lanxi.util.utils.LoggerUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wuxiaobo
 */
@Controller
@RequestMapping ("xmlApi")
@EasyLog (LoggerUtil.LogLevel.INFO)
public class XmlApiController {
    @Resource (name = "orderControllerServiceRef")
    private OrderService orderService;

    /**
     * 购买电子券
     *
     * @param req
     * @param res
     * @return
     */
    @RequestMapping (value = "purchaseOfElectronicGoods", method = RequestMethod.POST, produces = {"application/xml;charset=GBK"})
    @ResponseBody
    public String purchaseOfElectronicGoods(HttpServletRequest req, HttpServletResponse res) {
        try {
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
                    statusMap.put("ResCode", ProjectParam.RESCODE_SIGN_ERR);
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
                if (!RegularUtil.isNumeric(msgRechargeBean.getCount()) || Integer.parseInt(msgRechargeBean.getCount()) < 1) {
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
                /** 先生成订单订单状态为未完成 再生成串码插入订单 订单状态修改为完成 */
                /** 调用生成串码 */
                RetMessage<String> retMessage = orderService.addOrder(msgRechargeBean.getPhone(), CommodityType.getType(msgRechargeBean.getType()), Long.valueOf(msgRechargeBean.getSkuCode()),
                        Integer.valueOf(msgRechargeBean.getCount()), msgRechargeBean.getRemark(), msgRechargeBean.getSRC(),
                        "POST", msgRechargeBean.getMsgID(), msgRechargeBean.getNeedSend(), msgRechargeBean.getWorkDate(), msgRechargeBean.getWorkTime(),
                        msgRechargeBean.getCHKDate(), msgRechargeBean.getSerialNum());
                if (null != retMessage.getDetail()) {
                    JSONObject jsonObject = JSON.parseObject((String) retMessage.getDetail());
                    if (jsonObject.getString("count").equals(jsonObject.getString("successNum"))) {
                        Map<String, String> statusMap = new HashMap<String, String>();
                        statusMap.put("ResCode", ProjectParam.RESCODE_SUCC);
                        statusMap.put("ResMsg", "交易成功");
                        statusMap.put("TotalAmt", jsonObject.getString("cotalAmt"));
                        if (jsonObject.getString("code").lastIndexOf("|") < 0) {
                            statusMap.put("Code1", jsonObject.getString("code"));
                        } else {
                            statusMap = XmlUtil.split(jsonObject.getString("code").substring(0, jsonObject.getString("code").lastIndexOf("|")), 1, statusMap, "Code");
                        }
                        statusMap.put("Amt", jsonObject.getString("amt"));
                        statusMap.put("EndTime", jsonObject.getString("endTime"));
                        return XmlUtil.getPrepaidRechargeRespXmlStr(msgRechargeBean, statusMap);

                    } else if (Integer.valueOf(jsonObject.getString("successNum"))>0&&Integer.valueOf(jsonObject.getString("successNum")) < Integer.valueOf(jsonObject.getString("count"))) {
                        Map<String, String> statusMap = new HashMap<String, String>();
                        statusMap.put("ResCode", ProjectParam.RESCODE_ERR);
                        statusMap.put("ResMsg", "交易部分成功-成功笔数:" + jsonObject.getString("successNum"));
                        statusMap.put("TotalAmt", jsonObject.getString("totalAmt"));
                        if (jsonObject.getString("code").lastIndexOf("|") < 0) {
                            statusMap.put("Code1", jsonObject.getString("code"));
                        } else {
                            statusMap = XmlUtil.split(jsonObject.getString("code").substring(0, jsonObject.getString("code").lastIndexOf("|")), 1, statusMap, "Code");
                        }
                        statusMap.put("Amt", jsonObject.getString("amt"));
                        statusMap.put("EndTime", jsonObject.getString("endTime"));
                        return XmlUtil.getPrepaidRechargeRespXmlStr(msgRechargeBean, statusMap);
                    } else {
                        Map<String, String> statusMap = new HashMap<String, String>();
                        statusMap.put("ResCode", ProjectParam.RESCODE_ERR);
                        statusMap.put("ResMsg", retMessage.getRetMessage());
                        return XmlUtil.getPrepaidRechargeRespXmlStr(msgRechargeBean, statusMap);
                    }
                } else {
                    Map<String, String> statusMap = new HashMap<String, String>();
                    statusMap.put("ResCode", ProjectParam.RESCODE_ERR);
                    statusMap.put("ResMsg", retMessage.getRetMessage());
                    return XmlUtil.getPrepaidRechargeRespXmlStr(msgRechargeBean, statusMap);
                }

            } catch (Exception e) {
                LogFactory.error(this, "操作失败", e);
                Map<String, String> statusMap = new HashMap<String, String>();
                statusMap.put("ResCode", ProjectParam.RESCODE_ERR);
                statusMap.put("ResMsg", "操作失败");
                return XmlUtil.getPrepaidRechargeRespXmlStr(new MsgRechargeBean(), statusMap);
            }
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统异常,稍后再试!",null).toJson();
        }

    }

    /**
     * 查询订单明细
     *
     * @param req
     * @param res
     * @return
     */
    @RequestMapping (value = "enquiryOfOrderDetail", method = RequestMethod.POST, produces = {"application/xml;charset=GBK"})
    @ResponseBody
    public String enquiryOfOrderDetail(HttpServletRequest req, HttpServletResponse res) {
        try {
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
                    statusMap.put("ResCode", ProjectParam.RESCODE_SIGN_ERR);
                    statusMap.put("ResMsg", "签名验证失败");
                    return XmlUtil.getMsgOrderInfoResXmlStr(msgOrderInfoHeadBean, statusMap);
                }
                /** 调用查询订单详情 */
                RetMessage<String> retMessage = orderService.queryOrderInfo(msgOrderInfoHeadBean.getOrgWorkDate(),
                        msgOrderInfoHeadBean.getOrgMsgID(), msgOrderInfoHeadBean.getPhone(),
                        msgOrderInfoHeadBean.getRemark());
                if (retMessage.getDetail() == null) {
                    if (RetCodeEnum.fail.getValue().equals(retMessage.getRetCode())) {
                        Map<String, String> statusMap = new HashMap<String, String>();
                        statusMap.put("ResCode", ProjectParam.RESCODE_ERR);
                        statusMap.put("ResMsg", "请求参数错误");
                        return XmlUtil.getMsgOrderInfoResXmlStr(msgOrderInfoHeadBean, statusMap);
                    } else if (RetCodeEnum.success.getValue().equals(retMessage.getRetCode())) {
                        Map<String, String> statusMap = new HashMap<String, String>();
                        statusMap.put("ResCode", ProjectParam.RESCODE_SUCC);
                        statusMap.put("ResMsg", "没有查询到相关订单");
                        return XmlUtil.getMsgOrderInfoResXmlStr(msgOrderInfoHeadBean, statusMap);
                    } else {
                        Map<String, String> statusMap = new HashMap<String, String>();
                        statusMap.put("ResCode", ProjectParam.RESCODE_ERR);
                        statusMap.put("ResMsg", "没有查询到订单相关信息");
                        return XmlUtil.getMsgOrderInfoResXmlStr(msgOrderInfoHeadBean, statusMap);
                    }

                } else {
                    Map<String, String> statusMap = new HashMap<String, String>();
                    JSONObject jsonObject = JSON.parseObject((String) retMessage.getDetail());
                    statusMap.put("ResCode", ProjectParam.RESCODE_SUCC);
                    statusMap.put("ResMsg", "查询成功");
                    statusMap.put("Type", jsonObject.getString("type"));
                    statusMap.put("SkuCode", jsonObject.getString("skuCode"));

                    statusMap.put("TotalAmt", jsonObject.getString("totalAmt"));
                    statusMap.put("SkuCode", jsonObject.getString("skuCode"));
                    statusMap.put("Status", jsonObject.getString("orderStatus"));
                    if (jsonObject.getString("count") == null || jsonObject.getString("count").equals("")) {
                        statusMap.put("Count", "0");
                    } else {
                        statusMap.put("Count", jsonObject.getString("count"));
                    }
                    if (Integer.parseInt(statusMap.get("Count")) > 0) {
                        if (jsonObject.getString("Code").lastIndexOf("|") < 0) {
                            statusMap.put("Code1", jsonObject.getString("code"));
                        } else {
                            statusMap = XmlUtil.split(jsonObject.getString("code").substring(0, jsonObject.getString("code").lastIndexOf("|")), 1, statusMap, "Code");
                        }

                    }
                    statusMap.put("Amt", jsonObject.getString("amt"));
                    statusMap.put("EndTime", jsonObject.getString("endTime"));
                    statusMap.put("Remark", jsonObject.getString("remark"));
                    return XmlUtil.getMsgOrderInfoResXmlStr(msgOrderInfoHeadBean, statusMap);

                }

            } catch (Exception e) {
                LogFactory.error(this, "操作失败", e);
                Map<String, String> statusMap = new HashMap<String, String>();
                statusMap.put("ResCode", ProjectParam.RESCODE_ERR);
                statusMap.put("ResMsg", "操作失败");
                return XmlUtil.getMsgOrderInfoResXmlStr(new MsgOrderInfoHeadBean(), statusMap);
            }
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统异常,稍后再试!",null).toJson();
        }
    }

    /**
     * 查询历史订单
     *
     * @param req
     * @param res
     * @return
     */
    @RequestMapping (value = "queryHistoryOrder", method = RequestMethod.POST, produces = {"application/xml;charset=GBK"})
    @ResponseBody
    public String queryHistoryOrder(HttpServletRequest req, HttpServletResponse res) {
        try {
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
                MsgOrderInfoHeadBean msgOrderInfoHeadBean = XmlUtil.getMsgOrderInfoHeadBean(reqXmlStr);
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
                    statusMap.put("ResCode", ProjectParam.RESCODE_SIGN_ERR);
                    statusMap.put("ResMsg", "签名验证失败");
                    return XmlUtil.getMsgOrderResXml(msgOrderInfoHeadBean, statusMap);
                }
                /** 验证手机号 */
                if (!RegularUtil.isPhone(msgOrderInfoHeadBean.getPhone())) {
                    Map<String, String> statusMap = new HashMap<String, String>();
                    statusMap.put("ResCode", ProjectParam.RESCODE_ERR);
                    statusMap.put("ResMsg", "手机号码格式不正确");
                    return XmlUtil.getMsgOrderResXml(msgOrderInfoHeadBean, statusMap);
                }
                /** 调用生成历史订单 */
                RetMessage<String> retMessage = orderService.queryOrders(msgOrderInfoHeadBean.getStartDate(),
                        msgOrderInfoHeadBean.getEndDate(), msgOrderInfoHeadBean.getPhone(),
                        msgOrderInfoHeadBean.getRemark());
                if (retMessage.getDetail() == null) {
                    if (RetCodeEnum.fail.getValue().equals(retMessage.getRetCode())) {
                        Map<String, String> statusMap = new HashMap<String, String>();
                        statusMap.put("ResCode", ProjectParam.RESCODE_ERR);
                        statusMap.put("ResMsg", "请求参数错误");
                        return XmlUtil.getMsgOrderResXml(msgOrderInfoHeadBean, statusMap);
                    } else if (RetCodeEnum.success.getValue().equals(retMessage.getRetCode())) {
                        Map<String, String> statusMap = new HashMap<String, String>();
                        statusMap.put("ResCode", ProjectParam.RESCODE_SUCC);
                        statusMap.put("ResMsg", "没有查询到任何订单信息");
                        return XmlUtil.getMsgOrderResXml(msgOrderInfoHeadBean, statusMap);
                    } else {
                        Map<String, String> statusMap = new HashMap<String, String>();
                        statusMap.put("ResCode", ProjectParam.RESCODE_ERR);
                        statusMap.put("ResMsg", "没有查询到订单相关信息");
                        return XmlUtil.getMsgOrderResXml(msgOrderInfoHeadBean, statusMap);
                    }
                } else {
                    JSONArray jsonArray = JSON.parseArray((String) retMessage.getDetail());
                    if (jsonArray.size() > 0) {
                        Map<String, String> statusMap = new HashMap<String, String>();
                        statusMap.put("ResCode", ProjectParam.RESCODE_SUCC);
                        statusMap.put("ResMsg", "查询成功");
                        statusMap.put("Count", String.valueOf(jsonArray.size()));
                        for (int i = 0; i < jsonArray.size(); i++) {
                            statusMap.put("OrgWorkDate" + (i + 1), jsonArray.getJSONObject(i).getString("workDate"));
                            statusMap.put("OrgMsgID" + (i + 1),  jsonArray.getJSONObject(i).getString("msgID"));
                            statusMap.put("Type" + (i + 1),jsonArray.getJSONObject(i).getString("type"));
                            statusMap.put("SkuCode" +( i + 1),jsonArray.getJSONObject(i).getString("skuCode"));
                        }
                        return XmlUtil.getMsgOrderResXml(msgOrderInfoHeadBean, statusMap);
                    } else {
                        Map<String, String> statusMap = new HashMap<String, String>();
                        statusMap.put("ResCode", ProjectParam.RESCODE_ERR);
                        statusMap.put("ResMsg", "没有查询到订单相关信息");
                        statusMap.put("Count", "0");
                        return XmlUtil.getMsgOrderResXml(msgOrderInfoHeadBean, statusMap);
                    }
                }
            } catch (Exception e) {
                LogFactory.error(this, "操作失败", e);
                Map<String, String> statusMap = new HashMap<String, String>();
                statusMap.put("ResCode", ProjectParam.RESCODE_ERR);
                statusMap.put("ResMsg", "操作失败");
                return XmlUtil.getMsgOrderResXml(new MsgOrderInfoHeadBean(), statusMap);
            }
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统异常,稍后再试!",null).toJson();
        }
    }
}

