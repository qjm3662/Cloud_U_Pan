package com.qjm3662.cloud_u_pan.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.easybar.EasyBar;
import com.qjm3662.cloud_u_pan.EasyBarUtils;
import com.qjm3662.cloud_u_pan.NetWorkOperator;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Tool.MatcherUtils;
import com.qjm3662.cloud_u_pan.Widget.EasySweetAlertDialog;

public class Login extends BaseActivity implements View.OnClickListener {

    private EasyBar easyBar;
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
        EasyBarUtils.justSetTitleAndBack("登录", this, 2);
        btn_login = (Button) findViewById(R.id.login_btn);
        tv_forget_password = (TextView) findViewById(R.id.forget_password);
        et_username = (EditText) findViewById(R.id.login_phone);
        et_password = (EditText) findViewById(R.id.login_password);
        context = Login.this;

        btn_login.setOnClickListener(this);
        tv_forget_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                if(et_username.getText().toString().equals("") || et_password.getText().toString().equals("")){
                    EasySweetAlertDialog.ShowTip(this, "tip", "用户名或密码不能为空");
                }else{
                    if(MatcherUtils.isMobilePhone(et_username.getText().toString())){
                        NetWorkOperator.Login(context, et_username.getText().toString(), et_password.getText().toString());
                    }else{
                        EasySweetAlertDialog.ShowTip(this, "Tip", "请输入正确的手机号");
                    }
                }
                break;
            case R.id.forget_password:
                Intent intent = new Intent(context, RegisterUI.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
