package com.lanxi.couponcode.spi.assist;

import com.lanxi.util.utils.BeanUtil;
import com.lanxi.util.utils.OtherUtil;

import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Created by yangyuanjian on 2017/11/25.
 */
public interface PredicateAssist {
    //--------------------------------------------------------基础-------------------------------------------------
    Predicate<String> isPhone = e -> {
        if (e == null)
            return false;
        return OtherUtil.getServiceProvider(e) != null;
    };
    Predicate<Object> hasMerchantId = e -> Stream.of(e.getClass().getDeclaredFields()).parallel().filter(f -> f.getName().equals("merchantId") && f.getType().equals(Long.class)).findAny().isPresent();

    BiPredicate<Object, Object> sameMerchant = (o1, o2) -> {
        if (o1 == null || o2 == null)
            return false;
        if (hasMerchantId.negate().test(o1) || hasMerchantId.negate().test(o2))
            return false;
        if (BeanUtil.get(o1, "merchantId") == null || BeanUtil.get(o2, "merchantId") == null)
            return false;
        if (!BeanUtil.get(o1, "merchantId").equals(BeanUtil.get(o2, "merchantId")))
            return false;
        return true;
    };

    BiPredicate<Object, Object> diffMerchant = sameMerchant.negate();
    /**
     * 是否是null
     */
    Predicate<Object> isNull = CheckAssist::isNull;
    /**
     * 是否不是null
     */
    Predicate<Object> notNull = isNull.negate();
    //-------------------------------------------------------参数校验----------------------------------------------
    Predicate<String> isPage = e -> notNull.test(e) && e.matches("[1-9]+[0-9]{0,6}");
    Predicate<String> notPage = isPage.negate();

    BiPredicate<String, String> isPageArg = CheckAssist::checkPage;
    BiPredicate<String, String> notPageArg = isPageArg.negate();

    Predicate<String> isTime = CheckAssist::checkTimeArg;
    Predicate<String> notTime = isTime.negate();

    Predicate<String> isAddressOrName = CheckAssist::chineseAssicNumOnly;
    Predicate<String> notAddressOrName = isAddressOrName.negate();

    Predicate<Long> maybeCode = CheckAssist::isCode;
    Predicate<Long> cantBeCode = maybeCode.negate();

    Predicate<Long> maybeId = CheckAssist::isId;
    Predicate<Long> cantBeId = maybeId.negate();

    Predicate<String> isNullOrEmpty = e -> e == null || e.trim().isEmpty();
    Predicate<String> notNullOrEmpty = isNullOrEmpty.negate();

    Predicate<String> isCode = e -> notNull.test(e) && e.matches("[0-9]{10}");
    Predicate<String> notCode = isCode.negate();

    Predicate<String> isId = e -> notNull.test(e) && e.matches("[0-9]{18}");
    Predicate<String> notId = isId.negate();


//--------------------------------------------------------操作权限校验---------------------------------------------------

}
