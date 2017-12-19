package com.lanxi.couponcode.view;

import com.lanxi.couponcode.spi.assist.FileAssit;
import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.util.utils.LoggerUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangyuanjian on 12/18/2017.
 */
@Controller("patch")
@EasyLog(LoggerUtil.LogLevel.INFO)
public class PatchController {
    private static final Map<String,File> map=new HashMap<>();

    @RequestMapping("export")
    public void exportFile(HttpServletRequest req, HttpServletResponse res){
        String secret=req.getParameter("secret");
        File file=map.get(secret);
        synchronized (file){
            if(map.get(secret)!=null){
                FileAssit.export(map.get(secret),res);
                map.remove(secret);
            }
        }
    }
    public synchronized static void addFile(String key,File file){
        map.put(key,file);
    }
}
