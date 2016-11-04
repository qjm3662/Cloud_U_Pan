package com.qjm3662.cloud_u_pan.UI;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.qjm3662.cloud_u_pan.R;
import com.umeng.analytics.MobclickAgent;

public class BeforeLunchActivity extends BaseActivity {

    private ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_lunch);
        img = (ImageView) findViewById(R.id.imageView);
        isFirstLunch();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void isFirstLunch() {
        SharedPreferences sp = this.getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE);
        boolean b = sp.getBoolean("IS_FIRST", true);
        if(b){
            this.startActivity(new Intent(this, FirstLunchActivity.class));
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("IS_FIRST", false);
            editor.apply();
            startActivity(new Intent(BeforeLunchActivity.this, FirstLunchActivity.class));
            finish();
        }else{
            TimePicker timePicker = new TimePicker(this);
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.image_fade_big);
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
        }
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
