package com.lanxi.couponcode.spi.service;

import java.util.Optional;

import com.lanxi.couponcode.spi.abstractentity.AbstractAccount;
import com.lanxi.couponcode.spi.abstractentity.AbstractUser;

@FunctionalInterface
public interface Login {
//	boolean login(String name,String password);
	//TODO 返回用户实体或null
	Optional<? extends AbstractAccount> login(String phone, String password, String code);
}
