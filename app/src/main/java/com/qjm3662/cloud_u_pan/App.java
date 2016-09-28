package com.qjm3662.cloud_u_pan;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.p2p.WifiP2pDevice;

import com.qjm3662.cloud_u_pan.Data.FileInformation;
import com.qjm3662.cloud_u_pan.Data.LocalFile;
import com.qjm3662.cloud_u_pan.Data.LocalFileDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanshunwang on 2016/9/21 0021.
 */
public class App extends Application{

    public static String response = "";
    public static FileInformation fileInformation = new FileInformation();
    public static List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();

    public static List<LocalFile> Public_List_Local_File_Upload = new ArrayList<LocalFile>();
    public static List<LocalFile> Public_List_Local_File_Download = new ArrayList<LocalFile>();


    //可读数据库
    public static SQLiteDatabase dbRead;
    public static LocalFileDB localFileDB;
    public static SQLiteDatabase dbWrite;
    @Override
    public void onCreate() {
        super.onCreate();
        //数据库相关操作
        initDataBase();
        GetFileInformationFromDB();
    }

    private void GetFileInformationFromDB() {
        Cursor c = dbRead.query(LocalFileDB.TABLE_NAME_LOCAL_FILE_UPLOAD, null, null, null, null, null, null);
        while(c.moveToNext()){
            LocalFile localFile = new LocalFile();
            localFile.setName(c.getString(c.getColumnIndex(LocalFileDB.COLUMN_NAME_Name)));
            localFile.setPath(c.getString(c.getColumnIndex(LocalFileDB.COLUMN_NAME_Path)));
            localFile.setType(c.getString(c.getColumnIndex(LocalFileDB.COLUMN_NAME_Type)));
            Long times = c.getLong(c.getColumnIndex(LocalFileDB.COLUMN_NAME_DownTime));
            localFile.setDownTime(times);
            Public_List_Local_File_Upload.add(localFile);
        }

        c = dbRead.query(LocalFileDB.TABLE_NAME_LOCAL_FILE_DOWNLOAD, null, null, null, null, null, null);
        while(c.moveToNext()){
            LocalFile localFile = new LocalFile();
            localFile.setName(c.getString(c.getColumnIndex(LocalFileDB.COLUMN_NAME_Name)));
            localFile.setPath(c.getString(c.getColumnIndex(LocalFileDB.COLUMN_NAME_Path)));
            localFile.setType(c.getString(c.getColumnIndex(LocalFileDB.COLUMN_NAME_Type)));
            Long times = c.getLong(c.getColumnIndex(LocalFileDB.COLUMN_NAME_DownTime));
            localFile.setDownTime(times);
            Public_List_Local_File_Download.add(localFile);
        }
    }

    private void initDataBase() {
        localFileDB = new LocalFileDB(this);
        dbRead = localFileDB.getReadableDatabase();
        dbWrite = localFileDB.getWritableDatabase();

    }

    public static void openDB() {
        if (dbWrite.isOpen()) {
            dbWrite.close();
        }
        if (dbRead.isOpen()) {
            dbRead.close();
        }
        dbRead = localFileDB.getReadableDatabase();
        dbWrite = localFileDB.getWritableDatabase();
    }
}
