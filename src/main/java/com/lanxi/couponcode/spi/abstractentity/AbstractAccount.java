package com.lanxi.couponcode.spi.abstractentity;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.lanxi.couponcode.spi.defaultInterfaces.CommonDefaultMethodOfEntity;

/**
 * 抽象账户类
 *
 * @author yangyuanjian
 */
public abstract class AbstractAccount extends Model<AbstractAccount> implements Serializable, CommonDefaultMethodOfEntity {

}
