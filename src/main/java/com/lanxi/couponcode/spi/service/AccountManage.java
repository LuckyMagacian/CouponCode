package com.lanxi.couponcode.spi.service;

import java.util.Collection;
import java.util.Optional;

import com.lanxi.couponcode.spi.abstractentity.AbstractAccount;
import com.lanxi.couponcode.spi.abstractentity.AbstractRequest;

public interface AccountManage {
	Optional<AbstractAccount> addAccount(AbstractAccount account);
	Optional<AbstractAccount> addAccount(AbstractRequest<AbstractAccount> req);
	Optional<AbstractAccount> modifyAccount(AbstractAccount account);
	Optional<AbstractAccount> modifyAccount(AbstractRequest<AbstractAccount> req);
	Collection<AbstractAccount> queryAccounts(Object... args);
	Optional<AbstractAccount> queryAccount(String accountId);
	Optional<AbstractAccount> deleteAccount(AbstractAccount account);
	Optional<AbstractAccount> deleteAccount(AbstractRequest<AbstractAccount> req);
}
