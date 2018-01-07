package com.lanxi.couponcode.impl.config;

import com.lanxi.couponcode.impl.entity.*;
import com.lanxi.couponcode.spi.consts.enums.*;
import com.lanxi.couponcode.spi.consts.future.CouponType;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by yangyuanjian on 12/22/2017.
 */
public interface HiddenMapExcel {

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
        ALL_VERIFY.put("commodityType","商品类型");
        ALL_VERIFY.put("facePrice","商品面值");
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
        ADMIN_ACCOUNT.putAll(ALL_ACCOUNT);
        ADMIN_DAILY_CLEAR.putAll(ALL_DAILY_CLEAR);
        ADMIN_CLEAR_RECORD.putAll(ALL_CLEAR_RECORD);
        ADMIN_CODE_ALGORITHM.putAll(ALL_CODE_ALGORITHM);
        ADMIN_COMMODITY.putAll(ALL_COMMODITY);
        ADMIN_CODE.putAll(ALL_CODE);
        ADMIN_MERCHANT.putAll(ALL_MERCHANT);
        ADMIN_OPERATE.putAll(ALL_OPERATE);
        ADMIN_ORDER.putAll(ALL_ORDER);
        ADMIN_REQUEST.putAll(ALL_REQUEST);
        ADMIN_SHOP.putAll(ALL_SHOP);
        ADMIN_VERIFY.putAll(ALL_VERIFY);
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
        MERCHANTMANAGER_ACCOUNT.putAll(ADMIN_ACCOUNT);
        MERCHANTMANAGER_DAILY_CLEAR.putAll(ADMIN_DAILY_CLEAR);
        MERCHANTMANAGER_CLEAR_RECORD.putAll(ADMIN_CLEAR_RECORD);
        MERCHANTMANAGER_CODE_ALGORITHM.putAll(ADMIN_CODE_ALGORITHM);
        MERCHANTMANAGER_COMMODITY.putAll(ADMIN_COMMODITY);
        MERCHANTMANAGER_CODE.putAll(ADMIN_CODE);
        MERCHANTMANAGER_MERCHANT.putAll(ADMIN_MERCHANT);
        MERCHANTMANAGER_OPERATE.putAll(ADMIN_OPERATE);
        MERCHANTMANAGER_ORDER.putAll(ADMIN_ORDER);
        MERCHANTMANAGER_REQUEST.putAll(ADMIN_REQUEST);
        MERCHANTMANAGER_SHOP.putAll(ADMIN_SHOP);
        MERCHANTMANAGER_VERIFY.putAll(ADMIN_VERIFY);
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
        SHOPMANAGER_ACCOUNT.putAll(MERCHANTMANAGER_ACCOUNT);
        SHOPMANAGER_DAILY_CLEAR.putAll(MERCHANTMANAGER_DAILY_CLEAR);
        SHOPMANAGER_CLEAR_RECORD.putAll(MERCHANTMANAGER_CLEAR_RECORD);
        SHOPMANAGER_CODE_ALGORITHM.putAll(MERCHANTMANAGER_CODE_ALGORITHM);
        SHOPMANAGER_COMMODITY.putAll(MERCHANTMANAGER_COMMODITY);
        SHOPMANAGER_CODE.putAll(MERCHANTMANAGER_CODE);
        SHOPMANAGER_MERCHANT.putAll(MERCHANTMANAGER_MERCHANT);
        SHOPMANAGER_OPERATE.putAll(MERCHANTMANAGER_OPERATE);
        SHOPMANAGER_ORDER.putAll(MERCHANTMANAGER_ORDER);
        SHOPMANAGER_REQUEST.putAll(MERCHANTMANAGER_REQUEST);
        SHOPMANAGER_SHOP.putAll(MERCHANTMANAGER_SHOP);
        SHOPMANAGER_VERIFY.putAll(MERCHANTMANAGER_VERIFY);
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
        EMPLOYEE_ACCOUNT.putAll(SHOPMANAGER_ACCOUNT);
        EMPLOYEE_DAILY_CLEAR.putAll(SHOPMANAGER_DAILY_CLEAR);
        EMPLOYEE_CLEAR_RECORD.putAll(SHOPMANAGER_CLEAR_RECORD);
        EMPLOYEE_CODE_ALGORITHM.putAll(SHOPMANAGER_CODE_ALGORITHM);
        EMPLOYEE_COMMODITY.putAll(SHOPMANAGER_COMMODITY);
        EMPLOYEE_CODE.putAll(SHOPMANAGER_CODE);
        EMPLOYEE_MERCHANT.putAll(SHOPMANAGER_MERCHANT);
        EMPLOYEE_OPERATE.putAll(SHOPMANAGER_OPERATE);
        EMPLOYEE_ORDER.putAll(SHOPMANAGER_ORDER);
        EMPLOYEE_REQUEST.putAll(SHOPMANAGER_REQUEST);
        EMPLOYEE_SHOP.putAll(SHOPMANAGER_SHOP);
        EMPLOYEE_VERIFY.putAll(SHOPMANAGER_VERIFY);


        EMPLOYEE_MERCHANT.remove("merchantId");
    }

    Map<String, String> ACCOUNT_STATUS       = new HashMap<>();
    Map<String, String> ACCOUNT_TYPE         = new HashMap<>();
    Map<String, String> CHANNEL              = new HashMap<>();
    Map<String, String> CLEAR_STATUS         = new HashMap<>();
    Map<String, String> COMMODITY_STATUS     = new HashMap<>();
    Map<String, String> COMMODITY_TYPE       = new HashMap<>();
    Map<String, String> COUPON_CODE_STATUS   = new HashMap<>();
    Map<String, String> GENEERATE_TYPE       = new HashMap<>();
    Map<String, String> INVOICE_STATUS       = new HashMap<>();
    Map<String, String> MERCHANT_STATUS      = new HashMap<>();
    Map<String, String> OPERATE_TARGET_TYPE  = new HashMap<>();
    Map<String, String> OPERATE_TYPE         = new HashMap<>();
    Map<String, String> ORDER_STATUS         = new HashMap<>();
    Map<String, String> REQUEST_OPERATE_TYPE = new HashMap<>();
    Map<String, String> REQUEST_STATUS       = new HashMap<>();
    Map<String, String> SHOP_STATUS          = new HashMap<>();
    Map<String, String> VERIFICATION_TYPE    = new HashMap<>();

    static void initEnum() {
        //-----------------------------------------------------------------校验--------------------------------------------------------------
        ACCOUNT_STATUS.put(AccountStatus.normal.getValue(), "正常");
        ACCOUNT_STATUS.put(AccountStatus.freeze.getValue(), "冻结");
        ACCOUNT_STATUS.put(AccountStatus.deleted.getValue(), "删除");
        ACCOUNT_STATUS.put(AccountStatus.deleted.getValue(), "注销");
        //-----------------------------------------------------------------校验--------------------------------------------------------------
        ACCOUNT_TYPE.put(AccountType.admin.getValue(), "管理员");
        ACCOUNT_TYPE.put(AccountType.merchantManager.getValue(), "商户管理员");
        ACCOUNT_TYPE.put(AccountType.shopManager.getValue(), "门店负责人");
        ACCOUNT_TYPE.put(AccountType.shopEmployee.getValue(), "门店员工");
        //-----------------------------------------------------------------校验--------------------------------------------------------------
        CHANNEL.put("1", "报文购买");
        CHANNEL.put("0", "管理员生成");
        //-----------------------------------------------------------------校验--------------------------------------------------------------
        CLEAR_STATUS.put("1", "已结算");
        CLEAR_STATUS.put("2", "未结算");
        //-----------------------------------------------------------------校验--------------------------------------------------------------
        COMMODITY_STATUS.put("1", "上架");
        COMMODITY_STATUS.put("2", "下架");
        COMMODITY_STATUS.put("3", "删除");
        COMMODITY_STATUS.put("5", "上架待审核");
        COMMODITY_STATUS.put("6", "下架待审核");
        //-----------------------------------------------------------------校验--------------------------------------------------------------
        COMMODITY_TYPE.put("1", "电子券");
        COMMODITY_TYPE.put("2", "代金券");
        //-----------------------------------------------------------------校验--------------------------------------------------------------
        COUPON_CODE_STATUS.put("1", "已核销");
        COUPON_CODE_STATUS.put("2", "未核销");
        COUPON_CODE_STATUS.put("3", "已过期");
        COUPON_CODE_STATUS.put("5", "已注销");
        //-----------------------------------------------------------------校验--------------------------------------------------------------
        GENEERATE_TYPE.put("1", "手动生成");
        GENEERATE_TYPE.put("0", "自动生成");
        //-----------------------------------------------------------------校验--------------------------------------------------------------
        INVOICE_STATUS.put("1", "未寄出");
        INVOICE_STATUS.put("2", "已寄出");
        //-----------------------------------------------------------------校验--------------------------------------------------------------
        MERCHANT_STATUS.put("1", "正常");
        MERCHANT_STATUS.put("2", "暂停合作");
        MERCHANT_STATUS.put("3", "删除");
        //-----------------------------------------------------------------校验--------------------------------------------------------------
        OPERATE_TARGET_TYPE.put("1", "门店");
        OPERATE_TARGET_TYPE.put("2", "商户管理员");
        OPERATE_TARGET_TYPE.put("3", "门店负责人");
        OPERATE_TARGET_TYPE.put("4", "门店员工");
        OPERATE_TARGET_TYPE.put("5", "商品");
        OPERATE_TARGET_TYPE.put("6", "串码");
        OPERATE_TARGET_TYPE.put("7", "账户");
        OPERATE_TARGET_TYPE.put("8", "商户");
        OPERATE_TARGET_TYPE.put("9", "统计");
        OPERATE_TARGET_TYPE.put("10", "申请");
        OPERATE_TARGET_TYPE.put("11", "日结算记录");
        OPERATE_TARGET_TYPE.put("23", "结算记录");
        //-----------------------------------------------------------------校验--------------------------------------------------------------
        OPERATE_TYPE.put("11", "添加门店");
        OPERATE_TYPE.put("12", "冻结门店");
        OPERATE_TYPE.put("13", "解除门店冻结");
        OPERATE_TYPE.put("14", "删除门店");
        OPERATE_TYPE.put("15", "修改门店");
        OPERATE_TYPE.put("16", "查询门店");
        OPERATE_TYPE.put("17", "注销门店");
        OPERATE_TYPE.put("18", "导入门店");
        OPERATE_TYPE.put("19", "下载导入模版");
        OPERATE_TYPE.put("21", "添加商户");
        OPERATE_TYPE.put("22", "冻结商户");
        OPERATE_TYPE.put("23", "解除商户冻结");
        OPERATE_TYPE.put("24", "删除商户");
        OPERATE_TYPE.put("25", "修改商户");
        OPERATE_TYPE.put("26", "查询商户");
        OPERATE_TYPE.put("29", "注销商户");
        OPERATE_TYPE.put("31", "添加门店负责人");
        OPERATE_TYPE.put("32", "冻结门店负责人");
        OPERATE_TYPE.put("33", "解除门店负责人冻结");
        OPERATE_TYPE.put("34", "删除门店负责人");
        OPERATE_TYPE.put("35", "修改门店负责人");
        OPERATE_TYPE.put("36", "查询门店负责人");
        OPERATE_TYPE.put("39", "注销门店负责人");
        OPERATE_TYPE.put("41", "添加门店员工");
        OPERATE_TYPE.put("42", "冻结门店员工");
        OPERATE_TYPE.put("43", "解除门店员工冻结");
        OPERATE_TYPE.put("44", "删除门店员工");
        OPERATE_TYPE.put("45", "修改门店员工");
        OPERATE_TYPE.put("46", "查询门店员工");
        OPERATE_TYPE.put("49", "注销门店员工");
        OPERATE_TYPE.put("51", "添加商品");
        OPERATE_TYPE.put("52", "下架商品");
        OPERATE_TYPE.put("53", "上架商品");
        OPERATE_TYPE.put("54", "删除商品");
        OPERATE_TYPE.put("55", "修改商品");
        OPERATE_TYPE.put("56", "查询商品");
        OPERATE_TYPE.put("59", "注销商品");
        OPERATE_TYPE.put("61", "核销串码");
        OPERATE_TYPE.put("62", "添加串码");
        OPERATE_TYPE.put("66", "查询串码");
        OPERATE_TYPE.put("67", "延期串码");
        OPERATE_TYPE.put("68", "注销串码");
        OPERATE_TYPE.put("69", "查询串码列表");
        OPERATE_TYPE.put("71", "添加账号");
        OPERATE_TYPE.put("72", "冻结账号");
        OPERATE_TYPE.put("73", "解除账号冻结");
        OPERATE_TYPE.put("74", "查询账号");
        OPERATE_TYPE.put("75", "删除账号");
        OPERATE_TYPE.put("76", "修改账号");
        OPERATE_TYPE.put("77", "查询账号");
        OPERATE_TYPE.put("78", "注销账号");
        OPERATE_TYPE.put("79", "修改账号密码");
        OPERATE_TYPE.put("81", "添加商户");
        OPERATE_TYPE.put("82", "解除商户冻结");
        OPERATE_TYPE.put("83", "冻结商户");
        OPERATE_TYPE.put("84", "删除商户");
        OPERATE_TYPE.put("85", "修改商户");
        OPERATE_TYPE.put("86", "查询商户");
        OPERATE_TYPE.put("87", "输入商户信息");
        OPERATE_TYPE.put("88", "查询商户");
        OPERATE_TYPE.put("89", "注销商户");
        OPERATE_TYPE.put("91", "统计门店");
        OPERATE_TYPE.put("92", "统计商户负责人");
        OPERATE_TYPE.put("93", "统计门店负责人");
        OPERATE_TYPE.put("94", "统计门店员工");
        OPERATE_TYPE.put("95", "统计商品");
        OPERATE_TYPE.put("96", "统计串码");
        OPERATE_TYPE.put("97", "统计账户");
        OPERATE_TYPE.put("98", "统计商户");
        OPERATE_TYPE.put("101", "导出门店");
        OPERATE_TYPE.put("102", "导出商户负责人");
        OPERATE_TYPE.put("103", "导出门店负责人");
        OPERATE_TYPE.put("104", "导出门店员工");
        OPERATE_TYPE.put("105", "导出商品");
        OPERATE_TYPE.put("106", "导出串码");
        OPERATE_TYPE.put("107", "导出账户");
        OPERATE_TYPE.put("108", "导出商户");
        OPERATE_TYPE.put("111", "查询日结算记录");
        OPERATE_TYPE.put("112", "导出日结算记录");
        OPERATE_TYPE.put("113", "结算日结算记录");
        OPERATE_TYPE.put("121", "查询结算记录");
        OPERATE_TYPE.put("122", "导出结算记录");
        OPERATE_TYPE.put("123", "添加结算记录");
        OPERATE_TYPE.put("124", "查询结算记录列表");
        OPERATE_TYPE.put("125", "修改结算记录");
        OPERATE_TYPE.put("131", "查询核销记录");
        OPERATE_TYPE.put("132", "导出核销记录");
        OPERATE_TYPE.put("133", "添加核销记录");
        OPERATE_TYPE.put("134", "查询核销记录列表");
        OPERATE_TYPE.put("135", "查询所有核销记录");
        OPERATE_TYPE.put("141", "请求添加商品");
        OPERATE_TYPE.put("142", "请求修改商品");
        OPERATE_TYPE.put("143", "请求下架商品");
        OPERATE_TYPE.put("144", "请求上架商品");
        OPERATE_TYPE.put("145", "请求删除商品");
        OPERATE_TYPE.put("146", "查询请求");
        OPERATE_TYPE.put("147", "通过请求");
        OPERATE_TYPE.put("148", "回绝请求");
        OPERATE_TYPE.put("151", "查询操作记录");
        OPERATE_TYPE.put("152", "查询操作记录列表");
        //-----------------------------------------------------------------校验--------------------------------------------------------------
        ORDER_STATUS.put("0", "未完成");
        ORDER_STATUS.put("1", "完成");
        ORDER_STATUS.put("2", "部分完成");
        //-----------------------------------------------------------------校验--------------------------------------------------------------
        REQUEST_OPERATE_TYPE.put("11", "添加门店");
        REQUEST_OPERATE_TYPE.put("12", "冻结门店");
        REQUEST_OPERATE_TYPE.put("13", "解除门店冻结");
        REQUEST_OPERATE_TYPE.put("14", "删除门店");
        REQUEST_OPERATE_TYPE.put("15", "修改门店");
        REQUEST_OPERATE_TYPE.put("16", "查询门店");
        REQUEST_OPERATE_TYPE.put("17", "注销门店");
        REQUEST_OPERATE_TYPE.put("18", "导入门店");
        REQUEST_OPERATE_TYPE.put("19", "下载导入模版");
        REQUEST_OPERATE_TYPE.put("21", "添加商户");
        REQUEST_OPERATE_TYPE.put("22", "冻结商户");
        REQUEST_OPERATE_TYPE.put("23", "解除商户冻结");
        REQUEST_OPERATE_TYPE.put("24", "删除商户");
        REQUEST_OPERATE_TYPE.put("25", "修改商户");
        REQUEST_OPERATE_TYPE.put("26", "查询商户");
        REQUEST_OPERATE_TYPE.put("29", "注销商户");
        REQUEST_OPERATE_TYPE.put("31", "添加门店负责人");
        REQUEST_OPERATE_TYPE.put("32", "冻结门店负责人");
        REQUEST_OPERATE_TYPE.put("33", "解除门店负责人冻结");
        REQUEST_OPERATE_TYPE.put("34", "删除门店负责人");
        REQUEST_OPERATE_TYPE.put("35", "修改门店负责人");
        REQUEST_OPERATE_TYPE.put("36", "查询门店负责人");
        REQUEST_OPERATE_TYPE.put("39", "注销门店负责人");
        REQUEST_OPERATE_TYPE.put("41", "添加门店员工");
        REQUEST_OPERATE_TYPE.put("42", "冻结门店员工");
        REQUEST_OPERATE_TYPE.put("43", "解除门店员工冻结");
        REQUEST_OPERATE_TYPE.put("44", "删除门店员工");
        REQUEST_OPERATE_TYPE.put("45", "修改门店员工");
        REQUEST_OPERATE_TYPE.put("46", "查询门店员工");
        REQUEST_OPERATE_TYPE.put("49", "注销门店员工");
        REQUEST_OPERATE_TYPE.put("51", "添加商品");
        REQUEST_OPERATE_TYPE.put("52", "下架商品");
        REQUEST_OPERATE_TYPE.put("53", "上架商品");
        REQUEST_OPERATE_TYPE.put("54", "删除商品");
        REQUEST_OPERATE_TYPE.put("55", "修改商品");
        REQUEST_OPERATE_TYPE.put("56", "查询商品");
        REQUEST_OPERATE_TYPE.put("59", "注销商品");
        REQUEST_OPERATE_TYPE.put("61", "核销串码");
        REQUEST_OPERATE_TYPE.put("62", "添加串码");
        REQUEST_OPERATE_TYPE.put("66", "查询串码");
        REQUEST_OPERATE_TYPE.put("67", "延期串码");
        REQUEST_OPERATE_TYPE.put("68", "注销串码");
        REQUEST_OPERATE_TYPE.put("69", "查询串码列表");
        REQUEST_OPERATE_TYPE.put("71", "添加账号");
        REQUEST_OPERATE_TYPE.put("72", "冻结账号");
        REQUEST_OPERATE_TYPE.put("73", "解除账号冻结");
        REQUEST_OPERATE_TYPE.put("74", "查询账号");
        REQUEST_OPERATE_TYPE.put("75", "删除账号");
        REQUEST_OPERATE_TYPE.put("76", "修改账号");
        REQUEST_OPERATE_TYPE.put("77", "查询账号");
        REQUEST_OPERATE_TYPE.put("78", "注销账号");
        REQUEST_OPERATE_TYPE.put("79", "修改账号密码");
        REQUEST_OPERATE_TYPE.put("81", "添加商户");
        REQUEST_OPERATE_TYPE.put("82", "解除商户冻结");
        REQUEST_OPERATE_TYPE.put("83", "冻结商户");
        REQUEST_OPERATE_TYPE.put("84", "删除商户");
        REQUEST_OPERATE_TYPE.put("85", "修改商户");
        REQUEST_OPERATE_TYPE.put("86", "查询商户");
        REQUEST_OPERATE_TYPE.put("87", "输入商户信息");
        REQUEST_OPERATE_TYPE.put("88", "查询商户");
        REQUEST_OPERATE_TYPE.put("89", "注销商户");
        REQUEST_OPERATE_TYPE.put("91", "统计门店");
        REQUEST_OPERATE_TYPE.put("92", "统计商户负责人");
        REQUEST_OPERATE_TYPE.put("93", "统计门店负责人");
        REQUEST_OPERATE_TYPE.put("94", "统计门店员工");
        REQUEST_OPERATE_TYPE.put("95", "统计商品");
        REQUEST_OPERATE_TYPE.put("96", "统计串码");
        REQUEST_OPERATE_TYPE.put("97", "统计账户");
        REQUEST_OPERATE_TYPE.put("98", "统计商户");
        REQUEST_OPERATE_TYPE.put("101", "导出门店");
        REQUEST_OPERATE_TYPE.put("102", "导出商户负责人");
        REQUEST_OPERATE_TYPE.put("103", "导出门店负责人");
        REQUEST_OPERATE_TYPE.put("104", "导出门店员工");
        REQUEST_OPERATE_TYPE.put("105", "导出商品");
        REQUEST_OPERATE_TYPE.put("106", "导出串码");
        REQUEST_OPERATE_TYPE.put("107", "导出账户");
        REQUEST_OPERATE_TYPE.put("108", "导出商户");
        REQUEST_OPERATE_TYPE.put("111", "查询日结算记录");
        REQUEST_OPERATE_TYPE.put("112", "导出日结算记录");
        REQUEST_OPERATE_TYPE.put("113", "结算日结算记录");
        REQUEST_OPERATE_TYPE.put("121", "查询结算记录");
        REQUEST_OPERATE_TYPE.put("122", "导出结算记录");
        REQUEST_OPERATE_TYPE.put("123", "添加结算记录");
        REQUEST_OPERATE_TYPE.put("124", "查询结算记录列表");
        REQUEST_OPERATE_TYPE.put("125", "修改结算记录");
        REQUEST_OPERATE_TYPE.put("131", "查询核销记录");
        REQUEST_OPERATE_TYPE.put("132", "导出核销记录");
        REQUEST_OPERATE_TYPE.put("133", "添加核销记录");
        REQUEST_OPERATE_TYPE.put("134", "查询核销记录列表");
        REQUEST_OPERATE_TYPE.put("135", "查询所有核销记录");
        REQUEST_OPERATE_TYPE.put("141", "请求添加商品");
        REQUEST_OPERATE_TYPE.put("142", "请求修改商品");
        REQUEST_OPERATE_TYPE.put("143", "请求下架商品");
        REQUEST_OPERATE_TYPE.put("144", "请求上架商品");
        REQUEST_OPERATE_TYPE.put("145", "请求删除商品");
        REQUEST_OPERATE_TYPE.put("146", "查询请求");
        REQUEST_OPERATE_TYPE.put("147", "通过请求");
        REQUEST_OPERATE_TYPE.put("148", "回绝请求");
        REQUEST_OPERATE_TYPE.put("151", "查询操作记录");
        REQUEST_OPERATE_TYPE.put("152", "查询操作记录列表");
        //-----------------------------------------------------------------校验--------------------------------------------------------------
        REQUEST_STATUS.put("1", "已提交");
        REQUEST_STATUS.put("2", "已通过");
        REQUEST_STATUS.put("3", "已拒绝");
        //-----------------------------------------------------------------校验--------------------------------------------------------------
        SHOP_STATUS.put("1", "正常服务");
        SHOP_STATUS.put("2", "暂停服务");
        SHOP_STATUS.put("3", "删除");
        //-----------------------------------------------------------------校验--------------------------------------------------------------
        VERIFICATION_TYPE.put("1", "手动核销");
        VERIFICATION_TYPE.put("2", "扫码核销");
        VERIFICATION_TYPE.put("3", "未知");
    }

    BiFunction<Class, String, String> getAllFieldCN             = (c, f) -> {
        init();
        if (Account.class.equals(c))
            return ALL_ACCOUNT.get(f);
        else if (ClearDailyRecord.class.equals(c))
            return ALL_CLEAR_RECORD.get(f);
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
            return ADMIN_CLEAR_RECORD.get(f);
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
            return MERCHANTMANAGER_CLEAR_RECORD.get(f);
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
            return SHOPMANAGER_CLEAR_RECORD.get(f);
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
    BiFunction<Class, String, String> getEmployeeFieldCN     = (c, f) -> {
        init();
        if (Account.class.equals(c))
            return EMPLOYEE_ACCOUNT.get(f);
        else if (ClearDailyRecord.class.equals(c))
            return EMPLOYEE_CLEAR_RECORD.get(f);
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
    BiFunction<Class, String, String> getEnumCN = (c, f) -> {
        init();
        if (!c.isEnum())
            throw new IllegalArgumentException("c must be enum type !");
        if (AccountStatus.class.equals(c))
            return ACCOUNT_STATUS.get(f);
        if (AccountType.class.equals(c))
            return ACCOUNT_TYPE.get(f);
        if (Channel.class.equals(c))
            return CHANNEL.get(f);
        if (ClearStatus.class.equals(c))
            return CLEAR_STATUS.get(f);
        if (CommodityStatus.class.equals(c))
            return COMMODITY_STATUS.get(f);
        if (CommodityType.class.equals(c))
            return COMMODITY_TYPE.get(f);
        if (CouponCodeStatus.class.equals(c))
            return COUPON_CODE_STATUS.get(f);
        if (GenerateType.class.equals(c))
            return GENEERATE_TYPE.get(f);
        if (InvoiceStatus.class.equals(c))
            return INVOICE_STATUS.get(f);
        if (MerchantStatus.class.equals(c))
            return MERCHANT_STATUS.get(f);
        if (OperateTargetType.class.equals(c))
            return OPERATE_TARGET_TYPE.get(f);
        if (OperateType.class.equals(c))
            return OPERATE_TYPE.get(f);
        if (OrderStatus.class.equals(c))
            return ORDER_STATUS.get(f);
        if (RequestOperateType.class.equals(c))
            return REQUEST_OPERATE_TYPE.get(f);
        if (RequestStatus.class.equals(c))
            return REQUEST_STATUS.get(f);
        if (ShopStatus.class.equals(c))
            return SHOP_STATUS.get(f);
        if (VerificationType.class.equals(c))
            return VERIFICATION_TYPE.get(f);
        else
            return null;
    };

    static void init(){
        if(ALL_ACCOUNT.isEmpty()){
            initAll();
            initAdmin();
            initMerchant();
            initShopManager();
            initShopEmployee();
            initEnum();
        }
    }
}
