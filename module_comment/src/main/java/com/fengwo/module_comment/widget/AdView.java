package com.fengwo.module_comment.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fengwo.module_comment.R;
import com.zhy.autolayout.utils.AutoUtils;

public class AdView extends FrameLayout {

    private ImageView iv;
    private View close;

    public AdView(@NonNull Context context) {
        this(context, null);
    }

    public AdView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public AdView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View v = LayoutInflater.from(context).inflate(R.layout.view_ad, this, true);
        AutoUtils.autoSize(v);
        iv = v.findViewById(R.id.iv);
        v.findViewById(R.id.tv_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibility(GONE);
            }
        });
    }

    public void setImgUrl(String url){

    }
}
