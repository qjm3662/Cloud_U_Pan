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

import com.makeramen.roundedimageview.RoundedImageView;
import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.Data.User;
import com.qjm3662.cloud_u_pan.R;

public class UserMain extends AppCompatActivity implements View.OnClickListener {

    private ImageView img_edit_nickname;
    private RoundedImageView img_head;
    private TextView tv_name;
    private TextView tv_following;
    private TextView tv_upload;
    private TextView tv_down_in_wifi;
    private TextView tv_save_path;
    private TextView tv_callback;
    private TextView tv_bar;
    private ImageView img_back;
    private TextView tv_exit;
    private BroadcastReceiver receiver;
    public static final String ACTION_GET_USER_INFO_SUCCESS = "get userInfo success";
    private User user;

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
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                user = User.getInstance();
                System.out.println(user.getBitmap());
                img_head.setImageBitmap(user.getBitmap());
                tv_name.setText(user.getName());
            }
        };
        registerReceiver(receiver, intentFilter);

    }

    private void initView() {
        img_edit_nickname = (ImageView) findViewById(R.id.img_edit_nickname);
        img_head = (RoundedImageView) findViewById(R.id.img_avatar);
        tv_callback = (TextView) findViewById(R.id.tv_callback);
        tv_down_in_wifi = (TextView) findViewById(R.id.tv_down_in_wifi);
        tv_following = (TextView) findViewById(R.id.tv_following);
        tv_save_path = (TextView) findViewById(R.id.tv_save_path);
        tv_upload = (TextView) findViewById(R.id.tv_upload);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_bar = (TextView) findViewById(R.id.bar);
        tv_exit = (TextView) findViewById(R.id.tv_exit);
        tv_bar.setText("关于我的");
        img_back = (ImageView) findViewById(R.id.img_back);

        img_edit_nickname.setOnClickListener(this);
        img_head.setOnClickListener(this);
        tv_callback.setOnClickListener(this);
        tv_down_in_wifi.setOnClickListener(this);
        tv_following.setOnClickListener(this);
        tv_save_path.setOnClickListener(this);
        tv_upload.setOnClickListener(this);
        img_back.setOnClickListener(this);
        tv_exit.setOnClickListener(this);


        if(App.Flag_IsLogin){
            user = User.getInstance();
            img_head.setImageBitmap(user.getBitmap());
            tv_name.setText(user.getName());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_edit_nickname:

                break;
            case R.id.img_head:

                break;
            case R.id.tv_callback:

                break;
            case R.id.tv_down_in_wifi:

                break;
            case R.id.tv_following:

                break;
            case R.id.tv_save_path:

                break;
            case R.id.tv_upload:

                break;
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.tv_exit:
                User.deleteUser();
                App.Flag_IsLogin = false;
                this.startActivity(new Intent(this, Login.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
