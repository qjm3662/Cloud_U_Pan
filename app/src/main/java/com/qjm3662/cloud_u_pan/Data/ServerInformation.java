package com.qjm3662.cloud_u_pan.Data;

/**
 * Created by tanshunwang on 2016/9/21 0021.
 */
public class ServerInformation {

    public static final String IP = "http://119.29.55.243";
    public static final String PORT = "3000";

    public static final String url = IP + ":" + PORT;

    //上传文件路由
    public static final String UPLoadFile = url + "/upload";

    //下载文件
    public static final String DownLoadFile = url + "/download/";

    //下载文件
    public static final String GetInformation = url + "/name/";


    //上传文件路由(登录后)
    public static final String UPLoadFile_AfterLogin = url + "/user/upload";

    //下载文件(登录后)
    public static final String DownLoadFile_AfterLogin = url + "/user/download/";

    //下载文件(登录后)
    public static final String GetInformation_AfterLogin = url + "/user/name/";

    //获取用户信息
    public static final String GetUserInfo = url + "/message";

    //获取分享中心
    public static final String Get_Share_center = url + "/shared";

    //注册
    public static final String REGISTER = url + "/user-register";
    /**
     * url: 119.29.55.243:3000/user-register post方法
     传参数name,pwd
     注册成功
     {
     code: 0
     }
     用户已经存在
     {
     code: -6
     }
     参数没传
     {
     code: -7
     }
     */
    //登陆
    public static final String LOGIN = url + "/user-login";
    /**
     * url: 119.29.55.243:3000/user-login post方法
     传参数 name,pwd
     登陆成功
     {
     code: 0
     }
     用户已经存在
     {
     code: -6
     }
     参数没传
     {
     code: -7
     }
     */
}
