package com.fengwo.module_live_vedio.mvp.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.ui.activity.TeenagerModeSettingActivity;

import razerdp.basepopup.BasePopupWindow;

/**
 * 青少年模式 24小时 提示一次
 *
 * @Author gukaihong
 * @Time 2020/12/8
 */
public class TeenagerModeTipsPopup extends BasePopupWindow {

    public interface OnClickListen{
        void onClick();
    }

    public TeenagerModeTipsPopup(Context context,OnClickListen onClickListen) {
        super(context);
        findViewById(R.id.tv_sure).setOnClickListener(v -> dismiss());
        findViewById(R.id.tv_teenager_tips).setOnClickListener(v -> {
            onClickListen.onClick();
            dismiss();
        });
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.main_pop_teenager_mode_tips);
    }
}
