package com.lanxi.couponcode.spi.assist;

import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.util.entity.LogFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Arrays;

/**
 * Created by yangyuanjian on 12/13/2017.
 */
public interface FileAssit {
    static boolean write(File file, OutputStream out) throws IOException {
        FileInputStream fin = null;
        fin = new FileInputStream(file);
        byte[] bytes = new byte[1024];
        int readLength = -1;
        while ((readLength = fin.read(bytes)) > 0) {
            if (readLength == bytes.length)
                out.write(bytes);
            else
                out.write(Arrays.copyOf(bytes, readLength));
        }
        out.flush();
        return true;
    }

    static void export(RetMessage<File> retMessage,HttpServletResponse res){

        try {
            if(!retMessage.getRetCode().equals(RetCodeEnum.success)){
                res.setContentType("text/html;charset=utf8");
                res.getOutputStream().write(new RetMessage(RetCodeEnum.error,"导出异常!",null).toJson().getBytes("utf-8"));
            }else {
                res.setContentType("octets/stream");
                res.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(retMessage.getDetail().getName(), "utf-8"));
                FileAssit.write(retMessage.getDetail(), res.getOutputStream());
            }
        } catch (IOException e) {

            try {
                res.setContentType("text/html;charset=utf8");
                res.getOutputStream().write(new RetMessage(RetCodeEnum.error,"导出异常!",null).toJson().getBytes("utf-8"));
            } catch (IOException e1) {
                LogFactory.info(FileAssit.class,"导出文件时发生异常!",e);
            }
        }
    }
}
