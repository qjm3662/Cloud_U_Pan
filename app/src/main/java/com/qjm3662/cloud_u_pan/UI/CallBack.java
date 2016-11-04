package com.qjm3662.cloud_u_pan.UI;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.Data.User;
import com.qjm3662.cloud_u_pan.NetWorkOperator;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Widget.EasySweetAlertDialog;
import com.umeng.analytics.MobclickAgent;

public class CallBack extends BaseActivity implements View.OnClickListener {

    private TextView tv_bar;
    private ImageView img_back;
    private EditText et_callBack;
    private Button btn_callBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_back);
        initView();
    }

    private void initView() {
        tv_bar = (TextView) findViewById(R.id.bar);
        img_back = (ImageView) findViewById(R.id.img_back);
        btn_callBack = (Button) findViewById(R.id.btn_callBack);
        et_callBack = (EditText) findViewById(R.id.et_callBack);
        tv_bar.setText("意见反馈");

        img_back.setOnClickListener(this);
        btn_callBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_callBack:
                if(App.Flag_IsLogin){
                    NetWorkOperator.FeedBack(this, User.getInstance().getName(), et_callBack.getText().toString(), et_callBack);
                }else{
                    EasySweetAlertDialog.ShowTip(this, "Tip", "请登录后反馈");
                }
                break;
            case R.id.img_back:
                finish();
                break;
        }
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
    @Override
    public void finish() {
        super.finish();
        App.finishAnim(this);
    }
}
