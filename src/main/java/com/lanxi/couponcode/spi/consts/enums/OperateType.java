package com.lanxi.couponcode.spi.consts.enums;

import java.io.Serializable;

/**
 * 操作类型枚举
 * Created by yangyuanjian on 2017/11/9.
 */
public enum OperateType implements Serializable, Gettype {

    createShop(11), unfreezeShop(12), freezeShop(13), deleteShop(14),
    modifyShop(15), queryShop(16), cancelShop(19), importShops(17),
    downloadShopExcelTemplate(18),

    createMerchantManager(21), unfreezeMerchantManager(22), freezeMerchantManager(23), deleteMerchantManager(24),
    modifyMerchantManager(25), queryMerchantManager(26), cancelMerchantManager(29),

    createShopManager(31), unfreezeShopManager(32), freezeShopManager(33), deleteShopManager(34),
    modifyShopManager(35), queryShopManager(36), cancelShopManager(39),

    createEmployee(41), unfreezeEmployee(42), freezeEmployee(43), deleteEmployee(44),
    modifyEmployee(45), queryEmployee(46), cancelEmployee(49),

    createCommodity(51), unshelveCommodity(52), shelveCommodity(53), deleteCommodity(54),
    modifyCommodity(55), queryCommodity(56), cancelCommodity(59),

    destroyCouponCode(61), createCouponCode(62), queryCouponCode(66), postoneCouponCode(67),
    cancelCouponCode(69), queryCouponCodeList(68),

    createAccount(71), unfreezeAccount(72), freezeAccount(73), queryAccountInfo(78),
    deleteAccount(74), modifyAccount(75), queryAccount(76), cancelAccount(79),
    changePassword(77),

    createMerchant(81), unfreezeMerchant(82), freezeMerchant(83), deleteMerchant(84),
    modifyMerchant(85), queryMerchant(86), cancelMerchant(89), inputMerchantInfo(87),
    queryMerchantInfo(88),

    statscitcShop(91), statscitcMerchantManager(92), statscitcShopManager(93), statscitcEmployee(94),
    statscitcCommodity(95), statscitcCouponCode(96), statscitcAccount(97), statsticMerchant(98),

    exportShop(101), exportMerchantManager(102), exportShopManager(103), exportEmployee(104),
    exportCommodity(105), exportCouponCode(106), exportAccount(107), exportMerchant(108),

    queryDailyRecord(111), exportDailyRecord(112), clearDailyRecord(113),

    queryClearRecord(121), exportClearRecord(122), createClearRecord(123), queryClearRecordList(124), modifyClearRecord(125),

    queryVerifyRecord(131), exportVerifyRecord(132), createVerifyRecord(133), queryVerifyRecordList(134), queryVerifyRecordAll(135),

    requestAddCommodity(141), requestModifyCommodity(142), requestUnshelveCommodity(143), requestShelveCommodity(144),
    requestDelCommodity(145), queryRequest(146), passRequest(147), rejectRequest(148),

    queryOperateRecord(151), queryOperateRecordList(152);
    private String value;

    OperateType(int value) {
        this.value = value + "";
    }

    @Override
    public String toString() {
        return value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static OperateType getType(int value) {
        return getType(value + "");
    }


    public static OperateType getType(String value) {
        return Gettype.getType(value);
    }
}
