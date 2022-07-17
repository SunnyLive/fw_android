package com.fengwo.module_comment.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.fengwo.module_comment.R;

import razerdp.basepopup.BasePopupWindow;

public class TakePicPopwindow extends BasePopupWindow implements View.OnClickListener {

    private View btnTakepic, btnChoosePic, btnCancle,view;
    private TextView mTvDelete;
    OnTakePopwindowClickListener l;

    public void setOnTakePopwindowClickListener(OnTakePopwindowClickListener l) {
        this.l = l;
    }

    public interface OnTakePopwindowClickListener {
        void onTakeClick();

        void onChooseClick();

        void onDeleteClick();

    }


    /**
     * @param from   1:照片墙  0
     */
    public TakePicPopwindow(Context context,int from) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        btnTakepic = findViewById(R.id.btn_takepic);
        btnChoosePic = findViewById(R.id.btn_choosepic);
        btnCancle = findViewById(R.id.btn_cancle);
        view = findViewById(R.id.view);
        mTvDelete = findViewById(R.id.tv_delete);
        mTvDelete.setOnClickListener(this);
        btnTakepic.setOnClickListener(this);
        btnChoosePic.setOnClickListener(this);
        btnCancle.setOnClickListener(this);
        if(from == 0){
            view.setVisibility(View.GONE);
            mTvDelete.setVisibility(View.GONE);
        }else{
            view.setVisibility(View.VISIBLE);
            mTvDelete.setVisibility(View.VISIBLE);
        }



    }

    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.popwindow_takepic);
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
                l.onTakeClick();
            }
        } else if (id == R.id.btn_choosepic) {
            if (null != l) {
                l.onChooseClick();
            }

        } else if (id == R.id.btn_cancle) {
            dismiss();
        } else if(id == R.id.tv_delete){
            if (null != l) {
                l.onDeleteClick();
                dismiss();
            }
        }
    }
}
