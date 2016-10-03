package com.qjm3662.cloud_u_pan.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.Data.ServerInformation;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Tool.FileUtils;
import com.qjm3662.cloud_u_pan.Tool.TencentOperator;
import com.qjm3662.cloud_u_pan.Tool.TextUtil;
import com.qjm3662.cloud_u_pan.Widget.EasyButton;
import com.qjm3662.cloud_u_pan.Widget.EasySweetAlertDialog;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

public class UploadUi extends AppCompatActivity implements View.OnClickListener {

    //
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
    private Handler handler;
    private ImageView img_back;
    private TextView tv_bar;
    private boolean flag = false;               //标记动画完成，和页面数据填充都完成时显示上传成功页面

    private Tencent tencent;
    private IUiListener iUiListener;


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
        img_back = (ImageView) findViewById(R.id.img_back);

        btn_share_qq.setOnClickListener(this);
        btn_share_copy.setOnClickListener(this);
        btn_share_chat.setOnClickListener(this);
        img_back.setOnClickListener(this);

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


        tv_bar = (TextView) findViewById(R.id.bar);
        tv_bar.setText("上传");

        tencent = Tencent.createInstance(App.App_ID, this);
        iUiListener = new IUiListener() {
            @Override
            public void onComplete(Object o) {
                System.out.println("成功");
            }

            @Override
            public void onError(UiError uiError) {
                System.out.println("错误");
            }

            @Override
            public void onCancel() {
                System.out.println("取消");
            }
        };

        animatedCircleLoadingView = (AnimatedCircleLoadingView) findViewById(R.id.circle_loading_view);
        startLoading();
        animatedCircleLoadingView.setAnimationListener(new AnimatedCircleLoadingView.AnimationListener() {
            @Override
            public void onAnimationEnd(boolean success) {
                animatedCircleLoadingView.setVisibility(View.INVISIBLE);
            }
        });


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
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
                        break;
                }
            }
        };
    }

    private void initIntentFilterAndReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UploadProgressing);
        intentFilter.addAction(UploadSuccessWithFileInformation);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case UploadProgressing:
                        int progress = intent.getIntExtra(Progress, 0);
                        changePercent(progress);
                        break;
                    case UploadSuccessWithFileInformation:
                        fileName = intent.getStringExtra("name");
                        fileCode = intent.getStringExtra("code");
                        FileUtils.setImgHead(img_file, intent.getStringExtra("TYPE"), intent.getStringExtra("path"));
                        handler.sendEmptyMessage(0);
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_share_qq:
                TencentOperator.shareToQQ(this, tencent, iUiListener, fileName, "lalala", ServerInformation.DownLoadFile_AfterLogin+fileCode, "http://img4.duitang.com/uploads/item/201404/03/20140403133744_AhmYW.thumb.700_0.jpeg", "优云");
                break;
            case R.id.btn_share_chat:
                break;
            case R.id.btn_share_copy:
                TextUtil.copy(fileCode, this);
                EasySweetAlertDialog.ShowSuccess(this, "已成功复制到剪切板");
                break;
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_QQ_SHARE || requestCode == Constants.REQUEST_LOGIN) {
            if (resultCode == Constants.ACTIVITY_OK) {
                Tencent.handleResultData(data, iUiListener);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
