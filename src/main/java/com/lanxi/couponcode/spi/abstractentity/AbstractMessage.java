package com.lanxi.couponcode.spi.abstractentity;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.lanxi.couponcode.spi.defaultInterfaces.CommonDefaultMethodOfEntity;

/**
 * 抽象消息
 *
 * @author yangyuanjian
 */
public abstract class AbstractMessage extends Model<AbstractMessage> implements Serializable, CommonDefaultMethodOfEntity {

}
