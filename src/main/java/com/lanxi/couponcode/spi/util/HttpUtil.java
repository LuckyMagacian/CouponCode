package com.lanxi.couponcode.spi.util;

import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;


/**
 * 
 * @author wuxiaobo
 *
 */
public class HttpUtil {
	private static Logger log = Logger.getLogger(HttpUtil.class);
	 /**
     * 获取post请求的body
     * @param request
     * @param encode
     * @return
     */
    public static String getBodyFromPostReq(HttpServletRequest request, String encode) {

        StringBuffer bodySB = new StringBuffer();
        String line = null;
        try {
            request.setCharacterEncoding(encode);
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                bodySB.append(line);
        } catch (Exception e) {
            log.error("[HttpUtils->getBodyFromPostReq] 获取post body错误", e);
        }

        return bodySB.toString();
    }
   
}
