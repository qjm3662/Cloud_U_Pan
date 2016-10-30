package com.qjm3662.cloud_u_pan.WifiDirect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.qjm3662.cloud_u_pan.R;


public class TransMain extends AppCompatActivity implements View.OnClickListener {

    private Button btn_send;
    private Button btn_receive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_main);

        btn_send = (Button) findViewById(R.id.btn_send);
        btn_receive = (Button) findViewById(R.id.btn_receive);
        btn_send.setOnClickListener(this);
        btn_receive.setOnClickListener(this);
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
        }
    }
}
