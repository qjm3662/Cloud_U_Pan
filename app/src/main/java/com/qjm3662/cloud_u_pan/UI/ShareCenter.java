package com.qjm3662.cloud_u_pan.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.qjm3662.cloud_u_pan.Adapter.ShareCenterAdapter;
import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.Data.FileInformation;
import com.qjm3662.cloud_u_pan.NetWorkOperator;
import com.qjm3662.cloud_u_pan.R;

public class ShareCenter extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ShareCenterAdapter adapter;
    private TextView tv_empty_view;
    private TextView tv_bar;
    private ImageView img_back;
    private IntentFilter intentFilter;
    public static final String ACTION = "action";
    private BroadcastReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_center);
        initListView();
        initReceiver();
    }

    private void initReceiver() {
        intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                adapter.notifyDataSetChanged();
            }
        };
        registerReceiver(receiver, intentFilter);
    }

    private void initListView() {
        listView = (ListView) findViewById(R.id.list_localFile);
        adapter = new ShareCenterAdapter(this);
        listView.setAdapter(adapter);
        tv_empty_view = (TextView) findViewById(R.id.list_empty_view);
        listView.setEmptyView(tv_empty_view);
        listView.setOnItemClickListener(this);
        tv_bar = (TextView) findViewById(R.id.bar);
        tv_bar.setText("分享中心");
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final String code = App.Public_List_File_Info.get(App.Public_List_File_Info.size() - 1 - position).getIdentifyCode();
        FileInformation.callBack callBack = new FileInformation.callBack() {
            @Override
            public void call() {
                Intent intent = new Intent(ShareCenter.this, DownloadUi2.class);
                intent.putExtra("code", code);
                startActivity(intent);
            }
        };
        NetWorkOperator.GetFileInformation(this, code, App.fileInformation, callBack);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(receiver != null){
            unregisterReceiver(receiver);
        }
    }
}
