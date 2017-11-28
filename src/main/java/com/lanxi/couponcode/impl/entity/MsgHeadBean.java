package com.lanxi.couponcode.impl.entity;

/**
 * 
 * @author wuxiaobo
 *
 */
public class MsgHeadBean {

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
	
}
