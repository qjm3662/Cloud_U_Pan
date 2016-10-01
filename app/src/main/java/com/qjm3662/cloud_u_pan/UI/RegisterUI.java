package com.qjm3662.cloud_u_pan.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qjm3662.cloud_u_pan.NetWorkOperator;
import com.qjm3662.cloud_u_pan.R;

public class RegisterUI extends AppCompatActivity implements View.OnClickListener {

    private TextView register_to_login;
    private Button btn_register;
    private Intent intent;
    private EditText et_phone_number;
    private EditText et_password;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_ui);

        initView();
    }

    private void initView() {
        context = RegisterUI.this;
        register_to_login = (TextView) findViewById(R.id.register_to_login);
        btn_register = (Button) findViewById(R.id.register_btn);
        et_phone_number = (EditText) findViewById(R.id.register_phone);
        et_password = (EditText) findViewById(R.id.register_password);

        register_to_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_to_login:
                intent = new Intent(RegisterUI.this,Login.class);
                startActivity(intent);
                finish();
                break;
            case R.id.register_btn:
                NetWorkOperator.Register(context, et_phone_number.getText().toString(), et_password.getText().toString());
                break;
        }
    }
}
