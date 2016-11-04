package com.qjm3662.cloud_u_pan.WifiDirect;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Tool.FileUtils;
import com.qjm3662.cloud_u_pan.UI.BaseActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class Receive_Activity extends BaseActivity {

    private qjm_WifiP2pManager manager;
    private WifiP2pManager.PeerListListener peerListListener;
    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    private ProgressBar progressBar;
    private TextView tv_fileInfo;
    private Button btn_openFile;
    private String fileName = "";
    private String path = "";
    private int progress = 0;

    public WifiP2pManager.PeerListListener getPeerListListener() {
        return peerListListener;
    }

    public WifiP2pManager.ConnectionInfoListener getConnectionInfoListener() {
        return manager.getConnectionInfoListener();
    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:         //文件传输进度
                    progressBar.setProgress(progress);
                    break;
                case 1:
                    tv_fileInfo.append("FileName:" + fileName + "\n");
                    tv_fileInfo.append("Path:" + path + "\n");
                    break;
            }
        }
    };

    private Server.ServerListener serverListener = new Server.ServerListener() {
        @Override
        public void FileInfoCallback(String fileName, String path) {
            Receive_Activity.this.fileName =fileName;
            Receive_Activity.this.path = path;
            handler.sendEmptyMessage(1);
        }

        @Override
        public void FileProgressCallback(int progress) {
            Receive_Activity.this.progress = progress;
            handler.sendEmptyMessage(0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_);
        initListener();
        initView();
    }


    private void initView() {
        manager = new qjm_WifiP2pManager(this, peerListListener, serverListener);
        //收文件的人创建P2p群组
        manager.createGroup();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tv_fileInfo = (TextView) findViewById(R.id.tv_fileInfo);
        btn_openFile = (Button) findViewById(R.id.btn_openFile);
        btn_openFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开文件操作
                FileUtils.OpenFile(Receive_Activity.this, path, fileName);
            }
        });
    }

    private void initListener() {
        peerListListener = new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList peers) {
                Receive_Activity.this.peers.clear();
                Receive_Activity.this.peers.addAll(peers.getDeviceList());
            }
        };

    }


    @Override
    public void onBackPressed() {
        manager.DisConnect();
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
