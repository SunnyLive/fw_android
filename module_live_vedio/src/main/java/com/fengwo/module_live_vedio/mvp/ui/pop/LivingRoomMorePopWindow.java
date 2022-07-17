package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.ui.event.ChangeAnimateEvent;

import razerdp.basepopup.BasePopupWindow;

public class LivingRoomMorePopWindow extends BasePopupWindow {

    private ImageView btnToggleAnimate;
    private boolean isAnimate = true;

    public LivingRoomMorePopWindow(Context context, boolean defaultAnimate) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        btnToggleAnimate = findViewById(R.id.btn_toggle_animate);
        isAnimate = defaultAnimate;
        if (isAnimate) {
            btnToggleAnimate.setImageResource(R.drawable.live_ic_close_animate);
        } else {
            btnToggleAnimate.setImageResource(R.drawable.live_ic_open_animate);
        }
        btnToggleAnimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAnimate = !isAnimate;
                if (isAnimate) {
                    btnToggleAnimate.setImageResource(R.drawable.live_ic_close_animate);
                } else {
                    btnToggleAnimate.setImageResource(R.drawable.live_ic_open_animate);
                }
                RxBus.get().post(new ChangeAnimateEvent(isAnimate));
                dismiss();
            }
        });
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.live_pop_livingroom_more);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 300);
    }
}
