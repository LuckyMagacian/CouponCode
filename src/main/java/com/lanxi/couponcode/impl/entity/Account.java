package com.lanxi.couponcode.impl.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lanxi.couponcode.spi.abstractentity.AbstractAccount;
import com.lanxi.couponcode.spi.consts.enums.AccountStatus;
import com.lanxi.couponcode.spi.consts.enums.AccountType;

@TableName("account")
public class Account extends AbstractAccount{
	/**账户编号*/
	@TableId("account_id")
	private Long accountId;
	/**用户手机号码*/
	@TableField("phone")
	private String phone;
	/**登录密码*/
	@TableField("password")
	private String password;
	/**用户姓名*/
	@TableField("user_name")
	private String userName;
	/**账户状态*/
	@TableField("status")
	private AccountStatus status;
	/**账户类型*/
	@TableField("type")
	private AccountType type;

	/**所属商户编号*/
	@TableField("merchant_id")
	private Long merchantId;
	/**所属商户名称*/
	@TableField("merchant_name")
	private String merchantName;

	/**门店编号*/
	@TableField("shop_id")
	private Long shopId;
	/**门店名称*/
	@TableField("shop_name")
	private String shopName;

	/**添加者编号*/
	@TableField("add_by_id")
	private Long addById;
	/**添加者姓名*/
	@TableField("add_by_name")
	private String addByName;
	/**添加时间*/
	@TableField("add_time")
	private String addTime;

	/**请求者编号*/
	@TableField("requester_id")
	private Long requesterId;
	/**请求者姓名*/
	@TableField("requester_name")
	private String requesterName;
	/**请求编号*/
	@TableField("request_id")
	private Long requestId;
	/**请求时间*/
	@TableField("request_time")
	private String requestTime;



	/**审核者编号*/
	@TableField("check_by_id")
	private Long checkById;
	/**审核者姓名*/
	@TableField("check_name")
	private String checkName;
	/**审核时间*/
	@TableField("check_time")
	private String checkTime;
	/**备注*/
	@TableField("remark")
	private String remark;

	/**该方法用于指定主键*/
	@Override
	protected Serializable pkVal() {
		return this.accountId;
	}

	@Override
	public String toString() {
		return "Account{" +
				"accountId=" + accountId +
				", phone='" + phone + '\'' +
				", password='" + password + '\'' +
				", userName='" + userName + '\'' +
				", status=" + status +
				", type=" + type +
				", merchantId=" + merchantId +
				", merchantName='" + merchantName + '\'' +
				", shopId=" + shopId +
				", shopName='" + shopName + '\'' +
				", addById=" + addById +
				", addByName='" + addByName + '\'' +
				", addTime='" + addTime + '\'' +
				", requesterId=" + requesterId +
				", requesterName='" + requesterName + '\'' +
				", requestId=" + requestId +
				", requestTime='" + requestTime + '\'' +
				", checkById=" + checkById +
				", checkName='" + checkName + '\'' +
				", checkTime='" + checkTime + '\'' +
				", remark='" + remark + '\'' +
				'}';
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
	}

	public AccountType getType() {
		return type;
	}

	public void setType(AccountType type) {
		this.type = type;
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

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Long getAddById() {
		return addById;
	}

	public void setAddById(Long addById) {
		this.addById = addById;
	}

	public String getAddByName() {
		return addByName;
	}

	public void setAddByName(String addByName) {
		this.addByName = addByName;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public Long getRequesterId() {
		return requesterId;
	}

	public void setRequesterId(Long requesterId) {
		this.requesterId = requesterId;
	}

	public String getRequesterName() {
		return requesterName;
	}

	public void setRequesterName(String requesterName) {
		this.requesterName = requesterName;
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public Long getCheckById() {
		return checkById;
	}

	public void setCheckById(Long checkById) {
		this.checkById = checkById;
	}

	public String getCheckName() {
		return checkName;
	}

	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}

	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
