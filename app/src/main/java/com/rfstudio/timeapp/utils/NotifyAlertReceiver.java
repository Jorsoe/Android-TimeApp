package com.rfstudio.timeapp.utils;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rfstudio.timeapp.R;
import com.rfstudio.timeapp.application.MyApplication;
import com.rfstudio.timeapp.utilsModel.PlanModel;

/**
 * 定制好的通知栏通知
 */
public class NotifyAlertReceiver extends BroadcastReceiver {
    String channelId = "StartAlarmsssss"; //根据业务执行，名字任起
    String channelName = "StartAlarm conentssss"; //这个是channelid 的解释，在安装的时候会展示给用户看
    WindowAlertUtils winAlert;
    MyApplication application;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"时间到",Toast.LENGTH_LONG).show();

        application = (MyApplication)context.getApplicationContext();
        //PlanModel planItem = (PlanModel) intent.getSerializableExtra("PlanItem");
        PlanModel planItem = new PlanModel();
        planItem.setDoThing("222222222");
        // 通知栏
        notifyControl(context,planItem);

        // 弹窗
        winAlert = new WindowAlertUtils(context);
        winAlert.showWindow(getDialogView(context,planItem));

    }

    /**
     * 通知栏
     * @param context
     */
    private void notifyControl(Context context,PlanModel planItem){

        // 通知栏
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        // 构建, 应对高版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            // 定制channel
            NotificationChannel channel = new NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_HIGH);

            channel.setDescription(channelId);
            channel.enableLights(true);
            channel.setLightColor(1);
            channel.setSound(null,null);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            notificationManager.createNotificationChannel(channel);

        }
        // 定制通知栏
        Notification notification= null;


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            notification = new Notification.Builder(context,channelId)
                    .setContentTitle(application.getAppName() + " 提醒你 时间到啦！") // 标题
                    .setContentText("现在需要完成:" + planItem.getDoThing()) // 内容
                    .setSubText("时间到了!!")   // 下面的小字
                    .setTicker(application.getAppName() + "提醒你喽！") // 通知栏的状态栏标题
                    .setSmallIcon(R.drawable.ic_launcher_icon_foreground)
                    .setWhen(System.currentTimeMillis()) // 时间
                    .setShowWhen(true)
                    .setAutoCancel(true)               // 点击后自动取消
                    .build();
        }else{
            notification = new Notification.Builder(context)
                    .setContentTitle(application.getAppName() + " 提醒你 时间到啦！") // 标题
                    .setContentText("现在需要完成:" + planItem.getDoThing()) // 内容
                    .setSubText("时间到了!!")   // 下面的小字
                    .setTicker(application.getAppName() + "提醒你喽！") // 通知栏的状态栏标题
                    .setSmallIcon(R.drawable.ic_launcher_icon_foreground)
                    .setWhen(System.currentTimeMillis()) // 时间
                    .setShowWhen(true)
                    .setAutoCancel(true)               // 点击后自动取消
                    .build();
        }


        notificationManager.notify(1,notification);
    }


    /**
     * 时间到 弹窗
     * @param context
     */
    private View getDialogView(final Context context,PlanModel planItem){
        // 设置铃声/震动
        final VibrateAndRingUtil vibrateAndRingUtil = new VibrateAndRingUtil(context);
        int index = application.getRingtoneIndex();

        vibrateAndRingUtil.playBySetting(index,planItem);

        // view
        final View dialogView = View.inflate(context,R.layout.alertdialog_timeover,null);
        Button btnOk = dialogView.findViewById(R.id.btn_ok);
        Button btnCencel = dialogView.findViewById(R.id.btn_cencel);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                winAlert.removeWindowView(dialogView);
                vibrateAndRingUtil.closeAll();
                // 打开应用程序某一页
                Log.e("hahahah","windowManager.removeView(view);");
                /*
                Intent intent = new Intent(context, AlarmService.class);
                context.startService(intent);
                 */
            }
        });

        btnCencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                winAlert.removeWindowView(dialogView);
                vibrateAndRingUtil.closeAll();
                // 做些数据处理
            }
        });

        dialogView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (event.getKeyCode()){
                    case KeyEvent.KEYCODE_BACK:
                    case KeyEvent.KEYCODE_HOME:
                    case KeyEvent.KEYCODE_MENU:
                        winAlert.removeWindowView(dialogView);
                        vibrateAndRingUtil.closeAll();
                        // 去除弹出框，那么得设置一会再提醒
                        break;
                        default:return false;
                }
                return true;

            }
        });

        return dialogView;

    }







}
