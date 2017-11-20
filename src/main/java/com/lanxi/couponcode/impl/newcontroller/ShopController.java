package com.lanxi.couponcode.impl.newcontroller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.impl.config.ConstConfig;
import com.lanxi.couponcode.impl.entity.Shop;
import com.lanxi.couponcode.impl.newservice.MerchantService;
import com.lanxi.couponcode.impl.newservice.ShopService;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.couponcode.spi.consts.enums.ShopStatus;
import com.lanxi.util.entity.LogFactory;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


/**
 * 
 * @author wuxiaobo
 *
 */
public class ShopController implements com.lanxi.couponcode.spi.service.ShopService{
	@Resource
	private MerchantService merchantService;
	@Resource
	private ShopService shopService;
	@Override
	public RetMessage<Boolean> addShop(String shopName, String shopAddress, String minuteShopAddress, String serviceTel,
			Long merchantId, Long operaterId) {
		RetMessage<Boolean> retMessage=new RetMessage<Boolean>();
		Boolean result=false;
		Shop shop=null;
		//TODO 校验权限
		try {
			if (merchantId!=null) {
				if (shopName!=null&&!shopName.isEmpty()&&shopAddress!=null&&!shopAddress.isEmpty()) {
					shop=new Shop();
					shop.setShopName(shopName);
					shop.setShopAddress(shopAddress);
					shop.setMinuteShopAddress(minuteShopAddress);
					shop.setMerchantId(merchantId);
					shop.setServicetel(serviceTel);
				//if(shop.getShopId()==null) {
					shop.setShopId(IdWorker.getId());
				//}
				shop.setShopStatus(ShopStatus.normal);
				shop.setMerchantStatus(merchantService.queryMerchantStatusByid(shop.getMerchantId(), operaterId));
				shop.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
				result=shopService.addShop(shop);
					if (result) {
						retMessage.setRetCode(RetCodeEnum.success.getValue());
						retMessage.setRetMessage("添加门店成功");
						//TODO 添加操作记录
					}else {
						retMessage.setRetMessage("添加门店失败");
						retMessage.setRetCode(RetCodeEnum.exception.getValue());
					}
					retMessage.setDetail(result);
				}else {
					result=false;
					retMessage.setDetail(result);
					retMessage.setRetCode(RetCodeEnum.fail.getValue());
					retMessage.setRetMessage("门店名称和经营地址都不能为空");
				}
			}else {
				result=false;
				retMessage.setDetail(result);
				retMessage.setRetCode(RetCodeEnum.fail.getValue());
				retMessage.setRetMessage("必须提供门店所属商户的id");
			}
			
		} catch (Exception e) {
			LogFactory.error(this,"添加门店时发生异常\n",e);
			retMessage.setDetail(result);
			retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("添加门店时发生异常");
		}
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> importShops(File file, Long merchantId, Long operaterId) {
		RetMessage<Boolean> retMessage=new RetMessage<Boolean>();
		Boolean result=false;
		List<String>list=null;
		//TODO 校验
		try {
			if(merchantId!=null) {
				if (file!=null) {
					if (file.getName().endsWith(".xlsx")||file.getName().endsWith(".xls")) {
						String merchantStatus=merchantService.queryMerchantStatusByid(merchantId, operaterId);
						list=shopService.importShops(file, merchantId, operaterId,merchantStatus);
						if (list!=null&&list.size()==0) {
							result=true;
							retMessage.setDetail(result);
							retMessage.setRetCode(RetCodeEnum.success.getValue());
							retMessage.setRetMessage("导入成功");
						}else {
							result=false;
							retMessage.setDetail(result);
							retMessage.setRetCode(RetCodeEnum.exception.getValue());
							retMessage.setRetMessage(list.toString());
						}
					}else {
						result=false;
						retMessage.setDetail(result);
						retMessage.setRetCode(RetCodeEnum.fail.getValue());
						retMessage.setRetMessage("请导入Excel文件");
					}
				}else {
					result=false;
					retMessage.setDetail(result);
					retMessage.setRetCode(RetCodeEnum.fail.getValue());
					retMessage.setRetMessage("没有获取到导入的文件");
				}
			}else {
				result=false;
				retMessage.setDetail(result);
				retMessage.setRetCode(RetCodeEnum.fail.getValue());
				retMessage.setRetMessage("没有获取到所属商户的id无法导入");
			}
		} catch (Exception e) {
			
			LogFactory.error(this,"导入文件时发生异常");
			retMessage.setDetail(result);
			retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("导入文件时发生异常");
		}
		
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> freezeShop(Long shopId, Long operaterId) {
		RetMessage<Boolean>retMessage=new RetMessage<Boolean>();
		Boolean result=false;
		//TODO 校验
		try {
			if (shopId!=null) {
				Shop shop=new Shop();
				shop.setShopId(shopId);
				shop.setShopStatus(ShopStatus.freeze);
				result=shopService.freezeShop(shop);
				if (result) {
					retMessage.setRetCode(RetCodeEnum.success.getValue());
					retMessage.setRetMessage("冻结门店成功");
					//TODO 添加操作记录
				}else {
					retMessage.setRetMessage("冻结门店失败");
					retMessage.setRetCode(RetCodeEnum.exception.getValue());
				}
				retMessage.setDetail(result);
			}else {
				result=false;
				retMessage.setDetail(result);
				retMessage.setRetCode(RetCodeEnum.fail.getValue());
				retMessage.setRetMessage("没有获取到门店id无法冻结门店");
			}
		} catch (Exception e) {
			LogFactory.error(this,"冻结门店时发生异常\n",e);
			retMessage.setDetail(result);
			retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("冻结门店时发生异常");
		}
		
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> unfreezeShop(Long shopId, Long operaterId) {
		RetMessage<Boolean>retMessage=new RetMessage<Boolean>();
		Boolean result=false;
		//TODO 校验
		try {
			if (shopId!=null) {
				Shop shop=new Shop();
				shop.setShopId(shopId);
				shop.setShopStatus(ShopStatus.normal);
				result=shopService.unfreezeShop(shop);
				if (result) {
					retMessage.setRetCode(RetCodeEnum.success.getValue());
					retMessage.setRetMessage("开启门店成功");
					//TODO 添加操作记录
				}else {
					retMessage.setRetMessage("开启门店失败");
					retMessage.setRetCode(RetCodeEnum.exception.getValue());
				}
				retMessage.setDetail(result);
			}else {
				result=false;
				retMessage.setDetail(result);
				retMessage.setRetCode(RetCodeEnum.fail.getValue());
				retMessage.setRetMessage("没有获取到门店id无法开启门店");
			}
		} catch (Exception e) {
			LogFactory.error(this,"开启门店时发生异常\n",e);
			retMessage.setDetail(result);
			retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("开启门店时发生异常");
		}
		return retMessage;
	}

	@Override
	public RetMessage<String> queryShop(String merchantName, String shopName, ShopStatus status, String shopAddress,
			Integer pageNum, Integer pageSize, Long merchantId, Long operaterId) {
		RetMessage<String> retMessage=new RetMessage<String>();
		List<Shop> shops=null;
		//TODO 校验
		try {
			if(pageNum!=null) {
				pageSize=pageSize==null?ConstConfig.DEFAULT_PAGE_SIZE:pageSize;
			}
			Page<Shop> pageObj=new Page<>(pageNum, pageSize);
			EntityWrapper<Shop> wrapper=new EntityWrapper<Shop>();
			if (pageObj!=null) {
				if (shopName!=null&&!shopName.isEmpty()) {
					wrapper.eq("shop_name",shopName);
				}
				if (status!=null) {
					wrapper.eq("shop_status", status);
				}
				if(shopAddress!=null&&!shopAddress.isEmpty()) {
					wrapper.eq("shop_address",shopAddress);
				}
				if(merchantId!=null) {
					wrapper.eq("merchant_id",merchantId);
				}
			}
			LogFactory.info(this, "条件装饰结果["+wrapper+"]\n");
			shops=shopService.queryShop(wrapper,pageObj);
			if(shops!=null&&shops.size()>0) {
				retMessage.setRetCode(RetCodeEnum.success.getValue());
				retMessage.setRetMessage("查询完毕");
			}else {
				retMessage.setRetCode(RetCodeEnum.exception.getValue());
				retMessage.setRetMessage("没有查询到任何数据");
			}
			String result=JSON.toJSONString(shops);
			retMessage.setDetail(result);
		} catch (Exception e) {
			LogFactory.error(this, "查询数据时出现异常",e);
    		retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("查询数据时发生异常");
			retMessage.setDetail(null);
		}
		
		return retMessage;
	}

	@Override
	public RetMessage<String> queryShop(String merchantName, String shopName, ShopStatus status, String shopAddress,
			Long merchantId, Long operaterId) {
		RetMessage<String> retMessage=new RetMessage<String>();
		List<Shop> shops=null;
		//TODO 校验
		try {
			EntityWrapper<Shop> wrapper=new EntityWrapper<Shop>();
			
				if (shopName!=null&&!shopName.isEmpty()) {
					wrapper.eq("shop_name",shopName);
				}
				if (status!=null) {
					wrapper.eq("shop_status", status);
				}
				if(shopAddress!=null&&!shopAddress.isEmpty()) {
					wrapper.eq("shop_address",shopAddress);
				}
				if(merchantId!=null) {
					wrapper.eq("merchant_id",merchantId);
				}
			
			LogFactory.info(this, "条件装饰结果["+wrapper+"]\n");
			shops=shopService.queryShop(wrapper);
			if(shops!=null&&shops.size()>0) {
				retMessage.setRetCode(RetCodeEnum.success.getValue());
				retMessage.setRetMessage("查询完毕");
			}else {
				retMessage.setRetCode(RetCodeEnum.exception.getValue());
				retMessage.setRetMessage("没有查询到任何数据");
			}
			String result=JSON.toJSONString(shops);
			retMessage.setDetail(result);
		} catch (Exception e) {
			
			LogFactory.error(this, "查询数据时出现异常",e);
    		retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("查询数据时发生异常");
			retMessage.setDetail(null);
		}
		
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> modifyShop(String shopName, String shopAddress, String minuteShopAddress,
			String serviceTel, Long shopId, Long operaterId) {
		RetMessage<Boolean> retMessage=new RetMessage<Boolean>();
		Boolean result=false;
		//TODO 校验
		try {
			if (shopId!=null) {
				if (shopName!=null&&!shopName.isEmpty()&&shopAddress!=null&&!shopAddress.isEmpty()) {
					Shop shop=new Shop();
					shop.setShopId(shopId);
					shop.setShopName(shopName);
					shop.setShopAddress(shopAddress);
					if (minuteShopAddress!=null&&!minuteShopAddress.isEmpty()) {
						shop.setMinuteShopAddress(minuteShopAddress);
					}
					if (serviceTel!=null&&!serviceTel.isEmpty()) {
						shop.setServicetel(serviceTel);
					}
					result=shopService.modifyShop(shop);
					if (result) {
						retMessage.setRetCode(RetCodeEnum.success.getValue());
						retMessage.setRetMessage("修改门店成功");
						//TODO 添加操作记录
					}else {
						retMessage.setRetMessage("修改门店失败");
						retMessage.setRetCode(RetCodeEnum.exception.getValue());
					}
					retMessage.setDetail(result);
				}else {
					result=false;
					retMessage.setDetail(result);
					retMessage.setRetCode(RetCodeEnum.fail.getValue());
					retMessage.setRetMessage("门店名称和经营地址都不能为空");
				}
			}else {
				result=false;
				retMessage.setDetail(result);
				retMessage.setRetCode(RetCodeEnum.fail.getValue());
				retMessage.setRetMessage("没有获取到门店id无法修改门店信息");
			}
		} catch (Exception e) {
			
			LogFactory.error(this, "修改门店信息时出现异常",e);
    		retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("修改门店信息时发生异常");
			retMessage.setDetail(result);
		}
		
		return retMessage;
	}

	@Override
	public RetMessage<File> queryShopsExport(String shopName, String shopAddress, ShopStatus status,Integer pageNum,Integer pageSize, Long merchantId,
			Long operaterId) {
		RetMessage<File> retMessage=new RetMessage<File>();
		//TODO 校验
		try {
			if(pageNum!=null) {
				pageSize=pageSize==null?ConstConfig.DEFAULT_PAGE_SIZE:pageSize;
			}
			Page<Shop> pageObj=new Page<>(pageNum, pageSize);
			if (merchantId!=null) {
				EntityWrapper<Shop> wrapper=new EntityWrapper<Shop>();
				if (shopName!=null&&!shopName.isEmpty()) {
					wrapper.eq("shop_name",shopName);
				}
				if (status!=null) {
					wrapper.eq("shop_status", status);
				}
				if(shopAddress!=null&&!shopAddress.isEmpty()) {
					wrapper.eq("shop_address",shopAddress);
				}
					wrapper.eq("merchant_id",merchantId);
			LogFactory.info(this, "条件装饰结果["+wrapper+"]\n");
				File file=shopService.queryShopsExport(wrapper, pageObj);
				if (file!=null) {
					retMessage.setDetail(file);
					retMessage.setRetCode(RetCodeEnum.success.getValue());
					retMessage.setRetMessage("导出成功");
				}else {
					retMessage.setDetail(null);
					retMessage.setRetCode(RetCodeEnum.exception.getValue());
					retMessage.setRetMessage("导出失败");
				}
			}else {
				retMessage.setDetail(null);
				retMessage.setRetCode(RetCodeEnum.fail.getValue());
				retMessage.setRetMessage("没有获取到门店所对应的商户id无法导出");
			}
		} catch (Exception e) {
			
			LogFactory.error(this,"导出文件时放生异常");
			retMessage.setDetail(null);
			retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("导出文件时发生异常");
		}
		
		return retMessage;
	}

}
