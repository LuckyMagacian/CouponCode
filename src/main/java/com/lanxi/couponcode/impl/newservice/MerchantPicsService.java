package com.lanxi.couponcode.impl.newservice;

import com.lanxi.couponcode.impl.entity.MerchantPics;

import java.sql.Blob;

/**
 * @author yangyuanjian
 * @Date: Created in 1/18/2018 8:35 PM
 * @Modifer:
 */
public interface MerchantPicsService {

    boolean testExist(Long merchantId);
    Blob getOrganizingInstitutionBarCodePic(Long merchantId);

    Blob getBusinessLicensePic(Long merchantId);

    Blob getotherPic(Long merchantId);

    boolean addMerchantPics(MerchantPics merchantPics);

    boolean updateOrganizingInstitutionBarCodePic(Long merchantId, Blob pic);

    boolean updateBusinessLicensePic(Long merchantId, Blob pic);

    boolean updateotherPic(Long merchantId, Blob pic);

}
