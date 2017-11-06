package com.lanxi.couponcode.spi.service;

import java.util.Collection;
import java.util.Optional;

import com.lanxi.couponcode.spi.abstractentity.AbstractCouponCode;

public interface CouponManage {
	Collection<AbstractCouponCode> queryMerchants(Object... args);
	Optional<AbstractCouponCode> queryMerchant(String couponId);
}
