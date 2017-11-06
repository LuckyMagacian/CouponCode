package com.lanxi.couponcode.impl.service;

import com.lanxi.couponcode.impl.dao.*;

/**
 * dao接口
 * Created by yangyuanjian on 2017/11/1.
 */
public interface DaoService {
    public AccountDao getAccountDao();

    public BankApiDao getBankApiDao();

    public CodeAlgorithmDao getCodeAlgorithmDao();

    public CommodityDao getCommodityDao();

    public CouponCodeDao getCouponCodeDao();

    public EnterpriseDao getEnterpriseDao();

    public MerchantClearDao getMerchantClearDao();

    public MerchantDao getMerchantDao();

    public MessageDao getMessageDao();

    public MessageObserverDao getMessageObserverDao();

    public OperateRecordDao getOperateRecordDao();

    public OrderDao getOrderDao();

    public RecordDao getRecordDao();

    public RequestDao getRequestDao();

    public ShopDao getShopDao();

    public StatisticDao getStatisticDao();

    public SubOrderDao getSubOrderDao();

    public VerificationRecordDao getVerificationRecordDao();
}
