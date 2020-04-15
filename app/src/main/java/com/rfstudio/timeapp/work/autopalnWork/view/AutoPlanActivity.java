package com.rfstudio.timeapp.work.autopalnWork.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.rfstudio.timeapp.R;
import com.rfstudio.timeapp.application.MyApplication;
import com.rfstudio.timeapp.databinding.ActivityAddplanAutoBinding;
import com.rfstudio.timeapp.work.homeWork.view.MainActivity;

/**
 *  系统推荐 计划项
 */
public class AutoPlanActivity extends AppCompatActivity {
    private ActivityAddplanAutoBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_addplan_auto);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_addplan_auto);
        binding.executePendingBindings();
        binding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Log.e("MainActivity","---------------------看看-----------------------------");
                Intent intent = new Intent(AutoPlanActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }
}
