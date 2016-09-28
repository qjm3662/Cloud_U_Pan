package com.qjm3662.cloud_u_pan.Wifip2pDirect;

import android.annotation.TargetApi;
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
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Widget.EasyButton;
import com.qjm3662.cloud_u_pan.Wifip2pDirect.Adapter.MyAdapter;
import com.qjm3662.cloud_u_pan.Wifip2pDirect.BroadcastReceiver.WifiDirectBroadcastReceiver;
import com.qjm3662.cloud_u_pan.Wifip2pDirect.Service.DataTransferService;
import com.qjm3662.cloud_u_pan.Wifip2pDirect.Service.FileTransferService;
import com.qjm3662.cloud_u_pan.Wifip2pDirect.Task.DataServerAsyncTask;
import com.qjm3662.cloud_u_pan.Wifip2pDirect.Task.FileServerAsyncTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class WIFI extends AppCompatActivity {

    private EasyButton discover;
    private EasyButton stopdiscover;
    private EasyButton stopconnect;
    private EasyButton sendpicture;
//    private EasyButton senddata;
    private EasyButton begrouppwener;

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private List peers = new ArrayList();
    private List<HashMap<String, String>> peersshow = new ArrayList();

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
        setContentView(R.layout.activity_wifi);

        initView();
        initIntentFilter();
        initReceiver();
        initEvents();
    }

    private void initEvents() {

        discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiscoverPeers();
            }
        });
        begrouppwener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BeGroupOwener();
            }
        });

        stopdiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StopDiscoverPeers();
            }
        });
        stopconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StopConnect();
            }
        });
        sendpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 20);
            }
        });
//
//        senddata.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent serviceIntent = new Intent(WIFI.this,
//                        DataTransferService.class);
//                serviceIntent.setAction(DataTransferService.ACTION_SEND_FILE);
//                serviceIntent.putExtra(DataTransferService.EXTRAS_GROUP_OWNER_ADDRESS,
//                        info.groupOwnerAddress.getHostAddress());
//                Log.i("address", "owenerip is " + info.groupOwnerAddress.getHostAddress());
//                serviceIntent.putExtra(DataTransferService.EXTRAS_GROUP_OWNER_PORT,
//                        8888);
//                WIFI.this.startService(serviceIntent);
//            }
//        });


        mAdapter.SetOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                CreateConnect(peersshow.get(position).get("address"),
                        peersshow.get(position).get("name"));
            }

            @Override
            public void OnItemLongClick(View view, int position) {
            }
        });
    }

    private void BeGroupOwener() {
        mManager.createGroup(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int reason) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 20) {
            super.onActivityResult(requestCode, resultCode, data);
            Uri uri = data.getData();
            Intent serviceIntent = new Intent(WIFI.this,
                    FileTransferService.class);

            serviceIntent.setAction(FileTransferService.ACTION_SEND_FILE);
            serviceIntent.putExtra(FileTransferService.EXTRAS_FILE_PATH,
                    uri.toString());

            serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_ADDRESS,
                    info.groupOwnerAddress.getHostAddress());
            serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_PORT,
                    8988);
            WIFI.this.startService(serviceIntent);
        }
    }

    private void StopConnect() {
        SetEasyButtonGone();
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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

    private void initView() {
        begrouppwener= (EasyButton) findViewById(R.id.bt_bgowner);
        stopdiscover = (EasyButton) findViewById(R.id.bt_stopdiscover);
        discover = (EasyButton) findViewById(R.id.bt_discover);
        stopconnect = (EasyButton) findViewById(R.id.bt_stopconnect);
        sendpicture = (EasyButton) findViewById(R.id.bt_sendpicture);
//        senddata = (EasyButton) findViewById(R.id.bt_senddata);
        sendpicture.setVisibility(View.GONE);
//        senddata.setVisibility(View.GONE);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        mAdapter = new MyAdapter(peersshow);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager
                (this.getApplicationContext()));

    }

    private void initReceiver() {
        mManager = (WifiP2pManager) getSystemService(WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, Looper.myLooper(), null);
        WifiP2pManager.PeerListListener mPeerListListerner = new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList peersList) {
                peers.clear();
                peersshow.clear();
                Collection<WifiP2pDevice> aList = peersList.getDeviceList();
                peers.addAll(aList);

                //把列表的信息放到Map里，以便呈现在RecycleView
                for (int i = 0; i < aList.size(); i++) {
                    WifiP2pDevice a = (WifiP2pDevice) peers.get(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("name", a.deviceName);
                    map.put("address", a.deviceAddress);
                    peersshow.add(map);
                }
                mAdapter = new MyAdapter(peersshow);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager
                        (WIFI.this));
                mAdapter.SetOnItemClickListener(new MyAdapter.OnItemClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {
                        CreateConnect(peersshow.get(position).get("address"),
                                peersshow.get(position).get("name"));
                    }
                    @Override
                    public void OnItemLongClick(View view, int position) {

                    }
                });
            }
        };

        WifiP2pManager.ConnectionInfoListener mInfoListener = new WifiP2pManager.ConnectionInfoListener() {

            @Override
            public void onConnectionInfoAvailable(final WifiP2pInfo minfo) {

                Log.i("xyz", "InfoAvailable is on");
                info = minfo;
                TextView view = (TextView) findViewById(R.id.tv_main);
                if (info.groupFormed && info.isGroupOwner) {
                    Log.i("xyz", "owmer start");
                    mServerTask = new FileServerAsyncTask(WIFI.this);
                    mServerTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    mDataTask = new DataServerAsyncTask(WIFI.this, view);
                    mDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else if (info.groupFormed) {
                    SetEasyButtonVisible();
                }
            }
        };
        mReceiver = new WifiDirectBroadcastReceiver(mManager, mChannel, this, mPeerListListerner, mInfoListener);
    }

    private void SetEasyButtonVisible() {
        sendpicture.setVisibility(View.VISIBLE);
//        senddata.setVisibility(View.VISIBLE);
    }

    private void SetEasyButtonGone() {
        sendpicture.setVisibility(View.GONE);
//        senddata.setVisibility(View.GONE);
    }


    private void DiscoverPeers() {
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(int reason) {
            }
        });
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
        Log.i("xyz", "hehehehehe");
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StopConnect();
    }

    public void ResetReceiver() {

        unregisterReceiver(mReceiver);
        registerReceiver(mReceiver, mFilter);

    }
}
