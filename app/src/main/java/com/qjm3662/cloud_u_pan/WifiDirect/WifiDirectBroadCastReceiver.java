package com.qjm3662.cloud_u_pan.WifiDirect;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;

/**
 * Created by qjm3662 on 2016/10/25 0025.
 */

public class WifiDirectBroadCastReceiver extends BroadcastReceiver{
    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private Activity activity;
    private int flag_where = 0;
    public final static int FLAG_FROM_RECEIVE_ACTIVITY = 123;
    public final static int FLAG_FROM_SEND_ACTIVITY = 321;
    public final static String CONNECTED_ACTION = "connected action";
    public final static String UNCONNECTED_ACTION = "unconnected action";



    public WifiDirectBroadCastReceiver(int flag_where, WifiP2pManager manager, WifiP2pManager.Channel channel, Activity activity, WifiP2pManager.PeerListListener peerListListener) {
        this.manager = manager;
        this.channel = channel;
        this.activity = activity;
        this.flag_where = flag_where;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            String action = intent.getAction();
            switch (action){
                //当wifi功能打开关闭的时候会广播这个信号
                case WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION:
                    System.out.println("WIFI_P2P_STATE_CHANGED_ACTION");
                    int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                    if(state == WifiP2pManager.WIFI_P2P_STATE_ENABLED){
                        //Wifi Direct is enabled

                    }else{
                        //Wifi Direct is not enabled
                    }
                    break;
                //获取当前可用连接表的列表
                case WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION:
                    System.out.println("WIFI_P2P_PEERS_CHANGED_ACTION");
                    WifiP2pManager.PeerListListener listListener = null;
                    switch (flag_where){
                        case FLAG_FROM_RECEIVE_ACTIVITY:
                            listListener = ((Receive_Activity)activity).getPeerListListener();
                            break;
                        case FLAG_FROM_SEND_ACTIVITY:
                            listListener = ((Send_Activity)activity).getPeerListListener();
                            break;
                    }
                    if(manager != null){
                        manager.requestPeers(channel, listListener);
                    }
                    break;
                //当连接建立或者断开的时候会广播该信号
                case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION:
                    System.out.println("WIFI_P2P_CONNECTION_CHANGED_ACTION");
                    if(manager == null){
                        return;
                    }
                    NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                    WifiP2pManager.ConnectionInfoListener ll = null;
                    System.out.println("_____________________\nnetworkInfo : " + networkInfo.toString() + "\n_____________________________________________");
                    switch (flag_where){
                        case FLAG_FROM_RECEIVE_ACTIVITY:
                            ll = ((Receive_Activity)activity).getConnectionInfoListener();
                            break;
                        case FLAG_FROM_SEND_ACTIVITY:
                            ll = ((Send_Activity)activity).getConnectionInfoListener();
                            break;
                    }
                    if(networkInfo.isConnected()){
                        System.out.println("已连接上");
                        manager.requestConnectionInfo(channel, ll);
                        try{
                            Send_Activity.isConnect = true;
                        }catch(Exception e){
                            System.out.println("SendActivity not exist !!!");
                        }
                        Intent i = new Intent();
                        i.setAction(CONNECTED_ACTION);
                        context.sendBroadcast(i);
                    }else{
                        System.out.println("没有连接上");
                        Intent i = new Intent();
                        i.setAction(UNCONNECTED_ACTION);
                        context.sendBroadcast(i);
                        try{
                            Send_Activity.isConnect = false;
                        }catch (Exception e){
                            System.out.println("SendActivity not exist !!!");
                        }
                    }
                    break;
                //当前设备的Wifi状态发生改变时会广播该信号(Wifi连接，断开)
                case WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION:
                    System.out.println("WIFI_P2P_THIS_DEVICE_CHANGED_ACTION");
                    break;
            }
        }catch (Exception e){
            System.out.println("BrodCastReceiver Error : " + e.toString());
        }
    }

}
