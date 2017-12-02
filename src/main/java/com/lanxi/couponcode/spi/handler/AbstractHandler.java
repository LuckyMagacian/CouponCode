package com.lanxi.couponcode.spi.handler;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yangyuanjian on 2017/10/27.
 */
@Deprecated
public abstract class AbstractHandler implements Handler {
    /**
     * 销毁标记-当该标记为true时,代表接下里可能要销毁该实例,拒绝引用
     */
    private boolean destroyState = false;
    /**
     * 调用计数-该实例中的方法调用时应当+1,完成时-1,当销毁标记为true,调用计数为0时,注销实例
     */
    private volatile AtomicInteger callCount = new AtomicInteger(0);

}
