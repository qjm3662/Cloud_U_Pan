package com.qjm3662.cloud_u_pan.WifiDirect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.widget.Toast;

import com.qjm3662.cloud_u_pan.FileManager.FileManager;
import com.qjm3662.cloud_u_pan.UI.MainActivity;


/**
 *
 * Created by qjm3662 on 2016/10/30 0030.
 */

public class qjm_WifiP2pManager {
    private Context context;
    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private WifiDirectBroadCastReceiver receiver;
    private IntentFilter intentFilter;
    private WifiP2pManager.ConnectionInfoListener connectionInfoListener;       //连接状态变化监听器
    private String groupIp = null;      //P2p小组的组长的ip地址
    private int flag_where;
    private Thread StartServerThread = null;
    private Server.ServerListener serverListener;

    /**
     * 返回连接状态变化监听器
     * @return  connectionInfoListener
     */
    public WifiP2pManager.ConnectionInfoListener getConnectionInfoListener() {
        return connectionInfoListener;
    }

    /**
     * qjm_WifiP2pManager 构造函数(for Sender)
     * @param context require a Activity
     * @param listListener PeerListListener(可连接列表变化状态监听器)
     */
    public qjm_WifiP2pManager(Context context, WifiP2pManager.PeerListListener listListener) {
        this.context = context;
        this.flag_where = WifiDirectBroadCastReceiver.FLAG_FROM_SEND_ACTIVITY;
        manager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(context, context.getMainLooper(), null);
        receiver = new WifiDirectBroadCastReceiver(flag_where, manager, channel, ((Activity)context), listListener);

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        registerReceiver();
        initListener();
    }


    /**
     * qjm_WifiP2pManager 构造函数
     * @param context require a Activity
     * @param listListener  PeerListListener(可连接列表变化状态监听器)
     * @param serverListener    文件传输服务回调接口
     */
    public qjm_WifiP2pManager(Context context, WifiP2pManager.PeerListListener listListener, Server.ServerListener serverListener) {
        this.context = context;
        this.flag_where = WifiDirectBroadCastReceiver.FLAG_FROM_RECEIVE_ACTIVITY;
        manager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(context, context.getMainLooper(), null);
        receiver = new WifiDirectBroadCastReceiver(flag_where, manager, channel, ((Activity)context), listListener);
        this.serverListener = serverListener;
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        registerReceiver();
        initListener();
    }



    public void createGroup(){
        manager.createGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, "创建群组成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reason) {

            }
        });
    }
    /**
     * 初始化监听器
     */
    private void initListener() {

        switch (flag_where){
            case WifiDirectBroadCastReceiver.FLAG_FROM_SEND_ACTIVITY:
                //连接状态监听器
                connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
                    @Override
                    public void onConnectionInfoAvailable(WifiP2pInfo info) {
                        Toast.makeText(context, "连接成功", Toast.LENGTH_SHORT).show();
                        groupIp = String.valueOf(info.groupOwnerAddress);
                        System.out.println("WifiP2PInfo : " + info);
                        System.out.println("Address : " + info.groupOwnerAddress);
                    }
                };
                break;
            case WifiDirectBroadCastReceiver.FLAG_FROM_RECEIVE_ACTIVITY:
                connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
                    @Override
                    public void onConnectionInfoAvailable(WifiP2pInfo info) {
                        Toast.makeText(context, "连接成功", Toast.LENGTH_SHORT).show();
                        System.out.println("WifiP2PInfo : " + info);
                        System.out.println("Address : " + info.groupOwnerAddress);
                        if(StartServerThread == null){
                            StartServer(true);
                            System.out.println("创建了一个线程");
                        }
                        if(!StartServerThread.isAlive()){
                            StartServer(true);
                            System.out.println("又创建了一个线程");
                        }
                    }
                };
                break;
        }
    }


    private void StartServer(final boolean IsGroup) {
        System.out.println("StartServer1");
        StartServerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Server create !!!!!");
                    Server.getInstance(serverListener, IsGroup);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        StartServerThread.start();
    }


    /**
     * 注册服务
     */
    public void registerReceiver(){
        context.registerReceiver(receiver, intentFilter);
    }

    /**
     * 解除服务
     */
    public void unRegisterReceiver(){
        context.unregisterReceiver(receiver);
    }


    /**
     * 打开文件选择器选择文件
     */
    public void SendFile() {
        try {
            Intent intent = new Intent(context, FileManager.class);
            intent.putExtra(MainActivity.FILE_SELECT, MainActivity.FILE_SELECT_CODE);
            ((Activity)context).startActivityForResult(intent, 6);
            ((Activity)context).overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        } catch (Exception e) {
            System.out.println("打开错误 : " + e.toString());
        }
    }


    /**
     * 断开连接
     */
    public void DisConnect() {
        manager.removeGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, "断开连接", Toast.LENGTH_SHORT).show();
                Discover();
            }

            @Override
            public void onFailure(int reason) {

            }
        });
    }


    /**
     * 开始搜索附近的设备
     */
    public void Discover() {
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                System.out.println("Success");
            }

            @Override
            public void onFailure(int reason) {
                System.out.println("Fail");
            }
        });
    }


    /**
     * 连接(用已封装监听器)
     * @param config WifiP2pConfig
     */
    public void Connect(WifiP2pConfig config){
        manager.connect(channel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int reason) {
                // ...
            }
        });
    }

    /**
     * 连接（用户自定义监听器）
     * @param config    WifiP2pConfig
     * @param listener  连接状态监听器（onSuccess只是代表该操作成功， 并不能说明连接成功，需要在receive那边判断）
     */
    public void Connect(WifiP2pConfig config, WifiP2pManager.ActionListener listener){
        manager.connect(channel, config, listener);
    }

    /**
     * 发送文件--选择完要发送的文件以后的操作
     * @param data  OnActivityResult函数返回的Intent
     * @param listener  文件发送状态监听器
     */
    public void SendFileOnActivityResult(Intent data, FileSendAsycn.FileSendListener listener){
        System.out.println("Path : " + data.getStringExtra(MainActivity.PATH));
        FileSendAsycn fileSendAsycn = new FileSendAsycn(false, listener);
        if (groupIp == null) {
            Toast.makeText(context, "没有连接到wifi组", Toast.LENGTH_SHORT).show();
            return;
        }
        if (groupIp.startsWith("/")) {
            groupIp = groupIp.substring(1, groupIp.length());
        }
        System.out.println("Ip : " + groupIp);
        try {
            fileSendAsycn.execute(data.getStringExtra(MainActivity.PATH), groupIp);
        } catch (Exception e) {
            System.out.println("传送文件错误 ： " + e.toString());
            //文件传送出错时抛出异常
            throw e;
        }
    }
}
