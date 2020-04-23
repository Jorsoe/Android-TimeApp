package com.rfstudio.timeapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "TimeApp.db";     //数据库名称
    public static final int DATABASE_VERSION = 1;                //数据库版本号

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //第一次调用getWritableDatabase()或getReadableDatabase()时会执行
        //这个方法通常用来创建表,和初始数据的
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //当数据库版本号更新时执行的方法
    }
}
