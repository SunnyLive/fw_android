package com.fengwo.module_vedio.mvp.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.fengwo.module_vedio.R;

public class TestActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vedio_fragment_home);
    }
}
