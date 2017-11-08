package com.lanxi.couponcode.impl.config;

public interface ConstConfig {
	//-------------------------------about project---------------------------------
	/**公司名称*/
	String ORG_NAME="com.lanxi";
	/**项目名称*/
	String APP_NAME="couponcode";
	/**版本*/
	String VERSION="1.0";
	/**项目唯一*/
	String ARTIFCAT=ORG_NAME+"-"+APP_NAME+"-"+VERSION+"-";
	//-----------------------------------------------------------------------------
	/**配置文件路径*/
	String STATIC_CONFIG_PATH="classpath:properties";
	//--------------------------------id key-------------------------------------
	/**redis中账户key*/
	String ACCOUNT_ID_KEY=ARTIFCAT+"accountID";
	/**redis中商品key*/
	String COMMDITY_ID_KEY=ARTIFCAT+"commdityID";
	/**redis中串码key*/
	String COUPON_ID_KEY=ARTIFCAT+"couponID";
	/**redis中商户key*/
	String MERCHANT_ID_KEY=ARTIFCAT+"merchantID";
	/**redis中消息key*/
	String MESSAGE_ID_KEY=ARTIFCAT+"messageID";
	/**redis中操作记录key*/
	String OPERATE_RECORD__ID_KEY=ARTIFCAT+"operateRecordID";
	/**redis中请求key*/
	String REQUEST_ID_KEY=ARTIFCAT+"requestID";
	/**redis中门店key*/
	String SHOP_ID_KEY=ARTIFCAT+"shopID";
	/**redis中统计key*/
	String STATISTIC_ID_KEY=ARTIFCAT+"statisticID";
	/**redis中用户key*/
	String USER_ID_KEY=ARTIFCAT+"userID";
	/**redis中核销记录key*/
	String VERIFICATION_ID_KEY=ARTIFCAT+"verificationID";
	//----------------------------------fun key---------------------------------------
	
	//----------------------------------default value---------------------------------
	/**id默认值*/
	long ID_DEFAULT_VALUE=1000000L;
	/**机器编号  默认值*/
	String MACHINE_NUMER_DEFAULT_VALUE="1";
	/**默认分页参数*/
	Integer DEFAULT_PAGE=1;
	Integer DEFAULT_PAGE_SIZE=10;
	Long INVALID_LONG=-1L;
}

