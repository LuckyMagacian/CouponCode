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
    private CommodityDao commodityDao;
    @Resource
    private CouponCodeDao couponCodeDao;
    private EnterpriseDao enterpriseDao;
    private MerchantClearDao merchantClearDao;
    @Resource
    private MerchantDao merchantDao;
    private MessageDao messageDao;
    private MessageObserverDao messageObserverDao;
    @Resource
    private OperateRecordDao operateRecordDao;
    @Resource
    private OrderDao orderDao;
    private RecordDao recordDao;
    private RequestDao requestDao;
    @Resource
    private ShopDao shopDao;
    private StatisticDao statisticDao;
    private VerificationRecordDao verificationRecordDao;

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

    public EnterpriseDao getEnterpriseDao() {
        return enterpriseDao;
    }

    public MerchantClearDao getMerchantClearDao() {
        return merchantClearDao;
    }

    public MerchantDao getMerchantDao() {
        return merchantDao;
    }

    public MessageDao getMessageDao() {
        return messageDao;
    }

    public MessageObserverDao getMessageObserverDao() {
        return messageObserverDao;
    }

    public OperateRecordDao getOperateRecordDao() {
        return operateRecordDao;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

    public RecordDao getRecordDao() {
        return recordDao;
    }

    public RequestDao getRequestDao() {
        return requestDao;
    }

    public ShopDao getShopDao() {
        return shopDao;
    }

    public StatisticDao getStatisticDao() {
        return statisticDao;
    }

    public VerificationRecordDao getVerificationRecordDao() {
        return verificationRecordDao;
    }

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

    public void setEnterpriseDao(EnterpriseDao enterpriseDao) {
        this.enterpriseDao = enterpriseDao;
    }

    public void setMerchantClearDao(MerchantClearDao merchantClearDao) {
        this.merchantClearDao = merchantClearDao;
    }

    public void setMerchantDao(MerchantDao merchantDao) {
        this.merchantDao = merchantDao;
    }

    public void setMessageDao(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    public void setMessageObserverDao(MessageObserverDao messageObserverDao) {
        this.messageObserverDao = messageObserverDao;
    }

    public void setOperateRecordDao(OperateRecordDao operateRecordDao) {
        this.operateRecordDao = operateRecordDao;
    }

    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public void setRecordDao(RecordDao recordDao) {
        this.recordDao = recordDao;
    }

    public void setRequestDao(RequestDao requestDao) {
        this.requestDao = requestDao;
    }

    public void setShopDao(ShopDao shopDao) {
        this.shopDao = shopDao;
    }

    public void setStatisticDao(StatisticDao statisticDao) {
        this.statisticDao = statisticDao;
    }

    public void setVerificationRecordDao(VerificationRecordDao verificationRecordDao) {
        this.verificationRecordDao = verificationRecordDao;
    }
}
