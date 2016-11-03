package com.qjm3662.cloud_u_pan.UI;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.qjm3662.cloud_u_pan.Adapter.FirstViewAdapter;
import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.R;

import java.util.ArrayList;
import java.util.List;

public class FirstLunchActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private List<View> list;
    private FirstViewAdapter adapter;
    private LayoutInflater inflater;
    private ImageView img_point1;
    private ImageView img_point2;
    private ImageView img_point3;
    private Button btn_start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate ~~~~~~~");
        setContentView(R.layout.activity_first_lunch);
        initView();
    }

    private void initView() {
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        img_point1 = (ImageView) findViewById(R.id.point1);
        img_point2 = (ImageView) findViewById(R.id.point2);
        img_point3 = (ImageView) findViewById(R.id.point3);

        list = new ArrayList<View>();
        View v1 = inflater.inflate(R.layout.first_lunch_view_item1, null);
        View v2 = inflater.inflate(R.layout.first_lunch_view_item2, null);
        View v3 = inflater.inflate(R.layout.first_lunch_view_item3, null);
        btn_start = (Button) v3.findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("finish");
                FirstLunchActivity.this.finish();
            }
        });
        list.add(v1);
        list.add(v2);
        list.add(v3);
        adapter = new FirstViewAdapter(list);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                System.out.println("position : " + position);
                System.out.println("positionOffset:" + positionOffset);
                System.out.println("positionOffsetPixels" + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                System.out.println("position:" + position);
                switch (position){
                    case 0:
                        img_point1.setImageResource(R.drawable.blue_point);
                        img_point2.setImageResource(R.drawable.gray_point);
                        img_point3.setImageResource(R.drawable.gray_point);
                        break;
                    case 1:
                        img_point1.setImageResource(R.drawable.gray_point);
                        img_point2.setImageResource(R.drawable.blue_point);
                        img_point3.setImageResource(R.drawable.gray_point);
                        break;
                    case 2:
                        img_point1.setImageResource(R.drawable.gray_point);
                        img_point2.setImageResource(R.drawable.gray_point);
                        img_point3.setImageResource(R.drawable.blue_point);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                System.out.println("state:" + state);
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
}
