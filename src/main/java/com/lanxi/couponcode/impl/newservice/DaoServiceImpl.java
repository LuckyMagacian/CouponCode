package com.lanxi.couponcode.impl.newservice;

import com.lanxi.couponcode.impl.dao.*;
import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.util.utils.LoggerUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
/**
 * dao接口实现类
 * Created by yangyuanjian on 2017/11/1.
 */
@Service("daoService")
@EasyLog(LoggerUtil.LogLevel.DEBUG)
public class DaoServiceImpl implements DaoService {
    @Resource
    private AccountDao accountDao;
    private BankApiDao bankApiDao;
    @Resource
    private CodeAlgorithmDao codeAlgorithmDao;
    @Resource
    private CommodityDao commodityDao;
    @Resource
    private CouponCodeDao couponCodeDao;
    @Resource
    private MerchantDao merchantDao;
    @Resource
    private OperateRecordDao operateRecordDao;
    @Resource
    private OrderDao orderDao;
    @Resource
    private RequestDao requestDao;
    @Resource
    private ShopDao shopDao;
    @Resource
    private VerificationRecordDao verificationRecordDao;
    @Resource
    private ClearRecordDao clearRecordDao;
    @Resource
    private ClearDailyRecordDao clearDailyRecordDao;
//    @Resource
//    private MerchantPicsDao merchantPicsDao;

    public AccountDao getAccountDao() {
        return accountDao;
    }

    public BankApiDao getBankApiDao() {
        return bankApiDao;
    }

    public CodeAlgorithmDao getCodeAlgorithmDao() {
        return codeAlgorithmDao;
    }

    public CommodityDao getCommodityDao() {
        return commodityDao;
    }

    public CouponCodeDao getCouponCodeDao() {
        return couponCodeDao;
    }

    public MerchantDao getMerchantDao() {
        return merchantDao;
    }

    public OperateRecordDao getOperateRecordDao() {
        return operateRecordDao;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

    public RequestDao getRequestDao() {
        return requestDao;
    }

    public ShopDao getShopDao() {
        return shopDao;
    }

    public VerificationRecordDao getVerificationRecordDao() {
        return verificationRecordDao;
    }

    @Override
    public ClearDailyRecordDao getClearDailyRecordDao() {
        return clearDailyRecordDao;
    }

    @Override
    public ClearRecordDao getClearRecordDao() {
        return clearRecordDao;
    }

//    @Override public MerchantPicsDao getMerchantPicsDao() {
//        return merchantPicsDao;
//    }

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public void setBankApiDao(BankApiDao bankApiDao) {
        this.bankApiDao = bankApiDao;
    }

    public void setCodeAlgorithmDao(CodeAlgorithmDao codeAlgorithmDao) {
        this.codeAlgorithmDao = codeAlgorithmDao;
    }

    public void setCommodityDao(CommodityDao commodityDao) {
        this.commodityDao = commodityDao;
    }

    public void setCouponCodeDao(CouponCodeDao couponCodeDao) {
        this.couponCodeDao = couponCodeDao;
    }

    public void setMerchantDao(MerchantDao merchantDao) {
        this.merchantDao = merchantDao;
    }

    public void setOperateRecordDao(OperateRecordDao operateRecordDao) {
        this.operateRecordDao = operateRecordDao;
    }

    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public void setRequestDao(RequestDao requestDao) {
        this.requestDao = requestDao;
    }

    public void setShopDao(ShopDao shopDao) {
        this.shopDao = shopDao;
    }

    public void setVerificationRecordDao(VerificationRecordDao verificationRecordDao) {
        this.verificationRecordDao = verificationRecordDao;
    }

    public void setClearRecordDao(ClearRecordDao clearRecordDao) {
        this.clearRecordDao = clearRecordDao;
    }

    public void setClearDailyRecordDao(ClearDailyRecordDao clearDailyRecordDao) {
        this.clearDailyRecordDao = clearDailyRecordDao;
    }

//    public void setMerchantPicsDao(MerchantPicsDao merchantPicsDao) {
//        this.merchantPicsDao = merchantPicsDao;
//    }
}
