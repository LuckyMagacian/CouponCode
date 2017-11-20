package com.lanxi.couponcode.impl.newservice;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.lanxi.couponcode.impl.entity.Merchant;
import com.lanxi.couponcode.impl.entity.OperateRecord;
import com.lanxi.couponcode.impl.entity.Path;
import com.lanxi.couponcode.impl.util.FileUpLoadUtil;
import com.lanxi.couponcode.impl.util.ImageUtil;
import com.lanxi.couponcode.spi.consts.enums.MerchantStatus;
import com.lanxi.util.entity.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
/**
 * 商户操作实现类
 * @author wuxiaobo
 *
 */
@Service("merchantService")
public class MerchantServiceImpl implements MerchantService {
	
	private TransactionDefinition txDefinition;
	@Resource
	private PlatformTransactionManager txManager;
	@Resource
	private DaoService dao;
	@Resource
	private ShopService shopService;
	@Override
	public Boolean addMerchant(Merchant merchant) {
		LogFactory.info(this, "尝试添加商户\n");
		boolean result=false;
		try {
			merchant.setMerchantStatus(MerchantStatus.normal);
			merchant.setMerchantId(IdWorker.getId());
			//System.out.println(merchant.getMerchantId());
			merchant.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
			int var=dao.getMerchantDao().insert(merchant);
			if(var<0) {
				LogFactory.info(this,"添加商户失败\n");
				result=false;
			}else if (var>0) {
				LogFactory.info(this,"添加商户成功\n");
				result=true;
			}
		} catch (Exception e) {
			LogFactory.error(this,"添加商户时发生异常,\n",e);
			return result;
			//throw new RuntimeException("添加商户时发生异常",e);
		}
		return result;
	}
	@Override
	public Boolean updateMerchantById(Merchant merchant) {
		LogFactory.info(this, "尝试修改商户,\n");
		boolean result=false;
		try {
			int var=dao.getMerchantDao().updateById(merchant);
			if(var<0) {
				LogFactory.info(this,"修改商户失败\n");
				result=false;
			}else if (var>0) {
				LogFactory.info(this,"修改商户成功\n");
				result=true;
			}
		} catch (Exception e) {
			LogFactory.error(this,"修改商户时发生异常,\n",e);
			//throw new RuntimeException("修改商户时发生异常",e);
			return result;
		}
		return result;
	}
	
	@Override
	public List<Merchant> getAllMerchant(Page<Merchant> pageObj) {
		
		LogFactory.info(this, "尝试获取商户信息,\n");
		List<Merchant> list=null;
		try {
			if(pageObj!=null) {
			//执行查询
			list=dao.getMerchantDao().selectPage(pageObj,null);
			LogFactory.debug(this,"查询结果["+list+"]");
			LogFactory.info(this,"查询到的记录数["+list.size()+"]");
			}
			return list;
		} catch (Exception e) {
			LogFactory.error(this,"查询商户时发生异常",e);
			return list;
		}
		
		
	}
	@Override
	public List<Merchant> getMerchantByCondition(Page<Merchant> pageObj,EntityWrapper<Merchant> wrapper) {
		
		LogFactory.info(this, "尝试按条件获取商户信息\n,");
		List<Merchant> list=null;
		try {
	       if(pageObj!=null) {
		            list=dao.getMerchantDao().selectPage(pageObj, wrapper);
		            LogFactory.info(this, "条件装饰结果["+wrapper+"]\n");
		            LogFactory.debug(this,"查询结果["+list+"]\n");
					LogFactory.info(this,"查询到的记录数["+list.size()+"]\n");
	       }
	        return list;
		} catch (Exception e) {
			LogFactory.error(this,"查询商户时发生异常",e);
			return list;
		}
		
	}
	@Override
	public Merchant queryMerchantParticularsById(Long merchantId) {
		String locker="merchantId["+merchantId+"]\n";
		LogFactory.info(this,"尝试获取商户详情"+locker);
		Merchant merchant=null;
		try {
			if(merchantId!=null) {
				merchant=dao.getMerchantDao().selectById(merchantId);
				LogFactory.debug(this,"查询结果["+merchant+"]"+locker);
			}
			return merchant;
		} catch (Exception e) {
			LogFactory.error(this, "查询商户详情是发生异常"+locker,e);
			return merchant;
		}
		
	}
	
//	@Override
//	public Boolean changeMerchanStatus(Long merchantId, Long operaterId, String operaterInfo, String operaterPhone,
//			String merchantStatus) {
//		String locker="merchantId["+merchantId+"]\n"+
//                ",operaterId["+operaterId+"]\n"+
//                ",operaterPhone["+operaterPhone+"]\n"+
//                ",operaterInfo["+operaterInfo+"]\n"+
//                ",merchatStatus["+merchantStatus+"]\n";
//		LogFactory.info(this, "尝试修改商户状态,\n"+locker);
//		OperateRecord record=null;
//		Merchant merchant=null;
//		boolean result=false;
//		try {
//			merchant=new Merchant();
//			merchant.setMerchantId(merchantId);
//			record=new OperateRecord();
//            record.setRecordId(IdWorker.getId());
//            record.setOperaterId(operaterId);
//            record.setPhone(operaterPhone);
//            record.setOperaterInfo(operaterInfo);
//            record.setTargetType(OperateTargetType.merchant);
//            record.setDescription("修改商户状态");
//            record.setOperateTime(TimeUtil.getDateTime());
//            record.setTargetInfo(merchant.toJson());
//			//先判断要把商户状态修改为什么状态
//			if(merchantStatus.equals("normal")) {
//				merchant.setMerchantStatus(MerchantStatus.normal);
//				Integer var=dao.getMerchantDao().updateById(merchant);
//				if(var<0) {
//					LogFactory.info(this,"修改商户状态失败\n"+locker);
//					record.setOperateResult("失败");
//					result=false;
//					
//				}else if (var>0) {
//					LogFactory.info(this,"修改商户状态成功\n"+locker);
//					record.setOperateResult("成功");
//					result=true;
//					boolean flag=record.insert();
//		             LogFactory.info(this,"修改商户状态操作记录["+record+"]结果["+flag+"],\n"+locker); 
//				}
//			}
//			if(merchantStatus.equals("freeze")) {
//				merchant.setMerchantStatus(MerchantStatus.freeze);
//				Boolean change=changeStatus(merchant, operaterId, operaterInfo, operaterPhone);
//				if(change) {
//					LogFactory.info(this,"修改商户状态成功\n"+locker);
//					result=true;
//					record.setOperateResult("成功");
//					boolean flag=record.insert();
//		             LogFactory.info(this,"修改商户状态操作记录["+record+"]结果["+flag+"],\n"+locker); 
//				}
//			}
//			
//			
//		} catch (Exception e) {
//			
//			LogFactory.error(this,"尝试根据商户id修改商户状态时发生异常"+locker,e);
//			return result;
//		}
//		
//		return result;
//	}
	
//	public Boolean changeStatus(Merchant merchant,Long operaterId,String operaterInfo, String operaterPhone) {
//		
//		TransactionStatus txStatus = txManager.getTransaction(txDefinition);
//		Boolean result=false;
//		try {
//			if(merchant!=null) {
//				LogFactory.info(this,"尝试同时冻结门店和商户");
//				Boolean freezeResult=shopService.freezeAllShop(merchant.getMerchantId(),operaterId , operaterInfo, operaterPhone);
//				Integer var=dao.getMerchantDao().updateById(merchant);
//				if(var>0&&freezeResult) {
//					LogFactory.info(this,"同时冻结门店和商户成功");
//					result=true;	
//				}
//			}
//			
//			txManager.commit(txStatus);
//		} catch (Exception e) {
//			
//			LogFactory.error(this,"同时冻结门店和商户失败",e);
//			txManager.rollback(txStatus);
//			throw new RuntimeException("同时冻结门店和商户失败",e);
//			
//		}
//		return result;
//	}
	@Override
	public Boolean organizingInstitutionBarCodePicUpLoad(Merchant merchant, File file) {
		LogFactory.info(this, "尝试添加商户组织机构代码证,\n");
		boolean result=false;
		try {
			if(file!=null) {
				 //获取文件类型，即后缀名
                String str = file.getName();
                String suffix = str.substring(str.lastIndexOf("."));
                if(!ImageUtil.isImage(suffix)) {
                	LogFactory.error(this,"上传的不是图片文件");
                	result=false;
                	return result;
                }
                //为避免文件重复用日期+商户id+证件类型做文件名
                String time=LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                String path=Path.organizingInstitutionBarCodePicPath+time+merchant.getMerchantId()+"organizingInstitutionBarCodePic"+suffix;
                LogFactory.info(this, "尝试保存商户组织机构代码证");
                FileUpLoadUtil.fileUpLoad(file, path);
                merchant.setOrganizingInstitutionBarCodePic(path);
                Integer var=dao.getMerchantDao().updateById(merchant);
                if(var<0) {
					LogFactory.info(this,"保存商户组织机构代码证失败\n");
					result=false;
				}else if (var>0) {
					LogFactory.info(this,"保存商户组织机构代码证成功\n");
					result=true;
				}
			}
		} catch (Exception e) {
			LogFactory.error(this,"保存商户组织机构代码失败",e);
			return result;
		}
		
		return result;
	}
	@Override
	public Boolean businessLicensePicUpLoad(Merchant merchant, File file) {
		LogFactory.info(this, "尝试添加商户工商营业执照,\n");
		boolean result=false;
		try {
			if(file!=null) {
				 //获取文件类型，即后缀名
				 String str = file.getName();
                String suffix = str.substring(str.lastIndexOf("."));
                if(!ImageUtil.isImage(suffix)) {
                	LogFactory.error(this,"上传的不是图片文件");
                	result=false;
                	return result;
                }
                //为避免文件重复用日期+商户id+证件类型做文件名
                String time=LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                String path=Path.businessLicensePicPath+time+merchant.getMerchantId()+"businessLicensePic"+suffix;
                LogFactory.info(this, "尝试保存商户工商营业执照");
                FileUpLoadUtil.fileUpLoad(file, path);
                merchant.setBusinessLicensePic(path);
                Integer var=dao.getMerchantDao().updateById(merchant);
                if(var<0) {
					LogFactory.info(this,"保存商户工商营业执照失败\n");
					result=false;
				}else if (var>0) {
					LogFactory.info(this,"保存商户工商营业执照成功\n");
					result=true;
				}
			}
		} catch (Exception e) {
			LogFactory.error(this,"保存商户工商营业执照失败",e);
			return result;
		}
		return result;
		
	}
	@Override
	public Boolean otherPicUpLoad(Merchant merchant, File file) {
		LogFactory.info(this, "尝试添加商户其他证明资料,\n");
		boolean result=false;
		try {
			
			if(file!=null) {
				 //获取文件类型，即后缀名
                String str = file.getName();
                String suffix = str.substring(str.lastIndexOf("."));
                if(!ImageUtil.isImage(suffix)) {
                	LogFactory.error(this,"上传的不是图片文件");
                	result=false;
                	return result;
                }
                //为避免文件重复用日期+商户id+证件类型做文件名
                String time=LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                String path=Path.otherPicPath+time+merchant.getMerchantId()+"otherPic"+suffix;
                LogFactory.info(this, "尝试保存商户其他证明资料");
                FileUpLoadUtil.fileUpLoad(file, path);
                merchant.setOtherPic(path);
                Integer var=dao.getMerchantDao().updateById(merchant);
                if(var<0) {
					LogFactory.info(this,"保存商户其他证明资料\n");
					result=false;
				}else if (var>0) {
					LogFactory.info(this,"保存商户其他证明资料\n");
					result=true;
				}
			}
		} catch (Exception e) {
			LogFactory.error(this,"保存商户其他证明资料失败",e);
			return result;
		}
		return result;
	}
	@Override
	public Boolean fillInInformation(Merchant merchant, File organizingInstitutionBarCodePicFile,
			File businessLicensePicFile, File otherPicFile) {
		//TransactionStatus txStatus = txManager.getTransaction(txDefinition);
		LogFactory.info(this, "尝试添加商户详细信息,\n");
		boolean result=false;
		try {
            if(merchant!=null) {
            	LogFactory.info(this,"尝试添加商户资料");
            	int var=dao.getMerchantDao().updateById(merchant);
            	if(var<0) {
            		LogFactory.debug(this,"添加商户资料失败\n");
					result=false;
					return result;
            	}else if (var>0) {
            		LogFactory.info(this,"添加商户资料成功\n");
            		result=true;
				}
            }
            if(organizingInstitutionBarCodePicFile!=null) {
            	Boolean b=organizingInstitutionBarCodePicUpLoad(merchant, organizingInstitutionBarCodePicFile);
            	if(b) {
            		result=b;
            	}else {
					result=false;
//					String s=null;
//					s.length();
//					txManager.commit(txStatus);
					return result;
				}
            }
            if(businessLicensePicFile!=null) {
        	Boolean b=businessLicensePicUpLoad(merchant, businessLicensePicFile);
        		if(b) {
        		result=b;
        		}else {
				result=false;
//				String s=null;
//				s.length();
//				txManager.commit(txStatus);
				return result;
        		}
            }
            if(otherPicFile!=null) {
            	Boolean b=otherPicUpLoad(merchant, otherPicFile);
            	if(b) {
            		result=b;
            	}else {
					result=false;
//					String s=null;
//					s.length();
//					txManager.commit(txStatus);
					return result;
				}
            }

		} catch (Exception e) {
			
			LogFactory.error(this,"添加商户详细信息失败",e);
			result=false;
			//txManager.rollback(txStatus);
		}
		return result;
	}
	@Override
	public String queryMerchantStatusByid(Long merchantId, Long operaterId) {
		String locker="merchantId["+merchantId+"]"+
				"operaterId["+operaterId+"]\n";
		LogFactory.info(this, "尝试获取商户状态,\n"+locker);
		String merchantStatus=null;
		try {
			if(merchantId!=null) {
				Merchant merchant=dao.getMerchantDao().selectById(merchantId);
				merchantStatus=merchant.getMerchantStatus();
				LogFactory.info(this,"获取到的商户状态["+merchantStatus+"]"+locker);
			}
		} catch (Exception e) {
			LogFactory.error(this,"获取商户状态时发生异常",e);
			
		}
		
		return merchantStatus;
	}
	@Override
	public Boolean freezeMerchant(Merchant merchant) {
		Boolean result=false;
		try {
			Integer var=dao.getMerchantDao().updateById(merchant);
			if (var>0) {
			result=true;
			LogFactory.debug(this,"冻结商户成功");
			}else {
				result=false;
				LogFactory.debug(this,"冻结商户失败");
			}
		} catch (Exception e) {
			// TODO: handle exception
			result=false;
			LogFactory.debug(this,"冻结商户时发生异常");
		}
		
		return result;
	}
	@Override
	public Boolean unFreezeMerchant(Merchant merchant) {
		Boolean result=false;
		try {
			Integer var=dao.getMerchantDao().updateById(merchant);
			if (var>0) {
			result=true;
			LogFactory.debug(this,"开启商户成功");
			}else {
				result=false;
				LogFactory.debug(this,"开启商户失败");
			}
		} catch (Exception e) {
			// TODO: handle exception
			result=false;
			LogFactory.debug(this,"开启商户时发生异常");
		}
		
		return result;
	}
	
}
