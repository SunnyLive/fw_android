package com.fengwo.module_flirt.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_flirt.R;

import razerdp.basepopup.BasePopupWindow;

public class AudioPlayGif extends BasePopupWindow {



    public AudioPlayGif(Context context) {
        super(context);
        setPopupGravity(Gravity.CENTER);
        ImageLoader.loadGif(findViewById(R.id.iv_gif_play), R.drawable.voice_playing);
    }


    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.layout_audio_gif);
    }
}
