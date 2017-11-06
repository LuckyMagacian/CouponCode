package com.lanxi.couponcode.spi.abstractentity;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.lanxi.couponcode.spi.defaultInterfaces.CommonDefaultMethodOfEntity;
/**
 * 抽象门店
 * @author yangyuanjian
 *
 */
public abstract class  AbstractShop extends Model<AbstractShop> implements Serializable,CommonDefaultMethodOfEntity {

}
