package com.fengwo.module_chat.mvp.ui.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;
import com.fengwo.module_chat.mvp.model.bean.ChatCardBean;

import androidx.annotation.DimenRes;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import razerdp.basepopup.BasePopupWindow;

public class RePushPopWindow extends BasePopupWindow {

    private Unbinder bind;

    private ChatCardBean chatCardBean;

    public RePushPopWindow(Context context, ChatCardBean chatCardBean) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        this.chatCardBean = chatCardBean;
    }

    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.popwindow_chat_card_repush);
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

    @OnClick({R2.id.tvRePush, R2.id.tvDelete, R2.id.cancel})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.tvRePush) {
            if (listener != null)
                listener.cardRePush();
            dismiss();
        } else if (id == R.id.tvDelete) {
            if (listener != null)
                listener.cardDelete();
        } else if (id == R.id.cancel) {
            dismiss();
        }
    }

    public interface OnSelectListener {
        void cardRePush();

        void cardDelete();
    }

    public OnSelectListener listener;

    public void addOnClickListener(OnSelectListener listener) {
        this.listener = listener;
    }
}
