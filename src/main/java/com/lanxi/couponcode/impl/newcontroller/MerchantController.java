package com.lanxi.couponcode.impl.newcontroller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.lanxi.couponcode.impl.entity.Account;
import com.lanxi.couponcode.impl.entity.Merchant;
import com.lanxi.couponcode.impl.entity.OperateRecord;
import com.lanxi.couponcode.impl.newservice.*;
import com.lanxi.couponcode.impl.assist.FillAssist;
import com.lanxi.couponcode.spi.assist.FileDelete;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.assist.TimeAssist;
import com.lanxi.couponcode.spi.config.ConstConfig;
import com.lanxi.couponcode.impl.config.HiddenMap;
import com.lanxi.couponcode.spi.consts.annotations.CheckArg;
import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.couponcode.spi.consts.enums.*;
import com.lanxi.couponcode.spi.defaultInterfaces.ToJson;
import com.lanxi.couponcode.spi.service.RedisService;
import com.lanxi.util.entity.LogFactory;
import com.lanxi.util.utils.LoggerUtil;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.io.*;
import java.sql.Blob;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lanxi.couponcode.impl.assist.PredicateAssist.*;

/**
 * Created by yangyuanjian on 2017/11/13.
 */
@CheckArg
@Controller ("merchantControllerService")
@EasyLog (LoggerUtil.LogLevel.INFO)
public class MerchantController implements com.lanxi.couponcode.spi.service.MerchantService {
    @Resource
    private AccountService       accountService;
    @Resource
    private MerchantService      merchantService;
    @Resource
    private RedisService         redisService;
    @Resource
    private RedisEnhancedService redisEnhancedService;
    @Resource
    private OperateRecordService operateRecordService;
    @Resource
    private MerchantPicsService picsService;

    @Override
    public RetMessage<Boolean> addMerchant(String merchantName, String workAddress, String minuteWorkAddress,
                                           Long operaterId) {
        RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
        Boolean result = false;
        try {
            Account a = accountService.queryAccountById(operaterId);
            RetMessage message = checkAccount.apply(a, OperateType.createMerchant);
            if (notNull.test(message))
                return message;
            if (merchantService.isRepeat(merchantName)) {
                Merchant merchant = new Merchant();
                merchant.setMerchantName(merchantName);
                merchant.setWorkAddress(workAddress);
                if (minuteWorkAddress != null && !minuteWorkAddress.isEmpty()) {
                    merchant.setMinuteWorkAddress(minuteWorkAddress);
                }
                merchant.setMerchantId(IdWorker.getId());
                merchant.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
                merchant.setMerchantStatus(MerchantStatus.normal);
                merchant.setAddId(operaterId);
                merchant.setAddName(a.getUserName());
                result = merchantService.addMerchant(merchant);
                if (result) {
                    retMessage.setRetCode(RetCodeEnum.success.getValue());
                    retMessage.setRetMessage("添加商户成功");
                    OperateRecord record = new OperateRecord();
                    record.setRecordId(IdWorker.getId());
                    record.setOperaterId(operaterId);
                    record.setAccountType(a.getAccountType());
                    record.setPhone(a.getPhone());
                    record.setName(a.getUserName());
                    record.setTargetType(OperateTargetType.merchant);
                    record.setType(OperateType.createMerchant);
                    record.setOperateTime(TimeAssist.getNow());
                    record.setOperateResult("success");
                    record.setMerchantId(a.getMerchantId());
                    record.setShopId(a.getShopId());
                    record.setMerchantName(a.getMerchantName());
                    record.setShopName(a.getShopName());
//                    record.setDescription("添加商户[" + merchant.getMerchantId() + "]");
                    record.setDescription("添加商户");
                    operateRecordService.addRecord(record);
                }
                if (!result) {
                    retMessage.setRetCode(RetCodeEnum.exception.getValue());
                    retMessage.setRetMessage("添加商户失败");
                }
                retMessage.setDetail(result);
            } else {
                return new RetMessage<>(RetCodeEnum.fail, "商户名称重复", null);
            }

        } catch (Exception e) {
            LogFactory.error(this, "添加商户时发生异常", e);
            retMessage.setAll(RetCodeEnum.error, "添加商户时发生异常", result);
        }
        return retMessage;
    }

    @Override
    public RetMessage<Boolean> modifyMerchant(String merchantName, String workAddress, String minuteWorkAddress,
                                              Long operaterId, Long merchantId) {
        RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
        Boolean result = false;
        try {
            Account a = accountService.queryAccountById(operaterId);
            RetMessage message = checkAccount.apply(a, OperateType.modifyMerchant);
            if (notNull.test(message))
                return message;
            if (merchantService.isRepeat(merchantName, merchantId)) {
                Merchant m = merchantService.queryMerchantParticularsById(merchantId);
                message = checkMerchant.apply(m, OperateType.modifyMerchant);
                if (notNull.test(message))
                    return message;
                Merchant merchant = new Merchant();
                merchant.setMerchantId(merchantId);
                if (merchantName != null && !merchantName.isEmpty() && workAddress != null && !workAddress.isEmpty()) {
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
                    if (merchantName != null) {
                        accountService.modifyAccountMerchantName(merchantId, merchantName);
                    }
                    OperateRecord record = new OperateRecord();
                    record.setRecordId(IdWorker.getId());
                    record.setOperaterId(operaterId);
                    record.setAccountType(a.getAccountType());
                    record.setPhone(a.getPhone());
                    record.setName(a.getUserName());
                    record.setTargetType(OperateTargetType.merchant);
                    record.setType(OperateType.modifyMerchant);
                    record.setOperateTime(TimeAssist.getNow());
                    record.setMerchantId(a.getMerchantId());
                    record.setShopId(a.getShopId());
                    record.setMerchantName(a.getMerchantName());
                    record.setShopName(a.getShopName());
                    record.setOperateResult("success");
                    record.setDescription("修改商户");
//                    record.setDescription("修改商户[" + merchantId + "]");
                    operateRecordService.addRecord(record);
                }
                if (!result) {
                    retMessage.setRetCode(RetCodeEnum.exception.getValue());
                    retMessage.setRetMessage("修改商户失败");
                }
                retMessage.setDetail(result);
            } else {
                return new RetMessage<>(RetCodeEnum.fail, "商户名称重复", null);
            }

        } catch (Exception e) {
            LogFactory.error(this, "修改商户时发生异常", e);
            retMessage.setAll(RetCodeEnum.error, "修改商户时发生异常", result);
        }
        return retMessage;
    }

    @Override
    public RetMessage<String> queryMerchants(String merchantName, MerchantStatus merchantStatus, String timeStart,
                                             String timeStop, Integer pageNum, Integer pageSize, Long operaterId) {
        RetMessage<String> retMessage = new RetMessage<String>();
        List<Merchant> merchants = null;
        try {
            Account a = accountService.queryAccountById(operaterId);
            RetMessage message = checkAccount.apply(a, OperateType.queryMerchant);
            if (notNull.test(message))
                return message;
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
                wrapper.eq("merchant_status", merchantStatus + "");
            } else {
                wrapper.in("merchant_status", MerchantStatus.normal.getValue() + "," + MerchantStatus.freeze);
            }
            if (merchantName != null && !merchantName.isEmpty()) {
                wrapper.like("merchant_name", merchantName);
            }
            wrapper.orderBy("create_time",false);
            LogFactory.info(this, "组装成的条件[" + wrapper + "]");
            Page<Merchant> pageObj = new Page<Merchant>(pageNum, pageSize);
            merchants = merchantService.getMerchantByCondition(pageObj, wrapper);
            FillAssist.returnDeal.accept(HiddenMap.ADMIN_MERCHANT,merchants);
            Map<String, Object> map = new HashMap<>();
            map.put("page", pageObj);
            map.put("list", merchants);
            String result = ToJson.toJson(map);
            retMessage.setDetail(result);
            if (merchants != null && merchants.size() > 0) {
                retMessage.setRetCode(RetCodeEnum.success.getValue());
                retMessage.setRetMessage("查询完毕");
            } else {
                retMessage.setRetCode(RetCodeEnum.success.getValue());
                retMessage.setRetMessage("没有查询到任何数据");
            }
        } catch (Exception e) {
            LogFactory.error(this, "查询数据时出现异常", e);
            retMessage.setAll(RetCodeEnum.error, "查询数据时发生异常", null);
        }
        return retMessage;
    }

    @Override
    public RetMessage<File> queryMerchantsExport(String merchantName, MerchantStatus merchantStatus, String timeStart,
                                                 String timeStop, Long operaterId) {
        RetMessage<File> retMessage = new RetMessage<File>();
        try {
            Account a = accountService.queryAccountById(operaterId);
            RetMessage message = checkAccount.apply(a, OperateType.exportMerchant);
            if (notNull.test(message))
                return message;
            File file = merchantService.merchantExport(merchantName, merchantStatus, timeStart, timeStop, operaterId);
            FileDelete.add(file);
            if (file != null) {
                return new RetMessage<File>(RetCodeEnum.success, "导出成功", file);
            } else {
                return new RetMessage<File>(RetCodeEnum.fail, "导出失败", null);
            }
        } catch (Exception e) {
            LogFactory.error(this, "导出商户时发生异常", e);
            return new RetMessage<File>(RetCodeEnum.error, "导出商户时发生异常", null);
        }

    }

    @Override
    public RetMessage<Boolean> enableMerchant(Long merchantId, Long operaterId) {
        RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
        Boolean result = false;
        try {
            Account a = accountService.queryAccountById(operaterId);
            RetMessage message = checkAccount.apply(a, OperateType.unfreezeMerchant);
            if (notNull.test(message))
                return message;
            Merchant m = merchantService.queryMerchantParticularsById(merchantId);
            message = checkMerchant.apply(m, OperateType.unfreezeMerchant);
            if (notNull.test(message))
                return message;
            Merchant merchant = new Merchant();
            merchant.setMerchantId(merchantId);
            merchant.setMerchantStatus(MerchantStatus.normal);
            result = merchantService.unFreezeMerchant(merchant);
            if (result) {
                retMessage.setRetCode(RetCodeEnum.success.getValue());
                retMessage.setRetMessage("开启商户成功");
                OperateRecord record = new OperateRecord();
                record.setRecordId(IdWorker.getId());
                record.setOperaterId(operaterId);
                record.setAccountType(a.getAccountType());
                record.setPhone(a.getPhone());
                record.setName(a.getUserName());
                record.setTargetType(OperateTargetType.merchant);
                record.setType(OperateType.unfreezeMerchant);
                record.setOperateTime(TimeAssist.getNow());
                record.setOperateResult("success");
                record.setMerchantId(a.getMerchantId());
                record.setShopId(a.getShopId());
                record.setMerchantName(a.getMerchantName());
                record.setShopName(a.getShopName());
                record.setDescription("开启商户");
//                record.setDescription("开启商户[" + merchantId + "]");
                operateRecordService.addRecord(record);
            } else {
                retMessage.setRetCode(RetCodeEnum.exception.getValue());
                retMessage.setRetMessage("开启商户失败");
            }
            retMessage.setDetail(result);
        } catch (Exception e) {
            LogFactory.error(this, "开启商户时发生异常");
            retMessage.setAll(RetCodeEnum.error, "开启商户时发生异常", result);
        }
        return retMessage;
    }

    @Override
    public RetMessage<Boolean> disableMerchant(Long merchantId, Long operaterId) {
        RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
        Boolean result = false;
        try {
            Account a = accountService.queryAccountById(operaterId);
            RetMessage message = checkAccount.apply(a, OperateType.freezeMerchant);
            if (notNull.test(message))
                return message;
            Merchant m = merchantService.queryMerchantParticularsById(merchantId);
            message = checkMerchant.apply(m, OperateType.freezeMerchant);
            if (notNull.test(message))
                return message;
            Merchant merchant = new Merchant();
            merchant.setMerchantId(merchantId);
            merchant.setMerchantStatus(MerchantStatus.freeze);
            result = merchantService.freezeMerchant(merchant);
            if (result) {
                retMessage.setRetCode(RetCodeEnum.success.getValue());
                retMessage.setRetMessage("冻结商户成功");
                OperateRecord record = new OperateRecord();
                record.setRecordId(IdWorker.getId());
                record.setOperaterId(operaterId);
                record.setAccountType(a.getAccountType());
                record.setPhone(a.getPhone());
                record.setName(a.getUserName());
                record.setTargetType(OperateTargetType.merchant);
                record.setType(OperateType.freezeMerchant);
                record.setOperateTime(TimeAssist.getNow());
                record.setOperateResult("success");
                record.setMerchantId(a.getMerchantId());
                record.setShopId(a.getShopId());
                record.setMerchantName(a.getMerchantName());
                record.setShopName(a.getShopName());
                record.setDescription("冻结商户");
//                record.setDescription("冻结商户[" + merchantId + "]");
                operateRecordService.addRecord(record);
            } else {
                retMessage.setRetCode(RetCodeEnum.exception.getValue());
                retMessage.setRetMessage("冻结商户失败");
            }
            retMessage.setDetail(result);
        } catch (Exception e) {
            LogFactory.error(this, "冻结商户时发生异常");
            retMessage.setAll(RetCodeEnum.error, "冻结商户时发生异常", result);
        }
        return retMessage;
    }

    @Override
    public RetMessage<Boolean> inputMerchantInfo(String merchantName, String serviceDistription, String workAddress,
                                                 String minuteWorkAddress, String businessLicenseNum, String organizingInstitutionBarCode,
                                                 String enterpriseLegalRepresentativeName, String contactsName, String contactPhone, String serviceTel,
                                                 String contactEmail, Long operaterId, Long merchantId,String registerAddress,
                                                 String minuteRegisterAddress) {
        RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
        Boolean result = false;
        try {
            Account a = accountService.queryAccountById(operaterId);
            RetMessage message = checkAccount.apply(a, OperateType.inputMerchantInfo);
            if (notNull.test(message))
                return message;
            if (merchantService.isRepeat(merchantName, a.getMerchantId())) {
                Merchant m = merchantService.queryMerchantParticularsById(a.getMerchantId());
                message = checkMerchant.apply(m, OperateType.inputMerchantInfo);
                if (notNull.test(message))
                    return message;
                Merchant merchant = new Merchant();
                merchant.setMerchantName(merchantName);
//                merchant.setWorkAddress(workAddress);
//                merchant.setMinuteWorkAddress(minuteWorkAddress);
                merchant.setCharterCode(businessLicenseNum);
                merchant.setOraganizingCode(organizingInstitutionBarCode);
                merchant.setPrincipal(enterpriseLegalRepresentativeName);
                merchant.setLinkMan(contactsName);
               // if (RegularUtil.isPhone(contactPhone))
                merchant.setLinkManPhone(contactPhone);
               // else
                  //  merchant.setLinkManPhone(null);
                merchant.setServiceTel(serviceTel);
                //if (RegularUtil.isEmail(contactEmail))
                    merchant.setEmail(contactEmail);
               // else
                 //   merchant.setEmail(null);
                merchant.setMerchantId(m.getMerchantId());
                merchant.setServeExplain(serviceDistription);
                merchant.setRegisterAddress(registerAddress);
                merchant.setMinuteRegisterAddress(minuteRegisterAddress);
                result = merchantService.fillInInformation(merchant);
                if (result) {
                    retMessage.setRetCode(RetCodeEnum.success.getValue());
                    retMessage.setRetMessage("商户详细信息提交成功");
                    retMessage.setRetCode(RetCodeEnum.success.getValue());
                    retMessage.setRetMessage("商户详细信息提交成功");
                    OperateRecord record = new OperateRecord();
                    record.setRecordId(IdWorker.getId());
                    record.setOperaterId(operaterId);
                    record.setAccountType(a.getAccountType());
                    record.setPhone(a.getPhone());
                    record.setName(a.getUserName());
                    record.setMerchantId(a.getMerchantId());
                    record.setShopId(a.getShopId());
                    record.setTargetType(OperateTargetType.merchant);
                    record.setType(OperateType.inputMerchantInfo);
                    record.setOperateTime(TimeAssist.getNow());
                    record.setOperateResult("success");
                    record.setMerchantName(a.getMerchantName());
                    record.setShopName(a.getShopName());
                    record.setDescription("商户详细信息提交");
//                    record.setDescription("商户详细信息提交[" + m.getMerchantId() + "]");
                    operateRecordService.addRecord(record);
                } else {
                    retMessage.setRetCode(RetCodeEnum.exception.getValue());
                    retMessage.setRetMessage("商户详细信息提交失败");
                }
                retMessage.setDetail(result);
            } else {
                return new RetMessage<>(RetCodeEnum.fail, "商户名称重复", null);
            }

        } catch (Exception e) {
            LogFactory.error(this, "商户详细信息提交时发生异常", e);
            retMessage.setAll(RetCodeEnum.error, "商户详细信息提交时发生异常", result);
        }
        return retMessage;
    }

    @Override
    public RetMessage<Boolean> modifyMerchantInfo(String merchantName, String serviceDistription, String workAddress,
                                                  String minuteWorkAddress, String businessLicenseNum, String organizingInstitutionBarCode,
                                                  String enterpriseLegalRepresentativeName, String contactsName, String contactPhone, String serviceTel,
                                                  String contactEmail, Long operaterId, Long merchantId,String registerAddress,
                                                  String minuteRegisterAddress) {
        RetMessage<Boolean> retMessage = new RetMessage<Boolean>();
        Boolean result = false;
        try {
            Account a = accountService.queryAccountById(operaterId);
            RetMessage message = checkAccount.apply(a, OperateType.inputMerchantInfo);
            if (notNull.test(message))
                return message;
            if (merchantService.isRepeat(merchantName, a.getMerchantId())) {
                Merchant m = merchantService.queryMerchantParticularsById(a.getMerchantId());
                message = checkMerchant.apply(m, OperateType.inputMerchantInfo);
                if (notNull.test(message))
                    return message;
                Merchant merchant = new Merchant();
                merchant.setMerchantName(merchantName);
//                merchant.setWorkAddress(workAddress);
//                merchant.setMinuteWorkAddress(minuteWorkAddress);
                merchant.setServeExplain(serviceDistription);
                merchant.setCharterCode(businessLicenseNum);
                merchant.setOraganizingCode(organizingInstitutionBarCode);
                merchant.setPrincipal(enterpriseLegalRepresentativeName);
                merchant.setLinkMan(contactsName);
                //if (RegularUtil.isPhone(contactPhone))
                merchant.setLinkManPhone(contactPhone);
                //else
                   // merchant.setLinkManPhone(null);
                merchant.setServiceTel(serviceTel);
                //if (RegularUtil.isEmail(contactEmail))
                merchant.setEmail(contactEmail);
                //else
                  //  merchant.setEmail(null);
                merchant.setMerchantId(a.getMerchantId());
                merchant.setRegisterAddress(registerAddress);
                merchant.setMinuteRegisterAddress(minuteRegisterAddress);
                result = merchantService.fillInInformation(merchant);
                if (result) {
                    retMessage.setRetCode(RetCodeEnum.success.getValue());
                    retMessage.setRetMessage("商户详细信息修改成功");
                    OperateRecord record = new OperateRecord();
                    record.setRecordId(IdWorker.getId());
                    record.setOperaterId(operaterId);
                    record.setAccountType(a.getAccountType());
                    record.setPhone(a.getPhone());
                    record.setName(a.getUserName());
                    record.setTargetType(OperateTargetType.merchant);
                    record.setType(OperateType.modifyMerchant);
                    record.setOperateTime(TimeAssist.getNow());
                    record.setOperateResult("success");
                    record.setMerchantName(a.getMerchantName());
                    record.setShopName(a.getShopName());
                    record.setMerchantId(a.getMerchantId());
                    record.setShopId(a.getShopId());
                    record.setDescription("商户详细信息修改");
//                    record.setDescription("商户详细信息修改[" + a.getMerchantId() + "]");
                    operateRecordService.addRecord(record);
                } else {
                    retMessage.setRetCode(RetCodeEnum.exception.getValue());
                    retMessage.setRetMessage("商户详细信息修改失败");
                }
                retMessage.setDetail(result);
            } else {
                return new RetMessage<Boolean>(RetCodeEnum.fail, "商户名称重复", null);
            }


        } catch (Exception e) {
            LogFactory.error(this, "商户详细信息修改时发生异常", e);
            retMessage.setAll(RetCodeEnum.error, "商户详细信息修改时发生异常", result);
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
            Account a = accountService.queryAccountById(operaterId);
            RetMessage message = checkAccount.apply(a, OperateType.inputMerchantInfo);
            if (notNull.test(message))
                return message;
            Merchant m = merchantService.queryMerchantParticularsById(a.getMerchantId());
            message = checkMerchant.apply(m, OperateType.inputMerchantInfo);
            if (notNull.test(message))
                return message;

            merchant = new Merchant();
            merchant.setMerchantId(m.getMerchantId());
            result = merchantService.organizingInstitutionBarCodePicUpLoad(merchant,
                    organizingInstitutionBarCodePicFile);
            if (result) {
                retMessage.setAll(RetCodeEnum.success, "上传商户组织机构代码证成功", result);
                OperateRecord record = new OperateRecord();
                record.setRecordId(IdWorker.getId());
                record.setOperaterId(operaterId);
                record.setAccountType(a.getAccountType());
                record.setPhone(a.getPhone());
                record.setName(a.getUserName());
                record.setTargetType(OperateTargetType.merchant);
                record.setType(OperateType.inputMerchantInfo);
                record.setOperateTime(TimeAssist.getNow());
                record.setMerchantId(a.getMerchantId());
                record.setShopId(a.getShopId());
                record.setMerchantName(a.getMerchantName());
                record.setShopName(a.getShopName());
                record.setOperateResult("success");
                record.setDescription("商户详细信息提交");
//                record.setDescription("商户详细信息提交[" + m.getMerchantId() + "]");
                operateRecordService.addRecord(record);
            } else {
                retMessage.setAll(RetCodeEnum.exception, "上传商户组织机构代码证失败", result);
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
            Account a = accountService.queryAccountById(operaterId);
            RetMessage message = checkAccount.apply(a, OperateType.inputMerchantInfo);
            if (notNull.test(message))
                return message;
            Merchant m = merchantService.queryMerchantParticularsById(a.getMerchantId());
            message = checkMerchant.apply(m, OperateType.inputMerchantInfo);
            if (notNull.test(message))
                return message;

            merchant = new Merchant();
            merchant.setMerchantId(m.getMerchantId());
            result = merchantService.businessLicensePicUpLoad(merchant, businessLicensePicFile);
            if (result) {
                retMessage.setAll(RetCodeEnum.success, "上传商户营业执照成功", result);
                OperateRecord record = new OperateRecord();
                record.setRecordId(IdWorker.getId());
                record.setOperaterId(operaterId);
                record.setAccountType(a.getAccountType());
                record.setPhone(a.getPhone());
                record.setName(a.getUserName());
                record.setTargetType(OperateTargetType.merchant);
                record.setType(OperateType.inputMerchantInfo);
                record.setOperateTime(TimeAssist.getNow());
                record.setMerchantId(a.getMerchantId());
                record.setShopId(a.getShopId());
                record.setMerchantName(a.getMerchantName());
                record.setShopName(a.getShopName());
                record.setOperateResult("success");
//                record.setDescription("商户详细信息提交[" + m.getMerchantId() + "]");
                record.setDescription("商户详细信息提交");
                operateRecordService.addRecord(record);
            } else {
                retMessage.setAll(RetCodeEnum.exception, "上传商户营业执照失败", result);
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
            Account a = accountService.queryAccountById(operaterId);
            RetMessage message = checkAccount.apply(a, OperateType.inputMerchantInfo);
            if (notNull.test(message))
                return message;
            Merchant m = merchantService.queryMerchantParticularsById(a.getMerchantId());
            message = checkMerchant.apply(m, OperateType.inputMerchantInfo);
            if (notNull.test(message))
                return message;
            merchant = new Merchant();
            merchant.setMerchantId(m.getMerchantId());
            result = merchantService.otherPicUpLoad(merchant, otherPicFile);
            if (result) {
                retMessage.setAll(RetCodeEnum.success, "上传其他证明资料成功", result);
                OperateRecord record = new OperateRecord();
                record.setRecordId(IdWorker.getId());
                record.setOperaterId(operaterId);
                record.setAccountType(a.getAccountType());
                record.setPhone(a.getPhone());
                record.setName(a.getUserName());
                record.setTargetType(OperateTargetType.merchant);
                record.setType(OperateType.inputMerchantInfo);
                record.setOperateTime(TimeAssist.getNow());
                record.setOperateResult("success");
                record.setMerchantId(a.getMerchantId());
                record.setShopId(a.getShopId());
                record.setMerchantName(a.getMerchantName());
                record.setShopName(a.getShopName());
//                record.setDescription("商户详细信息提交[" + m.getMerchantId() + "]");
                record.setDescription("商户详细信息提交");
                operateRecordService.addRecord(record);
            } else {
                retMessage.setAll(RetCodeEnum.exception, "上传其他证明资料失败", result);
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
            Account a = accountService.queryAccountById(operaterId);
            RetMessage message = checkAccount.apply(a, OperateType.inputMerchantInfo);
            if (notNull.test(message))
                return message;
            Merchant m = merchantService.queryMerchantParticularsById(a.getMerchantId());
            message = checkMerchant.apply(m, OperateType.inputMerchantInfo);
            if (notNull.test(message))
                return message;

            merchant = new Merchant();
            merchant.setMerchantId(m.getMerchantId());
            result = merchantService.organizingInstitutionBarCodePicUpLoad(merchant,
                    organizingInstitutionBarCodePicFile);
            if (result) {
                retMessage.setAll(RetCodeEnum.success, "修改商户组织机构代码证成功", result);
                OperateRecord record = new OperateRecord();
                record.setRecordId(IdWorker.getId());
                record.setOperaterId(operaterId);
                record.setAccountType(a.getAccountType());
                record.setPhone(a.getPhone());
                record.setName(a.getUserName());
                record.setTargetType(OperateTargetType.merchant);
                record.setType(OperateType.inputMerchantInfo);
                record.setOperateTime(TimeAssist.getNow());
                record.setOperateResult("success");
                record.setMerchantName(a.getMerchantName());
                record.setShopName(a.getShopName());
                record.setMerchantId(a.getMerchantId());
                record.setShopId(a.getShopId());
                record.setDescription("修改商户组织机构代码证");
//                record.setDescription("修改商户组织机构代码证[" + m.getMerchantId() + "]");
                operateRecordService.addRecord(record);
            } else {
                retMessage.setAll(RetCodeEnum.exception, "修改商户组织机构代码证失败", result);
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
            Account a = accountService.queryAccountById(operaterId);
            RetMessage message = checkAccount.apply(a, OperateType.inputMerchantInfo);
            if (notNull.test(message))
                return message;
            Merchant m = merchantService.queryMerchantParticularsById(a.getMerchantId());
            message = checkMerchant.apply(m, OperateType.inputMerchantInfo);
            if (notNull.test(message))
                return message;

            merchant = new Merchant();
            merchant.setMerchantId(m.getMerchantId());
            result = merchantService.businessLicensePicUpLoad(merchant, businessLicensePicFile);
            if (result) {
                retMessage.setAll(RetCodeEnum.success, "修改商户营业执照成功", result);
                OperateRecord record = new OperateRecord();
                record.setRecordId(IdWorker.getId());
                record.setOperaterId(operaterId);
                record.setAccountType(a.getAccountType());
                record.setPhone(a.getPhone());
                record.setName(a.getUserName());
                record.setTargetType(OperateTargetType.merchant);
                record.setType(OperateType.inputMerchantInfo);
                record.setOperateTime(TimeAssist.getNow());
                record.setOperateResult("success");
                record.setMerchantId(a.getMerchantId());
                record.setShopId(a.getShopId());
                record.setMerchantName(a.getMerchantName());
                record.setShopName(a.getShopName());
                record.setDescription("修改商户营业执照");
//                record.setDescription("修改商户营业执照[" + m.getMerchantId() + "]");
                operateRecordService.addRecord(record);
            } else {
                retMessage.setAll(RetCodeEnum.exception, "修改商户营业执照失败", result);
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
            Account a = accountService.queryAccountById(operaterId);
            RetMessage message = checkAccount.apply(a, OperateType.inputMerchantInfo);
            if (notNull.test(message))
                return message;
            Merchant m = merchantService.queryMerchantParticularsById(a.getMerchantId());
            message = checkMerchant.apply(m, OperateType.inputMerchantInfo);
            if (notNull.test(message))
                return message;
            merchant = new Merchant();
            merchant.setMerchantId(m.getMerchantId());
            result = merchantService.otherPicUpLoad(merchant, otherPicFile);
            if (result) {
                retMessage.setAll(RetCodeEnum.success, "修改其他证明资料成功", result);
                OperateRecord record = new OperateRecord();
                record.setRecordId(IdWorker.getId());
                record.setOperaterId(operaterId);
                record.setAccountType(a.getAccountType());
                record.setPhone(a.getPhone());
                record.setName(a.getUserName());
                record.setTargetType(OperateTargetType.merchant);
                record.setType(OperateType.inputMerchantInfo);
                record.setOperateTime(TimeAssist.getNow());
                record.setOperateResult("success");
                record.setMerchantName(a.getMerchantName());
                record.setShopName(a.getShopName());
                record.setMerchantId(a.getMerchantId());
                record.setShopId(a.getShopId());
                record.setDescription("修改其他证明资料");
//                record.setDescription("修改其他证明资料[" + m.getMerchantId() + "]");
                operateRecordService.addRecord(record);
            } else {
                retMessage.setAll(RetCodeEnum.exception, "修改其他证明资料失败", result);
            }
        } catch (Exception e) {
            retMessage.setAll(RetCodeEnum.error, "修改其他证明资料时发生异常", result);
        }
        return retMessage;
    }

    @CheckArg
    @Override
//	@Cache (exclusionArgs = {"operaterId"})
    public RetMessage<String> queryMerchantInfo(Long operaterId, Long merchantId) {
        try {
            Account a = accountService.queryAccountById(operaterId);
            RetMessage message = checkAccount.apply(a, OperateType.queryMerchantInfo);
            if (notNull.test(message))
                return message;
            Merchant merchant = merchantService.queryMerchantParticularsById(merchantId);
            if(isAdmin.test(a))
                FillAssist.returnDeal.accept(FillAssist.getMap.apply(AccountType.admin,Merchant.class), merchant);
            else if(isMerchantManager.test(a))
                FillAssist.returnDeal.accept(FillAssist.getMap.apply(AccountType.merchantManager, Merchant.class), merchant);
            if (merchant != null) {
                String result = merchant.toJson();
                return new RetMessage<String>(RetCodeEnum.success, "查询商户详情成功", result);
            } else
                return new RetMessage<String>(RetCodeEnum.success, "查询商户详情失败", null);
        } catch (Exception e) {
            LogFactory.error(this, "查询商户详情时发生异常", e);
            return new RetMessage<String>(RetCodeEnum.error, "查询商户详情时发生异常", null);
        }
    }

    @CheckArg
    @Override
    public RetMessage<Serializable> queryAllMerchant(Long operaterId) {
        return queryAllMerchant(null,operaterId);
//        try {
//            Account account = accountService.queryAccountById(operaterId);
//            if (isAdmin.negate().test(account))
//                return new RetMessage<>(RetCodeEnum.fail, "非管理员无权操作!", null);
//            Map<Long,String> map = new HashMap<>();
//            merchantService.queryAll()
//                    .parallelStream()
//                    .forEach(e -> map.put(e.getMerchantId(),e.getMerchantName()));
//            return new RetMessage<>(RetCodeEnum.success, "查询成功!", (Serializable) map);
//        } catch (Exception e) {
//            LogFactory.error(this, "查询全部商户时发生异常", e);
//            return new RetMessage<>(RetCodeEnum.error, "查询全部商户时发生异常", null);
//        }
    }

    @Override public RetMessage<Serializable> queryAllMerchant(MerchantStatus status, Long operaterId){
        try {
            Account account = accountService.queryAccountById(operaterId);
            if (isAdmin.negate().test(account))
                return new RetMessage<>(RetCodeEnum.fail, "非管理员无权操作!", null);
            Map<Long,String> map = new HashMap<>();
            merchantService.queryAll()
                           .parallelStream()
                           .filter(m->status==null?true:status.equals(m.getMerchantStatus()))
                           .forEach(e -> map.put(e.getMerchantId(),e.getMerchantName()));
            return new RetMessage<>(RetCodeEnum.success, "查询成功!", (Serializable) map);
        } catch (Exception e) {
            LogFactory.error(this, "查询全部商户时发生异常", e);
            return new RetMessage<>(RetCodeEnum.error, "查询全部商户时发生异常", null);
        }
    }

    @Override
    public RetMessage<File> queryPic(String path) {
        FileOutputStream fout=null;
        try {
            if(path==null)
                return new RetMessage<>(RetCodeEnum.fail,"获取商户证件图片失败!路径为空!",null);
            //   /merchantId/type
            String merchantIdStr=null;
            String type=null;
            String[]  strs=path.split("/");

            if(strs.length==2){
                merchantIdStr=strs[0];
                type=strs[1];
            }else if(strs.length==3){
                merchantIdStr=strs[1];
                type=strs[2];
            }else{
                return new RetMessage<>(RetCodeEnum.fail,"获取商户证件图片失败!路径非法!",null);
            }
            if(merchantIdStr==null||!merchantIdStr.matches("[0-9]{18}")){
                return new RetMessage<>(RetCodeEnum.fail,"获取商户证件图片失败!商户编号非法!!",null);
            }if(type==null||!type.matches("[0-2]")){
                return new RetMessage<>(RetCodeEnum.fail,"获取商户证件图片失败!图片类型非法!!",null);
            }
            Long merchantId=Long.parseLong(merchantIdStr);
            Blob pic=null;

            switch(type){
                case "0":
                    pic=picsService.getOrganizingInstitutionBarCodePic(merchantId);
                    type="OrganizingInstitutionBarCodePic";
                    break;
                case "1":
                    pic=picsService.getBusinessLicensePic(merchantId);
                    type="BusinessLicensePic";
                    break;
                case "2":
                    pic=picsService.getotherPic(merchantId);
                    type="OtherPic";
            }
            if(pic==null){
                return new RetMessage<>(RetCodeEnum.fail,"获取商户证件图片失败!!",null);
            }else{
                File file=new File(merchantIdStr+type+".jpg");
                fout=new FileOutputStream(file);
                fout.write(pic.getBytes(1, (int)pic.length()));
                return new RetMessage<>(RetCodeEnum.success, "获取商户证件成功", file);
            }



//            File file = new File(path);
//            FileDelete.add(file);
//            if (file.exists()) {
//                InputStream is = new FileInputStream(file);
//                File file2 = new File(file.getName());
//                FileDelete.add(file);
//                OutputStream os = new FileOutputStream(file2);
//                int length = -1;
//                byte[] temp = new byte[1024];
//                while ((length = is.read(temp)) != -1) {
//                    os.write(temp, 0, length);
//                }
//                os.flush();
//                is.close();
//                os.close();
//                return new RetMessage<>(RetCodeEnum.success, "获取商户证件成功", file2);
//            } else
//                return new RetMessage<>(RetCodeEnum.fail, "获取商户证件失败", null);

        } catch (Exception e) {
            LogFactory.error(this, "获取商户证件时发生异常", e);
            return new RetMessage<>(RetCodeEnum.error, "获取商户证件时发生异常", null);
        }finally{
            if(fout!=null){
                try{
                    fout.close();
                }catch(IOException e){
                    LogFactory.error(this, "关闭文件输出流时发生异常!", e);
                }
            }
        }

    }


}
