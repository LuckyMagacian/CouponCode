package com.lanxi.couponcode.test;

import com.alibaba.dubbo.common.utils.LogUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.lanxi.couponcode.impl.assist.ExcelAssist;
import com.lanxi.couponcode.impl.assist.PredicateAssist;
import com.lanxi.couponcode.impl.entity.*;
import com.lanxi.couponcode.impl.newservice.MerchantServiceImpl;
import com.lanxi.couponcode.spi.aop.AddLog;
import com.lanxi.couponcode.spi.aop.AopOrder;
import com.lanxi.couponcode.spi.assist.*;
import com.lanxi.couponcode.spi.config.ConstConfig;
import com.lanxi.couponcode.spi.config.HiddenMap;
import com.lanxi.couponcode.spi.config.Path;
import com.lanxi.couponcode.spi.consts.enums.AccountType;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.couponcode.spi.defaultInterfaces.ToJson;
import com.lanxi.util.entity.LogFactory;
import com.lanxi.util.utils.LoggerUtil;
import com.lanxi.util.utils.RandomUtil;
import com.lanxi.util.utils.SignUtil;
import org.apache.ibatis.annotations.Arg;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.lang.reflect.Array;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by yangyuanjian on 2017/11/9.
 */
public class TestOther {

    @Test
    public void test1() {
        int[] i = new int[]{1, 2, 3};
        System.out.println(i.getClass());
        System.out.println(i instanceof Serializable);
        System.out.println(Array.class.isAssignableFrom(i.getClass()));
        System.out.println(Serializable.class.isAssignableFrom(ArrayList.class));
    }

    @Test
    public void test2() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        InputStream            is      = request.getInputStream();
        int                    temp    = -1;
        File                   file    = new File("file.name");
        file.createNewFile();
        FileOutputStream fout = new FileOutputStream(file);
        while ((temp = is.read()) != -1) {
            fout.write(temp);
        }

    }


    @Test
    public void test3() {
        RetMessage<Boolean> m1 = new RetMessage<>(RetCodeEnum.success, "布尔", false);
        RetMessage<String>  m2 = new RetMessage<>(RetCodeEnum.success, "字符串", "true");
        RetMessage<Boolean> m3 = new RetMessage<>(RetCodeEnum.fail, "布尔null", null);
        RetMessage<String>  m4 = new RetMessage<>(RetCodeEnum.fail, "字符串null", null);

        String s1 = m1.toJson();
        String s2 = m2.toJson();
        String s3 = m3.toJson();
        String s4 = m4.toJson();

        System.out.println(s1);
        System.out.println(s2);
        System.out.println(s3);
        System.out.println(s4);

        System.out.println(JSONObject.parseObject(s1, RetMessage.class).getDetail().getClass());
        System.out.println(JSONObject.parseObject(s2, RetMessage.class).getDetail().getClass());
        System.out.println((RetMessage<String>) JSONObject.parseObject(s3, RetMessage.class));
        System.out.println(JSONObject.parseObject(s4, RetMessage.class).getDetail());

    }

    @Test
    public void test4() {
        String pageNum  = "123";
        String pageSize = "2333";

        String time1 = "20161226";
        String time2 = "201711251040";

        String addr = " 这zzzz啊啊啊1-213(3幢)!";
        System.out.println(CheckAssist.chineseAssicNumOnly(addr));
    }

    @Test
    public void test5() {
        System.out.println(IdWorker.getId());
    }

    @Test
    public void test6() {
        String       str  = "012346";
        List<String> list = new ArrayList<>();
        while (list.size() < 10)
            list.add(list.size() + "");

        System.out.println((List) SerializeAssist.unserialize(SerializeAssist.serialize((Serializable) list)));

    }

    @Test
    public void test7() {
        Merchant merchant = new Merchant();
        merchant.setMerchantId(1L);
        Commodity commodity = new Commodity();
        commodity.setMerchantId(1L);
        Account account = new Account();
        account.setMerchantId(1L);
        Shop shop = new Shop();
        System.out.println(PredicateAssist.diffMerchant.test(merchant, commodity));
        System.out.println(PredicateAssist.diffMerchant.test(account, commodity));
        System.out.println(PredicateAssist.diffMerchant.test(merchant, shop));
        System.out.println(PredicateAssist.diffMerchant.test(shop, merchant));

    }

    @Test
    public void test8() {
        Account account = new Account();
        account.setAccountId(1234L);
        Shop shop = new Shop();
        shop.setShopId(1234L);
        System.out.println(shop.equals(account));
    }

    @Test
    public void test9() {
        RetMessage message = new RetMessage();
        message.setAll(RetCodeEnum.success, "嘿嘿嘿", new RetMessage<>(RetCodeEnum.fail, "啦啦啦啦", "oooo").toJson());
        System.out.println(message.toJson());
        System.out.println(SignUtil.md5LowerCase(message.toJson(), "utf-8"));
        System.out.println(SignUtil.md5En(SerializeAssist.serialize(message.toJson())));
    }

    @Test
    public void test10() {
        Account account = (Account) TestSpring.fillEntity.apply(new Account());
        System.out.println(account.insert());
    }

    @Test
    public void test11() {
        System.out.println(RetCodeEnum.success);
        System.out.println(new RetMessage<>(RetCodeEnum.success, "aaa").toJson());
        System.out.println(JSONObject.toJSON(new com.lanxi.util.entity.RetMessage(com.lanxi.util.consts.RetCodeEnum.SUCCESS, "123456", new Object())));
    }

    @Test
    public void test12() throws Exception {
        AopOrder.setEnvSizeLimit(1);
        AopOrder.addAop(AddLog.class);
        System.out.println(AopOrder.getEnvs());
    }

    @Test
    public void test13() {
        System.out.println(IdWorker.getId());
    }

    @Test
    public void test14() {
        Merchant merchant = new Merchant();
        TestSpring.fillEntity.apply(merchant);
        merchant.setMerchantId(IdWorker.getId());
        System.out.println(merchant.toJson());
    }

    @Test
    public void test15() {
        final String idRegex = "(id)||([a-zA-Z0-9]+Id)";
        System.out.println("validateCode".matches(idRegex));
        System.out.println("merchatnId".matches(idRegex));
        System.out.println("id".matches(idRegex));
    }

    @Test
    public void test16() {
        System.out.println(RedisKeyAssist.getLoginKey(938694848231235585L));
    }

    @Test
    public void test17() {
        Map<String, Object> map      = new HashMap<>();
        Merchant            merchant = new Merchant();
        TestSpring.fillEntity.apply(merchant);
        merchant.setMerchantId(IdWorker.getId());
        map.put("token", "12346");
        map.put("merchant", merchant);

        Map<String, Object> map1 = new HashMap<>();
        map1.put("detail", map);
        System.out.println(JSON.toJSONString(map));
        System.out.println(ToJson.toJson(map));
    }

    @Test
    public void test18() {
        Account code = new Account();
        TestSpring.fillEntity.apply(code);
        Map<String, Object> map = new HashMap<>();
        map.put("a", "aaa");
        map.put("code", null);
        System.out.println(new RetMessage<>(RetCodeEnum.success, "操作成功!", (HashMap) map).toJson());
    }

    @Test
    public void test19() {
        String realPath = MerchantServiceImpl.class.getClassLoader().getResource("").
                getPath() + Path.businessLicensePicPath.replace("classpath:", "");
        System.err.println(realPath);
    }

    @Test
    public void test20() {
        System.out.println(SignUtil.md5LowerCase("123456", "utf-8"));
    }

    @Test
    public void test21() {
        List<String> list = new ArrayList<>();
        list.add("merchantName");
        list.add("merchantId");
        Merchant merchant = new Merchant();
        FillAssist.fillEntityFieldAll(merchant);
        System.out.println(merchant.toJson(list));
    }

    @Test
    public void test22() {
        Map<String, Object> map = SignUtil.getKeyPair();
        System.err.println(SignUtil.getPrivateKey(map));
        System.out.println();
        System.out.println(SignUtil.getPublicKey(map));
    }

    @Test
    public void test23() {
        String key  = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmSnPI2rg92DhLF6PnwHoq2wgeMfKXkhzv4psnFuN7rPN8hWqRzCatraUVpQlTWmNd+IkcpQgN/ZEsDkuixtrLqPeyylgTLmZu0M+hhbp+tPhI2cOdvqveIHI3/0ErXipc0OkAmX2Hl+uEcl53js3qULzuHzBVkhlD2CxgKCFCrrZuO2foicsXl1OdHt7ZC+xfFS+N0mCPdibwKqBU4l5ETS9VdqNzsnz3zhzhxzRAlNwMQ7UhMVi+3zGqHFMMGqRFMBxKMFTpFwmh5Eaqxp0kw2/S8pNnNsILj9aYF6IgVC3+g7Rk/mzhBXTimQa81+kr1fgcR3adOtqasTW0wX5kQIDAQAB";
        String info = "U2FsdGVkX1/JqnDnxr0eZqV9frHC1aYe";
        System.out.println(ArgAssist.argStrDe.apply(key, info));
    }

    @Test
    public void test24() throws Exception {
        LoggerUtil.setLogLevel(LoggerUtil.LogLevel.DEBUG);
        LoggerUtil.init();
        String desKeyCipher            = "SElhNYycy5pEbdbi2TZd5jlNz5rvBRGlllokrRlQ7adbN1ykgJSzeEyctrYIJJ8Dy4vBXNV7g5taDCfBp3tZDt0ZA%2FPgNTUNwE7i7Y6flSGbdVV1PBJQnA3%2FYUMjUOFmnq1SbHfGSCTRz4nvAKvbsmJfQJKkTq6XSUq%2BNeooD%2B7qNhMAikhsDkulYSoowjPHSV3lBmFA91nkhNFKn6PzgeAkebHxAJcyzmkDGKc8SHD1xtAReUbsZ%2BNL47rehFycZkTcwflKYcztlUFuaA76p35aM4jaTy7Ge1vRDgml%2BNloOhNm1KwqEjFvt9QxCDFVT24mWrtlMynjd3krDY50rg%3D%3D";
        String argCipher="Cv6AZtQvyS3JgxuFNUUvPZh7dsCuCmYETZzgnr%2Fx2HSfntKWWn97Fv5y2Y1D9%2FrrBBZLxnzAFCcSx8WMcZFClA%3D%3D";
    MockHttpServletRequest req=new MockHttpServletRequest();
        req.addParameter("secret",desKeyCipher);
        req.addParameter("args",argCipher);
        System.out.println(ArgAssist.getArg.apply(req,"phone"));
    }

    @Test
    public void test25() throws Exception {
        String plainText="您好";
        String key="abcdefgabcdefg12";
        System.out.println(plainText);
        String cipher=AesAssist.aesEncrypt(plainText,key);
        System.out.println(cipher);
        System.out.println(AesAssist.aesDecrypt(cipher,key));
    }
    @Test
    public void test26(){
        System.out.println(FillAssist.getMap.apply(AccountType.admin,Merchant.class));
    }
    @Test
    public void test27(){
        System.out.println(Stream.of(HiddenMap.class.getDeclaredFields()).filter(f->Map.class.isAssignableFrom(f.getType())).map(f->f.getClass().getName()).collect(Collectors.toList()));
    }
    @Test
    public void test28() throws UnsupportedEncodingException {
        String value="7fivtSDmyGHV9aaj1wxVYocLNR967tnbx1UBAnZK0s+/PXNb0P8odlg0oAUt8P2oHixqt9eX4/LbwNl8wpkHTA==";
        System.out.println( SignUtil.rsaDe(
                ConstConfig.priKey, SignUtil.base64De(value.getBytes("utf-8"))));
    }
}
