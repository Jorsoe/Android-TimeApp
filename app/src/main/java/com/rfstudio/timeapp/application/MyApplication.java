package com.rfstudio.timeapp.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.rfstudio.timeapp.utilsModel.PlanModel;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {
    private ArrayList<PlanModel> planLists;      // PlanList

    private Context context;        // 存储本应用的上下文

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("aplci" ,"application 初见了:" + getBaseContext().getExternalFilesDir(null).getPath());
        context = getBaseContext();

    }

    public ArrayList<PlanModel> getPlanLists() {
        return planLists;
    }

    public void setPlanLists(ArrayList<PlanModel> planLists) {
        this.planLists = planLists;
    }

    public Context getContext() {
        return context;
    }

}
