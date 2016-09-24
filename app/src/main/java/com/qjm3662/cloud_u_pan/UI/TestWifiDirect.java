package com.qjm3662.cloud_u_pan.UI;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Receiver.WiFiDirectBroadcastReceiver;
import com.tt.whorlviewlibrary.WhorlView;

public class TestWifiDirect extends AppCompatActivity implements View.OnClickListener {

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private WiFiDirectBroadcastReceiver mReceiver;
    private IntentFilter mIntentFilter;
    private WifiP2pManager.PeerListListener mPeerListListener;

    private Button btn_find_deceive;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_wifi_direct);

        initWifiDirect();
        initIntentFilter();
        initButton();
    }

    private void initButton() {
        btn_find_deceive = (Button) findViewById(R.id.btn_find_deceive);
        btn_find_deceive.setOnClickListener(this);
//        WhorlView whorlView = (WhorlView) findViewById(R.id.whorl2);
//        whorlView.start();



    }



    /* register the broadcast receiver with the intent values to be matched */
    @Override
    protected void onResume(){
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    /* unregister the broadcast receiver */
    @Override
    protected void onPause(){
        super.onPause();
        unregisterReceiver(mReceiver);
    }


    private void initIntentFilter() {
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
//        unregisterReceiver(mReceiver);
        registerReceiver(mReceiver, mIntentFilter);
    }

    private void initWifiDirect() {
        mManager =(WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        //调用initialize()方法把你的应用程序注册到Wi-Fi Direct框架中.
        //这个方法会返回一个WifiP2pManager.Channel对象，它用于把应用程序连接到Wi-Fi Direct框架
        mChannel = mManager.initialize(this, getMainLooper(),null);
        mPeerListListener = new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList peers) {
                System.out.println("Peers : ");
                System.out.println(peers);
            }
        };
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel,this, mPeerListListener);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_find_deceive:
                /*
                * 如果发现处理成功，并检测到对等设备，系统会广播WIFI_P2P_PEERS_CHANGED_ACTION类型的Intent，
                * 你能够在一个广播接收器中监听这个Intent，以便获得对等设备的列表。当你的应用程序接收到这个Intent时，
                * 你能够使用requestPeers()方法来请求被发现的对等设备的列表。
                * */
                //发现有效的可连接的对等设备
                mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        System.out.println("discoverPeers success!!!");
                    }

                    @Override
                    public void onFailure(int reason) {
                        System.out.println("discoverPeers fail: " + reason);
                    }
                });
                break;
        }
    }
}
