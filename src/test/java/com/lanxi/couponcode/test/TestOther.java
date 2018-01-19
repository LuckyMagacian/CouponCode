package com.lanxi.couponcode.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.lanxi.couponcode.spi.assist.FileDelete;
import com.lanxi.couponcode.impl.assist.FillAssist;
import com.lanxi.couponcode.impl.assist.PredicateAssist;
import com.lanxi.couponcode.impl.entity.*;
import com.lanxi.couponcode.impl.newservice.MerchantServiceImpl;
import com.lanxi.couponcode.spi.aop.AddLog;
import com.lanxi.couponcode.spi.aop.AopOrder;
import com.lanxi.couponcode.spi.assist.*;
import com.lanxi.couponcode.spi.config.ConstConfig;
import com.lanxi.couponcode.impl.config.HiddenMap;
import com.lanxi.couponcode.spi.config.Path;
import com.lanxi.couponcode.spi.consts.enums.AccountType;
import com.lanxi.couponcode.spi.consts.enums.CommodityStatus;
import com.lanxi.couponcode.spi.consts.enums.CommodityType;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.couponcode.spi.defaultInterfaces.ToJson;
import com.lanxi.couponcode.spi.util.MsgRechargeBean;
import com.lanxi.couponcode.spi.util.XmlUtil;
import com.lanxi.util.utils.*;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.*;
import java.lang.reflect.Array;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by yangyuanjian on 2017/11/9.
 */
public class TestOther {

    @Test
    public void test1(){
        int[] i = new int[]{1, 2, 3};
        System.out.println(i.getClass());
        System.out.println(i instanceof Serializable);
        System.out.println(Array.class.isAssignableFrom(i.getClass()));
        System.out.println(Serializable.class.isAssignableFrom(ArrayList.class));
    }

    @Test
    public void test2() throws IOException{
        MockHttpServletRequest request = new MockHttpServletRequest();
        InputStream            is      = request.getInputStream();
        int                    temp    = -1;
        File                   file    = new File("file.name");
        file.createNewFile();
        FileOutputStream fout = new FileOutputStream(file);
        while((temp = is.read()) != -1){
            fout.write(temp);
        }

    }


    @Test
    public void test3(){
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
        System.out.println((RetMessage<String>)JSONObject.parseObject(s3, RetMessage.class));
        System.out.println(JSONObject.parseObject(s4, RetMessage.class).getDetail());

    }

    @Test
    public void test4(){
        String pageNum  = "123";
        String pageSize = "2333";

        String time1 = "20161226";
        String time2 = "201711251040";

        String addr = " 这zzzz啊啊啊1-213(3幢)!";
        System.out.println(CheckAssist.chineseAssicNumOnly(addr));
    }

    @Test
    public void test5(){
        System.out.println(IdWorker.getId());
    }

    @Test
    public void test6(){
        String       str  = "012346";
        List<String> list = new ArrayList<>();
        while(list.size() < 10)
            list.add(list.size()+"");

        System.out.println((List)SerializeAssist.unserialize(SerializeAssist.serialize((Serializable)list)));

    }

    @Test
    public void test7(){
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
    public void test8(){
        Account account = new Account();
        account.setAccountId(1234L);
        Shop shop = new Shop();
        shop.setShopId(1234L);
        System.out.println(shop.equals(account));
    }

    @Test
    public void test9(){
        RetMessage message = new RetMessage();
        message.setAll(RetCodeEnum.success, "嘿嘿嘿", new RetMessage<>(RetCodeEnum.fail, "啦啦啦啦", "oooo").toJson());
        System.out.println(message.toJson());
        System.out.println(SignUtil.md5LowerCase(message.toJson(), "utf-8"));
        System.out.println(SignUtil.md5En(SerializeAssist.serialize(message.toJson())));
    }

    @Test
    public void test10(){
        Account account = (Account)TestSpring.fillEntity.apply(new Account());
        System.out.println(account.insert());
    }

    @Test
    public void test11(){
        System.out.println(RetCodeEnum.success);
        System.out.println(new RetMessage<>(RetCodeEnum.success, "aaa").toJson());
        System.out.println(JSONObject.toJSON(new com.lanxi.util.entity.RetMessage(com.lanxi.util.consts.RetCodeEnum.SUCCESS, "123456", new Object())));
    }

    @Test
    public void test12() throws Exception{
        AopOrder.setEnvSizeLimit(1);
        AopOrder.addAop(AddLog.class);
        System.out.println(AopOrder.getEnvs());
    }

    @Test
    public void test13(){
        System.out.println(IdWorker.getId());
    }

    @Test
    public void test14(){
        Merchant merchant = new Merchant();
        TestSpring.fillEntity.apply(merchant);
        merchant.setMerchantId(IdWorker.getId());
        System.out.println(merchant.toJson());
    }

    @Test
    public void test15(){
        final String idRegex = "(id)||([a-zA-Z0-9]+Id)";
        System.out.println("validateCode".matches(idRegex));
        System.out.println("merchatnId".matches(idRegex));
        System.out.println("id".matches(idRegex));
    }

    @Test
    public void test16(){
        System.out.println(RedisKeyAssist.getLoginKey(938694848231235585L));
    }

    @Test
    public void test17(){
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
    public void test18(){
        Account code = new Account();
        TestSpring.fillEntity.apply(code);
        Map<String, Object> map = new HashMap<>();
        map.put("a", "aaa");
        map.put("code", null);
        System.out.println(new RetMessage<>(RetCodeEnum.success, "操作成功!", (HashMap)map).toJson());
    }

    @Test
    public void test19(){
        String realPath = MerchantServiceImpl.class.getClassLoader().getResource("").
                getPath()+Path.businessLicensePicPath.replace("classpath:", "");
        System.err.println(realPath);
    }

    @Test
    public void test20(){
        System.out.println(SignUtil.md5LowerCase("123456", "utf-8"));
    }

    @Test
    public void test21(){
        List<String> list = new ArrayList<>();
        list.add("merchantName");
        list.add("merchantId");
        Merchant merchant = new Merchant();
        FillAssist.fillEntityFieldAll(merchant);
        System.out.println(merchant.toJson(list));
    }

    @Test
    public void test22(){
        Map<String, Object> map = SignUtil.getKeyPair();
        System.err.println(SignUtil.getPrivateKey(map));
        System.out.println();
        System.out.println(SignUtil.getPublicKey(map));
    }

    @Test
    public void test23(){
        String key  = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmSnPI2rg92DhLF6PnwHoq2wgeMfKXkhzv4psnFuN7rPN8hWqRzCatraUVpQlTWmNd+IkcpQgN/ZEsDkuixtrLqPeyylgTLmZu0M+hhbp+tPhI2cOdvqveIHI3/0ErXipc0OkAmX2Hl+uEcl53js3qULzuHzBVkhlD2CxgKCFCrrZuO2foicsXl1OdHt7ZC+xfFS+N0mCPdibwKqBU4l5ETS9VdqNzsnz3zhzhxzRAlNwMQ7UhMVi+3zGqHFMMGqRFMBxKMFTpFwmh5Eaqxp0kw2/S8pNnNsILj9aYF6IgVC3+g7Rk/mzhBXTimQa81+kr1fgcR3adOtqasTW0wX5kQIDAQAB";
        String info = "U2FsdGVkX1/JqnDnxr0eZqV9frHC1aYe";
        System.out.println(ArgAssist.argStrDe.apply(key, info));
    }

    @Test
    public void test24() throws Exception{
        LoggerUtil.setLogLevel(LoggerUtil.LogLevel.DEBUG);
        LoggerUtil.init();
        String                 desKeyCipher = "SElhNYycy5pEbdbi2TZd5jlNz5rvBRGlllokrRlQ7adbN1ykgJSzeEyctrYIJJ8Dy4vBXNV7g5taDCfBp3tZDt0ZA%2FPgNTUNwE7i7Y6flSGbdVV1PBJQnA3%2FYUMjUOFmnq1SbHfGSCTRz4nvAKvbsmJfQJKkTq6XSUq%2BNeooD%2B7qNhMAikhsDkulYSoowjPHSV3lBmFA91nkhNFKn6PzgeAkebHxAJcyzmkDGKc8SHD1xtAReUbsZ%2BNL47rehFycZkTcwflKYcztlUFuaA76p35aM4jaTy7Ge1vRDgml%2BNloOhNm1KwqEjFvt9QxCDFVT24mWrtlMynjd3krDY50rg%3D%3D";
        String                 argCipher    = "Cv6AZtQvyS3JgxuFNUUvPZh7dsCuCmYETZzgnr%2Fx2HSfntKWWn97Fv5y2Y1D9%2FrrBBZLxnzAFCcSx8WMcZFClA%3D%3D";
        MockHttpServletRequest req          = new MockHttpServletRequest();
        req.addParameter("secret", desKeyCipher);
        req.addParameter("args", argCipher);
        System.out.println(ArgAssist.getArg.apply(req, "phone"));
    }

    @Test
    public void test25() throws Exception{
        String plainText = "您好";
        String key       = "abcdefgabcdefg12";
        System.out.println(plainText);
        String cipher = AesAssist.aesEncrypt(plainText, key);
        System.out.println(cipher);
        System.out.println(AesAssist.aesDecrypt(cipher, key));
    }

    @Test
    public void test26(){
        System.out.println(FillAssist.getMap.apply(AccountType.admin, Merchant.class));
    }

    @Test
    public void test27(){
        System.out.println(Stream.of(HiddenMap.class.getDeclaredFields()).filter(f -> Map.class.isAssignableFrom(f.getType())).map(f -> f.getClass().getName()).collect(Collectors.toList()));
    }

    @Test
    public void test28() throws UnsupportedEncodingException{
        String value = "7fivtSDmyGHV9aaj1wxVYocLNR967tnbx1UBAnZK0s+/PXNb0P8odlg0oAUt8P2oHixqt9eX4/LbwNl8wpkHTA==";
        System.out.println(SignUtil.rsaDe(
                ConstConfig.priKey, SignUtil.base64De(value.getBytes("utf-8"))));
    }

    @Test
    public void test29(){
        System.out.println(Account.class.getSimpleName());
    }

    @Test
    public void test30(){
        System.out.println(IdWorker.getId());
        System.out.println((IdWorker.getId()+"").length());
        System.out.println();
    }

    @Test
    public void test31(){
        Commodity commodity = new Commodity();
        FillAssist.fillEntityFieldAll(commodity);
        Commodity copy = BeanUtil.deepCopy(commodity);
        System.out.println(commodity.hashCode()+":"+commodity);
        System.out.println(copy.hashCode()+":"+copy);
    }

    @Test
    public void test32(){
        System.out.println(TimeAssist.timeFixZero(TimeAssist.getLastMonth(), 14));
    }

    @Test
    public void test33() throws UnsupportedEncodingException{
        String keyCipher   = "MExWvuYQck910FnaJEzD2YOo%2FIY6JiCJlDG4iGd20w2XXufvXG6QCWERElqdaes2%2BDAYK7lyjp%2FbXqnVYVcwooquPBFV7mhdl3RA1pZOW2MSrT1I%2FKgLxhl%2FPZVXYpNi6Wf2GLpAG8eun8tFeFX97mfMhpQ%2FkNEWZqjMycC0UUWY%2BOk70TFUMTXh3wLAmHMasTYqrW2NmxfMx%2FRgKyJjlSa7G6sRbNv44%2Fa%2FfrRhes1PGJucIqbatcrakWv5PRc%2B6b7YddnGmkyJJuLVncKk38hGnPFpcpYva7TAKGIvqrzh8rKxtS%2BP%2FJ423ZgJFC8vRZetAiHKCtFKrF0wGWcckg%3D%3D";
        String paramCipher = "VB0Gepg%2BbLdKB9BlH%2FDGLBzMmqetjGZB5haJBslaOf%2FECywW3H90D27wNhzaT02ChpsFYUY2A6Nrk%2B4%2B6z76VncsSnbKwYCNzNHTtjLgAKsKMqOsgJsXsNyLgcHijCR9h4d1QMVwaxINxyD4CFIsO3LFBy84aD5NslsWWo4f1QW511hJ7YMJ4DRKB60w1OVRfMUMGAUgjXfqVNnB9Eysp6HsbroL9zym4A%2Bm1MHestByhOI3uO2cq59sRigaZRqC4Y%2B7XFqXGTLvIr%2Bg3Rj29y9YKQfQll8Y%2BiAb%2FaWDthI%3D";

        keyCipher = URLDecoder.decode(keyCipher, "utf-8");
        paramCipher = URLDecoder.decode(paramCipher, "utf-8");


        String keyPlain   = ArgAssist.symmetricalKeyDe.apply(keyCipher);
        String paramPlain = ArgAssist.argStrDe.apply(keyPlain, paramCipher);


        System.out.println(keyPlain);
        System.out.println(paramPlain);
    }

    @Test
    public void test34(){
        String keyPlain    = "BEPiPTEZPLmke44h";
        String paramCipher = "kl2MkmdHG4bkspMC+oNLSxNAiLPjiQ9mo4cV9sCQsqn/ocRnIOP6wudSK00U2jvTgKVpanwLNNCoYHdQyzCb0SKwd+2CHfG7DSRadeboiYkXf6385qXpPsfvLZAQQgUZR6QudAMLSyGJG2tJP7+XJ7WfeAwSjh1Rb0Icny6Dv8gqrunjb+TlKBGbibOFVIQHhcYMq4xmFBvwWigQ3SMoJQmJT9ManeSr+tYh9RKFC4l+GDPRttjtqk+tRNnmAzw3Y7P7sRKkCucYUduptaIzlwZnbj7fYv6SLHehSBk8oEs=";
        System.out.println(ArgAssist.argStrDe.apply(keyPlain, paramCipher));
    }

    @Test
    public void test35() throws IOException{
        //        File file=File.createTempFile("code",".xlsx",null);
        File file = Files.createFile(Paths.get("aaa.test")).toFile();
        file.createNewFile();
        System.out.println(file.getAbsolutePath());
        file.delete();
    }

    @Test
    public void test36(){
        System.out.println("123456");
        try{
            test37();
        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println(78910);
    }

    @Test
    public void test37() throws IOException{
        File file = new File("aaa.test");
        file.createNewFile();
        System.out.println(file.getAbsolutePath());
        file.delete();
    }

    @Test
    public void test38(){
    }

    public <T extends Serializable> void ser(T t){
        System.out.println(t.getClass());
        System.out.println(t instanceof Serializable);
    }

    @Test
    public void test39(){
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("utf-8");
        Element root = DocumentHelper.createElement("JFDH");

        Element head     = DocumentHelper.createElement("HEAD");
        Element ver      = DocumentHelper.createElement("VER");
        Element msgNo    = DocumentHelper.createElement("MsgNo");
        Element chkdate  = DocumentHelper.createElement("CHKDATE");
        Element workdate = DocumentHelper.createElement("WorkDate");
        Element workTime = DocumentHelper.createElement("WorkTime");
        Element add      = DocumentHelper.createElement("ADD");
        Element src      = DocumentHelper.createElement("SRC");
        Element des      = DocumentHelper.createElement("DES");
        Element app      = DocumentHelper.createElement("APP");
        Element msgid    = DocumentHelper.createElement("MsgID");
        Element reserve  = DocumentHelper.createElement("Reserve");
        Element sign     = DocumentHelper.createElement("Sign");

        Element msg = DocumentHelper.createElement("MSG");
        Element serialNum = DocumentHelper.createElement("SerialNum");
        Element phone = DocumentHelper.createElement("Phone");
        Element type = DocumentHelper.createElement("Type");
        Element skuCode = DocumentHelper.createElement("SkuCode");
        Element count = DocumentHelper.createElement("Count");
        Element needSend = DocumentHelper.createElement("NeedSend");
        Element remark = DocumentHelper.createElement("Remark");

        document.add(root);

        root.add(head);
        root.add(msg);

        head.add(ver);
        head.add(msgNo);
        head.add(chkdate);
        head.add(workdate);
        head.add(workTime);
        head.add(add);
        head.add(src);
        head.add(des);
        head.add(app);
        head.add(msgid);
        head.add(reserve);
        head.add(sign);

        msg.add(serialNum);
        msg.add(phone);
        msg.add(type);
        msg.add(skuCode);
        msg.add(count);
        msg.add(needSend);
        msg.add(remark);

        ver.setText("1.0");
        msgNo.setText(RandomUtil.getRandomNumber(18));
        chkdate.setText(TimeUtil.getDate());
        workdate.setText(TimeUtil.getDate());
        workTime.setText(TimeUtil.getTime());
        add.setText(RandomUtil.getRandomNumber(18));
        src.setText(RandomUtil.getRandomNumber(18));
        des.setText(RandomUtil.getRandomNumber(18));
        app.setText("lanxi");
        msgid.setText(RandomUtil.getRandomNumber(18));
        reserve.setText(RandomUtil.getRandomChar(18));

        serialNum.setText(RandomUtil.getRandomNumber(18));
        phone.setText("15068610940");
        type.setText(CommodityType.eleCoupon.getValue());
        skuCode.setText("950993444853428224");
        count.setText(RandomUtil.getRandomNumber(1));
        needSend.setText("1");
        remark.setText(RandomUtil.getRandomChar(20));

        String xmlString=document.asXML();

        MsgRechargeBean msgRechargeBean = XmlUtil.getMsgPrepaidRechargeBeanByXmlStr(xmlString);

        StringBuilder signParamSB = new StringBuilder();
        /** HEAD */
        signParamSB.append(msgRechargeBean.getVER()).append(msgRechargeBean.getMsgNo())
                   .append(msgRechargeBean.getCHKDate()).append(msgRechargeBean.getWorkDate())
                   .append(msgRechargeBean.getWorkTime()).append(msgRechargeBean.getADD())
                   .append(msgRechargeBean.getSRC()).append(msgRechargeBean.getDES())
                   .append(msgRechargeBean.getAPP()).append(msgRechargeBean.getMsgID())
                   .append(msgRechargeBean.getReserve());
        /** MSG */
        signParamSB.append(msgRechargeBean.getSerialNum()).append(msgRechargeBean.getPhone()).append(msgRechargeBean.getType())
                   .append(msgRechargeBean.getSkuCode()).append(msgRechargeBean.getCount())
                   .append(msgRechargeBean.getNeedSend()).append(msgRechargeBean.getRemark());
        String signString = SignUtil.md5LowerCase(signParamSB.toString(),"utf-8");
        sign.setText(signString);

        xmlString=document.asXML();
    }

    @Test
    public  void test40(){
        Commodity commodity=new Commodity();
        commodity.setStatus(CommodityStatus.shelved);
        commodity.setType(CommodityType.eleCoupon);
        String str =commodity.toJson();
        commodity=JSON.parseObject(str,Commodity.class);
        System.out.println(commodity);
    }
    @Test
    public void test41(){
        System.out.println(TimeAssist.getLastdayBegin());
        System.out.println(TimeAssist.getLastdayEnd());
    }
    @Test
    public void test42() throws InterruptedException, IOException {
        File file1 =new File("1.txt");
        file1.createNewFile();
        File file2 =new File("2.txt");
        file2.createNewFile();
        File file3 =new File("3.txt");
        file3.createNewFile();
        File file4 =new File("4.txt");
        file4.createNewFile();
        File file5 =new File("5.txt");
        file5.createNewFile();
        File file6 =new File("6.txt");
        file6.createNewFile();
        File file7 =new File("7.txt");
        file7.createNewFile();
        File file8 =new File("8.txt");
        file8.createNewFile();
        FileDelete.add(file1);
        FileDelete.add(file2);
        FileDelete.add(file3);
        FileDelete.add(file4);
        Thread.sleep(5000);
        FileDelete.add(file5);
        FileDelete.add(file6);
        Thread.sleep(5000);
        FileDelete.add(file7);
        Thread.sleep(5000);
        FileDelete.add(file8);
        while(true);
    }
    @Test
    public void testThroable() {
        try{
            aa();
        }catch(Throwable throwable){
            throwable.printStackTrace();
        }
    }

    public void aa() throws Throwable {
        Exception exception=new NullPointerException();
        Throwable t=exception;
        throw t;
    }
    @Test
    public void test43(){
        String code="1192809696|5036660130|3856574778|3744787671|1490568608|5012846931|";
        System.out.println(code.substring(0,code.lastIndexOf("|")));
    }
    @Test
    public void test45(){
        System.out.println(SignUtil.md5LowerCase("131056","utf-8"));
    }
}
