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
import com.qjm3662.cloud_u_pan.Tool.MatcherUtils;
import com.qjm3662.cloud_u_pan.Widget.EasySweetAlertDialog;

public class RegisterUI extends AppCompatActivity implements View.OnClickListener {

    private TextView register_to_login;
    private Button btn_register;
    private Intent intent;
    private EditText et_phone_number;
    private EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_ui);
        initView();
    }

    private void initView() {
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
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
                break;
            case R.id.register_btn:
                if(et_phone_number.getText().toString().equals("") || et_password.getText().toString().equals("")){
                    EasySweetAlertDialog.ShowTip(this, "tip", "用户名或密码不能为空");
                }else{
                    if(MatcherUtils.isMobilePhone(et_phone_number.getText().toString())){
                        NetWorkOperator.Register(this, et_phone_number.getText().toString(), et_password.getText().toString());
                    }else{
                        EasySweetAlertDialog.ShowTip(this, "Tip", "请输入正确的手机号");
                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
