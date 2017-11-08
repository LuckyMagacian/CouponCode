package com.lanxi.couponcode.spi.abstractentity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.lanxi.couponcode.spi.defaultInterfaces.CommonDefaultMethodOfEntity;

import java.io.Serializable;

/**
 * 抽象子订单
 * @author yangyuanjian
 *
 */
@Deprecated
public abstract class AbstractSubOrder extends Model<AbstractSubOrder> implements Serializable,CommonDefaultMethodOfEntity {

}
