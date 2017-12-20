package com.lanxi.couponcode.spi.assist;

import com.lanxi.couponcode.spi.defaultInterfaces.ToJson;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Function;

import static com.lanxi.couponcode.spi.config.ConstConfig.*;

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
public interface RedisKeyAssist {
    @SuppressWarnings ("sure be called at where you really want !")
    Function<List<Object>, String> methodCacheKeyGenerator = (args) -> ReflectAssist.getEnvironmentClassName() + ReflectAssist.getEnvironmentMethodName() + ToJson.toJson(args);

    static String getVarKey(Long merchantId) {
        String funName = "CODE_VAR";
        String key = ARTIFCAT + funName + merchantId;
        return key;
    }

    static String getMerchantKey() {
        String funName = "MERCHANT";
        String key = ARTIFCAT + funName;
        return key;
    }

//    public static  String getMerchantKey(Long merchantId){
//        return getMerchantKey()+merchantId;
//    }

    static String getCommodityKey() {
        String funName = "COMMODITY";
        String key = ARTIFCAT + funName;
        return key;
    }

//    public static String getCommodityKey(Long commodityId){
//        return getCommodityKey()+commodityId;
//    }

    static String getShopKey() {
        String funName = "SHOP";
        String key = ARTIFCAT + funName;
        return key;
    }

//    public static String getShopKey(Long shopId){
//        return getShopKey()+shopId;
//    }

    static String getAccountKey() {
        String funName = "ACCOUNT";
        String key = ARTIFCAT + funName;
        return key;
    }

//    public static String getAccountKey(Long accountId){
//        return getAccountKey()+accountId;
//    }

    static String getCodeKey() {
        String funName = "COUPONCOUDE";
        String key = ARTIFCAT + funName;
        return key;
    }

    static String getCodeKey(Long merchantId) {
        String funName = "COUPONCOUDE" + merchantId;
        String key = ARTIFCAT + funName;
        return key;
    }

//    public static String getCodeKey(Long codeId){
//        return getCodeKey()+codeId;
//    }

    static String getRequestKey() {
        String funName = "REQUEST";
        String key = ARTIFCAT + funName;
        return key;
    }

//    public static String getRequestKey(Long requestId){
//        return getRequestKey()+requestId;
//    }

    static String getLoginKey(Long accountId) {
        String funName = "LOGIN";
        String key = ARTIFCAT + funName + accountId;
        return key;
    }

    static String getVerificateCodeKey(String phone) {
        String funName = "VerificateCode";
        String key = ARTIFCAT + funName + phone;
        return key;
    }

    static String getDailyRecordKey() {
        String funName = "DailyCode";
        String key = ARTIFCAT + funName;
        return key;
    }

    static String getClearRecordKey() {
        String funName = "ClearRecord";
        String key = ARTIFCAT + funName;
        return key;
    }

    static String getEnterpriseKey() {
        String funName = "Enterprise";
        String key = ARTIFCAT + funName;
        return key;
    }

    static String getOrderKey() {
        String funName = "Order";
        String key = ARTIFCAT + funName;
        return key;
    }


    static String getMethodCacheKey(String className, String methodName, String args) {
        return className + methodName + args;
    }

    static String getMethodCacheKey(String className, String methodName, List<Object> args) {
        String argStr = ToJson.toJson(args);
        return className + methodName + argStr;
    }

    static String getMethodCacheKey(final Method method, final List<Object> args) {
        Class clazz = method.getDeclaringClass();
        return getMethodCacheKey(clazz.getName(), method.getName(), args);
    }

    static String getMethodCacheKey(final Method method, final String args) {
        Class clazz = method.getDeclaringClass();
        return getMethodCacheKey(clazz.getName(), method.getName(), args);
    }

    static String getProjectKey(final String keyOrigin) {
        return ARTIFCAT + keyOrigin;
    }
}
