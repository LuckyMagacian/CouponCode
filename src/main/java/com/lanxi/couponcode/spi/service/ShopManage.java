package com.lanxi.couponcode.spi.service;

import java.util.Collection;
import java.util.Optional;

import com.lanxi.couponcode.spi.abstractentity.AbstractRequest;
import com.lanxi.couponcode.spi.abstractentity.AbstractShop;

public interface ShopManage {
	Optional<AbstractShop> addShop(AbstractShop shop);
	Optional<AbstractShop> addShop(AbstractRequest<AbstractShop> req);
	Optional<AbstractShop> modifyShop(AbstractShop shop);
	Optional<AbstractShop> modifyShop(AbstractRequest<AbstractShop> req);
	Collection<AbstractShop> queryShops(Object... args);
	Optional<AbstractShop> queryShop(String shopId);
}
