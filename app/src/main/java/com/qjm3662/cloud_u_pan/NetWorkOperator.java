package com.qjm3662.cloud_u_pan;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;

import com.google.gson.Gson;
import com.qjm3662.cloud_u_pan.Data.FileInformation;
import com.qjm3662.cloud_u_pan.Data.LocalFile;
import com.qjm3662.cloud_u_pan.Data.LocalFileDB;
import com.qjm3662.cloud_u_pan.Data.ServerInformation;
import com.qjm3662.cloud_u_pan.Data.User;
import com.qjm3662.cloud_u_pan.Tool.FileUtils;
import com.qjm3662.cloud_u_pan.Tool.NetworkUtils;
import com.qjm3662.cloud_u_pan.UI.DownloadUi2;
import com.qjm3662.cloud_u_pan.UI.ShareCenter;
import com.qjm3662.cloud_u_pan.UI.UploadUi;
import com.qjm3662.cloud_u_pan.UI.UserMain;
import com.qjm3662.cloud_u_pan.Widget.EasySweetAlertDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.request.RequestCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.OkHttpClient;

/**
 * Created by tanshunwang on 2016/9/21 0021.
 */
public class NetWorkOperator {


    /**
     * 获取分享中心
     * @param context
     */
    public static void getShareCenter(final Context context){
        if(App.NeworkFlag == NetworkUtils.NETWORK_FLAG_NOT_CONNECT){
            EasySweetAlertDialog.ShowTip(context, "tip", "请检查您的网络连接");
            return;
        }
        OkHttpUtils
                .get()
                .url(ServerInformation.Get_Share_center)
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
                                Gson gson = new Gson();
                                App.Public_List_File_Info.clear();
                                JSONArray jsonArray = jsonObject.getJSONArray("shares");
                                FileInformation fileInformation = null;
                                for(int i = 0; i < jsonArray.length(); i++) {
                                    fileInformation = gson.fromJson(jsonArray.get(i).toString(), FileInformation.class);
//                                    System.out.println(fileInformation);
                                    String type = FileUtils.getMIMEType(new File(fileInformation.getName()));
                                    fileInformation.setBitmap_type(FileUtils.getImgHead_not_down(context, type));
                                    App.Public_List_File_Info.add(fileInformation);
                                    Intent intent = new Intent();
                                    intent.setAction(ShareCenter.ACTION);
                                    context.sendBroadcast(intent);
                                }
                            }
                        } catch (JSONException e) {
                            System.out.println("json 错误");
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 获取用户信息
     * @param context
     * @param username
     */
    public static void getUserInfo(final Context context, String username){
        if(App.NeworkFlag == NetworkUtils.NETWORK_FLAG_NOT_CONNECT){
            EasySweetAlertDialog.ShowTip(context, "tip", "请检查您的网络连接");
            return;
        }
        OkHttpUtils
                .get()
                .url(ServerInformation.GetUserInfo)
                .addParams("name", username)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        System.out.println(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            final JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getInt("code") == 0){
                                App.Flag_IsLogin = true;
                                Gson gson = new Gson();
                                User.setUser(gson.fromJson(jsonObject.toString(), User.class));
                                System.out.println(User.getInstance().toString());
                                JSONArray jsonArray = jsonObject.getJSONArray("shares");
                                final List<FileInformation> shares = new ArrayList<FileInformation>();
                                for(int i = 0; i < jsonArray.length(); i++){
                                    shares.add(gson.fromJson(jsonArray.get(i).toString(), FileInformation.class));
                                }
                                context.startActivity(new Intent(context, UserMain.class));
                                ((Activity)context).finish();
                                User.getInstance().setShares_list(shares);
                                final Bitmap[] bitmap = {null};
                                final android.os.Handler handler = new android.os.Handler(){
                                    @Override
                                    public void handleMessage(Message msg) {
                                        super.handleMessage(msg);
                                        switch (msg.what){
                                            case 0:
                                                User.getInstance().setBitmap(bitmap[0]);
                                                Intent intent = new Intent();
                                                intent.setAction(UserMain.ACTION_GET_USER_INFO_SUCCESS);
                                                context.sendBroadcast(intent);
                                                break;
                                        }
                                    }
                                };
                                Thread thread = new Thread(){
                                    @Override
                                    public void run() {
                                        try {
                                            bitmap[0] = returnBitmap(jsonObject.getString("avatar"));
                                            handler.sendEmptyMessage(0);
                                            File file = new File(FileUtils.getPath(), "header.jpg");
                                            User.getInstance().setAvatarPath(file.getAbsolutePath());
                                            BufferedOutputStream bos = new BufferedOutputStream(
                                                    new FileOutputStream(file));
                                            bitmap[0].compress(Bitmap.CompressFormat.JPEG, 80, bos);
                                            bos.flush();
                                            bos.close();

                                            SharedPreferences sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sp.edit();
                                            editor.putString("UserJson", jsonObject.toString());
                                            editor.putString("AvatarPath", file.getAbsolutePath());
                                            editor.apply();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        super.run();
                                    }
                                };
                                thread.start();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }



    /**
     * 根据图片的url路径获得Bitmap对象
     *
     * @param url
     * @return
     */
    public static Bitmap returnBitmap(String url) {
        System.out.println("begin getBitmap");
        URL fileUrl = null;
        Bitmap bitmap = null;

        try {
            fileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        try {
            assert fileUrl != null;
            HttpURLConnection conn = (HttpURLConnection) fileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            return null;
        }
        System.out.println("return Bitmap end in!!!!");
        return bitmap;
    }

    public static String Down(Context context, final String id_, final FileInformation fileInformation) {
        if(App.NeworkFlag == NetworkUtils.NETWORK_FLAG_NOT_CONNECT){
            EasySweetAlertDialog.ShowTip(context, "tip", "请检查您的网络连接");
            return "";
        }
        OkHttpUtils
                .get()
                .url(ServerInformation.DownLoadFile_AfterLogin + id_)
                .build()
                .execute(new FileCallBack(FileUtils.getSDPath(), fileInformation.getName()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        System.out.println(e.toString());
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        System.out.println("response : " + response);
                        System.out.println(response.getAbsolutePath());

                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        System.out.println(progress);
                    }
                });
        return "  ";
    }

    /**
     * 下载文件
     * @param context
     * @param call
     * @param fileInformation
     * @return
     */
    public static String Down(final Context context, RequestCall call, FileInformation fileInformation) {
        if(App.NeworkFlag == NetworkUtils.NETWORK_FLAG_NOT_CONNECT){
            EasySweetAlertDialog.ShowTip(context, "tip", "请检查您的网络连接");
            return "";
        }
        final Intent[] intent = {new Intent(), new Intent()};
        call.execute(new FileCallBack(FileUtils.getSDPath(), fileInformation.getName()) {
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
                        LocalFile localFile = new LocalFile(response.getName(), response.getAbsolutePath(), System.currentTimeMillis(), type, FileUtils.getImgHead(context, type, response.getAbsolutePath()));
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
                        intent[0] = new Intent();
                        intent[0].setAction(DownloadUi2.DownLoadProgressAction);
                        intent[0].putExtra(DownloadUi2.DownloadProgressing, (int)(progress * 100));
//                        System.out.println((int)(progress * 100));
                        context.sendBroadcast(intent[0]);
                    }
                });
        return "  ";
    }

    /**
     * 下载文件之前获取文件的信息
     * @param id_
     * @param fileInformation
     * @param callBack
     * @return
     */
    public static String GetFileInformation(final Context context, final String id_, final FileInformation fileInformation, final FileInformation.callBack callBack) {
        if(App.NeworkFlag == NetworkUtils.NETWORK_FLAG_NOT_CONNECT){
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
                            if(!jo.getString("uploadUser").equals("nobody")){
                                fileInformation.setUpLoadUser(jo.getString("uploadUser"));
                                fileInformation.setUpLoadUserAvatar(jo.getJSONObject("user").getString("avatar"));
                                fileInformation.setUpLoadUserName(jo.getJSONObject("user").getString("username"));
                                fileInformation.setCreatedAt(jo.getLong("time"));
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
     * 上传文件
     * @param context
     * @param file
     * @param fileName
     * @param isShare
     * @return
     */
    public static String UP_FILE(final Context context, final File file, String fileName, boolean isShare) {
        final Intent[] intent = new Intent[1];
        if(App.NeworkFlag == NetworkUtils.NETWORK_FLAG_NOT_CONNECT){
            EasySweetAlertDialog.ShowTip(context, "tip", "请检查您的网络连接");
            return "";
        }
        String url = "";
        String userName;
        if(App.Flag_IsLogin){
            url =  ServerInformation.UPLoadFile_AfterLogin;
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
                                     EasySweetAlertDialog.ShowTip(context, "tip", "上传失败");
                                 }

                                 @Override
                                 public void onResponse(String response, int id) {
                                     System.out.println("Login :" +response);
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
                                         LocalFile localFile = new LocalFile(file.getName(), file.getAbsolutePath(), System.currentTimeMillis(), type, FileUtils.getImgHead(context, type, file.getAbsolutePath()));
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
        }else{
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
                                     EasySweetAlertDialog.ShowTip(context, "tip", "上传失败");
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
                                         LocalFile localFile = new LocalFile(file.getName(), file.getAbsolutePath(), System.currentTimeMillis(), type, FileUtils.getImgHead(context, type, file.getAbsolutePath()));
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
        return "asdfsa";
    }


    /**
     * 注册
     * @param context
     * @param username
     * @param password
     */
    public static void Register(final Context context, final String username, String password){
        if(App.NeworkFlag == NetworkUtils.NETWORK_FLAG_NOT_CONNECT){
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
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonObject = null;
                        System.out.println(response);
                        try {
                            jsonObject = new JSONObject(response);
                            if(jsonObject.getInt("code") == 0){
                                User.getInstance().setName(username);
                                User.getInstance().setAvatar(jsonObject.getString("avatar"));
                                getUserInfo(context, username);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    /**
     * 登陆
     * @param context
     * @param username
     * @param password
     */
    public static void Login(final Context context, final String username, String password){
        if(App.NeworkFlag == NetworkUtils.NETWORK_FLAG_NOT_CONNECT){
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
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            if(jsonObject.getInt("code") == 0){
                                getUserInfo(context, username);
//                                System.out.println("code 0");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
