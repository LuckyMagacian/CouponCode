package com.lanxi.couponcode.impl.newservice;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.Account;
import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.couponcode.spi.consts.enums.AccountStatus;
import com.lanxi.util.entity.LogFactory;
import com.lanxi.util.utils.LoggerUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service ("accountService")
@EasyLog (LoggerUtil.LogLevel.INFO)
public class AccountServiceImpl implements AccountService {
    @Resource
    private DaoService dao;

    @Override
    public Boolean addAccount(Account account) {

        try {
            LogFactory.info(this, "尝试添加商户账户,\n");
            return account.insert();
        } catch (Exception e) {
            LogFactory.error(this, "添加商户账户时发生异常\n", e);
            return false;
        }

    }

    @Override
    public Boolean freezeAccount(Account account) {
        synchronized (account) {
            try {
                LogFactory.info(this, "尝试冻结账号\n");
                account.setStatus(AccountStatus.freeze);
                return account.updateById();
            } catch (Exception e) {
                LogFactory.error(this, "冻结账户时发生异常\n", e);
                return false;
            }

        }
    }

    @Override
    public Boolean unfreezeAccount(Account account) {
        synchronized (account) {
            try {
                LogFactory.info(this, "尝试开启账户\n");
                account.setStatus(AccountStatus.normal);
                return account.updateById();
            } catch (Exception e) {
                LogFactory.error(this, "开启账户时发生异常\n", e);
                return false;
            }
        }
    }

    @Override
    public Boolean delAccount(Account account) {
        synchronized (account) {
            try {
                LogFactory.info(this, "尝试删除账号\n");
                account.setStatus(AccountStatus.deleted);
                return account.updateById();
            } catch (Exception e) {
                LogFactory.error(this, "删除账户时发生异常\n", e);
                return false;
            }
        }
    }

    @Override
    public List<Account> queryAccounts(EntityWrapper<Account> wrapper, Page<Account> pageObj) {
        LogFactory.info(this, "尝试获取账户信息/n");
        List<Account> list = null;
        try {
            if (pageObj != null) {
                wrapper.orderBy("add_time",false);
                list = dao.getAccountDao().selectPage(pageObj, wrapper);
                LogFactory.debug(this, "查询到的结果[" + list + "]\n");
                LogFactory.info(this, "查询到的总记录数[" + list.size() + "]\n");
            } else {
                list = dao.getAccountDao().selectList(wrapper);
            }
            return list;
        } catch (Exception e) {
            LogFactory.error(this, "获取账户信息时发生异常\n", e);
            return list;
        }
    }

    @Override
    public Account queryAccountInfo(Account account) {
        LogFactory.info(this, "尝试根据账户id获取账户信息\n");
        try {
            if (account != null) {
                EntityWrapper<Account> wrapper = new EntityWrapper<Account>();
                if (account.getAccountId() != null) {
                    wrapper.eq("account_id", account.getAccountId());
                }
                if (account.getPhone() != null && !account.getPhone().isEmpty()) {
                    wrapper.eq("phone", account.getPhone());
                }
                wrapper.in("status", AccountStatus.normal.getValue() + "," + AccountStatus.freeze.getValue());
                account = dao.getAccountDao().selectList(wrapper).get(0);
                LogFactory.info(this, "查询结果[" + account + "]\n");
                return account;
            } else
                return null;
        } catch (Exception e) {
            LogFactory.error(this, "根据账户id获取账户信息时发生异常\n");
            return null;
        }
    }

    @Override
    public Boolean phoneVerify(String phone) {
        LogFactory.info(this, "查询手机号码是否已注册\n");
        Boolean result = false;
        List<Account> list = null;
        try {
            EntityWrapper<Account> wrapper = new EntityWrapper<Account>();
            if (phone != null && !phone.isEmpty()) {
                wrapper.eq("phone", phone);
            }
            list = dao.getAccountDao().selectList(wrapper);
            if (list == null || list.size() == 0) {
                LogFactory.debug(this, "此手机号码可用\n");
                return true;
            } else {
                for (Account account : list) {
                    if (account.getStatus() == null)
                        return true;
                    else {
                        if (AccountStatus.normal.equals(account.getStatus())
                                || AccountStatus.freeze.equals(account.getStatus())) {
                            LogFactory.debug(this, "此手机号码已经被注册过\n");
                            return false;
                        }
                    }

                }
                return true;
            }
        } catch (Exception e) {
            LogFactory.error(this, "查询手机号码是否已注册时发生异常\n", e);

        }

        return result;
    }

    @Override
    public Boolean modifyAccount(Account account) {
        synchronized (account) {
            return account.updateById();
        }
    }

    @Override
    public Account login(Account account, String validateCode) {
        LogFactory.info(this, "尝试登录账户\n");
        try {
            EntityWrapper<Account> wrapper = new EntityWrapper<Account>();
            wrapper.eq("phone", account.getPhone());
            wrapper.in("status", AccountStatus.normal.getValue() + "," + AccountStatus.freeze.getValue());
            List<Account> list = dao.getAccountDao().selectList(wrapper);
            if (list == null || list.size() == 0)
                return null;
            else {
                for (Account account3 : list) {
                    if (account3.getStatus() != null) {
                        if (AccountStatus.normal.equals(account3.getStatus())
                                || AccountStatus.freeze.equals(account3.getStatus())) {
                            return account3;
                        }
                    } else
                        return null;
                }
                LogFactory.info(this, "此账户不存在");
                return null;
            }
        } catch (Exception e) {
            LogFactory.error(this, "登录账户时发生异常\n", e);
            return null;
        }

    }

    @Override
    public Boolean logout(Long accountId) {

        return null;
    }

    @Override
    public Boolean forgetPassword(String validateCode, Account account, Long accountId) {
        synchronized (account) {
            LogFactory.info(this, "尝试重置账户密码\n");
            Boolean result = false;
            try {
                Integer var = dao.getAccountDao().updateById(account);
                if (var < 0) {
                    result = false;
                    LogFactory.debug(this, "重置密码失败\n");
                } else if (var > 0) {
                    result = true;
                    LogFactory.debug(this, "重置密码成功\n");
                }

            } catch (Exception e) {
                LogFactory.error(this, "重置密码时发生异常\n");
            }
            return result;
        }
    }

    @Override
    public Boolean changePassword(Account account) {

        synchronized (account) {
            LogFactory.info(this, "尝试修改账户密码\n");
            Boolean result = false;
            try {
                Integer var = dao.getAccountDao().updateById(account);
                if (var < 0) {
                    result = false;
                    LogFactory.debug(this, "修改密码失败\n");

                } else if (var > 0) {
                    result = true;
                    LogFactory.debug(this, "修改密码成功\n");

                }

            } catch (Exception e) {
                LogFactory.error(this, "重置密码时发生异常\n", e);

                throw new RuntimeException("重置密码时发生异常", e);

            }
            return result;
        }
    }

    // 判断给定时间跟当前时间的时间差是否大于15分钟
    public Boolean localdateLtDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Date date1 = sdf.parse(date);
            Date now = sdf.parse(sdf.format(new Date()));
            if (now.getTime() - date1.getTime() > 900000) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            LogFactory.info(this, "判断时间差时发生异常");
            return false;
        }

    }

    @Override
    public String queryAccountStatusById(Long accountId) {
        LogFactory.info(this, "尝试通过账户id获取账户状态");
        String accountStatus = null;
        try {
            Account account = dao.getAccountDao().selectById(accountId);
            if (account != null) {
                accountStatus = account.getStatus();
            } else {
                LogFactory.info(this, "此账户不存在");
            }
        } catch (Exception e) {

            LogFactory.error(this, "通过账户id获取账户状态时发生异常", e);
        }
        return accountStatus;
    }

    @Override
    public Boolean modifyAccountMerchantName(Long merchantId, String merchantName) {
        LogFactory.info(this, "开始尝试修改账户所属商户名称");
        Boolean result = false;
        try {
            Account account = new Account();
            account.setMerchantName(merchantName);
            EntityWrapper<Account> wrapper = new EntityWrapper<Account>();
            if (merchantName != null) {
                wrapper.eq("merchant_id", merchantId);
                Integer var = dao.getAccountDao().update(account, wrapper);
                if (var > 0) {
                    result = true;
                    LogFactory.info(this, "修改账户所属商户名称成功");
                } else {
                    result = false;
                    LogFactory.info(this, "修改账户所属商户名称失败");
                }
            }

        } catch (Exception e) {
            LogFactory.info(this, "修改账户所属商户名称时发生异常", e);
        }
        return result;
    }

    @Override
    public Boolean modifyAccountShopName(Long shopId, String shopName) {
        LogFactory.info(this, "开始尝试修改账户所属门店名称");
        Boolean result = false;
        try {
            Account account = new Account();
            account.setShopName(shopName);
            EntityWrapper<Account> wrapper = new EntityWrapper<Account>();
            if (shopName != null) {
                wrapper.eq("shop_id", shopId);
                Integer var = dao.getAccountDao().update(account, wrapper);
                if (var > 0) {
                    result = true;
                    LogFactory.info(this, "修改账户所属门店名称成功");
                } else {
                    result = false;
                    LogFactory.info(this, "修改账户所属门店名称失败");
                }
            }

        } catch (Exception e) {
            LogFactory.info(this, "修改账户所属门店名称时发生异常", e);
        }
        return result;
    }

    @Override
    public Account queryAccountById(Long accountId) {
        return dao.getAccountDao().selectById(accountId);
    }

}
