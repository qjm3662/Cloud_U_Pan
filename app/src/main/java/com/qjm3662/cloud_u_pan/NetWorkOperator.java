package com.qjm3662.cloud_u_pan;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import com.qjm3662.cloud_u_pan.Data.FileInformation;
import com.qjm3662.cloud_u_pan.Data.LocalFile;
import com.qjm3662.cloud_u_pan.Data.LocalFileDB;
import com.qjm3662.cloud_u_pan.Data.ServerInformation;
import com.qjm3662.cloud_u_pan.Tool.FileUtils;
import com.qjm3662.cloud_u_pan.UI.DownloadUi2;
import com.qjm3662.cloud_u_pan.UI.UploadUi;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.Call;

/**
 * Created by tanshunwang on 2016/9/21 0021.
 */
public class NetWorkOperator {


    public static String Down(final String id_, final FileInformation fileInformation) {
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

    public static String Down(final Context context, RequestCall call, FileInformation fileInformation) {
        final Intent[] intent = {new Intent(), new Intent()};
        call.execute(new FileCallBack(FileUtils.getSDPath(), fileInformation.getName()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        System.out.println(e.toString());
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        System.out.println("response : " + response);
                        System.out.println(response.getAbsolutePath());
                        intent[1].setAction(DownloadUi2.DownloadFilePathAction);
                        intent[1].putExtra(DownloadUi2.DownloadfilePath, response.toString());

                        LocalFile localFile = new LocalFile(response.getName(), response.getAbsolutePath(), System.currentTimeMillis(), FileUtils.getMIMEType(response));
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

    public static String GetFileInformation(final String id_, final FileInformation fileInformation, final FileInformation.callBack callBack) {
        OkHttpUtils
                .get()
                .url(ServerInformation.GetInformation_AfterLogin + id_)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        System.out.println(e.toString());
                        System.out.println("id :" + id_);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        System.out.println(response);
                        try {
                            JSONObject jo = new JSONObject(response);
                            System.out.println(jo.get("code"));
                            fileInformation.setName(jo.getString("name"));
                            fileInformation.setSize((float) jo.getDouble("size"));
                            callBack.call();
//                            Down(id_, fileInformation);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        return "  ";
    }


    public static String UP_FILE(final Context context, final File file, String fileName, boolean isShare) {
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
                                     LocalFile localFile = new LocalFile(file.getName(), file.getAbsolutePath(), System.currentTimeMillis(), FileUtils.getMIMEType(file));
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
        return "asdfsa";
    }
}
