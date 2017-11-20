package com.lanxi.couponcode.impl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;



public class FileUpLoadUtil {
	public static void fileUpLoad(File file,String path) throws Exception{
			InputStream is=null;
			OutputStream os=null;
			is=new FileInputStream(file);
			File file2=new File(path);
			file2.createNewFile();
			os=new FileOutputStream(file2);
			byte temp[] = new byte[1024];  
            int size = -1;  
            while ((size = is.read(temp)) != -1) { // 每次读取1KB，直至读完  
                os.write(temp, 0, size);  
            }  
			is.close();
			os.close();
	}
}
