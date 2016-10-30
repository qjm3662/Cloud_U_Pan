package com.qjm3662.cloud_u_pan.WifiDirect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Widget.EasyButton;


public class TransMain extends AppCompatActivity implements View.OnClickListener {

    private EasyButton btn_send;
    private EasyButton btn_receive;
    private TextView tv_bar;
    private ImageView img_back;
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send:
                startActivity(new Intent(this, Send_Activity.class));
                break;
            case R.id.btn_receive:
                startActivity(new Intent(this, Receive_Activity.class));
                break;
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }
}