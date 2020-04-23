package com.rfstudio.timeapp.service;


import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.rfstudio.timeapp.application.MyApplication;
import com.rfstudio.timeapp.utils.NotifyAlertReceiver;
import com.rfstudio.timeapp.utils.WindowAlertUtils;
import com.rfstudio.timeapp.work.homeWork.view.MainActivity;

public class SystemTipService extends IntentService {

    MyApplication application;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public SystemTipService() {
        super("SystemTipService");
    }

    @Override
    public void onCreate() {
        application = (MyApplication) getApplication();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        if ( ! WindowAlertUtils.isIsOpenPermission(getBaseContext())) {
            // 提醒

            final AlertDialog.Builder builder = new AlertDialog.Builder(application.getMainContext());
            builder.setTitle("提示：");
            builder.setMessage("请为本应用打开权限，以使得弹窗提醒功能可用！");
            //builder.setCancelable(true);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    WindowAlertUtils.overlayAppPermission(getBaseContext());    // 启动浮窗权限
                }
            });
            builder.setNegativeButton("稍后", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getBaseContext(), "下次提醒!", Toast.LENGTH_LONG).show();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();



        }

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
