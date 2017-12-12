package com.lanxi.couponcode.spi.util;

import org.apache.log4j.Logger;

/**
 * 验证
 * @author wuxiaobo
 *
 */

public class ValidateUtil {
	 private static Logger log = Logger.getLogger(ValidateUtil.class);
	 /**
	  * 验证购买电子券串码签名
	  * @param msgRechargeBean
	  * @return
	  */
	 public static boolean validatePrepaidRechargeSign(MsgRechargeBean msgRechargeBean) {

	        StringBuilder signParamSB = new StringBuilder();
	        /** HEAD */
	        signParamSB.append(msgRechargeBean.getVER()).append(msgRechargeBean.getMsgNo())
	                .append(msgRechargeBean.getCHKDate()).append(msgRechargeBean.getWorkDate())
	                .append(msgRechargeBean.getWorkTime()).append(msgRechargeBean.getADD())
	                .append(msgRechargeBean.getSRC()).append(msgRechargeBean.getDES())
	                .append(msgRechargeBean.getAPP()).append(msgRechargeBean.getMsgID())
	                .append(msgRechargeBean.getReserve());
	        /** MSG */
	        signParamSB.append(msgRechargeBean.getPhone()).append(msgRechargeBean.getType())
	                .append(msgRechargeBean.getSkuCode()).append(msgRechargeBean.getCount())
	                .append(msgRechargeBean.getNeedSend()).append(msgRechargeBean.getRemark());
	        String sign = null;
	        try {
	            sign = MD5Util.md5LowerCase(signParamSB.toString());
	        } catch (Exception e) {
	            log.error("验证签名生成失败, ", e);
	            return false;
	        }
	        log.info("验签明文: " + signParamSB.toString() + ", req sign: " + msgRechargeBean.getSign() +
	                ", validate sign: " + sign);
	        if (!sign.equals(msgRechargeBean.getSign())) {
	            return false;
	        }else {
				return true;
			}
	    }
	 /**
	  * 验证订单详情签名
	  * @param msgOrderInfoHeadBean
	  * @return
	  */
	 public static Boolean validateOrderInfoSign(MsgOrderInfoHeadBean msgOrderInfoHeadBean) {
		 StringBuilder signParamSB = new StringBuilder();
		 /**HEAD*/
		 signParamSB.append(msgOrderInfoHeadBean.getVER()).append(msgOrderInfoHeadBean.getMsgNo())
         .append(msgOrderInfoHeadBean.getCHKDate()).append(msgOrderInfoHeadBean.getWorkDate())
         .append(msgOrderInfoHeadBean.getWorkTime()).append(msgOrderInfoHeadBean.getADD())
         .append(msgOrderInfoHeadBean.getSRC()).append(msgOrderInfoHeadBean.getDES())
         .append(msgOrderInfoHeadBean.getAPP()).append(msgOrderInfoHeadBean.getMsgID())
         .append(msgOrderInfoHeadBean.getReserve());
		 /**MSG*/
		 signParamSB.append(msgOrderInfoHeadBean.getOrgWorkDate()).append(msgOrderInfoHeadBean.getOrgMsgID())
		 .append(msgOrderInfoHeadBean.getPhone()).append(msgOrderInfoHeadBean.getRemark());
		 String sign=null;
		 try {
			sign= MD5Util.md5LowerCase(signParamSB.toString());
		} catch (Exception e) {
			log.error("验证签名生成失败,",e);
			return false;
		}
		 log.info("验签明文："+signParamSB.toString()+",req sign:"+msgOrderInfoHeadBean.getSign()+",validate sign:"+sign);
		 if (!sign.equals(msgOrderInfoHeadBean.getSign())) {
			return false;
		}else {
			return true;
		}
	 }
	 /**
	  * 验证历史订单签名
	  * @param msgOrderInfoHeadBean
	  * @return
	  */
	 public static Boolean validateOrderHistorySign(MsgOrderInfoHeadBean msgOrderInfoHeadBean) {
		 StringBuilder signParamSB = new StringBuilder();
		 /**HEAD*/
		 signParamSB.append(msgOrderInfoHeadBean.getVER()).append(msgOrderInfoHeadBean.getMsgNo())
         .append(msgOrderInfoHeadBean.getCHKDate()).append(msgOrderInfoHeadBean.getWorkDate())
         .append(msgOrderInfoHeadBean.getWorkTime()).append(msgOrderInfoHeadBean.getADD())
         .append(msgOrderInfoHeadBean.getSRC()).append(msgOrderInfoHeadBean.getDES())
         .append(msgOrderInfoHeadBean.getAPP()).append(msgOrderInfoHeadBean.getMsgID())
         .append(msgOrderInfoHeadBean.getReserve());
		 /**MSG*/
		 signParamSB.append(msgOrderInfoHeadBean.getStartDate()).append(msgOrderInfoHeadBean.getEndDate())
		 .append(msgOrderInfoHeadBean.getPhone()).append(msgOrderInfoHeadBean.getRemark());
		 String sign=null;
		 try {
			sign= MD5Util.md5LowerCase(signParamSB.toString());
		} catch (Exception e) {
			log.error("验证签名生成失败,",e);
			return false;
		}
		 log.info("验签明文："+signParamSB.toString()+",req sign:"+msgOrderInfoHeadBean.getSign()+",validate sign:"+sign);
		 if (!sign.equals(msgOrderInfoHeadBean.getSign())) {
			return false;
		}else {
			return true;
		}
	 }
}
