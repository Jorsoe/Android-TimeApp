package com.rfstudio.timeapp.autopalnWork.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.rfstudio.timeapp.R;
import com.rfstudio.timeapp.databinding.ActivityAddplanAutoBinding;

/**
 *  系统推荐 计划项
 */
public class AutoPlanActivity extends AppCompatActivity {
    private ActivityAddplanAutoBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_addplan_auto);
        binding.executePendingBindings();
    }
}
