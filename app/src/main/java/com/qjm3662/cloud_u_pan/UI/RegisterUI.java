package com.qjm3662.cloud_u_pan.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.NetWorkOperator;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Tool.MatcherUtils;
import com.qjm3662.cloud_u_pan.Widget.EasySweetAlertDialog;

public class RegisterUI extends BaseActivity implements View.OnClickListener {

    private TextView register_to_login;
    private Button btn_register;
    private Intent intent;
    private EditText et_phone_number;
    private EditText et_password;
    private ImageView img_back;
    private TextView tv_bar;

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
        img_back = (ImageView) findViewById(R.id.img_back);
        tv_bar = (TextView) findViewById(R.id.bar);
        tv_bar.setText("注册");
        img_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_to_login:
                intent = new Intent(RegisterUI.this,Login.class);
                startActivity(intent);
                App.startAnim(RegisterUI.this);
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
}
