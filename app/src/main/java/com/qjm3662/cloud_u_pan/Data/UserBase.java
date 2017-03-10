package com.qjm3662.cloud_u_pan.Data;

/**
 * Created by qjm3662 on 2017/2/15 0015.
 */

public class UserBase {
    protected String username;    //用户名
    protected String nickname;        //昵称
    protected String avatar;      //头像
    protected int sex;            //1->Man
    protected String signature;   //个签


    public UserBase() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
