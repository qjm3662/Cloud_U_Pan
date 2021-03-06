package com.qjm3662.cloud_u_pan.WifiDirect;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;

import com.example.easybar.EasyBar;
import com.example.easybar.OnImageCircleButtonClickedListener;
import com.example.easybar.RoundRectButton;
import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.EasyBarUtils;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.UI.BaseActivity;
import com.qjm3662.cloud_u_pan.Widget.EasyButton;


public class TransMain extends BaseActivity implements OnImageCircleButtonClickedListener {

    private RoundRectButton btn_send;
    private RoundRectButton btn_receive;
    private EasyBar easyBar;
    private WifiManager wifiManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_main);

        initView();
    }

    private void initView() {
        btn_send = (RoundRectButton) findViewById(R.id.btn_send);
        btn_receive = (RoundRectButton) findViewById(R.id.btn_receive);
        btn_send.setOnImageCircleButtonClickedListener(this);
        btn_receive.setOnImageCircleButtonClickedListener(this);

        EasyBarUtils.justSetTitleAndBack("直连快传", this, 1);
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
        }
    }
}
