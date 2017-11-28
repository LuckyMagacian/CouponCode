package com.lanxi.couponcode.impl.entity;

import java.io.Serializable;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lanxi.couponcode.spi.abstractentity.AbstractMerchant;
import com.lanxi.couponcode.spi.consts.enums.MerchantStatus;
/**
 * 商户类
 * @author wuxiaobo
 *
 */
@TableName("merchants")
public class Merchant extends AbstractMerchant{
	
	/*商户id*/
	@TableId("merchant_id")
	private Long merchantId;
	
	/*商户名称*/
	@TableField("merchant_name")
	private String merchantName;
	/*商户状态*/
	@TableField("merchant_status")
	private MerchantStatus merchantStatus;
	/*信息提交时间*/
	@TableField("create_time")
	private String createTime;
	/*办公地址*/
	@TableField("workaddress")
	private String workAddress;
	/*办公详细地址*/
	@TableField("minute_workaddress")
	private String minuteWorkAddress;
	/*服务或商品说明*/
	@TableField("serve_explain")
	private String serveExplain;
	/*注册地址*/
	@TableField("registeraddress")
	private String registerAddress;
	/*注册详细地址*/
	@TableField("minute_registeraddress")
	private String minuteRegisterAddress;
	/*组织机构代码*/
	@TableField("oraganizingcode")
	private String oraganizingCode;
	/*营业执照编号*/
	@TableField("chartercode")
	private String charterCode;
	/*法定代表人/商户负责人姓名*/
	@TableField("principal")
	private String principal;
	/*联系人姓名*/
	@TableField("linkman")
	private String linkMan;
	/*联系人手机号码*/
	@TableField("linkman_phone")
	private String linkManPhone;
	/*客服电话*/
	@TableField("servicetel")
	private String serviceTel;
	/*常用邮箱*/
	@TableField("email")
	private String email;
	/*组织机构代码证*/
	@TableField("organizing_institution_bar_code_pic")
	private String organizingInstitutionBarCodePic;
	/*工商营业执照*/
	@TableField("business_license_pic")
	private String businessLicensePic;
	/*其他证明材料*/
	@TableField("other_pic")
	private String otherPic;
	@Override
	protected Serializable pkVal() {
		// TODO Auto-generated method stub
		return this.merchantId;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMerchantStatus() {
		return merchantStatus==null?null:merchantStatus.getValue();
		
	}
	public void setMerchantStatus(String merchantStatus) {
		this.merchantStatus = MerchantStatus.getType(merchantStatus);
	}
	public void setMerchantStatus(MerchantStatus merchantStatus) {
		this.merchantStatus = merchantStatus;
	}



	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getWorkAddress() {
		return workAddress;
	}

	public void setWorkAddress(String workAddress) {
		this.workAddress = workAddress;
	}

	public String getMinuteWorkAddress() {
		return minuteWorkAddress;
	}

	public void setMinuteWorkAddress(String minuteWorkAddress) {
		this.minuteWorkAddress = minuteWorkAddress;
	}

	public String getServeExplain() {
		return serveExplain;
	}

	public void setServeExplain(String serveExplain) {
		this.serveExplain = serveExplain;
	}

	public String getRegisterAddress() {
		return registerAddress;
	}

	public void setRegisterAddress(String registerAddress) {
		this.registerAddress = registerAddress;
	}

	public String getMinuteRegisterAddress() {
		return minuteRegisterAddress;
	}

	public void setMinuteRegisterAddress(String minuteRegisterAddress) {
		this.minuteRegisterAddress = minuteRegisterAddress;
	}

	public String getOraganizingCode() {
		return oraganizingCode;
	}

	public void setOraganizingCode(String oraganizingCode) {
		this.oraganizingCode = oraganizingCode;
	}

	public String getCharterCode() {
		return charterCode;
	}

	public void setCharterCode(String charterCode) {
		this.charterCode = charterCode;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public String getLinkManPhone() {
		return linkManPhone;
	}

	public void setLinkManPhone(String linkManPhone) {
		this.linkManPhone = linkManPhone;
	}

	public String getServiceTel() {
		return serviceTel;
	}

	public void setServiceTel(String serviceTel) {
		this.serviceTel = serviceTel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOrganizingInstitutionBarCodePic() {
		return organizingInstitutionBarCodePic;
	}

	public void setOrganizingInstitutionBarCodePic(String organizingInstitutionBarCodePic) {
		this.organizingInstitutionBarCodePic = organizingInstitutionBarCodePic;
	}

	public String getBusinessLicensePic() {
		return businessLicensePic;
	}

	public void setBusinessLicensePic(String businessLicensePic) {
		this.businessLicensePic = businessLicensePic;
	}

	public String getOtherPic() {
		return otherPic;
	}

	public void setOtherPic(String otherPic) {
		this.otherPic = otherPic;
	}

	@Override
	public String toString() {
		return "Merchant [merchantId=" + merchantId + ", merchantName=" + merchantName + ", merchantStatus="
				+ merchantStatus +  ", createTime=" + createTime + ", workAddress="
				+ workAddress + ", minuteWorkAddress=" + minuteWorkAddress + ", serveExplain=" + serveExplain
				+ ", registerAddress=" + registerAddress + ", minuteRegisterAddress=" + minuteRegisterAddress
				+ ", oraganizingCode=" + oraganizingCode + ", charterCode=" + charterCode + ", principal=" + principal
				+ ", linkMan=" + linkMan + ", linkManPhone=" + linkManPhone + ", serviceTel=" + serviceTel + ", email="
				+ email + ", organizingInstitutionBarCodePic=" + organizingInstitutionBarCodePic
				+ ", businessLicensePic=" + businessLicensePic + ", otherPic=" + otherPic + "]";
	}
	
}
