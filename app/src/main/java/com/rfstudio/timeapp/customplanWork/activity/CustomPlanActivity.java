package com.rfstudio.timeapp.customplanWork.activity;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.rfstudio.timeapp.R;
import com.rfstudio.timeapp.databinding.ActivityAddplanCustomBinding;

/**
 * 用户自定义 计划项
 */
public class CustomPlanActivity extends AppCompatActivity {

    private ActivityAddplanCustomBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_addplan_custom);
        binding.executePendingBindings();
    }
}
