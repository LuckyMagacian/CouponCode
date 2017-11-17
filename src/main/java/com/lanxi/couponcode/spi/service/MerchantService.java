package com.lanxi.couponcode.spi.service;

import com.lanxi.couponcode.impl.assist.RetMessage;
import com.lanxi.couponcode.spi.abstractentity.AbstractMerchant;
import com.lanxi.couponcode.spi.consts.annotations.HiddenArg;
import com.lanxi.couponcode.spi.consts.annotations.RealReturnType;
import com.lanxi.couponcode.spi.consts.enums.MerchantStatus;

import java.io.File;
import java.util.List;

/**
 * Created by yangyuanjian on 2017/11/7.
 */
public interface MerchantService {
    RetMessage<Boolean> addMerchant(String merchantName,
                                    String workAddress,
                                    String minuteWorkAddress,
                                    @HiddenArg Long operaterId);

    RetMessage<Boolean> modifyMerchant(String merchantName,
                           String workAddress,
                           String minuteWorkAddress,
                           Long operaterId,
                           @HiddenArg Long merchantId);

    @RealReturnType("List<Merchant>")
    RetMessage<String> queryMerchants(String merchantName,
                          MerchantStatus merchantStatus,
                          String timeStart,
                          String timeStop,
                          @HiddenArg Integer pageNum,
                          @HiddenArg Integer pageSize,
                          @HiddenArg Long operaterId);

    RetMessage<File> queryMerchantsExport(String merchantName,
                              MerchantStatus merchantStatus,
                              String timeStart,
                              String  timeStop,
                              @HiddenArg Long operaterId);

    RetMessage<Boolean> enableMerchant(@HiddenArg Long merchantId,
                           @HiddenArg Long operaterId);

    RetMessage<Boolean> disableMerchant(@HiddenArg Long merchantId,
                            @HiddenArg Long operaterId);

    RetMessage<Boolean> inputMerchantInfo(String merchantName,
                              String serviceDistription,
                              String workAddress,
                              String minuteWorkAddress,
                              String businessLicenseNum,
                              String organizingInstitutionBarCode,
                              String enterpriseLegalRepresentativeName,
                              String contactsName,
                              String contactPhone,
                              String serviceTel,
                              String contactEmail,
                              File organizingInstitutionBarCodePic,
                              File businessLicensePic,
                              File otherFile,
                              @HiddenArg Long operaterId,
                              @HiddenArg Long merchantId);

    RetMessage<Boolean> modifyMerchantInfo(String merchantName,
                               String serviceDistription,
                               String workAddress,
                               String minuteWorkAddress,
                               String businessLicenseNum,
                               String organizingInstitutionBarCode,
                               String enterpriseLegalRepresentativeName,
                               String contactsName,
                               String contactPhone,
                               String serviceTel,
                               String contactEmail,
                               File organizingInstitutionBarCodePic,
                               File businessLicensePic,
                               File otherFile,
                               @HiddenArg Long operaterId,
                               @HiddenArg Long merchantId);

}

