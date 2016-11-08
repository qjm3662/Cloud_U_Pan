package com.qjm3662.cloud_u_pan.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.qjm3662.cloud_u_pan.Loading.LoadingView;
import com.qjm3662.cloud_u_pan.R;

public class LoadingTestActivity extends AppCompatActivity {

    private LoadingView loadingView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_test);


//        loadingView = (LoadingView) findViewById(R.id.loadingView);
    }
}
