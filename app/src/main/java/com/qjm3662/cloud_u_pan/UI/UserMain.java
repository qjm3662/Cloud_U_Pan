package com.qjm3662.cloud_u_pan.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.makeramen.roundedimageview.RoundedImageView;
import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.Data.User;
import com.qjm3662.cloud_u_pan.FileManager.FileManager;
import com.qjm3662.cloud_u_pan.NetWorkOperator;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Tool.DialogUtils;
import com.qjm3662.cloud_u_pan.Tool.NetworkUtils;
import com.qjm3662.cloud_u_pan.Widget.EasySweetAlertDialog;

import java.io.File;

public class UserMain extends BaseActivity implements View.OnClickListener {

    private ImageView img_edit_nickname;
    private RoundedImageView img_head;
    private TextView tv_name;
    private ViewGroup tv_following;
    private ViewGroup tv_upload;
    private ViewGroup tv_down_in_wifi;
    private ViewGroup tv_save_path;
    private ViewGroup tv_callback;
    private TextView tv_bar;
    private ImageView img_back;
    private ImageView img_zXing;
    private TextView tv_exit;
    private TextView tv_current_save_path;
    private ViewGroup tv_version;
    private SwitchButton btn_switch;
    private ViewGroup tv_about_us;
    private ViewGroup tv_revisePsd;
    private ViewGroup ll_header;
    private BroadcastReceiver receiver;
    public static final String ACTION_GET_USER_INFO_SUCCESS = "get userInfo success";
    public static final String ACTION_UPDATE_USERINFO = "update userInfo";
    public static final String FINISH_SIGNAL = "finish current activity";
    private User user;

    public static final int PATH_REQUEST = 1;

    public static final String PATH = "path";
    public static final int SELECT_PHOTO_RESULT_CODE = 2;
    private static final int REQUEST_CODE_GALLERY = 253;
    public static final int RESULT_CODE_MY_DCIM = 222;
    public static final int REQUEST_CODE_MY_DCIM = 212;
    private Object permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        initView();
        initReceiver();
    }

    private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_GET_USER_INFO_SUCCESS);
        intentFilter.addAction(FINISH_SIGNAL);
        if (!App.FLAG_IS_DATA_FINISH) {
            intentFilter.addAction(ACTION_UPDATE_USERINFO);
        }
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case ACTION_GET_USER_INFO_SUCCESS:
                        user = User.getInstance();
                        System.out.println(user.getBitmap());
                        img_head.setImageBitmap(user.getBitmap());
                        tv_name.setText(user.getUsername());
                        break;
                    case ACTION_UPDATE_USERINFO:
                        user = User.getInstance();
                        img_head.setImageBitmap(user.getBitmap());
                        tv_name.setText(user.getUsername());
                        tv_current_save_path.setText(App.currentSavePath);
                        break;
                    case FINISH_SIGNAL:
                        finish();
                        break;
                }
            }
        };
        registerReceiver(receiver, intentFilter);

    }

    private void initView() {
        img_edit_nickname = (ImageView) findViewById(R.id.img_edit_nickname);
        img_head = (RoundedImageView) findViewById(R.id.img_avatar);
        tv_callback = (ViewGroup) findViewById(R.id.tv_callback);
        tv_down_in_wifi = (ViewGroup) findViewById(R.id.tv_down_in_wifi);
        tv_following = (ViewGroup) findViewById(R.id.tv_following);
        tv_save_path = (ViewGroup) findViewById(R.id.tv_save_path);
        tv_upload = (ViewGroup) findViewById(R.id.tv_upload);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_bar = (TextView) findViewById(R.id.bar);
        tv_exit = (TextView) findViewById(R.id.tv_exit);
        tv_about_us = (ViewGroup) findViewById(R.id.tv_about_us);
        tv_version = (ViewGroup) findViewById(R.id.tv_version);
        btn_switch = (SwitchButton) findViewById(R.id.my_switch_button);
        tv_current_save_path = (TextView) findViewById(R.id.tv_current_save_path);
        tv_revisePsd = (ViewGroup) findViewById(R.id.tv_revisePsd);
        tv_current_save_path.setText(App.currentSavePath);
        tv_bar.setText("关于我的");
        img_back = (ImageView) findViewById(R.id.img_back);
        img_zXing = (ImageView) findViewById(R.id.img_zXing);
        ll_header = (ViewGroup) findViewById(R.id.ll_header);

        img_edit_nickname.setOnClickListener(this);
        tv_callback.setOnClickListener(this);
        tv_down_in_wifi.setOnClickListener(this);
        tv_following.setOnClickListener(this);
        tv_save_path.setOnClickListener(this);
        tv_upload.setOnClickListener(this);
        img_back.setOnClickListener(this);
        tv_exit.setOnClickListener(this);
        btn_switch.setOnClickListener(this);
        tv_version.setOnClickListener(this);
        tv_about_us.setOnClickListener(this);
        tv_current_save_path.setOnClickListener(this);
        tv_revisePsd.setOnClickListener(this);
        ll_header.setOnClickListener(this);
        img_zXing.setOnClickListener(this);

        img_head.setOnClickListener(this);
        if (App.Flag_IsLogin) {
            user = User.getInstance();
            img_head.setImageBitmap(user.getBitmap());
            tv_name.setText(user.getUsername());
            NetWorkOperator.getUserInfo(this, User.getInstance().getName(), 3);
        } else {
            tv_name.setText("点此登录");
        }
        initSwitch();
    }

    private void initSwitch() {
        if (!App.Down_In_Wifi_Switch_State) {
            btn_switch.setChecked(false);
        } else {
            btn_switch.setChecked(true);
        }
        btn_switch.setTintColor(ContextCompat.getColor(this, R.color.blue));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_edit_nickname:
                if (App.Flag_IsLogin) {
                    DialogUtils.ShowDialog(this, user.getUsername());
                }
                break;
            case R.id.ll_header:
                if (!App.Flag_IsLogin) {
                    finish();
                    this.startActivity(new Intent(this, Login.class));
                    App.startAnim(UserMain.this);
                }
                break;
            case R.id.img_avatar:
                if (App.NeworkFlag == NetworkUtils.NETWORK_FLAG_NOT_CONNECT) {
                    EasySweetAlertDialog.ShowTip(this, "tip", "请检查您的网络连接");
                    return;
                }
                if (App.Flag_IsLogin) {
                    startActivityForResult(new Intent(this, DCIMGirdActivity.class), REQUEST_CODE_MY_DCIM);
                } else {
                    EasySweetAlertDialog.ShowTip(this, "Tip", "请先登录");
                }
                break;
            case R.id.tv_callback:
                startActivity(new Intent(this, CallBack.class));
                App.startAnim(UserMain.this);
                break;
            case R.id.tv_following:
                if (App.Flag_IsLogin) {
                    Intent intent = new Intent(this, Followings.class);
                    intent.putExtra("WHERE", 3);
                    startActivity(intent);
                    App.startAnim(this);
                } else {
                    EasySweetAlertDialog.ShowTip(this, "Tip", "请先登录");
                }
                break;
            case R.id.tv_save_path:
                Intent i = new Intent(UserMain.this, FileManager.class);
                i.putExtra("WHERE", 2);
                startActivityForResult(i, PATH_REQUEST);
                App.startAnim(UserMain.this);
                break;
            case R.id.tv_upload:
                if (App.NeworkFlag == NetworkUtils.NETWORK_FLAG_NOT_CONNECT) {
                    EasySweetAlertDialog.ShowTip(this, "tip", "请检查您的网络连接");
                    return;
                }
                if (App.Flag_IsLogin) {
                    Intent intent = new Intent(UserMain.this, OthersMain.class);
                    intent.putExtra("WHERE", 1);
                    startActivity(intent);
                    App.startAnim(UserMain.this);
                } else {
                    EasySweetAlertDialog.ShowTip(this, "Tip", "请先登录");
                }
                break;
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.tv_exit:
                User.deleteUser();
                App.Flag_IsLogin = false;
                //删除SharedPreferences的记录
                App.deleteUserInfo(this);
                this.startActivity(new Intent(this, Login.class));
                App.startAnim(UserMain.this);
                finish();
                break;
            case R.id.my_switch_button:
                System.out.println("Click checked");
                SharedPreferences sp = this.getSharedPreferences("SWITCH", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                if (!App.Down_In_Wifi_Switch_State) {
                    App.Down_In_Wifi_Switch_State = true;
                } else {
                    App.Down_In_Wifi_Switch_State = false;
                }
                editor.putBoolean("SWITCH_WIFI", App.Down_In_Wifi_Switch_State);
                editor.apply();
                break;
            case R.id.tv_version:
                startActivity(new Intent(this, VersionInfo.class));
                App.startAnim(UserMain.this);
                break;
            case R.id.tv_about_us:
                startActivity(new Intent(this, AboutUs.class));
                App.startAnim(UserMain.this);
                break;
            case R.id.tv_revisePsd:
                if (App.NeworkFlag == NetworkUtils.NETWORK_FLAG_NOT_CONNECT) {
                    EasySweetAlertDialog.ShowTip(this, "tip", "请检查您的网络连接");
                    return;
                } else if (App.Flag_IsLogin) {
                    startActivity(new Intent(this, RevisePassword.class));
                    App.startAnim(UserMain.this);
                } else {
                    EasySweetAlertDialog.ShowTip(this, "Tip", "请先登录");
                }
                break;
            case R.id.img_zXing:
                System.out.println("click img_zing");
                if (App.Flag_IsLogin) {
                    startActivity(new Intent(this, ZXingAddFriend.class));
                    App.startAnim(UserMain.this);
                } else {
                    EasySweetAlertDialog.ShowTip(this, "Tip", "清先登录");
                }
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PATH_REQUEST:
                if (data != null) {
                    App.currentSavePath = data.getStringExtra(PATH);
                    tv_current_save_path.setText(App.currentSavePath);
                    SharedPreferences sp = this.getSharedPreferences("PATH", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("Path", App.currentSavePath);
                    editor.apply();
                }
                break;
            case REQUEST_CODE_MY_DCIM:
                if (data != null) {
                    String path = data.getStringExtra("PATH");
                    NetWorkOperator.ModifyUserAvatar(UserMain.this, new File(path));
                }


                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }


//
//    class mPermissionGrant implements PermissionUtils.PermissionGrant{
//
//        @Override
//        public void onPermissionGranted(int requestCode) {
//            switch (requestCode){
//                case PermissionUtils.CODE_CAMERA:
//                    Toast.makeText(UserMain.this, "PermissionUtils.CODE_CAMERA", Toast.LENGTH_SHORT).show();
//                    break;
//
//            }
//        }
//    }
//
//    private mPermissionGrant mPermissionGrant = new mPermissionGrant();
//    public void getPermission() {
//        PermissionUtils.requestPermission(this, PermissionUtils.CODE_CAMERA, mPermissionGrant);
//    }

}
