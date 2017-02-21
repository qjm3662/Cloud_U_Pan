package com.qjm3662.cloud_u_pan.Data;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by tanshunwang on 2016/10/1 0001.
 */

public class User extends UserBase{
    private Bitmap bitmap;
    private String avatarPath;
    private boolean relative;           //是否关注
    private String token;
    private List<FileInformation> shares_list;

    public boolean isRelative() {
        return relative;
    }

    public void setRelative(boolean relative) {
        this.relative = relative;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public User() {
        username = "我是昵称";
        nickname = "";
        avatar = ServerInformation.DEFAULT_AVATAR;
        sex = 1;
        bitmap = null;
    }


    public List<FileInformation> getShares_list() {
        return shares_list;
    }

    public void setShares_list(List<FileInformation> shares_list) {
        this.shares_list = shares_list;
    }


    public void setUser_not_static(User user){
        this.setNickname(user.getNickname());
        this.setAvatar(user.getAvatar());
        this.setSex(user.getSex());
        this.setUsername(user.getUsername());
        this.setSignature(user.getSignature());
    }

    public static void setUser(User user){
        if(instance == null){
            getInstance();
        }
        instance.setNickname(user.getNickname());
        instance.setAvatar(user.getAvatar());
        instance.setSex(user.getSex());
        instance.setUsername(user.getUsername());
        instance.setSignature(user.getSignature());
    }


    //单例化操作
    private static User instance = null;

    public static User getInstance(){
        if(instance == null){
            instance = new User();
        }
        return instance;
    }
    public static void deleteUser(){
        instance = null;
        getInstance();
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", sex=" + sex +
                ", signature='" + signature + '\'' +
                ", shares_list=" + shares_list +
                '}';
    }
}
