package com.lanxi.couponcode.spi.abstractentity;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.lanxi.couponcode.spi.defaultInterfaces.CommonDefaultMethodOfEntity;
/**
 * 抽象请求
 * @author yangyuanjian
 *
 * @param <T>操作的目标类型
 */
public abstract class AbstractRequest<T> extends Model<AbstractRequest> implements Serializable,CommonDefaultMethodOfEntity {
	
}
