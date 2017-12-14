package com.lanxi.couponcode.impl.assist;

import com.lanxi.couponcode.spi.consts.annotations.Comment;
import com.lanxi.util.utils.ExcelUtil;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by yangyuanjian on 12/12/2017.
 */
public class ExcelAssist {
    private Workbook workbook;

    private Boolean readSkipFirstLine=null;
    private Boolean writeSkipFirstLine=null;

    private Supplier<Object> checkSkip=()->{
        throw new IllegalStateException("must set readSkipFirstLine and writeSkipFirstLine otherwise may result wrong data !");
    };
//    @Comment("0 is first number in poi !")
//    public List<String> readRow(int sheetNum,int rowNum,int colstart,int colend){
//    };
//    @Comment("0 is first number in poi !")
//    public List<String> readCol(int sheetNum,int colNum,int rowstart,int rowend){
//
//    }

    public void writeList(OutputStream out, List<Object> list, Map<String,String> headMap){
        ExcelUtil.exportExcelFile(list,headMap,out);
    }

    public void writeList(File file, List<Object> list,Map<String,String> headMap){
        try {
            FileOutputStream fout=new FileOutputStream(file);
            ExcelUtil.exportExcelFile(list,headMap,fout);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
