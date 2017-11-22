package com.lanxi.couponcode.impl.entity;

/**
 * Created by yangyuanjian on 2017/11/22.
 */
public class Order {
    /**
     * 1 设计报文
     *  1.1 报文消息头
     *  1.2 报文消息体
     *
     * 2 设计订单类
     *  2.1 来源{发起方,绑定手机号码,交易序号,请求类型}[报文序号,测试标记]
     *  2.2 商品相关的信息{merchantId,commodityId,商品数量}
     *  2.3 返回给来源的信息{数量,单价,串码,过期时间,返回交易总额}
     *  2.4 订单发生时的商品信息
     *
     * 3 查询订单
     *  3.1 查询详情
     *  3.2 批量查询
     */
}
