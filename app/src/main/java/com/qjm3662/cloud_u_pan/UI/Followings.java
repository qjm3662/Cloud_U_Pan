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
import com.qjm3662.cloud_u_pan.Adapter.FollowingsAdapter;
import com.qjm3662.cloud_u_pan.Data.User;
import com.qjm3662.cloud_u_pan.EasyBarUtils;
import com.qjm3662.cloud_u_pan.Loading.DanceLoadingRenderer;
import com.qjm3662.cloud_u_pan.Loading.LoadingView;
import com.qjm3662.cloud_u_pan.NetWorkOperator;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Widget.EasySweetAlertDialog;

import java.util.ArrayList;
import java.util.List;

public class Followings extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private EasyBar easyBar;
    private FollowingsAdapter adapter;
    private TextView tv_emptyView;
    public static final String ACTION_SUCCESS = "action_SUCCESS";
    public static final String ACTION_FAIL = "action_fail";
    private IntentFilter intentFilter;
    private BroadcastReceiver receiver;
    private List<User> list = new ArrayList<User>();
    private LoadingView loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followings);
        NetWorkOperator.GetFollowingInformation(this, list);
        initLoadingView();
        initView();
        initReceiver();
    }

    private void initLoadingView() {
        loadingView = (LoadingView) findViewById(R.id.loadingView);
        loadingView.setLoadingRenderer(new DanceLoadingRenderer.Builder(this).build_1_point_5());
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
                        tv_emptyView.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();
                        break;
                    case ACTION_FAIL:
                        EasySweetAlertDialog.ShowTip(Followings.this, "加载失败");
                        loadingView.dismiss();
                        tv_emptyView.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };
        registerReceiver(receiver, intentFilter);
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.list_followings);
        EasyBarUtils.justSetTitleAndBack(easyBar, "关注的人", this, 1);
        tv_emptyView = (TextView) findViewById(R.id.list_empty_view);
        adapter = new FollowingsAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setEmptyView(tv_emptyView);

        listView.setOnItemClickListener(this);
        tv_emptyView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NetWorkOperator.getOtherUserInfoByUsername(this, list.get(list.size() - 1 - position).getUsername(), false);
    }
}
