package com.lanxi.couponcode.impl.newservice;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.lanxi.couponcode.spi.assist.TimeAssist;
import com.lanxi.couponcode.spi.config.Path;
import com.lanxi.couponcode.impl.entity.Shop;
import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.couponcode.spi.consts.enums.MerchantStatus;
import com.lanxi.couponcode.spi.consts.enums.ShopStatus;
import com.lanxi.util.entity.LogFactory;
import com.lanxi.util.utils.ExcelUtil;
import com.lanxi.util.utils.LoggerUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 门店操作实现类
 * 
 * @author wuxiaobo
 *
 */
@Service("shopService")
@EasyLog (LoggerUtil.LogLevel.INFO)
public class ShopServiceImpl implements ShopService {
	@Resource
	private DaoService dao;
	@Resource
	private MerchantService merchantService;

	@Override
	public Boolean freezeAllShop(Long merchantId, Long operaterId, String operaterInfo, String operaterPhone) {
		
		LogFactory.info(this, "尝试冻结" + merchantId + "所有门店,\n" );

		boolean result = false;
		// 因为要冻结所有门店shop直接设置为freeze
		Shop shop = null;
		try {
			shop = new Shop();
			shop.setShopStatus(ShopStatus.freeze);
			EntityWrapper<Shop> wrapper = new EntityWrapper<Shop>();
			wrapper.eq("merchant_id", merchantId);
			Integer var = dao.getShopDao().update(shop, wrapper);
			if (var < 0) {
				LogFactory.debug(this, "冻结所有门店失败" );
				result = false;
			} else if (var > 0) {
				LogFactory.info(this, "冻结所有门店成功" );
				result = true;
			}
		} catch (Exception e) {

			LogFactory.error(this, "冻结所有门店时发生异常", e);
		}

		return result;
	}

	@Override
	public Boolean addShop(Shop shop) {
		
		LogFactory.info(this, "尝试单个添加门店,\n" );
		boolean result = false;
		try {
			if (shop != null) {
				Integer var = dao.getShopDao().insert(shop);
				if (var < 0) {
					LogFactory.debug(this, "添加门店失败\n");
					result = false;
				} else if (var > 0) {
					LogFactory.debug(this, "添加门店成功\n" );
					result = true;
				}
			} else {
				LogFactory.info(this, "没有获取到要添加的信息\n");
			}
		} catch (Exception e) {

			LogFactory.error(this, "添加门店的时候发生异常,\n", e);

		}

		return result;
	}

	@Override
	public List<String> importShops(File file, Long merchantId, Long operaterId, String merchantStatus) {
		
		Shop shop = null;
		List<String> list = new ArrayList<String>();
		try {
			InputStream inputStream = new FileInputStream(file);
			Workbook workbook = null;
			try {
				workbook = new HSSFWorkbook(inputStream);
			} catch (Exception e) {
				// 解决read error异常
				inputStream = new FileInputStream(file);
				workbook = new XSSFWorkbook(inputStream);
			}
			// 获取sheet的数量
			int num = workbook.getNumberOfSheets();
			for (int j = 0; j < num; j++) {
				Sheet sheet = workbook.getSheetAt(j);
				// 获取数据的总行数
				int total = sheet.getLastRowNum();
				// int[] num=new int[total-1];
				for (int i = 1; i <= total; i++) {
					// 获得第i行对象
					Row row = sheet.getRow(i);
					shop = new Shop();
					if (merchantStatus != null) {
						shop.setMerchantStatus(merchantStatus);
					}
					shop.setShopStatus(ShopStatus.normal);
					shop.setMerchantId(merchantId);
					// 获得第i行第j列的String类型对象
					// Cell cell=row.getCell(1);
					try {
						// 当客服电话为座机时
						shop.setServicetel(row.getCell(3).getStringCellValue());
					} catch (NullPointerException e) {
						// 当客服电话为空的时候
						LogFactory.info(this, "第[" + j + "]个sheet的第[" + i + "]个门店客服电话为空");
					} catch (Exception e) {
						// 当客服电话为手机号码
						shop.setServicetel(getStringFromDouble(row.getCell(3).getNumericCellValue()));
					}
					shop.setShopName(row.getCell(1).getStringCellValue());
					if (isRepeat(row.getCell(1).getStringCellValue(), merchantId)) {
						shop.setShopAddress(row.getCell(2).getStringCellValue());
						shop.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
						shop.setShopId(IdWorker.getId());
						LogFactory.info(this,
								"批量添加门店----获取到第[" + j + "]个sheet第[" + i + "]个门店信息+[" + shop + "]开始尝试添加\n" );
						Integer var = dao.getShopDao().insert(shop);
						if (var > 0) {
						} else {
							list.add("批量添加门店----第[" + j + "]个sheet第[" + i + "]个门店添加失败");
							// num[i-1]=i;
							LogFactory.debug(this, "批量添加门店----第[" + j + "]个sheet第[" + i + "]个门店添加失败\n" );
						}
					}else {
						list.add("批量添加门店----第[" + j + "]个sheet第[" + i + "]个门店添加失败");
						// num[i-1]=i;
						LogFactory.debug(this, "批量添加门店----第[" + j + "]个sheet第[" + i + "]个门店添加失败\n");
					}
					
					
				
				}
				// workbook.close();
			}
		} catch (Exception e) {
			LogFactory.error(this, "批量添加门店的时候发生异常\n");
			list.add("批量添加门店的时候发生异常");
		}
		return list;
	}

	@Override
	public Boolean freezeShop(Shop shop) {
		synchronized (shop) {
			
			LogFactory.info(this, "尝试冻结门店\n" );
			Boolean result = false;
			try {
				Integer var = dao.getShopDao().updateById(shop);
				if (var > 0) {
					LogFactory.debug(this, "冻结门店成功\n");
					result = true;
				} else {
					LogFactory.debug(this, "冻结门店失败,\n" );
					result = false;
				}
			} catch (Exception e) {
				LogFactory.error(this, "冻结门店时发生异常,\n");
			}
			return result;
		}
	}

	@Override
	public Boolean unfreezeShop(Shop shop) {
		synchronized (shop) {
			
			LogFactory.info(this, "尝试开启门店\n" );
			Boolean result = false;
			try {
				Integer var = dao.getShopDao().updateById(shop);
				if (var > 0) {
					LogFactory.debug(this, "开启门店成功\n" );
					result = true;
				} else {
					LogFactory.debug(this, "开启门店失败,\n");
					result = false;
				}
			} catch (Exception e) {
				LogFactory.error(this, "开启门店时发生异常,\n");
			}
			return result;
		}
	}

	@Override
	public List<Shop> queryShop(EntityWrapper<Shop> wrapper, Page<Shop> pageObj) {
		LogFactory.info(this, "尝试获取门店信息\n");
		List<Shop> list = null;
		try {
			list = dao.getShopDao().selectPage(pageObj, wrapper);
			LogFactory.debug(this, "查询到的结果[" + list + "]\n");
			LogFactory.info(this, "查询到的总记录数[" + list.size() + "]\n");
		} catch (Exception e) {

			LogFactory.error(this, "查询门店时发生异常/n", e);
		}

		return list;
	}

	@Override
	public List<Shop> queryShop(EntityWrapper<Shop> wrapper) {

		LogFactory.info(this, "尝试获取门店信息\n");
		List<Shop> list = null;
		try {
			list = dao.getShopDao().selectList(wrapper);
			LogFactory.debug(this, "查询到的结果[" + list + "]\n");
			LogFactory.info(this, "查询到的总记录数[" + list.size() + "]\n");

		} catch (Exception e) {

			LogFactory.error(this, "查询门店时发生异常\n", e);
		}

		return list;
	}

	@Override
	public Boolean modifyShop(Shop shop) {
		synchronized (shop) {
			
			LogFactory.info(this, "尝试开始修改门店信息\n");
			Boolean result = false;
			try {
				Integer var = dao.getShopDao().updateById(shop);
				if (var > 0) {
					result = true;
					LogFactory.debug(this, "修改门店信息成功\n");
				} else {
					result = false;
					LogFactory.debug(this, "修改门店信息失败\n" );
				}
			} catch (Exception e) {
				LogFactory.error(this, "修改门店信息发生异常\n" ,e);

			}
			return result;
		}
	}

	@Override
	public File queryShopsExport(EntityWrapper<Shop> wrapper, Page<Shop> pageObj) {
		LogFactory.info(this, "尝试导出门店信息\n");
		List<Shop> list = null;
		
		try {
			if (pageObj!=null) {
				list = dao.getShopDao().selectPage(pageObj, wrapper);
			}else {
				list=dao.getShopDao().selectList(wrapper);
			}
			LogFactory.debug(this, "查询到的结果[" + list + "]\n");
			LogFactory.info(this, "查询到的总记录数[" + list.size() + "]\n");
			Map<String, String> map = new HashMap<>();
			map.put("shopId", "门店编号");
			map.put("shopName", "门店名称");
			map.put("shopAddress", "经营地址");
			map.put("minuteShopAddress", "详细地址");
			map.put("servicetel", "客服电话");
			map.put("shopStatus", "门店状态");
			File file=new File("导出门店"+TimeAssist.getNow()+".xls");
			OutputStream os=new FileOutputStream(file);
			ExcelUtil.exportExcelFile(list, map,os);
			return file;
		} catch (Exception e) {
			LogFactory.error(this, "导出文件时发生异常\n");
			return null;
		}

		
	}

	public String getStringFromDouble(double x) {
		Double dou_obj = new Double(x);
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		String dou_str = nf.format(dou_obj);
		return dou_str;
	}

	@Override
	public Shop queryShopInfo(Long shopId) {
		Shop shop = null;
		try {
			if (shopId != null) {
				shop = dao.getShopDao().selectById(shopId);
			}
		} catch (Exception e) {

			LogFactory.error(this, "查询门店详情的时候发生异常", e);
		}
		return shop;
	}

	@Override
	public File downloadExcelTemplate() {
		File file2 = null;
		InputStream is = null;
		OutputStream os = null;
		try {
			File file = new File(Path.excelTemplatePath);
			is = new FileInputStream(file);
			file2 = new File("file");
			os = new FileOutputStream(file2);
			byte temp[] = new byte[1024];
			int size = -1;
			while ((size = is.read(temp)) != -1) {
				os.write(temp, 0, size);
			}
			is.close();
			os.close();
			return file2;
		} catch (Exception e) {
			LogFactory.info(this, "下载Excel模板时发生异常", e);
			return file2;
		}
	}

	@Override
	public List<Shop> queryShops(Long merchantId,Page<Shop> pageObj) {
		try {
			EntityWrapper<Shop> wrapper=new EntityWrapper<>();
			if (merchantId!=null) {
				wrapper.eq("merchant_id", merchantId);
				wrapper.in("shop_status", ShopStatus.normal.getValue()+","+ShopStatus.freeze.getValue());
				if (pageObj!=null) {
					return dao.getShopDao().selectPage(pageObj, wrapper);
				}
				return dao.getShopDao().selectList(wrapper);
			}else
				return null;
		} catch (Exception e) {
			LogFactory.error(this,"查询门店时发生异常",e);
			return null;
		}
		
		
	}

	@Override
	public Boolean isRepeat(String shopName, Long merchantId) {
		try {
			EntityWrapper<Shop> wrapper=new EntityWrapper<Shop>();
			wrapper.eq("shop_name", shopName);
			wrapper.eq("merchant_id", merchantId);
			wrapper.in("shop_status", ShopStatus.normal.getValue()+","+ShopStatus.freeze.getValue());
			List<Shop>list=dao.getShopDao().selectList(wrapper);
			if (list==null||list.size()==0) {
				return true;
			}else {
				for (Shop shop : list) {
					if (shop.getShopStatus()==null)
						return true;
					else {
						if (ShopStatus.normal.equals(shop.getShopStatus())||
								ShopStatus.freeze.equals(shop.getShopStatus())) {
							return false;
						}
					}
				}
				return true;
			}
		} catch (Exception e) {
			LogFactory.error(this,"判断门店名称是否可用时发生异常",e);
			return false;
		}
	}

	@Override
	public Boolean isRepeat(Long shopId, String shopName,Long merchantId) {
		try {
			EntityWrapper<Shop> wrapper=new EntityWrapper<>();
			wrapper.eq("shop_name", shopName);
			wrapper.eq("merchant_id", merchantId);
			wrapper.in("shop_status", ShopStatus.normal.getValue()+","+ShopStatus.freeze.getValue());
			List<Shop>list=dao.getShopDao().selectList(wrapper);
			if (list==null||list.size()==0) {
				return true;
			}else {
				for (Shop shop : list) {
					if (shop.getShopStatus()==null) 
						return true;
					else {
						if (ShopStatus.normal.equals(shop.getShopStatus())||
								ShopStatus.freeze.equals(shop.getShopStatus())) {
							if (shop.getShopId().equals(shopId)) 
								return true;
							else
								return false;
						}
					}
				}
				return true;
			}
		} catch (Exception e) {
			LogFactory.error(this,"判断门店名称是否可用时发生异常",e);
			return false;
		}
	}

	@Override
	public List<Shop> adminQueryShop(EntityWrapper<Shop> wrapper, Page<Shop> pageObj) {
		try {
			return dao.getShopDao().selectPage(pageObj, wrapper);
		} catch (Exception e) {
			LogFactory.error(this,"查询门店时发生异常",e);
			return null;
		}
		
	}

	@Override
	public List<Shop> queryAllShop() {
		EntityWrapper<Shop> wrapper=new EntityWrapper<>();
		wrapper.in("shop_status", ShopStatus.normal.getValue()+","+ShopStatus.freeze.getValue());
		return dao.getShopDao().selectList(wrapper);
	}

	// @Override
	// public List<Shop> queryPossessShopByMerchantId(Integer page, Integer size,
	// Long merchantId, Long operaterId,
	// String operaterInfo, String operaterPhone) {
	// String locker="merchantId["+merchantId+"]\n"+
	// ",operaterId["+operaterId+"]\n"+
	// ",operaterPhone["+operaterPhone+"]\n"+
	// ",operaterInfo["+operaterInfo+"]\n";
	// LogFactory.info(this,"尝试根据商户id获取门店"+locker);
	// List<Shop> list=null;
	// try {
	// Page<Shop> pageObj=null;
	// EntityWrapper<Shop> wrapper=new EntityWrapper<Shop>();
	// if(page!=null) {
	// //配置分页信息
	// size=size==null? ConstConfig.DEFAULT_PAGE_SIZE:size;
	// pageObj=new Page<Shop>(page,size);
	// }
	// if(merchantId!=null) {
	// wrapper.eq("merchant_id", merchantId);
	// }
	// list=dao.getShopDao().selectPage(pageObj, wrapper);
	// LogFactory.info(this, "条件装饰结果["+wrapper+"]"+locker);
	// LogFactory.debug(this,"查询结果["+list+"]"+locker);
	// LogFactory.info(this,"查询到的记录数["+list.size()+"]"+locker);
	// return list;
	// } catch (Exception e) {
	//
	// LogFactory.error(this,"尝试根据商户id获取门店时发生异常",e);
	// return list;
	// }
	//
	// }
}
