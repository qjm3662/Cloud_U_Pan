package com.qjm3662.cloud_u_pan.Tool;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import com.qjm3662.cloud_u_pan.Data.LocalFile;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Widget.EasySweetAlertDialog;

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
     * 根据文件后缀名获得对应的MIME类型。
     * @param file
     */
    public static String getMIMEType(File file)
    {
        String type="*/*";
        String fName=file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if(dotIndex < 0){
            return type;
        }
    /* 获取文件的后缀名 */
        String end=fName.substring(dotIndex,fName.length()).toLowerCase();
        if(end=="")return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for(int i=0;i<MIME_MapTable.length;i++){
            if(end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }


    //建立一个MIME类型与文件后缀名的匹配表
    public static final String[][] MIME_MapTable={
            //{后缀名，    MIME类型}
            {".3gp", LocalFile.VIDEO},
            {".apk", LocalFile.ZIP},
            //{".asf",    "video/x-ms-asf"},
            {".avi",    LocalFile.VIDEO},
//            {".bin",    "application/octet-stream"},
            {".bmp",      LocalFile.PHOTO},
            {".c",        LocalFile.DOC},
            {".class",    LocalFile.DOC},
//            {".conf",    "text/plain"},
            {".cpp",    LocalFile.DOC},
            {".doc",    LocalFile.DOC},
//            {".exe",    "application/octet-stream"},
            {".gif",    LocalFile.PHOTO},
//            {".gtar",    "application/x-gtar"},
//            {".gz",        "application/x-gzip"},
            {".h",        LocalFile.DOC},
            {".htm",    LocalFile.HTML},
            {".html",    LocalFile.HTML},
//            {".jar",    "application/java-archive"},
            {".java",    LocalFile.DOC},
            {".jpeg",    LocalFile.PHOTO},
            {".jpg",    LocalFile.PHOTO},
            {".js",      LocalFile.DOC},
//            {".log",    "text/plain"},
//            {".m3u",    "audio/x-mpegurl"},
//            {".m4a",    "audio/mp4a-latm"},
//            {".m4b",    "audio/mp4a-latm"},
//            {".m4p",    "audio/mp4a-latm"},
//            {".m4u",    "video/vnd.mpegurl"},
//            {".m4v",    "video/x-m4v"},
//            {".mov",    "video/quicktime"},
//            {".mp2",    "audio/x-mpeg"},
            {".mp3",    LocalFile.MUSIC},
            {".mp4",    LocalFile.VIDEO},
            {".mpc",    LocalFile.VIDEO},
            {".mpe",    LocalFile.VIDEO},
            {".mpeg",    LocalFile.VIDEO},
            {".mpg",    LocalFile.VIDEO},
            {".mpg4",    LocalFile.VIDEO},
//            {".mpga",    "audio/mpeg"},
//            {".msg",    "application/vnd.ms-outlook"},
//            {".ogg",    "audio/ogg"},
            {".pdf",    LocalFile.PPT_PDF},
            {".png",    LocalFile.PHOTO},
//            {".pps",    "application/vnd.ms-powerpoint"},
            {".ppt",    LocalFile.PPT_PDF},
//            {".prop",    "text/plain"},
            {".rar",    LocalFile.ZIP},
//            {".rc",        "text/plain"},
            {".rmvb",    LocalFile.PPT_PDF},
//            {".rtf",    "application/rtf"},
//            {".sh",        "text/plain"},
//            {".tar",    "application/x-tar"},
//            {".tgz",    "application/x-compressed"},
            {".txt",    LocalFile.DOC},
            {".wav",    LocalFile.MUSIC},
            {".wma",    LocalFile.MUSIC},
            {".wmv",    LocalFile.MUSIC},
            {".wps",    LocalFile.DOC},
            //{".xml",    "text/xml"},
            {".xml",    LocalFile.DOC},
//            {".z",        "application/x-compress"},
            {".zip",    LocalFile.ZIP},
            {"",        "*/*"}
    };


    /**
     * 根据路径打开文件
     * @param context
     * @param path
     * @param fileName
     */
    public static void OpenFile(Context context, String path, String fileName) {
        if(path !=null)
        {
            File currentPath = new File(path);
            if(!currentPath.exists()){
                try {
                    currentPath.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Intent intent;
            if(checkEndsWithInStringArray(fileName, context.getResources().
                    getStringArray(R.array.fileEndingImage))){
                intent = OpenFiles.getImageFileIntent(currentPath);
                context.startActivity(intent);
            }else if(checkEndsWithInStringArray(fileName, context.getResources().
                    getStringArray(R.array.fileEndingWebText))){
                intent = OpenFiles.getHtmlFileIntent(currentPath);
                context.startActivity(intent);
            }else if(checkEndsWithInStringArray(fileName, context.getResources().
                    getStringArray(R.array.fileEndingPackage))){
                intent = OpenFiles.getApkFileIntent(currentPath);
                context.startActivity(intent);

            }else if(checkEndsWithInStringArray(fileName, context.getResources().
                    getStringArray(R.array.fileEndingAudio))){
                intent = OpenFiles.getAudioFileIntent(currentPath);
                context.startActivity(intent);
            }else if(checkEndsWithInStringArray(fileName, context.getResources().
                    getStringArray(R.array.fileEndingVideo))){
                intent = OpenFiles.getVideoFileIntent(currentPath);
                context.startActivity(intent);
            }else if(checkEndsWithInStringArray(fileName, context.getResources().
                    getStringArray(R.array.fileEndingText))){
                intent = OpenFiles.getTextFileIntent(currentPath);
                context.startActivity(intent);
            }else if(checkEndsWithInStringArray(fileName, context.getResources().
                    getStringArray(R.array.fileEndingPdf))){
                intent = OpenFiles.getPdfFileIntent(currentPath);
                context.startActivity(intent);
            }else if(checkEndsWithInStringArray(fileName, context.getResources().
                    getStringArray(R.array.fileEndingWord))){
                intent = OpenFiles.getWordFileIntent(currentPath);
                context.startActivity(intent);
            }else if(checkEndsWithInStringArray(fileName, context.getResources().
                    getStringArray(R.array.fileEndingExcel))){
                intent = OpenFiles.getExcelFileIntent(currentPath);
                context.startActivity(intent);
            }else if(checkEndsWithInStringArray(fileName, context.getResources().
                    getStringArray(R.array.fileEndingPPT))){
                intent = OpenFiles.getPPTFileIntent(currentPath);
                context.startActivity(intent);
            }else
            {
                EasySweetAlertDialog.ShowTip(context, "无法打开，请安装相应的软件！");
            }
        }else
        {
            EasySweetAlertDialog.ShowTip(context, "对不起，这不是文件！");
        }
    }


    public static boolean checkEndsWithInStringArray(String checkItsEnd,
                                               String[] fileEndings){
        for(String aEnd : fileEndings){
            if(checkItsEnd.endsWith(aEnd))
                return true;
        }
        return false;
    }

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
