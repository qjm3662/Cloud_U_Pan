package com.qjm3662.cloud_u_pan.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.easybar.EasyBar;
import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.EasyBarUtils;
import com.qjm3662.cloud_u_pan.R;

public class VersionInfo extends BaseActivity implements View.OnClickListener {

    private EasyBar easyBar;
    private TextView tv_check_for_update;
    private TextView tv_user_agreement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_info);

        initVIew();
    }

    private void initVIew() {
        EasyBarUtils.justSetTitleAndBack(easyBar, "版本信息", this, 1);
        tv_check_for_update = (TextView) findViewById(R.id.tv_check_for_update);
        tv_user_agreement = (TextView) findViewById(R.id.tv_user_agreement);

        tv_user_agreement.setOnClickListener(this);
        tv_check_for_update.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_check_for_update:

                break;
            case R.id.tv_user_agreement:
                startActivity(new Intent(this, UserAgreement.class));
                App.startAnim(VersionInfo.this);
                break;
        }
    }
}
