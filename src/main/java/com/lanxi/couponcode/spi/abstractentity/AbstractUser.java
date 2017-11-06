package com.lanxi.couponcode.spi.abstractentity;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.lanxi.couponcode.spi.defaultInterfaces.CommonDefaultMethodOfEntity;
/**
 * 抽象用户
 * @author yangyuanjian
 *
 */
@Deprecated
public abstract class AbstractUser extends Model<AbstractUser> implements Serializable,CommonDefaultMethodOfEntity {
	
}
