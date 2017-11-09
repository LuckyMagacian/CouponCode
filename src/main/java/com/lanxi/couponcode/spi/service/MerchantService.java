package com.lanxi.couponcode.spi.service;

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
    Boolean addMerchant(String merchantName,
                        String workAddress,
                        @HiddenArg Long operaterId);

    Boolean modifyMerchant(String merchantName,
                           String workAddress,
                           Long operaterId,
                           @HiddenArg Long merchantId);

    @RealReturnType("List<Merchant>")
    String queryMerchants(String merchantName,
                          MerchantStatus merchantStatus,
                          String timeStart,
                          String timeStop,
                          @HiddenArg Integer pageNum,
                          @HiddenArg Integer pageSize,
                          @HiddenArg Long operaterId);

    File queryMerchantsExport(String merchantName,
                              MerchantStatus merchantStatus,
                              String timeStart,
                              String  timeStop,
                              @HiddenArg Long operaterId);

    Boolean enableMerchant(@HiddenArg Long merchantId,
                           @HiddenArg Long operaterId);

    Boolean disableMerchant(@HiddenArg Long merchantId,
                            @HiddenArg Long operaterId);

    Boolean inputMerchantInfo(String merchantName,
                              String serviceDistription,
                              String workAddress,
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

    Boolean modifyMerchantInfo(String merchantName,
                               String serviceDistription,
                               String workAddress,
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

