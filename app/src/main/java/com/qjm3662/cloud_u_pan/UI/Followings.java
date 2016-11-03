package com.qjm3662.cloud_u_pan.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.qjm3662.cloud_u_pan.Adapter.FollowingsAdapter;
import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.NetWorkOperator;
import com.qjm3662.cloud_u_pan.R;

public class Followings extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private TextView tv_bar;
    private ImageView img_back;
    private FollowingsAdapter adapter;
    private TextView tv_emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followings);
        initView();
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.list_followings);
        tv_bar = (TextView) findViewById(R.id.bar);
        img_back = (ImageView) findViewById(R.id.img_back);
        tv_emptyView = (TextView) findViewById(R.id.list_empty_view);
        adapter = new FollowingsAdapter(this, App.Public_Following_Info);
        listView.setAdapter(adapter);
        listView.setEmptyView(tv_emptyView);

        tv_bar.setText("关注的人");
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        NetWorkOperator.getOtherUserInfoByName(this, App.Public_Following_Info.get(App.Public_Following_Info.size() - 1 - position).getName(), false);
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
