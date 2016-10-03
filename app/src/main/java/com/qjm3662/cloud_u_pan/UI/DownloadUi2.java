package com.qjm3662.cloud_u_pan.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.Data.ServerInformation;
import com.qjm3662.cloud_u_pan.Data.User;
import com.qjm3662.cloud_u_pan.NetWorkOperator;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Tool.AvatarUtils;
import com.qjm3662.cloud_u_pan.Tool.FileUtils;
import com.qjm3662.cloud_u_pan.Tool.TencentOperator;
import com.qjm3662.cloud_u_pan.Tool.TextUtil;
import com.qjm3662.cloud_u_pan.Widget.EasyButton;
import com.qjm3662.cloud_u_pan.Widget.EasySweetAlertDialog;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.wang.avi.AVLoadingIndicatorView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.request.RequestCall;

public class DownloadUi2 extends AppCompatActivity implements View.OnClickListener {

    private EasyButton btn_share_qq;
    private EasyButton btn_share_chat;
    private EasyButton btn_share_copy;
    private ImageView img_share_qq;
    private ImageView img_share_chat;
    private ImageView img_share_copy;
    private ImageView img_file;
    private TextView tv_fileName;
    private EasyButton btn_down;
    private TextView tv_code;
    private RoundedImageView img_avatar;
    private TextView tv_uploadInfo;
    private AVLoadingIndicatorView progress_circlr;
    private TextView tv_progress;

    private TextView tv_bar;
    private ImageView img_back;

    private String fileName;
    private String fileCode;
    private String filePath = null;
    private String uploadUser;
    private String uploadUserName;
    private String uploadUserAvatar;
    private String createAt;
    private RequestCall call;


    public static final String DownLoadProgressAction = "down load progress action";
    public static final String DownloadProgressing = "progressing";
    public static final String DownloadFilePathAction = "Download filePath action";
    public static final String DownloadfilePath = "Download filePath";
    private BroadcastReceiver receiver;
    private boolean is_upload_after_login = false;
    private Tencent tencent;
    private IUiListener iUiListener;
    private int current_progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_ui2);
        fileName = App.fileInformation.getName();
        fileCode = getIntent().getStringExtra("code");
        if(App.fileInformation.getUpLoadUser() != null){
            uploadUser = App.fileInformation.getUpLoadUser();
            uploadUserAvatar = App.fileInformation.getUpLoadUserAvatar();
            uploadUserName = App.fileInformation.getUpLoadUserName();
            createAt = App.fileInformation.getDownTimeString();
            is_upload_after_login = true;
        }
        initView();
        initReceiver();
        initTencent();
    }

    private void initTencent() {
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
    }

    private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownLoadProgressAction);
        intentFilter.addAction(DownloadFilePathAction);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println("receive");
                switch (intent.getAction()){
                    case DownLoadProgressAction:
                        int progress = intent.getIntExtra(DownloadProgressing, 0);
                        current_progress = progress;
                        System.out.println(progress);
                        tv_progress.setText(progress + "");
                        break;
                    case DownloadFilePathAction:
                        filePath = intent.getStringExtra(DownloadfilePath);
                        progress_circlr.hide();
                        tv_progress.setVisibility(View.INVISIBLE);
                        FileUtils.setImgHead(img_file, intent.getStringExtra("TYPE"), filePath);
                        btn_down.setText(" 打开 ");
                        btn_down.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };

        registerReceiver(receiver, intentFilter);
    }

    private void initView() {
        btn_share_qq = (EasyButton) findViewById(R.id.btn_share_qq);
        btn_share_copy = (EasyButton) findViewById(R.id.btn_share_copy);
        btn_share_chat = (EasyButton) findViewById(R.id.btn_share_chat);
        img_share_qq = (ImageView) findViewById(R.id.img_share_qq);
        img_share_chat = (ImageView) findViewById(R.id.img_share_chat);
        img_share_copy = (ImageView) findViewById(R.id.img_share_copy);
        img_file = (ImageView) findViewById(R.id.img_file);
        btn_down = (EasyButton) findViewById(R.id.btn_down);
        tv_fileName = (TextView) findViewById(R.id.tv_fileName);
        tv_code = (TextView) findViewById(R.id.tv_code);
        progress_circlr = (AVLoadingIndicatorView) findViewById(R.id.progress_circle);
        tv_progress = (TextView) findViewById(R.id.tv_progress);
        img_avatar = (RoundedImageView) findViewById(R.id.img_avatar);
        tv_uploadInfo = (TextView) findViewById(R.id.tv_uploadInfo);


        if(is_upload_after_login){
            tv_uploadInfo.setText(uploadUserName + "上传于" + createAt);
            img_avatar.setOnClickListener(this);
            final Bitmap[] b = {null};
            final Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what){
                        case 0:
                            img_avatar.setImageBitmap(b[0]);
                            break;
                    }
                }
            };
            AvatarUtils.AvatarCallBack callBack = new AvatarUtils.AvatarCallBack() {
                @Override
                public void callback(Bitmap bitmap) {
                    b[0] = bitmap;
                    handler.sendEmptyMessage(0);
                }

                @Override
                public void callBack_2(User u, Bitmap bitmap, int position) {

                }
            };
            AvatarUtils.getBitmapByUrl(App.fileInformation.getUpLoadUserAvatar(), callBack);
        }else{
            img_avatar.setVisibility(View.INVISIBLE);
            tv_uploadInfo.setVisibility(View.INVISIBLE);
        }
        tv_bar = (TextView) findViewById(R.id.bar);
        img_back = (ImageView) findViewById(R.id.img_back);
        tv_bar.setText("下载");
        img_back.setOnClickListener(this);

        progress_circlr.hide();
        tv_progress.setText("0");

        btn_share_qq.setOnClickListener(this);
        btn_share_copy.setOnClickListener(this);
        btn_share_chat.setOnClickListener(this);
        btn_down.setOnClickListener(this);
        img_file.setOnClickListener(this);

        tv_fileName.setText(fileName);
        tv_code.setText("提取码：" + fileCode);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_share_chat:

                break;
            case R.id.btn_share_qq:
                TencentOperator.shareToQQ(this, tencent, iUiListener, fileName, "lalala", ServerInformation.DownLoadFile_AfterLogin+fileCode, "http://img4.duitang.com/uploads/item/201404/03/20140403133744_AhmYW.thumb.700_0.jpeg", "优云");
                break;
            case R.id.btn_share_copy:
                TextUtil.copy(fileCode, this);
                EasySweetAlertDialog.ShowSuccess(this, "已成功复制到剪切板");
                break;
            case R.id.btn_down:
                if(btn_down.getText().toString().trim().equals("下载")){
                    progress_circlr.show();
                    btn_down.setVisibility(View.INVISIBLE);
                    call = OkHttpUtils
                            .get()
                            .url(ServerInformation.DownLoadFile + fileCode)
                            .build();
                    NetWorkOperator.Down(this, call, App.fileInformation);
                }else{
                    System.out.println("filePath : " + filePath);
                    FileUtils.OpenFile(this, filePath, fileName);
                }
                break;
            case R.id.img_file:
                break;
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.img_avatar:
                NetWorkOperator.getOtherUserInfoByName(this, uploadUser);
                System.out.println("UploadUser : " + uploadUser);
                break;
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        if(call != null){
            call.cancel();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_QQ_SHARE || requestCode == Constants.REQUEST_LOGIN) {
            if (resultCode == Constants.ACTIVITY_OK) {
                Tencent.handleResultData(data, iUiListener);
            }
        }
        tencent.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }




}
