package com.qjm3662.cloud_u_pan.Data;

import android.graphics.Bitmap;

import com.qjm3662.cloud_u_pan.Tool.TimeUtils;

/**
 * Created by tanshunwang on 2016/9/24 0024.
 */
public class FileInformation {
    private int id;
    private String identifyCode;
    private int isPublic;
    private int downloadCount;
    private long createTime;
    private String fileName;
    private double fileSize;
    private long updateTime;

    private Bitmap bitmap_type;
    private UserBase uploadUser;

    public FileInformation(){

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

    public int getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public double getFileSize() {
        return fileSize;
    }

    public void setFileSize(double fileSize) {
        this.fileSize = fileSize;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public Bitmap getBitmap_type() {
        return bitmap_type;
    }

    public void setBitmap_type(Bitmap bitmap_type) {
        this.bitmap_type = bitmap_type;
    }

    public UserBase getUploadUser() {
        return uploadUser;
    }

    public void setUploadUser(UserBase uploadUser) {
        this.uploadUser = uploadUser;
    }

    public String getDownTimeString(){
        return TimeUtils.returnTime(createTime);
    }


    public interface callBack{
        void call();
    }

    @Override
    public String toString() {
        return "FileInformation{" +
                "id=" + id +
                ", identifyCode='" + identifyCode + '\'' +
                ", isPublic=" + isPublic +
                ", downloadCount=" + downloadCount +
                ", createTime=" + createTime +
                ", fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", updateTime=" + updateTime +
                ", bitmap_type=" + bitmap_type +
                '}';
    }
}
