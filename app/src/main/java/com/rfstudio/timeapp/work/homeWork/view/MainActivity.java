package com.rfstudio.timeapp.work.homeWork.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.rfstudio.timeapp.R;
import com.rfstudio.timeapp.application.MyApplication;
import com.rfstudio.timeapp.utils.FileManager;
import com.rfstudio.timeapp.utils.TimeStruct;
import com.rfstudio.timeapp.utilsModel.ConfigInfoModel;
import com.rfstudio.timeapp.utilsModel.PlanModel;
import com.rfstudio.timeapp.work.autopalnWork.view.AutoPlanActivity;
import com.rfstudio.timeapp.work.customplanWork.view.CustomPlanActivity;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    MyApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listenFab();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        // 泵没有这个home
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,R.id.nav_setting,R.id.nav_about)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        ArrayList<PlanModel> planList = new ArrayList<PlanModel>();
        for (int i = 0 ; i< 5 ; i++){
            PlanModel planItem = new PlanModel();
            planItem.setDoThing("dothing+" + i );
            planItem.setId(i);
            TimeStruct t = new TimeStruct("sTime :" + i , "eTime" + i);
            planItem.setDuring(t);

            planList.add(planItem);
        }

        // 写入list到文件
        //FileManager.addPlanListInXmlFile(planList,FileManager.savefileName,getApplicationContext());
        // 构建Config
        ConfigInfoModel configInfoModel = FileManager.buildConfigInfoModel(FileManager.configfileName,getApplicationContext());

        Toast.makeText(getApplicationContext(),"新用户："+configInfoModel.isNewAccount ,Toast.LENGTH_LONG).show();

        Log.e("看看填写 : " , TimeStruct.timeToString(null,null,"12"));




    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

     */

    /**
     * 监听悬浮按钮事件
     * @return
     */
    private void listenFab(){
        FloatingActionButton fab1 = findViewById(R.id.fab_1);
        FloatingActionButton fab2 = findViewById(R.id.fab_2);

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AutoPlanActivity.class);
                startActivity(intent);
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CustomPlanActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
