package com.qjm3662.cloud_u_pan.Data;

/**
 * Created by tanshunwang on 2016/9/24 0024.
 */
public class FileInformation {
    private String name;
    private float size;

    public FileInformation(){

    }
    public FileInformation(String name, float size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public interface callBack{
        void call();
    }
}
