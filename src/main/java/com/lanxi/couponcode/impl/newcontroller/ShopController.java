package com.lanxi.couponcode.impl.newcontroller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.impl.config.ConstConfig;
import com.lanxi.couponcode.impl.entity.Shop;
import com.lanxi.couponcode.impl.newservice.ShopService;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.couponcode.spi.consts.enums.ShopStatus;
import com.lanxi.util.entity.LogFactory;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

public class ShopController implements com.lanxi.couponcode.spi.service.ShopService{
	@Resource
	private ShopService shopService;
	@Override
	public RetMessage<Boolean> addShop(String shopName, String shopAddress, String minuteShopAddress, String serviceTel,
			Long merchantId, Long operaterId) {
		RetMessage<Boolean> retMessage=new RetMessage<Boolean>();
		Boolean result=false;
		try {
			if (merchantId!=null) {
				if (shopName!=null&&!shopName.isEmpty()&&shopAddress!=null&&!shopAddress.isEmpty()) {
					result=shopService.addShop(shopName, shopAddress, minuteShopAddress, serviceTel, merchantId, operaterId);
					if (result) {
						retMessage.setRetCode(RetCodeEnum.success.getValue());
						retMessage.setRetMessage("添加门店成功");
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
			// TODO: handle exception
			LogFactory.error(this,"添加门店时发生异常\n",e);
			retMessage.setDetail(result);
			retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("添加门店时发生异常");
		}
		// TODO Auto-generated method stub
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> importShops(File file, Long merchantId, Long operaterId) {
		RetMessage<Boolean> retMessage=new RetMessage<Boolean>();
		Boolean result=false;
		List<String>list=null;
		try {
			if(merchantId!=null) {
				if (file!=null) {
					if (file.getName().endsWith(".xlsx")||file.getName().endsWith(".xls")) {
						list=shopService.importShops(file, merchantId, operaterId);
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
			// TODO: handle exception
			LogFactory.error(this,"导入文件时发生异常");
			retMessage.setDetail(result);
			retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("导入文件时发生异常");
		}
		// TODO Auto-generated method stub
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> freezeShop(Long shopId, Long operaterId) {
		RetMessage<Boolean>retMessage=new RetMessage<Boolean>();
		Boolean result=false;
		try {
			if (shopId!=null) {
				result=shopService.freezeShop(shopId, operaterId);
				if (result) {
					retMessage.setRetCode(RetCodeEnum.success.getValue());
					retMessage.setRetMessage("冻结门店成功");
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
			// TODO: handle exception
			LogFactory.error(this,"冻结门店时发生异常\n",e);
			retMessage.setDetail(result);
			retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("冻结门店时发生异常");
		}
		// TODO Auto-generated method stub
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> unfreezeShop(Long shopId, Long operaterId) {
		RetMessage<Boolean>retMessage=new RetMessage<Boolean>();
		Boolean result=false;
		try {
			if (shopId!=null) {
				result=shopService.unfreezeShop(shopId, operaterId);
				if (result) {
					retMessage.setRetCode(RetCodeEnum.success.getValue());
					retMessage.setRetMessage("开启门店成功");
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
			// TODO: handle exception
			LogFactory.error(this,"开启门店时发生异常\n",e);
			retMessage.setDetail(result);
			retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("开启门店时发生异常");
		}
		// TODO Auto-generated method stub
		return retMessage;
	}

	@Override
	public RetMessage<String> queryShop(String merchantName, String shopName, ShopStatus status, String shopAddress,
			Integer pageNum, Integer pageSize, Long merchantId, Long operaterId) {
		RetMessage<String> retMessage=new RetMessage<String>();
		List<Shop> shops=null;
		try {
			if(pageNum!=null) {
				pageSize=pageSize==null?ConstConfig.DEFAULT_PAGE_SIZE:pageSize;
			}
			Page<Shop> pageObj=new Page<>(pageNum, pageSize);
			shops=shopService.queryShop(shopName, status, shopAddress, pageObj, merchantId, operaterId);
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
			// TODO: handle exception
			LogFactory.error(this, "查询数据时出现异常",e);
    		retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("查询数据时发生异常");
			retMessage.setDetail(null);
		}
		// TODO Auto-generated method stub
		return retMessage;
	}

	@Override
	public RetMessage<String> queryShop(String merchantName, String shopName, ShopStatus status, String shopAddress,
			Long merchantId, Long operaterId) {
		RetMessage<String> retMessage=new RetMessage<String>();
		List<Shop> shops=null;
		try {
			shops=shopService.queryShop(shopName, status, shopAddress, merchantId, operaterId);
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
			// TODO: handle exception
			LogFactory.error(this, "查询数据时出现异常",e);
    		retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("查询数据时发生异常");
			retMessage.setDetail(null);
		}
		// TODO Auto-generated method stub
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> modifyShop(String shopName, String shopAddress, String minuteShopAddress,
			String serviceTel, Long shopId, Long operaterId) {
		RetMessage<Boolean> retMessage=new RetMessage<Boolean>();
		Boolean result=false;
		try {
			if (shopId!=null) {
				if (shopName!=null&&!shopName.isEmpty()&&shopAddress!=null&&!shopAddress.isEmpty()) {
					result=shopService.modifyShop(shopName, shopAddress, minuteShopAddress, serviceTel, shopId, operaterId);
					if (result) {
						retMessage.setRetCode(RetCodeEnum.success.getValue());
						retMessage.setRetMessage("修改门店成功");
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
			// TODO: handle exception
			LogFactory.error(this, "修改门店信息时出现异常",e);
    		retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("修改门店信息时发生异常");
			retMessage.setDetail(result);
		}
		// TODO Auto-generated method stub
		return retMessage;
	}

	@Override
	public RetMessage<File> queryShopsExport(String shopName, String shopAddress, ShopStatus status, Long merchantId,
			Long operaterId) {
		RetMessage<File> retMessage=new RetMessage<File>();
		
		try {
			if (merchantId!=null) {
				File file=shopService.queryShopsExport(shopName, shopAddress, status, merchantId, operaterId);
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
			// TODO: handle exception
			LogFactory.error(this,"导出文件时放生异常");
			retMessage.setDetail(null);
			retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("导出文件时发生异常");
		}
		// TODO Auto-generated method stub
		return retMessage;
	}

}
