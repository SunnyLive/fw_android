package com.fengwo.module_comment.widget.floatingview;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import com.fengwo.module_comment.R;
import com.tencent.rtmp.ui.TXCloudVideoView;


/**
 * @ClassName EnFloatingView
 * @Description 悬浮窗
 * @Author Yunpeng Li
 * @Creation 2018/3/15 下午5:04
 * @Mender Yunpeng Li
 * @Modification 2018/3/15 下午5:04
 */
public class EnFloatingView extends FloatingMagnetView {

    private TXCloudVideoView videoView;
    private AppCompatImageView ivClose;

    public EnFloatingView(@NonNull Context context) {
        this(context, R.layout.en_floating_view);
    }

    public EnFloatingView(@NonNull Context context, @LayoutRes int resource) {
        super(context, null);
        inflate(context, resource, this);
        videoView = findViewById(R.id.video_view);
        ivClose = findViewById(R.id.close);
        ivClose.setOnClickListener((view)->{onRemove();});
    }

//    public void setIconImage(@DrawableRes int resId){
//        mIcon.setImageResource(resId);
//    }

    public TXCloudVideoView getVideoView() {
        return videoView;
    }

    public void showClose() {
       ivClose.setVisibility(View.VISIBLE);
    }



}
