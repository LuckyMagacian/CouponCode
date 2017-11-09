package com.lanxi.couponcode.impl.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.lanxi.couponcode.impl.config.ConstConfig;
import com.lanxi.couponcode.impl.entity.Merchant;
import com.lanxi.couponcode.impl.entity.OperateRecord;
import com.lanxi.couponcode.impl.entity.Shop;
import com.lanxi.couponcode.spi.consts.enums.MerchantStatus;
import com.lanxi.couponcode.spi.consts.enums.OperateTargetType;
import com.lanxi.util.entity.LogFactory;
import com.lanxi.util.utils.TimeUtil;
/**
 * 
 * @author wuxiaobo
 *
 */
@Service("merchantService")
public class MerchantServiceImpl implements MerchantService{
	
	private TransactionDefinition txDefinition;
	@Resource
	private PlatformTransactionManager txManager;
	@Resource
	private DaoService dao;
	@Resource
	private ShopService shopService;
	@Override
	public Boolean addMerchant(Merchant merchant,Long operaterId,String operaterInfo,String operaterPhone) {
		// TODO Auto-generated method stub
		String locker="operaterId["+operaterId+"]\n"+
                ",operaterPhone["+operaterPhone+"]\n"+
                ",operaterInfo["+operaterInfo+"]\n";
		LogFactory.info(this, "尝试添加商户,\n"+locker);
		OperateRecord record=null;
		boolean result=false;
		try {
			merchant.setMerchantStatus(MerchantStatus.normal+"");
			merchant.setMerchantId(IdWorker.getId());
			System.out.println(merchant.getMerchantId());
			merchant.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss")));
			//尝试添加操作记录
			record=new OperateRecord();
            record.setRecordId(IdWorker.getId());
            record.setOperaterId(operaterId);
            record.setOperaterPhone(operaterPhone);
            record.setOperaterInfo(operaterInfo);
            record.setTargetType(OperateTargetType.code);
            record.setDescription("添加商户");
            record.setOperateTime(TimeUtil.getDateTime());
            record.setTargetInfo(merchant.toJson());
			int var=dao.getMerchantDao().insert(merchant);
			if(var<0) {
				LogFactory.info(this,"添加商户失败\n"+locker);
				record.setOperateResult("失败");
				result=false;
			}else if (var>0) {
				LogFactory.info(this,"添加商户成功\n"+locker);
				record.setOperateResult("成功");
				result=true;
				boolean flag=record.insert();
	             LogFactory.info(this,"添加商户操作记录["+record+"]结果["+flag+"],\n"+locker); 
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this,"添加商户时发生异常,\n"+locker,e);
			//throw new RuntimeException("添加商户时发生异常",e);
		}
		return result;
	}
	@Override
	public Boolean updateMerchantById(Merchant merchant, Long operaterId, String operaterInfo,
										String operaterPhone) {
		String locker="operaterId["+operaterId+"]\n"+
                ",operaterPhone["+operaterPhone+"]\n"+
                ",operaterInfo["+operaterInfo+"]\n";
		LogFactory.info(this, "尝试修改商户,\n"+locker);
		OperateRecord record=null;
		boolean result=false;
		try {
			//尝试添加操作记录
			record=new OperateRecord();
            record.setRecordId(IdWorker.getId());
            record.setOperaterId(operaterId);
            record.setOperaterPhone(operaterPhone);
            record.setOperaterInfo(operaterInfo);
            record.setTargetType(OperateTargetType.code);
            record.setDescription("修改商户");
            record.setOperateTime(TimeUtil.getDateTime());
            record.setTargetInfo(merchant.toJson());
			int var=dao.getMerchantDao().updateById(merchant);
			if(var<0) {
				LogFactory.info(this,"修改商户失败\n"+locker);
				record.setOperateResult("失败");
				result=false;
			}else if (var>0) {
				LogFactory.info(this,"修改商户成功\n"+locker);
				record.setOperateResult("成功");
				result=true;
				boolean flag=record.insert();
	             LogFactory.info(this,"修改商户操作记录["+record+"]结果["+flag+"],\n"+locker); 
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this,"修改商户时发生异常,\n"+locker,e);
			//throw new RuntimeException("修改商户时发生异常",e);
		}
		return result;
	}
	
	@Override
	public List<Merchant> getAllMerchant(Integer page,Integer size, Long operaterId, String operaterInfo,
			String operaterPhone) {
		String locker="page["+page+"]\n"+
			    ",size["+size+"]\n"+
                ",operaterId["+operaterId+"]\n"+
                ",operaterPhone["+operaterPhone+"]\n"+
                ",operaterInfo["+operaterInfo+"]\n";
		LogFactory.info(this, "尝试获取商户信息,\n"+locker);
		List<Merchant> list=null;
		try {
			Page<Merchant>pageObj=null;
			if(page!=null) {
				//分页信息
				size=size==null? ConstConfig.DEFAULT_PAGE_SIZE:size;
				pageObj=new Page<Merchant>(page,size);
			}
			//执行查询
			list=dao.getMerchantDao().selectPage(pageObj,null);
			LogFactory.debug(this,"查询结果["+list+"]"+locker);
			LogFactory.info(this,"查询到的记录数["+list.size()+"]"+locker);
			return list;
		} catch (Exception e) {
			LogFactory.error(this,"查询商户时发生异常"+locker,e);
			return list;
		}
		// TODO Auto-generated method stub
		
	}
	@Override
	public List<Merchant> getMerChantByCondition(Integer page, Integer size,Long operaterId,
			String operaterInfo, String operaterPhone, String startTime, String endTime, String merchantStatus,
			String merchantName) {
		String locker="page["+page+"]\n"+
				",size["+size+"]\n"+
				",operaterId["+operaterId+"]\n"+
				",operaterInfo["+operaterInfo+"]\n"+
				",operaterPhone["+operaterPhone+"]\n"+
				",startTime["+startTime+"]\n"+
				",endTime["+endTime+"]\n"+
				",merchantStatus["+merchantStatus+"]\n"+
				",merchantName["+merchantName+"]\n";
		LogFactory.info(this, "尝试按条件获取商户信息\n,"+locker);
		List<Merchant> list=null;
		try {
			 Page<Merchant> pageObj=null;
	         EntityWrapper<Merchant> wrapper=new EntityWrapper<Merchant>();
	         if(page!=null) {
	        	 //配置分页信息
	        	 size=size==null? ConstConfig.DEFAULT_PAGE_SIZE:size;
	        	 pageObj=new Page<Merchant>(page,size);
	         }
	         //组装查询条件
	         if(startTime!=null&&!startTime.isEmpty()){
	                while(startTime.length()<14)
	                    startTime+="0";
	                wrapper.ge("create_time",startTime);
	            }
	            if(endTime!=null&&!endTime.isEmpty()){
	                while(endTime.length()<14)
	                    endTime+="9";
	                wrapper.le("create_time",endTime);
	            }
	            if(merchantStatus!=null&&!merchantStatus.isEmpty()) {
	            	wrapper.eq("merchant_status", merchantStatus);
	            }
	            if(merchantName!=null&&!merchantName.isEmpty()) {
	            	wrapper.eq("merchant_name", merchantName);
	            }
	            list=dao.getMerchantDao().selectPage(pageObj, wrapper);
	            //日志记录
	            LogFactory.info(this, "条件装饰结果["+wrapper+"]"+locker);
	            LogFactory.debug(this,"查询结果["+list+"]"+locker);
				LogFactory.info(this,"查询到的记录数["+list.size()+"]"+locker);
				
				return list;
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this,"查询商户时发生异常"+locker,e);
			return list;
		}
		
	}
	@Override
	public Merchant queryMerchantParticularsById(Long merchantId, Long operaterId,
			String operaterInfo, String operaterPhone) {
		String locker="merchantId["+merchantId+"]\n"+
                ",operaterId["+operaterId+"]\n"+
                ",operaterPhone["+operaterPhone+"]\n"+
                ",operaterInfo["+operaterInfo+"]\n";
		LogFactory.info(this,"尝试获取商户详情"+locker);
		Merchant merchant=null;
		try {
			
			if(merchantId!=null) {
				merchant=dao.getMerchantDao().selectById(merchantId);
				LogFactory.debug(this,"查询结果["+merchant+"]"+locker);
			}
			return merchant;
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this, "查询商户详情是发生异常"+locker,e);
			return merchant;
		}
		
	}
	@Override
	public List<Shop> queryPossessShopByMerchantId(Integer page,Integer size,Long merchantId, Long operaterId, String operaterInfo,
			String operaterPhone) {
		String locker="merchantId["+merchantId+"]\n"+
                ",operaterId["+operaterId+"]\n"+
                ",operaterPhone["+operaterPhone+"]\n"+
                ",operaterInfo["+operaterInfo+"]\n";
		LogFactory.info(this,"尝试获取商户下属门店"+locker);
		List<Shop> list=null;
		try {
			Page<Shop> pageObj=null;
	        EntityWrapper<Shop> wrapper=new EntityWrapper<Shop>();
	        if(page!=null) {
	        	 //配置分页信息
	        	 size=size==null? ConstConfig.DEFAULT_PAGE_SIZE:size;
	        	 pageObj=new Page<Shop>(page,size);
	         }
			if(merchantId!=null) {
				wrapper.eq("merchant_id", merchantId);
			}
			list=dao.getShopDao().selectPage(pageObj, wrapper);
			LogFactory.info(this, "条件装饰结果["+wrapper+"]"+locker);
			LogFactory.debug(this,"查询结果["+list+"]"+locker);
			LogFactory.info(this,"查询到的记录数["+list.size()+"]"+locker);
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this,"尝试根据商户id获取门店时发生异常"+locker,e);
			return list;
		}
		
	}
	@Override
	public Boolean changeMerchanStatus(Long merchantId, Long operaterId, String operaterInfo, String operaterPhone,
			String merchantStatus) {
		String locker="merchantId["+merchantId+"]\n"+
                ",operaterId["+operaterId+"]\n"+
                ",operaterPhone["+operaterPhone+"]\n"+
                ",operaterInfo["+operaterInfo+"]\n"+
                ",merchatStatus["+merchantStatus+"]\n";
		LogFactory.info(this, "尝试修改商户状态,\n"+locker);
		OperateRecord record=null;
		Merchant merchant=null;
		boolean result=false;
		try {
			merchant=new Merchant();
			merchant.setMerchantId(merchantId);
			record=new OperateRecord();
            record.setRecordId(IdWorker.getId());
            record.setOperaterId(operaterId);
            record.setOperaterPhone(operaterPhone);
            record.setOperaterInfo(operaterInfo);
            record.setTargetType(OperateTargetType.code);
            record.setDescription("修改商户状态");
            record.setOperateTime(TimeUtil.getDateTime());
            record.setTargetInfo(merchant.toJson());
			//先判断要把商户状态修改为什么状态
			if(merchantStatus.equals("normal")) {
				merchant.setMerchantStatus(MerchantStatus.normal+"");
				Integer var=dao.getMerchantDao().updateById(merchant);
				if(var<0) {
					LogFactory.info(this,"修改商户状态失败\n"+locker);
					record.setOperateResult("失败");
					result=false;
					
				}else if (var>0) {
					LogFactory.info(this,"修改商户状态成功\n"+locker);
					record.setOperateResult("成功");
					result=true;
					boolean flag=record.insert();
		             LogFactory.info(this,"修改商户状态操作记录["+record+"]结果["+flag+"],\n"+locker); 
				}
			}
			if(merchantStatus.equals("freeze")) {
				merchant.setMerchantStatus(MerchantStatus.freeze+"");
				Boolean change=changeStatus(merchant, operaterId, operaterInfo, operaterPhone);
				if(change) {
					LogFactory.info(this,"修改商户状态成功\n"+locker);
					result=true;
					record.setOperateResult("成功");
					boolean flag=record.insert();
		             LogFactory.info(this,"修改商户状态操作记录["+record+"]结果["+flag+"],\n"+locker); 
				}
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this,"尝试根据商户id修改商户状态时发生异常"+locker,e);
		}
		// TODO Auto-generated method stub
		return result;
	}
	
	public Boolean changeStatus(Merchant merchant,Long operaterId,String operaterInfo, String operaterPhone) {
		
		TransactionStatus txStatus = txManager.getTransaction(txDefinition);
		Boolean result=false;
		try {
			if(merchant!=null) {
				LogFactory.info(this,"尝试同时冻结门店和商户");
				Boolean freezeResult=shopService.freezeAllShop(merchant.getMerchantId(),operaterId , operaterInfo, operaterPhone);
				
				Integer var=dao.getMerchantDao().updateById(merchant);
				if(var>0&&freezeResult) {
					LogFactory.info(this,"同时冻结门店和商户成功");
					result=true;
				}
			}
			txManager.commit(txStatus);
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this,"同时冻结门店和商户失败",e);
			txManager.rollback(txStatus);
			throw new RuntimeException("同时冻结门店和商户失败",e);
			
		}
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
