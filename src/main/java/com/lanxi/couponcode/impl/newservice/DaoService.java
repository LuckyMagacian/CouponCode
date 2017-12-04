package com.lanxi.couponcode.impl.newservice;

import com.lanxi.couponcode.impl.dao.*;

/**
 * dao接口
 * Created by yangyuanjian on 2017/11/1.
 */
  public interface DaoService {
      AccountDao getAccountDao();

      BankApiDao getBankApiDao();

      CodeAlgorithmDao getCodeAlgorithmDao();

      CommodityDao getCommodityDao();

      CouponCodeDao getCouponCodeDao();

      MerchantDao getMerchantDao();

      OperateRecordDao getOperateRecordDao();

      OrderDao getOrderDao();

      RequestDao getRequestDao();

      ShopDao getShopDao();

      VerificationRecordDao getVerificationRecordDao();

      ClearDailyRecordDao getClearDailyRecordDao();

      ClearRecordDao getClearRecordDao();
    
}
