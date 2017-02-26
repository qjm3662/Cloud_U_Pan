package com.qjm3662.cloud_u_pan.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.qjm3662.cloud_u_pan.Tool.FileUtils;

/**
 * Created by qjm3662 on 2017/2/26 0026.
 */

public class DownloadFileReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
//        System.out.println("receive");
//        switch (intent.getAction()){
//            case DownLoadProgressAction:
//                int progress = intent.getIntExtra(DownloadProgressing, 0);
//                tv_progress.setText(progress + "");
//                break;
//            case DownloadFilePathAction:
//                filePath = intent.getStringExtra(DownloadFilePath);
//                progress_circle.hide();
//                tv_progress.setVisibility(View.INVISIBLE);
//                FileUtils.setImgHead(img_file, intent.getStringExtra("TYPE"), filePath);
//                btn_down.setText(" 打开 ");
//                btn_down.setVisibility(View.VISIBLE);
//                break;
//        }
    }
}
