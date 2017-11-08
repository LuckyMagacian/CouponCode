package com.lanxi.couponcode.impl.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.lanxi.couponcode.impl.entity.Merchant;
import com.lanxi.couponcode.impl.entity.OperateRecord;
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
	@Resource
	private DaoService dao;
	@Override
	public boolean addMerchant(Merchant merchant,Long merchantId,Long operaterId,String operaterInfo,String operaterPhone) {
		// TODO Auto-generated method stub
		String locker="merchantId["+merchantId+"]\n"+
                
                "operaterId["+operaterId+"]\n"+
                "operaterPhone["+operaterPhone+"]\n"+
                "operaterInfo["+operaterInfo+"]\n";
		LogFactory.info(this, "尝试添加商户,\n"+locker);
		OperateRecord record=null;
		boolean result=false;
		try {
			merchant.setMerchantId(IdWorker.getId());
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
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this,"添加商户时发生异常,\n"+locker,e);
			//throw new RuntimeException("添加商户时发生异常",e);
		}finally {
			 boolean flag=record.insert();
             LogFactory.info(this,"添加商户操作记录["+record+"]结果["+flag+"],\n"+locker);  
		}
		return result;
	}
	@Override
	public boolean updateMerchantById(Merchant merchant, Long merchantId, Long operaterId, String operaterInfo,
										String operaterPhone) {
		String locker="merchantId["+merchantId+"]\n"+
                "operaterId["+operaterId+"]\n"+
                "operaterPhone["+operaterPhone+"]\n"+
                "operaterInfo["+operaterInfo+"]\n";
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
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this,"修改商户时发生异常,\n"+locker,e);
			//throw new RuntimeException("修改商户时发生异常",e);
		}finally {
			 boolean flag=record.insert();
             LogFactory.info(this,"修改商户操作记录["+record+"]结果["+flag+"],\n"+locker);  
		}
		return result;
	}
	
	@Override
	public List<Merchant> getAllMerchant() {
		
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
