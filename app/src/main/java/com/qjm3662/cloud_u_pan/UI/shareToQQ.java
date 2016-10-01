package com.qjm3662.cloud_u_pan.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.R;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

//import com.tencent.connect.common.Constants;

public class shareToQQ extends AppCompatActivity {

    private Tencent tencent;
    private Button btn_share;
    private TextView tv;
    private Button btn_getUserInfo;
    private String SCOPE = "get_user_info,add_t";
    private IUiListener iUiListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_to_qq);

        tencent = Tencent.createInstance(App.App_ID, getApplicationContext());
        if(tencent == null){
            System.out.println("tencent is null");
        }
        initView();

    }

    private void initView() {
        btn_share = (Button) findViewById(R.id.btn_share);
        tv = (TextView) findViewById(R.id.textView);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iUiListener = new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        System.out.println("成功");
                        JSONObject jsonObject = null;
                        if(o instanceof JSONObject){
                            jsonObject = (JSONObject) o;
                        }
                        try {
                            tencent.setOpenId(jsonObject.getString("openid"));
                            long expires_in = System.currentTimeMillis() + Long.parseLong(jsonObject.getString("expires_in")) * 1000;
                            tencent.setAccessToken(jsonObject.getString("access_token"), String.valueOf((expires_in - System.currentTimeMillis())/1000));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println(jsonObject);
                    }

                    @Override
                    public void onError(UiError uiError) {
                        System.out.println("错误");
                    }

                    @Override
                    public void onCancel() {
                        System.out.println("取消");
                    }
                };
                tencent.login(shareToQQ.this, SCOPE,iUiListener);

            }
        });


        btn_getUserInfo = (Button) findViewById(R.id.btn_getUerInfo);
        btn_getUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                tencent.requestAsync(Constants.GRAPH_INTIMATE_FRIENDS,null, Constants.HTTP_GET, );
                QQToken token = tencent.getQQToken();
                System.out.println(token.getAppId());
                System.out.println(token.getAccessToken());
                System.out.println(token.getOpenId());
                UserInfo info = new UserInfo(shareToQQ.this, tencent.getQQToken());
                info.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        System.out.println("成功！！");
                        JSONObject jsonObject = (JSONObject) o;
                        System.out.println(jsonObject);
                    }

                    @Override
                    public void onError(UiError uiError) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        tencent.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.REQUEST_QQ_SHARE || requestCode == Constants.REQUEST_LOGIN){
            if (resultCode == Constants.ACTIVITY_OK) {
                Tencent.handleResultData(data, iUiListener);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
