package com.rfstudio.timeapp.db;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.rfstudio.timeapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImportDatabaseUtil {
    public static final String DATABASE_NAME = "timeapp.db";            //数据库名称
    public static final String PACKAGE_NAME = "com.rfstudio.timeapp";    //包名
    public static final String DATABASE_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath()
            + "/" + PACKAGE_NAME+"/databases";     //数据库的绝对路径(/data/data/com.rfstudio.timeapp)
    private SQLiteDatabase sqLiteDatabase;      //SQLite数据库对象，内涵对数据库的操作方法
    private Context context;        //上下文对象
    private final int BUFFER_SIZE = 400000;     //字符缓冲

    //构造方法
    public ImportDatabaseUtil(Context context) {
        this.context = context;
    }

    //对外提供打开数据库接口
    public void openDatabase() {
        this.sqLiteDatabase = this.openDatabase(DATABASE_PATH + "/" + DATABASE_NAME);
    }

    //获取打开后的数据库
    public SQLiteDatabase getDatabase() {
        this.openDatabase();
        return this.sqLiteDatabase;
    }

    //本地打开数据库方法
    public SQLiteDatabase openDatabase(String filePath) {
        try {
            File nameFile = new File(DATABASE_PATH);
            nameFile.exists();
            nameFile.mkdir();
            File file = new File(filePath);
            if (!file.exists()) {        //判断文件是否存在
                //然后通过输入输出流，将数据Copy到filePath目录下
                //file.mkdirs();
                InputStream inputStream = context.getResources().openRawResource(R.raw.timeapp);
                FileOutputStream fileoutputStream = new FileOutputStream(filePath);
                byte[] buffer = new byte[BUFFER_SIZE];
                int readCount;
                while ((readCount = inputStream.read(buffer)) > 0) {
                    fileoutputStream.write(buffer, 0, readCount);
                }
                inputStream.close();
                fileoutputStream.close();
            }
            SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(filePath,null);
            return database;
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //关闭数据库
    public void closeDatabase(){
        if (this.sqLiteDatabase != null) {
            sqLiteDatabase.close();
        }
    }
}