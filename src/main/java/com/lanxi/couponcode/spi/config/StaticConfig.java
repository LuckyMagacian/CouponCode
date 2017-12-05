package com.lanxi.couponcode.spi.config;

import com.lanxi.couponcode.spi.defaultInterfaces.GetFieldValue;
/**
 * 静态配置类<br>
 * @author yangyuanjian
 *
 */

public class StaticConfig implements ConstConfig,GetFieldValue{
	/**服务器编号*/
	protected String machineNum;
	
	/**账户id初始值*/
	protected String accountIdDefaultValue;
	/**商品id初始值*/
	protected String commdityIdDefaultValue;
	/**串码id初始值*/
	protected String couponIdDefaultValue;
	/**商户id初始值*/
	protected String merchantIdDefaultValue;
	/**消息id初始值*/
	protected String messageIdDefaultValue;
	/**操作记录id初始值*/
	protected String operateRecordIdDefaultValue;
	/**请求id初始值*/
	protected String requestIdDefaultValue;
	/**门店id初始值*/
	protected String shopIdDefaultValue;
	/**统计id初始值*/
	protected String statsticIdDefaultValue;
	/**用户id初始值*/
	protected String userIdDefaultValue;
	/**核销记录id初始值*/
	protected String verificationIdDefaultValue;

	
	
	
	
	
	
	
	
	public String getMachineNum() {
		return machineNum;
	}
	public String getAccountIdDefaultValue() {
		return accountIdDefaultValue;
	}
	public String getCommdityIdDefaultValue() {
		return commdityIdDefaultValue;
	}
	public String getCouponIdDefaultValue() {
		return couponIdDefaultValue;
	}
	public String getMerchantIdDefaultValue() {
		return merchantIdDefaultValue;
	}
	public String getMessageIdDefaultValue() {
		return messageIdDefaultValue;
	}
	public String getOperateRecordIdDefaultValue() {
		return operateRecordIdDefaultValue;
	}
	public String getRequestIdDefaultValue() {
		return requestIdDefaultValue;
	}
	public String getShopIdDefaultValue() {
		return shopIdDefaultValue;
	}
	public String getStatsticIdDefaultValue() {
		return statsticIdDefaultValue;
	}
	public String getUserIdDefaultValue() {
		return userIdDefaultValue;
	}
	public String getVerificationIdDefaultValue() {
		return verificationIdDefaultValue;
	}
	protected void setMachineNum(String machineNum) {
		this.machineNum = machineNum;
	}
	protected void setAccountIdDefaultValue(String accountIdDefaultValue) {
		this.accountIdDefaultValue = accountIdDefaultValue;
	}
	protected void setCommdityIdDefaultValue(String commdityIdDefaultValue) {
		this.commdityIdDefaultValue = commdityIdDefaultValue;
	}
	protected void setCouponIdDefaultValue(String couponIdDefaultValue) {
		this.couponIdDefaultValue = couponIdDefaultValue;
	}
	protected void setMerchantIdDefaultValue(String merchantIdDefaultValue) {
		this.merchantIdDefaultValue = merchantIdDefaultValue;
	}
	protected void setMessageIdDefaultValue(String messageIdDefaultValue) {
		this.messageIdDefaultValue = messageIdDefaultValue;
	}
	protected void setOperateRecordIdDefaultValue(String operateRecordIdDefaultValue) {
		this.operateRecordIdDefaultValue = operateRecordIdDefaultValue;
	}
	protected void setRequestIdDefaultValue(String requestIdDefaultValue) {
		this.requestIdDefaultValue = requestIdDefaultValue;
	}
	protected void setShopIdDefaultValue(String shopIdDefaultValue) {
		this.shopIdDefaultValue = shopIdDefaultValue;
	}
	protected void setStatsticIdDefaultValue(String statsticIdDefaultValue) {
		this.statsticIdDefaultValue = statsticIdDefaultValue;
	}
	protected void setUserIdDefaultValue(String userIdDefaultValue) {
		this.userIdDefaultValue = userIdDefaultValue;
	}
	protected void setVerificationIdDefaultValue(String verificationIdDefaultValue) {
		this.verificationIdDefaultValue = verificationIdDefaultValue;
	}
	
	
}
