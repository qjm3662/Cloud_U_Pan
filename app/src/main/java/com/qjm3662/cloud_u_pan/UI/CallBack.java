package com.qjm3662.cloud_u_pan.UI;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.easybar.EasyBar;
import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.Data.User;
import com.qjm3662.cloud_u_pan.EasyBarUtils;
import com.qjm3662.cloud_u_pan.NetWorkOperator;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Widget.EasySweetAlertDialog;

public class CallBack extends BaseActivity implements View.OnClickListener {

    private EditText et_callBack;
    private Button btn_callBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_back);
        initView();
    }

    private void initView() {
        EasyBarUtils.justSetTitleAndBack("团队信息", this, 2);
        btn_callBack = (Button) findViewById(R.id.btn_callBack);
        et_callBack = (EditText) findViewById(R.id.et_callBack);
        btn_callBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_callBack:
                if(App.Flag_IsLogin){
                    NetWorkOperator.FeedBack(this, User.getInstance().getUsername(), et_callBack.getText().toString(), et_callBack);
                }else{
                    EasySweetAlertDialog.ShowTip(this, "Tip", "请登录后反馈");
                }
                break;
        }
    }
}
