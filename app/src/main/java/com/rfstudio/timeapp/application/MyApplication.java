package com.rfstudio.timeapp.application;

import android.app.Application;

import com.rfstudio.timeapp.utilsModel.PlanModel;

import java.util.List;

public class MyApplication extends Application {
    private List<PlanModel> planLists;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public List<PlanModel> getPlanLists() {
        return planLists;
    }

    public void setPlanLists(List<PlanModel> planLists) {
        this.planLists = planLists;
    }
}
