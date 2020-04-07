package com.rfstudio.timeapp.utilsModel;

import android.graphics.Color;

import com.rfstudio.timeapp.utils.TimeStruct;

public class PlanModel {
    private String occupation;
    private TimeStruct startTime;
    private TimeStruct endTime;
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

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public TimeStruct getStartTime() {
        return startTime;
    }

    public void setStartTime(TimeStruct startTime) {
        this.startTime = startTime;
    }

    public TimeStruct getEndTime() {
        return endTime;
    }

    public void setEndTime(TimeStruct endTime) {
        this.endTime = endTime;
    }
}
