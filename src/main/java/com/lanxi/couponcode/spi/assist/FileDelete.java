package com.lanxi.couponcode.spi.assist;

import java.io.File;
import java.util.*;
import java.util.Set;

/**
 * @author yangyuanjian
 * @Date: Created in 1/17/2018 2:05 PM
 * @Modifer:
 */
public abstract class FileDelete {
    private static final Set<File> files1 = new HashSet<>();

    private static final Set<File> files2 = new HashSet<>();

    private static Set<File> files = null;

    private static Boolean on=true;

    public static void add(File file) {
        files = files1;
        files.add(file);
        synchronized(on){
            if (on){
                remove();
                on = false;
            }
        }
    }

    private static Set<File> remove() {
        Set<File> temp = null;
        synchronized(files){
            temp = files;
            files = files == files1 ? files2 : files1;
        }

        if (temp != null && !temp.isEmpty()){
            remove(temp);
        }
        return temp;
    }

    private static void remove(final Set<File> files) {
        Thread thread = new Thread(() -> {
            try{
                Thread.sleep(10000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            if(files.size()>0){
                List<File> temp = new ArrayList<>(files);
                while(temp.size() > 0){
                    File   file = temp.get(0);
                    String name = file.getName();
                    temp.remove(file);
                    files.remove(file);
                    if (file != null && file.exists())
                        file.delete();
                }
            }
            remove(remove());
        });
        thread.start();
    }


}
