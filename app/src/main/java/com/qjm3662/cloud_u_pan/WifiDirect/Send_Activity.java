package com.qjm3662.cloud_u_pan.WifiDirect;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qjm3662.cloud_u_pan.Data.User;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.UI.BaseActivity;
import com.qjm3662.cloud_u_pan.UI.MainActivity;
import com.qjm3662.cloud_u_pan.Widget.EasyButton;
import com.qjm3662.cloud_u_pan.WifiDirect.Socket.FileSendAsycn;
import com.qjm3662.cloud_u_pan.WifiDirect.Socket.MsgSendAsync;

import java.util.ArrayList;
import java.util.List;

public class Send_Activity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private qjm_WifiP2pManager manager;
    private WifiP2pManager.PeerListListener peerListListener;
    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    private ListView list_wifi_group;
    private PeersListAdapter adapter;
    private int pro = 0;
    private MyDialog progressDialog;
    private boolean isConnecting = false;               //是否正在连接
    private boolean isConnected = false;                //是否已经连接
    private boolean isFlushing = false;                 //是否正在搜索刷新
    private EasyButton btn_sendFifle;
    public static boolean isConnect = false;
    private BroadcastReceiver receiver;
    private IntentFilter intentFilter;
    private TextView tv_state;
    private TextView tv_name;
    private ImageView img_flush;

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
        initReceiver();
    }

    private void initReceiver() {
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiDirectBroadCastReceiver.CONNECTED_ACTION);
        intentFilter.addAction(WifiDirectBroadCastReceiver.UNCONNECTED_ACTION);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()){
                    case WifiDirectBroadCastReceiver.CONNECTED_ACTION:
                        if(!isConnected){
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println("启动线程发信息");
                                    manager.SendMsg(new MsgSendAsync.MsgSendListener() {
                                        @Override
                                        public void sendSuccess() {
                                            handler.sendEmptyMessage(3);
                                        }

                                        @Override
                                        public void sendFail() {
                                            handler.sendEmptyMessage(4);
                                        }
                                    }, true);
                                }
                            }).start();
                        }
                        break;
                    case WifiDirectBroadCastReceiver.UNCONNECTED_ACTION:
                        tv_state.setText("未连接");
                        isConnected = false;
                        break;
                }
            }
        };
        registerReceiver(receiver, intentFilter);
    }

    private void initView() {
        manager = new qjm_WifiP2pManager(this, peerListListener);
        progressDialog = new MyDialog(this);
        progressDialog.setMessage("文件正在传输");
        btn_sendFifle = (EasyButton) findViewById(R.id.btn_sendFile);
        btn_sendFifle.setOnClickListener(this);

        list_wifi_group = (ListView) findViewById(R.id.list_wifi_group);
        list_wifi_group.setOnItemClickListener(this);
        adapter = new PeersListAdapter(this, peers, manager);
        list_wifi_group.setAdapter(adapter);
        tv_state = (TextView) findViewById(R.id.tv_state);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_name.setText(User.getInstance().getUsername());
        img_flush = (ImageView) findViewById(R.id.img_flush);
        img_flush.setOnClickListener(this);
        ((TextView)findViewById(R.id.bar)).setText("发送文件");
        findViewById(R.id.img_back).setOnClickListener(this);
        discover();
    }

    private void discover(){
        isFlushing = true;
        //发文件人搜索可连接列表
        try {
            manager.Discover();
            showDialog("正在搜索可用列表");
        } catch (Exception e) {
            System.out.println("Discover Error : " + e.toString());
        }
    }
    private void showDialog(String str){
        progressDialog.setMessage(str);
        progressDialog.show();
    }

    private void initListener() {
        //搜索可用列表监听器
        peerListListener = new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList peers) {
                System.out.println(peers.toString());
                Send_Activity.this.peers.clear();
                Send_Activity.this.peers.addAll(peers.getDeviceList());
                adapter.setPeers(Send_Activity.this.peers);
                adapter.notifyDataSetChanged();
                if(Send_Activity.this.peers.size() != 0 && progressDialog.isShowing()){
                    if(!isConnecting && isFlushing){
                        progressDialog.dismiss();
                    }
                }
            }
        };
    }

    class MyDialog extends ProgressDialog{

        public MyDialog(Context context) {
            super(context);
        }

        @Override
        public void dismiss() {
            super.dismiss();
            isFlushing = false;
        }
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
                case 3: //连接信息发送成功
                    tv_state.setText("已连接");
                    isConnected = true;
                    progressDialog.dismiss();
                    isConnecting = false;
                    break;
                case 4: //连接信息发送失败
                    isConnecting = false;
                    progressDialog.dismiss();
                    manager.DisConnect();
                    tv_state.setText("连接失败，请重新连接");
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MainActivity.requestCode_selectFile:
                if (data != null) {
                    progressDialog.setMessage("文件正在传输");
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
        progressDialog.setMessage("正在连接");
        isConnecting = true;
        progressDialog.show();
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
            case R.id.img_back:
                finish();
                break;
            case R.id.img_flush:
                discover();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        manager.registerReceiver();
        registerReceiver(receiver, intentFilter);
    }
    @Override
    public void onPause() {
        super.onPause();
        manager.unRegisterReceiver();
        unregisterReceiver(receiver);
    }
}
