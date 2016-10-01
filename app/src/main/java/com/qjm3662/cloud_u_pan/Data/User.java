package com.qjm3662.cloud_u_pan.Data;

/**
 * Created by tanshunwang on 2016/10/1 0001.
 */

class User{
    private String username;    //昵称
    private String name;        //用户账户
    private String avatar;      //头像
    private int sex;            //1->Man

    private User() {
        username = "我是昵称";
        name = "";
        avatar = "http://119.29.55.243:3000/avatar.jpg";
        sex = 1;
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
