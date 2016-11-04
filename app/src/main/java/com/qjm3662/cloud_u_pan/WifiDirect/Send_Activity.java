package com.qjm3662.cloud_u_pan.WifiDirect;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.UI.BaseActivity;
import com.qjm3662.cloud_u_pan.UI.MainActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class Send_Activity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private qjm_WifiP2pManager manager;
    private WifiP2pManager.PeerListListener peerListListener;
    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    private ListView list_wifi_group;
    private PeersListAdapter adapter;
    private int pro = 0;
    private ProgressDialog progressDialog;
    private Button btn_sendFifle;
    private Button btn_disConnect;
    public static boolean isConnect = false;

    public WifiP2pManager.PeerListListener getPeerListListener() {
        return peerListListener;
    }

    public WifiP2pManager.ConnectionInfoListener getConnectionInfoListener() {
        return manager.getConnectionInfoListener();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_);
        initListener();
        initView();
    }

    private void initView() {
        manager = new qjm_WifiP2pManager(this, peerListListener);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("文件正在传输");
        btn_sendFifle = (Button) findViewById(R.id.btn_sendFile);
        btn_disConnect = (Button) findViewById(R.id.btn_disConnect);
        btn_sendFifle.setOnClickListener(this);
        btn_disConnect.setOnClickListener(this);

        list_wifi_group = (ListView) findViewById(R.id.list_wifi_group);
        list_wifi_group.setOnItemClickListener(this);
        adapter = new PeersListAdapter(this, peers);
        list_wifi_group.setAdapter(adapter);

        //发文件人搜索可连接列表
        try {
            manager.Discover();
        } catch (Exception e) {
            System.out.println("Discover Error : " + e.toString());
        }
    }

    private void initListener() {
        //搜索可用列表监听器
        peerListListener = new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList peers) {
                Send_Activity.this.peers.clear();
                Send_Activity.this.peers.addAll(peers.getDeviceList());
                adapter.setPeers(Send_Activity.this.peers);
                adapter.notifyDataSetChanged();
            }
        };
    }



    //处理文件传输时的handler
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: //传文件进度
                    progressDialog.setProgress(pro);
                    break;
                case 1: //文件传输成功
                    progressDialog.dismiss();
                    break;
                case 2: //文件传输失败
                    progressDialog.dismiss();
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MainActivity.requestCode_selectFile:
                if (data != null) {
                    progressDialog.show();
                    try{
                        manager.SendFileOnActivityResult(data, new FileSendAsycn.FileSendListener() {
                            @Override
                            public void progress(int progress) {
                                pro = progress;
                                handler.sendEmptyMessage(0);
                            }
                            @Override
                            public void success() {
                                handler.sendEmptyMessage(1);
                            }

                            @Override
                            public void fail() {
                                handler.sendEmptyMessage(2);
                            }
                        });
                    }catch (Exception e){
                        System.out.println(e.toString());
                        progressDialog.dismiss();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = peers.get(position).deviceAddress;
        config.wps.setup = WpsInfo.PBC;
        System.out.println("Connect DeviceAddress : " + config.deviceAddress + "  ing.......");
        manager.Connect(config);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sendFile:
                if (isConnect) {
                    manager.SendFile();
                } else {
                    Toast.makeText(this, "请先连接一个群组", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_disConnect:
                manager.DisConnect();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        App.finishAnim(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        manager.registerReceiver();
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        manager.unRegisterReceiver();
    }
}
