package com.lanxi.couponcode.impl.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lanxi.couponcode.spi.abstractentity.AbstractCouponCode;
import com.lanxi.couponcode.spi.consts.enums.ClearStatus;
import com.lanxi.couponcode.spi.consts.enums.CouponCodeStatus;
import com.lanxi.couponcode.spi.consts.enums.GenerateType;
import com.lanxi.couponcode.spi.consts.enums.VerificationType;

import java.io.Serializable;
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
	@TableField(value = "code_status")
	private CouponCodeStatus codeStatus;
	/**串码所属商户编号*/
	@TableField("merchant_id")
	private Long merchantId;
	/**串码所属商户名称*/
	@TableField("merchant_name")
	private String merchantName;
	/**清算状态*/
	@TableField("clear_status")
	private ClearStatus clearStatus;
	/**结算时间*/
	@TableField("clear_time")
	private String clearTime;
	/**串码终止时间,即串码 核销|过期|销毁 的时间*/
	@TableField("final_time")
	private String finalTime;
	/**生成原因*/
	@TableField("reason")
	private String reason;
	/**渠道*/
	@TableField("channel")
	private Integer channel;
	/**核销方式*/
	@TableField("verification_type")
	private VerificationType verificationType;
	/**生成方式*/
	@TableField("generate_type")
	private GenerateType generateType;
	/**核销时间*/
	@TableField("verify_time")
	private String verifyTime;
	@Override
	public Serializable pkVal(){
		return this.codeId;
	}

	public boolean isExisted(){
		return isExisted(this.merchantId,this.code);
	}

	public boolean isExisted(long merchantId, long code){
		List<AbstractCouponCode> codes=this.selectList(new EntityWrapper<CouponCode>().eq("merchant_id",merchantId).eq("code",code));
		if(codes==null||codes.isEmpty())
			return false;
		return true;
	}

	public Integer getChannel() {
		return channel;
	}

	public void setChannel(Integer channel) {
		this.channel = channel;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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

	public String getVerificationType() {
		return verificationType==null?null:verificationType.getValue();
	}

	public void setVerificationType(VerificationType verificationType) {
		this.verificationType = verificationType;
	}
	public void setVerificationType(String verificationType) {
		this.verificationType = VerificationType.getType(verificationType);
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
				", clearStatus=" + clearStatus +
				", clearTime='" + clearTime + '\'' +
				", finalTime='" + finalTime + '\'' +
				", reason='" + reason + '\'' +
				", channel=" + channel +
				", verificationType=" + verificationType +
				", generateType=" + generateType +
				", verifyTime='" + verifyTime + '\'' +
				'}';
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

	public String getClearStatus() {
		return clearStatus==null?null:clearStatus.getValue();
	}
	public ClearStatus getClearStatusEnum() {
		return clearStatus;
	}

	public void setClearStatus(ClearStatus clearStatus) {
		this.clearStatus = clearStatus;
	}
	public void setClearStatus(String clearStatus) {
		this.clearStatus = ClearStatus.getType(clearStatus);
	}

	public String getClearTime() {
		return clearTime;
	}

	public void setClearTime(String clearTime) {
		this.clearTime = clearTime;
	}


	public String getFinalTime() {
		return finalTime;
	}

	public void setFinalTime(String finalTime) {
		this.finalTime = finalTime;
	}

	public String getGenerateType() {
		return generateType==null?null:generateType.getValue();
	}

	public void setGenerateType(GenerateType generateType) {
		this.generateType = generateType;
	}
	public void setGenerateType(String generateType) {
		this.generateType = GenerateType.getType(generateType);
	}
	public String getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(String verifyTime) {
		this.verifyTime = verifyTime;
	}
}
