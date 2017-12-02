package com.lanxi.couponcode.spi.abstractentity;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.lanxi.couponcode.spi.defaultInterfaces.CommonDefaultMethodOfEntity;

/**
 * 抽象统计数据
 *
 * @author yangyuanjian
 */
public abstract class AbstractStatistic extends Model<AbstractStatistic> implements Serializable, CommonDefaultMethodOfEntity {

}
