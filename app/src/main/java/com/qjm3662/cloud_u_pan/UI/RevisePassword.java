package com.qjm3662.cloud_u_pan.UI;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.NetWorkOperator;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Widget.EasySweetAlertDialog;
import com.umeng.analytics.MobclickAgent;

public class RevisePassword extends BaseActivity {

    private TextView tv_bar;
    private ImageView img_back;
    private EditText et_old_psd;
    private EditText et_new_psd;
    private EditText et_new_psd_confirm;
    private Button btn_confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise_password);

        initView();
    }

    private void initView() {
        tv_bar = (TextView) findViewById(R.id.bar);
        img_back = (ImageView) findViewById(R.id.img_back);
        et_old_psd = (EditText) findViewById(R.id.et_old_psd);
        et_new_psd = (EditText) findViewById(R.id.et_new_psd);
        et_new_psd_confirm = (EditText) findViewById(R.id.et_new_psd_confirm);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);

        tv_bar.setText("修改密码");
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_old_psd.getText().toString().equals("") || et_new_psd.getText().toString().equals("") ||et_new_psd_confirm.getText().toString().equals("")){
                    EasySweetAlertDialog.ShowTip(RevisePassword.this, "Tip", "密码不能为空");
                }else if(!et_new_psd.getText().toString().equals(et_new_psd_confirm.getText().toString())){
                    EasySweetAlertDialog.ShowTip(RevisePassword.this, "Tip", "两次输入密码不一致");
                }else{
                    NetWorkOperator.RevisePsd(RevisePassword.this, et_old_psd.getText().toString(), et_new_psd.getText().toString());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void finish() {
        super.finish();
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
