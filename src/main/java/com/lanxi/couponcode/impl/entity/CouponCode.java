package com.lanxi.couponcode.impl.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lanxi.couponcode.spi.consts.enums.CouponCodeStatus;
import com.lanxi.couponcode.spi.abstractentity.AbstractCouponCode;

import java.io.Serializable;
import java.sql.JDBCType;
import java.util.List;

@TableName("coupon_code")
public class CouponCode extends AbstractCouponCode{
	/**串码id*/
	@TableId(value = "code_id",type = IdType.ID_WORKER)
	private Long codeId;
	/**串码*/
	@TableField("code")
	private Long code;
	/**创建时间*/
	@TableField("create_time")
	private String createTime;
	/**过期时间*/
	@TableField("over_time")
	private String overTime;
	/**有效期(单位天)*/
	@TableField("life_time")
	private Integer lifeTime;
	/**商品id*/
	@TableField("commodity_id")
	private Long commodityId;
	/**商品名称*/
	@TableField("commodity_name")
	private String commodityName;
	/**商品详情*/
	@TableField("commodity_info")
	private String commodityInfo;
	/**核销记录id*/
	@TableField("destroyed_record_id")
	private Long destroyRecordId;
	/**串码状态*/
	@TableField(value = "coupon_code_status")
	private CouponCodeStatus codeStatus;
	/**串码所属商户编号*/
	@TableField("merchant_id")
	private Long merchantId;
	/**串码所属商户名称*/
	@TableField("merchant_name")
	private String merchantName;
	@Override
	public Serializable pkVal(){
		return this.codeId;
	}

	public boolean isExisted(){
		return isExisted(this.merchantId,this.code);
	}

	public boolean isExisted(long merchantId,long code){
		List<AbstractCouponCode> codes=this.selectList(new EntityWrapper<CouponCode>().eq("merchant_id",merchantId).eq("code",code));
		if(codes==null||codes.isEmpty())
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CouponCode{" +
				"codeId=" + codeId +
				", code=" + code +
				", createTime='" + createTime + '\'' +
				", overTime='" + overTime + '\'' +
				", lifeTime=" + lifeTime +
				", commodityId=" + commodityId +
				", commodityName='" + commodityName + '\'' +
				", commodityInfo='" + commodityInfo + '\'' +
				", destroyRecordId=" + destroyRecordId +
				", codeStatus=" + codeStatus +
				", merchantId=" + merchantId +
				", merchantName='" + merchantName + '\'' +
				'}';
	}

	public Long getCodeId() {
		return codeId;
	}
	public Long getCode() {
		return code;
	}
	public String getCreateTime() {
		return createTime;
	}
	public String getOverTime() {
		return overTime;
	}
	public Integer getLifeTime() {
		return lifeTime;
	}
	public Long getDestroyRecordId() {
		return destroyRecordId;
	}
	public String getCodeStatus() {
		return codeStatus==null?null:codeStatus.getValue();
	}
	public void setCodeId(Long codeId) {
		this.codeId = codeId;
	}
	public void setCode(Long code) {
		this.code = code;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public void setOverTime(String overTime) {
		this.overTime = overTime;
	}
	public void setLifeTime(Integer lifeTime) {
		this.lifeTime = lifeTime;
	}
	public void setCommodityId(Long commodityId) {
		this.commodityId = commodityId;
	}
	public void setDestroyRecordId(Long destroyRecordId) {
		this.destroyRecordId = destroyRecordId;
	}
	public void setCodeStatus(CouponCodeStatus codeStatus) {
		this.codeStatus = codeStatus;
	}
	public void setCodeStatus(String codeStatus) {
		this.codeStatus = CouponCodeStatus.getType(codeStatus);
	}
	public Long getCommodityId() {
		return commodityId;
	}



	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public String getCommodityInfo() {
		return commodityInfo;
	}

	public void setCommodityInfo(String commodityInfo) {
		this.commodityInfo = commodityInfo;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
}
