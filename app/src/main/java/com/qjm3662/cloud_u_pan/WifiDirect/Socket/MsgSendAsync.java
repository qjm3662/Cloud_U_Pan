package com.qjm3662.cloud_u_pan.WifiDirect.Socket;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by qjm3662 on 2016/11/9 0009.
 */

public class MsgSendAsync extends AsyncTask<String, Integer, String> {
    private Socket client;
    private MsgSendListener listener;

    public MsgSendAsync(MsgSendListener listener) {
        this.listener = listener;
    }

    public interface MsgSendListener {
        void sendSuccess();
        void sendFail();
    }

    @Override
    protected String doInBackground(String... params) {
        String ip = params[0];
        System.out.println("send ip : " + ip);
        boolean b = Boolean.parseBoolean(params[1]);
        System.out.println("send boolean : " + b);
        int port = 14539;
        DataOutputStream dos = null;
        try {
            if (client == null) {
                client = new Socket();
                client.connect(new InetSocketAddress(ip, port), 3000);
            }
            dos = new DataOutputStream(client.getOutputStream());
            dos.writeBoolean(b);
            dos.flush();
            System.out.println("发送信息成功");
            listener.sendSuccess();
        } catch (IOException e) {
            e.printStackTrace();
            listener.sendFail();
            return null;
        } finally {
            try {
                if (client != null) {
                    client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("MSG_SEND_2", e.toString());
            }
        }
        return null;
    }
}
