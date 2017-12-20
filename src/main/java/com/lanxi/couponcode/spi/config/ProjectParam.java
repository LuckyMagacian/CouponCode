package com.lanxi.couponcode.spi.config;

/**
 * @author wuxiaobo
 */
public class ProjectParam {
    /**
     * 需要我们发送短信
     */
    public final static String NEED_SEND_Y = "0";
    /**
     * 不需要我们发送短信
     */
    public final static String NEED_SEND_N = "1";

    /**
     * 交易发送状态  1 ：已发送
     */
    public final static String SEND_STATE_SEND = "1";
    /**
     * 交易发送状态  7 ：成功
     */
    public final static String SEND_STATE_SUCC = "7";
    /**
     * 交易发送状态  16 ：合作机构超时
     */
    public final static String SEND_STATE_TIME_OUT = "16";
    /**
     * 交易发送状态  17：合作机构拒绝
     */
    public final static String SEND_STATE_REFUSE = "17";
    /**
     * 交易发送状态  18：部分成功
     */
    public final static String SEND_STATE_PART_SUCC = "18";

    /**
     * 响应信息码(返给机构)：成功
     */
    public final static String RESCODE_SUCC = "0000";
    /**
     * 响应信息码(返给机构)：失败
     */
    public final static String RESCODE_FAIL = "9001";
    /**
     * 响应信息码(返给机构)：处理错误
     */
    public final static String RESCODE_ERR = "9999";
    /**
     * 响应信息码(返给机构)：验签错误
     */
    public final static String RESCODE_SIGN_ERR = "5001";
}
