package com.qjm3662.cloud_u_pan.Data;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by tanshunwang on 2016/10/1 0001.
 */

public class User{
    private String username;    //昵称
    private String name;        //用户账户
    private String avatar;      //头像
    private int sex;            //1->Man
    private String signature;   //个签
    private List<FileInformation> shares_list;
    private Bitmap bitmap;
    private String avatarPath;
    private boolean relative;

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

    private String token;

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
        name = "";
        avatar = "http://119.29.55.243:3000/avatar.jpg";
        sex = 1;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public List<FileInformation> getShares_list() {
        return shares_list;
    }

    public void setShares_list(List<FileInformation> shares_list) {
        this.shares_list = shares_list;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }


    public void setUser_not_static(User user){
        this.setName(user.getName());
        this.setAvatar(user.getAvatar());
        this.setSex(user.getSex());
        this.setUsername(user.getUsername());
        this.setSignature(user.getSignature());
    }

    public static void setUser(User user){
        if(instance == null){
            getInstance();
        }
        instance.setName(user.getName());
        instance.setAvatar(user.getAvatar());
        instance.setSex(user.getSex());
        instance.setUsername(user.getUsername());
        instance.setSignature(user.getSignature());
    }
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", sex=" + sex +
                ", signature='" + signature + '\'' +
                ", shares_list=" + shares_list +
                '}';
    }

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
}
