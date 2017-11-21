package com.lanxi.couponcode.impl.assist;
import static com.lanxi.couponcode.impl.config.ConstConfig.*;
/**
 * codeVar->hmap(key,merchantId,codeVar)
 * merchants->hmap(key,merchantId,Locker)
 * commoditys->hmap(key,commodityId,locker)
 * shops->hmap(key,shopId,locker)
 * accounts->hmap(key,phone,locker)
 * codes->hmap(key,code,locker)
 * requests->ham(key,requestId,locker)
 * Created by yangyuanjian on 2017/11/21.
 */
public class RedisKeyAssist {

    public static String getVarKey(Long merchantId){
        String funName="CODE_VAR";
        String key=ARTIFCAT+funName;
        return key;
    }

    public static String getMerchantKey(){
        String funName="MERCHANT";
        String key=ARTIFCAT+funName;
        return key;
    }

//    public static  String getMerchantKey(Long merchantId){
//        return getMerchantKey()+merchantId;
//    }

    public static String getCommodityKey(){
        String funName="COMMODITY";
        String key=ARTIFCAT+funName;
        return key;
    }

//    public static String getCommodityKey(Long commodityId){
//        return getCommodityKey()+commodityId;
//    }

    public static String getShopKey(){
        String funName="SHOP";
        String key=ARTIFCAT+funName;
        return key;
    }

//    public static String getShopKey(Long shopId){
//        return getShopKey()+shopId;
//    }

    public static String getAccountKey(){
        String funName="ACCOUNT";
        String key=ARTIFCAT+funName;
        return key;
    }

//    public static String getAccountKey(Long accountId){
//        return getAccountKey()+accountId;
//    }

    public static String getCodeKey(){
        String funName="COUPONCOUDE";
        String key=ARTIFCAT+funName;
        return key;
    }

//    public static String getCodeKey(Long codeId){
//        return getCodeKey()+codeId;
//    }

    public static String getRequestKey(){
        String funName="REQUEST";
        String key=ARTIFCAT+funName;
        return key;
    }

//    public static String getRequestKey(Long requestId){
//        return getRequestKey()+requestId;
//    }

    public static String getLoginKey(Long accountId){
        String funName="LOGIN";
        String key=ARTIFCAT+funName+accountId;
        return key;
    }

    public static String getVerificateCodeKey(String phone){
        String funName="VerificateCode";
        String key=ARTIFCAT+funName+phone;
        return key;
    }
}
