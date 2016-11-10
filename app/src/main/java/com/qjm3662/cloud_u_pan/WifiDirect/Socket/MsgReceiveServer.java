package com.qjm3662.cloud_u_pan.WifiDirect.Socket;

import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by qjm3662 on 2016/11/9 0009.
 */

public class MsgReceiveServer {
    private static final int PORT = 14539;
    public static ServerSocket server;
    private Socket client;
    public static DataInputStream dis;
    public static MsgReceiveServerListener msgReceiveServerListener;
    public static MsgReceiveServer instance;
    private boolean close_flag = false;
    private ThreadPool threadPool = null;

    public MsgReceiveServer(MsgReceiveServerListener listener) {
        msgReceiveServerListener = listener;
        try {
            if(server == null || !server.isBound() || server.isClosed()){
                server = new ServerSocket(PORT);
                threadPool = new ThreadPool(5);
                System.out.println("执行一次创建");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (!close_flag) {
            try {
                instance = this;
                System.out.println("等待客户端信息接入");
                if(threadPool == null){
                    threadPool = new ThreadPool(5);
                }
                client = server.accept();
                System.out.println("有客户端信息接入");
                threadPool.execute(createTaskRunnable(client));
            } catch (IOException e) {
                Log.e("MSG_RECEIVE", e.toString());
                if (server != null) {
                    try {
                        server.close();
                        server = null;
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                e.printStackTrace();
            }
        }
        System.out.println("close");
        if(server != null && server.isBound()){
            try {
                server.close();
                server = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Runnable createTaskRunnable(final Socket client){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    dis = new DataInputStream(client.getInputStream());
                    boolean b = dis.readBoolean();
                    System.out.println("bool : " + b);
                    if (b) {
                        msgReceiveServerListener.connected();
                        System.out.println("我收到你的bool值了");
                    } else {
                        msgReceiveServerListener.disconnected();
                    }
                } catch (IOException e) {
                    try {
                        if (client != null) {
                            client.close();
                        }
                    } catch (IOException i) {
                        i.printStackTrace();
                        Log.e("MSG_RECEIVE_IO", i.toString());
                    }
                    e.printStackTrace();
                }
            }
        };
        return runnable;
    }

    public interface MsgReceiveServerListener {
        void connected();
        void disconnected();
    }

    public void close(){
        close_flag = true;
        instance = null;
        threadPool.close();
    }


    public static void setListener(MsgReceiveServerListener listener) {
        msgReceiveServerListener = listener;
    }

    public static MsgReceiveServer getInstance(MsgReceiveServerListener listener) {
        if (instance != null) {
            setListener(listener);
            return instance;
        } else {
            try {
                instance = new MsgReceiveServer(listener);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

}
