package com.qjm3662.cloud_u_pan.Data;

/**
 * Created by tanshunwang on 2016/9/21 0021.
 */
public class ServerInformation {

    public static final String ERR_MSG = "errMSG";
        public static final String IP = "http://123.207.96.66";
//    public static final String IP = "http://172.6.2.8";
//    public static final String IP = "http://192.168.1.4";

    public static final String PORT = "8080";

        public static final String url = IP + ":" + PORT + "/MyUpan_war";
//    public static final String url = IP + ":" + PORT;

    //默认头像
    public static final String DEFAULT_AVATAR = url + "/download?fileName=default_avatar.jpg";
    //上传文件路由
    public static final String UPLoadFile = url + "/upload";

    //下载文件
    public static final String DownLoadFile = url + "/download/";

    //下载文件(下载前获取文件信息)
    public static final String GetInformation = url + "/GetFileInfoAction/";


    //上传文件路由(登录后)
    public static final String UPLoadFile_AfterLogin = url + "/user/upload";


    //获取用户信息
    public static final String GetUserInfo = url + "/GetUserInfoAction";

    //获取分享中心
    public static final String Get_Share_center = url + "/share";

    //修改用户头像
    public static final String Modify_User_Avatar = url + "/user/modifyAvatar";

    //注册
    public static final String REGISTER = url + "/user/RegisterAction";
    /**
     * url: 119.29.55.243:3000/user-register post方法
     * 传参数name,pwd
     * 注册成功
     * {
     * code: 0
     * }
     * 用户已经存在
     * {
     * code: -6
     * }
     * 参数没传
     * {
     * code: -7
     * }
     */
    //登陆
    public static final String LOGIN = url + "/user/LoginAction";
    /**
     * url: 119.29.55.243:3000/user-login post方法
     * 传参数 name,pwd
     * 登陆成功
     * {
     * code: 0
     * }
     * 用户已经存在
     * {
     * code: -6
     * }
     * 参数没传
     * {
     * code: -7
     * }
     */


    //修改用户信息
    public static final String Modify_User_Info = url + "/user/ModifyUserInfoAction";

    /**
     * 修改用户信息
     * <p>
     * url: 119.29.55.243:3000/user/edit post方法
     * 传参数 name username sex signature
     * reture
     * <p>
     * {
     * code: 0,
     * username: "String",
     * sex: "Int",
     * signature: "String"
     * }
     */
    //关注某人
    public static final String FollowSB = url + "/user/follow";
    //取消关注某人
    public static final String UnFollowSB = url + "/user/unFollow";
    //获取关注的人的信息
    public static final String GetFollowingInfo = url + "/user/followInfo";
    //反馈
    public static final String CallBackInfo = url + "/user/feedback";
    //修改密码
    public static final String RevisePsd = url + "/user/revisePSD";

    //百度云盘资源搜索
    public static final String BAIDU_SEARCH = IP + ":8096/yunPan/index";


}
