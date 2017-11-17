package com.lanxi.couponcode.spi.service;

import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.annotations.HiddenArg;
import com.lanxi.couponcode.spi.consts.annotations.RealReturnType;
import com.lanxi.couponcode.spi.consts.enums.AccountStatus;
import com.lanxi.couponcode.spi.consts.enums.AccountType;

/**
 * Created by yangyuanjian on 2017/11/9.
 */
public interface AccountService {
    /**
     * 添加账户
     * @param type
     * @param name
     * @param phone
     * @param merchantName
     * @param merchantId
     * @param operaterId
     * @return
     */
    RetMessage<Boolean> addAccount(AccountType type,
                          String name,
                          String phone,
                          String merchantName,
                          @HiddenArg Long merchantId,
                          @HiddenArg Long operaterId);

    RetMessage<Boolean> merchantAddAccount(AccountType type,
                       String name,
                       String phone,
                       String shopName,
                       @HiddenArg Long shopId,
                       @HiddenArg Long operaterId);

    RetMessage<Boolean> freezeAccount(Long accountId,
                          @HiddenArg Long operaterId);

    RetMessage<Boolean> unfreezeAccount(Long accountId,
                            @HiddenArg Long operaterId);

    RetMessage<Boolean> delAccount(Long accountId,
                       @HiddenArg Long operaterId);

    @RealReturnType("List<Account>")
    RetMessage<String> queryAccounts(String phone,
                         String merchantName,
                         AccountType type,
                         AccountStatus status,
                         @HiddenArg Integer pageNum,
                         @HiddenArg Integer pageSize,
                         @HiddenArg Long operaterId);


    @RealReturnType("List<Account>")
    RetMessage<String> merchantQueryAccounts(String phone,
                         String shopName,
                         AccountType type,
                         AccountStatus status,
                         @HiddenArg Integer pageNum,
                         @HiddenArg Integer pageSize,
                         @HiddenArg Long operaterId);


    RetMessage<String> queryAccountInfo(Long accountId,
                            @HiddenArg Long operaterId);

    @RealReturnType("List<Account>")
    RetMessage<String> queryShopAccounts(@HiddenArg Long shopId,
                             @HiddenArg Long operaterId);

}
