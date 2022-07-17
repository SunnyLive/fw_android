package com.fengwo.module_vedio.mvp.ui.pop;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.fengwo.module_comment.base.BasePopWindow;
import com.fengwo.module_vedio.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/1/21
 */
public class DeleteVideoPop extends BasePopupWindow {

    private Context context;
    private ImageView ivDelete;
    private TextView tvCancel;

    public DeleteVideoPop(Context context,onDeleteListener onDeleteListener) {
        super(context);
        this.context = context;
        setPopupGravity(Gravity.BOTTOM);
        ivDelete = findViewById(R.id.iv_delete);
        tvCancel = findViewById(R.id.tv_cancel);
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteListener.onDelete();
                dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

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
        return createPopupById(R.layout.pop_delete_video);
    }


    public interface onDeleteListener{
        void onDelete();
    }


}
