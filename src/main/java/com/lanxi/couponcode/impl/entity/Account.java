package com.lanxi.couponcode.impl.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.lanxi.couponcode.spi.abstractentity.AbstractAccount;

public class Account extends AbstractAccount{

	private static final long serialVersionUID = 3512133793272036104L;

	/**该方法用于指定主键*/
	@Override
	protected Serializable pkVal() {
		return null;
	}

}
