package com.rfstudio.timeapp.utils;

import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.os.Vibrator;
import android.util.Log;

import com.rfstudio.timeapp.utilsModel.PlanModel;

/**
 * 针对 弹窗提醒
 * 震动 与 响铃 的控制
 */
public class VibrateAndRingUtil {
    private Context context;
    AudioManager audioManager;
    private Ringtone ringtone;
    private Vibrator vibrator;

    public VibrateAndRingUtil(Context context) {
        this.context = context;
        audioManager = (AudioManager) context.getSystemService(Service.AUDIO_SERVICE);
    }


    public void playBySetting(int index, PlanModel planItem){
        Log.e("audiomanager" , "看看模式:" + audioManager.getMode());
        if (WindowAlertUtils.isIsOpenPermission(context)){
            // 只有开启权限 才能有提示音
            if (planItem.isBellAlert)
                playRing(index);
            if (planItem.isShock)
                playVibrate();
        }

    }

    /**
     *  播放铃声
     * @param index
     */
    public void playRing(int index){
        RingtoneOrder ringtoneOrder = new RingtoneOrder(context);
        ringtone = ringtoneOrder.getRingtoneList().get(index);
        ringtone.play();
    }
    public void closeRing(){
        ringtone.stop();
    }

    /**
     * 播放振动
     */
    public void playVibrate(){
        vibrator = (Vibrator)context.getSystemService(Service.VIBRATOR_SERVICE);

        long[] patter = {1000,2000,1000};
        vibrator.vibrate(patter, 0);


    }
    public void closeVibrate(){
        vibrator.cancel();
    }

    /**
     * 关闭 响铃和振动
     */
    public void closeAll(){
        closeRing();
        closeVibrate();
    }

}
