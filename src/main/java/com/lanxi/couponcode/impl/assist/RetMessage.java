package com.lanxi.couponcode.impl.assist;

import com.lanxi.util.interfaces.ToJson;
import com.lanxi.util.interfaces.ToMap;

/**
 * Created by yangyuanjian on 2017/11/2.
 */
public class RetMessage implements ToJson,ToMap{
    private String retCode;
    private String retMessage;
    private Object detail;

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMessage() {
        return retMessage;
    }

    public void setRetMessage(String retMessage) {
        this.retMessage = retMessage;
    }

    public Object getDetail() {
        return detail;
    }

    public void setDetail(Object detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "RetMessage{" +
                "retCode='" + retCode + '\'' +
                ", retMessage='" + retMessage + '\'' +
                ", detail=" + detail +
                '}';
    }
}
