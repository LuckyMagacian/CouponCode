package com.lanxi.couponcode.impl.util;

import org.apache.log4j.Logger;


import com.lanxi.couponcode.impl.entity.MsgRechargeBean;

/**
 * 验证
 * @author wuxiaobo
 *
 */

public class ValidateUtil {
	 private static Logger log = Logger.getLogger(ValidateUtil.class);
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
	        }

	        return true;
	    }
}
