package com.lanxi.couponcode.impl.newcontroller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.impl.assist.TimeAssist;
import com.lanxi.couponcode.impl.config.ConstConfig;
import com.lanxi.couponcode.impl.entity.Account;
import com.lanxi.couponcode.impl.entity.Merchant;
import com.lanxi.couponcode.impl.entity.OperateRecord;
import com.lanxi.couponcode.impl.entity.Shop;
import com.lanxi.couponcode.impl.newservice.AccountService;
import com.lanxi.couponcode.impl.newservice.MerchantService;
import com.lanxi.couponcode.impl.newservice.OperateRecordService;
import com.lanxi.couponcode.impl.newservice.RedisEnhancedService;
import com.lanxi.couponcode.impl.newservice.RedisService;
import com.lanxi.couponcode.impl.newservice.ShopService;
import com.lanxi.couponcode.spi.consts.enums.AccountStatus;
import com.lanxi.couponcode.spi.consts.enums.AccountType;
import com.lanxi.couponcode.spi.consts.enums.OperateTargetType;
import com.lanxi.couponcode.spi.consts.enums.OperateType;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.util.entity.LogFactory;
import com.lanxi.util.utils.SignUtil;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import static com.lanxi.couponcode.impl.assist.PredicateAssist.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author wuxiaobo
 */
@Controller
public class AccountController implements com.lanxi.couponcode.spi.service.AccountService {
    @Resource
    private MerchantService merchantService;
    @Resource
    private ShopService shopService;
    @Resource
    private AccountService accountService;
    @Resource
    private OperateRecordService operateRecordService;
    @Resource
    private RedisService redisService;
    @Resource
    private RedisEnhancedService redisEnhancedService;

    @Override
    public RetMessage<Boolean> addAccount(AccountType type, String name, String phone, String merchantName,
                                          Long merchantId, Long shopId, String shopName, Long operaterId) {
        RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
        Boolean result = false;
        // TODO 校验
        try {
            RetMessage message = null;
            Account a = accountService.queryAccountById(operaterId);
            Merchant m = null;
            Shop s = null;
            OperateType ot = null;
            // ------------------------------------添加商户管理员权限验证-------------------------------------------------------------
            if (AccountType.merchantManager.equals(type.getValue())) {
                message = checkAccount.apply(a, OperateType.createMerchantManager);
                if (notNull.test(message))
                    return message;
                m = merchantService.queryMerchantParticularsById(merchantId);
                message = checkMerchant.apply(m, OperateType.createMerchantManager);
                if (notNull.test(message))
                    return message;
                ot = OperateType.createMerchantManager;
            } else {

                // -------------------------------------添加门店管理员权限验证-------------------------------------------------------------
                m = merchantService.queryMerchantParticularsById(a.getMerchantId());
                s = shopService.queryShopInfo(shopId);
                if (AccountType.shopManager.equals(type.getValue())) {
                    message = checkAccount.apply(a, OperateType.createShopManager);
                    if (notNull.test(message))
                        return message;
                    message = checkMerchant.apply(m, OperateType.createShopManager);
                    if (notNull.test(message))
                        return message;
                    message = checkShop.apply(s, OperateType.createShopManager);
                    if (notNull.test(message))
                        return message;
                    ot = OperateType.createShopManager;
                }
                // --------------------------------------添加核销员权限验证----------------------------------------------------------------
                if (AccountType.shopEmployee.equals(type.getValue())) {
                    message = checkAccount.apply(a, OperateType.createEmployee);
                    if (notNull.test(message))
                        return message;
                    message = checkMerchant.apply(m, OperateType.createEmployee);
                    if (notNull.test(message))
                        return message;
                    message = checkShop.apply(s, OperateType.createEmployee);
                    if (notNull.test(message))
                        return message;
                    ot = OperateType.createEmployee;
                }
            }
            Account account = new Account();
            account.setAccountId(IdWorker.getId());
            account.setAccountType(type);
            account.setLoginFailureNum(0);
            account.setLoginFailureTime("20171114033028");
            account.setUserName(name);
            account.setPhone(phone);
            account.setStatus(AccountStatus.normal);
            account.setMerchantName(merchantName);
            if (s != null) {
                account.setMerchantId(a.getMerchantId());
                account.setShopId(shopId);
            } else {
                account.setMerchantId(merchantId);
            }
            account.setAddTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            account.setAddById(operaterId);
            account.setAddByName(a.getUserName());
            result = accountService.addAccount(account);
            if (result) {
                retMessage.setRetCode(RetCodeEnum.success.getValue());
                retMessage.setRetMessage("添加账户成功");
                OperateRecord record = new OperateRecord();
                record.setRecordId(IdWorker.getId());
                record.setOperaterId(operaterId);
                record.setAccountType(a.getAccountType());
                record.setPhone(a.getPhone());
                record.setName(a.getUserName());
                record.setTargetType(OperateTargetType.account);
                record.setType(ot);
                record.setOperateTime(TimeAssist.getNow());
                record.setOperateResult("success");
                record.setDescription("添加账户[" + account.getAccountId() + "]");
                operateRecordService.addRecord(record);
            }
            if (!result) {
                retMessage.setRetCode(RetCodeEnum.exception.getValue());
                retMessage.setRetMessage("添加账户失败");
            }
            retMessage.setDetail(result);
        } catch (Exception e) {
            LogFactory.error(this, "添加账户时发生异常", e);
            retMessage.setAll(RetCodeEnum.error, "添加账户时发生异常", result);
        }
        return retMessage;
    }


	@Override
	public RetMessage<Boolean> adminAddAccount(AccountType type, String merchantName, String name, String phone,
			Long operaterId, Long merchantId) {
		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result = false;
		try {
			Account a = accountService.queryAccountById(operaterId);
			Merchant merchant = null;
			OperateType ot = null;
			if (AccountType.admin.equals(type.getValue())) {
				RetMessage message = checkAccount.apply(a, OperateType.createAccount);
				if (notNull.test(message))
					return message;
				ot = OperateType.createAccount;
			} else if (AccountType.merchantManager.equals(type.getValue())) {
				RetMessage message = checkAccount.apply(a, OperateType.createMerchantManager);
				if (notNull.test(message))
					return message;
				merchant = merchantService.queryMerchantParticularsById(merchantId);
				message = checkMerchant.apply(merchant, OperateType.createMerchantManager);
				if (notNull.test(message))
					return message;
				ot = OperateType.createMerchantManager;
			} else {
				return new RetMessage<>(RetCodeEnum.exception, "此接口只允许添加admin账户和商户管理员账户", null);
			}
			if(!accountService.phoneVerify(phone))
				return new RetMessage<>(RetCodeEnum.exception, "此手机号码已被注册", null);
			Account account = new Account();
			account = new Account();
			account.setAccountId(IdWorker.getId());
			account.setAccountType(type);
			account.setLoginFailureNum(0);
			account.setLoginFailureTime("20171114033028");
			account.setUserName(name);
			account.setPhone(phone);
			account.setStatus(AccountStatus.normal);
			account.setAddTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
			account.setAddById(operaterId);
			account.setAddByName(a.getUserName());
			if (merchant != null) {
				account.setMerchantId(merchantId);
				account.setMerchantName(merchantName);
			}
			account.setPassword(SignUtil.md5LowerCase("123456","utf-8"));
			result = accountService.addAccount(account);
			if (result) {
				retMessage.setRetCode(RetCodeEnum.success.getValue());
				retMessage.setRetMessage("添加管理员账户成功");
				OperateRecord record = new OperateRecord();
				record.setRecordId(IdWorker.getId());
				record.setOperaterId(operaterId);
				record.setAccountType(a.getAccountType());
				record.setPhone(a.getPhone());
				record.setName(a.getUserName());
				record.setTargetType(OperateTargetType.account);
				record.setType(ot);
				record.setOperateTime(TimeAssist.getNow());
				record.setOperateResult("success");
				record.setDescription("添加账户[" + account.getAccountId() + "]");
				operateRecordService.addRecord(record);
			}
			if (!result) {
				retMessage.setRetCode(RetCodeEnum.exception.getValue());
				retMessage.setRetMessage("添加账户失败");
			}
			retMessage.setDetail(result);
		} catch (Exception e) {
			return new RetMessage<>(RetCodeEnum.error, "添加管理员账户时发生异常", result);
		}
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> shopAddAccount(AccountType type, String name, String phone, Long operaterId) {
		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result = false;
		try {
			Account a = accountService.queryAccountById(operaterId);
			Merchant m = merchantService.queryMerchantParticularsById(a.getMerchantId());
			Shop s = shopService.queryShopInfo(a.getShopId());
			if (AccountType.shopEmployee.equals(type.getValue())) {
				RetMessage message = checkAccount.apply(a, OperateType.createEmployee);
				if (notNull.test(message))
					return message;
				message = checkMerchant.apply(m, OperateType.createEmployee);
				if (notNull.test(message))
					return message;
				message = checkShop.apply(s, OperateType.createEmployee);
				if (notNull.test(message))
					return message;
			} else {
				return new RetMessage<>(RetCodeEnum.fail, "您没有权限添加此类账户", null);
			}
			if(!accountService.phoneVerify(phone))
				return new RetMessage<>(RetCodeEnum.exception, "此手机号码已被注册", null);
			Account account = new Account();
			account.setAccountId(IdWorker.getId());
			account.setAccountType(type);
			account.setUserName(name);
			account.setLoginFailureNum(0);
			account.setLoginFailureTime("20171114033028");
			account.setPhone(phone);
			account.setStatus(AccountStatus.normal);
			account.setShopName(a.getShopName());
			account.setShopId(a.getShopId());
			account.setMerchantId(a.getMerchantId());
			account.setMerchantName(a.getMerchantName());
			account.setAddTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
			account.setAddById(operaterId);
			account.setAddByName(a.getUserName());
			account.setPassword(SignUtil.md5LowerCase("123456","utf-8"));
			result = accountService.addAccount(account);
			if (result) {
				retMessage.setRetCode(RetCodeEnum.success.getValue());
				retMessage.setRetMessage("添加账户成功");
				OperateRecord record = new OperateRecord();
				record.setRecordId(IdWorker.getId());
				record.setOperaterId(operaterId);
				record.setAccountType(a.getAccountType());
				record.setPhone(a.getPhone());
				record.setName(a.getUserName());
				record.setTargetType(OperateTargetType.account);
				record.setType(OperateType.createEmployee);
				record.setOperateTime(TimeAssist.getNow());
				record.setOperateResult("success");
				record.setDescription("添加账户[" + account.getAccountId() + "]");
				operateRecordService.addRecord(record);
			}
			if (!result) {
				retMessage.setRetCode(RetCodeEnum.exception.getValue());
				retMessage.setRetMessage("添加账户失败");
			}
			retMessage.setDetail(result);

		} catch (Exception e) {
			retMessage.setAll(RetCodeEnum.error, "添加核销员时发生异常", result);
		}
		return retMessage;
	}
	@Override
	public RetMessage<Boolean> merchantAddAccount(AccountType type, String name, String phone, String shopName,
			Long shopId, Long operaterId) {
		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result = false;
		// TODO 权限校验
		try {
			Account a = accountService.queryAccountById(operaterId);
			Merchant m = merchantService.queryMerchantParticularsById(a.getMerchantId());
			Shop shop = shopService.queryShopInfo(shopId);
			OperateType ot = null;
			if (AccountType.shopManager.equals(type.getValue())) {
				RetMessage message = checkAccount.apply(a, OperateType.createShopManager);
				if (notNull.test(message))
					return message;
				message = checkMerchant.apply(m, OperateType.createShopManager);
				if (notNull.test(message))
					return message;
				message = checkShop.apply(shop, OperateType.createShopManager);
				if (notNull.test(message))
					return message;
				ot = OperateType.createShopManager;
			} else if (AccountType.shopEmployee.equals(type.getValue())) {
				RetMessage message = checkAccount.apply(a, OperateType.createEmployee);
				if (notNull.test(message))
					return message;
				message = checkMerchant.apply(m, OperateType.createEmployee);
				if (notNull.test(message))
					return message;
				message = checkShop.apply(shop, OperateType.createEmployee);
				if (notNull.test(message))
					return message;
				ot = OperateType.createEmployee;
			} else {
				return new RetMessage<>(RetCodeEnum.fail, "您没有权限添加该类型账户", null);
			}
			if(!accountService.phoneVerify(phone))
				return new RetMessage<>(RetCodeEnum.exception, "此手机号码已被注册", null);
			Account account = new Account();
			account.setAccountId(IdWorker.getId());
			account.setAccountType(type);
			account.setUserName(name);
			account.setLoginFailureNum(0);
			account.setLoginFailureTime("20171114033028");
			account.setPhone(phone);
			account.setStatus(AccountStatus.normal);
			account.setShopName(shopName);
			account.setShopId(shopId);
			account.setMerchantId(a.getMerchantId());
			account.setMerchantName(a.getMerchantName());
			account.setAddTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
			account.setAddById(operaterId);
			account.setAddByName(a.getUserName());
			account.setPassword(SignUtil.md5LowerCase("123456","utf-8"));
			result = accountService.addAccount(account);
			if (result) {
				retMessage.setRetCode(RetCodeEnum.success.getValue());
				retMessage.setRetMessage("添加账户成功");
				OperateRecord record = new OperateRecord();
				record.setRecordId(IdWorker.getId());
				record.setOperaterId(operaterId);
				record.setAccountType(a.getAccountType());
				record.setPhone(a.getPhone());
				record.setName(a.getUserName());
				record.setTargetType(OperateTargetType.account);
				record.setType(ot);
				record.setOperateTime(TimeAssist.getNow());
				record.setOperateResult("success");
				record.setDescription("添加账户[" + account.getAccountId() + "]");
				operateRecordService.addRecord(record);
			}
			if (!result) {
				retMessage.setRetCode(RetCodeEnum.exception.getValue());
				retMessage.setRetMessage("添加账户失败");
			}
			retMessage.setDetail(result);
		} catch (Exception e) {
			LogFactory.error(this, "添加账户时发生异常", e);
			retMessage.setAll(RetCodeEnum.error, "添加账户时发生异常", result);
		}
		return retMessage;
	}

    @Override
    public RetMessage<Boolean> freezeAccount(Long accountId, Long operaterId) {
        RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
        Boolean result = false;
        // TODO 权限校验
        try {
            RetMessage message = null;
            OperateType ot = null;
            Account a = accountService.queryAccountById(operaterId);
            Account b = accountService.queryAccountById(accountId);
            if (AccountType.merchantManager.equals(b.getAccountType())) {
                message = checkAccount.apply(a, OperateType.freezeMerchantManager);
                if (notNull.test(message))
                    return message;
                ot = OperateType.freezeMerchantManager;
            }
            if (AccountType.shopManager.equals(b.getAccountType())) {
                message = checkAccount.apply(a, OperateType.freezeShopManager);
                if (notNull.test(message))
                    return message;
                ot = OperateType.freezeShopManager;
            }
            if (AccountType.shopEmployee.equals(b.getAccountType())) {
                message = checkAccount.apply(a, OperateType.freezeEmployee);
                if (notNull.test(message))
                    return message;
                ot = OperateType.freezeEmployee;
            }
            Account account = new Account();
            account.setAccountId(accountId);
            result = accountService.freezeAccount(account);
            if (result) {
                retMessage.setRetCode(RetCodeEnum.success.getValue());
                retMessage.setRetMessage("冻结账户成功");
                // TODO 添加操作记录
                OperateRecord record = new OperateRecord();
                record.setRecordId(IdWorker.getId());
                record.setOperaterId(operaterId);
                record.setAccountType(a.getAccountType());
                record.setPhone(a.getPhone());
                record.setName(a.getUserName());
                record.setTargetType(OperateTargetType.account);
                record.setType(ot);
                record.setOperateTime(TimeAssist.getNow());
                record.setOperateResult("success");
                record.setDescription("冻结账户[" + account.getAccountId() + "]");
                operateRecordService.addRecord(record);
            }
            if (!result) {
                retMessage.setRetCode(RetCodeEnum.exception.getValue());
                retMessage.setRetMessage("冻结账户失败");
            }
            retMessage.setDetail(result);

        } catch (Exception e) {
            LogFactory.error(this, "冻结账户时发生异常", e);
            retMessage.setAll(RetCodeEnum.error, "冻结账户时发生异常", result);
        }
        return retMessage;
    }

    @Override
    public RetMessage<Boolean> unfreezeAccount(Long accountId, Long operaterId) {

        RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
        Boolean result = false;
        // TODO 权限校验
        try {
            RetMessage message = null;
            OperateType ot = null;
            Account a = accountService.queryAccountById(operaterId);
            Account b = accountService.queryAccountById(accountId);
            if (AccountType.merchantManager.equals(b.getAccountType())) {
                message = checkAccount.apply(a, OperateType.unfreezeMerchantManager);
                if (notNull.test(message))
                    return message;
                ot = OperateType.unfreezeMerchantManager;
            }
            if (AccountType.shopManager.equals(b.getAccountType())) {
                message = checkAccount.apply(a, OperateType.unfreezeShopManager);
                if (notNull.test(message))
                    return message;
                ot = OperateType.unfreezeShopManager;
            }
            if (AccountType.shopEmployee.equals(b.getAccountType())) {
                message = checkAccount.apply(a, OperateType.unfreezeEmployee);
                if (notNull.test(message))
                    return message;
                ot = OperateType.unfreezeEmployee;
            }
            Account account = new Account();
            account.setAccountId(accountId);
            result = accountService.unfreezeAccount(account);
            if (result) {
                retMessage.setRetCode(RetCodeEnum.success.getValue());
                retMessage.setRetMessage("开启账户成功");
                OperateRecord record = new OperateRecord();
                record.setRecordId(IdWorker.getId());
                record.setOperaterId(operaterId);
                record.setAccountType(a.getAccountType());
                record.setPhone(a.getPhone());
                record.setName(a.getUserName());
                record.setTargetType(OperateTargetType.account);
                record.setType(ot);
                record.setOperateTime(TimeAssist.getNow());
                record.setOperateResult("success");
                record.setDescription("开启账户[" + account.getAccountId() + "]");
                operateRecordService.addRecord(record);
            }
            if (!result) {
                retMessage.setRetCode(RetCodeEnum.exception.getValue());
                retMessage.setRetMessage("开启账户失败");
            }
            retMessage.setDetail(result);
        } catch (Exception e) {
            LogFactory.error(this, "开启账户时发生异常", e);
            retMessage.setAll(RetCodeEnum.error, "开启账户时发生异常", result);
        }
        return retMessage;
    }

    @Override
    public RetMessage<Boolean> delAccount(Long accountId, Long operaterId) {
        RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
        Boolean result = false;
        // TODO 校验权限
        try {
            RetMessage message = null;
            OperateType ot = null;
            Account a = accountService.queryAccountById(operaterId);
            Account b = accountService.queryAccountById(accountId);
            if (AccountType.merchantManager.equals(b.getAccountType())) {
                message = checkAccount.apply(a, OperateType.deleteMerchantManager);
                if (notNull.test(message))
                    return message;
                ot = OperateType.deleteMerchantManager;
            }
            if (AccountType.shopManager.equals(b.getAccountType())) {
                message = checkAccount.apply(a, OperateType.deleteShopManager);
                if (notNull.test(message))
                    return message;
                ot = OperateType.deleteShopManager;
            }
            if (AccountType.shopEmployee.equals(b.getAccountType())) {
                message = checkAccount.apply(a, OperateType.deleteEmployee);
                if (notNull.test(message))
                    return message;
                ot = OperateType.deleteEmployee;
            }
            Account account = new Account();
            account.setAccountId(accountId);
            account.setStatus(AccountStatus.deleted);
            result = accountService.delAccount(account);
            if (result) {
                retMessage.setRetCode(RetCodeEnum.success.getValue());
                retMessage.setRetMessage("删除账户成功");
                OperateRecord record = new OperateRecord();
                record.setRecordId(IdWorker.getId());
                record.setOperaterId(operaterId);
                record.setAccountType(a.getAccountType());
                record.setPhone(a.getPhone());
                record.setName(a.getUserName());
                record.setTargetType(OperateTargetType.account);
                record.setType(ot);
                record.setOperateTime(TimeAssist.getNow());
                record.setOperateResult("success");
                record.setDescription("删除账户[" + account.getAccountId() + "]");
                operateRecordService.addRecord(record);
            } else {
                retMessage.setRetCode(RetCodeEnum.exception.getValue());
                retMessage.setRetMessage("删除账户失败");
            }
            retMessage.setDetail(result);
        } catch (Exception e) {
            LogFactory.error(this, "删除账户时发生异常", e);
            retMessage.setAll(RetCodeEnum.error, "删除账户时发生异常", result);
        }
        return retMessage;
    }

    @Override
    public RetMessage<String> queryAccounts(Long merchantId, Long shopId, String shopName, String phone,
                                            String merchantName, AccountType type, AccountStatus status, Integer pageNum, Integer pageSize,
                                            Long operaterId) {
        RetMessage<String> retMessage = new RetMessage<String>();
        List<Account> accounts = null;
        // TODO 校验
        try {
            RetMessage message = null;
            Account a = accountService.queryAccountById(operaterId);
            if (type == null) {
                if (merchantId == null) {
                    if (shopId == null) {
                        message = checkAccount.apply(a, OperateType.queryMerchantManager);
                        if (notNull.test(message))
                            return message;
                    } else {
                        message = checkAccount.apply(a, OperateType.queryEmployee);
                        if (notNull.test(message))
                            return message;
                    }
                } else {
                    message = checkAccount.apply(a, OperateType.queryShopManager);
                    if (notNull.test(message))
                        return message;
                }
            } else {
                if (AccountType.merchantManager.equals(type.getValue())) {
                    message = checkAccount.apply(a, OperateType.queryMerchantManager);
                    if (notNull.test(message))
                        return message;
                }
                if (AccountType.shopManager.equals(type.getValue())) {
                    message = checkAccount.apply(a, OperateType.queryShopManager);
                    if (notNull.test(message))
                        return message;
                }
                if (AccountType.shopEmployee.equals(type.getValue())) {
                    message = checkAccount.apply(a, OperateType.queryShopManager);
                    if (notNull.test(message))
                        return message;
                }
            }
            if (pageNum != null) {
                pageSize = pageSize == null ? ConstConfig.DEFAULT_PAGE_SIZE : pageSize;
            }
            Page<Account> pageObj = new Page<Account>(pageNum, pageSize);
            EntityWrapper<Account> wrapper = new EntityWrapper<Account>();
            if (merchantId != null) {
                wrapper.eq("merchant_id", merchantId);
            }
            if (shopId != null) {
                wrapper.eq("shop_id", shopId);
            }
            if (shopName != null && !shopName.isEmpty()) {
                wrapper.like("shop_name", shopName);
            }
            if (phone != null && !phone.isEmpty()) {
                wrapper.eq("phone", phone);
            }
            if (merchantName != null && !merchantName.isEmpty()) {
                wrapper.like("merchant_name", merchantName);
            }
            if (type != null) {
                wrapper.eq("account_type", type);
            }
            if (status != null) {
                wrapper.eq("status", status);
            }
            LogFactory.info(this, "条件装饰结果[" + wrapper + "]\n");
            accounts = accountService.queryAccounts(wrapper, pageObj);
            if (accounts != null && accounts.size() > 0) {
                retMessage.setRetCode(RetCodeEnum.success.getValue());
                retMessage.setRetMessage("查询完毕");
            } else {
                retMessage.setRetCode(RetCodeEnum.exception.getValue());
                retMessage.setRetMessage("没有查询到任何数据");
            }
            String result = JSON.toJSONString(accounts);
            retMessage.setDetail(result);
        } catch (Exception e) {
            LogFactory.error(this, "查询数据时出现异常", e);
            retMessage.setAll(RetCodeEnum.error, "查询数据时发生异常", null);
        }
        return retMessage;
    }

	@Override
	public RetMessage<String> queryAccounts( String shopName, String phone,
			String merchantName, AccountType type, AccountStatus status, Integer pageNum, Integer pageSize,
			Long operaterId) {
		RetMessage<String> retMessage = new RetMessage<String>();
		List<Account> accounts = null;
		// TODO 校验
		try {
			Account a = accountService.queryAccountById(operaterId);
			RetMessage message = checkAccount.apply(a, OperateType.queryAccount);
			if(notNull.test(message))
				return message;
			if (pageNum != null) {
				pageSize = pageSize == null ? ConstConfig.DEFAULT_PAGE_SIZE : pageSize;
			}
			Page<Account> pageObj = new Page<Account>(pageNum, pageSize);
			EntityWrapper<Account> wrapper = new EntityWrapper<Account>();
			if (shopName != null && !shopName.isEmpty()) {
				wrapper.like("shop_name", shopName);
			}
			if (phone != null && !phone.isEmpty()) {
				wrapper.eq("phone", phone);
			}
			if (merchantName != null && !merchantName.isEmpty()) {
				wrapper.like("merchant_name", merchantName);
			}
			if (type != null) {
				wrapper.eq("account_type", type);
			}
			if (status != null) {
				wrapper.eq("status", status);
			}
			LogFactory.info(this, "条件装饰结果[" + wrapper + "]\n");
			accounts = accountService.queryAccounts(wrapper, pageObj);
			if (accounts != null && accounts.size() > 0) {
				retMessage.setRetCode(RetCodeEnum.success.getValue());
				retMessage.setRetMessage("查询完毕");
			} else {
				retMessage.setRetCode(RetCodeEnum.exception.getValue());
				retMessage.setRetMessage("没有查询到任何数据");
			}
			String result = JSON.toJSONString(accounts);
			retMessage.setDetail(result);
		} catch (Exception e) {
			LogFactory.error(this, "查询数据时出现异常", e);
			retMessage.setAll(RetCodeEnum.error, "查询数据时发生异常", null);
		}
		return retMessage;
	}

	@Override
	public RetMessage<String> merchantQueryAccounts(String phone, String shopName, AccountType type,
			AccountStatus status, Integer pageNum, Integer pageSize, Long operaterId) {
		RetMessage<String> retMessage = new RetMessage<String>();
		List<Account> accounts = null;
		// TODO 校验
		try {
			Account a = accountService.queryAccountById(operaterId);
			RetMessage message = checkAccount.apply(a, OperateType.queryShopManager);
			if (notNull.test(message)) {
				return message;
			}
			Merchant m=merchantService.queryMerchantParticularsById(a.getMerchantId());
			message=checkMerchant.apply(m, OperateType.queryShopManager);
			if (notNull.test(message))
			return message;
			
			if(type!=null&&(AccountType.admin.equals(type.getValue())||AccountType.shopManager.equals(type.getValue()))){
				return new RetMessage<>(RetCodeEnum.fail,"您没有权限查询此类账户",null);
			}
			if (pageNum != null) {
				pageSize = pageSize == null ? ConstConfig.DEFAULT_PAGE_SIZE : pageSize;
			}
			Page<Account> pageObj = new Page<>(pageNum, pageSize);
			EntityWrapper<Account> wrapper = new EntityWrapper<Account>();
			if (phone != null && !phone.isEmpty()) {
				wrapper.eq("phone", phone);
			}
			if (shopName != null && !shopName.isEmpty()) {
				wrapper.eq("shop_name", shopName);
			}
			if (type==null) {
				wrapper.ne("account_type",AccountType.merchantManager);
			}
			if (type != null) {
				wrapper.eq("account_type", type);
			}
			if (status != null) {
				wrapper.eq("status", status);
			}
			wrapper.eq("merchant_id", a.getMerchantId());
			LogFactory.info(this, "条件装饰结果[" + wrapper + "]\n");
			accounts = accountService.queryAccounts(wrapper, pageObj);
			if (accounts != null && accounts.size() > 0) {
				retMessage.setRetCode(RetCodeEnum.success.getValue());
				retMessage.setRetMessage("查询完毕");
			} else {
				retMessage.setRetCode(RetCodeEnum.exception.getValue());
				retMessage.setRetMessage("没有查询到任何数据");
			}
			String result = JSON.toJSONString(accounts);
			retMessage.setDetail(result);
		} catch (Exception e) {
			LogFactory.error(this, "查询数据时出现异常", e);
			retMessage.setAll(RetCodeEnum.error, "查询数据时发生异常", null);
		}
		return retMessage;
	}

    @Override
    public RetMessage<String> queryAccountInfo(Long accountId, Long operaterId) {
        RetMessage<String> retMessage = new RetMessage<String>();
        Account account2 = null;
        // TODO 校验
        try {
            Account a = accountService.queryAccountById(operaterId);
            RetMessage message = checkAccount.apply(a, OperateType.queryAccountInfo);
            if (notNull.test(message)) {
                return message;
            }
            Account account = new Account();
            account2 = accountService.queryAccountInfo(account);
            if (account2 != null) {
                retMessage.setRetCode(RetCodeEnum.success.getValue());
                retMessage.setRetMessage("查询账户信息完毕");
            } else {
                retMessage.setRetCode(RetCodeEnum.exception.getValue());
                retMessage.setRetMessage("没有查询到任何数据");
            }
            String result = JSON.toJSONString(account2);
            retMessage.setDetail(result);
        } catch (Exception e) {
            LogFactory.error(this, "查询账户信息时出现异常", e);
            retMessage.setAll(RetCodeEnum.error, "查询账户信息时发生异常", null);
        }
        return retMessage;
    }

	@Override
	public RetMessage<String> queryShopAccounts(Long shopId, Long operaterId, Integer pageNum, Integer pageSize) {
		RetMessage<String> retMessage = new RetMessage<String>();
		List<Account> accounts = null;
		// TODO 校验
		try {
			Account a = accountService.queryAccountById(operaterId);
			RetMessage message = checkAccount.apply(a, OperateType.queryEmployee);
			if (notNull.test(message)) {
				return message;
			}
			Shop shop = shopService.queryShopInfo(shopId);
			message = checkShop.apply(shop, OperateType.queryEmployee);
			if (notNull.test(message)) {
				return message;
			}
			Page<Account> pageObj=null;
			if (pageNum != null) {
				pageSize = pageSize == null ? ConstConfig.DEFAULT_PAGE_SIZE : pageSize;
				pageObj = new Page<>(pageNum, pageSize);
			}
			EntityWrapper<Account> wrapper = new EntityWrapper<Account>();
			if (shopId != null) {
				wrapper.eq("shop_id", shopId);
			}
			LogFactory.info(this, "条件装饰结果[" + wrapper + "]\n");
			accounts = accountService.queryAccounts(wrapper, pageObj);
			if (accounts != null && accounts.size() > 0) {
				retMessage.setRetCode(RetCodeEnum.success.getValue());
				retMessage.setRetMessage("查询完毕");
			} else {
				retMessage.setRetCode(RetCodeEnum.exception.getValue());
				retMessage.setRetMessage("没有查询到任何数据");
			}
			String result = JSON.toJSONString(accounts);
			retMessage.setDetail(result);
		} catch (Exception e) {
			LogFactory.error(this, "查询数据时出现异常", e);
			retMessage.setAll(RetCodeEnum.error, "查询数据时发生异常", null);
		}
		return retMessage;
	}

}
