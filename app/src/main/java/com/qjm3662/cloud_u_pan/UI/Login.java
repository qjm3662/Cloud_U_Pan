package com.qjm3662.cloud_u_pan.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qjm3662.cloud_u_pan.NetWorkOperator;
import com.qjm3662.cloud_u_pan.R;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private ImageView img_back;
    private Button btn_login;
    private TextView tv_forget_password;
    private EditText et_username;
    private EditText et_password;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        img_back = (ImageView) findViewById(R.id.img_back);
        btn_login = (Button) findViewById(R.id.login_btn);
        tv_forget_password = (TextView) findViewById(R.id.forget_password);
        et_username = (EditText) findViewById(R.id.login_phone);
        et_password = (EditText) findViewById(R.id.login_password);
        context = Login.this;

        img_back.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        tv_forget_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.login_btn:
                NetWorkOperator.Login(context, et_username.getText().toString(), et_password.getText().toString());
                break;
            case R.id.forget_password:
                Intent intent = new Intent(context, RegisterUI.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
