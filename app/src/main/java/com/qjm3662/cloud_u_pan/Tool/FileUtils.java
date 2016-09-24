package com.qjm3662.cloud_u_pan.Tool;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by tanshunwang on 2016/9/21 0021.
 */
public class FileUtils {

    /**
     * 根据uri返回路径
     * @param context
     * @param uri
     * @return
     */
    public static String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection,null, null, null);
                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    /**
     * 获得SDCard的根目录
     * @return
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = new File("/sdcard/U_pan_file");//获取跟目录
        }
        if(!sdDir.exists()){
            sdDir.mkdirs();
//            System.out.println("dfgweasrrgesgesgrsdegerg");
        }
        return sdDir.toString();
    }

    public static String getPath(){
        String path = getSDPath() + "/U_pan_file/";
        File file = new File(path);
        if(!file.exists()){
            file.mkdir();
            System.out.println("54555");
        }
        return path;
    }


    /**
     * 保存文件
     *
     * @param file
     * @param fileName
     * @throws IOException
     */
    public static File saveFile(File file, String fileName) throws IOException {
        //文件夹的创建
        String path = getSDPath();
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
            System.out.println("maki");
        }

        File myCaptureFile = new File(path + "/" + fileName);
        FileInputStream in = new FileInputStream(file);
        BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        //读取大文件
        byte[] buffer=new byte[4*1024];
        while(in.read(buffer)!=-1){
            output.write(buffer);
        }
        output.flush();
        output.close();
        System.out.println("name : " + myCaptureFile.getName());
        System.out.println("path ： " + myCaptureFile.getPath());
        System.out.println(myCaptureFile.isFile());
        return myCaptureFile;
    }
}
