package com.qjm3662.cloud_u_pan.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.easybar.EasyBar;
import com.qjm3662.cloud_u_pan.Adapter.ShareCenterAdapter;
import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.Data.FileInformation;
import com.qjm3662.cloud_u_pan.EasyBarUtils;
import com.qjm3662.cloud_u_pan.Loading.DanceLoadingRenderer;
import com.qjm3662.cloud_u_pan.Loading.LoadingView;
import com.qjm3662.cloud_u_pan.NetWorkOperator;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Widget.EasySweetAlertDialog;

import java.util.ArrayList;
import java.util.List;

public class ShareCenter extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ShareCenterAdapter adapter;
    private TextView tv_empty_view;
    private EasyBar easyBar;
    private IntentFilter intentFilter;
    private LoadingView loadingView;
    public static final String ACTION_SUCCESS = "action_SUCCESS";
    public static final String ACTION_FAIL = "action_fail";
    private BroadcastReceiver receiver;
    private List<FileInformation> list = new ArrayList<FileInformation>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_center);
        NetWorkOperator.getShareCenter(this, list);
        initListView();
        initReceiver();
    }

    private void initReceiver() {
        intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_SUCCESS);
        intentFilter.addAction(ACTION_FAIL);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()){
                    case ACTION_SUCCESS:
                        loadingView.dismiss();
                        tv_empty_view.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();
                        break;
                    case ACTION_FAIL:
                        EasySweetAlertDialog.ShowTip(ShareCenter.this, "加载失败");
                        loadingView.dismiss();
                        tv_empty_view.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };
        registerReceiver(receiver, intentFilter);
    }

    private void initListView() {
        EasyBarUtils.justSetTitleAndBack(easyBar, "分享中心", this, 1);
        listView = (ListView) findViewById(R.id.list_localFile);
        adapter = new ShareCenterAdapter(this, list);
        listView.setAdapter(adapter);
        tv_empty_view = (TextView) findViewById(R.id.list_empty_view);
        listView.setEmptyView(tv_empty_view);
        listView.setOnItemClickListener(this);
        tv_empty_view.setVisibility(View.INVISIBLE);
        initLoadingView();
    }

    private void initLoadingView() {
        loadingView = (LoadingView) findViewById(R.id.loadingView);
        loadingView.setLoadingRenderer(new DanceLoadingRenderer.Builder(this).build_1_point_5());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final String code = list.get(list.size() - 1 - position).getIdentifyCode();
        FileInformation.callBack callBack = new FileInformation.callBack() {
            @Override
            public void call() {
                Intent intent = new Intent(ShareCenter.this, DownloadUi2.class);
                intent.putExtra("code", code);
                startActivity(intent);
                App.startAnim(ShareCenter.this);
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
