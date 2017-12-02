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
     *
     * admin添加账户
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
                                   @HiddenArg Long shopId,
                                   String shopName,
                                   @HiddenArg Long operaterId);

    RetMessage<Boolean> adminAddAccount(AccountType type,
    		String merchantName,
    		String name,
    		String phone,
    		@HiddenArg Long operaterId,
    		@HiddenArg Long merchantId);
    /**
     * merchant添加账户
     * @param type
     * @param name
     * @param phone
     * @param shopName
     * @param shopId
     * @param operaterId
     * @return
     */
    RetMessage<Boolean> merchantAddAccount(AccountType type,
                                           String name,
                                           String phone,
                                           String shopName,
                                           @HiddenArg Long shopId,
                                           @HiddenArg Long operaterId);

    @RealReturnType ("List<Account>")
    RetMessage<String> queryAccounts(Long merchantId, Long shopId,
                                     String shopName,
                                     String phone,
                                     String merchantName,
                                     AccountType type,
                                     AccountStatus status,
                                     @HiddenArg Integer pageNum,
                                     @HiddenArg Integer pageSize,
                                     @HiddenArg Long operaterId);


    /**
     * shop添加账户
     * @param type
     * @param name
     * @param phone
     * @param operaterId
     * @return
     */
    RetMessage<Boolean> shopAddAccount(AccountType type,
    		String name,
    		String phone,
    		@HiddenArg Long operaterId);
    /**
     * 冻结账户
     * @param accountId
     * @param operaterId
     * @return
     */
    RetMessage<Boolean> freezeAccount(Long accountId,
                          @HiddenArg Long operaterId);
    /**
     * 开启账户
     * @param accountId
     * @param operaterId
     * @return
     */
    RetMessage<Boolean> unfreezeAccount(Long accountId,
                            @HiddenArg Long operaterId);
    /**
     * 删除账户
     * @param accountId
     * @param operaterId
     * @return
     */
    RetMessage<Boolean> delAccount(Long accountId,
                       @HiddenArg Long operaterId);
    /**
     * admin查询账户
     * @param shopName
     * @param phone
     * @param merchantName
     * @param type
     * @param status
     * @param pageNum
     * @param pageSize
     * @param operaterId
     * @return
     */
    @RealReturnType("List<Account>")
    RetMessage<String> queryAccounts(
    		String shopName,
    		String phone, 
    		String merchantName,
    		AccountType type, 
    		AccountStatus status,
    		@HiddenArg Integer pageNum,
    		@HiddenArg Integer pageSize,
    		@HiddenArg Long operaterId);
    
    /**
     * merchant查询账户
     * @param phone
     * @param shopName
     * @param type
     * @param status
     * @param pageNum
     * @param pageSize
     * @param operaterId
     * @return
     */
    @RealReturnType("List<Account>")
    RetMessage<String> merchantQueryAccounts(String phone,
                                             String shopName,
                                             AccountType type,
                                             AccountStatus status,
                                             @HiddenArg Integer pageNum,
                                             @HiddenArg Integer pageSize,
                                             @HiddenArg Long operaterId);

    /**
     * 查询账户详情
     * @param accountId
     * @param operaterId
     * @return
     */
    RetMessage<String> queryAccountInfo(Long accountId,
                            @HiddenArg Long operaterId);
    /**
     * 门店查询账户
     * @param shopId
     * @param operaterId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RealReturnType("List<Account>")
    RetMessage<String> queryShopAccounts(@HiddenArg Long shopId,
                                         @HiddenArg Long operaterId,
                                         @HiddenArg Integer pageNum,
                                         @HiddenArg Integer pageSize);

}
