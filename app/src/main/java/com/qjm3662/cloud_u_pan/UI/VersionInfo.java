package com.qjm3662.cloud_u_pan.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.R;
import com.umeng.analytics.MobclickAgent;

public class VersionInfo extends BaseActivity implements View.OnClickListener {

    private TextView tv_bar;
    private ImageView img_back;
    private TextView tv_check_for_update;
    private TextView tv_user_agreement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_info);

        initVIew();
    }

    private void initVIew() {
        tv_bar = (TextView) findViewById(R.id.bar);
        img_back = (ImageView) findViewById(R.id.img_back);
        tv_check_for_update = (TextView) findViewById(R.id.tv_check_for_update);
        tv_user_agreement = (TextView) findViewById(R.id.tv_user_agreement);

        tv_bar.setText("版本信息");
        img_back.setOnClickListener(this);
        tv_user_agreement.setOnClickListener(this);
        tv_check_for_update.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.tv_check_for_update:

                break;
            case R.id.tv_user_agreement:
                startActivity(new Intent(this, UserAgreement.class));
                App.startAnim(VersionInfo.this);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        App.finishAnim(VersionInfo.this);
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
