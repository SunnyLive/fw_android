package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.eventbus.ShowGiftEvent;

import razerdp.basepopup.BasePopupWindow;

public class ToutiaoPopwindow extends BasePopupWindow {

    public ToutiaoPopwindow(Context context) {
        super(context);
        findViewById(R.id.ivClose).setOnClickListener(v -> dismiss());
        TextView tvContent = findViewById(R.id.tvContent);
        findViewById(R.id.tvConfirm).setOnClickListener(view -> {
            RxBus.get().post(new ShowGiftEvent(ShowGiftEvent.GIFT_TYPE_TOUTIAO));
            dismiss();
        });

        tvContent.setText(Html.fromHtml(getContext().getString(R.string.live_tou_tiao)));
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.live_pop_toutiao);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getDefaultScaleAnimation(true);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getDefaultScaleAnimation(false);
    }
}
