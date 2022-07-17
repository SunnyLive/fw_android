package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.fengwo.module_live_vedio.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/6/14
 */
public class ActTransformationPop extends BasePopupWindow {

    public ActTransformationPop(Context context, String pic, String url) {
        super(context);

        setPopupGravity(Gravity.CENTER);

        ImageView ivClose = findViewById(R.id.iv_close);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });




    }


    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.pop_act_tips_newyear);
    }
}
