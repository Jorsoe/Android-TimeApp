package com.rfstudio.timeapp.utils;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;


import java.util.ArrayList;

/**
 * 用于获取系统铃声的类
 * 无 静态属性与方法
 */
public class RingtoneOrder {

    private ArrayList<Ringtone> ringtoneList;       // 存储这所有 Ringtone 的list
    private int count;                              // 铃声列表数量
    private ArrayList<Uri> uriList;                 // 音量源uri


    public RingtoneOrder(Context context) {
        this.ringtoneList = new ArrayList<Ringtone>();
        this.uriList = new ArrayList<Uri>();

        RingtoneManager ringtoneManager = new RingtoneManager(context);
        this.count = ringtoneManager.getCursor().getCount();


        for (int i=0 ; i<count ; i++){
            ringtoneList.add(ringtoneManager.getRingtone(i));
            uriList.add(ringtoneManager.getRingtoneUri(i));
        }

    }

    public ArrayList<Ringtone> getRingtoneList() {
        return ringtoneList;
    }

    public int getCount() {
        return count;
    }

    public ArrayList<Uri> getUriList() {
        return uriList;
    }
}
