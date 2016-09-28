package com.qjm3662.cloud_u_pan.UI;

import android.content.Intent;
import android.graphics.Path;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.qjm3662.cloud_u_pan.NetWorkOperator;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Tool.FileUtils;
import com.qjm3662.cloud_u_pan.Tool.GetPathFromUri4kitkat;
import com.qjm3662.cloud_u_pan.Widget.EasyButton;
import com.qjm3662.cloud_u_pan.Wifip2pDirect.EmptyActivity;
import com.qjm3662.cloud_u_pan.Wifip2pDirect.WIFI;
import com.qjm3662.cloud_u_pan.Wifip2pDirect.WifiDirect;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EasyButton btn_upload;
    private EasyButton btn_download;
    private EasyButton btn_bluetooth;
    private EasyButton btn_more;
    private EasyButton btn_my;
    private static final int FILE_SELECT_CODE = 1;

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

//        File file = new File();
//        file.getAbsolutePath()
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_upload:
                showFileChooser();
                break;
            case R.id.btn_download:
//                NetWorkOperator.GetFileInformation("516290");
                startActivity(new Intent(this, DownloadUi.class));
                break;
            case R.id.btn_bluetooth:
                startActivity(new Intent(this, EmptyActivity.class));
//                startActivity(new Intent(this, WifiDirect.class));

                break;
            case R.id.btn_more:
                startActivity(new Intent(this, LocalFileRecording_Download.class));
                break;
            case R.id.btn_my:

                break;
        }
    }


    /**
     * 打开文件选择器
     */
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "选择要分享的文件"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a FileInformation Manager.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    System.out.println(uri);
                    String path;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        path = GetPathFromUri4kitkat.getPath(this, uri);
                    } else {
                        path = FileUtils.getPath(this, uri);
                    }
                    System.out.println("path" + path);
                    File file = new File(path);

                    startActivity(new Intent(this, UploadUi.class));
                    NetWorkOperator.UP_FILE(this, file, file.getName(), true);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
