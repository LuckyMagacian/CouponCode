package com.lanxi.couponcode.impl.config;

/**
 * Created by yangyuanjian on 2017/10/31.
 */
public interface FunName extends ConstConfig{
    //--------------------------------------------------redis--------------------------------------------------------------
    /**redis功能标签*/
    String REDIS=ARTIFCAT+"-REDIS-";
    /**redis串码变量*/
    String MERCHANT_COUDE_VAR="MERCHANT-COUDE-VAR-";
    /**redis商户编号缓存*/
    String MERCHANT_ID="MERCHANT-ID-";
    /**redis商户商品编号缓存*/
    String MERCHANT_COMMODITY_ID="MERCHANT-COMMODITY-ID-";
    /**redis商户门店编号缓存*/
    String MERCHANT_SHOP_ID="MERCHANT-SHOP-ID-";
    /**redis商户员工编号缓存*/
    String MERCHANT_USER_ID="MERCHANT-USER-ID-";
    /**redis商户清算编号缓存*/
    String MERCHANT_CLEAR_ID="MERCHANT-CLEAR-ID-";
    /**redis操作记录编号缓存*/
    String MERCHANT_OPERATE_ID="MERCHANT-OPERATE-ID-";


    String CODE_VALUE="CODE-VALUE-";
    //-------------------------------------------------------------------------------------------------------------------


}
