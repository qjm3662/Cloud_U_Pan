package com.qjm3662.cloud_u_pan.UI;

import android.app.Dialog;
import android.content.Intent;
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
import com.tencent.tauth.Tencent;

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
    private Tencent mTencent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
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
                Intent intent = new Intent(this, FileManager.class);
                intent.putExtra(FILE_SELECT, FILE_SELECT_CODE);
                startActivityForResult(intent, requestCode_selectFile);
                break;
            case R.id.btn_download:
                startActivity(new Intent(this, DownloadUi.class));
                break;
            case R.id.btn_bluetooth:
//                NetWorkOperator.Login(this, "123456", "123456");
                break;
            case R.id.btn_more:
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()){
                            case R.id.btn_cancel:
                                dialog.cancel();
                                System.out.println("dialog cancel!!");
                                break;
                            case R.id.img_upload_record:
                                startActivity(new Intent(MainActivity.this, LocalFileRecording_Upload.class));
                                dialog.cancel();
                                break;
                            case R.id.img_download_record:
                                startActivity(new Intent(MainActivity.this, LocalFileRecording_Download.class));
                                dialog.cancel();
                                break;
                            case R.id.img_share_center:
                                startActivity(new Intent(MainActivity.this, ShareCenter.class));
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
                if(App.Flag_IsLogin){
                    startActivity(new Intent(this, UserMain.class));
                }else{
                    startActivity(new Intent(this, Login.class));
                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case requestCode_selectFile:
                if(data != null){
                    System.out.println(data.getStringExtra(PATH));
                    File file = new File(data.getStringExtra(PATH));
                    NetWorkOperator.UP_FILE(this, file, file.getName(), true);
                    if(!(App.NeworkFlag == NetworkUtils.NETWORK_FLAG_NOT_CONNECT)){
                        startActivity(new Intent(this, UploadUi.class));
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
