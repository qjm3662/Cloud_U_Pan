package com.qjm3662.cloud_u_pan.UI;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.easybar.EasyBar;
import com.qjm3662.cloud_u_pan.EasyBarUtils;
import com.qjm3662.cloud_u_pan.NetWorkOperator;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Widget.EasySweetAlertDialog;

public class RevisePassword extends BaseActivity {

    private EasyBar easyBar;
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
        EasyBarUtils.justSetTitleAndBack(easyBar, "修改密码", this, 1);
        et_old_psd = (EditText) findViewById(R.id.et_old_psd);
        et_new_psd = (EditText) findViewById(R.id.et_new_psd);
        et_new_psd_confirm = (EditText) findViewById(R.id.et_new_psd_confirm);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);


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

}
