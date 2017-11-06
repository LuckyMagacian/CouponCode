package com.lanxi.couponcode.spi.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lanxi.couponcode.spi.abstractentity.AbstractUser;
import com.lanxi.couponcode.spi.consts.enums.AccountStatus;
import com.lanxi.couponcode.spi.consts.enums.AccountType;
@Deprecated
@TableName("users")
public class User extends AbstractUser{
	/**serializable number*/
	@TableField(exist=false)
	private static final long serialVersionUID = -2108648502880987111L;
	@TableId("user_id")
	private String userId;
	/**用户手机号 用户表唯一索引*/
	private String phone;
	/**用户密码*/
	private String password;
	/**姓名*/
	private String name;
	/**账户类型<br> see {@link AccountType}*/
	private AccountType type;
	/**商户编号 */
	private String merchantId;
	/**商户名称*/
	private String merchantName;
	/**门店编号*/
	private String shopId;
	/**门店名称*/
	private String shopName;
	/**创建时间*/
	private String createTime;
	/**审核人员*/
	private String checkUserId;
	/**请求人员*/
	private String requestUserId;
	/**账户状态<br> {@link AccountStatus}*/
	private AccountStatus accountStatus;
	/** 备注*/
	private String remark;
	/**版本号*/
	private Integer version;
	@Override
	protected Serializable pkVal() {
		return this.userId;
	}

	
	
	
	
	
	
	
	public String getUserId() {
		return userId;
	}



	public void setUserId(String userId) {
		this.userId = userId;
	}



	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AccountType getType() {
		return type;
	}

	public void setType(AccountType type) {
		this.type = type;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCheckUserId() {
		return checkUserId;
	}

	public void setCheckUserId(String checkUserId) {
		this.checkUserId = checkUserId;
	}

	public String getRequestUserId() {
		return requestUserId;
	}

	public void setRequestUserId(String requestUserId) {
		this.requestUserId = requestUserId;
	}

	public AccountStatus getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(AccountStatus accountStatus) {
		this.accountStatus = accountStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "User [phone=" + phone + ", password=" + password + ", name=" + name + ", type=" + type + ", merchantId="
				+ merchantId + ", merchantName=" + merchantName + ", shopId=" + shopId + ", shopName=" + shopName
				+ ", createTime=" + createTime + ", checkUserId=" + checkUserId + ", requestUserId=" + requestUserId
				+ ", accountStatus=" + accountStatus + ", remark=" + remark + ", version=" + version + "]";
	}
	
	
	
	
}
