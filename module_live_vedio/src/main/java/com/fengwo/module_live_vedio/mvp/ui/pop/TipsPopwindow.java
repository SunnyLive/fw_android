package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fengwo.module_live_vedio.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/6/11
 */
public class TipsPopwindow extends BasePopupWindow {

    private ImageView tv_tip_content;
    public TipsPopwindow(Context context,int content) {
        super(context);
        setPopupGravity(Gravity.CENTER);
        tv_tip_content = findViewById(R.id.tv_tip_content);
        tv_tip_content.setImageResource(content);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.pop_tips);
    }
}
