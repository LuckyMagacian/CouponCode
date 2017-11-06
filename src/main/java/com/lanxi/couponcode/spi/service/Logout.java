package com.lanxi.couponcode.spi.service;

import java.util.Optional;

import com.lanxi.couponcode.spi.abstractentity.AbstractUser;
@FunctionalInterface
public interface Logout {
	//TODO 返回用户实体类型
	Optional<? extends AbstractUser> logout(String phone);
}
