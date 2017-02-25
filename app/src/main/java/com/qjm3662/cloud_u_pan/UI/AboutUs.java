package com.qjm3662.cloud_u_pan.UI;

import android.os.Bundle;
import com.example.easybar.EasyBar;
import com.qjm3662.cloud_u_pan.EasyBarUtils;
import com.qjm3662.cloud_u_pan.R;

public class AboutUs extends BaseActivity {

    private EasyBar easyBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initView();
    }

    private void initView() {
        EasyBarUtils.justSetTitleAndBack("团队信息", this, 1);
    }
}
