package com.lanxi.couponcode.spi.abstractentity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.lanxi.couponcode.spi.defaultInterfaces.CommonDefaultMethodOfEntity;

import java.io.Serializable;

/**
 * 抽象操作记录
 *
 * @author yangyuanjian
 */
public abstract class AbstractOperateRecord extends Model<AbstractOperateRecord> implements Serializable, CommonDefaultMethodOfEntity {

}
