package com.lanxi.couponcode.spi.assist;

import com.lanxi.couponcode.spi.consts.enums.CommodityType;
import com.lanxi.couponcode.spi.consts.enums.VerificationType;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by yangyuanjian on 11/30/2017.
 */
public interface ArgAssist {
    static <T extends Number> T parseArg(String str, Class<T> clazz){
        try {
            if(str==null)
                return null;
            if(clazz.equals(Long.class))
                return (T) Long.valueOf(str);
            if(clazz.equals(Integer.class))
                return (T) Integer.valueOf(str);
            if(clazz.equals(BigDecimal.class))
                return (T) new BigDecimal(str);
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    BiFunction<HttpServletRequest,String,String> getArg=(r, n)->r.getParameter(n);
    Function<String,Long> toLongArg= s->s==null?null:parseArg(s,Long.class);
    Function<String,Integer> toIntArg=s->parseArg(s,Integer.class);
    Function<String,BigDecimal> toDecimalArg=s->parseArg(s,BigDecimal.class);
    Function<String,CommodityType> toCommodityTypeArg=s->s==null?null:CommodityType.getType(s.trim());
    Function<String,VerificationType> toVerificationType=s->s==null?null:VerificationType.getType(s);
   
}
