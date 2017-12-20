package com.lanxi.couponcode.spi.util;

import java.util.List;

/**
 * @author wuxiaobo
 */
public class MsgOrderInfoHeadBean {

    private String VER;// 版本号
    private String MsgNo;// 报文编号
    private String CHKDate;// 清算日期
    private String WorkDate;// 交易日期
    private String WorkTime;// 交易时间
    private String ADD;// 地区号
    private String SRC;// 发起节点代码
    private String DES;// 接收节点代码
    private String APP;// 应用名称
    private String MsgID;// 交易序号
    private String Reserve;// 预留字段
    private String Sign;// 签名
    private String ResCode;// 返回码
    private String ResMsg;// 返回信息
    private String OrgWorkDate;// 原交易日期
    private String OrgMsgID;// 原交易序号
    private String Phone;// 手机号
    private String Remark;//备注
    private String Type;//商品类型
    private String SkuCode;//商品编号
    private String Count;//商品数量
    private String TotalAmt;//总发生额
    private String Status;//交易结果
    private String Amt;//发生额
    private List<String> Code;//电子券串码集合
    private String EndTime;//串码截止日期
    private String StartDate;//交易起始日期
    private String EndDate;//交易终止日期

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

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

    public void setResMsg(String resMsg) {
        ResMsg = resMsg;
    }

    public String getOrgWorkDate() {
        return OrgWorkDate;
    }

    public void setOrgWorkDate(String orgWorkDate) {
        OrgWorkDate = orgWorkDate;
    }

    public String getOrgMsgID() {
        return OrgMsgID;
    }

    public void setOrgMsgID(String orgMsgID) {
        OrgMsgID = orgMsgID;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
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

    public String getTotalAmt() {
        return TotalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        TotalAmt = totalAmt;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getAmt() {
        return Amt;
    }

    public void setAmt(String amt) {
        Amt = amt;
    }

    public List<String> getCode() {
        return Code;
    }

    public void setCode(List<String> code) {
        Code = code;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

}
