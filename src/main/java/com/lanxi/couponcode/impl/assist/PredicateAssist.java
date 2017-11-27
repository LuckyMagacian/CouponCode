package com.lanxi.couponcode.impl.assist;

import com.lanxi.couponcode.impl.entity.Account;
import com.lanxi.couponcode.impl.entity.Merchant;
import com.lanxi.couponcode.impl.entity.Shop;
import com.lanxi.couponcode.spi.consts.enums.AccountType;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static com.lanxi.couponcode.spi.consts.enums.AccountType.admin;

/**
 * Created by yangyuanjian on 2017/11/25.
 */
public interface PredicateAssist {
    Predicate<Object> isNUll=CheckAssist::isNull;
    Predicate<Object> notNUll= isNUll.negate();

    Predicate<Account> isAdmin= e->admin.equals(e.getAccountType());
    Predicate<Account> notAdmin=isAdmin.negate();

    Predicate<Account> isShopManager=e-> AccountType.shopManager.equals(e.getAccountType());
    Predicate<Account> notShopManager=isShopManager.negate();

    BiPredicate<Account,Shop> belongToShop=(a, s)->a.getShopId().equals(s.getShopId());
    BiPredicate<Account,Shop> notBelongToShop=belongToShop.negate();

    BiPredicate<Account,Merchant> belongToMerchant=(a, m)->a.getMerchantId().equals(m.getMerchantId());
    BiPredicate<Account,Merchant> notBelongToMerchant=belongToMerchant.negate();

    BiPredicate<Shop,Merchant> shopBelongToMerchant=(s,m)->s.getMerchantId().equals(m.getMerchantId());
    BiPredicate<Shop,Merchant> shopNotBelongToMerchantShop=shopBelongToMerchant.negate();

    Predicate<String> isPage=e-> notNUll.test(e)&&e.matches("[1-9]+[0-9]{1,6}");
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

    Predicate<String> isCode=e->notNUll.test(e)&&e.matches("[0-9]{10}");
    Predicate<String> notCode=isCode.negate();

    Predicate<String> isId=e-> notNUll.test(e)&&e.matches("[0-9]{18}");
    Predicate<String> notId=isId.negate();
}
