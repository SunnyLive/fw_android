package com.fengwo.module_live_vedio.mvp.ui.activity.debug;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.fengwo.module_live_vedio.R;


public class DebugActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_activity_debug);

        findViewById(R.id.btn_tohome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DebugActivity.this,DebugHomeActivity.class);
                startActivity(i);
            }
        });
    }
}
