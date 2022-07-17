package com.fengwo.module_login.mvp.ui.pop;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;

public class CardDetailView extends BasePopupWindow {

    @BindView(R2.id.tv_choose_modify)
    View mTvChooseModify;
    @BindView(R2.id.tv_choose_delete)
    View mTvChooseDelete;
    @BindView(R2.id.tv_choose_authority)
    View mTvChooseAuthority;
    @BindView(R2.id.tv_choose_stick)
    View mTvChooseStick;
    @BindView(R2.id.tv_choose_un_stick)
    View mTvChooseUnStick;

    private boolean isStick = true;
    private int mState;
    private OnItemClickListener ol;

    public CardDetailView(Context context) {
        super(context);
        ButterKnife.bind(this, getContentView());
        setPopupGravity(Gravity.BOTTOM);
    }


    @OnClick({R2.id.tv_mine_more_cancel, R2.id.tv_choose_modify, R2.id.tv_choose_delete, R2.id.tv_choose_authority, R2.id.tv_choose_stick, R2.id.tv_choose_un_stick})
    public void onViewClick(View v) {
        dismiss();
        if (ol == null) {
            return;
        }
        //编辑
        if (v.getId() == R.id.tv_choose_modify) {
            ol.modify();
        }
        //删除
        else if (v.getId() == R.id.tv_choose_delete) {
            ol.delete();
        }
        //权限
        else if (v.getId() == R.id.tv_choose_authority) {
            ol.authority();
        }
        //置顶
        else if (v.getId() == R.id.tv_choose_stick) {
            ol.stick();
        }
        //取消置顶
        else if (v.getId() == R.id.tv_choose_un_stick) {
            ol.unStick();
        }
    }

    public boolean isStick() {
        return isStick;
    }

    public void setStick(boolean isStick) {
        this.isStick = isStick;
        requestLayout();
        //getContentView().postInvalidate();
    }


    /**
     * 动态模块：
     * 1成功           ： 置顶（取消置顶）  权限  删除  取消
     * 4草稿，5拒审     ： 编辑  删除  取消
     * 0审核中          ： 删除  取消
     * 3封禁           ： 删除  取消
     * -----------------------------------------------
     * native
     * <p>
     * void modify();
     * void delete();
     * void authority();
     * void stick();
     * void unStick();
     */
    public void chooseItem(int state) {
        mState = state;
        requestLayout();
    }

    private void requestLayout() {
        mTvChooseStick.setVisibility(View.GONE);
        mTvChooseUnStick.setVisibility(View.GONE);
        mTvChooseModify.setVisibility(View.GONE);
        mTvChooseDelete.setVisibility(View.GONE);
        mTvChooseAuthority.setVisibility(View.GONE);
        switch (mState) {
            case 0:
            case 3:
                mTvChooseDelete.setVisibility(View.VISIBLE);
                break;
            case 4:
            case 5:
                mTvChooseModify.setVisibility(View.VISIBLE);
                mTvChooseDelete.setVisibility(View.VISIBLE);
                break;
            case 1:
                if (isStick) {
                    mTvChooseStick.setVisibility(View.VISIBLE);
                } else {
                    mTvChooseUnStick.setVisibility(View.VISIBLE);
                }
                mTvChooseAuthority.setVisibility(View.VISIBLE);
                mTvChooseDelete.setVisibility(View.VISIBLE);
                break;
        }
    }


    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.dialog_card_more);
    }


    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 300);
    }

    public void setOnMoreItemClickListener(OnItemClickListener ol) {
        this.ol = ol;
    }


    public interface OnItemClickListener {
        void modify();

        void delete();

        void authority();

        void stick();

        void unStick();
    }


}
