package com.qjm3662.cloud_u_pan.Data;

/**
 * Created by tanshunwang on 2016/9/21 0021.
 */
public class ServerInformation {

    public static final String IP = "http://119.29.55.243";
//    public static final String IP = "http://172.6.2.8";

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


    //获取用户信息
    public static final String GetUserInfo = url + "/message";

    //获取分享中心
    public static final String Get_Share_center = url + "/shared";

    //修改用户头像
    public static final String Modify_User_Avatar = url + "/user/avatar";

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




    //修改用户信息
    public static final String Modify_User_Info = url + "/user/edit";

    /**
     * 修改用户信息

     url: 119.29.55.243:3000/user/edit post方法
     传参数 name username sex signature
     reture

     {
     code: 0,
     username: "String",
     sex: "Int",
     signature: "String"
     }
     */
    //关注某人
    public static final String FollowSB = url + "/user/follow";
    //取消关注某人
    public static final String UnFollowSB = url + "/user/unfollow";
    //获取关注的人的信息
    public static final String GetFollowingInfo = url + "/user/followMessage";
    //反馈
    public static final String CallBackInfo = url + "/user/feedback";
    //修改密码
    public static final String RevisePsd = url + "/user/editPass";
}
