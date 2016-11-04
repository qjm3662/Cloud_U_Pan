package com.qjm3662.cloud_u_pan.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.qjm3662.cloud_u_pan.App;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

/**
 * Created by qjm3662 on 2016/11/4 0004.
 */

public class BaseActivity extends AppCompatActivity {
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
    }

    @Override
    public void finish() {
        super.finish();
        App.finishAnim(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);

    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
