package com.lanxi.couponcode.view;

import com.lanxi.couponcode.spi.assist.ArgAssist;
import com.lanxi.couponcode.spi.assist.FileAssit;
import com.lanxi.couponcode.spi.assist.FileDelete;
import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.couponcode.spi.consts.enums.CommodityType;
import com.lanxi.couponcode.spi.service.QuartzService;
import com.lanxi.couponcode.spi.util.MsgRechargeBean;
import com.lanxi.couponcode.spi.util.XmlUtil;
import com.lanxi.util.entity.LogFactory;
import com.lanxi.util.utils.LoggerUtil;
import com.lanxi.util.utils.RandomUtil;
import com.lanxi.util.utils.SignUtil;
import com.lanxi.util.utils.TimeUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yangyuanjian on 12/18/2017.
 */
@Controller("patch")
@RequestMapping("patch")
@EasyLog(LoggerUtil.LogLevel.INFO)
public class PatchController {
    private static final Map<String, File> map = new ConcurrentHashMap<>();
    @Resource(name = "quartzControllerServiceRef")
    private QuartzService quartzService;

    @Resource
    private XmlApiController xml;

    public void exportFile(String secret,HttpServletResponse res){
        try{
            File   file   = map.get(secret);
            file.length();
            synchronized(file){
                if(file != null){
                    FileAssit.export(file, res);
                    if((file.getName().endsWith(".xlsx") || file.getName().endsWith(".xls")) && (!file.getName().equals("template.xlsx"))){
                        Thread.sleep(1000);
                        map.remove(secret);
                    }
                }
            }
        }catch(Exception e){
            LogFactory.warn(this, "下载文件时发生异常!", e);
        }
    }

    @RequestMapping("export")
    public void exportFile(HttpServletRequest req, HttpServletResponse res){

        String secret = req.getParameter("secret");
        exportFile(secret,res);

    }

    @RequestMapping("overtime")
    public void codeOvertime(HttpServletRequest req, HttpServletResponse res){
        quartzService.codeOverTime();
    }

    @RequestMapping("dailyRecord")
    public void daiyRecord(HttpServletRequest req, HttpServletResponse res){
        quartzService.addClearDailyRecord();
    }

    @RequestMapping("clearRecord")
    public void clearRecord(HttpServletRequest req, HttpServletResponse res){
        quartzService.addClearRecords();
    }

    @RequestMapping("mockorder")
    @ResponseBody
    public String mockOrder(HttpServletRequest req, HttpServletResponse res) throws UnsupportedEncodingException{
        String commodityId=req.getParameter("commodityId");
        //创建文档
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("utf-8");
        //创建根节点
        Element root = DocumentHelper.createElement("JFDH");
        //创建消息头
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

        //创建消息体
        Element msg = DocumentHelper.createElement("MSG");
        Element serialNum = DocumentHelper.createElement("SerialNum");
        Element phone = DocumentHelper.createElement("Phone");
        Element type = DocumentHelper.createElement("Type");
        Element skuCode = DocumentHelper.createElement("SkuCode");
        Element count = DocumentHelper.createElement("Count");
        Element needSend = DocumentHelper.createElement("NeedSend");
        Element remark = DocumentHelper.createElement("Remark");
        //添加根节点
        document.add(root);
        //添加消息头与消息体
        root.add(head);
        root.add(msg);

        //消息头填充
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

        //消息体填充
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
        msgid.setText(RandomUtil.getRandomNumber(8));
        reserve.setText(RandomUtil.getRandomChar(18));

        serialNum.setText(RandomUtil.getRandomNumber(18));
        phone.setText("15068610940");
        type.setText(CommodityType.eleCoupon.getValue());
        skuCode.setText(commodityId==null?"954257525001568256":commodityId);
        count.setText(RandomUtil.getRandomNumber(1));
        needSend.setText("1");
        remark.setText(RandomUtil.getRandomChar(20));

        //计算签名
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
        String signString = SignUtil.md5LowerCase(signParamSB.toString(), "utf-8");
        //填充签名
        sign.setText(signString);

        xmlString=document.asXML();

        //创建mock请求
        MockHttpServletRequest mockreq=new MockHttpServletRequest();
        MockHttpServletResponse mockres=new MockHttpServletResponse();
        mockreq.setContent(xmlString.getBytes("utf-8"));
        //模拟调用
        return xml.purchaseOfElectronicGoods(mockreq,mockres);
    }

    public static void addFile(String key, File file){
        map.put(key, file);
        FileDelete.add(file);
    }
}
