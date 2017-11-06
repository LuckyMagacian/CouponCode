package com.lanxi.couponcode.spi.service;

import java.util.Optional;

import com.lanxi.couponcode.spi.abstractentity.AbstractAccount;
import com.lanxi.couponcode.spi.abstractentity.AbstractUser;

public interface ForgetPwd {
	Optional<? extends AbstractAccount> resetPwd(String phone, String pwd, String pwdRepeat, String smsCode);
}
