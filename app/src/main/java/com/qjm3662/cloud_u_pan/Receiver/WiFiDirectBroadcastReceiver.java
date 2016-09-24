package com.qjm3662.cloud_u_pan.Receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;

/**
 *
 * Created by qjm253 on 2016/9/24 0024.
 */
public class WiFiDirectBroadcastReceiver extends BroadcastReceiver{

    private WifiP2pManager manager;

    private WifiP2pManager.Channel channel;

    private Activity activity;

    private WifiP2pManager.PeerListListener mPeerListListener;

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel, Activity activity, WifiP2pManager.PeerListListener mPeerListListener) {
        super();
        this.manager = manager;
        this.channel = channel;
        this.activity = activity;
        this.mPeerListListener = mPeerListListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        System.out.println("Action : " + action);
        switch (action){
            case WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION:
                // Check to see if Wi-Fi is enabled and notify appropriate activity
                //获取应用设备的状态（检查是否支持Wifi Direct）
                int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,-1);
                if(state ==WifiP2pManager.WIFI_P2P_STATE_ENABLED){
                    // Wifi Direct is enabled
                    System.out.println("Wifi Direct is enabled~``~");
                }else{
                    // Wi-Fi Direct is not enabled
                    System.out.println("Wi-Fi Direct is not enabled");
                }
                break;
            case WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION:
                // Call WifiP2pManager.requestPeers() to get a list of current peers
                System.out.println("Call WifiP2pManager.requestPeers() to get a list of current peers");

                // request available peers from the wifi p2p manager. This is an
                // asynchronous call and the calling activity is notified with a
                // callback on PeerListListener.onPeersAvailable()
                if(manager !=null){
                    manager.requestPeers(channel, mPeerListListener);
                }

                break;
            case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION:
                // Respond to new connection or disconnections
                System.out.println("Respond to new connection or disconnections");

                break;
            case WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION:
                // Respond to this device's wifi state changing
                System.out.println("Respond to this device's wifi state changing");
                break;
        }
    }
}
