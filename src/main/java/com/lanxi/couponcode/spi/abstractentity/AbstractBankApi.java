package com.lanxi.couponcode.spi.abstractentity;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.lanxi.couponcode.spi.defaultInterfaces.CommonDefaultMethodOfEntity;

/**
 * 抽象银行api类
 *
 * @author yangyuanjian
 */
public abstract class AbstractBankApi extends Model<AbstractBankApi> implements Serializable, CommonDefaultMethodOfEntity {

}
