package com.lanxi.couponcode.spi.abstractentity;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.lanxi.couponcode.spi.defaultInterfaces.CommonDefaultMethodOfEntity;
/**
 * 抽象商品类
 * @author yangyuanjian
 *
 */
public abstract class AbstractCommodity extends Model<AbstractCommodity> implements Serializable,CommonDefaultMethodOfEntity {
}
