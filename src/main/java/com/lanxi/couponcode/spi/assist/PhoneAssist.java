package com.lanxi.couponcode.spi.assist;

import com.lanxi.couponcode.spi.consts.enums.Gettype;

import java.io.Serializable;
import static com.lanxi.couponcode.spi.assist.PhoneAssist.PhoneServer.*;
/**
 * Created by yangyuanjian on 2017/11/23.
 */
public interface PhoneAssist {
    enum PhoneServer implements Gettype,Serializable{
        mobile("mobile"),unicom("unicom"),chinanet("chinanet"),virtual("virtual"),other("other");
        String value;

        PhoneServer(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }


        @Override
        public String toString() {
            return getValue();
        }
    }

    static PhoneServer getServiceProvider(String phone){
        if(phone==null||phone.isEmpty())
            return null;
        if(!phone.matches("1[0-9]{10}"))
            return null;
        final String  mobileRegex="(13[4-9])|(15[0-2])|(15[7-9])|(18[2-4])|(18[7-8])|(178)|(147)";
        final String  unionRegex="(133)|(153)|(189)|(18[0-1])|(173)|(177)";
        final String  unicomRegex="(13[0-2])|(15[5-6])|(18[5-6])|(145)|(176)";
        phone = phone.substring(0,3);
        if(phone.matches(mobileRegex))
            return mobile;
        if(phone.matches(unionRegex))
            return chinanet;
        if(phone.matches(unicomRegex))
            return unicom;
        return virtual;
    }
}
