package com.qjm3662.cloud_u_pan.Data;

import android.graphics.Bitmap;

import com.qjm3662.cloud_u_pan.Tool.TimeUtils;
import com.qjm3662.cloud_u_pan.UI.Login;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by qjm3662 on 2016/9/26 0026.
 */

public class LocalFile {
    private String name;
    private String path;
    private long downTime;
    private String type;
    private Bitmap bitmap_type;
    private boolean flag_is_DateNode;
    public static final String PHOTO = "photo";
    public static final String MUSIC = "music";
    public static final String VIDEO = "video";
    public static final String DOC = "doc";
    public static final String HTML = "html";
    public static final String PPT_PDF = "ppt pdf";
//    public static final String FILE = "file";
    public static final String ZIP = "zip";

    public LocalFile(String name, String path, long downTime, String type, Bitmap bitmap){
        this.name = name;
        this.path = path;
        this.downTime = downTime;
        this.type = type;
        this.flag_is_DateNode = false;
        this.bitmap_type = bitmap;
    }
    public LocalFile(){

    }


    public boolean isFlag_is_DateNode() {
        return flag_is_DateNode;
    }

    public void setFlag_is_DateNode(boolean flag_is_DateNode) {
        this.flag_is_DateNode = flag_is_DateNode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getDownTime() {
        return downTime;
    }

    public void setDownTime(long downTime) {
        this.downTime = downTime;
    }

    public String getDownTimeString(){
        return TimeUtils.returnTime(downTime);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Bitmap getBitmap_type() {
        return bitmap_type;
    }

    public void setBitmap_type(Bitmap bitmap_type) {
        this.bitmap_type = bitmap_type;
    }

    @Override
    public String toString() {
        return "LocalFile{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", downTime='" + downTime + '\'' +
                ", type='" + type + '\'' +
                ", flag_is_DateNode=" + flag_is_DateNode +
                '}';
    }
}
