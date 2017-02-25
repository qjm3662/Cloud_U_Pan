package com.qjm3662.cloud_u_pan.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.easybar.EasyBar;
import com.example.easybar.ImageCircleButton;
import com.example.easybar.OnImageCircleButtonClickedListener;
import com.example.easybar.RoundRectButton;
import com.makeramen.roundedimageview.RoundedImageView;
import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.Data.ServerInformation;
import com.qjm3662.cloud_u_pan.Data.User;
import com.qjm3662.cloud_u_pan.Data.UserBase;
import com.qjm3662.cloud_u_pan.EasyBarUtils;
import com.qjm3662.cloud_u_pan.NetWorkOperator;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Tool.AvatarUtils;
import com.qjm3662.cloud_u_pan.Tool.FileUtils;
import com.qjm3662.cloud_u_pan.Tool.NetworkUtils;
import com.qjm3662.cloud_u_pan.Tool.ShareOperator;
import com.qjm3662.cloud_u_pan.Tool.TextUtil;
import com.qjm3662.cloud_u_pan.Widget.EasyButton;
import com.qjm3662.cloud_u_pan.Widget.EasySweetAlertDialog;
import com.wang.avi.AVLoadingIndicatorView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.request.RequestCall;

public class DownloadUi2 extends BaseActivity implements OnImageCircleButtonClickedListener {

    private ImageCircleButton btn_share_qq;
    private ImageCircleButton btn_share_chat;
    private ImageCircleButton btn_share_copy;
    private ImageView img_file;
    private TextView tv_fileName;
    private RoundRectButton btn_down;
    private TextView tv_code;
    private RoundedImageView img_avatar;
    private TextView tv_uploadInfo;
    private AVLoadingIndicatorView progress_circle;
    private TextView tv_progress;

    private EasyBar easyBar;

    private String fileName;
    private String fileCode;
    private String filePath = null;
    private UserBase uploader;
    private String createAt;
    private RequestCall call;


    public static final String DownLoadProgressAction = "down load progress action";
    public static final String DownloadProgressing = "progressing";
    public static final String DownloadFilePathAction = "Download filePath action";
    public static final String DownloadFilePath = "Download filePath";
    private BroadcastReceiver receiver;
    private boolean is_upload_after_login = false;
    private int current_progress = 0;
    private int where = 0;  // 0->app主页获取下载信息   1->用户个人主页访问，不可再重复访问

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_ui2);
        fileName = App.fileInformation.getFileName();
        Intent intent = getIntent();
        fileCode = intent.getStringExtra("code");
        where = intent.getIntExtra("WHERE", 0);
        if(App.fileInformation.getUploadUser() != null){
            uploader = App.fileInformation.getUploadUser();
            createAt = App.fileInformation.getDownTimeString();
            is_upload_after_login = true;
        }
        initView();
        initReceiver();
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
//                        System.out.println(progress);
                        tv_progress.setText(progress + "");
                        break;
                    case DownloadFilePathAction:
                        filePath = intent.getStringExtra(DownloadFilePath);
                        progress_circle.hide();
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
        btn_share_qq = (ImageCircleButton) findViewById(R.id.btn_share_qq);
        btn_share_copy = (ImageCircleButton) findViewById(R.id.btn_share_copy);
        btn_share_chat = (ImageCircleButton) findViewById(R.id.btn_share_chat);

        img_file = (ImageView) findViewById(R.id.img_file);
        btn_down = (RoundRectButton) findViewById(R.id.btn_down);

        tv_fileName = (TextView) findViewById(R.id.tv_fileName);
        tv_code = (TextView) findViewById(R.id.tv_code);
        progress_circle = (AVLoadingIndicatorView) findViewById(R.id.progress_circle);
        tv_progress = (TextView) findViewById(R.id.tv_progress);
        img_avatar = (RoundedImageView) findViewById(R.id.img_avatar);
        tv_uploadInfo = (TextView) findViewById(R.id.tv_uploadInfo);


        if(is_upload_after_login){
            tv_uploadInfo.setText(uploader.getNickname() + "上传于" + createAt);
            img_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(where == 0){
                        NetWorkOperator.getOtherUserInfoByUsername(DownloadUi2.this, uploader.getUsername(), true);
                    }else{
                        EasySweetAlertDialog.ShowNormal(DownloadUi2.this, "厉害了我滴哥", "退一步海阔天空~");
                    }
                }
            });
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
            AvatarUtils.getBitmapByUrl(uploader.getAvatar(), callBack);
        }else{
            img_avatar.setVisibility(View.INVISIBLE);
            tv_uploadInfo.setVisibility(View.INVISIBLE);
        }
        EasyBarUtils.justSetTitleAndBack("下载", this, 1);

        progress_circle.hide();
        tv_progress.setText("0");

        btn_share_qq.setOnImageCircleButtonClickedListener(this);
        btn_share_copy.setOnImageCircleButtonClickedListener(this);
        btn_share_chat.setOnImageCircleButtonClickedListener(this);
        btn_down.setOnImageCircleButtonClickedListener(this);

        tv_fileName.setText(fileName);
        tv_code.setText("提取码：" + fileCode);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_share_chat:
                ShareOperator.ShareTextToChat(this, fileName, fileCode);
                break;
            case R.id.btn_share_qq:
                System.out.println("share");
                ShareOperator.ShareTextToQQ(this, fileName, fileCode);
                break;
            case R.id.btn_share_copy:
                TextUtil.copy(fileCode, this);
                EasySweetAlertDialog.ShowSuccess(this, "已成功复制到剪切板");
                break;
            case R.id.btn_down:
                if (App.NeworkFlag == NetworkUtils.NETWORK_FLAG_NOT_CONNECT) {
                    EasySweetAlertDialog.ShowTip(this, "tip", "请检查您的网络连接");
                    return;
                } else if (App.NeworkFlag == NetworkUtils.NETWORK_FLAG_MOBILE && App.Down_In_Wifi_Switch_State) {
                    EasySweetAlertDialog.ShowTip(this, "tip", "已开启wifi下下载");
                    return;
                }
                if(btn_down.getText().toString().trim().equals("下载")){
                    progress_circle.show();
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
}
