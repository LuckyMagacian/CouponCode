package com.lanxi.couponcode.spi.abstractentity;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.lanxi.couponcode.spi.defaultInterfaces.CommonDefaultMethodOfEntity;
/**
 * 抽象商户
 * @author yangyuanjian
 *
 */
public abstract class AbstractMerchant extends Model<AbstractMerchant> implements Serializable ,CommonDefaultMethodOfEntity{

}
