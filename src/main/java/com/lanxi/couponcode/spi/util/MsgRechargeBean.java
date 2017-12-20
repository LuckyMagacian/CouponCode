package com.lanxi.couponcode.spi.util;


/**
 * 购买电子券请求实体类
 *
 * @author wuxiaobo
 */
public class MsgRechargeBean extends MsgHeadBean implements Cloneable {
    private String Phone = "";//手机号码
    private String Type = "";//商品类型
    private String SkuCode = "";//商品编号
    private String Count = "";//商品数量
    private String NeedSend = "";//是否需要发送    0-需要，1-不需要
    private String Remark = "";//备注
    private String SerialNum = "";//平台流水号

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getSkuCode() {
        return SkuCode;
    }

    public void setSkuCode(String skuCode) {
        SkuCode = skuCode;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    public String getNeedSend() {
        return NeedSend;
    }

    public void setNeedSend(String needSend) {
        NeedSend = needSend;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getSerialNum() {
        return SerialNum;
    }

    public void setSerialNum(String serialNum) {
        SerialNum = serialNum;
    }

    @Override
    public Object clone() {
        try {
            return (MsgRechargeBean) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
