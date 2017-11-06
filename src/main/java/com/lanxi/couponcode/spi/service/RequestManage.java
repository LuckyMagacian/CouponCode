package com.lanxi.couponcode.spi.service;

import java.util.Collection;
import java.util.Optional;

import com.lanxi.couponcode.spi.abstractentity.AbstractRequest;

public interface RequestManage {
	Optional<AbstractRequest<?>> addRequest(AbstractRequest<?> request);
	Optional<AbstractRequest<?>> modifyRequest(AbstractRequest<?> request);
	Collection<AbstractRequest<?>> queryRequests(Object... args);
	Optional<AbstractRequest<?>> queryRequest(String requestId);
}
