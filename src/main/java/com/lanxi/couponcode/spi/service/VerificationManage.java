package com.lanxi.couponcode.spi.service;

import java.util.Collection;
import java.util.Optional;

import com.lanxi.couponcode.spi.abstractentity.AbstractRequest;
import com.lanxi.couponcode.spi.abstractentity.AbstractVerificationRecord;

public interface VerificationManage {
	Optional<AbstractVerificationRecord> modifyMerchant(AbstractVerificationRecord verificationRecord);
	Optional<AbstractVerificationRecord> modifyMerchant(AbstractRequest<AbstractVerificationRecord> req);
	Collection<AbstractVerificationRecord> queryMerchants(Object... args);
	Optional<AbstractVerificationRecord> queryMerchant(String recordId);
}
