package com.lanxi.couponcode.impl.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.lanxi.couponcode.impl.config.ConstConfig;
import com.lanxi.couponcode.impl.entity.OperateRecord;
import com.lanxi.couponcode.impl.entity.Shop;
import com.lanxi.couponcode.spi.consts.enums.OperateTargetType;
import com.lanxi.couponcode.spi.consts.enums.ShopStatus;
import com.lanxi.util.entity.LogFactory;
import com.lanxi.util.utils.TimeUtil;
@Service("shopService")
public class ShopServiceImpl implements ShopService{
	@Resource
	private DaoService dao;

	@Override
	public Boolean freezeAllShop(Long merchantId, Long operaterId, String operaterInfo, String operaterPhone) {
		String locker="merchantId["+merchantId+"]\n"+
				",operaterId["+operaterId+"]\n"+
                ",operaterPhone["+operaterPhone+"]\n"+
                ",operaterInfo["+operaterInfo+"]\n";
		LogFactory.info(this, "尝试冻结"+merchantId+"所有门店,\n"+locker);
		
		boolean result=false;
		//因为要冻结所有门店shop直接设置为freeze
		Shop shop=null;
		try {
			shop=new Shop();
			shop.setShopStatus(ShopStatus.freeze+"");
			EntityWrapper<Shop> wrapper=new EntityWrapper<Shop>();
			wrapper.eq("merchant_id", merchantId);
            Integer var=dao.getShopDao().update(shop, wrapper);
            if(var<0) {
            	LogFactory.debug(this, "冻结所有门店失败"+locker);
            	result=false;
            }else if (var>0) {
				LogFactory.info(this,"冻结所有门店成功"+locker);
				result=true;
			}
            }catch (Exception e) {
				// TODO: handle exception
            	LogFactory.error(this,"冻结所有门店时发生异常",e);
			}
		// TODO Auto-generated method stub
		return result;
	}

	

//	@Override
//	public List<Shop> queryPossessShopByMerchantId(Integer page, Integer size, Long merchantId, Long operaterId,
//			String operaterInfo, String operaterPhone) {
//		String locker="merchantId["+merchantId+"]\n"+
//                ",operaterId["+operaterId+"]\n"+
//                ",operaterPhone["+operaterPhone+"]\n"+
//                ",operaterInfo["+operaterInfo+"]\n";
//		LogFactory.info(this,"尝试根据商户id获取门店"+locker);
//		List<Shop> list=null;
//		try {
//			Page<Shop> pageObj=null;
//	        EntityWrapper<Shop> wrapper=new EntityWrapper<Shop>();
//	        if(page!=null) {
//	        	 //配置分页信息
//	        	 size=size==null? ConstConfig.DEFAULT_PAGE_SIZE:size;
//	        	 pageObj=new Page<Shop>(page,size);
//	         }
//			if(merchantId!=null) {
//				wrapper.eq("merchant_id", merchantId);
//			}
//			list=dao.getShopDao().selectPage(pageObj, wrapper);
//			LogFactory.info(this, "条件装饰结果["+wrapper+"]"+locker);
//			LogFactory.debug(this,"查询结果["+list+"]"+locker);
//			LogFactory.info(this,"查询到的记录数["+list.size()+"]"+locker);
//			return list;
//			} catch (Exception e) {
//			// TODO: handle exception
//			LogFactory.error(this,"尝试根据商户id获取门店时发生异常",e);
//			return list;
//		}
//		
//	}
}
