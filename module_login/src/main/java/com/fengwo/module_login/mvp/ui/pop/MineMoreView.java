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

public class MineMoreView extends BasePopupWindow {


    public MineMoreView(Context context) {
        super(context);
        ButterKnife.bind(this, getContentView());
        setPopupGravity(Gravity.BOTTOM);
    }

    private MineMoreItemClickListener mListener;

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 300);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.layout_mine_more);
    }

    @OnClick({R2.id.tv_mine_more_modify, R2.id.tv_mine_more_del, R2.id.tv_mine_more_cancel})
    public void onViewClick(View v) {
        //编辑
        if (v.getId() == R.id.tv_mine_more_modify) {
            if (mListener != null) {
                mListener.modify();
            }
        }
        //删除
        else if(v.getId() == R.id.tv_mine_more_del){
            if (mListener != null) {
                mListener.delete();
            }
        }
        dismiss();
    }

    public void setOnItemClickListener(MineMoreItemClickListener mListener) {
        this.mListener = mListener;
    }



    public interface MineMoreItemClickListener{
        void modify();
        void delete();
    }


}
