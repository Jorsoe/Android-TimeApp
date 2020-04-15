package com.rfstudio.timeapp.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.rfstudio.timeapp.application.MyApplication;
import com.rfstudio.timeapp.utils.NotifyAlertReceiver;
import com.rfstudio.timeapp.utilsModel.PlanModel;

import java.util.ArrayList;
import java.util.Map;

public class AlarmService extends IntentService {
    String FLAG = "AlarmService";

    void logE(String name){
        Log.e(FLAG,"--------------------------------------------------- " + name);
    }

    @Override
    public void onCreate() {
        logE("onCreate");
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        logE("OnHandleIntent");
        testAlarm(intent);


    }

    private void testAlarm(@Nullable Intent intent){
        // 闹钟管理
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        // 设置时间,毫秒计算
        long hourMs = 1*3600000;
        long minteMs = 1*60000;
        long seconds = 10*1000;

        // 获取系统至今的时间,并加上设定的时间
        long triggerAtMillis = SystemClock.elapsedRealtime() + seconds;
        // 设置要触发的intent,可以设置提醒方式，可以广播接受，或者,自身
        Intent alarmIntent = new Intent(this, NotifyAlertReceiver.class);
        // 设置padingIntent,提醒方式等
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,alarmIntent,0);
        // 闹钟设置

        alarmManager.cancel(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// 6.0
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtMillis, pendingIntent);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//  4.4
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtMillis, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtMillis, pendingIntent);
        }
    }


    /**
     *  正式程序，
     *  一次性启动设定多个 alarm，
     *  将pending 以<key,value>形式存储，用于后面的 删除/和修改 ；
     * @param intent
     */
    private void allocAlarmService(Intent intent){
        MyApplication application = (MyApplication) getApplication();
        // 根据后面的设计，可能会获取其他list
        ArrayList<PlanModel> planLists = application.getPlanLists();
        Map<String,PendingIntent> pendingIntentMap = application.getPendingIntentMap();

        for (PlanModel planItem: planLists
             ) {
            if (planItem.isOpen){ // 如果此项目 打开状态，判断是否已执行
                if (! planItem.isExecuted ){    // 没有执行,就可以设定了
                    int requestCode = planItem.getDuring().getRequestCode();
                    PendingIntent pending = setAlarmForPlanItem(planItem,requestCode);
                    pendingIntentMap.put(String.valueOf(requestCode),pending);
                }

            }
        }

        // 设定完毕后，保存PendingMap
        application.setPendingIntentMap(pendingIntentMap);


    }

    /**
     * 为一个 palnItem 设定定时
     * @param planItem  当然最好是可以进行的定时的 item
     * @return PendingIntent,外部存储是，最好用 requestCode为key
     */
    private PendingIntent setAlarmForPlanItem(PlanModel planItem,int requestCode){
        PendingIntent pendingIntent = null;

        if (planItem != null){
            long timeMillis = planItem.getDuring().getStartTimeMillis();

            if (System.currentTimeMillis() <= timeMillis){
                // 没过时间
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                // 设置要触发的intent,可以设置提醒方式，可以广播接受，或者,自身
                Intent alarmIntent = new Intent(this, NotifyAlertReceiver.class);
                // 传递对象数据
                alarmIntent.putExtra("PlanItem",planItem);
                // 设置padingIntent,提醒方式等
                pendingIntent = PendingIntent.getBroadcast(this,requestCode,alarmIntent,0);
                // 闹钟设置,先取消闹钟，防止覆盖; 可用于更新
                alarmManager.cancel(pendingIntent);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// 6.0
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, timeMillis, pendingIntent);

                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//  4.4
                    alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,timeMillis, pendingIntent);
                } else {
                    alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,timeMillis, pendingIntent);
                }
            }

            return pendingIntent;
        }else{
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getBaseContext(),"定时服务启动失败",Toast.LENGTH_LONG).show();
                    long time = 3000;
                    try {
                        wait(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            });
        }

        return pendingIntent;
    }

    public AlarmService() { super("AlarmService"); }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        logE("onStart");
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        logE("onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        logE("onDestory");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        logE("onBind");
        return super.onBind(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        logE("onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        logE("onLowMemory");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        logE("onTrimMemory");
        super.onTrimMemory(level);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        logE("onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        logE("onRebind");
        super.onRebind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        logE("ontastRemove");
        super.onTaskRemoved(rootIntent);
    }
}
