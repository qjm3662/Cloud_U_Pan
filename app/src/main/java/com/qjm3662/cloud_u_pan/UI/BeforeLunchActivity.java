package com.qjm3662.cloud_u_pan.UI;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.qjm3662.cloud_u_pan.R;

public class BeforeLunchActivity extends BaseActivity {

    private ImageView img;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_lunch);
        img = (ImageView) findViewById(R.id.imageView);
        new Thread(new Runnable() {
            @Override
            public void run() {
                isFirstLunch();
            }
        }).start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    BeforeLunchActivity.this.startActivity(new Intent(BeforeLunchActivity.this, FirstLunchActivity.class));
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("IS_FIRST", false);
                    editor.apply();
                    startActivity(new Intent(BeforeLunchActivity.this, FirstLunchActivity.class));
                    finish();
                    break;
                case 1:
                    TimePicker timePicker = new TimePicker(BeforeLunchActivity.this);
                    Animation animation = AnimationUtils.loadAnimation(BeforeLunchActivity.this, R.anim.image_fade_big);
                    img.startAnimation(animation);
                    timePicker.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            img.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(BeforeLunchActivity.this, MainActivity.class));
                            finish();
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                    },1000);
                    break;
            }
        }
    };
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void isFirstLunch() {
        sp = this.getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE);
        boolean b = sp.getBoolean("IS_FIRST", true);
        if(b){
            handler.sendEmptyMessage(0);
        }else{
            handler.sendEmptyMessage(1);
        }
    }
}
