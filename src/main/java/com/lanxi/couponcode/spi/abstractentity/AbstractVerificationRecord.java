package com.lanxi.couponcode.spi.abstractentity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.lanxi.couponcode.spi.defaultInterfaces.CommonDefaultMethodOfEntity;

import java.io.Serializable;

/**
 * 抽象核销记录
 *
 * @author yangyuanjian
 */
public abstract class AbstractVerificationRecord extends Model<AbstractVerificationRecord> implements Serializable,
        CommonDefaultMethodOfEntity {

}
