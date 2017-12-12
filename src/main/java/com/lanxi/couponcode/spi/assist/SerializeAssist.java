package com.lanxi.couponcode.spi.assist;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.lanxi.util.utils.SignUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * Created by yangyuanjian on 2017/11/25.
 */
public interface SerializeAssist {

    static  byte[] serialize(Serializable t, Class clazz){
        Kryo kryo=new Kryo();
        kryo.setReferences(true);
        if(clazz==null)
            kryo.setRegistrationRequired(false);
        else
            kryo.register(clazz);
        Output output=new Output(new ByteArrayOutputStream());
        kryo.writeClassAndObject(output,t);
        return output.getBuffer();
    }

    static <T extends Serializable> T unserialize(byte[] bytes, Class<? extends Serializable> clazz){
        Kryo kryo=new Kryo();
        kryo.setReferences(true);
        if(clazz==null)
            kryo.setRegistrationRequired(false);
        else
            kryo.register(clazz);
        Input input=new Input(new ByteArrayInputStream(bytes));
        if(clazz!=null)
            return kryo.readObject(input, (Class<T>) clazz);
        return (T) kryo.readClassAndObject(input);
    }



    static  byte[] serialize(Serializable t){
        return serialize(t,null);
    }

    static <T extends Serializable> T unserialize(byte[] bytes){
        return unserialize(bytes,null);
    }

    static  String serializeToString(Serializable t, Class clazz){
        byte[] bytes=serialize(t,clazz);
        try {
            return new String(SignUtil.base64De(bytes),"utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("exception occurred when serialize T["+t+"],Class["+clazz+"]");
        }
    }

    static String serializeToString(Serializable t){
        return serializeToString(t,null);
    }




}
