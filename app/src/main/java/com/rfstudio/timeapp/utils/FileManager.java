package com.rfstudio.timeapp.utils;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.rfstudio.timeapp.application.MyApplication;
import com.rfstudio.timeapp.utilsModel.PlanModel;
import com.rfstudio.timeapp.work.homeWork.view.MainActivity;

import java.io.File;
import java.util.List;

/**
 * 用于对 存档 的管理
 * 包含文件数据的 读 和 写 ，等
 */
public class FileManager {
    public static String savefileName = "SaveFile.xml";

    private Context context;

    /**
     * 初始化，需要applicationContext
     * 在可使用 getApplicationContext（）情况下 作为参数传入
     * @param context 请使用ApplicationContext
     */
    public FileManager(Context context) {
        MyApplication application = new MyApplication();
        this.context = context;
        Log.e("File", "来不: " + context.getExternalFilesDir(null).getPath());
    }

    /**
     *  获取应用根目录
     *  （自身有Context）
     * @return 返回 应用数据 根目录
     */
    public  String getDiskDirPath(){

        String dataPath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || ! Environment.isExternalStorageRemovable()){
            // 如果外存卡 存在
            dataPath = context.getExternalFilesDir(null).getPath();
        }else{
            dataPath = context.getFilesDir().getPath();
        }

        return dataPath;
    }

    /**
     *  判定 指定文件 是否存在
     *  当不存在时，会自行生成
     * @param fileName
     * @return true/false
     */
    public boolean fileIsExisted(String fileName){
        try{

            File file = new File(getDiskDirPath() + fileName);
            if (!file.exists()){
                // 如果不存在，会自行创建
                file.mkdir();
                return false;
            }
        }catch (Exception e){
            Log.e("文件存储："+getClass().getName().toString() , "出现异常： " + e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * 添加list到文件
     * @param planlist 计划列表
     * @param fileName 要存储的文件名
     */
    public void addPlanList (List<PlanModel> planlist , String fileName){

    }
}
