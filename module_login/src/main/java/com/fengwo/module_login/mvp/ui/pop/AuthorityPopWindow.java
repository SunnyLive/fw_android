package com.fengwo.module_login.mvp.ui.pop;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;

import butterknife.ButterKnife;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;

public class AuthorityPopWindow extends BasePopupWindow {

    private onItemChooseListener ml;

    public void setOnItemChooseListener(onItemChooseListener ml){
        this.ml = ml;
    }

    public AuthorityPopWindow(Context context) {
        super(context);
        ButterKnife.bind(this, getContentView());
        setPopupGravity(Gravity.BOTTOM);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.dialog_authority);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 300);
    }

    //	integer($int32)
    //权限状态 0 所有人可见 1 仅自己可见 2 仅好友可见
    @OnClick({R2.id.tv_mine_more_cancel, R2.id.tv_choose_all, R2.id.tv_choose_private, R2.id.tv_choose_friend})
    public void onViewClick(View v) {
        dismiss();
        if (ml == null) {
            return;
        }
        if (v.getId() == R.id.tv_choose_all) {
            ml.onChoose(0);
        }
        else if (v.getId() == R.id.tv_choose_private){
            ml.onChoose(1);
        }else if (v.getId() == R.id.tv_choose_friend){
            ml.onChoose(2);
        }
    }


    public interface onItemChooseListener{
        void onChoose(int state);
    }


}
