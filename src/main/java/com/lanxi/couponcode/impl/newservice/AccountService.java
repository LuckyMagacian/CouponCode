package com.lanxi.couponcode.impl.newservice;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.Account;

import java.util.List;

/**
 * 账户操作接口
 *
 * @author wuxiaobo
 */
public interface AccountService {
    /*添加账户*/
    Boolean addAccount(Account account);

    /*商户添加账户*/
    Boolean merchantAccount(Account account);

    /*冻结账户*/
    Boolean freezeAccount(Account account);

    /*开启账户*/
    Boolean unfreezeAccount(Account account);

    /*删除账户*/
    Boolean delAccount(Account account);

    /*账户查询*/
    List<Account> queryAccounts(EntityWrapper<Account> wrapper,
                                Page<Account> pageObj);

    List<Account> merchantQueryAccounts(EntityWrapper<Account> wrapper,
                                        Page<Account> pageObj);

    List<Account> queryShopAccounts(EntityWrapper<Account> wrapper,
                                    Page<Account> pageObj);

    /*账户详情查询*/
    Account queryAccountInfo(Account account);

    /*手机号码验证*/
    Boolean phoneVerify(String phone);

    /*修改账户仅在用户登录时用来修改登录失败次数和登录失败时间*/
    Boolean modifyAccount(Account account);

    /*修改账户的商户名称*/
    Boolean modifyAccountMerchantName(Long merchantId, String merchantName);

    /*修改账户的门店名称*/
    Boolean modifyAccountShopName(Long shopId, String shopName);

    /*登录*/
    Account login(Account account,
                  String validateCode);

    /*登出*/
    Boolean logout(Long accountId);

    /*忘记密码*/
    Boolean forgetPassword(String validateCode,
                           Account account,
                           Long accountId);

    /*修改密码*/
    Boolean changePassword(Account account);

    /*判断时间两个时间差值是否大于15分钟*/
    Boolean localdateLtDate(String date);

    /*根据账户id查询账户状态*/
    String queryAccountStatusById(Long accountId);

    /*根据账户id获取账户详情*/
    Account queryAccountById(Long accountId);
}
