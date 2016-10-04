package com.qjm3662.cloud_u_pan.Tool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.qjm3662.cloud_u_pan.Data.ServerInformation;

/**
 * Created by qjm3662 on 2016/10/1 0001.
 */

public class ShareOperator {

    /**
     * 判断是否安装腾讯、新浪等指定的分享应用
     * @param packageName 应用的包名
     */
    public boolean checkInstallation(Context context, String packageName){
        try {
            context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * Intent intent=new Intent(Intent.ACTION_SEND);
     intent.setType("text/plain");
     intent.setPackage("com.tencent.mobileqq");
     intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
     intent.putExtra(Intent.EXTRA_TEXT, "你好 ");
     intent.putExtra(Intent.EXTRA_TITLE, "我是标题");
     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
     startActivity(Intent.createChooser(intent, "请选择"));
     */

    public static void ShareTextToQQ(Context context, String fileName, String fileCode){
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("text/*");
        intent.setPackage("com.tencent.mobileqq");
        intent.putExtra(Intent.EXTRA_SUBJECT, fileName);
        intent.putExtra(Intent.EXTRA_TEXT, "★★★"+ "来自优云的分享" + "★★★\n" + "文件：" + fileName + "\n" + "下载链接：" + ServerInformation.DownLoadFile + fileCode);
        intent.putExtra(Intent.EXTRA_TITLE, fileName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, "请选择"));
    }

    public static void ShareTextToChat(Context context, String fileName, String fileCode){
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("text/*");
        intent.setPackage("com.tencent.mm");
        intent.putExtra(Intent.EXTRA_SUBJECT, fileName);
        intent.putExtra(Intent.EXTRA_TEXT, "★★★"+ "来自优云的分享" + "★★★\n" + "文件：" + fileName + "\n" + "下载链接：" + ServerInformation.DownLoadFile + fileCode);
        intent.putExtra(Intent.EXTRA_TITLE, fileName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, "请选择"));
    }

}
