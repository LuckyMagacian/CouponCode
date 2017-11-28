package com.lanxi.couponcode.impl.assist;

import com.lanxi.couponcode.impl.entity.*;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.enums.*;

import static com.lanxi.couponcode.spi.consts.enums.AccountType.*;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by yangyuanjian on 2017/11/25.
 */
public interface PredicateAssist {
    //--------------------------------------------------------基础-------------------------------------------------

    /**是否是null*/
    Predicate<Object> isNull=CheckAssist::isNull;
    /**是否不是null*/
    Predicate<Object> notNull= isNull.negate();
    //-------------------------------------------------------参数校验----------------------------------------------
    Predicate<String> isPage=e-> notNull.test(e)&&e.matches("[1-9]+[0-9]{1,6}");
    Predicate<String> notPage= isPage.negate();

    BiPredicate<String,String> isPageArg=CheckAssist::checkPage;
    BiPredicate<String,String> notPageArg=isPageArg.negate();

    Predicate<String> isTime=CheckAssist::checkTimeArg;
    Predicate<String> notTime=isTime.negate();

    Predicate<String> isAddressOrName=CheckAssist::chineseAssicNumOnly;
    Predicate<String> notAddressOrName=isAddressOrName.negate();

    Predicate<Long> maybeCode=CheckAssist::isCode;
    Predicate<Long> cantBeCode=maybeCode.negate();

    Predicate<Long> maybeId=CheckAssist::isId;
    Predicate<Long> cantBeId=maybeId.negate();

    Predicate<String> isNullOrEmpty=e->e==null||e.trim().isEmpty();
    Predicate<String> notNullOrEmpty=isNullOrEmpty.negate();

    Predicate<String> isCode=e->notNull.test(e)&&e.matches("[0-9]{10}");
    Predicate<String> notCode=isCode.negate();

    Predicate<String> isId=e-> notNull.test(e)&&e.matches("[0-9]{18}");
    Predicate<String> notId=isId.negate();


//--------------------------------------------------------操作权限校验---------------------------------------------------
    Predicate<Account> isAdmin= e->notNull.test(e)&&admin.equals(e.getAccountType());
    Predicate<Account> notAdmin=isAdmin.negate();

    Predicate<Account> isShopManager=e-> notNull.test(e)&&shopManager.equals(e.getAccountType());
    Predicate<Account> notShopManager=isShopManager.negate();

    Predicate<Account> isMerchantManager=e->notNull.test(e)&&merchantManager.equals(e.getAccountType());

    Predicate<Account> isMerchantStaff=e->
                merchantManager.equals(e.getAccountType())||
                shopManager.equals(e.getAccountType())||
                shopEmployee.equals(e.getAccountType());

    Predicate<Account> notMerchantStaff=isMerchantStaff.negate();

    BiPredicate<Account,Shop> belongToShop=(a, s)->a.getShopId().equals(s.getShopId());
    BiPredicate<Account,Shop> notBelongToShop=belongToShop.negate();

    BiPredicate<Account,Merchant> belongToMerchant=(a, m)->a.getMerchantId().equals(m.getMerchantId());
    BiPredicate<Account,Merchant> notBelongToMerchant=belongToMerchant.negate();

    BiPredicate<Shop,Merchant> shopBelongToMerchant=(s,m)->s.getMerchantId().equals(m.getMerchantId());
    BiPredicate<Shop,Merchant> shopNotBelongToMerchantShop=shopBelongToMerchant.negate();

    BiPredicate<CouponCode,Account> sameMerchantCodeAccount=(c,a)->
            notNull.test(c)&&
            notNull.test(a)&&
            notNull.test(c.getMerchantId())&&
            notNull.test(a.getMerchantId())&&
            c.getMerchantId().equals(a.getMerchantId());
    BiPredicate<CouponCode,Account> diffMerchantCodeAccount=sameMerchantCodeAccount.negate();

    Predicate<Account> canVerifyCode=e->shopManager.equals(e.getAccountType())||shopEmployee.equals(e.getAccountType());
    Predicate<Account> cantVerifyCode=canVerifyCode.negate();

    Predicate<Account> canDestroyCode=e->merchantManager.equals(e.getAccountType())||shopManager.equals(e.getAccountType());
    Predicate<Account> cantDestroyCode=canDestroyCode.negate();

    Predicate<Account> canPostoneCode=canDestroyCode;
    Predicate<Account> cantPostoneCode=canPostoneCode.negate();

    Predicate<Account> canQueryCode=isMerchantStaff;
    Predicate<Account> cantQueryCode=canQueryCode.negate();

    Predicate<CouponCode> canVerify=e-> CouponCodeStatus.undestroyed.equals(e.getCodeStatus());

    Predicate<CouponCode> cantVerify=canVerify.negate();
    Predicate<CouponCode> canCancle=canVerify;

    Predicate<CouponCode> cantCancle=canCancle.negate();
    Predicate<CouponCode> canPostone=canVerify;

    Predicate<CouponCode> cantPostone=canPostone.negate();
    //------------------------------------------------------复合--------------------------------------------------------
    BiFunction<Account,OperateType,RetMessage> checkAccount=(a,o)->{
        if(isNull.test(a))
            return new RetMessage(RetCodeEnum.fail,"账户不存在!",null);
        switch (o){
            case requestAddCommodity:
            case requestModifyCommodity:
            case requestUnshelveCommodity:
            case requestShelveCommodity:
            case rquestDelCommodity:
                if(isMerchantManager.negate().test(a))
                    return new RetMessage(RetCodeEnum.fail,"非商户管理员无权操作!",null);
            case queryRequest:
                if(notAdmin.test(a)||isMerchantManager.negate().test(a))
                    return new RetMessage(RetCodeEnum.fail,"非管理员及商户管理员无权操作!",null);
            case passRequest:
            case rejectRequest:
                if(notAdmin.test(a))
                    return new RetMessage(RetCodeEnum.fail,"非管理员无权操作!",null);





            case queryOperateRecordList:
                if(notAdmin.test(a)||isMerchantManager.negate().test(a))
                    return new RetMessage(RetCodeEnum.fail,"非管理员及商户管理员无权操作!",null);
            case queryOperateRecord:
                if(notAdmin.test(a)||isMerchantManager.negate().test(a)||isShopManager.negate().test(a))
                    return new RetMessage(RetCodeEnum.fail,"非管理员及商户管理员无权操作!",null);

            case createShop:
            case unfreezeShop:
            case freezeShop:
            case deleteShop:
            case modifyShop:
            case cancelShop:
                if(isMerchantManager.negate().test(a))
                    return new RetMessage(RetCodeEnum.fail,"非商户管理员无权操作!",null);
            case queryShop:
                if(notAdmin.test(a)||isMerchantManager.negate().test(a))
                    return new RetMessage(RetCodeEnum.fail,"非管理员及商户管理员无权操作!",null);

            case createMerchantManager:
            case unfreezeMerchantManager:
            case freezeMerchantManager:
            case deleteMerchantManager:
            case modifyMerchantManager:
            case queryMerchantManager:
            case cancelMerchantManager:
                if(notAdmin.test(a))
                    return new RetMessage(RetCodeEnum.fail,"非管理员无权操作!",null);

            case createShopManager:
            case unfreezeShopManager:
            case freezeShopManager:
            case deleteShopManager:
            case modifyShopManager:
            case cancelShopManager:
                if(isMerchantManager.negate().test(a))
                    return new RetMessage(RetCodeEnum.fail,"非商户管理员无权操作!",null);
            case queryShopManager:
                if(notAdmin.test(a)||isMerchantManager.negate().test(a))
                    return new RetMessage(RetCodeEnum.fail,"非管理员及商户管理员无权操作!",null);

            case createEmployee:
            case unfreezeEmployee:
            case freezeEmployee:
            case deleteEmployee:
            case modifyEmployee:
            case cancelEmployee:
                if(isMerchantManager.negate().test(a)||isShopManager.negate().test(a))
                    return new RetMessage(RetCodeEnum.fail,"非商户管理员门店管理员无权操作!",null);
            case queryEmployee:
                if(isMerchantManager.negate().test(a)||isShopManager.negate().test(a)||notAdmin.test(a))
                    return new RetMessage(RetCodeEnum.fail,"非管理员,商户管理员,门店管理员无权操作!",null);

            case createCommodity:
            case unshelveCommodity:
            case shelveCommodity:
            case deleteCommodity:
            case modifyCommodity:
            case cancelCommodity:
                if(notAdmin.test(a))
                    return new RetMessage(RetCodeEnum.fail,"非管理员无权操作!",null);
            case queryCommodity:
                if(isMerchantManager.negate().test(a)||notAdmin.test(a))
                    return new RetMessage(RetCodeEnum.fail,"非管理员,商户管理员无权操作!",null);

            case destroyCouponCode:
                if(notMerchantStaff.test(a))
                    return new RetMessage(RetCodeEnum.fail,"非商户人员无权操作!",null);
            case createCouponCode:
                return new RetMessage(RetCodeEnum.fail,"只有系统有权创建!",null);
            case postoneCouponCode:
            case cancelCouponCode:
                if(isMerchantManager.negate().test(a))
                    return new RetMessage(RetCodeEnum.fail,"非商户管理员无权操作!",null);
            case queryCouponCodeList:
                if(isMerchantManager.negate().test(a)||notAdmin.test(a))
                    return new RetMessage(RetCodeEnum.fail,"非管理员,商户管理员无权操作!",null);
            case queryCouponCode:
                //全部有权查询
                return null;

            case createAccount:
            case unfreezeAccount:
            case freezeAccount:
            case deleteAccount:
            case modifyAccount:
            case cancelAccount:
                if(notAdmin.test(a))
                    return new RetMessage(RetCodeEnum.fail,"非管理员无权操作!",null);
            case queryAccount:
                if(isMerchantManager.negate().test(a)||notAdmin.test(a))
                    return new RetMessage(RetCodeEnum.fail,"非管理员,商户管理员无权操作!",null);

            case createMerchant:
            case unfreezeMerchant:
            case freezeMerchant:
            case deleteMerchant:
            case modifyMerchant:
            case queryMerchant:
            case cancelMerchant:
                if(notAdmin.test(a))
                    return new RetMessage(RetCodeEnum.fail,"非管理员无权操作!",null);

            case statscitcShop:
                if(isMerchantManager.negate().test(a)||notAdmin.test(a))
                    return new RetMessage(RetCodeEnum.fail,"非管理员,商户管理员无权操作!",null);
            case statscitcMerchantManager:
                if(notAdmin.test(a))
                    return new RetMessage(RetCodeEnum.fail,"非管理员无权操作!",null);
            case statscitcShopManager:
            case statscitcEmployee:
            case statscitcCommodity:
            case statscitcCouponCode:
            case statscitcAccount:
                if(isMerchantManager.negate().test(a)||notAdmin.test(a))
                    return new RetMessage(RetCodeEnum.fail,"非管理员,商户管理员无权操作!",null);
            case statsticMerchant:
                if(notAdmin.test(a))
                    return new RetMessage(RetCodeEnum.fail,"非管理员无权操作!",null);

            case exportMerchant:
            case exportMerchantManager:
                if(notAdmin.test(a))
                    return new RetMessage(RetCodeEnum.fail,"非管理员无权操作!",null);
            case exportShop:
            case exportShopManager:
            case exportEmployee:
            case exportCommodity:
            case exportCouponCode:
            case exportAccount:
                if(isMerchantManager.negate().test(a)||notAdmin.test(a))
                    return new RetMessage(RetCodeEnum.fail,"非管理员,商户管理员无权操作!",null);

        }
        return null;
    };

    BiFunction<Commodity,OperateType,RetMessage> checkCommodity=(c, o)->{
        if(isNull.test(c))
            return new RetMessage(RetCodeEnum.fail,"商品不存在!",null);
        switch (o){
            case createCouponCode:
            case requestModifyCommodity:
            case requestShelveCommodity:
            case shelveCommodity:
            case modifyCommodity:
            case cancelCommodity:
                if(!CommodityStatus.unshelved.equals(c.getStatus()))
                    return new RetMessage(RetCodeEnum.fail,"非下架状态,无法操作!",null);
            case unshelveCommodity:
            case requestUnshelveCommodity:
                if(!CommodityStatus.shelved.equals(c.getStatus()))
                    return new RetMessage(RetCodeEnum.fail,"非上架状态,无法下架!",null);
            case queryCommodity:
            case statscitcCommodity:
            case exportCommodity:
                if(CommodityStatus.deleted.equals(c.getStatus()))
                    return new RetMessage(RetCodeEnum.fail,"已删除!",null);
            case deleteCommodity:
        }
        return null;
    };
    BiFunction<Shop,OperateType,RetMessage> checkShop=(s,o)->{
        if(isNull.test(s))
            return new RetMessage(RetCodeEnum.fail,"门店不存在!",null);
        switch (o){
            case cancelShop:
            case modifyShop:
            case unfreezeShop:
                if(!ShopStatus.freeze.equals(s.getShopStatus()))
                    return new RetMessage(RetCodeEnum.fail,"非冻结状态,无法操作!",null);
            case freezeShop:
                 if(!ShopStatus.normal.equals(s.getShopStatus()))
                     return new RetMessage(RetCodeEnum.fail,"非正常状态,无法冻结!",null);
            case queryShop:
            case statscitcShop:
            case exportShop:
                if(ShopStatus.deleted.equals(s.getShopStatus()))
                    return new RetMessage(RetCodeEnum.fail,"已删除!",null);
            case deleteShop:
        }
        return null;
    };
    BiFunction<Merchant,OperateType,RetMessage> checkMerchant=(m,o)->{
        if(isNull.test(m))
            return new RetMessage(RetCodeEnum.fail,"商户不存在!",null);

        switch (o){
            case queryMerchantManager:
            case queryShopManager:
            case queryEmployee:
            case queryCommodity:
            case queryCouponCode:
            case queryAccount:
            case statscitcShop:
            case statscitcMerchantManager:
            case statscitcShopManager:
            case statscitcEmployee:
            case statscitcCommodity:
            case statscitcCouponCode:
            case statscitcAccount:
            case queryRequest:
                return null;
            default:
                if(MerchantStatus.normal.equals(m))
                    return new RetMessage(RetCodeEnum.fail,"非正常状态!无法操作!",null);
        }
        return null;
    };
    BiFunction<CouponCode,OperateType,RetMessage> checkCode=(c,o)->{
        if(isNull.test(c))
            return new RetMessage(RetCodeEnum.fail,"串码不存在!",null);
      switch (o){
          case destroyCouponCode:
          case postoneCouponCode:
          case cancelCouponCode:
              if(!CouponCodeStatus.undestroyed.equals(c.getCodeStatus()))
                  return new RetMessage(RetCodeEnum.fail,"非正常状态,无法操作!",null);
          case queryCouponCode:
      }
      return null;
    };

    BiFunction<Request,OperateType,RetMessage> checkRequest=(r,o)->{
        switch (o){
            case passRequest:
            case rejectRequest:
                if(!RequestStatus.submit.equals(r.getStatus()))
                    return new RetMessage(RetCodeEnum.fail,"非提交状态,无法操作!",null);
        }
        return null;
    };
}
