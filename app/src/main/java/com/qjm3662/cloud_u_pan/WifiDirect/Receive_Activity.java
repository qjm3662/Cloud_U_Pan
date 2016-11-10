package com.qjm3662.cloud_u_pan.WifiDirect;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qjm3662.cloud_u_pan.Adapter.RecordAdapter;
import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.Data.LocalFile;
import com.qjm3662.cloud_u_pan.Data.LocalFileDB;
import com.qjm3662.cloud_u_pan.Data.User;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Tool.FileUtils;
import com.qjm3662.cloud_u_pan.UI.BaseActivity;
import com.qjm3662.cloud_u_pan.WifiDirect.Socket.MsgReceiveServer;
import com.qjm3662.cloud_u_pan.WifiDirect.Socket.ReceiveFileServer;

import java.util.ArrayList;
import java.util.List;

public class Receive_Activity extends BaseActivity {

    private qjm_WifiP2pManager manager;
    private WifiP2pManager.PeerListListener peerListListener;
    private ReceiveFileServer.ServerListener serverListener;
    private MsgReceiveServer.MsgReceiveServerListener msgReceiveServerListener;
    private ProgressBar progressBar;
    private String fileName = "";
    private String path = "";
    private int progress = 0;
    private TextView tv_bar;
    private ImageView img_back;
    private ListView listView;
    private RecordAdapter adapter;
    private List<LocalFile> list = new ArrayList<LocalFile>();
    private BroadcastReceiver receiver;
    private IntentFilter intentFilter;
    private TextView tv_state;
    private TextView tv_name;


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
                    System.out.println("11111111111");
                    try{
                        final LocalFile localFile = new LocalFile(fileName, path, System.currentTimeMillis());
                        App.Public_List_Wifi_Trans_Record.add(localFile);
                        list.add(localFile);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                ContentValues cv = new ContentValues();
                                cv.put(LocalFileDB.COLUMN_NAME_Name, localFile.getName());
                                cv.put(LocalFileDB.COLUMN_NAME_DownTime, System.currentTimeMillis());
                                cv.put(LocalFileDB.COLUMN_NAME_Path, localFile.getPath());
                                cv.put(LocalFileDB.COLUMN_NAME_Type, localFile.getType());
                                App.dbWrite.insert(LocalFileDB.TABLE_NAME_LOCAL_FILE_WIFI_DIRECT_RECORD, null, cv);
                            }
                        }).start();
                        adapter.notifyDataSetChanged();
                    }catch (Exception e){
                        System.out.println("文件传输完成后error : " + e.toString());
                    }
                    break;
                case 2: //连接成功回执
                    tv_state.setText("已连接");
                    break;
                case 3://断开连接回执
                    tv_state.setText("等待连接");
                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_);
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
//                        tv_state.setText("已连接");
                        break;
                    case WifiDirectBroadCastReceiver.UNCONNECTED_ACTION:
                        tv_state.setText("等待连接");
                        break;
                }
            }
        };
        registerReceiver(receiver, intentFilter);
    }


    private void initView() {
        manager = new qjm_WifiP2pManager(this, peerListListener, serverListener, msgReceiveServerListener);
        //收文件的人创建P2p群组
        manager.createGroup();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tv_bar = (TextView) findViewById(R.id.bar);
        tv_bar.setText("接收文件");
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView = (ListView) findViewById(R.id.list);
        adapter = new RecordAdapter(this, list);
        tv_state = (TextView) findViewById(R.id.tv_state);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_name.setText(User.getInstance().getUsername());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LocalFile localFile = list.get(list.size() - 1 - position);
                if(!localFile.isFlag_is_DateNode()){
                    FileUtils.OpenFile(Receive_Activity.this, localFile.getPath(), localFile.getName());
                }
            }
        });
    }

    private void initListener() {
        peerListListener = new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList peers) {
            }
        };
        serverListener = new ReceiveFileServer.ServerListener() {
            @Override
            public void FileInfoCallback(String fileName, String path) {
                try{
                    Receive_Activity.this.fileName =fileName;
                    Receive_Activity.this.path = path;
                }catch (Exception e){
                    System.out.println("返回操作错误： " + e.toString());
                }
            }

            @Override
            public void FileSuccessCallback() {
                handler.sendEmptyMessage(1);
            }

            @Override
            public void FileProgressCallback(int progress) {
                Receive_Activity.this.progress = progress;
                handler.sendEmptyMessage(0);
            }
        };
        msgReceiveServerListener = new MsgReceiveServer.MsgReceiveServerListener() {
            @Override
            public void connected() {
                handler.sendEmptyMessage(2);
            }

            @Override
            public void disconnected() {
                handler.sendEmptyMessage(3);
            }
        };
    }


    @Override
    public void onBackPressed() {
        manager.justDisconnect();
        System.out.println("just disconnect");
        super.onBackPressed();
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
