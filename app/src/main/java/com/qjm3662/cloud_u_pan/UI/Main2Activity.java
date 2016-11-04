package com.qjm3662.cloud_u_pan.UI;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Tool.NetworkUtils;

public class Main2Activity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        OuterSendData();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void OuterSendData() {
        Intent data = getIntent();
        if (data != null && data.getAction().equals("android.intent.action.SEND")) {
            System.out.println("****************************" + data.getClipData().getItemCount() + "**************************************");
            String p = String.valueOf(data.getClipData().getItemAt(data.getClipData().getItemCount() - 1).getUri());
            if(p.startsWith("file")){
                String temp = Uri.decode(p);
                p = temp.substring(8, temp.length());
                System.out.println(p);
                App.NeworkFlag = NetworkUtils.getNetworkFlag(this);
                Intent intent = new Intent(this, UploadUi.class);
                intent.putExtra(MainActivity.FILE_PATH_TO_BE_UPLOAD, p);
                this.startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
            }
        }else{
            System.out.println("*****************************data is null****************************");
            finish();
        }
    }

}
