package com.qjm3662.cloud_u_pan;

import com.qjm3662.cloud_u_pan.Data.FileInformation;
import com.qjm3662.cloud_u_pan.Data.ServerInformation;
import com.qjm3662.cloud_u_pan.Tool.FileUtils;
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




    public static String UP_FILE(File file, String fileName, boolean isShare){
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
                             }
                         }
                );
        return "asdfsa";
    }
}
