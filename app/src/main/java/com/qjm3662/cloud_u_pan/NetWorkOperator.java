package com.qjm3662.cloud_u_pan;

import android.content.Context;
import android.content.Intent;

import com.qjm3662.cloud_u_pan.Data.FileInformation;
import com.qjm3662.cloud_u_pan.Data.ServerInformation;
import com.qjm3662.cloud_u_pan.Tool.FileUtils;
import com.qjm3662.cloud_u_pan.UI.UploadUi;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.Call;

/**
 * Created by tanshunwang on 2016/9/21 0021.
 */
public class NetWorkOperator {


    public static String Down(final String id_, final FileInformation fileInformation){
        OkHttpUtils
                .get()
                .url(ServerInformation.DownLoadFile + id_)
                .build()
                .execute(new FileCallBack(FileUtils.getSDPath(), id_ + "") {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        System.out.println(e.toString());
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        System.out.println("response : " + response);
                        response.renameTo(new File(fileInformation.getName()));

                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                    }
                });
        return "  ";
    }

    public static String GetFileInformation(final String id_){
        OkHttpUtils
                .get()
                .url(ServerInformation.GetInformation + id_)
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
                            JSONObject jo = new JSONObject(response);
                            System.out.println(jo.get("code"));
                            FileInformation fileInformation = new FileInformation(jo.getString("code"), (float) jo.getDouble("size"));
                            Down(id_, fileInformation);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        return "  ";
    }




    public static String UP_FILE(final Context context, final File file, String fileName, boolean isShare){
        final Intent[] intent = new Intent[1];
        OkHttpUtils
                .post()
                .url(ServerInformation.UPLoadFile_AfterLogin)
                .addFile("file", fileName, file)
                .addParams("share", String.valueOf(isShare))
                .build()
                .execute(new StringCallback() {
                             @Override
                             public void onError(Call call, Exception e, int id) {
                                 System.out.println("Error :" + e.toString());
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
                                     System.out.println("file.getName() : " + file.getName());
                                     System.out.println("jsonObject.getString(\"identifyCode\")" + jsonObject.getString("identifyCode"));
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
        return "asdfsa";
    }
}
