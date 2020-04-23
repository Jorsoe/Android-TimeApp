package com.rfstudio.timeapp.work.homeWork.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ActionMode;
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
import com.rfstudio.timeapp.service.AlarmService;
import com.rfstudio.timeapp.service.SystemTipService;
import com.rfstudio.timeapp.utils.FileManager;
import com.rfstudio.timeapp.utils.PlanListUtils;
import com.rfstudio.timeapp.utils.TimeStruct;
import com.rfstudio.timeapp.utilsModel.PlanModel;
import com.rfstudio.timeapp.work.countdownWork.view.CountdownActivity;
import com.rfstudio.timeapp.work.customplanWork.view.CustomPlanActivity;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String FLAG = "MainActivity";
    private AppBarConfiguration mAppBarConfiguration;
    MyApplication application;
    // 右上角的图标
    MenuItem fillingItem;
    // 是否有正在执行的计划,在start中修改，主要用于控制 右上角图标的显示
    private boolean isHaveDoingPlan = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(FLAG,"--------------------------------------------------oncreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        application = (MyApplication)getApplication();
        application.setMainContext(this);

        // 开启提醒服务
        startService(new Intent(this, SystemTipService.class));

        orangialIniteView();

        ////////////////////测试//////////////////////////////////////////

        ArrayList<PlanModel> planList = new ArrayList<PlanModel>();
        for (int i = 0 ; i< 5 ; i++){
            PlanModel planItem = new PlanModel();
            planItem.setDoThing("dothing+" + i );
            TimeStruct t = new TimeStruct("sTime :" + i , "eTime" + i);
            planItem.setDuring(t);

            planList.add(planItem);
        }

        // 写入list到文件
        //FileManager.addPlanListInXmlFile(planList,FileManager.savefileName,getApplicationContext());

        Log.e("看看填写 : " , TimeStruct.timeToString(null,null,"12"));

        Intent intent = new Intent(this, AlarmService.class);
       // startService(intent);

        application = (MyApplication)getApplication();
        application.setPlanLists(FileManager.getPlanlistInXmlFile(FileManager.savefileName,getBaseContext()));

        //startActivity(new Intent(this, CowndownActivity.class));


    }

    private void orangialIniteView(){

        // 原生创建view
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_countdown:
                        // 跳转即可
                        startActivity(new Intent(MainActivity.this,CountdownActivity.class));
                        break;
                }
                return true;
            }
        });
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 最后执行
        Log.e(FLAG,"-------------------onCreateOptionMenu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        fillingItem = menu.findItem(R.id.action_countdown);
        if (isHaveDoingPlan)
            fillingItem.setVisible(true);
        else
            fillingItem.setVisible(false);
        return true;
    }


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
                /*
                Intent intent = new Intent(MainActivity.this, AutoPlanActivity.class);
                startActivity(intent);

                 */
                // 非重新创建模式启动
                Intent intent = new Intent(MainActivity.this, CountdownActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
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
     //   Log.e(FLAG,"--------------------------------------------------onSupportNavigateUp");
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onRestart() {
        Log.e(FLAG,"--------------------------------------------------onRestart");
        // 开启提醒服务
        startService(new Intent(this, SystemTipService.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

        super.onRestart();


    }


    @Override
    protected void onStart() {
        Log.e(FLAG,"--------------------------------------------------onStart");
        // 这里获取 nowplan （真）
        //application.setNowPlanItem(PlanListUtils.getNowPlanItem(application.getPlanLists())); //可能
        /*
        if (application.getNowPlanItem() != null)
            isHaveDoingPlan = true;
        else
            isHaveDoingPlan = false;

         */


        isHaveDoingPlan = true;
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.e(FLAG,"--------------------------------------------------onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.e(FLAG,"--------------------------------------------------onDestory");
        super.onDestroy();
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        Log.e(FLAG,"--------------------------------------------------onTitleChanged");
        super.onTitleChanged(title, color);
    }

    @Override
    public void onSupportActionModeStarted(@NonNull ActionMode mode) {
        Log.e(FLAG,"--------------------------------------------------onSupportActionModeStarted");
        super.onSupportActionModeStarted(mode);
    }

    @Override
    public void onSupportActionModeFinished(@NonNull ActionMode mode) {
        Log.e(FLAG,"--------------------------------------------------onSupportActionModeFisished");
        super.onSupportActionModeFinished(mode);
    }

    @Override
    public void onContentChanged() {
        Log.e(FLAG,"--------------------------------------------------onContentChanged");
        super.onContentChanged();
    }

    @Override
    public void onPanelClosed(int featureId, @NonNull Menu menu) {
        Log.e(FLAG,"--------------------------------------------------onPanelClosed");
        super.onPanelClosed(featureId, menu);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Log.e(FLAG,"--------------------------------------------------onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onNightModeChanged(int mode) {
        Log.e(FLAG,"--------------------------------------------------onNightModeChanged");
        super.onNightModeChanged(mode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.e(FLAG,"--------------------------------------------------onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        Log.e(FLAG,"--------------------------------------------------onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.e(FLAG,"--------------------------------------------------onPause");
        super.onPause();
    }


}
