package com.fengwo.module_login.mvp.ui.pop;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.fengwo.module_login.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/6/18
 */
public class MineMorePopwindow extends BasePopupWindow {

    public MineMorePopwindow(Context context) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        TextView tvReport = findViewById(R.id.tv_report);
        TextView tvBlock = findViewById(R.id.tv_block);
        TextView tvCancel = findViewById(R.id.tv_cancel);
        tvReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (onMineMoreListener!=null){
                  onMineMoreListener.onReport();
              }
                dismiss();
            }
        });
        tvBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onMineMoreListener!=null){
                    onMineMoreListener.onBlock();
                }
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
    public View onCreateContentView() {
        return createPopupById(R.layout.pop_mine_more);
    }

    private OnMineMoreListener onMineMoreListener;

    public void setOnMineMoreListener(OnMineMoreListener l){
        this.onMineMoreListener = l;
    }

    public interface OnMineMoreListener {
        void onReport();
        void onBlock();
    }
}
