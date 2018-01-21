package com.lanxi.couponcode.view;

import com.lanxi.couponcode.spi.aop.CheckLogin;
import com.lanxi.couponcode.spi.assist.FileAssit;
import com.lanxi.couponcode.spi.assist.FileDelete;
import com.lanxi.couponcode.view.assist.FileAssist;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.annotations.Comment;
import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.couponcode.spi.consts.annotations.LoginCheck;
import com.lanxi.couponcode.spi.consts.annotations.SetUtf8;
import com.lanxi.couponcode.spi.consts.enums.*;
import com.lanxi.couponcode.spi.service.*;
import com.lanxi.util.entity.LogFactory;
import com.lanxi.util.utils.LoggerUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;

import static com.lanxi.couponcode.spi.assist.ArgAssist.*;

/**
 * 商户管理端 Created by yangyuanjian on 2017/11/20.
 */
@Controller
@RequestMapping ("merchantManager")
@EasyLog (LoggerUtil.LogLevel.INFO)
public class MerchantManageController {
    @Resource (name = "accountControllerServiceRef")
    private AccountService accountService;
    @Resource (name = "codeControllerServiceRef")
    private CouponService codeService;
    @Resource (name = "clearControllerServiceRef")
    private ClearService clearService;
    @Resource (name = "operateRecordControllerServiceRef")
    private OperateRecordService operateRecordService;
    @Resource (name = "requestControllerServiceRef")
    private RequestService requestService;
    @Resource (name = "verificationRecordControllerServiceRef")
    private VerificationRecordService verificationRecordService;
    @Resource (name = "merchantControllerServiceRef")
    private MerchantService merchantService;
    @Resource (name = "shopControllerServiceRef")
    private ShopService shopService;
    @Resource (name = "commodityControllerServiceRef")
    private CommodityService commodityService;
    @Resource(name = "shopVerifyService")
    private ShopDailyVerifyStatsticService shopDailyVerifyStatsticService;
    @Comment ("aop注解-用于处理文件上传时的参数问题")
    @Resource
    private CheckLogin checkLogin;

    /* 完善商户详细信息 */
    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "inputMerchantInfo", produces = "application/json;charset=utf-8")
    public String inputMerchantInfo(HttpServletRequest req, HttpServletResponse res) {
        try {
            String merchantName = getArg.apply(req, "merchantName");
            String serveExplain = getArg.apply(req, "serveExplain");
            //        String workAddress = getArg.apply(req, "workAddress");
            //        String minuteWorkAddress = getArg.apply(req, "minuteWorkAddress");
            String charterCode = getArg.apply(req, "charterCode");
            String oraganizingCode = getArg.apply(req, "oraganizingCode");
            String principal = getArg.apply(req, "principal");
            String linkMan = getArg.apply(req, "linkMan");
            String linkManPhone = getArg.apply(req, "linkManPhone");
            String serviceTel = getArg.apply(req, "serviceTel");
            String email = getArg.apply(req, "email");
            String operaterIdStr = getArg.apply(req, "operaterId");
            String merchantIdStr = getArg.apply(req, "merchantId");
            String registeraddress=getArg.apply(req,"registerAddress");
            String minuteRegisterAddress=getArg.apply(req,"minuteRegisterAddress");
            Long operaterId = parseArg(operaterIdStr, Long.class);
            Long merchantId = parseArg(merchantIdStr, Long.class);
            return merchantService
                    .inputMerchantInfo(merchantName, serveExplain, null, null, charterCode,
                            oraganizingCode, principal, linkMan, linkManPhone, serviceTel, email, operaterId, merchantId,registeraddress,minuteRegisterAddress)
                    .toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    /* 完善商户组织机构代码证 */
    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "organizingInstitutionBarCodePicUpLoad", produces = "application/json;charset=utf-8")
    public String organizingInstitutionBarCodePicUpLoad(HttpServletRequest req, HttpServletResponse res, @RequestParam ("file") CommonsMultipartFile file) {
        try {
            String operaterIdStr = getArg.apply(req, "operaterId");
            String merchantIdStr = getArg.apply(req, "merchantId");
            Long operaterId = parseArg(operaterIdStr, Long.class);
            Long merchantId = parseArg(merchantIdStr, Long.class);

            if (!checkLogin.checkLogin(getArg.apply(req,"token"), operaterIdStr))
                return new RetMessage<>(RetCodeEnum.fail, "not login !", null).toJson();

            String result = null;
            try {
                if (!file.isEmpty()) {
                    File file2 = new File(file.getOriginalFilename());
                    InputStream is = file.getInputStream();
                    OutputStream os = new FileOutputStream(file2);
                    byte[] temp = new byte[1024];
                    int size = -1;
                    while ((size = is.read(temp)) != -1) {
                        os.write(temp, 0, size);
                    }
                    os.close();
                    result = merchantService.organizingInstitutionBarCodePicUpLoad(file2, operaterId, merchantId).toJson();
                    FileDelete.add(file2);
                }
            } catch (Exception e) {
                LogFactory.error(this, "上传组织机构代码证时发生异常");

            }
            return result;
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    /* 完善商户营业执照 */
    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "businessLicensePicUpLoad", produces = "application/json;charset=utf-8")
    public String businessLicensePicUpLoad(HttpServletRequest req, HttpServletResponse res, @RequestParam ("file") CommonsMultipartFile file) {
        try {
            String operaterIdStr = getArg.apply(req, "operaterId");
            String merchantIdStr = getArg.apply(req, "merchantId");
            Long operaterId = parseArg(operaterIdStr, Long.class);
            Long merchantId = parseArg(merchantIdStr, Long.class);
            if (!checkLogin.checkLogin(getArg.apply(req,"token"), operaterIdStr))
                return new RetMessage<>(RetCodeEnum.fail, "not login !", null).toJson();
            String result = null;
            try {
                if (!file.isEmpty()) {
                    File file2 = new File(file.getOriginalFilename());
                    InputStream is = file.getInputStream();
                    OutputStream os = new FileOutputStream(file2);
                    byte[] temp = new byte[1024];
                    int size = -1;
                    while ((size = is.read(temp)) != -1) {
                        os.write(temp, 0, size);
                    }
                    os.close();
                    result = merchantService.businessLicensePicUpLoad(file2, operaterId, merchantId).toJson();
                    FileDelete.add(file2);
                }
            } catch (Exception e) {
                LogFactory.error(this, "上传组织机构代码证时发生异常");

            }
            return result;
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    /* 完善商户其他证明资料 */
    @SetUtf8
    @LoginCheck
    @RequestMapping (value = "otherPicUpLoad", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String otherPicUpLoad(HttpServletRequest req, HttpServletResponse res, @RequestParam ("file") CommonsMultipartFile file) {
        try {
            String operaterIdStr = getArg.apply(req, "operaterId");
            String merchantIdStr = getArg.apply(req, "merchantId");
            Long operaterId = parseArg(operaterIdStr, Long.class);
            Long merchantId = parseArg(merchantIdStr, Long.class);
            if (!checkLogin.checkLogin(getArg.apply(req,"token"), operaterIdStr))
                return new RetMessage<>(RetCodeEnum.fail, "not login !", null).toJson();
            String result = null;
            try {
                if (!file.isEmpty()) {
                    File file2 = new File(file.getOriginalFilename());
                    InputStream is = file.getInputStream();
                    OutputStream os = new FileOutputStream(file2);
                    byte[] temp = new byte[1024];
                    int size = -1;
                    while ((size = is.read(temp)) != -1) {
                        os.write(temp, 0, size);
                    }
                    os.close();
                    result = merchantService.otherPicUpLoad(file2, operaterId, merchantId).toJson();
                    FileDelete.add(file2);
                }
            } catch (Exception e) {
                LogFactory.error(this, "上传组织机构代码证时发生异常");

            }
            return result;
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    /* 修改商户详细信息 */
    @SetUtf8
    @LoginCheck
    @RequestMapping (value = "modifyMerchantInfo", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String modifyMerchantInfo(HttpServletRequest req, HttpServletResponse res) {
        try {
            String merchantName = getArg.apply(req, "merchantName");
            String serveExplain = getArg.apply(req, "serveExplain");
            String workAddress = getArg.apply(req, "workAddress");
            String minuteWorkAddress = getArg.apply(req, "minuteWorkAddress");
            String charterCode = getArg.apply(req, "charterCode");
            String oraganizingCode = getArg.apply(req, "oraganizingCode");
            String principal = getArg.apply(req, "principal");
            String linkMan = getArg.apply(req, "linkMan");
            String linkManPhone = getArg.apply(req, "linkManPhone");
            String serviceTel = getArg.apply(req, "serviceTel");
            String email = getArg.apply(req, "email");
            String registeraddress=getArg.apply(req,"registeraddress");
            String minuteRegisterAddress=getArg.apply(req,"minuteRegisterAddress");
            String operaterIdStr = getArg.apply(req, "operaterId");
            String merchantIdStr = getArg.apply(req, "merchantId");
            Long operaterId = parseArg(operaterIdStr, Long.class);
            Long merchantId = parseArg(merchantIdStr, Long.class);
            return merchantService
                    .modifyMerchantInfo(merchantName, serveExplain, workAddress, minuteWorkAddress, charterCode,
                            oraganizingCode, principal, linkMan, linkManPhone, serviceTel, email, operaterId, merchantId,registeraddress,minuteRegisterAddress)
                    .toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    /* 修改商户组织机构代码证 */
    @SetUtf8
    @LoginCheck
    @RequestMapping (value = "modifyOrganizingInstitutionBarCodePic", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String modifyOrganizingInstitutionBarCodePic(HttpServletRequest req, HttpServletResponse res, @RequestParam ("file") CommonsMultipartFile file) {
        try {
            String operaterIdStr = getArg.apply(req, "operaterId");
            String merchantIdStr = getArg.apply(req, "merchantId");
            Long operaterId = parseArg(operaterIdStr, Long.class);
            Long merchantId = parseArg(merchantIdStr, Long.class);
            String result = null;
            try {
                if (!file.isEmpty()) {
                    File file2 = new File(file.getOriginalFilename());
                    InputStream is = file.getInputStream();
                    OutputStream os = new FileOutputStream(file2);
                    byte[] temp = new byte[1024];
                    int size = -1;
                    while ((size = is.read(temp)) != -1) {
                        os.write(temp, 0, size);
                    }
                    os.close();
                    result = merchantService.modifyOrganizingInstitutionBarCodePic(file2, operaterId, merchantId).toJson();
                }
            } catch (Exception e) {
                LogFactory.error(this, "上传组织机构代码证时发生异常");
            }
            return result;
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    /* 修改商户营业执照 */
    @SetUtf8
    @LoginCheck
    @RequestMapping (value = "modifyBusinessLicensePic", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String modifyBusinessLicensePic(HttpServletRequest req, HttpServletResponse res, CommonsMultipartFile file) {
        try {
            String operaterIdStr = getArg.apply(req, "operaterId");
            String merchantIdStr = getArg.apply(req, "merchantId");
            Long operaterId = parseArg(operaterIdStr, Long.class);
            Long merchantId = parseArg(merchantIdStr, Long.class);
            String result = null;
            try {
                if (!file.isEmpty()) {
                    File file2 = new File(file.getOriginalFilename());
                    InputStream is = file.getInputStream();
                    OutputStream os = new FileOutputStream(file2);
                    byte[] temp = new byte[1024];
                    int size = -1;
                    while ((size = is.read(temp)) != -1) {
                        os.write(temp, 0, size);
                    }
                    os.close();
                    result = merchantService.modifyBusinessLicensePic(file2, operaterId, merchantId).toJson();
                }
            } catch (Exception e) {
                LogFactory.error(this, "上传组织机构代码证时发生异常");

            }
            return result;
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    /* 修改商户其他证明资料 */
    @SetUtf8
    @LoginCheck
    @RequestMapping (value = "modifyOtherPic", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String modifyOtherPic(HttpServletRequest req, HttpServletResponse res, CommonsMultipartFile file) {
        try {
            String operaterIdStr = getArg.apply(req, "operaterId");
            String merchantIdStr = getArg.apply(req, "merchantId");
            Long operaterId = parseArg(operaterIdStr, Long.class);
            Long merchantId = parseArg(merchantIdStr, Long.class);
            String result = null;
            try {
                if (!file.isEmpty()) {
                    File file2 = new File(file.getOriginalFilename());
                    InputStream is = file.getInputStream();
                    OutputStream os = new FileOutputStream(file2);
                    byte[] temp = new byte[1024];
                    int size = -1;
                    while ((size = is.read(temp)) != -1) {
                        os.write(temp, 0, size);
                    }
                    os.close();
                    result = merchantService.modifyOtherPic(file2, operaterId, merchantId).toJson();
                }
            } catch (Exception e) {
                LogFactory.error(this, "上传组织机构代码证时发生异常");

            }
            return result;
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    /* 冻结门店 */
    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "freezeShop", produces = "application/json;charset=utf-8")
    public String freezeShop(HttpServletRequest req, HttpServletResponse res) {
        try {
            String shopIdStr = getArg.apply(req, "shopId");
            String operaterIdStr = getArg.apply(req, "operaterId");
            Long operaterId = toLongArg.apply(operaterIdStr);
            Long shopId = toLongArg.apply(shopIdStr);
            return shopService.freezeShop(shopId, operaterId).toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    /* 开启门店 */
    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "unfreezeShop", produces = "application/json;charset=utf-8")
    public String unfreezeShop(HttpServletRequest req, HttpServletResponse res) {
        try {
            String shopIdStr = getArg.apply(req, "shopId");
            String operaterIdStr = getArg.apply(req, "operaterId");
            Long operaterId = toLongArg.apply(operaterIdStr);
            Long shopId = toLongArg.apply(shopIdStr);
            return shopService.unfreezeShop(shopId, operaterId).toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    /* 修改门店 */
    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "modifyShop", produces = "application/json;charset=utf-8")
    public String modifyShop(HttpServletRequest req, HttpServletResponse res) {
        try {
            String shopName = getArg.apply(req, "shopName");
            String shopAddress = getArg.apply(req, "shopAddress");
            String minuteShopAddress = getArg.apply(req, "minuteShopAddress");
            String serviceTel = getArg.apply(req, "serviceTel");
            String operaterIdStr = getArg.apply(req, "operaterId");
            String shopIdStr = getArg.apply(req, "shopId");
            Long operaterId = toLongArg.apply(operaterIdStr);
            Long shopId = toLongArg.apply(shopIdStr);
            return shopService.modifyShop(shopName, shopAddress, minuteShopAddress, serviceTel, shopId, operaterId)
                    .toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    /* 查询门店 */
    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "queryShop", produces = "application/json;charset=utf-8")
    public String queryShop(HttpServletRequest req, HttpServletResponse res) {
        try {
            String merchantName = getArg.apply(req, "merchantName");
            String shopName = getArg.apply(req, "shopName");
            String shopAddress = getArg.apply(req, "shopAddress");
            String shopStatusStr = getArg.apply(req, "shopStatus");
            String pageNumStr = getArg.apply(req, "pageNum");
            String pageSizeStr = getArg.apply(req, "pageSize");
            String operaterIdStr = getArg.apply(req, "operaterId");
            ShopStatus shopStatus = ShopStatus.getType(shopStatusStr);
            Integer pageNum = toIntArg.apply(pageNumStr);
            Integer pageSize = toIntArg.apply(pageSizeStr);
            Long operaterId = toLongArg.apply(operaterIdStr);
            return shopService
                    .queryShop(merchantName, shopName, shopStatus, shopAddress, pageNum, pageSize, operaterId)
                    .toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }

    }

    /* 导出 */
    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "queryShopsExport", produces = "application/octet-stream")
    public String queryShopsExport(HttpServletRequest req, HttpServletResponse res) {
        try {
            String shopName = getArg.apply(req, "shopName");
            String shopAddress = getArg.apply(req, "shopAddress");
            String shopStatusStr = getArg.apply(req, "shopStatus");
            String pageNumStr = getArg.apply(req, "pageNum");
            String pageSizeStr = getArg.apply(req, "pageSize");
            String operaterIdStr = getArg.apply(req, "operaterId");
            String merchantIdStr = getArg.apply(req, "merchantId");
            ShopStatus shopStatus = ShopStatus.getType(shopStatusStr);
            Integer pageNum = toIntArg.apply(pageNumStr);
            Integer pageSize = toIntArg.apply(pageSizeStr);
            Long operaterId = toLongArg.apply(operaterIdStr);
            Long merchantId = toLongArg.apply(merchantIdStr);
            File file = shopService.queryShopsExport(shopName, shopAddress, shopStatus, pageNum, pageSize, merchantId, operaterId).getDetail();
            return FileAssist.exportTest(file, res);
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
        //        try {
//            FileAssit.write(file,res.getOutputStream());
//			file.delete();
//		} catch (IOException e) {
//			LogFactory.error(this,"导出门店时发生异常",e);
//		}


    }

    /* 商户负责人添加门店管理员和核销员账户 */
    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "addAccount", produces = "application/json;charset=utf-8")
    public String addAccount(HttpServletRequest req, HttpServletResponse res) {
        try {
            String userName = getArg.apply(req, "userName");
            String phone = getArg.apply(req, "phone");
            String operaterIdStr = getArg.apply(req, "operaterId");
            String typeStr = getArg.apply(req, "type");
            String shopIdStr = getArg.apply(req, "shopId");
            String shopName = getArg.apply(req, "shopName");
            Long shopId = parseArg(shopIdStr, Long.class);
            Long operaterId = parseArg(operaterIdStr, Long.class);
            AccountType type = AccountType.getType(typeStr);
            return accountService.merchantAddAccount(type, userName, phone, shopName, shopId, operaterId).toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    /* 商户管理员查询账户 */
    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "queryAccounts", produces = "application/json;charset=utf-8")
    public String queryAccounts(HttpServletRequest req, HttpServletResponse res) {
        try {
            String phone = getArg.apply(req, "phone");
            String typeStr = getArg.apply(req, "type");
            AccountType type = AccountType.getType(typeStr);
            String operaterIdStr = getArg.apply(req, "operaterId");
            Long operaterId = parseArg(operaterIdStr, Long.class);
            String statusStr = getArg.apply(req, "status");
            AccountStatus status = AccountStatus.getType(statusStr);
            String pageNumStr = getArg.apply(req, "pageNum");
            String pageSizeStr = getArg.apply(req, "pageSize");
            Integer pageNum = parseArg(pageNumStr, Integer.class);
            Integer pageSize = parseArg(pageSizeStr, Integer.class);
            String shopIdStr = getArg.apply(req, "shopId");
            Long shopId=parseArg(shopIdStr,Long.class);
            return accountService.merchantQueryAccounts(phone, shopId, type, status, pageNum, pageSize, operaterId)
                    .toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    /* 冻结账户 */
    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "freezeAccount", produces = "application/json;charset=utf-8")
    public String freezeAccount(HttpServletRequest req, HttpServletResponse res) {
        try {
            String operaterIdStr = getArg.apply(req, "operaterId");
            Long operaterId = parseArg(operaterIdStr, Long.class);
            String accountIdStr = getArg.apply(req, "accountId");
            Long accountId = parseArg(accountIdStr, Long.class);
            return accountService.freezeAccount(accountId, operaterId).toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    /* 开启账户 */
    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "unfreezeAccount", produces = "application/json;charset=utf-8")
    public String unfreezeAccount(HttpServletRequest req, HttpServletResponse res) {
        try {
            String operaterIdStr = getArg.apply(req, "operaterId");
            Long operaterId = parseArg(operaterIdStr, Long.class);
            String accountIdStr = getArg.apply(req, "accountId");
            Long accountId = parseArg(accountIdStr, Long.class);
            return accountService.unfreezeAccount(accountId, operaterId).toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    /* 删除账户 */
    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "delAccount", produces = "application/json;charset=utf-8")
    public String deleteAccount(HttpServletRequest req, HttpServletResponse res) {
        try {
            String operaterIdStr = getArg.apply(req, "operaterId");
            Long operaterId = parseArg(operaterIdStr, Long.class);
            String accountIdStr = getArg.apply(req, "accountId");
            Long accountId = parseArg(accountIdStr, Long.class);
            return accountService.delAccount(accountId, operaterId).toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    // -------------------------------------------------------------------------------------------------------------------------------
    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "destroyCode", produces = "application/json;charset=utf-8")
    public String destroyCode(HttpServletRequest req, HttpServletResponse res) {
        try {
            String codIdStr = getArg.apply(req, "codeId");
            String operaterIdStr = getArg.apply(req, "operaterId");
            String codeStr = getArg.apply(req, "code");

            Long codeId = parseArg(codIdStr, Long.class);
            Long operaterId = parseArg(operaterIdStr, Long.class);
            Long code = parseArg(codeStr, Long.class);

            if (codeId == null)
                return codeService.destroyCode(code, null, operaterId).toJson();
            else
                return codeService.destroyCode(codeId, operaterId).toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "postoneCode", produces = "application/json;charset=utf-8")
    public String postoneCode(HttpServletRequest req, HttpServletResponse res) {
        try {
            String codIdStr = getArg.apply(req, "codeId");
            String operaterIdStr = getArg.apply(req, "operaterId");
            Long codeId = parseArg(codIdStr, Long.class);
            Long operaterId = parseArg(operaterIdStr, Long.class);
            return codeService.postoneCode(codeId, operaterId).toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "queryCode", produces = "application/json;charset=utf-8")
    public String queryCode(HttpServletRequest req, HttpServletResponse res) {
        try {
            String codIdStr = getArg.apply(req, "codeId");
            String operaterIdStr = getArg.apply(req, "operaterId");
            String codeStr = getArg.apply(req, "code");
            Long codeId = parseArg(codIdStr, Long.class);
            Long operaterId = parseArg(operaterIdStr, Long.class);
            Long code = parseArg(codeStr, Long.class);
            if (codeId == null)
                return codeService.couponCodeInfo(null, code, operaterId).toJson();
            else
                return codeService.couponCodeInfo(codeId, operaterId).toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }
//    @SetUtf8
//    @LoginCheck
//    @ResponseBody
//    @RequestMapping (value = "queryCodes", produces = "application/json;charset=utf-8")
//    public String queryCodes(HttpServletRequest req, HttpServletResponse res) {
//        String timeStart = getArg.apply(req, "timeStart");
//        String timeEnd = getArg.apply(req, "timeStop");
//        String merchantName = getArg.apply(req, "merchantName");
//        String commodityName = getArg.apply(req, "commodityName");
//
//        String codeStr = getArg.apply(req, "code");
//        String codeIdStr = getArg.apply(req, "codeId");
//        String pageNumStr = getArg.apply(req, "pageNum");
//        String pageSizeStr = getArg.apply(req, "pageSize");
//        String operaterIdStr = getArg.apply(req, "operaterId");
//
//        Long code = parseArg(codeStr, Long.class);
//        Long codeId = parseArg(codeIdStr, Long.class);
//        Long operaterId = parseArg(operaterIdStr, Long.class);
//        Long commodityId= (Long) getArgDir.apply(getArg.apply(req,"commodity_id"),Long.class);
//        Integer pageNum = parseArg(pageNumStr, Integer.class);
//        Integer pageSize = parseArg(pageSizeStr, Integer.class);
//
//        return codeService.queryCodes(timeStart, timeEnd, merchantName, commodityName, code, codeId, commodityId, pageNum, pageSize,
//                                      operaterId).toJson();
//    }
    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "queryDailyRecords", produces = "application/json;charset=utf-8")
    public String queryDailyRecords(HttpServletRequest req, HttpServletResponse res) {
        try {
            String timeStart = getArg.apply(req, "timeStart");
            String timeEnd = getArg.apply(req, "timeStop");
            String clearStatusStr = getArg.apply(req, "clearStatus");
            String pageNumStr = getArg.apply(req, "pageNum");
            String pageSizeStr = getArg.apply(req, "pageSize");
            String operaterIdStr = getArg.apply(req, "operaterId");
            String shopName=getArg.apply(req,"shopName");
//            ClearStatus clearStatus = ClearStatus.getType(clearStatusStr);
            Long operaterId = parseArg(operaterIdStr, Long.class);
            Integer pageNum = parseArg(pageNumStr, Integer.class);
            Integer pageSize = parseArg(pageSizeStr, Integer.class);

            return shopDailyVerifyStatsticService.queryStatsticsList(shopName,null,timeStart,timeEnd,pageNum,pageSize,operaterId).toJson();
//            return clearService.queryDailyRecords(timeStart, timeEnd, clearStatus, pageNum, pageSize, operaterId).toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "queryDailyRecord", produces = "application/json;charset=utf-8")
    public String queryDailyRecord(HttpServletRequest req, HttpServletResponse res) {
        try {
            String recordIdStr = getArg.apply(req, "recordId");
            String operaterIdStr = getArg.apply(req, "operaterId");
            Long recordId = parseArg(recordIdStr, Long.class);
            Long operaterId = parseArg(operaterIdStr, Long.class);
            return  shopDailyVerifyStatsticService.queryStatsticsInfo(recordId,operaterId).toJson();
//            return clearService.queryDailyRecordInfo(recordId, operaterId).toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "queryClearRecords", produces = "application/json;charset=utf-8")
    public String queryClearRecords(HttpServletRequest req, HttpServletResponse res) {
        try {
            String timeStart = getArg.apply(req, "timeStart");
            String timeEnd = getArg.apply(req, "timeStop");
            String clearStatusStr = getArg.apply(req, "clearStatus");
            String invoiceStatusStr = getArg.apply(req, "invoiceStatus");
            String pageNumStr = getArg.apply(req, "pageNum");
            String pageSizeStr = getArg.apply(req, "pageSize");
            String operaterIdStr = getArg.apply(req, "operaterId");

            ClearStatus clearStatus = ClearStatus.getType(clearStatusStr);
            Long operaterId = parseArg(operaterIdStr, Long.class);
            Integer pageNum = parseArg(pageNumStr, Integer.class);
            Integer pageSize = parseArg(pageSizeStr, Integer.class);
            InvoiceStatus invoiceStatus = InvoiceStatus.getType(invoiceStatusStr);
            return clearService
                    .queryClearRecords(timeStart, timeEnd, clearStatus, invoiceStatus, pageNum, pageSize, operaterId)
                    .toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "queryClearRecord", produces = "application/json;charset=utf-8")
    public String queryClearRecord(HttpServletRequest req, HttpServletResponse res) {
        try {
            String recordIdStr = getArg.apply(req, "recordId");
            String operaterIdStr = getArg.apply(req, "operaterId");
            Long recordId = parseArg(recordIdStr, Long.class);
            Long operaterId = parseArg(operaterIdStr, Long.class);

            return clearService.queryRecordInfo(recordId, operaterId).toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "queryOperateRecords", produces = "application/json;charset=utf-8")
    public String queryOperateRecords(HttpServletRequest req, HttpServletResponse res) {
        try {
            String typeStr = getArg.apply(req, "type");
            String targetTypeStr = getArg.apply(req, "targetType");
            String accountTypeStr = getArg.apply(req, "accountType");
            String shopName = getArg.apply(req, "shopName");
            String timeStart = getArg.apply(req, "timeStart");
            String timeEnd = getArg.apply(req, "timeStop");
            String name = getArg.apply(req, "name");
            String phone = getArg.apply(req, "phone");
            String pageNumStr = getArg.apply(req, "pageNum");
            String pageSizeStr = getArg.apply(req, "pageSize");
            String operaterIdStr = getArg.apply(req, "operaterId");

            OperateType type = OperateType.getType(typeStr);
            OperateTargetType targetType = OperateTargetType.getType(targetTypeStr);
            AccountType accountType = AccountType.getType(accountTypeStr);
            Long operaterId = parseArg(operaterIdStr, Long.class);
            Integer pageNum = parseArg(pageNumStr, Integer.class);
            Integer pageSize = parseArg(pageSizeStr, Integer.class);
            return operateRecordService.queryMerchantOperateRecord(type, targetType, shopName, timeStart, timeEnd,
                    accountType, name, phone, pageNum, pageSize, operaterId).toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "queryOperateRecord", produces = "application/json;charset=utf-8")
    public String queryOperateRecord(HttpServletRequest req, HttpServletResponse res) {
        try {
            String recordIdStr = getArg.apply(req, "recordId");
            String operaterIdStr = getArg.apply(req, "operaterId");
            Long recordId = parseArg(recordIdStr, Long.class);
            Long operaterId = parseArg(operaterIdStr, Long.class);
            return operateRecordService.queryOperateRecordInfo(recordId, operaterId).toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }


    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "addShop", produces = "application/json;charset=utf-8")
    public String addShop(HttpServletRequest req, HttpServletResponse res) {
        try {
            String shopName = getArg.apply(req, "shopName");
            String shopAddress = getArg.apply(req, "shopAddress");
            String minuteShopAddress = getArg.apply(req, "minuteShopAddress");
            String serviceTel = getArg.apply(req, "serviceTel");
            String operaterIdStr = getArg.apply(req, "operaterId");
            String merchantIdStr = getArg.apply(req, "merchantId");
            Long operaterId = toLongArg.apply(operaterIdStr);
            Long merchantId = toLongArg.apply(merchantIdStr);
            return shopService.addShop(shopName, shopAddress, minuteShopAddress, serviceTel, merchantId, operaterId)
                    .toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "importShops", produces = "application/json;charset=utf-8")
    public String importShops(HttpServletRequest req, HttpServletResponse res, @RequestParam ("file") CommonsMultipartFile file) {
        try {
            String operaterIdStr = getArg.apply(req, "operaterId");
            String merchantIdStr = getArg.apply(req, "merchantId");
            Long operaterId = toLongArg.apply(operaterIdStr);
            Long merchantId = toLongArg.apply(merchantIdStr);
            String result = null;
            try {
                InputStream is = file.getInputStream();
                File file2 = new File(file.getOriginalFilename());
                OutputStream os = new FileOutputStream(file2);
                byte[] temp = new byte[1024];
                int size = -1;
                while ((size = is.read(temp)) != -1) {
                    os.write(temp, 0, size);
                }
                os.close();
                result = shopService.importShops(file2, merchantId, operaterId).toJson();
            } catch (Exception e) {
                LogFactory.error(this, "批量导入门店的时候发生异常", e);
            }
            return result;
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "queryVerifyRecords", produces = "application/json;charset=utf-8")
    public String queryVerifyRecords(final HttpServletRequest req, final HttpServletResponse res) {
        try {
            String codeStr = getArg.apply(req, "code");
            String timeStart = getArg.apply(req, "timeStart");
            String timeEnd = getArg.apply(req, "timeStop");
            String shopName = getArg.apply(req, "shopName");
            String phone = getArg.apply(req, "phone");
            String verificationTypeString = getArg.apply(req, "verificationType");
            String pageNumStr = getArg.apply(req, "pageNum");
            String pageSizeStr = getArg.apply(req, "pageSize");
            String operaterIdStr = getArg.apply(req, "operaterId");
            String commodityName = getArg.apply(req, "commodityName");

            Long code = toLongArg.apply(codeStr);
            VerificationType type = toVerificationType.apply(verificationTypeString);
            Integer pageNum = toIntArg.apply(pageNumStr);
            Integer pageSize = toIntArg.apply(pageSizeStr);
            Long operaterId = toLongArg.apply(operaterIdStr);
            return verificationRecordService.queryShopVerificationRecords(timeStart, timeEnd, shopName, code, commodityName, phone, type, pageNum, pageSize, operaterId).toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "queryVerifyRecord", produces = "application/json;charset=utf-8")
    public String queryVerifyRecord(final HttpServletRequest req, final HttpServletResponse res) {
        try {
            String recordIdStr = getArg.apply(req, "recordId");
            String operaterIdStr = getArg.apply(req, "operaterId");
            Long recordId = parseArg(recordIdStr, Long.class);
            Long operaterId = parseArg(operaterIdStr, Long.class);
            return verificationRecordService.queryVerificationRecordInfo(recordId, operaterId).toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }
    @SetUtf8
    @RequestMapping (value = "downloadExcelTemplate")
    public void downloadExcelTemplate(HttpServletRequest req, HttpServletResponse res) {
        try {
            String operaterIdStr = req.getParameter("operaterId");
            Long operaterId = toLongArg.apply(operaterIdStr);
            File file = (File) shopService.downloadExcelTemplate(operaterId).getDetail();
            //        return FileAssit.exportTest(file, res);
            FileAssit.export(file, res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "queryCommodities", produces = "application/json;charset=utf-8")
    public String merchantQueryCommodities(HttpServletRequest req, HttpServletResponse res) {
        try {
            String commodityName = getArg.apply(req, "commodityName");
            String commodityTypeStr = getArg.apply(req, "commodityType");
            String commodityStatusStr = getArg.apply(req, "commodityStatus");
            String pageNumStr = getArg.apply(req, "pageNum");
            String pageSizeStr = getArg.apply(req, "pageSize");
            String operaterIdStr = getArg.apply(req, "operaterId");
            Long commodityId=(Long) (getArgDir.apply(getArg.apply(req,"commodityId"),Long.class));
            CommodityType commodityType = CommodityType.getType(commodityTypeStr);
            CommodityStatus commodityStatus = CommodityStatus.getType(commodityStatusStr);
            Integer pageNum = toIntArg.apply(pageNumStr);
            Integer pageSize = toIntArg.apply(pageSizeStr);
            Long operaterId = toLongArg.apply(operaterIdStr);
            return commodityService.merchantQueryCommodities(commodityName, commodityType, commodityStatus, pageNum, pageSize, commodityId, operaterId).toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "exportCommodities", produces = "application/octet-stream;charset=utf-8")
    public String merchantExportCommodities(HttpServletRequest req, HttpServletResponse res) {
        try {
            String commodityName = getArg.apply(req, "commodityName");
            String commodityTypeStr = getArg.apply(req, "commodityType");
            String commodityStatusStr = getArg.apply(req, "commodityStatus");
            String operaterIdStr = getArg.apply(req, "operaterId");
            Long commodityId= (Long) getArgDir.apply(getArg.apply(req,"commodityId"),Long.class);
            CommodityType commodityType = CommodityType.getType(commodityTypeStr);
            CommodityStatus commodityStatus = CommodityStatus.getType(commodityStatusStr);
            Long operaterId = toLongArg.apply(operaterIdStr);
            File file = commodityService.merchantQueryCommoditiesExport(commodityName, commodityType, commodityStatus, commodityId, operaterId).getDetail();
            return FileAssist.exportTest(file, res);
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
        //        FileAssit.export(file,res);
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "queryRequests", produces = "application/json;charset=utf-8")
    public String queryRequests(HttpServletRequest req, HttpServletResponse res) {
        try {
            String commodityName = getArg.apply(req, "commodityName");
            String commodityTypeStr = getArg.apply(req, "commodityType");
            String operateTypeStr = getArg.apply(req, "operateType");
            String statusStr = getArg.apply(req, "status");
            String timeStart = getArg.apply(req, "timeStart");
            String timeEnd = getArg.apply(req, "timeEnd");
            String pageNumStr = getArg.apply(req, "pageNum");
            String pageSizeStr = getArg.apply(req, "pageSize");
            String operaterIdStr = getArg.apply(req, "operaterId");
            CommodityType commodityType = CommodityType.getType(commodityTypeStr);
            RequestOperateType operateType = RequestOperateType.getType(operateTypeStr);
            RequestStatus status = RequestStatus.getType(statusStr);
            Integer pageNum = toIntArg.apply(pageNumStr);
            Integer pageSize = toIntArg.apply(pageSizeStr);
            Long operaterId = toLongArg.apply(operaterIdStr);
            return requestService.queryCommodityRequest(commodityName, commodityType, operateType, status, timeStart, timeEnd, pageNum, pageSize, operaterId).toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "requestAddCommodity", produces = "application/json;charset=utf-8")
    public String requestAddCommodity(HttpServletRequest req, HttpServletResponse res) {
        try {
            String commodityName = getArg.apply(req, "commodityName");
            String commodityTypeStr = getArg.apply(req, "commodityType");
            String facePriceStr = getArg.apply(req, "facePrice");
            String costPriceStr = getArg.apply(req, "costPrice");
            String sellPriceStr = getArg.apply(req, "sellPrice");
            String lifeTimeStr = getArg.apply(req, "lifeTime");
            String merchantIdStr = getArg.apply(req, "merchantId");
            String operaterIdStr = getArg.apply(req, "operaterId");
            String useDestription = getArg.apply(req, "useDestription");

            CommodityType commodityType = CommodityType.getType(commodityTypeStr);
            BigDecimal facePrice = parseArg(facePriceStr, BigDecimal.class);
            BigDecimal costPrice = parseArg(costPriceStr, BigDecimal.class);
            BigDecimal sellPrice = parseArg(sellPriceStr, BigDecimal.class);
            Integer lifeTime = parseArg(lifeTimeStr, Integer.class);
            Long merchantId = parseArg(merchantIdStr, Long.class);
            Long operaterId = parseArg(operaterIdStr, Long.class);

            return requestService.requestAddCommodity(commodityName, commodityType, facePrice, costPrice, sellPrice, lifeTime, useDestription, operaterId, null).toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "requstModifyCommodity", produces = "application/json;charset=utf-8")
    public String requstModifyCommodity(HttpServletRequest req, HttpServletResponse res) {
        try {
            String facePriceStr = getArg.apply(req, "facePrice");
            String costPriceStr = getArg.apply(req, "costPrice");
            String sellPriceStr = getArg.apply(req, "sellPrice");
            String lifeTimeStr = getArg.apply(req, "lifeTime");
            String commodityIdStr = getArg.apply(req, "commodityId");
            String operaterIdStr = getArg.apply(req, "operaterId");

            BigDecimal facePrice = parseArg(facePriceStr, BigDecimal.class);
            BigDecimal costPrice = parseArg(costPriceStr, BigDecimal.class);
            BigDecimal sellPrice = parseArg(sellPriceStr, BigDecimal.class);
            Integer lifeTime = parseArg(lifeTimeStr, Integer.class);
            Long commodityId = parseArg(commodityIdStr, Long.class);
            Long operaterId = parseArg(operaterIdStr, Long.class);
            return requestService.requestModifyCommodity(costPrice, facePrice, sellPrice, lifeTime, commodityId, operaterId).toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "requestShelveCommodity", produces = "application/json;charset=utf-8")
    public String requestShelveCommodity(HttpServletRequest req, HttpServletResponse res) {
        try {
            String commodityIdStr = getArg.apply(req, "commodityId");
            String operaterIdStr = getArg.apply(req, "operaterId");
            Long commodityId = parseArg(commodityIdStr, Long.class);
            Long operaterId = parseArg(operaterIdStr, Long.class);
            return requestService.requestShelveCommodity(commodityId, operaterId).toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "requestUnshelveCommodity", produces = "application/json;charset=utf-8")
    public String requestUnshelveCommodity(HttpServletRequest req, HttpServletResponse res) {
        try {
            String commodityIdStr = getArg.apply(req, "commodityId");
            String operaterIdStr = getArg.apply(req, "operaterId");
            Long commodityId = parseArg(commodityIdStr, Long.class);
            Long operaterId = parseArg(operaterIdStr, Long.class);
            return requestService.requestUnshelveCommodity(commodityId, operaterId).toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "requestDelCommodity", produces = "application/json;charset=utf-8")
    public String requestDelCommodity(HttpServletRequest req, HttpServletResponse res) {
        try {
            String commodityIdStr = getArg.apply(req, "commodityId");
            String operaterIdStr = getArg.apply(req, "operaterId");
            Long commodityId = parseArg(commodityIdStr, Long.class);
            Long operaterId = parseArg(operaterIdStr, Long.class);
            return requestService.requestDelCommodity(commodityId, operaterId).toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "queryRequest", produces = "application/json;charset=utf-8")
    public String queryRequest(HttpServletRequest req, HttpServletResponse res) {
        try {
            String requestIdStr = getArg.apply(req, "requestId");
            String operaterIdStr = getArg.apply(req, "operaterId");
            Long requestId = parseArg(requestIdStr, Long.class);
            Long operaterId = parseArg(operaterIdStr, Long.class);
            return requestService.queryRequest(requestId, operaterId).toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "queryCommodity", produces = "application/json;charset=utf-8")
    public String queryCommodity(HttpServletRequest req, HttpServletResponse res) {
        try {
            String commodityIdStr = getArg.apply(req, "commodityId");
            String operaterIdStr = getArg.apply(req, "operaterId");
            Long operaterId = toLongArg.apply(operaterIdStr);
            Long commodityId = toLongArg.apply(commodityIdStr);
            return commodityService.queryCommodity(commodityId, operaterId).toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "queryMerchantInfo", produces = "application/json;charset=utf-8")
    public String queryMerchantInfo(HttpServletRequest req, HttpServletResponse res) {
        try {
            String merchantIdStr = getArg.apply(req, "merchantId");
            String operaterIdStr = getArg.apply(req, "operaterId");
            Long operaterId = toLongArg.apply(operaterIdStr);
            Long merchantId = toLongArg.apply(merchantIdStr);
            return merchantService.queryMerchantInfo(operaterId, merchantId).toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "exportDailyRecords", produces = "application/octet-stream;charset=utf-8")
    public String exportDailyRecords(HttpServletRequest req, HttpServletResponse res) {
        try {
            String timeStart = getArg.apply(req, "timeStart");
            String timeEnd = getArg.apply(req, "timeStop");
            String clearStatusStr = getArg.apply(req, "clearStatus");
            String invoiceStatusStr = getArg.apply(req, "invoiceStatus");
            String operaterIdStr = getArg.apply(req, "operaterId");
            String shopName=getArg.apply(req,"shopName");
//            ClearStatus clearStatus = ClearStatus.getType(clearStatusStr);
            Long operaterId = parseArg(operaterIdStr, Long.class);
            InvoiceStatus invoiceStatus = InvoiceStatus.getType(invoiceStatusStr);
            RetMessage<File> retMessage =shopDailyVerifyStatsticService.exportStatsticsList(shopName,null,timeStart,timeEnd,operaterId);
//            RetMessage<File> retMessage = clearService.exoirtDailyRecords(timeStart, timeEnd, clearStatus, invoiceStatus, operaterId);
            return FileAssist.exportTest(retMessage.getDetail(), res);
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
        //        FileAssit.export(retMessage,res);
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "exportVerifyRecords", produces = "application/octet-stream;charset=utf-8")
    public String exportVerifyRecords(HttpServletRequest req, HttpServletResponse res) {
        try {
            String codeStr = getArg.apply(req, "code");
            String timeStart = getArg.apply(req, "timeStart");
            String timeEnd = getArg.apply(req, "timeStop");
            String shopName = getArg.apply(req, "shopName");
            String phone = getArg.apply(req, "phone");
            String verificationTypeString = getArg.apply(req, "verificationType");
            String pageNumStr = getArg.apply(req, "pageNum");
            String pageSizeStr = getArg.apply(req, "pageSize");
            String operaterIdStr = getArg.apply(req, "operaterId");
            String commodityName = getArg.apply(req, "commodityName");

            Long code = toLongArg.apply(codeStr);
            VerificationType type = toVerificationType.apply(verificationTypeString);
            Integer pageNum = toIntArg.apply(pageNumStr);
            Integer pageSize = toIntArg.apply(pageSizeStr);
            Long operaterId = toLongArg.apply(operaterIdStr);

            RetMessage<File> retMessage = verificationRecordService.exportShopVerificationRecords(timeStart, timeEnd, shopName, code, commodityName, phone, type, operaterId);
            return FileAssist.exportTest(retMessage.getDetail(), res);
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
        //        FileAssit.export(retMessage,res);
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "verifyCode", produces = "application/json;charset=utf-8")
    public String verifyCode(HttpServletRequest req, HttpServletResponse res) {
        try {
            String codIdStr = getArg.apply(req, "codeId");
            String operaterIdStr = getArg.apply(req, "operaterId");
            String codeStr = getArg.apply(req, "code");
            String verifyTypeStr = getArg.apply(req, "verificationType");
            Long codeId = parseArg(codIdStr, Long.class);
            Long operaterId = parseArg(operaterIdStr, Long.class);
            Long code = parseArg(codeStr, Long.class);
            VerificationType verifyType = toVerificationType.apply(verifyTypeStr);
            if (codeId == null)
                return codeService.verificateCode(code, null, operaterId, verifyType).toJson();
            else
                return codeService.verificateCode(codeId, operaterId, verifyType).toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "exportClearRecords", produces = "application/octet-stream;charset=utf-8")
    public String exportClearRecords(HttpServletRequest req, HttpServletResponse res) {
        try {
            String timeStart = getArg.apply(req, "timeStart");
            String timeEnd = getArg.apply(req, "timeStop");
            String clearStatusStr = getArg.apply(req, "clearStatus");
            String invoiceStatusStr = getArg.apply(req, "invoiceStatus");
            String operaterIdStr = getArg.apply(req, "operaterId");

            ClearStatus clearStatus = ClearStatus.getType(clearStatusStr);
            Long operaterId = parseArg(operaterIdStr, Long.class);
            InvoiceStatus invoiceStatus = InvoiceStatus.getType(invoiceStatusStr);
            RetMessage<File> retMessage = clearService.exportClearRecords(timeStart, timeEnd, clearStatus, invoiceStatus, operaterId);
            return FileAssist.exportTest(retMessage.getDetail(), res);
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
        //        FileAssit.export(retMessage,res);
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "statsticDailyRecords", produces = "application/json;charset=utf-8")
    public String statsticDailyRecords(HttpServletRequest req, HttpServletResponse res) {
        try {
            String timeStart = getArg.apply(req, "timeStart");
            String timeEnd = getArg.apply(req, "timeStop");
            String clearStatusStr = getArg.apply(req, "clearStatus");
            String pageNumStr = getArg.apply(req, "pageNum");
            String pageSizeStr = getArg.apply(req, "pageSize");
            String operaterIdStr = getArg.apply(req, "operaterId");

            //###
            String shopName=getArg.apply(req,"shopName");







            ClearStatus clearStatus = ClearStatus.getType(clearStatusStr);
            Long operaterId = parseArg(operaterIdStr, Long.class);
            Integer pageNum = parseArg(pageNumStr, Integer.class);
            Integer pageSize = parseArg(pageSizeStr, Integer.class);


            return  verificationRecordService.statisticVerifyRecord(shopName,timeStart,timeEnd,pageNum,pageSize,null,operaterId).toJson();
//            return clearService.statsticDailyRecords(timeStart, timeEnd, clearStatus, pageNum, pageSize, operaterId).toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "exportStaticDailyRecords", produces = "application/octet-stream;charset=utf-8")
    public String exportStaticDailyRecords(HttpServletRequest req, HttpServletResponse res) {
        try {
            String timeStart = getArg.apply(req, "timeStart");
            String timeEnd = getArg.apply(req, "timeStop");
            String clearStatusStr = getArg.apply(req, "clearStatus");
            String operaterIdStr = getArg.apply(req, "operaterId");



            //###
            String shopName=getArg.apply(req,"shopName");


            ClearStatus clearStatus = ClearStatus.getType(clearStatusStr);
            Long operaterId = parseArg(operaterIdStr, Long.class);
//            RetMessage<File> retMessage = clearService.exportStatsticDailyRecords(timeStart, timeEnd, clearStatus, operaterId);
            RetMessage<File> retMessage = verificationRecordService.exportStatisticVerifyRecord(shopName,timeStart,timeEnd,null,operaterId);
            return FileAssist.exportTest(retMessage.getDetail(), res);


        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
        //        FileAssit.export(retMessage,res);
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "queryAllShopIds", produces = "application/json;charset=utf-8")
    public String queryAllShopIds(HttpServletRequest req, HttpServletResponse res) {
        try {
            String operaterIdStr = getArg.apply(req, "operaterId");
            Long operaterId = parseArg(operaterIdStr, Long.class);
            return shopService.queruAllShopIds(operaterId).toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "modifyCommodity", produces = "application/json;charset=utf-8")
    public String modifyCommodity(HttpServletRequest req, HttpServletResponse res) {
        try {
            String facePriceStr = getArg.apply(req, "facePrice");
            String costPriceStr = getArg.apply(req, "costPrice");
            String sellPriceStr = getArg.apply(req, "sellPrice");
            String lifeTimeStr = getArg.apply(req, "lifeTime");
            String commodityIdStr = getArg.apply(req, "commodityId");
            String operaterIdStr = getArg.apply(req, "operaterId");
            String useDetail = getArg.apply(req, "useDestription");

            BigDecimal facePrice = parseArg(facePriceStr, BigDecimal.class);
            BigDecimal costPrice = parseArg(costPriceStr, BigDecimal.class);
            BigDecimal sellPrice = parseArg(sellPriceStr, BigDecimal.class);
            Integer lifeTime = parseArg(lifeTimeStr, Integer.class);
            Long commodityId = parseArg(commodityIdStr, Long.class);
            Long operaterId = parseArg(operaterIdStr, Long.class);

            return commodityService.modifyCommodity(costPrice, facePrice, sellPrice, lifeTime, useDetail, commodityId, operaterId).toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "addCommodity", produces = "application/json;charset=utf-8")
    public String addCommodity(HttpServletRequest req, HttpServletResponse res) {
        try {
            String commodityName = getArg.apply(req, "commodityName");
            String commodityTypeStr = getArg.apply(req, "commodityType");
            String facePriceStr = getArg.apply(req, "facePrice");
            String costPriceStr = getArg.apply(req, "costPrice");
            String sellPriceStr = getArg.apply(req, "sellPrice");
            String lifeTimeStr = getArg.apply(req, "lifeTime");
            String merchantNameStr = getArg.apply(req, "merchantName");
            String merchantIdStr = getArg.apply(req, "merchantId");
            String operaterIdStr = getArg.apply(req, "operaterId");
            String useDestription = getArg.apply(req, "useDestription");

            CommodityType commodityType = CommodityType.getType(commodityTypeStr);
            BigDecimal facePrice = parseArg(facePriceStr, BigDecimal.class);
            BigDecimal costPrice = parseArg(costPriceStr, BigDecimal.class);
            BigDecimal sellPrice = parseArg(sellPriceStr, BigDecimal.class);
            Integer lifeTime = parseArg(lifeTimeStr, Integer.class);
            Long merchantId = parseArg(merchantIdStr, Long.class);
            Long operaterId = parseArg(operaterIdStr, Long.class);

            return commodityService.addCommodity(commodityName, commodityType, facePrice, costPrice, sellPrice, lifeTime, merchantNameStr, useDestription, null, operaterId).toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }
    @SetUtf8
    @LoginCheck
    @ResponseBody
    @RequestMapping (value = "delCommodity", produces = "application/json;charset=utf-8")
    public String delCommodity(HttpServletRequest req, HttpServletResponse res) {
        try {
            String commodityIdStr = getArg.apply(req, "commodityId");
            String operaterIdStr = getArg.apply(req, "operaterId");
            Long commodityId = parseArg(commodityIdStr, Long.class);
            Long operaterId = parseArg(operaterIdStr, Long.class);
            return commodityService.delCommodity(commodityId, operaterId).toJson();
        } catch (Exception e) {
            LogFactory.error(this,"响应时发生异常!",e);
            return new RetMessage<String>(RetCodeEnum.error,"系统繁忙,稍后再试!",null).toJson();
        }
    }

}
