package com.qjm3662.cloud_u_pan.UI;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.qjm3662.cloud_u_pan.Adapter.FirstViewAdapter;
import com.qjm3662.cloud_u_pan.R;

import java.util.ArrayList;
import java.util.List;

public class FirstLunchActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private List<View> list;
    private FirstViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_lunch);

        initView();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        list = new ArrayList<View>();
        adapter = new FirstViewAdapter(list);
        viewPager.setAdapter(adapter);
    }
}
