package com.rfstudio.timeapp.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.Ringtone;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.rfstudio.timeapp.application.MyApplication;

public class WindowAlertUtils {
    private MyApplication application;
    private Context context;
    private WindowManager windowManager;

    public WindowAlertUtils(Context context) {
        this.context = context;
        application = (MyApplication) context.getApplicationContext();
    }

    /**
     * 展示弹窗
     * @param view 要展示的内容
     */
    public void showWindow(View view){
        // 显示弹窗

        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(context)) {
                Intent intent1 = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //startActivityForResult(intent, 1);
                context.startActivity(intent1);
                return;
            } else {
                //TODO do something you need
                dialogWindow(view);
            }
        }else
            dialogWindow(view);
    }

    /**
     *  弹窗的设定，
     * @param view 弹窗内容view
     */
    private void dialogWindow(View view){
        int index = application.getRingtoneIndex();

        // 铃声
        RingtoneOrder ringtoneOrder = new RingtoneOrder(context);
        Ringtone ringtone = ringtoneOrder.getRingtoneList().get(index);
        ringtone.play();
        // 震动
        VibrateAndRingUtil vibrateAndRingUtil = new VibrateAndRingUtil(context);
        vibrateAndRingUtil.playBySetting();

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                0,0,
                PixelFormat.TRANSPARENT
        );

        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_DIM_BEHIND
                | WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER
                | WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;

        layoutParams.gravity = Gravity.CENTER;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        windowManager.addView(view,layoutParams);

        // 弹窗
        //alertDialogWindow(context,intent);
    }

    /**
     * 移除 windowMnager
     * @param view 要关闭的view
     */
    public void removeWindowView(View view){
        windowManager.removeView(view);
    }
}
