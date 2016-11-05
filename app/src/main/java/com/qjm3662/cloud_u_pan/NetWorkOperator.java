package com.qjm3662.cloud_u_pan;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.widget.EditText;

import com.google.gson.Gson;
import com.qjm3662.cloud_u_pan.Data.FileInformation;
import com.qjm3662.cloud_u_pan.Data.LocalFile;
import com.qjm3662.cloud_u_pan.Data.LocalFileDB;
import com.qjm3662.cloud_u_pan.Data.ServerInformation;
import com.qjm3662.cloud_u_pan.Data.User;
import com.qjm3662.cloud_u_pan.Tool.AvatarUtils;
import com.qjm3662.cloud_u_pan.Tool.FileUtils;
import com.qjm3662.cloud_u_pan.Tool.NetworkUtils;
import com.qjm3662.cloud_u_pan.UI.DownloadUi2;
import com.qjm3662.cloud_u_pan.UI.Followings;
import com.qjm3662.cloud_u_pan.UI.Login;
import com.qjm3662.cloud_u_pan.UI.OthersMain;
import com.qjm3662.cloud_u_pan.UI.ShareCenter;
import com.qjm3662.cloud_u_pan.UI.UploadUi;
import com.qjm3662.cloud_u_pan.UI.UserMain;
import com.qjm3662.cloud_u_pan.Widget.EasySweetAlertDialog;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;

/**
 * 优云网络相关操作
 * Created by qjm3662 on 2016/9/21 0021.
 */
public class NetWorkOperator {
    /**
     * 修改密码
     * @param context
     * @param oldPsd
     * @param newPsd
     */
    public static void RevisePsd(final Context context, String oldPsd, String newPsd){
        if (App.NeworkFlag == NetworkUtils.NETWORK_FLAG_NOT_CONNECT) {
            EasySweetAlertDialog.ShowTip(context, "tip", "请检查您的网络连接");
            return;
        }
        OkHttpUtils
                .post()
                .url(ServerInformation.RevisePsd)
                .addParams("password", oldPsd)
                .addParams("newpassword", newPsd)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        System.out.println(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getInt("code") == 0){
                                EasySweetAlertDialog.ShowSuccess(context, "修改成功，请重新登陆", new EasySweetAlertDialog.SuccessCallBack() {
                                    @Override
                                    public void Success(Context c) {
                                        c.startActivity(new Intent(c, Login.class));
                                        ((Activity)c).overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                        Intent intent = new Intent();
                                        intent.setAction(UserMain.FINISH_SIGNAL);
                                        c.sendBroadcast(intent);
                                        User.deleteUser();
                                        App.Flag_IsLogin = false;
                                        //删除SharedPreferences的记录
                                        App.deleteUserInfo(c);
                                        ((Activity)c).finish();
                                    }
                                });
                            }else{
                                EasySweetAlertDialog.ShowTip(context, "Tip", "密码不正确，请重新输入");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    /**
     * 反馈
     *
     * @param context
     * @param name
     * @param text
     */
    public static void FeedBack(final Context context, String name, String text, final EditText et_feedBack) {
        if (App.NeworkFlag == NetworkUtils.NETWORK_FLAG_NOT_CONNECT) {
            EasySweetAlertDialog.ShowTip(context, "tip", "请检查您的网络连接");
            return;
        }
        OkHttpUtils
                .post()
                .url(ServerInformation.CallBackInfo)
                .addParams("name", name)
                .addParams("text", text)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        System.out.println(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            switch (jsonObject.getInt("code")) {
                                case 0:
                                    EasySweetAlertDialog.ShowSuccess(context, "Success", "我们已收到您的反馈，谢谢合作！");
                                    et_feedBack.setText("");
                                    break;
                                case -5:
                                    EasySweetAlertDialog.ShowTip(context, "Tip", "登录失效，请退出后重新登录");
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 获取关注的人信息
     *
     * @param context
     * @param list
     */
    public static void GetFollowingInformation(final Context context, final List<User> list) {
        if (App.NeworkFlag == NetworkUtils.NETWORK_FLAG_NOT_CONNECT) {
            EasySweetAlertDialog.ShowTip(context, "tip", "请检查您的网络连接");
            return;
        }
        OkHttpUtils
                .get()
                .url(ServerInformation.GetFollowingInfo)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        System.out.println(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            switch (jsonObject.getInt("code")) {
                                case 0:
                                    JSONArray jsonArray = jsonObject.getJSONArray("message");
                                    final int length = jsonArray.length();
                                    Gson gson = new Gson();
                                    User user = null;
                                    list.clear();
                                    AvatarUtils.AvatarCallBack callBack = new AvatarUtils.AvatarCallBack() {
                                        @Override
                                        public void callback(Bitmap bitmap) {

                                        }

                                        @Override
                                        public void callBack_2(User u, Bitmap bitmap, int position) {
                                            u.setBitmap(bitmap);
                                            list.add(u);
                                            if (position == length - 1) {
                                                Intent intent = new Intent();
                                                intent.setAction(Followings.ACTION_SUCCESS);
                                                context.sendBroadcast(intent);
                                            }
                                        }
                                    };
                                    if (jsonArray.length() == 0) {
                                        Intent intent = new Intent();
                                        intent.setAction(Followings.ACTION_SUCCESS);
                                        context.sendBroadcast(intent);
                                        return;
                                    }
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        user = gson.fromJson(jsonArray.get(i).toString(), User.class);
                                        AvatarUtils.getBitmapByUrl(user.getAvatar(), callBack, user, i);
                                    }
                                    System.out.println(Arrays.toString(list.toArray()));
                                    break;
                                case -5:
                                    EasySweetAlertDialog.ShowTip(context, "Tip", "登录失效，请退出后重新登录");
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    /**
     * 取消关注某人
     *
     * @param context
     * @param targetName
     * @param viewHolder
     */
    public static void UnFollowSB(final Context context, String targetName, final OthersMain.ViewHolder viewHolder) {
        if (App.NeworkFlag == NetworkUtils.NETWORK_FLAG_NOT_CONNECT) {
            EasySweetAlertDialog.ShowTip(context, "tip", "请检查您的网络连接");
            return;
        }
        OkHttpUtils
                .post()
                .url(ServerInformation.UnFollowSB)
                .addParams("myselfName", User.getInstance().getName())
                .addParams("otherName", targetName)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        System.out.println("关注api ERROR : " + e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            switch (jsonObject.getInt("code")) {
                                case 0:
                                    viewHolder.set(false);
                                    break;
                                case -5:
                                    EasySweetAlertDialog.ShowTip(context, "Tip", "登录失效，请退出后重新登录");
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 关注某人
     *
     * @param context
     * @param targetName
     * @param viewHolder
     */
    public static void FollowSB(final Context context, String targetName, final OthersMain.ViewHolder viewHolder) {
        if (App.NeworkFlag == NetworkUtils.NETWORK_FLAG_NOT_CONNECT) {
            EasySweetAlertDialog.ShowTip(context, "tip", "请检查您的网络连接");
            return;
        }
        OkHttpUtils
                .get()
                .url(ServerInformation.FollowSB)
                .addParams("myselfName", User.getInstance().getName())
                .addParams("otherName", targetName)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        System.out.println("关注api ERROR : " + e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            switch (jsonObject.getInt("code")) {
                                case 0:
                                    viewHolder.set(true);
                                    break;
                                case -5:
                                    EasySweetAlertDialog.ShowTip(context, "Tip", "登录失效，请退出后重新登录");
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    /**
     * 修改用户信息
     *
     * @param context
     * @param username
     */
    public static void modifyUserInfo(final Context context, String username) {
        if (App.NeworkFlag == NetworkUtils.NETWORK_FLAG_NOT_CONNECT) {
            EasySweetAlertDialog.ShowTip(context, "tip", "请检查您的网络连接");
            return;
        }
        User user = User.getInstance();
        OkHttpUtils
                .post()
                .url(ServerInformation.Modify_User_Info)
                .addParams("name", user.getName())
                .addParams("username", username)
                .addParams("sex", String.valueOf(user.getSex()))
                .addParams("signature", user.getSignature())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        System.out.println(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            switch (jsonObject.getInt("code")) {
                                case 0:
                                    getUserInfo(context, User.getInstance().getName(), 3);
                                    break;
                                case -5:
                                    EasySweetAlertDialog.ShowTip(context, "Tip", "登录失效，请退出后重新登录");
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    /**
     * 获取分享中心
     * @param context
     */
    public static void getShareCenter(final Context context, final List<FileInformation> list) {
        OkHttpUtils
                .get()
                .url(ServerInformation.Get_Share_center)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        System.out.println(e.toString());
                        Intent intent = new Intent();
                        intent.setAction(ShareCenter.ACTION_FAIL);
                        context.sendBroadcast(intent);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getInt("code") == 0) {
                                Gson gson = new Gson();
                                list.clear();
                                JSONArray jsonArray = jsonObject.getJSONArray("shares");
                                FileInformation fileInformation = null;
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    fileInformation = gson.fromJson(jsonArray.get(i).toString(), FileInformation.class);
                                    String type = FileUtils.getMIMEType(new File(fileInformation.getName()));
                                    fileInformation.setBitmap_type(FileUtils.getImgHead_not_down(type));
                                    list.add(fileInformation);
                                    Intent intent = new Intent();
                                    intent.setAction(ShareCenter.ACTION_SUCCESS);
                                    context.sendBroadcast(intent);
                                }
                            }
                        } catch (JSONException e) {
                            System.out.println("json 错误");
                            System.out.println(e.toString());
                            Intent intent = new Intent();
                            intent.setAction(ShareCenter.ACTION_FAIL);
                            context.sendBroadcast(intent);
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 获取其他用户的信息
     *
     * @param context
     * @param name
     */
    public static void getOtherUserInfoByName(final Context context, String name, final boolean isNeedFinish) {
        if (App.NeworkFlag == NetworkUtils.NETWORK_FLAG_NOT_CONNECT) {
            EasySweetAlertDialog.ShowTip(context, "tip", "请检查您的网络连接");
            return;
        }
        String myName = null;
        if (App.Flag_IsLogin) {
            myName = User.getInstance().getName();
        } else {
            myName = "NULL";
        }
        OkHttpUtils
                .get()
                .url(ServerInformation.GetUserInfo)
                .addParams("name", myName)
                .addParams("other", name)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        System.out.println(e.toString());
                        EasySweetAlertDialog.ShowTip(context, "Tip", "获取失败，请检查您的网络设置");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        System.out.println("获取用户信息 ：" + response);
                        try {
                            final JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getInt("code") == 0) {
                                Gson gson = new Gson();
                                App.user_temp = new User();
                                App.user_temp.setUser_not_static(gson.fromJson(jsonObject.toString(), User.class));
                                if (jsonObject.getBoolean("relative")) {
                                    App.user_temp.setRelative(true);
                                } else {
                                    System.out.println("mdzz");
                                }
                                JSONArray jsonArray = jsonObject.getJSONArray("shares");
                                final List<FileInformation> shares = new ArrayList<FileInformation>();
                                FileInformation fileInformation = null;
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    fileInformation = gson.fromJson(jsonArray.get(i).toString(), FileInformation.class);
                                    String type = FileUtils.getMIMEType(new File(fileInformation.getName()));
                                    fileInformation.setBitmap_type(FileUtils.getImgHead_not_down(type));
                                    shares.add(fileInformation);
                                }
                                App.user_temp.setShares_list(shares);
                                AvatarUtils.AvatarCallBack callBack = new AvatarUtils.AvatarCallBack() {
                                    @Override
                                    public void callback(Bitmap bitmap) {
                                        App.user_temp.setBitmap(bitmap);
                                        Intent intent = new Intent(context, OthersMain.class);
                                        intent.putExtra("WHERE", 3);
                                        context.startActivity(intent);
                                        App.startAnim((Activity)context);
                                        if(isNeedFinish){
                                            ((Activity) context).finish();
                                        }
                                    }

                                    @Override
                                    public void callBack_2(User u, Bitmap bitmap, int position) {

                                    }
                                };
                                AvatarUtils.getBitmapByUrl(App.user_temp.getAvatar(), callBack);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 获取用户信息
     *
     * @param context
     * @param name
     * @param where   1-登录   2-注册   3-非第一次
     */
    public static void getUserInfo(final Context context, String name, final int where) {
        if (App.NeworkFlag == NetworkUtils.NETWORK_FLAG_NOT_CONNECT) {
            EasySweetAlertDialog.ShowTip(context, "tip", "请检查您的网络连接");
            return;
        }
        OkHttpUtils
                .get()
                .url(ServerInformation.GetUserInfo)
                .addParams("name", "NULL")
                .addParams("other", name)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        System.out.println(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        System.out.println("获取用户信息1 ：" + response);
                        try {
                            final JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getInt("code") == 0) {
                                App.Flag_IsLogin = true;
                                Gson gson = new Gson();
                                User.setUser(gson.fromJson(jsonObject.toString(), User.class));
                                JSONArray jsonArray = jsonObject.getJSONArray("shares");
                                final List<FileInformation> shares = new ArrayList<FileInformation>();
                                FileInformation fileInformation = null;
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    fileInformation = gson.fromJson(jsonArray.get(i).toString(), FileInformation.class);
                                    String type = FileUtils.getMIMEType(new File(fileInformation.getName()));
                                    fileInformation.setBitmap_type(FileUtils.getImgHead_not_down(type));
                                    shares.add(fileInformation);
                                }
                                if (where != 3) {
                                    context.startActivity(new Intent(context, UserMain.class));
                                    ((Activity) context).finish();
                                    App.startAnim((Activity)context);
                                }
                                User.getInstance().setShares_list(shares);
                                System.out.println(User.getInstance().toString());
                                AvatarUtils.AvatarCallBack callBack = new AvatarUtils.AvatarCallBack() {
                                    @Override
                                    public void callback(Bitmap bitmap) {
                                        User.getInstance().setBitmap(bitmap);
                                        File file = new File(FileUtils.getPath(), "header.jpg");
                                        User.getInstance().setAvatarPath(file.getAbsolutePath());
                                        BufferedOutputStream bos = null;
                                        try {
                                            bos = new BufferedOutputStream(
                                                    new FileOutputStream(file));
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                                            bos.flush();
                                            bos.close();
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        SharedPreferences sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("UserJson", jsonObject.toString());
                                        editor.putString("AvatarPath", file.getAbsolutePath());
                                        editor.apply();
                                        Intent intent = new Intent();
                                        intent.setAction(UserMain.ACTION_GET_USER_INFO_SUCCESS);
                                        context.sendBroadcast(intent);
                                    }

                                    @Override
                                    public void callBack_2(User u, Bitmap bitmap, int position) {

                                    }
                                };
                                AvatarUtils.getBitmapByUrl(User.getInstance().getAvatar(), callBack);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    /**
     * 下载文件
     *
     * @param context
     * @param call
     * @param fileInformation
     * @return
     */
    public static void Down(final Context context, RequestCall call, FileInformation fileInformation) {
        if (App.NeworkFlag == NetworkUtils.NETWORK_FLAG_NOT_CONNECT) {
            EasySweetAlertDialog.ShowTip(context, "tip", "请检查您的网络连接");
            return;
        } else if (App.NeworkFlag == NetworkUtils.NETWORK_FLAG_MOBILE && App.Down_In_Wifi_Switch_State) {
            EasySweetAlertDialog.ShowTip(context, "tip", "已开启wifi下下载");
            return;
        }
        final Intent[] intent = {new Intent(), new Intent()};
        final int[] current_progress = {0};
        call.execute(new FileCallBack(App.currentSavePath, fileInformation.getName()) {
            @Override
            public void onError(Call call, Exception e, int id) {
                System.out.println(e.toString());
                EasySweetAlertDialog.ShowTip(context, "tip", "下载失败");
            }

            @Override
            public void onResponse(File response, int id) {
                System.out.println("response : " + response);
                System.out.println(response.getAbsolutePath());
                intent[1].setAction(DownloadUi2.DownloadFilePathAction);
                intent[1].putExtra(DownloadUi2.DownloadfilePath, response.toString());
                intent[1].putExtra("TYPE", FileUtils.getMIMEType(response));

                String type = FileUtils.getMIMEType(response);
                LocalFile localFile = new LocalFile(response.getName(), response.getAbsolutePath(), System.currentTimeMillis(), type);
                App.Public_List_Local_File_Download.add(localFile);
                ContentValues cv = new ContentValues();
                cv.put(LocalFileDB.COLUMN_NAME_Name, localFile.getName());
                cv.put(LocalFileDB.COLUMN_NAME_DownTime, System.currentTimeMillis());
                cv.put(LocalFileDB.COLUMN_NAME_Path, localFile.getPath());
                cv.put(LocalFileDB.COLUMN_NAME_Type, localFile.getType());
                App.dbWrite.insert(LocalFileDB.TABLE_NAME_LOCAL_FILE_DOWNLOAD, null, cv);
                context.sendBroadcast(intent[1]);
            }

            @Override
            public void inProgress(float progress, long total, int id) {
                super.inProgress(progress, total, id);
                if (current_progress[0] != (int) (progress * 100)) {
                    current_progress[0] = (int) (progress * 100);
                    intent[0] = new Intent();
                    intent[0].setAction(DownloadUi2.DownLoadProgressAction);
                    intent[0].putExtra(DownloadUi2.DownloadProgressing, current_progress[0]);
                    context.sendBroadcast(intent[0]);
                }
            }
        });
    }

    /**
     * 下载文件之前获取文件的信息
     *
     * @param id_
     * @param fileInformation
     * @param callBack
     * @return
     */
    public static String GetFileInformation(final Context context, final String id_, final FileInformation fileInformation, final FileInformation.callBack callBack) {
        if (App.NeworkFlag == NetworkUtils.NETWORK_FLAG_NOT_CONNECT) {
            EasySweetAlertDialog.ShowTip(context, "tip", "请检查您的网络连接");
            return "";
        }
        OkHttpUtils
                .get()
                .url(ServerInformation.GetInformation + id_)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        System.out.println(e.toString());
                        System.out.println("id :" + id_);
                        EasySweetAlertDialog.ShowTip(context, "tip", "获取文件信息失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        System.out.println(response);
                        try {
                            JSONObject jo = new JSONObject(response);
                            System.out.println(jo.get("code"));
                            fileInformation.setName(jo.getString("name"));
                            fileInformation.setSize((float) jo.getDouble("size"));
                            if (!jo.getString("uploadUser").equals("nobody")) {
                                fileInformation.setUpLoadUser(jo.getString("uploadUser"));
                                fileInformation.setUpLoadUserAvatar(jo.getJSONObject("user").getString("avatar"));
                                fileInformation.setUpLoadUserName(jo.getJSONObject("user").getString("username"));
                                fileInformation.setCreatedAt(jo.getLong("time"));
                            }else{
                                fileInformation.setUpLoadUser(null);
                            }
                            callBack.call();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        return "  ";
    }


    /**
     * 更改用户头像
     *
     * @param context
     * @param file
     */
    public static void ModifyUserAvatar(final Context context, File file) {
        if (App.NeworkFlag == NetworkUtils.NETWORK_FLAG_NOT_CONNECT) {
            EasySweetAlertDialog.ShowTip(context, "tip", "请检查您的网络连接");
            return;
        }
        OkHttpUtils
                .post()
                .url(ServerInformation.Modify_User_Avatar)
                .addFile("avatar", file.getName(), file)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        System.out.println();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            switch (jsonObject.getInt("code")) {
                                case 0:
                                    getUserInfo(context, User.getInstance().getName(), 3);
                                    break;
                                case -5:
                                    EasySweetAlertDialog.ShowTip(context, "Tip", "登录失效，请退出后重新登录");
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }



    static class MySuccessCalback implements EasySweetAlertDialog.SuccessCallBack{

        @Override
        public void Success(Context context) {
            ((Activity)context).finish();
        }
    }

    /**
     * 上传文件
     * @param context
     * @param file
     * @param fileName
     * @param isShare
     * @return
     */
    public static void UP_FILE(final Context context, final File file, String fileName, boolean isShare) {
        final Intent[] intent = new Intent[1];
        String url = "";
        String userName;
        final int current_progress[] = {0};
        if (App.Flag_IsLogin) {
            url = ServerInformation.UPLoadFile_AfterLogin;
            userName = User.getInstance().getName();
            System.out.println("UserName :" + userName);
            OkHttpUtils
                    .post()
                    .url(url)
                    .addFile("file", fileName, file)
                    .addParams("share", String.valueOf(isShare))
                    .addParams("name", userName)
                    .build()
                    .execute(new StringCallback() {
                                 @Override
                                 public void onError(Call call, Exception e, int id) {
                                     System.out.println("Error :" + e.toString());
                                     EasySweetAlertDialog.ShowTip(context, "tip", "上传失败", "OK", new MySuccessCalback());
                                 }

                                 @Override
                                 public void onResponse(String response, int id) {
                                     System.out.println("Login :" + response);
                                     try {
                                         JSONObject jsonObject = new JSONObject(response);
                                         if (jsonObject.getInt("code") == 0) {
                                             Intent intent_information = new Intent();
                                             intent_information.setAction(UploadUi.UploadSuccessWithFileInformation);
                                             intent_information.putExtra("name", file.getName());
                                             intent_information.putExtra("code", jsonObject.getString("identifyCode"));
                                             intent_information.putExtra("TYPE", FileUtils.getMIMEType(file));
                                             intent_information.putExtra("path", file.getPath());
                                             System.out.println("file.getName() : " + file.getName());
                                             System.out.println("jsonObject.getString(\"identifyCode\")" + jsonObject.getString("identifyCode"));
                                             String type = FileUtils.getMIMEType(file);
                                             LocalFile localFile = new LocalFile(file.getName(), file.getAbsolutePath(), System.currentTimeMillis(), type);
                                             App.Public_List_Local_File_Upload.add(localFile);

                                             ContentValues cv = new ContentValues();
                                             cv.put(LocalFileDB.COLUMN_NAME_Name, localFile.getName());
                                             cv.put(LocalFileDB.COLUMN_NAME_DownTime, System.currentTimeMillis());
                                             cv.put(LocalFileDB.COLUMN_NAME_Path, localFile.getPath());
                                             cv.put(LocalFileDB.COLUMN_NAME_Type, localFile.getType());
                                             App.dbWrite.insert(LocalFileDB.TABLE_NAME_LOCAL_FILE_UPLOAD, null, cv);
                                             context.sendBroadcast(intent_information);
                                         }else{
                                             Intent intent_finish_upload_activity = new Intent();
                                             intent_finish_upload_activity.setAction(UploadUi.FINISH_SIGNAL);
                                             context.sendBroadcast(intent_finish_upload_activity);
                                             EasySweetAlertDialog.ShowTip(context, "tip", "上传失败", "OK", new MySuccessCalback());
                                         }
                                     } catch (JSONException e) {
                                         e.printStackTrace();
                                         EasySweetAlertDialog.ShowTip(context, "tip", "上传失败", "OK", new MySuccessCalback());
                                     }
                                 }

                                 @Override
                                 public void inProgress(float progress, long total, int id) {
                                     super.inProgress(progress, total, id);
                                     int progress_ = (int) (progress * 100);
                                     if (current_progress[0] != progress_) {
                                         current_progress[0] = progress_;
                                         intent[0] = new Intent();
                                         intent[0].setAction(UploadUi.UploadProgressing);
                                         intent[0].putExtra(UploadUi.Progress, progress_);
                                         context.sendBroadcast(intent[0]);
                                     }
                                 }
                             }
                    );
        } else {
            url = ServerInformation.UPLoadFile;
            OkHttpUtils
                    .post()
                    .url(url)
                    .addFile("file", fileName, file)
                    .addParams("share", String.valueOf(isShare))
                    .build()
                    .execute(new StringCallback() {
                                 @Override
                                 public void onError(Call call, Exception e, int id) {
                                     System.out.println("Error :" + e.toString());
                                     EasySweetAlertDialog.ShowTip(context, "tip", "上传失败", "OK", new MySuccessCalback());
                                 }

                                 @Override
                                 public void onResponse(String response, int id) {
                                     System.out.println(response);
                                     try {
                                         JSONObject jsonObject = new JSONObject(response);
                                         Intent intent_information = new Intent();
                                         intent_information.setAction(UploadUi.UploadSuccessWithFileInformation);
                                         intent_information.putExtra("name", file.getName());
                                         intent_information.putExtra("code", jsonObject.getString("identifyCode"));
                                         intent_information.putExtra("TYPE", FileUtils.getMIMEType(file));
                                         intent_information.putExtra("path", file.getPath());
                                         System.out.println("file.getName() : " + file.getName());
                                         System.out.println("jsonObject.getString(\"identifyCode\")" + jsonObject.getString("identifyCode"));
                                         String type = FileUtils.getMIMEType(file);
                                         LocalFile localFile = new LocalFile(file.getName(), file.getAbsolutePath(), System.currentTimeMillis(), type);
                                         App.Public_List_Local_File_Upload.add(localFile);

                                         ContentValues cv = new ContentValues();
                                         cv.put(LocalFileDB.COLUMN_NAME_Name, localFile.getName());
                                         cv.put(LocalFileDB.COLUMN_NAME_DownTime, System.currentTimeMillis());
                                         cv.put(LocalFileDB.COLUMN_NAME_Path, localFile.getPath());
                                         cv.put(LocalFileDB.COLUMN_NAME_Type, localFile.getType());
                                         App.dbWrite.insert(LocalFileDB.TABLE_NAME_LOCAL_FILE_UPLOAD, null, cv);
                                         context.sendBroadcast(intent_information);
                                     } catch (JSONException e) {
                                         e.printStackTrace();
                                         EasySweetAlertDialog.ShowTip(context, "tip", "上传失败", "OK", new MySuccessCalback());
                                     }
                                 }

                                 @Override
                                 public void inProgress(float progress, long total, int id) {
                                     super.inProgress(progress, total, id);
                                     int progress_ = (int) (progress * 100);
                                     intent[0] = new Intent();
                                     intent[0].setAction(UploadUi.UploadProgressing);
                                     intent[0].putExtra(UploadUi.Progress, progress_);
                                     context.sendBroadcast(intent[0]);
                                 }
                             }
                    );
        }
    }


    /**
     * 注册
     *
     * @param context
     * @param username
     * @param password
     */
    public static void Register(final Context context, final String username, String password) {
        if (App.NeworkFlag == NetworkUtils.NETWORK_FLAG_NOT_CONNECT) {
            EasySweetAlertDialog.ShowTip(context, "tip", "请检查您的网络连接");
            return;
        }
        OkHttpUtils
                .post()
                .url(ServerInformation.REGISTER)
                .addParams("name", username)
                .addParams("pwd", password)
                .addParams("sex", "1")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        System.out.println(e.toString());
                        EasySweetAlertDialog.ShowTip(context, "Tip", "注册失败，请检查您的网络设置");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonObject = null;
                        System.out.println(response);
                        try {
                            jsonObject = new JSONObject(response);
                            switch (jsonObject.getInt("code")) {
                                case 0:
                                    User.getInstance().setName(username);
                                    User.getInstance().setAvatar(jsonObject.getString("avatar"));
                                    getUserInfo(context, username, 2);
                                    break;
                                case -6:
                                    EasySweetAlertDialog.ShowTip(context, "Tip", "用户名已存在！");
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    /**
     * 登陆
     *
     * @param context
     * @param username
     * @param password
     */
    public static void Login(final Context context, final String username, String password) {
        if (App.NeworkFlag == NetworkUtils.NETWORK_FLAG_NOT_CONNECT) {
            EasySweetAlertDialog.ShowTip(context, "tip", "请检查您的网络连接");
            return;
        }
        OkHttpUtils
                .post()
                .url(ServerInformation.LOGIN)
                .addParams("name", username)
                .addParams("pwd", password)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        System.out.println(e.toString());
                        EasySweetAlertDialog.ShowTip(context, "Tip", "登录失败，请检查您的网络设置");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            switch (jsonObject.getInt("code")) {
                                case 0:
                                    MobclickAgent.onProfileSignIn(username);
                                    getUserInfo(context, username, 1);
                                    break;
                                case -1:
                                    EasySweetAlertDialog.ShowTip(context, "Tip", "用户名或密码错误!");
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
