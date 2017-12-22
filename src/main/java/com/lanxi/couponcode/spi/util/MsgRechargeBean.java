package com.lanxi.couponcode.spi.util;


/**
 * 购买电子券请求实体类
 *
 * @author wuxiaobo
 */
public class MsgRechargeBean implements Cloneable {
    private String VER="";// 版本号
    private String MsgNo="";// 报文编号
    private String CHKDate="";// 清算日期
    private String WorkDate="";// 交易日期
    private String WorkTime="";// 交易时间
    private String ADD="";// 地区号
    private String SRC="";// 发起节点代码
    private String DES="";// 接收节点代码
    private String APP="";// 应用名称
    private String MsgID="";// 交易序号
    private String Reserve="";// 预留字段
    private String Sign="";// 签名
    private String ResCode="";// 返回码
    private String ResMsg="";// 返回信息

    private String Phone = "";//手机号码
    private String Type = "";//商品类型
    private String SkuCode = "";//商品编号
    private String Count = "0";//商品数量
    private String NeedSend = "";//是否需要发送    0-需要，1-不需要
    private String Remark = "";//备注
    private String SerialNum = "";//平台流水号
    public String getVER() {
        return VER;
    }

    public void setVER(String vER) {
        VER = vER;
    }

    public String getMsgNo() {
        return MsgNo;
    }

    public void setMsgNo(String msgNo) {
        MsgNo = msgNo;
    }

    public String getCHKDate() {
        return CHKDate;
    }

    public void setCHKDate(String cHKDate) {
        CHKDate = cHKDate;
    }

    public String getWorkDate() {
        return WorkDate;
    }

    public void setWorkDate(String workDate) {
        WorkDate = workDate;
    }

    public String getWorkTime() {
        return WorkTime;
    }

    public void setWorkTime(String workTime) {
        WorkTime = workTime;
    }

    public String getADD() {
        return ADD;
    }

    public void setADD(String aDD) {
        ADD = aDD;
    }

    public String getSRC() {
        return SRC;
    }

    public void setSRC(String sRC) {
        SRC = sRC;
    }

    public String getDES() {
        return DES;
    }

    public void setDES(String dES) {
        DES = dES;
    }

    public String getAPP() {
        return APP;
    }

    public void setAPP(String aPP) {
        APP = aPP;
    }

    public String getMsgID() {
        return MsgID;
    }

    public void setMsgID(String msgID) {
        MsgID = msgID;
    }

    public String getReserve() {
        return Reserve;
    }

    public void setReserve(String reserve) {
        Reserve = reserve;
    }

    public String getSign() {
        return Sign;
    }

    public void setSign(String sign) {
        Sign = sign;
    }

    public String getResCode() {
        return ResCode;
    }

    public void setResCode(String resCode) {
        ResCode = resCode;
    }

    public String getResMsg() {
        return ResMsg;
    }

    public void setResMsg(String ResMsg) {
        this.ResMsg = ResMsg;
    }


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
    public String toString() {
        return "MsgRechargeBean{" +
                "VER='" + VER + '\'' +
                ", MsgNo='" + MsgNo + '\'' +
                ", CHKDate='" + CHKDate + '\'' +
                ", WorkDate='" + WorkDate + '\'' +
                ", WorkTime='" + WorkTime + '\'' +
                ", ADD='" + ADD + '\'' +
                ", SRC='" + SRC + '\'' +
                ", DES='" + DES + '\'' +
                ", APP='" + APP + '\'' +
                ", MsgID='" + MsgID + '\'' +
                ", Reserve='" + Reserve + '\'' +
                ", Sign='" + Sign + '\'' +
                ", ResCode='" + ResCode + '\'' +
                ", ResMsg='" + ResMsg + '\'' +
                ", Phone='" + Phone + '\'' +
                ", Type='" + Type + '\'' +
                ", SkuCode='" + SkuCode + '\'' +
                ", Count='" + Count + '\'' +
                ", NeedSend='" + NeedSend + '\'' +
                ", Remark='" + Remark + '\'' +
                ", SerialNum='" + SerialNum + '\'' +
                '}';
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
