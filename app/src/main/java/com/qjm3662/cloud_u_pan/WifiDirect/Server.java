package com.qjm3662.cloud_u_pan.WifiDirect;

/**
 * Created by qjm3662 on 2016/10/26 0026.
 */


import com.qjm3662.cloud_u_pan.Tool.FileUtils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务器
 */
public class Server extends ServerSocket {

    private static final int PORT = 14538;

    private ServerSocket server;
    public static Socket client;
    private DataInputStream dis;
    private FileOutputStream fos;
    public static ServerListener serverListener;

    public static Server instance;

    public static void setServerListener(ServerListener ss) {
        serverListener = ss;
    }


    public Server(ServerListener ss, boolean IsGroup) throws Exception {
        serverListener = ss;
        try {
            try {
                server = new ServerSocket(PORT);
                instance = this;
                while (true) {
                    System.out.println("等待客户端接入");
                    client = server.accept();
                    System.out.println("客户端已接入");
                    dis = new DataInputStream(client.getInputStream());
                    //文件名和长度
                    boolean isgroup = dis.readBoolean();
                    if(isgroup == IsGroup){
                        continue;
                    }
                    String fileName = dis.readUTF();
                    long fileLength = dis.readLong();
                    fos = new FileOutputStream(new File(FileUtils.getSDPath() + "/" + fileName));

                    byte[] sendBytes = new byte[1024];
                    long transLen = 0;
                    System.out.println("----开始接收文件<" + fileName + ">,文件大小为<" + fileLength + ">----");
                    serverListener.FileInfoCallback(fileName, FileUtils.getSDPath() + "/" + fileName);
                    long startTime = System.currentTimeMillis();
                    int progress = 0;
                    int temp = -1;
                    while (true) {
                        int read = 0;
                        read = dis.read(sendBytes);
                        if (read == -1)
                            break;
                        transLen += read;
                        temp = progress;
                        progress = (int) (100 * transLen / fileLength);
                        if(temp != progress){
                            serverListener.FileProgressCallback(progress);
                            System.out.println("接收文件进度" + 100 * transLen / fileLength + "%...");
                        }
                        fos.write(sendBytes, 0, read);
                        fos.flush();
                    }
                    long span = System.currentTimeMillis() - startTime;
                    System.out.println("times : " + System.currentTimeMillis());
                    System.out.println("----接收文件<" + fileName + "(" + fileLength + ")" + ">成功-------");
                    System.out.println("span : " + span/1000);
                    client.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (dis != null)
                    dis.close();
                if (fos != null)
                    fos.close();
                server.close();
                instance = null;
            }
            System.out.println("Server Over !!!!----------------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Server getInstance(ServerListener serverListener, boolean IsGroup){
        if(instance != null){
            setServerListener(serverListener);
            return instance;
        }else{
            try {
                instance = new Server(serverListener, IsGroup);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }




    public static Socket getCurrentClient(){
        return client;
    }
    public interface ServerListener{
        void FileInfoCallback(String fileName, String path);
        void FileProgressCallback(int progress);
    }
}
