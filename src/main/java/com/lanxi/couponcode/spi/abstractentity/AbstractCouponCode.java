package com.lanxi.couponcode.spi.abstractentity;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.lanxi.couponcode.spi.defaultInterfaces.CommonDefaultMethodOfEntity;
/**
 * 抽象串码类
 * @author yangyuanjian
 *
 */
public abstract class AbstractCouponCode extends Model<AbstractCouponCode> implements Serializable,CommonDefaultMethodOfEntity {

}
