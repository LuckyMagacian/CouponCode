package com.lanxi.couponcode.spi.abstractentity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.lanxi.couponcode.spi.defaultInterfaces.CommonDefaultMethodOfEntity;

import java.io.Serializable;

/**
 * 抽象订单
 * @author yangyuanjian
 *
 */
public abstract class AbstractOrder extends Model<AbstractOrder> implements Serializable, CommonDefaultMethodOfEntity {

}
