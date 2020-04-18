package com.rfstudio.timeapp.work.countdownWork.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.rfstudio.timeapp.R;
import com.rfstudio.timeapp.service.CountdownService;
import com.rfstudio.timeapp.service.SystemTipService;
import com.rfstudio.timeapp.utils.TimeStruct;
import com.rfstudio.timeapp.work.homeWork.view.MainActivity;


import java.io.InputStream;
import java.util.ArrayList;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;



public class CountdownActivity extends AppCompatActivity {
    // 计时器
    private CircleProgressBar mCircleProgressBar;
    // 完成按钮
    private CircularProgressButton finishButton;
    // 按钮进度条是否完成
    private boolean isProgressFinished = false;
    // 进度
    private float nowProgress = 0f;
    private long targetTimeMillis;               // 从纪元到目标时间的毫秒数,,其实为当前计划的 结束时间endTimeMillis
    private long totalMillis;                      // 倒计时时长，，从plan的timrstruct中d的 duringMillis获取
    private long remainTimeMillis ;                // 剩余时间,, 某种形式可以获取到



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e("COUNTSOWN","-------------------------------onCreate" );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown_page);

        mCircleProgressBar = findViewById(R.id.circle_progress);
        finishButton = findViewById(R.id.btn_cpb_countdown_ok);
        initefinishButton();
        // 初始关闭
        finishButton.setVisibility(View.INVISIBLE);

        // 倒计时进度
        //initeTimeParam();
        //simulateProgress(remainTimeMillis);
        simulateProgress(10*1000);

        /*
        // 注册广播
        IntentFilter intentFilter = new IntentFilter("com.countdown.receiver");
        registerReceiver(new BroadcastInCountdown(),intentFilter);

        // 启动服务
        if (! isServiceWorked(CountdownService.class.getName())){
            Log.e("CountdownServcie ","服务没有启动！！！！！！！");
           // startService(new Intent(CountdownActivity.this,CountdownService.class));
        }

         */


    }

    @Override
    protected void onRestart() {
        // remainTimeMillis  = TimeStruct.getRemainMillis(targetTimeMillis);
        super.onRestart();
    }

    /**
     * 初始化 时间相关的数据
     */
    private void initeTimeParam(){
        /////////// 测试///////////
        // 从plan中获取endTimeMillis
        targetTimeMillis = 0;
        // 总时间, 从plan中获取duringMillis
        totalMillis = 0;
        // 获取剩余时间
        remainTimeMillis  = TimeStruct.getRemainMillis(targetTimeMillis);
    }

    /**
     *  倒计时时钟的进度条
     * @param remainTime 剩余时间
     */
    private void simulateProgress(long remainTime) {
        /*
        nowProgress = (totalMillis - remainTimeMillis)/totalMillis;
        int downPart = floatToIntProgress(nowProgress);
        */
        int downPart = 0;
        ValueAnimator animator = ValueAnimator.ofInt(downPart, 100);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int progress = (int) animation.getAnimatedValue();
                mCircleProgressBar.setProgress(progress);
            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // 动画播完后
                // 弹框（或者出现按钮）提醒(并且出队)
                //
                Log.e("AAA","xhuxian;");
                finishButton.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        // 重复情况
       // animator.setRepeatCount(ValueAnimator.INFINITE);
        // 时间
        animator.setDuration(remainTime);
        animator.start();
    }

    /**
     *  设置更新 倒计时时钟 的进度
     * @param nowProgress float [0,1] d的进度
     */
    private void updateProgressClock(float nowProgress){
        // 转换
        int progress = floatToIntProgress(nowProgress);
        if (progress >=100)
            progress =100;
        finishButton.setProgress(progress);
    }

    /**
     *  完成计划任务后
     */
    private void finishPlanWork(){
        finishButton.setVisibility(View.VISIBLE);
        // 完成后可以做点其他事，比如 队列问题等
    }


    /**
     *  初始化 finishbutton
     */
    private void initefinishButton(){
        //finishButton.startAnimation();
        // 结束图片
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //finishButton.setProgress(50);
                finishButton.startMorphAnimation();
                finishButtonAnimator((long) (1.5*1000));

            }
        });
    }

    /**
     * finishbutton d的动画控制
     */
    private void finishButtonAnimator(final Long timeMillis){
        final ValueAnimator btnAnimator = ValueAnimator.ofInt(0,100);
        btnAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int progress = (int) animation.getAnimatedValue();
                finishButton.setProgress(progress);
                if (progress == 100){
                    //btnAnimator.end();
                    // 图片
                    Resources r = CountdownActivity.this.getResources();
                    @SuppressLint("ResourceType") InputStream is = r.openRawResource(R.drawable.ic_done_white_48dp);
                    BitmapDrawable bmpDraw = new BitmapDrawable(is);
                    Bitmap bmp = bmpDraw.getBitmap();
                    // 结束图案
                    finishButton.doneLoadingAnimation(Color.GREEN,bmp);

                    // 可以弹窗了,push**********************************************************
                    addAlert();
                }
            }
        });

        btnAnimator.setDuration(timeMillis);
        btnAnimator.start();
    }

    /**
     *  添加弹窗
     */
    private void addAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("任务完成")
                .setMessage("再接再厉哟！！")
                .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 除了跳转 还要对队列做点什么

                        startActivity(new Intent(CountdownActivity.this,MainActivity.class));
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_SEARCH)
                {
                    return true;
                }
                else
                {
                    return false; //默认返回 false
                }
            }
        });
        alertDialog.show();

        alertDialog.show();
    }

    /**
     *  service 是否正在运行
     * @param className
     * @return
     */
    private boolean isServiceWorked(String className){
        if (className.isEmpty())
            return false;
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningServiceInfos = (ArrayList<ActivityManager.RunningServiceInfo>) activityManager.getRunningServices(30);
        for (ActivityManager.RunningServiceInfo running : runningServiceInfos
             ) {
            String name = running.service.getClassName();
            if (name.equals(name)){
                return true;
            }
        }

        return false;
    }

    /**
     *  将 float 进度 转换为可设置进度的 int类型
     * @param progress
     * @return  返回 转换好的 int
     */
    private int floatToIntProgress(float progress){
        int result = (int) progress * 100;
        return result;
    }

    @Override
    protected void onDestroy() {
        Log.e("COUNTDOWN","------------------------onDestory");
        super.onDestroy();
    }

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
        moveTaskToBack(false);

    }


}
