package com.qjm3662.cloud_u_pan.Data;

/**
 * Created by tanshunwang on 2016/9/24 0024.
 */
public class FileInformation {
    private int id;
    private String identifyCode;
    private boolean share;
    private int downloadCount;
    private String createdAt;
    private String name;
    private double size;
    public FileInformation(){

    }
    public FileInformation(String name, float size) {
        this.name = name;
        this.size = size;
    }

    public FileInformation(int id, String identifyCode, boolean share, String createdAt, String name, double size) {
        this.id = id;
        this.identifyCode = identifyCode;
        this.share = share;
        this.createdAt = createdAt;
        this.name = name;
        this.size = size;
        this.downloadCount = 6;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdentifyCode() {
        return identifyCode;
    }

    public void setIdentifyCode(String identifyCode) {
        this.identifyCode = identifyCode;
    }

    public boolean isShare() {
        return share;
    }

    public void setShare(boolean share) {
        this.share = share;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public interface callBack{
        void call();
    }

    @Override
    public String toString() {
        return "FileInformation{" +
                "id=" + id +
                ", identifyCode='" + identifyCode + '\'' +
                ", share=" + share +
                ", downloadCount=" + downloadCount +
                ", createdAt=" + createdAt +
                ", name='" + name + '\'' +
                ", size=" + size +
                '}';
    }
}
