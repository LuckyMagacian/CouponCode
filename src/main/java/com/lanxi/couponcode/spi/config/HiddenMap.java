package com.lanxi.couponcode.spi.config;

import com.lanxi.couponcode.impl.entity.*;
import com.lanxi.couponcode.spi.consts.enums.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Created by yangyuanjian on 12/27/2017.
 */
public interface HiddenMap {
    Map<String, String> ALL_ACCOUNT        = new HashMap<>();
    Map<String, String> ALL_DAILY_CLEAR    = new HashMap<>();
    Map<String, String> ALL_CLEAR_RECORD   = new HashMap<>();
    Map<String, String> ALL_CODE_ALGORITHM = new HashMap<>();
    Map<String, String> ALL_COMMODITY      = new HashMap<>();
    Map<String, String> ALL_CODE           = new HashMap<>();
    Map<String, String> ALL_MERCHANT       = new HashMap<>();
    Map<String, String> ALL_OPERATE        = new HashMap<>();
    Map<String, String> ALL_ORDER          = new HashMap<>();
    Map<String, String> ALL_REQUEST        = new HashMap<>();
    Map<String, String> ALL_SHOP           = new HashMap<>();
    Map<String, String> ALL_VERIFY         = new HashMap<>();

    static void initAll() {
        //-----------------------------------------------------------------账户--------------------------------------------------------------
        ALL_ACCOUNT.put("accountId", "账户编号");
        ALL_ACCOUNT.put("loginFailureNum", "账户登录失败次数");
        ALL_ACCOUNT.put("loginFailureTime", "账户上次登录失败时间");
        ALL_ACCOUNT.put("phone", "用户手机号码");
        ALL_ACCOUNT.put("password", "登录密码");
        ALL_ACCOUNT.put("userName", "用户姓名");
        ALL_ACCOUNT.put("status", "账户状态");
        ALL_ACCOUNT.put("type", "账户类型");
        ALL_ACCOUNT.put("merchantId", "所属商户编号");
        ALL_ACCOUNT.put("merchantName", "所属商户名称");
        ALL_ACCOUNT.put("shopId", "门店编号");
        ALL_ACCOUNT.put("shopName", "门店名称");
        ALL_ACCOUNT.put("addById", "添加者编号");
        ALL_ACCOUNT.put("addByName", "添加者姓名");
        ALL_ACCOUNT.put("addTime", "添加时间");
        ALL_ACCOUNT.put("requesterId", "请求者编号");
        ALL_ACCOUNT.put("requesterName", "请求者姓名");
        ALL_ACCOUNT.put("requestId", "请求编号");
        ALL_ACCOUNT.put("requestTime", "请求时间");
        ALL_ACCOUNT.put("accountType", "账户类型");
        ALL_ACCOUNT.put("checkById", "审核者编号");
        ALL_ACCOUNT.put("checkName", "审核者姓名");
        ALL_ACCOUNT.put("checkTime", "审核时间");
        ALL_ACCOUNT.put("remark", "备注");
        //-----------------------------------------------------------------日结算记录--------------------------------------------------------------
        ALL_DAILY_CLEAR.put("recordId", "记录编号");
        ALL_DAILY_CLEAR.put("recordTime", "记录时间");
        ALL_DAILY_CLEAR.put("showTotal", "应结算总额");
        ALL_DAILY_CLEAR.put("merchantId", "商户编号");
        ALL_DAILY_CLEAR.put("merchantName", "商户名称");
        ALL_DAILY_CLEAR.put("verificateNum", "核销数量");
        ALL_DAILY_CLEAR.put("cancelationNum", "销毁数量");
        ALL_DAILY_CLEAR.put("overtimeNum", "过期数量");
        ALL_DAILY_CLEAR.put("verificateCost", "核销成本");
        ALL_DAILY_CLEAR.put("cancelationCost", "销毁成本");
        ALL_DAILY_CLEAR.put("overtimeCost", "过期成本");
        ALL_DAILY_CLEAR.put("clearStatus", "结算状态");
        ALL_DAILY_CLEAR.put("clearTime", "清算时间");
        ALL_DAILY_CLEAR.put("commodityClearRecords", "商品清算记录列表");
        //-----------------------------------------------------------------结算记录--------------------------------------------------------------
        ALL_CLEAR_RECORD.put("recordId", "记录编号");
        ALL_CLEAR_RECORD.put("merchantId", "商户编号");
        ALL_CLEAR_RECORD.put("merchantName", "商户名称");
        ALL_CLEAR_RECORD.put("dailyRecordIds", "核销记录列表");
        ALL_CLEAR_RECORD.put("timeStart", "清算时间起");
        ALL_CLEAR_RECORD.put("timeStop", "清算时间止");
        ALL_CLEAR_RECORD.put("showTotal", "应结算金额");
        ALL_CLEAR_RECORD.put("factTotal", "实际结算金额");
        ALL_CLEAR_RECORD.put("clearTime", "清算时间");
        ALL_CLEAR_RECORD.put("operaterId", "操作者编号");
        ALL_CLEAR_RECORD.put("operaterName", "操作者姓名");
        ALL_CLEAR_RECORD.put("taxNum", "税号");
        ALL_CLEAR_RECORD.put("logisticsCompany", "物流公司\n");
        ALL_CLEAR_RECORD.put("orderNum", "运单号");
        ALL_CLEAR_RECORD.put("postTime", "寄出时间");
        ALL_CLEAR_RECORD.put("invoiceStatus", "发票状态");
        ALL_CLEAR_RECORD.put("createTime", "创建时间");
        ALL_CLEAR_RECORD.put("clearStatus", "结算状态");
        ALL_CLEAR_RECORD.put("remark", "备注");
        //-----------------------------------------------------------------串码算法--------------------------------------------------------------
        ALL_CODE_ALGORITHM.put("merchantId", "商户编号");
        ALL_CODE_ALGORITHM.put("power", "乘方值");
        ALL_CODE_ALGORITHM.put("p1", "大质数1");
        ALL_CODE_ALGORITHM.put("p2", "大质数2");
        ALL_CODE_ALGORITHM.put("n", "大质数之积");
        ALL_CODE_ALGORITHM.put("var", "自增变量");
        //-----------------------------------------------------------------商品--------------------------------------------------------------
        ALL_COMMODITY.put("commodityId", "商品编号");
        ALL_COMMODITY.put("commodityName", "商品名称");
        ALL_COMMODITY.put("type", "商品类型");
        ALL_COMMODITY.put("facePrice", "商品面值");
        ALL_COMMODITY.put("sellPrice", "商品销售价");
        ALL_COMMODITY.put("costPrice", "商品成本价");
        ALL_COMMODITY.put("lifeTime", "商品有效期");
        ALL_COMMODITY.put("status", "商品状态");
        ALL_COMMODITY.put("lessNum", "剩余库存");
        ALL_COMMODITY.put("warnningNum", "库存警告值");
        ALL_COMMODITY.put("merchantId", "商户编号");
        ALL_COMMODITY.put("merchantName", "商户名称");
        ALL_COMMODITY.put("description", "商品描述");
        ALL_COMMODITY.put("useDetail", "使用详情");
        ALL_COMMODITY.put("addId", "添加者编号");
        ALL_COMMODITY.put("addTime", "添加时间");
        ALL_COMMODITY.put("addName", "添加者姓名");
        ALL_COMMODITY.put("remark", "请求编号");
        //-----------------------------------------------------------------串码--------------------------------------------------------------
        ALL_CODE.put("codeId", "串码编号");
        ALL_CODE.put("code", "串码");
        ALL_CODE.put("createTime", "创建时间");
        ALL_CODE.put("overTime", "过期时间");
        ALL_CODE.put("lifeTime", "有效期(单位天)");
        ALL_CODE.put("commodityId", "商品编号");
        ALL_CODE.put("commodityName", "商品名称");
        ALL_CODE.put("commodityInfo", "商品详情");
        ALL_CODE.put("destroyRecordId", "核销记录编号");
        ALL_CODE.put("codeStatus", "串码状态");
        ALL_CODE.put("merchantId", "商户编号");
        ALL_CODE.put("merchantName", "商户名称");
        ALL_CODE.put("clearStatus", "结算状态");
        ALL_CODE.put("clearTime", "结算时间");
        ALL_CODE.put("finalTime", "失效时间");
        ALL_CODE.put("reason", "生成原因");
        ALL_CODE.put("channel", "生成渠道");
        ALL_CODE.put("verificationType", "核销方式");
        ALL_CODE.put("generateType", "生成方式");
        ALL_CODE.put("verifyTime", "核销时间");
        //-----------------------------------------------------------------校验--------------------------------------------------------------
        ALL_MERCHANT.put("merchantId", "商户编号");
        ALL_MERCHANT.put("merchantName", "商户名称");
        ALL_MERCHANT.put("merchantStatus", "商户状态");
        ALL_MERCHANT.put("createTime", "信息提交时间");
        ALL_MERCHANT.put("workAddress", "办公地址");
        ALL_MERCHANT.put("minuteWorkAddress", "办公详细地址");
        ALL_MERCHANT.put("serveExplain", "服务或商品说明");
        ALL_MERCHANT.put("registerAddress", "注册地址");
        ALL_MERCHANT.put("minuteRegisterAddress", "注册详细地址");
        ALL_MERCHANT.put("oraganizingCode", "组织机构代码");
        ALL_MERCHANT.put("charterCode", "营业执照编号");
        ALL_MERCHANT.put("principal", "法定代表人");
        ALL_MERCHANT.put("linkMan", "联系人姓名");
        ALL_MERCHANT.put("linkManPhone", "联系人手机号码");
        ALL_MERCHANT.put("serviceTel", "客服电话");
        ALL_MERCHANT.put("email", "电子邮箱");
        ALL_MERCHANT.put("organizingInstitutionBarCodePic", "组织机构代码证");
        ALL_MERCHANT.put("businessLicensePic", "工商营业执照");
        ALL_MERCHANT.put("otherPic", "其他证明材料");
        ALL_MERCHANT.put("addId", "添加者编号");
        ALL_MERCHANT.put("addName", "添加者姓名");
        //-----------------------------------------------------------------校验--------------------------------------------------------------
        ALL_OPERATE.put("recordId", "记录编号");
        ALL_OPERATE.put("operaterId", "操作者编号");
        ALL_OPERATE.put("accountType", "操作员账户类型");
        ALL_OPERATE.put("phone", "操作员手机号码");
        ALL_OPERATE.put("name", "操作员姓名");
        ALL_OPERATE.put("targetType", "目标类型");
        ALL_OPERATE.put("type", "操作类型");
        ALL_OPERATE.put("operateTime", "操作时间");
        ALL_OPERATE.put("operateResult", "操作结果");
        ALL_OPERATE.put("description", "操作描述");
        ALL_OPERATE.put("remark", "备注");
        ALL_OPERATE.put("merchantId", "商户编号");
        ALL_OPERATE.put("merchantName", "商户名称");
        ALL_OPERATE.put("shopId", "门店编号");
        ALL_OPERATE.put("shopName", "门店名称");
        ALL_OPERATE.put("operaterInfo", "操作员详细信息");
        ALL_OPERATE.put("targetInfo", "操作目标信息");
        ALL_OPERATE.put("moreInfo", "附属信息");
        //-----------------------------------------------------------------校验--------------------------------------------------------------
        ALL_ORDER.put("orderId", "订单编号");
        ALL_ORDER.put("src", "订单发起机构");
        ALL_ORDER.put("phone", "绑定手机号");
        ALL_ORDER.put("chkDate", "清算日期");
        ALL_ORDER.put("needSend", "是否需要下发");
        ALL_ORDER.put("orderStatus", "订单状态");
        ALL_ORDER.put("endTime", "串码过期时间");
        ALL_ORDER.put("code", "串码");
        ALL_ORDER.put("msgId", "交易序号");
        ALL_ORDER.put("requestType", "请求类型");
        ALL_ORDER.put("merchantId", "商品所属商户编号");
        ALL_ORDER.put("type", "商品类型");
        ALL_ORDER.put("skuCode", "商品编号");
        ALL_ORDER.put("count", "商品数量");
        ALL_ORDER.put("workDate", "交易日期");
        ALL_ORDER.put("createTime", "订单创建时间");
        ALL_ORDER.put("codeCreateTime", "串码生成时间");
        ALL_ORDER.put("workTime", "交易时间");
        ALL_ORDER.put("amt", "商品单价");
        ALL_ORDER.put("totalAmt", "易总额");
        ALL_ORDER.put("successNum", "交易成功笔数");
        ALL_ORDER.put("commodityName", "商品名称");
        ALL_ORDER.put("merchantName", "商户名称");
        ALL_ORDER.put("serialNum", "平台流水号");
        ALL_ORDER.put("remark", "备注");
        //-----------------------------------------------------------------校验--------------------------------------------------------------
        ALL_REQUEST.put("requestId", "请求编号");
        ALL_REQUEST.put("requestTime", "请求时间");
        ALL_REQUEST.put("requesterId", "请求者发起者编号");
        ALL_REQUEST.put("requesterName", "请求发起者姓名");
        ALL_REQUEST.put("requesterPhone", "请求发起者手机号码");
        ALL_REQUEST.put("type", "请求类型");
        ALL_REQUEST.put("commodityInfo", "商品信息");
        ALL_REQUEST.put("status", "请求状态");
        ALL_REQUEST.put("checkerId", "审核者编号");
        ALL_REQUEST.put("checkerName", "审核者姓名");
        ALL_REQUEST.put("checkerPhone", "审核者手机号码");
        ALL_REQUEST.put("checkTime", "审核时间");
        ALL_REQUEST.put("commodityId", "关联商品编号");
        ALL_REQUEST.put("commodityName", "关联商品名称");
        ALL_REQUEST.put("commodityType", "关联商品类型");
        ALL_REQUEST.put("merchantId", "关联商户编号");
        ALL_REQUEST.put("merchantName", "关联商户名称");
        ALL_REQUEST.put("commodityInfoBefore", "请求发起时商品信息");
        ALL_REQUEST.put("commodityInfoAfter", "请求处理后商品信息");
        ALL_REQUEST.put("reason", "原因");
        //-----------------------------------------------------------------校验--------------------------------------------------------------
        ALL_SHOP.put("shopId", "门店编号");
        ALL_SHOP.put("merchantId", "所属商户编号");
        ALL_SHOP.put("merchantName", "所属商户名称");
        ALL_SHOP.put("shopName", "门店名称");
        ALL_SHOP.put("createTime", "创建时间");
        ALL_SHOP.put("merchantStatus", "所对应商户的状态");
        ALL_SHOP.put("shopAddress", "门店地址");
        ALL_SHOP.put("minuteShopAddress", "门店详细地址");
        ALL_SHOP.put("shopStatus", "门店状态");
        ALL_SHOP.put("addId", "添加者编号");
        ALL_SHOP.put("addName", "添加者姓名");
        ALL_SHOP.put("servicetel", "客服电话");
        //-----------------------------------------------------------------校验--------------------------------------------------------------
        ALL_VERIFY.put("recordId", "记录编号");
        ALL_VERIFY.put("code", "串码");
        ALL_VERIFY.put("operaterId", "操作者编号");
        ALL_VERIFY.put("operaterPhone", "操作者手机号码");
        ALL_VERIFY.put("commodityId", "商品编号");
        ALL_VERIFY.put("commodityName", "商品名称");
        ALL_VERIFY.put("verficateTime", "核销时间");
        ALL_VERIFY.put("merchantId", "商户编号");
        ALL_VERIFY.put("merchantName", "商户名称");
        ALL_VERIFY.put("shopId", "门店编号");
        ALL_VERIFY.put("shopName", "门店名称");
        ALL_VERIFY.put("verificationType", "验证方式");
        ALL_VERIFY.put("clearStatus", "清算状态");
        ALL_VERIFY.put("shopInfo", "门店详情");
    }

    Map<String, String> ADMIN_ACCOUNT        = new HashMap<>();
    Map<String, String> ADMIN_DAILY_CLEAR    = new HashMap<>();
    Map<String, String> ADMIN_CLEAR_RECORD   = new HashMap<>();
    Map<String, String> ADMIN_CODE_ALGORITHM = new HashMap<>();
    Map<String, String> ADMIN_COMMODITY      = new HashMap<>();
    Map<String, String> ADMIN_CODE           = new HashMap<>();
    Map<String, String> ADMIN_MERCHANT       = new HashMap<>();
    Map<String, String> ADMIN_OPERATE        = new HashMap<>();
    Map<String, String> ADMIN_ORDER          = new HashMap<>();
    Map<String, String> ADMIN_REQUEST        = new HashMap<>();
    Map<String, String> ADMIN_SHOP           = new HashMap<>();
    Map<String, String> ADMIN_VERIFY         = new HashMap<>();

    static void initAdmin() {
        ADMIN_ACCOUNT.put("shopName", "门店名称");
        ADMIN_ACCOUNT.put("userName", "用户姓名");
        ADMIN_ACCOUNT.put("phone", "用户手机号码");
        ADMIN_ACCOUNT.put("accountType", "账户类型");
        ADMIN_ACCOUNT.put("status", "账户状态");
        ADMIN_ACCOUNT.put("accountId", "账户编号");

        ADMIN_CODE.put("codeId", "串码编号");
        ADMIN_CODE.put("merchantName", "商户名称");
        ADMIN_CODE.put("commodityName", "商品名称");
        ADMIN_CODE.put("code", "串码");
        ADMIN_CODE.put("lifeTime", "有效期(单位天)");
        ADMIN_CODE.put("codeStatus", "串码状态");
        ADMIN_CODE.put("finalTime", "失效时间");
        ADMIN_CODE.put("generateType", "生成方式");
        ADMIN_CODE.put("commodityId", "商品编号");
        ADMIN_CODE.put("createTime", "创建时间");
        ADMIN_CODE.put("commodityInfo", "商品详情");
        ADMIN_CODE.put("overTime", "过期时间");

        ADMIN_DAILY_CLEAR.put("merchantName", "商户名称");
        ADMIN_DAILY_CLEAR.put("overtimeNum", "过期数量");
        ADMIN_DAILY_CLEAR.put("verificateNum", "核销数量");
        ADMIN_DAILY_CLEAR.put("cancelationNum", "销毁数量");
        ADMIN_DAILY_CLEAR.put("verificateCost", "核销成本");
        ADMIN_DAILY_CLEAR.put("cancelationCost", "销毁成本");
        ADMIN_DAILY_CLEAR.put("overtimeCost", "过期成本");
        ADMIN_DAILY_CLEAR.put("recordTime", "记录时间");
        ADMIN_DAILY_CLEAR.put("clearTime", "清算时间");
        ADMIN_DAILY_CLEAR.put("showTotal", "应结算总额");
        ADMIN_DAILY_CLEAR.put("recordId", "记录编号");
        ADMIN_DAILY_CLEAR.put("commodityClearRecords", "商品清算记录列表");

        ADMIN_REQUEST.put("merchantName", "关联商户名称");
        ADMIN_REQUEST.put("requestTime", "请求时间");
        ADMIN_REQUEST.put("requestId", "请求编号");
        ADMIN_REQUEST.put("commodityInfo", "商品信息");
        ADMIN_REQUEST.put("requestTime", "请求时间");
        ADMIN_REQUEST.put("reason", "原因");
        ADMIN_REQUEST.put("status", "请求状态");
        ADMIN_REQUEST.put("type", "请求类型");
        ADMIN_REQUEST.put("commodityType", "关联商品类型");
        ADMIN_REQUEST.put("commodityId", "关联商品编号");
        ADMIN_REQUEST.put("status", "请求状态");
        ADMIN_REQUEST.put("commodityName", "关联商品名称");

        ADMIN_COMMODITY.put("commodityId", "商品编号");
        ADMIN_COMMODITY.put("merchantName", "商户名称");
        ADMIN_COMMODITY.put("commodityName", "商品名称");
        ADMIN_COMMODITY.put("type", "商品类型");
        ADMIN_COMMODITY.put("status", "商品状态");
        ADMIN_COMMODITY.put("facePrice", "商品面值");
        ADMIN_COMMODITY.put("sellPrice", "商品销售价");
        ADMIN_COMMODITY.put("costPrice", "商品成本价");
        ADMIN_COMMODITY.put("lifeTime", "商品有效期");

        ADMIN_OPERATE.put("operateTime", "操作时间");
        ADMIN_OPERATE.put("description", "操作描述");
        ADMIN_OPERATE.put("name", "操作员姓名");
        ADMIN_OPERATE.put("accountType", "操作员账户类型");
        ADMIN_OPERATE.put("merchantName", "商户名称");
        ADMIN_OPERATE.put("shopName", "门店名称");

        ADMIN_MERCHANT.put("merchantName", "商户名称");
        ADMIN_MERCHANT.put("merchantStatus", "商户状态");
        ADMIN_MERCHANT.put("createTime", "信息提交时间");
        ADMIN_MERCHANT.put("workAddress", "办公地址");
        ADMIN_MERCHANT.put("minuteWorkAddress", "办公详细地址");
        ADMIN_MERCHANT.put("merchantId", "商户编号");

        ADMIN_CLEAR_RECORD.put("recordId", "记录编号");
        ADMIN_CLEAR_RECORD.put("merchantName", "商户名称");
        ADMIN_CLEAR_RECORD.put("timeStart", "清算时间起");
        ADMIN_CLEAR_RECORD.put("showTotal", "应结算金额");
        ADMIN_CLEAR_RECORD.put("factTotal", "实际结算金额");
        ADMIN_CLEAR_RECORD.put("invoiceStatus", "发票状态");
        ADMIN_CLEAR_RECORD.put("clearTime", "清算时间");
        ADMIN_CLEAR_RECORD.put("remark", "备注");
        ADMIN_CLEAR_RECORD.put("taxNum", "税号");
        ADMIN_CLEAR_RECORD.put("logisticsCompany", "物流公司\n");
        ADMIN_CLEAR_RECORD.put("orderNum", "运单号");
        ADMIN_CLEAR_RECORD.put("postTime", "寄出时间");

        ADMIN_ORDER.put("serialNum", "平台流水号");
        ADMIN_ORDER.put("orderId", "订单编号");
        ADMIN_ORDER.put("phone", "绑定手机号");
        ADMIN_ORDER.put("commodityName", "商品名称");
        ADMIN_ORDER.put("src", "订单发起机构");
        ADMIN_ORDER.put("orderStatus", "订单状态");
        ADMIN_ORDER.put("createTime", "订单创建时间");
        ADMIN_ORDER.put("skuCode", "商品编号");
        ADMIN_ORDER.put("code", "串码");
        ADMIN_ORDER.put("successNum", "交易成功笔数");
        ADMIN_ORDER.put("codeCreateTime", "串码生成时间");
        ADMIN_ORDER.put("endTime", "串码过期时间");

        ADMIN_SHOP.put("merchantName", "所属商户名称");
        ADMIN_SHOP.put("shopName", "门店名称");
        ADMIN_SHOP.put("shopId", "门店编号");
        ADMIN_SHOP.put("shopStatus", "门店状态");
        ADMIN_SHOP.put("shopAddress", "门店地址");
        ADMIN_SHOP.put("minuteShopAddress", "门店详细地址");
        ADMIN_SHOP.put("servicetel", "客服电话");

    }

    Map<String, String> MERCHANTMANAGER_ACCOUNT        = new HashMap<>();
    Map<String, String> MERCHANTMANAGER_DAILY_CLEAR    = new HashMap<>();
    Map<String, String> MERCHANTMANAGER_CLEAR_RECORD   = new HashMap<>();
    Map<String, String> MERCHANTMANAGER_CODE_ALGORITHM = new HashMap<>();
    Map<String, String> MERCHANTMANAGER_COMMODITY      = new HashMap<>();
    Map<String, String> MERCHANTMANAGER_CODE           = new HashMap<>();
    Map<String, String> MERCHANTMANAGER_MERCHANT       = new HashMap<>();
    Map<String, String> MERCHANTMANAGER_OPERATE        = new HashMap<>();
    Map<String, String> MERCHANTMANAGER_ORDER          = new HashMap<>();
    Map<String, String> MERCHANTMANAGER_REQUEST        = new HashMap<>();
    Map<String, String> MERCHANTMANAGER_SHOP           = new HashMap<>();
    Map<String, String> MERCHANTMANAGER_VERIFY         = new HashMap<>();

    static void initMerchant() {
        MERCHANTMANAGER_ACCOUNT.put("shopName", "门店名称");
        MERCHANTMANAGER_ACCOUNT.put("userName", "用户姓名");
        MERCHANTMANAGER_ACCOUNT.put("phone", "用户手机号码");
        MERCHANTMANAGER_ACCOUNT.put("accountType", "账户类型");
        MERCHANTMANAGER_ACCOUNT.put("status", "账户状态");
        MERCHANTMANAGER_ACCOUNT.put("accountId", "账户编号");

        MERCHANTMANAGER_CODE.put("code", "串码");
        MERCHANTMANAGER_CODE.put("commodityName", "商品名称");
        MERCHANTMANAGER_CODE.put("lifeTime", "有效期(单位天)");
        MERCHANTMANAGER_CODE.put("codeStatus", "串码状态");
        MERCHANTMANAGER_CODE.put("commodityInfo", "商品详情");

        MERCHANTMANAGER_DAILY_CLEAR.put("merchantName", "商户名称");
        MERCHANTMANAGER_DAILY_CLEAR.put("overtimeNum", "过期数量");
        MERCHANTMANAGER_DAILY_CLEAR.put("verificateNum", "核销数量");
        MERCHANTMANAGER_DAILY_CLEAR.put("cancelationNum", "销毁数量");
        MERCHANTMANAGER_DAILY_CLEAR.put("verificateCost", "核销成本");
        MERCHANTMANAGER_DAILY_CLEAR.put("cancelationCost", "销毁成本");
        MERCHANTMANAGER_DAILY_CLEAR.put("overtimeCost", "过期成本");
        MERCHANTMANAGER_DAILY_CLEAR.put("recordTime", "记录时间");
        MERCHANTMANAGER_DAILY_CLEAR.put("clearTime", "清算时间");
        MERCHANTMANAGER_DAILY_CLEAR.put("showTotal", "应结算总额");
        MERCHANTMANAGER_DAILY_CLEAR.put("recordId", "记录编号");
        MERCHANTMANAGER_DAILY_CLEAR.put("commodityClearRecords", "商品清算记录列表");

        MERCHANTMANAGER_CLEAR_RECORD.put("recordId", "记录编号");
        MERCHANTMANAGER_CLEAR_RECORD.put("merchantName", "商户名称");
        MERCHANTMANAGER_CLEAR_RECORD.put("timeStart", "清算时间起");
        MERCHANTMANAGER_CLEAR_RECORD.put("showTotal", "应结算金额");
        MERCHANTMANAGER_CLEAR_RECORD.put("factTotal", "实际结算金额");
        MERCHANTMANAGER_CLEAR_RECORD.put("invoiceStatus", "发票状态");
        MERCHANTMANAGER_CLEAR_RECORD.put("clearTime", "清算时间");
        MERCHANTMANAGER_CLEAR_RECORD.put("remark", "备注");
        MERCHANTMANAGER_CLEAR_RECORD.put("taxNum", "税号");
        MERCHANTMANAGER_CLEAR_RECORD.put("logisticsCompany", "物流公司\n");
        MERCHANTMANAGER_CLEAR_RECORD.put("orderNum", "运单号");
        MERCHANTMANAGER_CLEAR_RECORD.put("postTime", "寄出时间");

        MERCHANTMANAGER_REQUEST.put("requestTime", "请求时间");
        MERCHANTMANAGER_REQUEST.put("requestId", "请求编号");
        MERCHANTMANAGER_REQUEST.put("commodityInfo", "商品信息");
        MERCHANTMANAGER_REQUEST.put("requestTime", "请求时间");
        MERCHANTMANAGER_REQUEST.put("reason", "原因");
        MERCHANTMANAGER_REQUEST.put("status", "请求状态");
        MERCHANTMANAGER_REQUEST.put("type", "请求类型");
        MERCHANTMANAGER_REQUEST.put("commodityType", "关联商品类型");
        MERCHANTMANAGER_REQUEST.put("commodityId", "关联商品编号");

        MERCHANTMANAGER_COMMODITY.put("commodityId", "商品编号");
        MERCHANTMANAGER_COMMODITY.put("merchantName", "商户名称");
        MERCHANTMANAGER_COMMODITY.put("commodityName", "商品名称");
        MERCHANTMANAGER_COMMODITY.put("type", "商品类型");
        MERCHANTMANAGER_COMMODITY.put("status", "商品状态");
        MERCHANTMANAGER_COMMODITY.put("facePrice", "商品面值");
        MERCHANTMANAGER_COMMODITY.put("sellPrice", "商品销售价");
        MERCHANTMANAGER_COMMODITY.put("costPrice", "商品成本价");
        MERCHANTMANAGER_COMMODITY.put("lifeTime", "商品有效期");

        MERCHANTMANAGER_OPERATE.put("operateTime", "操作时间");
        MERCHANTMANAGER_OPERATE.put("description", "操作描述");
        MERCHANTMANAGER_OPERATE.put("name", "操作员姓名");
        MERCHANTMANAGER_OPERATE.put("accountType", "操作员账户类型");
        MERCHANTMANAGER_OPERATE.put("merchantName", "商户名称");
        MERCHANTMANAGER_OPERATE.put("shopName", "门店名称");

        MERCHANTMANAGER_SHOP.put("shopName", "门店名称");
        MERCHANTMANAGER_SHOP.put("shopId", "门店编号");
        MERCHANTMANAGER_SHOP.put("shopStatus", "门店状态");
        MERCHANTMANAGER_SHOP.put("shopAddress", "门店地址");
        MERCHANTMANAGER_SHOP.put("minuteShopAddress", "门店详细地址");
        MERCHANTMANAGER_SHOP.put("servicetel", "客服电话");

        MERCHANTMANAGER_VERIFY.put("commodityName", "商品名称");
        MERCHANTMANAGER_VERIFY.put("code", "串码");
        MERCHANTMANAGER_VERIFY.put("verficateTime", "核销时间");
        MERCHANTMANAGER_VERIFY.put("verificationType", "验证方式");
        MERCHANTMANAGER_VERIFY.put("operaterPhone", "操作者手机号码");
        MERCHANTMANAGER_VERIFY.put("recordId", "记录编号");
        MERCHANTMANAGER_VERIFY.put("shopName", "门店名称");
        MERCHANTMANAGER_VERIFY.put("commodityType", "商品类型");
        MERCHANTMANAGER_VERIFY.put("facePrice", "商品面值");

    }

    Map<String, String> SHOPMANAGER_ACCOUNT        = new HashMap<>();
    Map<String, String> SHOPMANAGER_DAILY_CLEAR    = new HashMap<>();
    Map<String, String> SHOPMANAGER_CLEAR_RECORD   = new HashMap<>();
    Map<String, String> SHOPMANAGER_CODE_ALGORITHM = new HashMap<>();
    Map<String, String> SHOPMANAGER_COMMODITY      = new HashMap<>();
    Map<String, String> SHOPMANAGER_CODE           = new HashMap<>();
    Map<String, String> SHOPMANAGER_MERCHANT       = new HashMap<>();
    Map<String, String> SHOPMANAGER_OPERATE        = new HashMap<>();
    Map<String, String> SHOPMANAGER_ORDER          = new HashMap<>();
    Map<String, String> SHOPMANAGER_REQUEST        = new HashMap<>();
    Map<String, String> SHOPMANAGER_SHOP           = new HashMap<>();
    Map<String, String> SHOPMANAGER_VERIFY         = new HashMap<>();

    static void initShopManager() {
    }

    Map<String, String> EMPLOYEE_ACCOUNT        = new HashMap<>();
    Map<String, String> EMPLOYEE_DAILY_CLEAR    = new HashMap<>();
    Map<String, String> EMPLOYEE_CLEAR_RECORD   = new HashMap<>();
    Map<String, String> EMPLOYEE_CODE_ALGORITHM = new HashMap<>();
    Map<String, String> EMPLOYEE_COMMODITY      = new HashMap<>();
    Map<String, String> EMPLOYEE_CODE           = new HashMap<>();
    Map<String, String> EMPLOYEE_MERCHANT       = new HashMap<>();
    Map<String, String> EMPLOYEE_OPERATE        = new HashMap<>();
    Map<String, String> EMPLOYEE_ORDER          = new HashMap<>();
    Map<String, String> EMPLOYEE_REQUEST        = new HashMap<>();
    Map<String, String> EMPLOYEE_SHOP           = new HashMap<>();
    Map<String, String> EMPLOYEE_VERIFY         = new HashMap<>();

    static void initShopEmployee() {

    }

    Map<String, String> CODE_COMMODITY             = new HashMap<>();
    Map<String, String> REQUEST_COMMODITY_ADMIN    = new HashMap<>();
    Map<String, String> REQUEST_COMMODITY_MERCHANT = new HashMap<>();
    Map<String, String> DAILY_RECORD_COMMODITY     = new HashMap<>();

    static void initOther(){
        CODE_COMMODITY.put("type","商品类型");
        CODE_COMMODITY.put("facePrice","面值");
        CODE_COMMODITY.put("costPrice","成本价");

        REQUEST_COMMODITY_ADMIN.put("commodityName","商品名称");
        REQUEST_COMMODITY_ADMIN.put("type","商品类型");
        REQUEST_COMMODITY_ADMIN.put("sellPrice","销售价");
        REQUEST_COMMODITY_ADMIN.put("facePrice","面值");
        REQUEST_COMMODITY_ADMIN.put("costPrice","成本价");
        REQUEST_COMMODITY_ADMIN.put("lifeTime","有效期");
        REQUEST_COMMODITY_ADMIN.put("useDetail","使用说明");

        REQUEST_COMMODITY_MERCHANT.put("commodityName","商品名称");
        REQUEST_COMMODITY_MERCHANT.put("type","商品类型");
        REQUEST_COMMODITY_MERCHANT.put("facePrice","面值");
        REQUEST_COMMODITY_MERCHANT.put("costPrice","成本价");
        REQUEST_COMMODITY_MERCHANT.put("lifeTime","有效期");
        REQUEST_COMMODITY_MERCHANT.put("useDetail","使用说明");

        DAILY_RECORD_COMMODITY.put("commodityId","商品编号");
        DAILY_RECORD_COMMODITY.put("commodityName","商品名称");
        DAILY_RECORD_COMMODITY.put("commodityType","商品类型");
        DAILY_RECORD_COMMODITY.put("overtimeNum", "过期数量");
        DAILY_RECORD_COMMODITY.put("verificateNum", "核销数量");
        DAILY_RECORD_COMMODITY.put("cancelationNum", "销毁数量");
        DAILY_RECORD_COMMODITY.put("verificateCost", "核销成本");
        DAILY_RECORD_COMMODITY.put("cancelationCost", "销毁成本");
        DAILY_RECORD_COMMODITY.put("overtimeCost", "过期成本");
    }

    BiFunction<Class, String, String> getAllFieldCN             = (c, f) -> {
        init();
        if (Account.class.equals(c))
            return ALL_ACCOUNT.get(f);
        else if (ClearDailyRecord.class.equals(c))
            return ALL_DAILY_CLEAR.get(f);
        else if (ClearRecord.class.equals(c))
            return ALL_CLEAR_RECORD.get(f);
        else if (CodeAlgorithm.class.equals(c))
            return ALL_CODE_ALGORITHM.get(f);
        else if (Commodity.class.equals(c))
            return ALL_COMMODITY.get(f);
        else if (CouponCode.class.equals(c))
            return ALL_CODE.get(f);
        else if (Merchant.class.equals(c))
            return ALL_MERCHANT.get(f);
        else if (OperateRecord.class.equals(c))
            return ALL_OPERATE.get(f);
        else if (Order.class.equals(c))
            return ALL_ORDER.get(f);
        else if (Request.class.equals(c))
            return ALL_REQUEST.get(f);
        else if (Shop.class.equals(c))
            return ALL_SHOP.get(f);
        else if (VerificationRecord.class.equals(c))
            return ALL_VERIFY.get(f);
        else
            return null;
    };
    BiFunction<Class, String, String> getAdminFieldCN           = (c, f) -> {
        init();
        if (Account.class.equals(c))
            return ADMIN_ACCOUNT.get(f);
        else if (ClearDailyRecord.class.equals(c))
            return ADMIN_DAILY_CLEAR.get(f);
        else if (ClearRecord.class.equals(c))
            return ADMIN_CLEAR_RECORD.get(f);
        else if (CodeAlgorithm.class.equals(c))
            return ADMIN_CODE_ALGORITHM.get(f);
        else if (Commodity.class.equals(c))
            return ADMIN_COMMODITY.get(f);
        else if (CouponCode.class.equals(c))
            return ADMIN_CODE.get(f);
        else if (Merchant.class.equals(c))
            return ADMIN_MERCHANT.get(f);
        else if (OperateRecord.class.equals(c))
            return ADMIN_OPERATE.get(f);
        else if (Order.class.equals(c))
            return ADMIN_ORDER.get(f);
        else if (Request.class.equals(c))
            return ADMIN_REQUEST.get(f);
        else if (Shop.class.equals(c))
            return ADMIN_SHOP.get(f);
        else if (VerificationRecord.class.equals(c))
            return ADMIN_VERIFY.get(f);
        else
            return null;
    };
    BiFunction<Class, String, String> getMerchantManagerFieldCN = (c, f) -> {
        init();
        if (Account.class.equals(c))
            return MERCHANTMANAGER_ACCOUNT.get(f);
        else if (ClearDailyRecord.class.equals(c))
            return MERCHANTMANAGER_DAILY_CLEAR.get(f);
        else if (ClearRecord.class.equals(c))
            return MERCHANTMANAGER_CLEAR_RECORD.get(f);
        else if (CodeAlgorithm.class.equals(c))
            return MERCHANTMANAGER_CODE_ALGORITHM.get(f);
        else if (Commodity.class.equals(c))
            return MERCHANTMANAGER_COMMODITY.get(f);
        else if (CouponCode.class.equals(c))
            return MERCHANTMANAGER_CODE.get(f);
        else if (Merchant.class.equals(c))
            return MERCHANTMANAGER_MERCHANT.get(f);
        else if (OperateRecord.class.equals(c))
            return MERCHANTMANAGER_OPERATE.get(f);
        else if (Order.class.equals(c))
            return MERCHANTMANAGER_ORDER.get(f);
        else if (Request.class.equals(c))
            return MERCHANTMANAGER_REQUEST.get(f);
        else if (Shop.class.equals(c))
            return MERCHANTMANAGER_SHOP.get(f);
        else if (VerificationRecord.class.equals(c))
            return MERCHANTMANAGER_VERIFY.get(f);
        else
            return null;
    };
    BiFunction<Class, String, String> getShopManagerFieldCN     = (c, f) -> {
        init();
        if (Account.class.equals(c))
            return SHOPMANAGER_ACCOUNT.get(f);
        else if (ClearDailyRecord.class.equals(c))
            return SHOPMANAGER_DAILY_CLEAR.get(f);
        else if (ClearRecord.class.equals(c))
            return SHOPMANAGER_CLEAR_RECORD.get(f);
        else if (CodeAlgorithm.class.equals(c))
            return SHOPMANAGER_CODE_ALGORITHM.get(f);
        else if (Commodity.class.equals(c))
            return SHOPMANAGER_COMMODITY.get(f);
        else if (CouponCode.class.equals(c))
            return SHOPMANAGER_CODE.get(f);
        else if (Merchant.class.equals(c))
            return SHOPMANAGER_MERCHANT.get(f);
        else if (OperateRecord.class.equals(c))
            return SHOPMANAGER_OPERATE.get(f);
        else if (Order.class.equals(c))
            return SHOPMANAGER_ORDER.get(f);
        else if (Request.class.equals(c))
            return SHOPMANAGER_REQUEST.get(f);
        else if (Shop.class.equals(c))
            return SHOPMANAGER_SHOP.get(f);
        else if (VerificationRecord.class.equals(c))
            return SHOPMANAGER_VERIFY.get(f);
        else
            return null;
    };
    BiFunction<Class, String, String> getEmployeeFieldCN        = (c, f) -> {
        init();
        if (Account.class.equals(c))
            return EMPLOYEE_ACCOUNT.get(f);
        else if (ClearDailyRecord.class.equals(c))
            return EMPLOYEE_DAILY_CLEAR.get(f);
        else if (ClearRecord.class.equals(c))
            return EMPLOYEE_CLEAR_RECORD.get(f);
        else if (CodeAlgorithm.class.equals(c))
            return EMPLOYEE_CODE_ALGORITHM.get(f);
        else if (Commodity.class.equals(c))
            return EMPLOYEE_COMMODITY.get(f);
        else if (CouponCode.class.equals(c))
            return EMPLOYEE_CODE.get(f);
        else if (Merchant.class.equals(c))
            return EMPLOYEE_MERCHANT.get(f);
        else if (OperateRecord.class.equals(c))
            return EMPLOYEE_OPERATE.get(f);
        else if (Order.class.equals(c))
            return EMPLOYEE_ORDER.get(f);
        else if (Request.class.equals(c))
            return EMPLOYEE_REQUEST.get(f);
        else if (Shop.class.equals(c))
            return EMPLOYEE_SHOP.get(f);
        else if (VerificationRecord.class.equals(c))
            return EMPLOYEE_VERIFY.get(f);
        else
            return null;
    };

    static void init() {
        if (ALL_ACCOUNT.isEmpty()) {
            initAll();
            initAdmin();
            initMerchant();
            initShopManager();
            initShopEmployee();
            initOther();
        }
    }
}
