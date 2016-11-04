package com.qjm3662.cloud_u_pan.WifiDirect;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.UI.BaseActivity;
import com.qjm3662.cloud_u_pan.Widget.EasyButton;
import com.umeng.analytics.MobclickAgent;


public class TransMain extends BaseActivity implements View.OnClickListener {

    private EasyButton btn_send;
    private EasyButton btn_receive;
    private TextView tv_bar;
    private ImageView img_back;
    private WifiManager wifiManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_main);

        initView();
    }

    private void initView() {
        btn_send = (EasyButton) findViewById(R.id.btn_send);
        btn_receive = (EasyButton) findViewById(R.id.btn_receive);
        btn_send.setOnClickListener(this);
        btn_receive.setOnClickListener(this);

        tv_bar = (TextView) findViewById(R.id.bar);
        tv_bar.setText("直连快传");
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
        wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send:
                startActivity(new Intent(this, Send_Activity.class));
                App.startAnim(this);
                break;
            case R.id.btn_receive:
                startActivity(new Intent(this, Receive_Activity.class));
                App.startAnim(this);
                break;
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
