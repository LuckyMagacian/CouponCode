package com.lanxi.couponcode.spi.service;

import java.util.Collection;
import java.util.Optional;

import com.lanxi.couponcode.spi.abstractentity.AbstractMerchant;
import com.lanxi.couponcode.spi.abstractentity.AbstractRequest;

public interface MerchantManage {
	Optional<AbstractMerchant> addMerchant(AbstractMerchant merchant);
	Optional<AbstractMerchant> addMerchant(AbstractRequest<AbstractMerchant> req);
	Optional<AbstractMerchant> modifyMerchant(AbstractMerchant merchant);
	Optional<AbstractMerchant> modifyMerchant(AbstractRequest<AbstractMerchant> req);
	Collection<AbstractMerchant> queryMerchants(Object... args);
	Optional<AbstractMerchant> queryMerchant(String merchantId);
}
