package com.rfstudio.timeapp.utilsModel;

import android.graphics.Color;

import com.rfstudio.timeapp.utils.TimeStruct;

import java.io.Serializable;

/**
 * planmodel  顺便序列化，可由intent传输
 */
public class PlanModel implements Serializable {
    private int id;
    private String doThing;
    private TimeStruct during;

    //
    public Color color;
    public boolean isBellAlert;         // 响铃是否开启
    public boolean isShock;             // 震动是否开启
    public boolean isOpen;              // 计划是否已经打开
    public boolean isExecuted;          // 计划是否已执行

    public PlanModel() {
        this.isBellAlert = true;
        this.isShock = true;
        this.isOpen = true;
        this.isExecuted = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public TimeStruct getDuring() {
        return during;
    }

    public void setDuring(TimeStruct during) {
        this.during = during;
    }

    public String getDoThing() {
        return doThing;
    }

    public void setDoThing(String doThing) {
        this.doThing = doThing;
    }
}
