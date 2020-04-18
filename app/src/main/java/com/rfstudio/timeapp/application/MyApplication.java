package com.rfstudio.timeapp.application;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.rfstudio.timeapp.utilsModel.PlanModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class MyApplication extends Application {
    private String appName = "叮铃咚日程";
    private int ringtoneIndex = 0;                // 用户选择的铃声下标

    private ArrayList<PlanModel> planLists;      // PlanList
    private PlanModel nowPlanItem;              // 当前正在执行的（等待提醒/List的第一个元素）的计划

    private Context mainContext;        // 存储MainActivity的上下文
    // 设定alarmManager 的pendingIntent，用于解除/删除/修改 查找对应闹钟
    // String 就以 requestCode 来作为 key
    private Map<String,PendingIntent> pendingIntentMap;
    private CircleProgressBar circleProgressBar;        // 环形进度条控件()



    @Override
    public void onCreate() {
        super.onCreate();
       /// Log.i("aplci" ,"application 初见了:" + getBaseContext().getExternalFilesDir(null).getPath());
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        TypefaceProvider.registerDefaultIconSets();
    }

    public ArrayList<PlanModel> getPlanLists() {
        return planLists;
    }

    public void setPlanLists(ArrayList<PlanModel> planLists) {
        this.planLists = planLists;
    }

    public void setMainContext(Context mainContext) {
        this.mainContext = mainContext;
    }

    public Context getMainContext() {
        return mainContext;
    }

    public String getAppName() {
        return appName;
    }

    public PlanModel getNowPlanItem() {
        nowPlanItem = planLists.get(0);
        return nowPlanItem;
    }

    public int getRingtoneIndex() {
        return ringtoneIndex;
    }

    public void setRingtoneIndex(int ringtoneIndex) {
        this.ringtoneIndex = ringtoneIndex;
    }

    public Map<String, PendingIntent> getPendingIntentMap() {
        return pendingIntentMap;
    }

    public void setPendingIntentMap(Map<String, PendingIntent> pendingIntentMap) {
        this.pendingIntentMap = pendingIntentMap;
    }

    public CircleProgressBar getCircleProgressBar() {
        return circleProgressBar;
    }

    public void setCircleProgressBar(CircleProgressBar circleProgressBar) {
        this.circleProgressBar = circleProgressBar;
    }
}
