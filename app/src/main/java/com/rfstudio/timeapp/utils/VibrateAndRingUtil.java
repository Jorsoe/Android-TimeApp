package com.rfstudio.timeapp.utils;

import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

/**
 * 针对 弹窗提醒
 * 震动 与 响铃 的控制
 */
public class VibrateAndRingUtil {
    private Context context;
    AudioManager audioManager;

    public VibrateAndRingUtil(Context context) {
        this.context = context;
        audioManager = (AudioManager) context.getSystemService(Service.AUDIO_SERVICE);
    }


    public void playBySetting(){
        Log.e("audiomanager" , "看看模式:" + audioManager.getMode());
    }

}
