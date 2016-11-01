package com.qjm3662.cloud_u_pan.UI;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.FileManager.FileManager;
import com.qjm3662.cloud_u_pan.NetWorkOperator;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Tool.DialogUtils;
import com.qjm3662.cloud_u_pan.Tool.NetworkUtils;
import com.qjm3662.cloud_u_pan.Widget.EasyButton;
import com.qjm3662.cloud_u_pan.Widget.EasySweetAlertDialog;
import com.qjm3662.cloud_u_pan.WifiDirect.TransMain;

import java.io.File;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EasyButton btn_upload;
    private EasyButton btn_download;
    private EasyButton btn_bluetooth;
    private EasyButton btn_more;
    private EasyButton btn_my;
    public static final int FILE_SELECT_CODE = 1;
    public static final String FILE_SELECT = "file select";
    public static final int requestCode_selectFile = 6;
    public static final int resultCode = 9;
    public static final String PATH = "path";

    private Dialog dialog = null;
    private static final int REQUEST_CODE_GALLERY = 253;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFirstLunch();
        initView();
    }
    public void isFirstLunch() {
        SharedPreferences sp = this.getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE);
        boolean b = sp.getBoolean("IS_FIRST", true);
        if(b){
            this.startActivity(new Intent(this, FirstLunchActivity.class));
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("IS_FIRST", false);
            editor.apply();
            setContentView(R.layout.activity_main);
        }else{
            setContentView(R.layout.activity_main);
        }
    }

    private void initView() {
        btn_upload = (EasyButton) findViewById(R.id.btn_upload);
        btn_download = (EasyButton) findViewById(R.id.btn_download);
        btn_bluetooth = (EasyButton) findViewById(R.id.btn_bluetooth);
        btn_more = (EasyButton) findViewById(R.id.btn_more);
        btn_my = (EasyButton) findViewById(R.id.btn_my);
        findViewById(R.id.img_back).setVisibility(View.INVISIBLE);

        btn_upload.setOnClickListener(this);
        btn_download.setOnClickListener(this);
        btn_bluetooth.setOnClickListener(this);
        btn_more.setOnClickListener(this);
        btn_my.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_upload:
                if (App.NeworkFlag == NetworkUtils.NETWORK_FLAG_NOT_CONNECT) {
                    EasySweetAlertDialog.ShowTip(this, "tip", "请检查您的网络连接");
                    return;
                } else {
                    Intent intent = new Intent(this, FileManager.class);
                    intent.putExtra(FILE_SELECT, FILE_SELECT_CODE);
                    startActivityForResult(intent, requestCode_selectFile);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
                break;
            case R.id.btn_download:
                startActivity(new Intent(this, DownloadUi.class));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.btn_bluetooth:
                startActivity(new Intent(this, TransMain.class));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.btn_more:
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.btn_cancel:
                                dialog.cancel();
                                System.out.println("dialog cancel!!");
                                break;
                            case R.id.img_upload_record:
                                startActivity(new Intent(MainActivity.this, LocalFileRecording_Upload.class));
                                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                dialog.cancel();
                                break;
                            case R.id.img_download_record:
                                startActivity(new Intent(MainActivity.this, LocalFileRecording_Download.class));
                                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                dialog.cancel();
                                break;
                            case R.id.img_share_center:
                                startActivity(new Intent(MainActivity.this, ShareCenter.class));
                                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                dialog.cancel();
                                NetWorkOperator.getShareCenter(MainActivity.this);
                                break;
                        }
                    }
                };
                dialog = new Dialog(this, R.style.common_dialog);
                DialogUtils.showSelectDialog(this, dialog, listener);
                break;
            case R.id.btn_my:
                startActivity(new Intent(this, UserMain.class));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case requestCode_selectFile:
                if (data != null) {
                    System.out.println(data.getStringExtra(PATH));
                    File file = new File(data.getStringExtra(PATH));
                    this.startActivity(new Intent(this, UploadUi.class));
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    NetWorkOperator.UP_FILE(this, file, file.getName(), true);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

}
