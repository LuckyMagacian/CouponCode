package com.lanxi.couponcode.spi.abstractentity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.lanxi.couponcode.spi.defaultInterfaces.CommonDefaultMethodOfEntity;

import java.io.Serializable;

/**
 * Created by yangyuanjian on 2017/11/1.
 */
public abstract class AbstractCodeAlgorithm extends Model<AbstractCodeAlgorithm> implements Serializable, CommonDefaultMethodOfEntity {
}
