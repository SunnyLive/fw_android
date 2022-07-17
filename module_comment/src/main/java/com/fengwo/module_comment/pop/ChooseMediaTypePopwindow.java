package com.fengwo.module_comment.pop;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import com.fengwo.module_comment.R;

import razerdp.basepopup.BasePopupWindow;

public class ChooseMediaTypePopwindow extends BasePopupWindow implements View.OnClickListener {

    public View btnTakepic, btnChoosePic, btnCancle,mLlVideoColumns;
    OnChooseClickListener l;

    public void setOnTakePopwindowClickListener(OnChooseClickListener l) {
        this.l = l;
    }

    public interface OnChooseClickListener {
        void onImgClick();

        void onVideoClick();
    }


    public ChooseMediaTypePopwindow(Context context) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        mLlVideoColumns = findViewById(R.id.ll_video_columns);
        btnTakepic = findViewById(R.id.btn_takepic);
        btnChoosePic = findViewById(R.id.btn_choosepic);
        btnCancle = findViewById(R.id.btn_cancle);
        btnTakepic.setOnClickListener(this);
        btnChoosePic.setOnClickListener(this);
        btnCancle.setOnClickListener(this);
    }

    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.popwindow_choose_media);
        return v;
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 500);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 500);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_takepic) {
            if (null != l) {
                l.onVideoClick();
            }
            dismiss();
        } else if (id == R.id.btn_choosepic) {
            if (null != l) {
                l.onImgClick();
            }
            dismiss();
        } else if (id == R.id.btn_cancle) {
            dismiss();
        }
    }
}
