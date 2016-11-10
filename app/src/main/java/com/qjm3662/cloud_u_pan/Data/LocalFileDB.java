package com.qjm3662.cloud_u_pan.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by qjm3662 on 2016/9/27 0027.
 */

public class LocalFileDB extends SQLiteOpenHelper {
    public static final String TABLE_NAME_LOCAL_FILE_UPLOAD = "LocalFileUpload";
    public static final String TABLE_NAME_LOCAL_FILE_DOWNLOAD = "LocalFileDownload";
    public static final String TABLE_NAME_LOCAL_FILE_WIFI_DIRECT_RECORD = "wifi_direct_record";
    public static final String COLUMN_NAME_ID = "_id";
    public static final String COLUMN_NAME_Name = "name";
    public static final String COLUMN_NAME_Path = "path";
    public static final String COLUMN_NAME_DownTime = "downTime";
    public static final String COLUMN_NAME_Type = "type";
    public static final int Version = 1;

    public LocalFileDB(Context context) {
        super(context, "LocalFileDB", null, Version);
    }

    public LocalFileDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME_LOCAL_FILE_UPLOAD + "(" +
                COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME_Name + " TEXT NOT NULL DEFAULT \"\"," +
                COLUMN_NAME_Path + " TEXT NOT NULL DEFAULT \"\"," +
                COLUMN_NAME_DownTime + " TEXT NOT NULL DEFAULT \"\"," +
                COLUMN_NAME_Type + " TEXT NOT NULL DEFAULT \"\"" +
                ")");
        db.execSQL("CREATE TABLE " + TABLE_NAME_LOCAL_FILE_DOWNLOAD + "(" +
                COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME_Name + " TEXT NOT NULL DEFAULT \"\"," +
                COLUMN_NAME_Path + " TEXT NOT NULL DEFAULT \"\"," +
                COLUMN_NAME_DownTime + " TEXT NOT NULL DEFAULT \"\"," +
                COLUMN_NAME_Type + " TEXT NOT NULL DEFAULT \"\"" +
                ")");
        db.execSQL("CREATE TABLE " + TABLE_NAME_LOCAL_FILE_WIFI_DIRECT_RECORD + "(" +
                COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME_Name + " TEXT NOT NULL DEFAULT \"\"," +
                COLUMN_NAME_Path + " TEXT NOT NULL DEFAULT \"\"," +
                COLUMN_NAME_DownTime + " TEXT NOT NULL DEFAULT \"\"," +
                COLUMN_NAME_Type + " TEXT NOT NULL DEFAULT \"\"" +
                ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
