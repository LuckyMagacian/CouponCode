package com.lanxi.couponcode.impl.entity;
/**
 * 
 * @author wuxiaobo
 *
 */
public class ProjectParam {
	 /**
     * 需要我们发送短信
     */
    public static String NEED_SEND_Y="0";
    /**
     * 不需要我们发送短信
     */
    public static String NEED_SEND_N="1";

    /** 交易发送状态  1 ：已发送 */
    public static String SEND_STATE_SEND = "1";
    /** 交易发送状态  7 ：成功 */
    public static String SEND_STATE_SUCC = "7";
    /** 交易发送状态  16 ：合作机构超时 */
    public static String SEND_STATE_TIME_OUT="16";
    /** 交易发送状态  17：合作机构拒绝 */
    public static String SEND_STATE_REFUSE="17";
    /** 交易发送状态  18：部分成功 */
    public static String SEND_STATE_PART_SUCC="18";

    /** 响应信息码(返给机构)：成功 */
    public static String RESCODE_SUCC = "0000";
    /** 响应信息码(返给机构)：失败 */
    public static String RESCODE_FAIL = "9001";
    /** 响应信息码(返给机构)：处理错误 */
    public static String RESCODE_ERR = "9999";
}
