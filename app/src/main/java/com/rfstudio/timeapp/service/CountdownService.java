package com.rfstudio.timeapp.service;


import android.app.IntentService;
import android.app.Service;
import android.content.Intent;

import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;



public class CountdownService extends IntentService {

    Handler handler;
    Runnable runnable;
    String BROADCAST_ACTION = "com.countdown.receiver";

    //
    // 鉴于oncreate 只会运行一次，在oncreate中 为 mainCountdownMillis 赋值
    // 可能会有用户重启应用的情况，所以要从某个地方确保得到时长
    private long mainCountdownMillis;         // service 倒计时的时长 (针对某个任务的 是不变的)
    private long timedMillis ;                // 已计时的进度 (有某个方法根据当前时间计算所得)
    private boolean isFished ;               // 倒计时是否完成的标志
    private float nowProcess ;               // 当前进度 可以计算所得

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public CountdownService() {
        super("CountdownService");
    }

    @Override
    public void onCreate() {
        ////////////////测试/////////////////////
        mainCountdownMillis = 10*1000;
        timedMillis = 0;
        // 其实z最开始需要获取某个值 来判断完成情况,有一开始就 已经完成的情况
        isFished = false;

        super.onCreate();
        android.os.Debug.waitForDebugger();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Handler handler1 = new Handler();
        handler1.post(new Runnable() {
            @Override
            public void run() {
                Log.e("hander1","-------这下可以了吧");
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        runTimer();

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 每秒运行一次
     */
    private void runTimer(){
        final long delayMillis = 1*1000;  // 一秒间隔
        handler = new Handler();


        runnable = new Runnable() {
            @Override
            public void run() {
                Log.e("Handke handle","也该有动作------------------run");
                // 处理一些事项
                nowProcess = timedMillis/mainCountdownMillis;

                if (nowProcess >= 1){
                    // 完成了，就可以自我销毁了，并传递完成情况
                    isFished = true;
                    sendToBroadcast(nowProcess,isFished);

                    // 销毁service
                    stopSelf();
                }

                sendToBroadcast(nowProcess,isFished);

                // 每秒更新
                timedMillis += 1000;

                handler.postDelayed(this,delayMillis);
            }
        };

        handler.postDelayed(runnable,0);
    }

    /**
     *  发送广播
     * @param nowProcess
     * @param isFished
     */
    private void sendToBroadcast(float nowProcess,boolean isFished){
        // 发送广播
        Intent intent = new Intent();
        intent.setAction(BROADCAST_ACTION)
                .putExtra("nowProgress",nowProcess)
                .putExtra("isFinished",isFished);
        sendBroadcast(intent);
    }



}
