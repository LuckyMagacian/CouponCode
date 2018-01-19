package com.lanxi.couponcode.impl.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lanxi.couponcode.spi.consts.annotations.Comment;

import java.sql.Blob;

/**
 * Created by yangyuanjian on 1/9/2018.
 */

@Comment("商户图片类")
@TableName("merchant_pics")
public class MerchantPics {
    @TableId("merchant_id")
    @Comment("商户编号")
    private Long merchantId;
    @Comment("组织机构代码证图片")
    @TableField("organizing_institution_bar_code_pic")
    private Blob organizingInstitutionBarCodePic;
    @Comment("工商营业执照")
    @TableField("business_license_pic")
    private Blob businessLicensePic;
    @Comment("其他证明材料")
    @TableField("other_pic")
    private Blob otherPic;

    public Long getMerchantId(){
        return merchantId;
    }

    public void setMerchantId(Long merchantId){
        this.merchantId = merchantId;
    }

    public Blob getOrganizingInstitutionBarCodePic(){
        return organizingInstitutionBarCodePic;
    }

    public void setOrganizingInstitutionBarCodePic(Blob organizingInstitutionBarCodePic){
        this.organizingInstitutionBarCodePic = organizingInstitutionBarCodePic;
    }

    public Blob getBusinessLicensePic(){
        return businessLicensePic;
    }

    public void setBusinessLicensePic(Blob businessLicensePic){
        this.businessLicensePic = businessLicensePic;
    }

    public Blob getOtherPic(){
        return otherPic;
    }

    public void setOtherPic(Blob otherPic){
        this.otherPic = otherPic;
    }

    @Override public String toString(){
        final StringBuffer sb = new StringBuffer("MerchantPics{");
        sb.append("merchantId=").append(merchantId);
        sb.append(", organizingInstitutionBarCodePic=").append(organizingInstitutionBarCodePic);
        sb.append(", businessLicensePic=").append(businessLicensePic);
        sb.append(", otherPic=").append(otherPic);
        sb.append('}');
        return sb.toString();
    }
}
