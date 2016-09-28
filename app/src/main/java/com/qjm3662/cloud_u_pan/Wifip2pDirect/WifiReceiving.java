package com.qjm3662.cloud_u_pan.Wifip2pDirect;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Widget.EasyButton;
import com.qjm3662.cloud_u_pan.Wifip2pDirect.BroadcastReceiver.WifiDirectBroadcastReceiver;
import com.qjm3662.cloud_u_pan.Wifip2pDirect.Service.FileTransferService;
import com.qjm3662.cloud_u_pan.Wifip2pDirect.Task.DataServerAsyncTask;
import com.qjm3662.cloud_u_pan.Wifip2pDirect.Task.FileServerAsyncTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WifiReceiving extends AppCompatActivity {
    private EasyButton btn_joinAGroup;
    private EasyButton btn_create_a_group;
    private ImageView img_back;
    private TextView tv_bar;
    private TextView tv1;
    private TextView tv2;
    private TextView tv_bottom;
    private ImageView img_joinGroup;
    private ImageView img_create_a_group;
    private TextView tv_justview;

    private List peers = new ArrayList();
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private BroadcastReceiver mReceiver;
    private IntentFilter mFilter;
    private WifiP2pInfo info;

    private FileServerAsyncTask mServerTask;
    private DataServerAsyncTask mDataTask;

    private Utils utils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_receiveing);
        initIntentFilter();
        initReceiver();
        BeGroupOwner();
    }

    /**
     * 创建群组
     */
    private void BeGroupOwner() {
        mManager.createGroup(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                System.out.println("创建群组成功！！！");
            }

            @Override
            public void onFailure(int reason) {

            }
        });
    }


    private void StopConnect() {
        mManager.removeGroup(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(int reason) {

            }
        });
    }

    /*A demo base on API which you can connect android device by wifidirect,
    and you can send file or data by socket,what is the most important is that you can set
    which device is the client or service.*/

    private void CreateConnect(String address, final String name) {
        WifiP2pDevice device;
        WifiP2pConfig config = new WifiP2pConfig();
        Log.i("xyz", address);

        config.deviceAddress = address;
        /*mac地址*/
        config.wps.setup = WpsInfo.PBC;
        Log.i("address", "MAC IS " + address);
        if (address.equals("9a:ff:d0:23:85:97")) {
            config.groupOwnerIntent = 0;
            Log.i("address", "lingyige shisun");
        }
        if (address.equals("36:80:b3:e8:69:a6")) {
            config.groupOwnerIntent = 15;
            Log.i("address", "lingyigeshiwo");
        }

        Log.i("address", "lingyige youxianji" + String.valueOf(config.groupOwnerIntent));

        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int reason) {


            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void StopDiscoverPeers() {
        mManager.stopPeerDiscovery(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(int reason) {


            }
        });
    }

    private void initReceiver() {
        mManager = (WifiP2pManager) getSystemService(WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, Looper.myLooper(), null);

        WifiP2pManager.PeerListListener mPeerListListener = new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList peersList) {
                peers.clear();
                Collection<WifiP2pDevice> aList = peersList.getDeviceList();
                peers.addAll(aList);
                for (int i = 0; i < aList.size(); i++) {
                    WifiP2pDevice device = (WifiP2pDevice) peers.get(i);
                }
            }
        };

        WifiP2pManager.ConnectionInfoListener mInfoListener = new WifiP2pManager.ConnectionInfoListener() {

            @Override
            public void onConnectionInfoAvailable(final WifiP2pInfo minfo) {
                Log.i("xyz", "InfoAvailable is on");
                info = minfo;
//                TextView view = (TextView) findViewById(R.id.tv_main);
                if (info.groupFormed && info.isGroupOwner) {
                    Log.i("xyz", "owmer start");
                    mServerTask = new FileServerAsyncTask(WifiReceiving.this);
                    mServerTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    Toast.makeText(WifiReceiving.this, "连接成功", Toast.LENGTH_SHORT).show();
//                    tv_bottom.setText("");
                } else if (info.groupFormed) {
//                    SetButtonVisible();
                }
            }
        };
        mReceiver = new WifiDirectBroadcastReceiver(mManager, mChannel, this, mPeerListListener, mInfoListener);
    }

    private void initIntentFilter() {
        mFilter = new IntentFilter();
        mFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mFilter.addAction(WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION);
        mFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StopConnect();
        Log.i("xyz", "hehehehehe");
        unregisterReceiver(mReceiver);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            StopDiscoverPeers();
        }
    }
}
