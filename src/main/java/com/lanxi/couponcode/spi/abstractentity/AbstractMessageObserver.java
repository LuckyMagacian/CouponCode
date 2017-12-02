package com.lanxi.couponcode.spi.abstractentity;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.lanxi.couponcode.spi.defaultInterfaces.CommonDefaultMethodOfEntity;

/**
 * 抽象消息观察者
 *
 * @author yangyuanjian
 */
public abstract class AbstractMessageObserver extends Model<AbstractMessageObserver> implements Serializable, CommonDefaultMethodOfEntity {

}
