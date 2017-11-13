package com.lanxi.couponcode.impl.controller;

import com.lanxi.couponcode.impl.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.enums.MerchantStatus;

import java.io.File;

/**
 * Created by yangyuanjian on 2017/11/13.
 */
public class MerchantServiceImpl implements com.lanxi.couponcode.spi.service.MerchantService{
    @Override
    public RetMessage<Boolean> addMerchant(String merchantName, String workAddress, Long operaterId) {
        return null;
    }

    @Override
    public RetMessage<Boolean> modifyMerchant(String merchantName, String workAddress, Long operaterId, Long merchantId) {
        return null;
    }

    @Override
    public RetMessage<String> queryMerchants(String merchantName, MerchantStatus merchantStatus, String timeStart, String timeStop, Integer pageNum, Integer pageSize, Long operaterId) {
        return null;
    }

    @Override
    public RetMessage<File> queryMerchantsExport(String merchantName, MerchantStatus merchantStatus, String timeStart, String timeStop, Long operaterId) {
        return null;
    }

    @Override
    public RetMessage<Boolean> enableMerchant(Long merchantId, Long operaterId) {
        return null;
    }

    @Override
    public RetMessage<Boolean> disableMerchant(Long merchantId, Long operaterId) {
        return null;
    }

    @Override
    public RetMessage<Boolean> inputMerchantInfo(String merchantName, String serviceDistription, String workAddress, String businessLicenseNum, String organizingInstitutionBarCode, String enterpriseLegalRepresentativeName, String contactsName, String contactPhone, String serviceTel, String contactEmail, File organizingInstitutionBarCodePic, File businessLicensePic, File otherFile, Long operaterId, Long merchantId) {
        return null;
    }

    @Override
    public RetMessage<Boolean> modifyMerchantInfo(String merchantName, String serviceDistription, String workAddress, String businessLicenseNum, String organizingInstitutionBarCode, String enterpriseLegalRepresentativeName, String contactsName, String contactPhone, String serviceTel, String contactEmail, File organizingInstitutionBarCodePic, File businessLicensePic, File otherFile, Long operaterId, Long merchantId) {
        return null;
    }
}
