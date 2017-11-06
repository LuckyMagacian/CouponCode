package com.lanxi.couponcode.spi.service;

import java.util.Collection;
import java.util.Optional;

import com.lanxi.couponcode.spi.abstractentity.AbstractCommodity;
import com.lanxi.couponcode.spi.abstractentity.AbstractRequest;

public interface CommodityManage {
	Optional<AbstractCommodity> addCommodity(AbstractCommodity commodity);
	Optional<AbstractCommodity> addCommodity(AbstractRequest<AbstractCommodity> req);
	Optional<AbstractCommodity> modifyCommodity(AbstractCommodity commodity);
	Optional<AbstractCommodity> modifyCommodity(AbstractRequest<AbstractCommodity> req);
	Collection<AbstractCommodity> queryCommodities(Object... args);
	Optional<AbstractCommodity> queryCommodity(String commodityId);
	Optional<AbstractCommodity> deleteCommodity(AbstractCommodity commodity);
	Optional<AbstractCommodity> deleteCommodity(AbstractRequest<AbstractCommodity> req);
}
