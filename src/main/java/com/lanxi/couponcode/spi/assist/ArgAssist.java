package com.lanxi.couponcode.spi.assist;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lanxi.couponcode.spi.config.ConstConfig;
import com.lanxi.couponcode.spi.consts.annotations.Comment;
import com.lanxi.couponcode.spi.consts.enums.CommodityType;
import com.lanxi.couponcode.spi.consts.enums.VerificationType;
import com.lanxi.util.entity.LogFactory;
import com.lanxi.util.utils.SignUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Created by yangyuanjian on 11/30/2017.
 */
public interface ArgAssist {
    Map<String, JSONObject> argCache = new HashMap<>();

    static <T extends Number> T parseArg(String str, Class<T> clazz) {
        try {
            if (str == null)
                return null;
            if (clazz.equals(Long.class))
                return (T) Long.valueOf(str);
            if (clazz.equals(Integer.class))
                return (T) Integer.valueOf(str);
            if (clazz.equals(BigDecimal.class))
                return (T) new BigDecimal(str);
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Comment ("对称加密密钥非对称解密")
    Function<String, String>                        symmetricalKeyDe = e -> {
        try {
            String plainTextKey= new String(
                    SignUtil.rsaDe(
                            ConstConfig.priKey, SignUtil.base64De(e.getBytes("utf-8")))
            );
            return plainTextKey;
        } catch (Exception e1) {
            LogFactory.error(ArgAssist.class, "解密对称密钥时发生异常!", e1);
            return null;
        }
    };
    @Comment ("加密参数对称解密")
    BiFunction<String, String, String>              argStrDe         = (sk, s) -> {
        try {
           String plainTextArg= AesAssist.aesDecrypt(s,sk);
            return plainTextArg;
        } catch (Exception e) {
            LogFactory.error(ArgAssist.class, "解密加密参数时发生异常!", e);
            return null;
        }
    };
    @Comment ("keyValue参数转map")
    Function<String, JSONObject>                    argStrToMap      = s -> JSON.parseObject(s);


    Function<String, Long>             toLongArg          = s -> s == null ? null : parseArg(s, Long.class);
    Function<String, Integer>          toIntArg           = s -> parseArg(s, Integer.class);
    Function<String, BigDecimal>       toDecimalArg       = s -> parseArg(s, BigDecimal.class);
    Function<String, CommodityType>    toCommodityTypeArg = s -> s == null ? null : CommodityType.getType(s.trim());
    Function<String, VerificationType> toVerificationType = s -> s == null ? null : VerificationType.getType(s);
    @Comment ("从req中获取参数")
    BiFunction<HttpServletRequest, String, String> getArg    = (r, n) -> {
        try {
            JSONObject map = argCache.get(r.toString());
            if (map != null) {
                return map.getString(n);
            } else {
                String keyCipher=r.getParameter("secret");
                String paramCipher=r.getParameter("args");

                keyCipher= URLDecoder.decode(keyCipher,"utf-8");
                paramCipher=URLDecoder.decode(paramCipher,"utf-8");

                String keyPlain=ArgAssist.symmetricalKeyDe.apply(keyCipher);
                String paramPlain=ArgAssist.argStrDe.apply(keyPlain,paramCipher);

//                String key    = r.getParameter("secret");
//                String desKey = symmetricalKeyDe.apply(URLDecoder.decode(key,"utf-8"));
//                String argsCipher=r.getParameter("args");
//                String argsStr=argStrDe.apply(desKey,URLDecoder.decode(argsCipher,"utf-8"));
                map = argStrToMap.apply(paramPlain);
                argCache.put(r.toString(), map);
                return map.getString(n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    };
    @Comment ("获取参数并转换为对应的类型")
    BiFunction<String, Class, Object>              getArgDir = (s, c) -> {
        if (s == null || s.isEmpty())
            return null;
        if (Long.class.equals(c))
            return toLongArg.apply(s);
        else if (Integer.class.equals(c))
            return toIntArg.apply(s);
        else if (BigDecimal.class.equals(c))
            return toDecimalArg.apply(s);
        else if (CommodityType.class.equals(s))
            return toCommodityTypeArg.apply(s);
        else if (VerificationType.class.equals(s))
            return toVerificationType.apply(s);
        else
            return null;
    };
}
