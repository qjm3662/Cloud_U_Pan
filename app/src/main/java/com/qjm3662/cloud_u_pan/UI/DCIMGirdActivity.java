package com.qjm3662.cloud_u_pan.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.qjm3662.cloud_u_pan.R;

public class DCIMGirdActivity extends AppCompatActivity {

    private GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dcimgird);

        initView();
    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.my_DCIM_grid);
    }
}
