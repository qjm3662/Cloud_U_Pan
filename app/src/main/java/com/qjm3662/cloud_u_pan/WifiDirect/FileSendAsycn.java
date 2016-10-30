package com.qjm3662.cloud_u_pan.WifiDirect;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by qjm3662 on 2016/10/27 0027.
 */
public class FileSendAsycn extends AsyncTask<String, File, String> {
    private boolean IsGroup;
    private Socket client = null;
    private FileSendListener listener = null;

    public FileSendAsycn(boolean IsGroup) {
        this.IsGroup = IsGroup;
    }
    public FileSendAsycn(boolean IsGroup, FileSendListener listener) {
        this.IsGroup = IsGroup;
        this.listener = listener;
    }

    public FileSendAsycn(boolean IsGroup, Socket client){
        this.IsGroup = IsGroup;
        this.client = client;
    }

    public interface FileSendListener{
        void progress(int progress);
        void success();
        void fail();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String path = params[0];
            String ip = params[1];
            int port = 14538;
            FileInputStream fis = null;
            DataOutputStream dos = null;
            File file = null;
            try {
                if(client == null){
                    client = new Socket(ip, port);
                }
                System.out.println("FilePath :" + path);
                file = new File(path);
                fis = new FileInputStream(file);
                dos = new DataOutputStream(client.getOutputStream());

                //文件名和长度
                dos.writeBoolean(IsGroup);
                dos.flush();
                dos.writeUTF(file.getName());
                dos.flush();
                dos.writeLong(file.length());
                dos.flush();

                int transLen = 0;
                Long fileLength = file.length();
                //传输文件
                byte[] sendBytes = new byte[1024];
                int length = 0;
                if(listener != null){
                    while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {
                        dos.write(sendBytes, 0, length);
                        transLen += length;
                        listener.progress((int) (transLen*100/fileLength));
                        dos.flush();
                    }
                }else{
                    while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {
                        dos.write(sendBytes, 0, length);
                        dos.flush();
                    }
                }

                if(listener != null){
                    listener.success();
                }
            } catch (IOException e) {
                e.printStackTrace();
                if(listener != null){
                    listener.fail();
                }
            } finally {
                if (fis != null)
                    fis.close();
                if (dos != null) {
                    dos.close();
                }
                client.close();
            }
            publishProgress(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("未知错误 ： " + e.toString());
        }
        return null;
    }


    @Override
    protected void onProgressUpdate(File... values) {
        super.onProgressUpdate(values);
    }
}
