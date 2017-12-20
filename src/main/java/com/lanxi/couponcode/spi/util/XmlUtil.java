package com.lanxi.couponcode.spi.util;

import org.apache.log4j.Logger;
import org.dom4j.*;

import java.util.Map;

/**
 * @author wuxiaobo
 */
public class XmlUtil {

    private static Logger log = Logger.getLogger(HttpUtil.class);

    /**
     * 根据节点获取节点的值 但节点必须是从高到低依次的 如 "/JFDH/HEAD/VER" JFDH是最顶层节点
     *
     * @param doc
     * @param strXmlPath
     * @return
     */
    public static String getXmlStrByKey(Document doc, String strXmlPath) {
        Node node = doc.selectSingleNode(strXmlPath);
        if (node == null) {
            return "";
        }
        return node.getText();
    }

    public static Document getDocument(String strXmlContent) throws DocumentException {
        Document doc = DocumentHelper.parseText(strXmlContent);
        return doc;
    }

    /**
     * xmlStr 转换 MsgRechargeBean
     *
     * @param xmlStr
     * @return
     */
    public static MsgRechargeBean getMsgPrepaidRechargeBeanByXmlStr(String xmlStr) {

        Document xmlDoc;
        try {
            xmlDoc = DocumentHelper.parseText(xmlStr);
        } catch (Exception e) {
            log.error("[XmlUtil->MsgRechargeBean] 解析xml string错误, ", e);
            return null;
        }
        MsgRechargeBean msgRechargeBean = new MsgRechargeBean();

        msgRechargeBean.setVER(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/VER"));
        msgRechargeBean.setMsgNo(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/MsgNo"));
        msgRechargeBean.setCHKDate(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/CHKDate"));
        msgRechargeBean.setWorkDate(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/WorkDate"));
        msgRechargeBean.setWorkTime(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/WorkTime"));
        msgRechargeBean.setADD(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/ADD"));
        msgRechargeBean.setSRC(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/SRC"));
        msgRechargeBean.setDES(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/DES"));
        msgRechargeBean.setAPP(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/APP"));
        msgRechargeBean.setMsgID(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/MsgID"));
        msgRechargeBean.setReserve(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/Reserve"));
        msgRechargeBean.setSign(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/Sign"));

		msgRechargeBean.setSerialNum(getXmlStrByKey(xmlDoc, "/JFDH/MSG/SerialNum"));
        msgRechargeBean.setPhone(getXmlStrByKey(xmlDoc, "/JFDH/MSG/Phone"));
        msgRechargeBean.setType(getXmlStrByKey(xmlDoc, "/JFDH/MSG/Type"));
        msgRechargeBean.setSkuCode(getXmlStrByKey(xmlDoc, "/JFDH/MSG/SkuCode"));
        msgRechargeBean.setCount(getXmlStrByKey(xmlDoc, "/JFDH/MSG/Count"));
        msgRechargeBean.setNeedSend(getXmlStrByKey(xmlDoc, "/JFDH/MSG/NeedSend"));
        msgRechargeBean.setRemark(getXmlStrByKey(xmlDoc, "/JFDH/MSG/Remark"));

        return msgRechargeBean;
    }

    /**
     * 生成购买电子券响应xml报文
     *
     * @param msg
     * @param statusMap
     * @return
     */
    public static String getPrepaidRechargeRespXmlStr(MsgRechargeBean msg, Map<String, String> statusMap) {

        try {
            Document xmlDoc = DocumentHelper.createDocument();
            Element jfdh = xmlDoc.addElement("JFDH");
            /** HEAD */
            Element head = jfdh.addElement("HEAD");
            head.addElement("VER").setText(msg.getVER());
            head.addElement("MsgNo").setText(msg.getMsgNo());
            head.addElement("CHKDate").setText(msg.getCHKDate());
            head.addElement("WorkDate").setText(msg.getWorkDate());
            head.addElement("WorkTime").setText(msg.getWorkTime());
            head.addElement("ADD").setText(msg.getADD());
            head.addElement("SRC").setText(msg.getSRC());
            head.addElement("DES").setText(msg.getDES());
            head.addElement("APP").setText(msg.getAPP());
            head.addElement("MsgID").setText(msg.getMsgID());
            head.addElement("Reserve").setText(msg.getReserve());
            head.addElement("Sign").setText(msg.getSign());
            head.addElement("ResCode").setText(statusMap.get("ResCode"));
            head.addElement("ResMsg").setText(statusMap.get("ResMsg"));
            /** MSG */
            Element msgElEment = jfdh.addElement("MSG");
            msgElEment.addElement("TotalAmt").setText(statusMap.get("TotalAmt") == null ? "0" : statusMap.get("TotalAmt"));
            Element skuList = msgElEment.addElement("SkuList");
            if (msg.getCount() != null && Integer.parseInt(msg.getCount()) > 0) {
                for (int i = 0; i < Integer.parseInt(msg.getCount()); i++) {
                    skuList.addElement("Amt").setText(statusMap.get("Amt") == null ? "0" : statusMap.get("Amt"));
                    skuList.addElement("Code")
							.setText(statusMap.get("Code" + (i + 1)) == null ? "" : statusMap.get("Code" + (i + 1)));
                    skuList.addElement("EndTime")
                            .setText(statusMap.get("EndTime") == null ? "" : statusMap.get("EndTime"));

                }
            }

            xmlDoc.setXMLEncoding("GBK");
            return xmlDoc.asXML();
        } catch (Exception e) {
            log.error("生成电子商品返回报文错误, ", e);
            return "";
        }
    }

    /**
     * 根据xml生成MsgOrderInfoHeadBean
     *
     * @param XmlStr
     * @return
     */
    public static MsgOrderInfoHeadBean getMsgOrderInfoHeadBeanByXmlStr(String XmlStr) {
        Document xmlDoc;
        try {
            xmlDoc = DocumentHelper.parseText(XmlStr);

        } catch (Exception e) {
			log.error("[XmlUtil->MsgOrderInfoHeadBean] 解析xml string错误, ", e);
            return null;
        }
        MsgOrderInfoHeadBean msgOrderInfoHeadBean = new MsgOrderInfoHeadBean();
        msgOrderInfoHeadBean.setVER(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/VER"));
        msgOrderInfoHeadBean.setMsgNo(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/MsgNo"));
        msgOrderInfoHeadBean.setCHKDate(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/CHKDate"));
        msgOrderInfoHeadBean.setWorkDate(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/WorkDate"));
        msgOrderInfoHeadBean.setWorkTime(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/WorkTime"));
        msgOrderInfoHeadBean.setADD(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/ADD"));
        msgOrderInfoHeadBean.setSRC(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/SRC"));
        msgOrderInfoHeadBean.setDES(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/DES"));
        msgOrderInfoHeadBean.setAPP(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/APP"));
        msgOrderInfoHeadBean.setMsgID(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/MsgID"));
        msgOrderInfoHeadBean.setReserve(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/Reserve"));
        msgOrderInfoHeadBean.setSign(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/Sign"));
        msgOrderInfoHeadBean.setPhone(getXmlStrByKey(xmlDoc, "/JFDH/MSG/Phone"));
        msgOrderInfoHeadBean.setRemark(getXmlStrByKey(xmlDoc, "/JFDH/MSG/Remark"));
        msgOrderInfoHeadBean.setOrgWorkDate(getXmlStrByKey(xmlDoc, "/JFDH/MSG/OrgWorkDate"));
        msgOrderInfoHeadBean.setOrgMsgID(getXmlStrByKey(xmlDoc, "/JFDH/MSG/OrgMsgID"));
        return msgOrderInfoHeadBean;
    }
	/**
	 * 根据xml生成MsgOrderInfoHeadBean
	 * 用于查询历史订单
	 * @param XmlStr
	 * @return
	 */
	public static MsgOrderInfoHeadBean getMsgOrderInfoHeadBean(String XmlStr) {
		Document xmlDoc;
		try {
			xmlDoc = DocumentHelper.parseText(XmlStr);

		} catch (Exception e) {
			log.error("[XmlUtil->MsgOrderInfoHeadBean] 解析xml string错误, ", e);
			return null;
		}
		MsgOrderInfoHeadBean msgOrderInfoHeadBean = new MsgOrderInfoHeadBean();
		msgOrderInfoHeadBean.setVER(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/VER"));
		msgOrderInfoHeadBean.setMsgNo(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/MsgNo"));
		msgOrderInfoHeadBean.setCHKDate(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/CHKDate"));
		msgOrderInfoHeadBean.setWorkDate(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/WorkDate"));
		msgOrderInfoHeadBean.setWorkTime(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/WorkTime"));
		msgOrderInfoHeadBean.setADD(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/ADD"));
		msgOrderInfoHeadBean.setSRC(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/SRC"));
		msgOrderInfoHeadBean.setDES(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/DES"));
		msgOrderInfoHeadBean.setAPP(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/APP"));
		msgOrderInfoHeadBean.setMsgID(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/MsgID"));
		msgOrderInfoHeadBean.setReserve(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/Reserve"));
		msgOrderInfoHeadBean.setSign(getXmlStrByKey(xmlDoc, "/JFDH/HEAD/Sign"));
		
		msgOrderInfoHeadBean.setStartDate(getXmlStrByKey(xmlDoc, "/JFDH/MSG/StartDate"));
		msgOrderInfoHeadBean.setEndDate(getXmlStrByKey(xmlDoc, "/JFDH/MSG/EndDate"));
		msgOrderInfoHeadBean.setPhone(getXmlStrByKey(xmlDoc, "/JFDH/MSG/Phone"));
		msgOrderInfoHeadBean.setRemark(getXmlStrByKey(xmlDoc, "/JFDH/MSG/Remark"));
		return msgOrderInfoHeadBean;
	}

    /**
     * 生成订单详情响应xml报文
     *
     * @param msg
     * @param statusMap
     * @return
     */
    public static String getMsgOrderInfoResXmlStr(MsgOrderInfoHeadBean msg, Map<String, String> statusMap) {
        try {
            Document xmlDoc = DocumentHelper.createDocument();
            Element jfdh = xmlDoc.addElement("JFDH");
            /** HEAD */
            Element head = jfdh.addElement("HEAD");
            head.addElement("VER").setText(msg.getVER());
            head.addElement("MsgNo").setText(msg.getMsgNo());
            head.addElement("CHKDate").setText(msg.getCHKDate());
            head.addElement("WorkDate").setText(msg.getWorkDate());
            head.addElement("WorkTime").setText(msg.getWorkTime());
            head.addElement("ADD").setText(msg.getADD());
            head.addElement("SRC").setText(msg.getSRC());
            head.addElement("DES").setText(msg.getDES());
            head.addElement("APP").setText(msg.getAPP());
            head.addElement("MsgID").setText(msg.getMsgID());
            head.addElement("Reserve").setText(msg.getReserve());
            head.addElement("Sign").setText(msg.getSign());
            head.addElement("ResCode").setText(statusMap.get("ResCode"));
            head.addElement("ResMsg").setText(statusMap.get("ResMsg"));
            /** MSG */
            Element msgElEment = jfdh.addElement("MSG");
            msgElEment.addElement("OrgWorkDate").setText(msg.getOrgWorkDate());
            msgElEment.addElement("OrgMsgID").setText(msg.getOrgMsgID());
            msgElEment.addElement("Phone").setText(msg.getPhone());
            msgElEment.addElement("Type").setText(statusMap.get("Type") == null ? "0" : statusMap.get("Type"));
            msgElEment.addElement("SkuCode").setText(statusMap.get("SkuCode") == null ? "0" : statusMap.get("SkuCode"));
            msgElEment.addElement("Count").setText(statusMap.get("Count") == null ? "0" : statusMap.get("Count"));
            msgElEment.addElement("TotalAmt")
                    .setText(statusMap.get("TotalAmt") == null ? "0" : statusMap.get("TotalAmt"));
            msgElEment.addElement("Status").setText(statusMap.get("Status") == null ? "" : statusMap.get("Status"));
            Element skuList = msgElEment.addElement("SkuList");
            if (Integer.parseInt(statusMap.get("Count")) > 0) {
                for (int i = 0; i < Integer.parseInt(statusMap.get("Count")); i++) {
                    skuList.addElement("Amt").setText(statusMap.get("Amt") == null ? "0" : statusMap.get("Amt"));
                    skuList.addElement("Code")
							.setText(statusMap.get("Code" + (i + 1)) == null ? "" : statusMap.get("Code" + (i + 1)));
                    skuList.addElement("EndTime")
                            .setText(statusMap.get("EndTime") == null ? "" : statusMap.get("EndTime"));
                }
            } else {
                skuList.addElement("Amt").setText("0");
                skuList.addElement("Code")
                        .setText("");
                skuList.addElement("EndTime")
                        .setText("");
            }
            msgElEment.addElement("Remark").setText(statusMap.get("Remark") == null ? "" : statusMap.get("Remark"));
            xmlDoc.setXMLEncoding("GBK");
            return xmlDoc.asXML();
        } catch (Exception e) {
            log.error("生成订单详情返回报文错误, ", e);
            return "";
        }
    }

    /**
     * 生成历史订单响应xml报文
     *
     * @param msg
     * @param statusMap
     * @return
     */
    public static String getMsgOrderResXml(MsgOrderInfoHeadBean msg, Map<String, String> statusMap) {
        try {
            Document xmlDoc = DocumentHelper.createDocument();
            Element jfdh = xmlDoc.addElement("JFDH");
            /** HEAD */
            Element head = jfdh.addElement("HEAD");
            head.addElement("VER").setText(msg.getVER());
            head.addElement("MsgNo").setText(msg.getMsgNo());
            head.addElement("CHKDate").setText(msg.getCHKDate());
            head.addElement("WorkDate").setText(msg.getWorkDate());
            head.addElement("WorkTime").setText(msg.getWorkTime());
            head.addElement("ADD").setText(msg.getADD());
            head.addElement("SRC").setText(msg.getSRC());
            head.addElement("DES").setText(msg.getDES());
            head.addElement("APP").setText(msg.getAPP());
            head.addElement("MsgID").setText(msg.getMsgID());
            head.addElement("Reserve").setText(msg.getReserve());
            head.addElement("Sign").setText(msg.getSign());
            head.addElement("ResCode").setText(statusMap.get("ResCode"));
            head.addElement("ResMsg").setText(statusMap.get("ResMsg"));
            /** MSG */
            Element msgElEment = jfdh.addElement("MSG");
            msgElEment.addElement("Phone").setText(msg.getPhone());
            msgElEment.addElement("Count").setText(statusMap.get("Count") == null ? "0" : statusMap.get("Count"));
            Element orderList = msgElEment.addElement("OrderList");
            if (Integer.parseInt(statusMap.get("Count")) > 0) {
                for (int i = 0; i < Integer.parseInt(statusMap.get("Count")); i++) {
                    orderList.addElement("OrgWorkDate").setText(
							statusMap.get("OrgWorkDate" + (i + 1)) == null ? "0" : statusMap.get("OrgWorkDate" + (i + 1)));
                    orderList.addElement("OrgMsgID").setText(
							statusMap.get("OrgMsgID" + (i + 1)) == null ? "0" : statusMap.get("OrgMsgID" + (i + 1)));
                    orderList.addElement("Type")
							.setText(statusMap.get("Type" + (i + 1)) == null ? "" : statusMap.get("Type" + (i + 1)));
                    orderList.addElement("SkuCode")
							.setText(statusMap.get("SkuCode" + (i + 1)) == null ? "" : statusMap.get("SkuCode" + (i + 1)));
                }
            } else {
                orderList.addElement("OrgWorkDate").setText("0");
                orderList.addElement("OrgMsgID").setText("0");
                orderList.addElement("Type").setText("");
                orderList.addElement("SkuCode").setText("");

            }
            xmlDoc.setXMLEncoding("GBK");
            return xmlDoc.asXML();
        } catch (Exception e) {
            log.error("生成历史订单返回报文错误, ", e);
            return "";
        }
    }

    /**
     * 递归拆分字符串 存储在map里边
     *
     * @param s
     * @param x   x=1 x必须从1开始
     * @param map
     * @param n
     * @return
     */
    public static Map<String, String> split(String s, int x, Map<String, String> map, String n) {

        int a = s.indexOf("|");
        if (s.length() > a) {
            map.put(n + x, s.substring(0, a));
            x++;
            String m = s.substring(a + 1, s.length());
            if (m.length() > a) {
                split(m, x, map, n);
            } else {
                map.put(n + x, m);
            }
        } else {
            map.put(n + x, s);
        }
        return map;
    }
}
