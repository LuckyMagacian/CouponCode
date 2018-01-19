package com.lanxi.couponcode.spi.service;

import com.lanxi.couponcode.spi.assist.RetMessage;

import com.lanxi.couponcode.spi.consts.annotations.HiddenArg;
import com.lanxi.couponcode.spi.consts.annotations.RealReturnType;
import com.lanxi.couponcode.spi.consts.enums.MerchantStatus;

import java.io.File;
import java.io.Serializable;

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
                                          String timeStop,
                                          @HiddenArg Long operaterId);

    RetMessage<Boolean> enableMerchant(@HiddenArg Long merchantId,
                                       @HiddenArg Long operaterId);

    RetMessage<Boolean> disableMerchant(@HiddenArg Long merchantId,
                                        @HiddenArg Long operaterId);

    RetMessage<Boolean> inputMerchantInfo(String merchantName,
                                          String serveExplain,
                                          String workAddress,
                                          String minuteWorkAddress,
                                          String charterCode,
                                          String oraganizingCode,
                                          String principal,
                                          String linkMan,
                                          String linkManPhone,
                                          String serviceTel,
                                          String email,
                                          @HiddenArg Long operaterId,
                                          @HiddenArg Long merchantId,
                                          String registerAddress,
                                          String minuteRegisterAddress);

    RetMessage<Boolean> modifyMerchantInfo(String merchantName,
                                           String serveExplain,
                                           String workAddress,
                                           String minuteWorkAddress,
                                           String charterCode,
                                           String oraganizingCode,
                                           String principal,
                                           String linkMan,
                                           String linkManPhone,
                                           String serviceTel,
                                           String email,
                                           @HiddenArg Long operaterId,
                                           @HiddenArg Long merchantId,
                                           String registerAddress,
                                           String minuteRegisterAddress);
    
    RetMessage<Boolean> organizingInstitutionBarCodePicUpLoad(
            File organizingInstitutionBarCodePicFile,
            @HiddenArg Long operaterId,
            @HiddenArg Long merchantId);
    
    RetMessage<Boolean> businessLicensePicUpLoad(
            File businessLicensePicFile,
            @HiddenArg Long operaterId,
            @HiddenArg Long merchantId);
    
    RetMessage<Boolean> otherPicUpLoad(
            File otherPicFile,
            @HiddenArg Long operaterId,
            @HiddenArg Long merchantId);
    
    RetMessage<Boolean> modifyOrganizingInstitutionBarCodePic(
            File organizingInstitutionBarCodePicFile,
            @HiddenArg Long operaterId,
            @HiddenArg Long merchantId);
    
    RetMessage<Boolean> modifyBusinessLicensePic(
            File businessLicensePicFile,
            @HiddenArg Long operaterId,
            @HiddenArg Long merchantId);
    
    RetMessage<Boolean> modifyOtherPic(
            File otherPicFile,
            @HiddenArg Long operaterId,
            @HiddenArg Long merchantId);
    RetMessage<String> queryMerchantInfo(@HiddenArg Long operaterId,
                                         @HiddenArg Long merchantId);
    RetMessage<Serializable> queryAllMerchant(@HiddenArg Long operaterId);
    RetMessage<Serializable> queryAllMerchant(MerchantStatus status,@HiddenArg Long operaterId);
    RetMessage<File> queryPic(String path);
}

