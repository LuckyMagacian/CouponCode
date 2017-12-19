package com.lanxi.couponcode.spi.assist;

import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.couponcode.view.PatchController;
import com.lanxi.util.entity.LogFactory;
import com.lanxi.util.utils.SignUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Optional;

/**
 * Created by yangyuanjian on 12/13/2017.
 */
public interface FileAssit {
    static boolean write(File file, OutputStream out) throws IOException {
        FileInputStream fin = null;
        try {
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
        } finally {
            Optional.of(fin).ifPresent(e -> {
                try {
                    e.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });
        }
    }

    static void export(RetMessage<File> retMessage, HttpServletResponse res) {
        try {
            if (!retMessage.getRetCode().equals(RetCodeEnum.success)) {
                res.setContentType("text/html;charset=utf8");
                res.getOutputStream().write(new RetMessage(RetCodeEnum.fail, "导出失败!", null).toJson().getBytes("utf-8"));
            } else {
                res.setContentType("octets/stream");
                res.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(retMessage.getDetail().getName(), "utf-8"));
                FileAssit.write(retMessage.getDetail(), res.getOutputStream());
            }
        } catch (IOException e) {
            try {
                res.setContentType("text/html;charset=utf8");
                res.getOutputStream().write(new RetMessage(RetCodeEnum.error, "导出异常!", null).toJson().getBytes("utf-8"));
            } catch (IOException e1) {
                LogFactory.info(FileAssit.class, "导出文件时发生异常!", e);
            }
        }
    }

    static void export(File file, HttpServletResponse res) {
        try {
            res.setContentType("application/octet-stream");
            res.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(file.getName(), "utf-8"));
            FileAssit.write(file, res.getOutputStream());
        } catch (IOException e) {
            try {
                res.setContentType("text/html;charset=utf8");
                res.getOutputStream().write(new RetMessage(RetCodeEnum.error, "导出异常!", null).toJson().getBytes("utf-8"));
            } catch (IOException e1) {
                LogFactory.info(FileAssit.class, "导出文件时发生异常!", e);
            }
        }
    }

    static void exportTest(File file, HttpServletResponse res) {
        try {
            res.setContentType("text/html;charset=utf8");
            if (file == null) {
                res.getOutputStream().write(new RetMessage(RetCodeEnum.fail, "导出失败!", null).toJson().getBytes("utf-8"));
            } else {
                String fileName = file.getName();
                String secret=SignUtil.md5LowerCase(fileName+TimeAssist.getNow(), "utf-8");
                PatchController.addFile(secret, file);
                res.getOutputStream().write(new RetMessage(RetCodeEnum.success, "导出成功!", secret).toJson().getBytes("utf-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
