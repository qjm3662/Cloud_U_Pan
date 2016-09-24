package com.qjm3662.cloud_u_pan.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Widget.EasyButton;

public class UploadUi extends AppCompatActivity implements View.OnClickListener {

    public static final String UploadProgressing = "upload_progressing";
    public static final String UploadSuccessWithFileInformation = "upload success with information";
    public static final String Progress = "progress";
    private AnimatedCircleLoadingView animatedCircleLoadingView;
    private BroadcastReceiver receiver;

    private String fileName = null;
    private String fileCode = null;

    private EasyButton btn_share_qq;
    private EasyButton btn_share_chat;
    private EasyButton btn_share_copy;
    private ImageView img_share_qq;
    private ImageView img_share_chat;
    private ImageView img_share_copy;
    private ImageView img_file;
    private TextView tv_fileName;
    private TextView tv_success;
    private TextView tv_code;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_ui);
        initIntentFilterAndReceiver();
        initView();
    }

    private void initView() {
        btn_share_qq = (EasyButton) findViewById(R.id.btn_share_qq);
        btn_share_copy = (EasyButton) findViewById(R.id.btn_share_copy);
        btn_share_chat = (EasyButton) findViewById(R.id.btn_share_chat);
        img_share_qq = (ImageView) findViewById(R.id.img_share_qq);
        img_share_chat = (ImageView) findViewById(R.id.img_share_chat);
        img_share_copy = (ImageView) findViewById(R.id.img_share_copy);
        img_file = (ImageView) findViewById(R.id.img_file);
        tv_success = (TextView) findViewById(R.id.tv_success);
        tv_fileName = (TextView) findViewById(R.id.tv_fileName);
        tv_code = (TextView) findViewById(R.id.tv_code);

        btn_share_qq.setOnClickListener(this);
        btn_share_copy.setOnClickListener(this);
        btn_share_chat.setOnClickListener(this);

        btn_share_qq.setVisibility(View.INVISIBLE);
        btn_share_copy.setVisibility(View.INVISIBLE);
        btn_share_chat.setVisibility(View.INVISIBLE);
        img_share_qq.setVisibility(View.INVISIBLE);
        img_share_chat.setVisibility(View.INVISIBLE);
        img_share_copy.setVisibility(View.INVISIBLE);
        img_file.setVisibility(View.INVISIBLE);
        tv_success.setVisibility(View.INVISIBLE);
        tv_fileName.setVisibility(View.INVISIBLE);
        tv_code.setVisibility(View.INVISIBLE);


        animatedCircleLoadingView = (AnimatedCircleLoadingView) findViewById(R.id.circle_loading_view);
        startLoading();
        animatedCircleLoadingView.setAnimationListener(new AnimatedCircleLoadingView.AnimationListener() {
            @Override
            public void onAnimationEnd(boolean success) {
                animatedCircleLoadingView.setVisibility(View.INVISIBLE);
                btn_share_qq.setVisibility(View.VISIBLE);
                btn_share_copy.setVisibility(View.VISIBLE);
                btn_share_chat.setVisibility(View.VISIBLE);
                img_share_chat.setVisibility(View.VISIBLE);
                img_share_copy.setVisibility(View.VISIBLE);
                img_share_qq.setVisibility(View.VISIBLE);
                img_file.setVisibility(View.VISIBLE);
                tv_success.setVisibility(View.VISIBLE);
                tv_fileName.setVisibility(View.VISIBLE);
                tv_code.setVisibility(View.VISIBLE);
                tv_fileName.setText(fileName);
                tv_code.setText("提取码：" + fileCode);
            }
        });
    }

    private void initIntentFilterAndReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UploadProgressing);
        intentFilter.addAction(UploadSuccessWithFileInformation);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()){
                    case UploadProgressing:
                        int progress = intent.getIntExtra(Progress, 0);
                        changePercent(progress);
                        break;
                    case UploadSuccessWithFileInformation:
                        fileName = intent.getStringExtra("name");
                        fileCode = intent.getStringExtra("code");
                        break;
                }
            }
        };

        registerReceiver(receiver, intentFilter);
    }


    private void startLoading() {
        animatedCircleLoadingView.startDeterminate();
    }

    public void changePercent(final int percent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                animatedCircleLoadingView.setPercent(percent);
            }
        });
    }

    public void resetLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                animatedCircleLoadingView.resetLoading();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_share_qq:

                break;
            case R.id.btn_share_chat:

                break;
            case R.id.btn_share_copy:

                break;
        }
    }
}
