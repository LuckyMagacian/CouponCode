package com.lanxi.couponcode.spi.consts.enums;

import java.io.Serializable;

/**
 * <strong>请求操作类型</strong>
 * createMerchant 1 请求类型-添加商户<br>
 * unfreezeMerchant 2 请求类型-解冻商户<br>
 * freezeMerchant 3 请求类型-冻结商户<br>
 * deleteMerchant 4 请求类型-删除商户<br>
 * modifyMerchant 5 请求类型-修改商户<br>
 * queryMerchant 6 请求类型-查询商户<br>
 * createShop 11 请求类型-添加门店<br>
 * unfreezeShop 12 请求类型-解冻门店<br>
 * freezeShop 13 请求类型-冻结门店<br>
 * deleteShop 14 请求类型-删除门店<br>
 * modifyShop 15 请求类型-修改门店<br>
 * queryShop 16 请求类型-查询门店<br>
 * createMerchantManager 21 请求类型-创建商户管理员<br>
 * unfreezeMerchantManager 22 请求类型-解冻商户管理员<br>
 * freezeMerchantManager 23 请求类型-冻结商户管理员<br>
 * deleteMerchantManager 24 请求类型-删除商户管理员<br>
 * modifyMerchantManager 25 请求类型-修改商户管理员<br>
 * queryMerchantManager 26 请求类型-查询商户管理员<br>
 * createShopManager 31 请求类型-创建门店管理员<br>
 * unfreezeShopManager 32 请求类型-解冻门店管理员<br>
 * freezeShopManager 33 请求类型-冻结门店管理员<br>
 * deleteShopManager 34 请求类型-删除门店管理员<br>
 * modifyShopManager 35 请求类型-修改门店管理员<br>
 * queryShopManager 36 请求类型-查询门店管理员<br>
 * createEmployee 41 请求类型-创建门店员工<br>
 * unfreezeEmployee 42 请求类型-解冻门店员工<br>
 * freezeEmployee 43 请求类型-冻结门店员工<br>
 * deleteEmployee 44 请求类型-删除门店员工<br>
 * modifyEmployee 45 请求类型-修改门店员工<br>
 * queryEmployee 46 请求类型-查询门店员工<br>
 * createCommdity 51 请求类型-创建商品<br>
 * unshelveCommdity 52 请求类型-下架商品<br>
 * shelveCommdity 53 请求类型-上架商品<br>
 * deleteCommdity 54 请求类型-删除商品<br>
 * modifyCommdity 55 请求类型-修改商品<br>
 * queryCommdity 56 请求类型-查询商品<br>
 * destroyCouponCode 61 请求类型-核销串码<br>
 * createCouponCode 62 请求类型-创建串码<br>
 * queryCouponCode 66 请求类型-查询串码<br>
 * createAccount 71 请求类型-创建账户<br>
 * unfreezeAccount 72 请求类型-解冻账户<br>
 * freezeAccount 73 请求类型-冻结账户<br>
 * deleteAccount 74 请求类型-删除账户<br>
 * modifyAccount 75 请求类型-修改账户<br>
 * queryAccount 76 请求类型-查询账户<br>
 *
 * @author yangyuanjian
 */

public enum RequestOperateType implements Serializable, Gettype {

    createShop(11), unfreezeShop(12), freezeShop(13), deleteShop(14), modifyShop(15), queryShop(16), cancelShop(19),
    createMerchantManager(21), unfreezeMerchantManager(22), freezeMerchantManager(23), deleteMerchantManager(24), modifyMerchantManager(25), queryMerchantManager(26), cancelMerchantManager(29),
    createShopManager(31), unfreezeShopManager(32), freezeShopManager(33), deleteShopManager(34), modifyShopManager(35), queryShopManager(36), cancelShopManager(39),
    createEmployee(41), unfreezeEmployee(42), freezeEmployee(43), deleteEmployee(44), modifyEmployee(45), queryEmployee(46), cancelEmployee(49),
    createCommodity(51), unshelveCommodity(52), shelveCommodity(53), deleteCommodity(54), modifyCommodity(55), queryCommodity(56), cancelCommodity(59),
    destroyCouponCode(61), createCouponCode(62), queryCouponCode(66), cancelCouponCode(69),
    createAccount(71), unfreezeAccount(72), freezeAccount(73), deleteAccount(74), modifyAccount(75), queryAccount(76), cancelAccount(79),
    createMerchant(81), unfreezeMerchant(82), freezeMerchant(83), deleteMerchant(84), modifyMerchant(85), queryMerchant(86), cancelMerchant(89);
    private String value;

    private RequestOperateType(int value) {
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

    public static RequestOperateType getType(int value) {
        return getType(value + "");
    }


    public static RequestOperateType getType(String value) {
        return Gettype.getType(value);
    }

}
