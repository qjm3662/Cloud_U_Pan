package com.qjm3662.cloud_u_pan.UI;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qjm3662.cloud_u_pan.R;

public class AboutUs extends BaseActivity {

    private TextView tv_bar;
    private ImageView img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initView();
    }

    private void initView() {
        tv_bar = (TextView) findViewById(R.id.bar);
        tv_bar.setText("团队信息");
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
