package com.lanxi.couponcode.impl.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;

import com.lanxi.couponcode.impl.entity.OperateRecord;
import com.lanxi.couponcode.impl.entity.Shop;
import com.lanxi.couponcode.spi.consts.enums.OperateTargetType;
import com.lanxi.couponcode.spi.consts.enums.ShopStatus;
import com.lanxi.util.entity.LogFactory;
import com.lanxi.util.utils.ExcelUtil;
import com.lanxi.util.utils.TimeUtil;
@Service("shopService")
public class ShopServiceImpl implements ShopService{
	@Resource
	private DaoService dao;
	@Resource
	private MerchantService merchantService;
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
			shop.setShopStatus(ShopStatus.freeze);
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

	@Override
	public Boolean addShop(String shopName,String shopAddress,String minuteShopAddress,String serviceTel,Long merchantId,
	           Long operaterId) {
		String locker="operaterId["+operaterId+"]\n"+
				",serviceTel["+serviceTel+"]\n"+
				",merchantId["+merchantId+"]\n"+
                ",shopName["+shopName+"]\n"+
                ",shopAddress["+shopAddress+"]\n";
		LogFactory.info(this, "尝试单个添加门店,\n"+locker);
		OperateRecord record=null;
		boolean result=false;
		Shop shop=null;
		try {
			if (shopName!=null&&!shopName.isEmpty()&&shopAddress!=null&&!shopAddress.isEmpty()&&merchantId!=null) {
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
            Integer var=dao.getShopDao().insert(shop);
            if(var<0) {
				LogFactory.debug(this,"添加门店失败\n"+locker);
				result=false;
			}else if (var>0) {
				LogFactory.debug(this,"添加门店成功\n"+locker);
				result=true;
				LogFactory.info(this,"尝试单个添加门店\n"+locker);
				record=new OperateRecord();
	            record.setRecordId(IdWorker.getId());
	            record.setOperaterId(operaterId);
	            record.setTargetType(OperateTargetType.shop);
	            record.setDescription("单个添加门店");
	            record.setOperateTime(TimeUtil.getDateTime());
	            record.setTargetInfo(shop.toJson());
	            record.setOperateResult("成功");
				boolean flag=record.insert();
	            LogFactory.info(this,"添加门店操作记录["+record+"]结果["+flag+"],\n"+locker); 
			}
			}
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this,"添加门店的时候发生异常,\n"+locker,e);
			
		}
		// TODO Auto-generated method stub
		return result;
	}

	@Override
	public List<String> importShops(File file, Long merchantId, Long operaterId) {
		String locker="shopId["+merchantId+"]\n"+
				",operaterId["+operaterId+"]\n";
		Shop shop=null;
		List<String> list=new ArrayList<String>();
		try {
			if (file!=null&&merchantId!=null) {
				String merchantStatus=merchantService.queryMerchantStatusByid(merchantId, operaterId);
				InputStream inputStream=new FileInputStream(file);
				
				Workbook workbook=null;
				try {
					workbook=new HSSFWorkbook(inputStream);
				} catch (Exception e) {
					// TODO: handle exception
					//解决read error异常
					inputStream=new FileInputStream(file);
					workbook=new XSSFWorkbook(inputStream);
				}
				//获取sheet的数量
				int num=workbook.getNumberOfSheets();
				for(int j=0;j<num;j++) {
					Sheet sheet=workbook.getSheetAt(j);
					//获取数据的总行数
					int total=sheet.getLastRowNum();
					//int[] num=new int[total-1];
					for(int i=1;i<=total;i++) {
						//获得第i行对象
						Row row=sheet.getRow(i);
						shop=new Shop();
						shop.setMerchantStatus(merchantStatus);
						shop.setShopStatus(ShopStatus.normal);
						shop.setMerchantId(merchantId);
							//获得第i行第j列的String类型对象
							//Cell cell=row.getCell(1);
						try {
							//当客服电话为座机时
							shop.setServicetel(row.getCell(3).getStringCellValue());
						} catch (NullPointerException e) {
							//当客服电话为空的时候
							LogFactory.info(this,"第["+j+"]个sheet的第["+i+"]个门店客服电话为空");
						}catch (Exception e) {
							//当客服电话为手机号码
							shop.setServicetel(getStringFromDouble(row.getCell(3).getNumericCellValue()));
						}
							shop.setShopName(row.getCell(1).getStringCellValue());
							shop.setShopAddress(row.getCell(2).getStringCellValue());
							shop.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
							shop.setShopId(IdWorker.getId());
							LogFactory.info(this,"批量添加门店----获取到第["+j+"]个sheet第["+i+"]个门店信息+["+shop+"]开始尝试添加\n"+locker);
							Integer var=dao.getShopDao().insert(shop);
							if(var>0) {
								LogFactory.debug(this,"批量添加门店----第["+j+"]个sheet第["+i+"]个门店添加成功\n"+locker);
								LogFactory.info(this,"开始尝试添加添加门店操作记录\n"+locker);
								try {
									OperateRecord record=new OperateRecord();
									record=new OperateRecord();
						            record.setRecordId(IdWorker.getId());
						            record.setOperaterId(operaterId);
						            record.setTargetType(OperateTargetType.shop);
						            record.setDescription("单个添加门店");
						            record.setOperateTime(TimeUtil.getDateTime());
						            record.setTargetInfo(shop.toJson());
						            record.setOperateResult("成功");
									boolean flag=record.insert();
						            LogFactory.info(this,"添加门店操作记录["+record+"]结果["+flag+"],\n"+locker); 
								} catch (Exception e) {
									// TODO: handle exception
									LogFactory.info(this, "添加门店操作记录时发生异常\n"+locker);
								}
								
							}else {
								list.add("批量添加门店----第["+j+"]个sheet第["+i+"]个门店添加失败");
								//num[i-1]=i;
								LogFactory.debug(this,"批量添加门店----第["+j+"]个sheet第["+i+"]个门店添加失败\n"+locker);
							}
					}
					//workbook.close();
				}
					
			}
			
		} catch (Exception e) {
			LogFactory.error(this,"批量添加门店的时候发生异常\n"+locker);
			list.add("批量添加门店的时候发生异常");
			// TODO: handle exception
		}
		// TODO Auto-generated method stub
		return list;
	}
	
	@Override
	public Boolean freezeShop(Long shopId, Long operaterId) {
		String locker="shopId["+shopId+"]\n"+
	
				",operaterId["+operaterId+"]\n";
		LogFactory.info(this,"尝试冻结门店\n"+locker);
		Boolean result=false;
		Shop shop=null;
		OperateRecord record=null;
		try {
			if (shopId!=null) {
				shop=new Shop();
				shop.setShopId(shopId);
				shop.setShopStatus(ShopStatus.freeze);
				Integer var=dao.getShopDao().updateById(shop);
				if(var>0) {
					LogFactory.debug(this,"冻结门店成功\n"+locker);
					result=true;
					LogFactory.info(this,"尝试添加冻结门店操作记录\n"+locker);
					record=new OperateRecord();
		            record.setRecordId(IdWorker.getId());
		            record.setOperaterId(operaterId);
		            record.setTargetType(OperateTargetType.shop);
		            record.setDescription("冻结门店");
		            record.setOperateTime(TimeUtil.getDateTime());
		            record.setTargetInfo(shop.toJson());
		            record.setOperateResult("成功");
					boolean flag=record.insert();
		            LogFactory.info(this,"添加冻结门店操作记录["+record+"]结果["+flag+"],\n"+locker); 
				}else {
					LogFactory.debug(this,"冻结门店失败,\n"+locker);
					result=false;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this,"冻结门店时发生异常,\n"+locker);
			
		}
		// TODO Auto-generated method stub
		return result;
	}

	@Override
	public Boolean unfreezeShop(Long shopId, Long operaterId) {
		String locker="shopId["+shopId+"]\n"+
				",operaterId["+operaterId+"]\n";
		LogFactory.info(this,"尝试开启门店\n"+locker);
		Boolean result=false;
		Shop shop=null;
		OperateRecord record=null;
		try {
			if (shopId!=null) {
				shop=new Shop();
				shop.setShopId(shopId);
				shop.setShopStatus(ShopStatus.normal);
				Integer var=dao.getShopDao().updateById(shop);
				if(var>0) {
					LogFactory.debug(this,"开启门店成功\n"+locker);
					result=true;
					LogFactory.info(this,"尝试添加开启门店操作记录\n"+locker);
					record=new OperateRecord();
		            record.setRecordId(IdWorker.getId());
		            record.setOperaterId(operaterId);
		            record.setTargetType(OperateTargetType.shop);
		            record.setDescription("开启门店");
		            record.setOperateTime(TimeUtil.getDateTime());
		            record.setTargetInfo(shop.toJson());
		            record.setOperateResult("成功");
					boolean flag=record.insert();
		            LogFactory.info(this,"添加开启门店操作记录["+record+"]结果["+flag+"],\n"+locker); 
				}else {
					LogFactory.debug(this,"开启门店失败,\n"+locker);
					result=false;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this,"开启门店时发生异常,\n"+locker);
			
		}
		// TODO Auto-generated method stub
		return result;
	}

	@Override
	public List<Shop> queryShop(String shopName, ShopStatus status, String shopAddress,
			Page<Shop> pageObj, Long merchantId, Long operaterId) {
		String locker="shopName["+shopName+"]\n"+
				",status["+status+"]\n"+
				",shopAddress["+shopAddress+"]\n"+
				",merchantId["+merchantId+"]\n"+
				",operaterId["+operaterId+"]\n";
		LogFactory.info(this,"尝试获取门店信息\n"+locker);
		List<Shop> list=null;
		try {
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
			LogFactory.info(this, "条件装饰结果["+wrapper+"]\n"+locker);
			list=dao.getShopDao().selectPage(pageObj, wrapper);
			LogFactory.debug(this,"查询到的结果["+list+"]\n"+locker);
			LogFactory.info(this,"查询到的总记录数["+list.size()+"]\n"+locker);
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this,"查询门店时发生异常/n"+locker,e);
		}
		// TODO Auto-generated method stub
		return list;
	}

	@Override
	public List<Shop> queryShop(String shopName, ShopStatus status, String shopAddress,
			Long merchantId, Long operaterId) {
		String locker="shopName["+shopName+"]\n"+
				",status["+status+"]\n"+
				",shopAddress["+shopAddress+"]\n"+
				",merchantId["+merchantId+"]\n"+
				",operaterId["+operaterId+"]\n";
		LogFactory.info(this,"尝试获取门店信息\n"+locker);
		List<Shop> list=null;
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
			LogFactory.info(this, "条件装饰结果["+wrapper+"]\n"+locker);
			list=dao.getShopDao().selectList(wrapper);
			LogFactory.debug(this,"查询到的结果["+list+"]\n"+locker);
			LogFactory.info(this,"查询到的总记录数["+list.size()+"]\n"+locker);
		} catch (Exception e) {
			// TODO: handle exception
			LogFactory.error(this,"查询门店时发生异常\n"+locker,e);
		}
		// TODO Auto-generated method stub
		return list;
	}

	@Override
	public Boolean modifyShop(String shopName, String shopAddress,String minuteShopAddress,String serviceTel, Long shopId, Long operaterId) {
		String locker="shopName["+shopName+"]\n"+
				",serviceTel["+serviceTel+"]\n"+
				",shopAddress["+shopAddress+"]\n"+
				",minuteShopAddress["+minuteShopAddress+"]\n"+
				",shopId["+shopId+"]\n"+
				",operaterId["+operaterId+"]\n";
		LogFactory.info(this,"尝试开始修改门店信息\n"+locker);
		Boolean result=false;
		OperateRecord record=null;
		try {
			if (shopId!=null&&shopName!=null&&!shopName.isEmpty()&&shopAddress!=null&&!shopAddress.isEmpty()) {
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
				Integer var=dao.getShopDao().updateById(shop);
				if (var>0) {
					result=true;
					LogFactory.debug(this,"修改门店信息成功\n"+locker);
					LogFactory.info(this,"开始尝试添加修改门店信息操作记录\n"+locker);
					record=new OperateRecord();
		            record.setRecordId(IdWorker.getId());
		            record.setOperaterId(operaterId);
		            record.setTargetType(OperateTargetType.shop);
		            record.setDescription("修改门店信息");
		            record.setOperateTime(TimeUtil.getDateTime());
		            record.setTargetInfo(shop.toJson());
		            record.setOperateResult("成功");
					boolean flag=record.insert();
		            LogFactory.info(this,"修改门店操作记录["+record+"]结果["+flag+"],\n"+locker); 
				}else {
					result=false;
					LogFactory.debug(this,"修改门店信息失败\n"+locker);
				}
			}else {
				result=false;
				LogFactory.warn(this,"shopId,shopName,shopAddress不能为空\n"+locker);
			}
		} catch (Exception e) {
			LogFactory.error(this,"修改门店信息发生异常\n"+locker);
			// TODO: handle exception
		}
		// TODO Auto-generated method stub
		return result;
	}
	//门店编号、门店名称、经营地址、客服电话、门店状态
	@Override
	public File queryShopsExport(String shopName, String shopAddress, ShopStatus status, Long merchantId,
			Long operaterId) {
		String locker="shopName["+shopName+"]\n"+
				",status["+status+"]\n"+
				",shopAddress["+shopAddress+"]\n"+
				",merchantId["+merchantId+"]\n"+
				",operaterId["+operaterId+"]\n";
			LogFactory.info(this,"尝试导出门店信息\n"+locker);
			List<Shop> list=null;
			File file=null;
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
			LogFactory.info(this, "条件装饰结果["+wrapper+"]\n"+locker);
			list=dao.getShopDao().selectList(wrapper);
			LogFactory.debug(this,"查询到的结果["+list+"]\n"+locker);
			LogFactory.info(this,"查询到的总记录数["+list.size()+"]\n"+locker);
			Map<String,String> map=new HashMap<>();
			map.put("shopId","门店编号");
			map.put("shopName","门店名称");
			map.put("shopAddress","经营地址");
			map.put("minuteShopAddress","详细地址");
			map.put("servicetel","客服电话");
			map.put("shopStatus","门店状态");
			file=ExcelUtil.exportExcelFile(list, map);
			} catch (Exception e) {
				// TODO: handle exception
				LogFactory.error(this,"导出文件时发生异常\n"+locker);
			}
		// TODO Auto-generated method stub
		return file;
	}
	public String getStringFromDouble(double x) {
		 Double dou_obj = new Double(x);
	        NumberFormat nf = NumberFormat.getInstance();
	        nf.setGroupingUsed(false);
	        String dou_str = nf.format(dou_obj);
	        return dou_str;
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
