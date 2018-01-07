package com.lanxi.couponcode.view.assist;

import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.assist.TimeAssist;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.couponcode.view.PatchController;
import com.lanxi.util.utils.SignUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * Created by yangyuanjian on 1/2/2018.
 */
public interface FillAssist {
    static String exportTest(File file, HttpServletResponse res) {
        res.setContentType("text/html;charset=utf8");
        if (file == null) {
            return new RetMessage(RetCodeEnum.fail, "导出失败!", null).toJson();
        } else {
            String fileName = file.getName();
            String secret = SignUtil.md5LowerCase(fileName + TimeAssist.getNow(), "utf-8");
            PatchController.addFile(secret, file);
            return new RetMessage(RetCodeEnum.success, "导出成功!", secret).toJson();
        }
    }
}
