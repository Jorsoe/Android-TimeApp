package com.rfstudio.timeapp.work.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rfstudio.timeapp.R;
import com.rfstudio.timeapp.application.MyApplication;
import com.rfstudio.timeapp.utils.FileManager;
import com.rfstudio.timeapp.utils.PlanListUtils;
import com.rfstudio.timeapp.utilsModel.ConfigInfoModel;
import com.rfstudio.timeapp.work.homeWork.view.MainActivity;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGHT=2000;   // 2秒启动
    private Handler handler;                        // 线程 用于延迟跳转

    MyApplication application;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 构建Config
        ConfigInfoModel configInfoModel = FileManager.buildConfigInfoModel(FileManager.configfileName,getApplicationContext());

        // 引导页的启动
        if (configInfoModel.isNewAccount){  // 是新用户
            Toast.makeText(getApplicationContext(),"新用户："+configInfoModel.isNewAccount ,Toast.LENGTH_LONG).show();

            // 要跳转到引导页
        }

        application = (MyApplication)getApplication();

        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        // 实现启动上次页面
        if (! isTaskRoot()){
            finish();
            return;
        }


        // 延时跳转 main
        handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 处理appliction
                //initeApplicaiton();

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                // 销毁
                SplashActivity.this.finish();

            }
        },SPLASH_DISPLAY_LENGHT );

    }

    /**
     * 初始化 apllication 的数据
     * 获取 planlist
     */
    private void initeApplicaiton(){
        // 文件中获取 planlist
        application.setPlanLists(
                FileManager.getPlanlistInXmlFile(FileManager.savefileName,this)
        );
        // 排序planlist
        application.setPlanLists(PlanListUtils.sortList(application.getPlanLists()));
    }

    /**
     *  返回无效
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        return ;
    }

}
