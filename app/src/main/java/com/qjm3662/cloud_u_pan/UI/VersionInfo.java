package com.qjm3662.cloud_u_pan.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qjm3662.cloud_u_pan.R;

public class VersionInfo extends AppCompatActivity implements View.OnClickListener {

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

                break;
        }
    }
}
