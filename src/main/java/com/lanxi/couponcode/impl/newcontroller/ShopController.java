package com.lanxi.couponcode.impl.newcontroller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.lanxi.couponcode.impl.entity.Account;
import com.lanxi.couponcode.impl.entity.Merchant;
import com.lanxi.couponcode.impl.entity.OperateRecord;
import com.lanxi.couponcode.impl.entity.Shop;
import com.lanxi.couponcode.impl.newservice.*;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.assist.TimeAssist;
import com.lanxi.couponcode.spi.config.ConstConfig;
import com.lanxi.couponcode.spi.consts.annotations.CheckArg;
import com.lanxi.couponcode.spi.consts.enums.OperateTargetType;
import com.lanxi.couponcode.spi.consts.enums.OperateType;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.couponcode.spi.consts.enums.ShopStatus;
import com.lanxi.couponcode.spi.defaultInterfaces.ToJson;
import com.lanxi.util.entity.LogFactory;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lanxi.couponcode.impl.assist.PredicateAssist.*;

/**
 * 
 * @author wuxiaobo
 *
 */
@Controller("shopControllerService")
public class ShopController implements com.lanxi.couponcode.spi.service.ShopService {
	@Resource
	private AccountService accountService;
	@Resource
	private MerchantService merchantService;
	@Resource
	private ShopService shopService;
	@Resource
	private RedisService redisService;
	@Resource
	private RedisEnhancedService redisEnhancedService;
	@Resource
	private OperateRecordService operateRecordService;

	@Override
	public RetMessage<Boolean> addShop(String shopName, String shopAddress, String minuteShopAddress, String serviceTel,
                                       Long merchantId, Long operaterId) {
		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result = false;
		Shop shop = null;
		// TODO 校验权限
		try {
			Account a = accountService.queryAccountById(operaterId);
			RetMessage message = checkAccount.apply(a, OperateType.createShop);
			if (notNull.test(message))
				return message;
			if (shopService.isRepeat(shopName,a.getMerchantId())) {
				Merchant m = merchantService.queryMerchantParticularsById(a.getMerchantId());
				message = checkMerchant.apply(m, OperateType.createShop);
				if (notNull.test(message))
					return message;
				shop = new Shop();
				shop.setShopName(shopName);
				shop.setShopAddress(shopAddress);
				shop.setMinuteShopAddress(minuteShopAddress);
				shop.setMerchantId(m.getMerchantId());
				shop.setServicetel(serviceTel);
				shop.setShopId(IdWorker.getId());
				shop.setShopStatus(ShopStatus.normal);
				shop.setAddId(operaterId);
				shop.setAddName(a.getUserName());
				shop.setMerchantName(m.getMerchantName());
				shop.setMerchantStatus(merchantService.queryMerchantStatusByid(shop.getMerchantId(), operaterId));
				shop.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
				result = shopService.addShop(shop);
				if (result) {
					retMessage.setRetCode(RetCodeEnum.success.getValue());
					retMessage.setRetMessage("添加门店成功");
					OperateRecord record = new OperateRecord();
					record.setRecordId(IdWorker.getId());
					record.setOperaterId(operaterId);
					record.setAccountType(a.getAccountType());
					record.setPhone(a.getPhone());
					record.setName(a.getUserName());
					record.setTargetType(OperateTargetType.shop);
					record.setType(OperateType.createShop);
					record.setOperateTime(TimeAssist.getNow());
					record.setOperateResult("success");
					record.setDescription("添加门店[" + shop.getShopId() + "]");
					operateRecordService.addRecord(record);
				} else {
					retMessage.setRetMessage("添加门店失败");
					retMessage.setRetCode(RetCodeEnum.exception.getValue());
				}
				retMessage.setDetail(result);
			}else {
				return new RetMessage<>(RetCodeEnum.fail,"门店名称重复",null);
			}
		
		} catch (Exception e) {
			retMessage.setAll(RetCodeEnum.error, "添加门店时发生异常", result);
		}
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> importShops(File file, Long merchantId, Long operaterId) {
		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result = false;
		List<String> list = null;
		// TODO 校验
		try {
			Account a = accountService.queryAccountById(operaterId);
			RetMessage message = checkAccount.apply(a, OperateType.importShops);
			if (notNull.test(message))
				return message;
			Merchant m = merchantService.queryMerchantParticularsById(merchantId);
			message = checkMerchant.apply(m, OperateType.importShops);
			if (notNull.test(message))
				return message;
			if (file.getName().endsWith(".xlsx") || file.getName().endsWith(".xls")) {
				String merchantStatus = merchantService.queryMerchantStatusByid(m.getMerchantId(), operaterId);
				list = shopService.importShops(file, m.getMerchantId(), operaterId, merchantStatus);
				if (list != null && list.size() == 0) {
					result = true;
					retMessage.setDetail(result);
					retMessage.setRetCode(RetCodeEnum.success.getValue());
					retMessage.setRetMessage("导入成功");
					OperateRecord record = new OperateRecord();
					record.setRecordId(IdWorker.getId());
					record.setOperaterId(operaterId);
					record.setAccountType(a.getAccountType());
					record.setPhone(a.getPhone());
					record.setName(a.getUserName());
					record.setTargetType(OperateTargetType.shop);
					record.setType(OperateType.importShops);
					record.setOperateTime(TimeAssist.getNow());
					record.setOperateResult("success");
					record.setDescription("批量导入门店[]");
					operateRecordService.addRecord(record);
				} else {
					result = false;
					retMessage.setDetail(result);
					retMessage.setRetCode(RetCodeEnum.exception.getValue());
					retMessage.setRetMessage(list.toString());
				}
			} else {
				result = false;
				retMessage.setDetail(result);
				retMessage.setRetCode(RetCodeEnum.fail.getValue());
				retMessage.setRetMessage("请导入Excel文件");
			}
		} catch (Exception e) {
			LogFactory.error(this, "导入文件时发生异常");
			retMessage.setAll(RetCodeEnum.error, "导入文件时发生异常", result);
		}
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> freezeShop(Long shopId, Long operaterId) {
		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result = false;
		// TODO 校验
		try {
			Account a = accountService.queryAccountById(operaterId);
			RetMessage message = checkAccount.apply(a, OperateType.freezeShop);
			if (notNull.test(message))
				return message;
			Shop s = shopService.queryShopInfo(shopId);
			message = checkShop.apply(s, OperateType.freezeShop);
			if (notNull.test(message))
				return message;
			Shop shop = new Shop();
			shop.setShopId(shopId);
			shop.setShopStatus(ShopStatus.freeze);
			result = shopService.freezeShop(shop);
			if (result) {
				retMessage.setRetCode(RetCodeEnum.success.getValue());
				retMessage.setRetMessage("冻结门店成功");
				OperateRecord record = new OperateRecord();
				record.setRecordId(IdWorker.getId());
				record.setOperaterId(operaterId);
				record.setAccountType(a.getAccountType());
				record.setPhone(a.getPhone());
				record.setName(a.getUserName());
				record.setTargetType(OperateTargetType.shop);
				record.setType(OperateType.freezeShop);
				record.setOperateTime(TimeAssist.getNow());
				record.setOperateResult("success");
				record.setDescription("冻结门店[" + shopId + "]");
				operateRecordService.addRecord(record);
			} else {
				retMessage.setRetMessage("冻结门店失败");
				retMessage.setRetCode(RetCodeEnum.exception.getValue());
			}
			retMessage.setDetail(result);
		} catch (Exception e) {
			LogFactory.error(this, "冻结门店时发生异常\n", e);
			retMessage.setAll(RetCodeEnum.error, "冻结门店时发生异常", result);
		}
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> unfreezeShop(Long shopId, Long operaterId) {
		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result = false;
		// TODO 校验
		try {
			Account a = accountService.queryAccountById(operaterId);
			RetMessage message = checkAccount.apply(a, OperateType.unfreezeShop);
			if (notNull.test(message))
				return message;
			Shop s = shopService.queryShopInfo(shopId);
			message = checkShop.apply(s, OperateType.unfreezeShop);
			if(message!=null)
				return message;
			Shop shop = new Shop();
			shop.setShopId(shopId);
			shop.setShopStatus(ShopStatus.normal);
			result = shopService.unfreezeShop(shop);
			if (result) {
				retMessage.setRetCode(RetCodeEnum.success.getValue());
				retMessage.setRetMessage("开启门店成功");
				OperateRecord record = new OperateRecord();
				record.setRecordId(IdWorker.getId());
				record.setOperaterId(operaterId);
				record.setAccountType(a.getAccountType());
				record.setPhone(a.getPhone());
				record.setName(a.getUserName());
				record.setTargetType(OperateTargetType.shop);
				record.setType(OperateType.unfreezeShop);
				record.setOperateTime(TimeAssist.getNow());
				record.setOperateResult("success");
				record.setDescription("开启门店[" + shopId + "]");
				operateRecordService.addRecord(record);
			} else {
				retMessage.setRetMessage("开启门店失败");
				retMessage.setRetCode(RetCodeEnum.exception.getValue());
			}
			retMessage.setDetail(result);
		} catch (Exception e) {
			LogFactory.error(this, "开启门店时发生异常\n", e);
			retMessage.setAll(RetCodeEnum.error, "开启门店时发生异常", result);
		}
		return retMessage;
	}

	@Override
	public RetMessage<String> queryShop(String merchantName, String shopName, ShopStatus status, String shopAddress,
                                        Integer pageNum, Integer pageSize, Long operaterId) {
		RetMessage<String> retMessage = new RetMessage<String>();
		List<Shop> shops = null;
		// TODO 校验
		try {
			Account a = accountService.queryAccountById(operaterId);
			RetMessage message = checkAccount.apply(a, OperateType.queryShop);
			if (notNull.test(message))
				return message;
			Merchant m = merchantService.queryMerchantParticularsById(a.getMerchantId());
			message = checkMerchant.apply(m, OperateType.queryShop);
			if (notNull.test(message))
				return message;
			if (pageNum != null) {
				pageSize = pageSize == null ? ConstConfig.DEFAULT_PAGE_SIZE : pageSize;
			}
			Page<Shop> pageObj = new Page<>(pageNum, pageSize);
			EntityWrapper<Shop> wrapper = new EntityWrapper<Shop>();
			if (pageObj != null) {
				if (shopName != null && !shopName.isEmpty()) {
					wrapper.like("shop_name", shopName);
				}
				if (status != null) {
					wrapper.eq("shop_status", status+"");
				}else {
					wrapper.in("shop_status", ShopStatus.normal.getValue()+","+ShopStatus.freeze.getValue());
				}
				
				if (shopAddress != null && !shopAddress.isEmpty()) {
					wrapper.like("shop_address", shopAddress);
				}
				wrapper.eq("merchant_id", a.getMerchantId());
			}
			LogFactory.info(this, "条件装饰结果[" + wrapper + "]\n");
			shops = shopService.queryShop(wrapper, pageObj);
			if (shops != null && shops.size() > 0) {
				retMessage.setRetCode(RetCodeEnum.success.getValue());
				retMessage.setRetMessage("查询完毕");
			} else {
				retMessage.setRetCode(RetCodeEnum.success.getValue());
				retMessage.setRetMessage("没有查询到任何数据");
			}
			Map<String, Object> map=new HashMap<>();
            map.put("page", pageObj);
            map.put("list", shops);
			String result =  ToJson.toJson(map);
			retMessage.setDetail(result);
		} catch (Exception e) {
			LogFactory.error(this, "查询数据时出现异常", e);
			retMessage.setAll(RetCodeEnum.error, "查询数据时发生异常", null);
		}

		return retMessage;
	}

	@Override
	public RetMessage<String> queryShop(String merchantName, String shopName, ShopStatus status, String shopAddress,
                                        Long merchantId, Long operaterId) {
		RetMessage<String> retMessage = new RetMessage<String>();
		List<Shop> shops = null;
		// TODO 校验
		try {
			Account a = accountService.queryAccountById(operaterId);
			RetMessage message = checkAccount.apply(a, OperateType.queryShop);
			if (merchantId != null) {
				Merchant m = merchantService.queryMerchantParticularsById(merchantId);
				message = checkMerchant.apply(m, OperateType.queryShop);
				if (notNull.test(message))
					return message;
			}
			EntityWrapper<Shop> wrapper = new EntityWrapper<Shop>();

			if (shopName != null && !shopName.isEmpty()) {
				wrapper.like("shop_name", shopName);
			}
			if (status != null) {
				wrapper.eq("shop_status", status+"");
			}
			wrapper.in("shop_status", ShopStatus.normal.getValue()+","+ShopStatus.freeze.getValue());
			if (merchantName!=null&&!merchantName.isEmpty()) {
				wrapper.like("merchant_name",merchantName);
			}
			if (shopAddress != null && !shopAddress.isEmpty()) {
				wrapper.like("shop_address", shopAddress);
			}
			wrapper.eq("merchant_id", merchantId);

			LogFactory.info(this, "条件装饰结果[" + wrapper + "]\n");
			shops = shopService.queryShop(wrapper);
			if (shops != null && shops.size() > 0) {
				retMessage.setRetCode(RetCodeEnum.success.getValue());
				retMessage.setRetMessage("查询完毕");
			} else {
				retMessage.setRetCode(RetCodeEnum.exception.getValue());
				retMessage.setRetMessage("没有查询到任何数据");
			}
			
			String result =  ToJson.toJson(shops);
			retMessage.setDetail(result);
		} catch (Exception e) {
			LogFactory.error(this, "查询数据时出现异常", e);
			retMessage.setAll(RetCodeEnum.error, "查询数据时发生异常", null);
		}
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> modifyShop(String shopName, String shopAddress, String minuteShopAddress,
                                          String serviceTel, Long shopId, Long operaterId) {
		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result = false;
		// TODO 校验
		try {
			Account a = accountService.queryAccountById(operaterId);
			RetMessage message = checkAccount.apply(a, OperateType.modifyShop);
			if (notNull.test(message))
				return message;
			Merchant m = merchantService
					.queryMerchantParticularsById(shopService.queryShopInfo(shopId).getMerchantId());
			message = checkMerchant.apply(m, OperateType.modifyShop);
			if (notNull.test(message))
				return message;
			if (shopService.isRepeat(shopId, shopName, m.getMerchantId())) {
				Shop s = shopService.queryShopInfo(shopId);
				message = checkShop.apply(s, OperateType.modifyShop);
				if (notNull.test(message))
					return message;
				
				Shop shop = new Shop();
				shop.setShopId(shopId);
				shop.setShopName(shopName);
				shop.setShopAddress(shopAddress);
				if (minuteShopAddress != null && !minuteShopAddress.isEmpty()) {
					shop.setMinuteShopAddress(minuteShopAddress);
				}
				if (serviceTel != null && !serviceTel.isEmpty()) {
					shop.setServicetel(serviceTel);
				}
				result = shopService.modifyShop(shop);
				if (result) {
					retMessage.setRetCode(RetCodeEnum.success.getValue());
					retMessage.setRetMessage("修改门店成功");
					accountService.modifyAccountShopName(shopId, shopName);
					OperateRecord record = new OperateRecord();
					record.setRecordId(IdWorker.getId());
					record.setOperaterId(operaterId);
					record.setAccountType(a.getAccountType());
					record.setPhone(a.getPhone());
					record.setName(a.getUserName());
					record.setTargetType(OperateTargetType.shop);
					record.setType(OperateType.modifyShop);
					record.setOperateTime(TimeAssist.getNow());
					record.setOperateResult("success");
					record.setDescription("修改门店[" + shopId + "]");
					operateRecordService.addRecord(record);
				} else {
					retMessage.setRetMessage("修改门店失败");
					retMessage.setRetCode(RetCodeEnum.exception.getValue());
				}
				retMessage.setDetail(result);
			}else {
				return new RetMessage<>(RetCodeEnum.fail,"门店名称重复",null);
			}
			
		} catch (Exception e) {
			retMessage.setAll(RetCodeEnum.error, "修改门店信息时发生异常", result);
		}
		return retMessage;
	}

	@Override
	public RetMessage<File> queryShopsExport(String shopName, String shopAddress, ShopStatus status, Integer pageNum,
                                             Integer pageSize, Long merchantId, Long operaterId) {
		RetMessage<File> retMessage = new RetMessage<File>();
		// TODO 校验
		try {
			Account a = accountService.queryAccountById(operaterId);
			RetMessage message = checkAccount.apply(a, OperateType.exportShop);
			if (notNull.test(message))
				return message;
			if (merchantId != null) {
				Merchant m = merchantService.queryMerchantParticularsById(merchantId);
				message = checkMerchant.apply(m, OperateType.exportShop);
				if (notNull.test(message))
					return message;
			}
//			if (pageNum != null) {
//				pageSize = pageSize == null ? ConstConfig.DEFAULT_PAGE_SIZE : pageSize;
//			}
//			Page<Shop> pageObj = new Page<>(pageNum, pageSize);
			if (merchantId != null) {
				EntityWrapper<Shop> wrapper = new EntityWrapper<Shop>();
				if (shopName != null && !shopName.isEmpty()) {
					wrapper.like("shop_name", shopName);
				}
				if (status != null) {
					wrapper.eq("shop_status", status+"");
				}else
				wrapper.in("shop_status", ShopStatus.normal.getValue()+","+ShopStatus.freeze.getValue());
				if (shopAddress != null && !shopAddress.isEmpty()) {
					wrapper.like("shop_address", shopAddress);
				}
				wrapper.eq("merchant_id", merchantId);
				LogFactory.info(this, "条件装饰结果[" + wrapper + "]\n");
				File file = shopService.queryShopsExport(wrapper, null);
				if (file != null) {
					retMessage.setAll(RetCodeEnum.success, "导出成功", file);
				} else {
					retMessage.setAll(RetCodeEnum.exception, "导出失败", null);
				}
			}
		} catch (Exception e) {
			LogFactory.error(this, "导出文件时发生异常");
			retMessage.setAll(RetCodeEnum.error, "导出文件时发生异常", null);
		}

		return retMessage;
	}

	@Override
	public RetMessage<File> downloadExcelTemplate(Long operaterId) {
		RetMessage<File> retMessage = new RetMessage<File>();
		try {
			Account a = accountService.queryAccountById(operaterId);
			RetMessage message = checkAccount.apply(a, OperateType.downloadShopExcelTemplate);
			if (notNull.test(message))
				return message;
			File file = shopService.downloadExcelTemplate();
			if (file != null) {
				retMessage.setAll(RetCodeEnum.success, "下载Excel模板成功", file);
			} else {
				retMessage.setAll(RetCodeEnum.exception, "下载Excel模板失败", null);
			}
		} catch (Exception e) {
			retMessage.setAll(RetCodeEnum.error, "下载Excel模板时发生异常", null);
		}
		return retMessage;
	}

	@Override
	public RetMessage<String> queryShops(Long merchantId, Long operaterId, Integer pageNum, Integer pageSize) {
		try {
			Account a = accountService.queryAccountById(operaterId);
			RetMessage message = checkAccount.apply(a, OperateType.queryMerchantInfo);
			if (notNull.test(message))
				return message;
			List<Shop> shops = null;
			Page<Shop> pageObj=null;
			if (pageNum != null) {
				pageSize = pageSize == null ? ConstConfig.DEFAULT_PAGE_SIZE : pageSize;
				pageObj = new Page<>(pageNum, pageSize);
				shops = shopService.queryShops(merchantId, pageObj);
			} else {
				shops = shopService.queryShops(merchantId, null);
			}
			
			if (shops != null && shops.size() > 0) {
				Map<String, Object> map=new HashMap<>();
	            map.put("page", pageObj);
	            map.put("list", shops);
				String result =  ToJson.toJson(map);
				return new RetMessage<String>(RetCodeEnum.success, "查询成功", result);
			} else
				return new RetMessage<String>(RetCodeEnum.success, "没有查询到任何信息", null);
		} catch (Exception e) {
			LogFactory.error(this, "查询时发生异常", e);
			return new RetMessage<>(RetCodeEnum.error, "查询时发生异常", null);
		}

	}
	@Override
	public RetMessage<String> adminQueryShop(String merchantName,String shopName,ShopStatus shopStatus,Long operaterId, Integer pageNum, Integer pageSize){
		try {
			Account a=accountService.queryAccountById(operaterId);
			if(isAdmin.negate().test(a))
			    return new RetMessage<>(RetCodeEnum.fail,"非管理员无权操作!",null);
			if (pageNum!=null) {
				pageSize = pageSize == null ? ConstConfig.DEFAULT_PAGE_SIZE : pageSize;
			}
			Page<Shop> pageObj=new Page<>(pageNum,pageSize);
			EntityWrapper<Shop> wrapper=new EntityWrapper<>();
			if (merchantName!=null&&!merchantName.isEmpty()) {
				wrapper.like("merchant_name", merchantName);
			}
			if (shopName!=null&&!shopName.isEmpty()) {
				wrapper.like("shop_name",shopName);
			}
			if (shopStatus!=null) {
				wrapper.eq("shop_status", shopStatus+"");
			}else {
				wrapper.in("shop_status", ShopStatus.normal.getValue()+","+ShopStatus.freeze.getValue());
				}
			
			List<Shop> list=shopService.adminQueryShop(wrapper, pageObj);
			
			if (list != null && list.size() > 0) {
				Map<String, Object> map=new HashMap<>();
	            map.put("page", pageObj);
	            map.put("list", list);
				String result =  ToJson.toJson(map);
				return new RetMessage<String>(RetCodeEnum.success, "查询成功", result);
			} else
				return new RetMessage<String>(RetCodeEnum.success, "没有查询到任何信息", null);
		} catch (Exception e) {
			LogFactory.error(this,"查询门店时发生异常",e);
			return new RetMessage<>(RetCodeEnum.error,"查询门店时发生异常",null);
		}
	}
	@CheckArg
	@Override
	public RetMessage<Serializable>queruAllShopIds(Long operaterId){
		Account account=accountService.queryAccountById(operaterId);
		if (isAdmin.test(account)||isMerchantManager.test(account)) {
			Map<String,Long>map=new HashMap<>();
			shopService.queryAllShop().parallelStream()
			.forEach(e->map.put(e.getShopName(), e.getShopId()));
			return new RetMessage<>(RetCodeEnum.success,"查询成功",(Serializable)map);
		}else
			return new RetMessage<>(RetCodeEnum.fail,"非管理员或者商户管理员无法操作!",null);
		
	}
}
