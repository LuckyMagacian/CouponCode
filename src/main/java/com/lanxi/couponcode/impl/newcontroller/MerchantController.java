package com.lanxi.couponcode.impl.newcontroller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.impl.config.ConstConfig;
import com.lanxi.couponcode.impl.entity.Merchant;
import com.lanxi.couponcode.impl.newservice.AccountService;
import com.lanxi.couponcode.impl.newservice.MerchantService;
import com.lanxi.couponcode.impl.newservice.RedisEnhancedService;
import com.lanxi.couponcode.impl.newservice.RedisService;
import com.lanxi.couponcode.spi.consts.enums.AccountStatus;
import com.lanxi.couponcode.spi.consts.enums.MerchantStatus;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.util.entity.LogFactory;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import java.io.File;
import java.util.List;

/**
 * Created by yangyuanjian on 2017/11/13.
 */
@Controller
public class MerchantController implements com.lanxi.couponcode.spi.service.MerchantService {
	@Resource
	private AccountService accountService;
	@Resource
	private MerchantService merchantService;
	@Resource
	private RedisService redisService;
	@Resource
	private RedisEnhancedService redisEnhancedService;

	@Override
	public RetMessage<Boolean> addMerchant(String merchantName, String workAddress, String minuteWorkAddress,
			Long operaterId) {
		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result = false;
		// TODO 校验
		try {
			if (operaterId != null) {
				// 校验当前账号状态
				if (AccountStatus.normal.equals(accountService.queryAccountStatusById(operaterId))) {
					Merchant merchant = new Merchant();
					if (merchantName != null && !merchantName.isEmpty() && workAddress != null
							&& !workAddress.isEmpty()) {
						merchant.setMerchantName(merchantName);
						merchant.setWorkAddress(workAddress);
						if (minuteWorkAddress != null && !minuteWorkAddress.isEmpty()) {
							merchant.setMinuteWorkAddress(minuteWorkAddress);
						}
						result = merchantService.addMerchant(merchant);
						if (result) {
							retMessage.setRetCode(RetCodeEnum.success.getValue());
							retMessage.setRetMessage("添加商户成功");
							// TODO 添加操作记录
						}
						if (!result) {
							retMessage.setRetCode(RetCodeEnum.exception.getValue());
							retMessage.setRetMessage("添加商户失败");
						}
						retMessage.setDetail(result);
					} else {
						retMessage.setAll(RetCodeEnum.fail, "商户名称和经营地址不能为空", false);
					}
				} else {
					retMessage.setAll(RetCodeEnum.warning, "您的账户状态不正常", false);
				}
			} else {
				result = false;
				retMessage.setAll(RetCodeEnum.fail, "账户id为空", result);
			}

		} catch (Exception e) {

			LogFactory.error(this, "添加商户时发生异常", e);
			retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("添加商户时发生异常");
			retMessage.setDetail(result);
		}
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> modifyMerchant(String merchantName, String workAddress, String minuteWorkAddress,
			Long operaterId, Long merchantId) {
		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result = false;
		// TODO 校验
		try {
			if (operaterId != null) {
				if (merchantId != null) {
					// 先校验操作者账户状态
					if (AccountStatus.normal.equals(accountService.queryAccountStatusById(operaterId))) {
						if (MerchantStatus.normal
								.equals(merchantService.queryMerchantStatusByid(merchantId, operaterId))) {
							Merchant merchant = new Merchant();

							merchant.setMerchantId(merchantId);
							if (merchantName != null && !merchantName.isEmpty() && workAddress != null
									&& !workAddress.isEmpty()) {
								merchant.setMerchantName(merchantName);
								merchant.setWorkAddress(workAddress);
							}
							if (minuteWorkAddress != null && !minuteWorkAddress.isEmpty()) {
								merchant.setMinuteWorkAddress(minuteWorkAddress);
							}
							result = merchantService.updateMerchantById(merchant);
							if (result) {
								retMessage.setRetCode(RetCodeEnum.success.getValue());
								retMessage.setRetMessage("修改商户成功");
								if (merchantName!=null) {
									accountService.modifyAccountMerchantName(merchantId, merchantName);
								}
								// TODO 添加操作记录
							}
							if (!result) {
								retMessage.setRetCode(RetCodeEnum.exception.getValue());
								retMessage.setRetMessage("修改商户失败");
							}
							retMessage.setDetail(result);
						} else {
							retMessage.setAll(RetCodeEnum.warning, "当前商户状态不正常", false);
						}
					} else {
						retMessage.setAll(RetCodeEnum.warning, "您的账户状态不正常", false);
					}
				} else {
					result = false;
					retMessage.setAll(RetCodeEnum.fail, "商户id为空", result);
				}
			} else {
				result = false;
				retMessage.setAll(RetCodeEnum.fail, "账户id为空", result);
			}

		} catch (Exception e) {
			LogFactory.error(this, "修改商户时发生异常", e);
			retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("修改商户时发生异常");
			retMessage.setDetail(result);
		}
		return retMessage;
	}

	@Override
	public RetMessage<String> queryMerchants(String merchantName, MerchantStatus merchantStatus, String timeStart,
			String timeStop, Integer pageNum, Integer pageSize, Long operaterId) {
		RetMessage<String> retMessage = new RetMessage<String>();
		List<Merchant> merchants = null;
		// TODO 校验权限
		try {
			if (operaterId != null) {
				// 先校验操作者账户状态
				if (AccountStatus.normal.equals(accountService.queryAccountStatusById(operaterId))) {
					EntityWrapper<Merchant> wrapper = new EntityWrapper<Merchant>();
					if (pageNum != null) {
						pageSize = pageSize == null ? ConstConfig.DEFAULT_PAGE_SIZE : pageSize;
					}
					if (timeStart != null && !timeStart.isEmpty()) {
						while (timeStart.length() < 14)
							timeStart += "0";
						wrapper.ge("create_time", timeStart);
					}
					if (timeStop != null && !timeStop.isEmpty()) {
						while (timeStop.length() < 14)
							timeStop += "9";
						wrapper.le("create_time", timeStop);
					}
					if (merchantStatus != null) {
						wrapper.eq("merchant_status", merchantStatus);
					}
					if (merchantName != null && !merchantName.isEmpty()) {
						wrapper.eq("merchant_name", merchantName);
					}
					Page<Merchant> pageObj = new Page<Merchant>(pageNum, pageSize);
					merchants = merchantService.getMerchantByCondition(pageObj, wrapper);
					String result = JSON.toJSONString(merchants);
					retMessage.setDetail(result);
					if (merchants != null && merchants.size() > 0) {
						retMessage.setRetCode(RetCodeEnum.success.getValue());
						retMessage.setRetMessage("查询完毕");
					} else {
						retMessage.setRetCode(RetCodeEnum.exception.getValue());
						retMessage.setRetMessage("没有查询到任何数据");
					}
				} else {
					retMessage.setAll(RetCodeEnum.warning, "您的账户状态不正常", null);
				}
			} else {
				retMessage.setAll(RetCodeEnum.fail, "账户id为空", null);
			}

		} catch (Exception e) {
			LogFactory.error(this, "查询数据时出现异常", e);
			retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("查询数据时发生异常");
			retMessage.setDetail(null);

		}
		return retMessage;
	}

	@Override
	public RetMessage<File> queryMerchantsExport(String merchantName, MerchantStatus merchantStatus, String timeStart,
			String timeStop, Long operaterId) {
		RetMessage<File> retMessage = new RetMessage<File>();
		// TODO 校验
		try {
			if (operaterId != null) {
				// 先校验操作者账户状态
				if (AccountStatus.normal.equals(accountService.queryAccountStatusById(operaterId))) {

				} else {
					retMessage.setAll(RetCodeEnum.warning, "您的账户状态不正常", null);
				}
			} else {

				retMessage.setAll(RetCodeEnum.fail, "账户id为空", null);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> enableMerchant(Long merchantId, Long operaterId) {
		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result = false;
		// TODO 权限校验
		try {
			// 先校验操作者账户状态
			if (AccountStatus.normal.equals(accountService.queryAccountStatusById(operaterId))) {
				if (merchantId != null) {
					Merchant merchant = new Merchant();
					merchant.setMerchantId(merchantId);
					merchant.setMerchantStatus(MerchantStatus.normal);
					result = merchantService.unFreezeMerchant(merchant);
						
					if (result) {
						retMessage.setRetCode(RetCodeEnum.success.getValue());
						retMessage.setRetMessage("开启商户成功");
						// TODO 添加操作记录
					} else {
						retMessage.setRetCode(RetCodeEnum.exception.getValue());
						retMessage.setRetMessage("开启商户失败");
					}
					retMessage.setDetail(result);
				} else {
					result = false;
					retMessage.setAll(RetCodeEnum.fail, "商户id不能为空", result);
				}
			} else {
				retMessage.setAll(RetCodeEnum.warning, "您的账户状态不正常", false);
			}

		} catch (Exception e) {

			LogFactory.error(this, "开启商户时发生异常");
			retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("开启商户时发生异常");
			retMessage.setDetail(result);

		}
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> disableMerchant(Long merchantId, Long operaterId) {
		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result = false;
		// TODO 权限校验
		try {
			if (operaterId != null) {
				// 先校验操作者账户状态
				if (AccountStatus.normal.equals(accountService.queryAccountStatusById(operaterId))) {
					if (merchantId != null) {
						Merchant merchant = new Merchant();
						merchant.setMerchantId(merchantId);
						merchant.setMerchantStatus(MerchantStatus.freeze);
						result = merchantService.freezeMerchant(merchant);

						if (result) {
							retMessage.setRetCode(RetCodeEnum.success.getValue());
							retMessage.setRetMessage("冻结商户成功");
							
							// TODO 添加操作记录
						} else {
							retMessage.setRetCode(RetCodeEnum.exception.getValue());
							retMessage.setRetMessage("冻结商户失败");
						}
						retMessage.setDetail(result);
					} else {
						result = false;
						retMessage.setRetCode(RetCodeEnum.fail.getValue());
						retMessage.setRetMessage("商户id不能为空");
						retMessage.setDetail(result);
					}
				} else {
					retMessage.setAll(RetCodeEnum.warning, "您的账户状态不正常", false);
				}
			} else {
				result = false;
				retMessage.setAll(RetCodeEnum.fail, "账户id为空", result);
			}

		} catch (Exception e) {
			LogFactory.error(this, "冻结商户时发生异常");
			retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("冻结商户时发生异常");
			retMessage.setDetail(result);

		}
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> inputMerchantInfo(String merchantName, String serviceDistription, String workAddress,
			String minuteWorkAddress, String businessLicenseNum, String organizingInstitutionBarCode,
			String enterpriseLegalRepresentativeName, String contactsName, String contactPhone, String serviceTel,
			String contactEmail,
			Long operaterId, Long merchantId) {
		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result = false;
		// TODO 校验
		try {
			if (operaterId != null) {
				if (merchantId != null) {
					// 先校验操作者账户状态
					if (AccountStatus.normal.equals(accountService.queryAccountStatusById(operaterId))) {
						// 校验当前商户状态
						if (MerchantStatus.normal
								.equals(merchantService.queryMerchantStatusByid(merchantId, operaterId))) {
							Merchant merchant = new Merchant();
							merchant.setMerchantName(merchantName);
							merchant.setWorkAddress(workAddress);
							merchant.setMinuteWorkAddress(minuteWorkAddress);
							merchant.setCharterCode(businessLicenseNum);
							merchant.setOraganizingCode(organizingInstitutionBarCode);
							merchant.setPrincipal(enterpriseLegalRepresentativeName);
							merchant.setLinkMan(contactsName);
							merchant.setLinkManPhone(contactPhone);
							merchant.setServiceTel(serviceTel);
							merchant.setEmail(contactEmail);
							merchant.setMerchantId(merchantId);
							merchant.setServeExplain(serviceDistription);
							result = merchantService.fillInInformation(merchant);
							if (result) {
								retMessage.setRetCode(RetCodeEnum.success.getValue());
								retMessage.setRetMessage("商户详细信息提交成功");
								// TODO 添加操作记录
							} else {
								retMessage.setRetCode(RetCodeEnum.exception.getValue());
								retMessage.setRetMessage("商户详细信息提交失败");
							}
							retMessage.setDetail(result);
						} else {
							retMessage.setAll(RetCodeEnum.warning, "当前商户状态不正常", false);
						}

					} else {
						retMessage.setAll(RetCodeEnum.warning, "您的账户状态不正常", false);
					}
				} else {
					result = false;
					retMessage.setAll(RetCodeEnum.fail, "商户id为空", result);
				}
			} else {
				result = false;
				retMessage.setAll(RetCodeEnum.fail, "账户id为空", result);
			}

		} catch (Exception e) {
			LogFactory.error(this, "商户详细信息提交时发生异常", e);
			retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("商户详细信息提交时发生异常");
			retMessage.setDetail(result);
		}
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> modifyMerchantInfo(String merchantName, String serviceDistription, String workAddress,
			String minuteWorkAddress, String businessLicenseNum, String organizingInstitutionBarCode,
			String enterpriseLegalRepresentativeName, String contactsName, String contactPhone, String serviceTel,
			String contactEmail, Long operaterId, Long merchantId) {
		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result = false;
		// TODO 权限校验
		try {
			if (operaterId != null) {
				if (merchantId != null) {
					// 先校验操作者账户状态
					if (AccountStatus.normal.equals(accountService.queryAccountStatusById(operaterId))) {
						// 校验商户状态
						if (MerchantStatus.normal
								.equals(merchantService.queryMerchantStatusByid(merchantId, operaterId))) {
							Merchant merchant = new Merchant();
							merchant.setMerchantName(merchantName);
							merchant.setWorkAddress(workAddress);
							merchant.setMinuteWorkAddress(minuteWorkAddress);
							merchant.setServeExplain(serviceDistription);
							merchant.setCharterCode(businessLicenseNum);
							merchant.setOraganizingCode(organizingInstitutionBarCode);
							merchant.setPrincipal(enterpriseLegalRepresentativeName);
							merchant.setLinkMan(contactsName);
							merchant.setLinkManPhone(contactPhone);
							merchant.setServiceTel(serviceTel);
							merchant.setEmail(contactEmail);
							merchant.setMerchantId(merchantId);
							result = merchantService.fillInInformation(merchant);
							if (result) {
								retMessage.setRetCode(RetCodeEnum.success.getValue());
								retMessage.setRetMessage("商户详细信息修改成功");
								// TODO 添加操作记录
							} else {
								retMessage.setRetCode(RetCodeEnum.exception.getValue());
								retMessage.setRetMessage("商户详细信息修改失败");
							}
							retMessage.setDetail(result);
						} else {
							retMessage.setAll(RetCodeEnum.warning, "当前商户状态不正常", false);
						}
					} else {
						retMessage.setAll(RetCodeEnum.warning, "您的账户状态不正常", false);
					}
				} else {
					result = false;
					retMessage.setAll(RetCodeEnum.fail, "商户id为空", result);
				}
			} else {
				result = false;
				retMessage.setAll(RetCodeEnum.fail, "账户id为空", result);
			}
		} catch (Exception e) {
			LogFactory.error(this, "商户详细信息修改时发生异常", e);
			retMessage.setRetCode(RetCodeEnum.error.getValue());
			retMessage.setRetMessage("商户详细信息修改时发生异常");
			retMessage.setDetail(result);
		}
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> organizingInstitutionBarCodePicUpLoad(File organizingInstitutionBarCodePicFile,
			Long operaterId, Long merchantId) {
		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result = false;
		Merchant merchant = null;
		try {
			if (merchantId != null) {
				if (operaterId != null) {
					// 先校验操作者账户状态
					if (AccountStatus.normal.equals(accountService.queryAccountStatusById(operaterId))) {
						// 校验商户状态
						if (MerchantStatus.normal
								.equals(merchantService.queryMerchantStatusByid(merchantId, operaterId))) {
							merchant = new Merchant();
							merchant.setMerchantId(merchantId);
							result = merchantService.organizingInstitutionBarCodePicUpLoad(merchant,
									organizingInstitutionBarCodePicFile);
							if (result) {
								retMessage.setAll(RetCodeEnum.success, "上传商户组织机构代码证成功", result);
							} else {
								retMessage.setAll(RetCodeEnum.exception, "上传商户组织机构代码证失败", result);
							}
						} else {
							retMessage.setAll(RetCodeEnum.warning, "当前商户状态不正常", false);
						}
					} else {
						retMessage.setAll(RetCodeEnum.warning, "您的账户状态不正常", false);
					}

				} else {
					result = false;
					retMessage.setAll(RetCodeEnum.fail, "账户id为空", result);
				}

			} else {
				result = false;
				retMessage.setAll(RetCodeEnum.fail, "商户id为空", result);
			}

		} catch (Exception e) {
			retMessage.setAll(RetCodeEnum.error, "上传商户组织机构代码证时发生异常", result);
		}
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> businessLicensePicUpLoad(File businessLicensePicFile, Long operaterId, Long merchantId) {
		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result = false;
		Merchant merchant = null;
		try {
			if (merchantId != null) {
				if (operaterId != null) {
					// 先校验操作者账户状态
					if (AccountStatus.normal.equals(accountService.queryAccountStatusById(operaterId))) {
						// 校验商户状态
						if (MerchantStatus.normal
								.equals(merchantService.queryMerchantStatusByid(merchantId, operaterId))) {
							merchant = new Merchant();
							merchant.setMerchantId(merchantId);
							result = merchantService.businessLicensePicUpLoad(merchant, businessLicensePicFile);
							if (result) {
								retMessage.setAll(RetCodeEnum.success, "上传商户营业执照成功", result);
							} else {
								retMessage.setAll(RetCodeEnum.exception, "上传商户营业执照失败", result);
							}
						} else {
							retMessage.setAll(RetCodeEnum.warning, "当前商户状态不正常", false);
						}
					} else {
						retMessage.setAll(RetCodeEnum.warning, "您的账户状态不正常", false);
					}

				} else {
					result = false;
					retMessage.setAll(RetCodeEnum.fail, "账户id为空", result);
				}

			} else {
				result = false;
				retMessage.setAll(RetCodeEnum.fail, "商户id为空", result);
			}

		} catch (Exception e) {
			retMessage.setAll(RetCodeEnum.error, "上传商户营业执照时发生异常", result);
		}
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> otherPicUpLoad(File otherPicFile, Long operaterId, Long merchantId) {
		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result = false;
		Merchant merchant = null;
		try {
			if (merchantId != null) {
				if (operaterId != null) {
					// 先校验操作者账户状态
					if (AccountStatus.normal.equals(accountService.queryAccountStatusById(operaterId))) {
						// 校验商户状态
						if (MerchantStatus.normal
								.equals(merchantService.queryMerchantStatusByid(merchantId, operaterId))) {
							merchant = new Merchant();
							merchant.setMerchantId(merchantId);
							result = merchantService.otherPicUpLoad(merchant, otherPicFile);
							if (result) {
								retMessage.setAll(RetCodeEnum.success, "上传其他证明资料成功", result);
							} else {
								retMessage.setAll(RetCodeEnum.exception, "上传其他证明资料失败", result);
							}
						} else {
							retMessage.setAll(RetCodeEnum.warning, "当前商户状态不正常", false);
						}
					} else {
						retMessage.setAll(RetCodeEnum.warning, "您的账户状态不正常", false);
					}

				} else {
					result = false;
					retMessage.setAll(RetCodeEnum.fail, "账户id为空", result);
				}

			} else {
				result = false;
				retMessage.setAll(RetCodeEnum.fail, "商户id为空", result);
			}

		} catch (Exception e) {
			retMessage.setAll(RetCodeEnum.error, "上传其他证明资料时发生异常", result);
		}
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> modifyOrganizingInstitutionBarCodePic(File organizingInstitutionBarCodePicFile,
			Long operaterId, Long merchantId) {
		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result = false;
		Merchant merchant = null;
		try {
			if (merchantId != null) {
				if (operaterId != null) {
					// 先校验操作者账户状态
					if (AccountStatus.normal.equals(accountService.queryAccountStatusById(operaterId))) {
						// 校验商户状态
						if (MerchantStatus.normal
								.equals(merchantService.queryMerchantStatusByid(merchantId, operaterId))) {
							merchant = new Merchant();
							merchant.setMerchantId(merchantId);
							result = merchantService.organizingInstitutionBarCodePicUpLoad(merchant,
									organizingInstitutionBarCodePicFile);
							if (result) {
								retMessage.setAll(RetCodeEnum.success, "修改商户组织机构代码证成功", result);
							} else {
								retMessage.setAll(RetCodeEnum.exception, "修改商户组织机构代码证失败", result);
							}
						} else {
							retMessage.setAll(RetCodeEnum.warning, "当前商户状态不正常", false);
						}
					} else {
						retMessage.setAll(RetCodeEnum.warning, "您的账户状态不正常", false);
					}

				} else {
					result = false;
					retMessage.setAll(RetCodeEnum.fail, "账户id为空", result);
				}
			} else {
				result = false;
				retMessage.setAll(RetCodeEnum.fail, "商户id为空", result);
			}

		} catch (Exception e) {
			retMessage.setAll(RetCodeEnum.error, "修改商户组织机构代码证时发生异常", result);
		}
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> modifyBusinessLicensePic(File businessLicensePicFile, Long operaterId, Long merchantId) {
		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result = false;
		Merchant merchant = null;
		try {
			if (merchantId != null) {
				if (operaterId != null) {
					// 先校验操作者账户状态
					if (AccountStatus.normal.equals(accountService.queryAccountStatusById(operaterId))) {
						// 校验商户状态
						if (MerchantStatus.normal
								.equals(merchantService.queryMerchantStatusByid(merchantId, operaterId))) {
							merchant = new Merchant();
							merchant.setMerchantId(merchantId);
							result = merchantService.businessLicensePicUpLoad(merchant, businessLicensePicFile);
							if (result) {
								retMessage.setAll(RetCodeEnum.success, "修改商户营业执照成功", result);
							} else {
								retMessage.setAll(RetCodeEnum.exception, "修改商户营业执照失败", result);
							}
						} else {
							retMessage.setAll(RetCodeEnum.warning, "当前商户状态不正常", false);
						}
					} else {
						retMessage.setAll(RetCodeEnum.warning, "您的账户状态不正常", false);
					}

				} else {
					result = false;
					retMessage.setAll(RetCodeEnum.fail, "账户id为空", result);
				}

			} else {
				result = false;
				retMessage.setAll(RetCodeEnum.fail, "商户id为空", result);
			}

		} catch (Exception e) {
			retMessage.setAll(RetCodeEnum.error, "修改商户营业执照时发生异常", result);
		}
		return retMessage;
	}

	@Override
	public RetMessage<Boolean> modifyOtherPic(File otherPicFile, Long operaterId, Long merchantId) {
		RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
		Boolean result = false;
		Merchant merchant = null;
		try {
			if (merchantId != null) {
				if (operaterId != null) {
					// 先校验操作者账户状态
					if (AccountStatus.normal.equals(accountService.queryAccountStatusById(operaterId))) {
						// 校验商户状态
						if (MerchantStatus.normal
								.equals(merchantService.queryMerchantStatusByid(merchantId, operaterId))) {
							merchant = new Merchant();
							merchant.setMerchantId(merchantId);
							result = merchantService.otherPicUpLoad(merchant, otherPicFile);
							if (result) {
								retMessage.setAll(RetCodeEnum.success, "修改其他证明资料成功", result);
							} else {
								retMessage.setAll(RetCodeEnum.exception, "修改其他证明资料失败", result);
							}
						} else {
							retMessage.setAll(RetCodeEnum.warning, "当前商户状态不正常", false);
						}
					} else {
						retMessage.setAll(RetCodeEnum.warning, "您的账户状态不正常", false);
					}

				} else {
					result = false;
					retMessage.setAll(RetCodeEnum.fail, "账户id为空", result);
				}

			} else {
				result = false;
				retMessage.setAll(RetCodeEnum.fail, "商户id为空", result);
			}

		} catch (Exception e) {
			retMessage.setAll(RetCodeEnum.error, "修改其他证明资料时发生异常", result);
		}
		return retMessage;
	}
}
