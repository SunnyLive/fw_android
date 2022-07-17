package com.fengwo.module_chat.mvp.ui.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import razerdp.basepopup.BasePopupWindow;

/**
 * 所有人/仅自己/仅好友
 */
public class ChatCardPowerPopwindow extends BasePopupWindow {

    private Unbinder bind;

    public ChatCardPowerPopwindow(Context context) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        initUI();
    }

    private void initUI() {

    }

    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.popwindow_chat_card_setting_power);
        bind = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bind != null) bind.unbind();
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 200);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 200);
    }

    @OnClick({R2.id.tvPowerAll, R2.id.tvPowerMine, R2.id.tvPowerFriends, R2.id.cancel})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.tvPowerAll) {
            if (listener != null)
                listener.powerStatus(0);
        } else if (id == R.id.tvPowerMine) {
            if (listener != null)
                listener.powerStatus(1);
        } else if (id == R.id.tvPowerFriends) {
            if (listener != null)
                listener.powerStatus(2);
        } else if (id == R.id.cancel) {
            dismiss();
        }
    }

    public interface OnSelectListener {
        void powerStatus(int status);
    }

    public OnSelectListener listener;

    public void addOnClickListener(OnSelectListener listener) {
        this.listener = listener;
    }
}
