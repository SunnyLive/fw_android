package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fengwo.module_live_vedio.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * @author dqm
 * 举报
 */
public class ReportPopWindow extends BasePopupWindow {
    public ReportPopWindow(Context context, int roomManager, boolean isLive, int isBlack) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        TextView tvReport = findViewById(R.id.tv_report);
        TextView tvBlock = findViewById(R.id.tv_block);
        if (isBlack == 1) {
            tvBlock.setText("已拉黑");
        } else {
            tvBlock.setText("拉黑");
        }
        TextView tvCancel = findViewById(R.id.tv_cancel);
        TextView tvForbiddenWords = findViewById(R.id.tv_forbidden_words);
        TextView tvKickOut = findViewById(R.id.tv_kick_out);
        LinearLayout llHousingManagement = findViewById(R.id.ll_housing_management);

        if (roomManager == 1) {   //是房管
            if (isLive) {
                llHousingManagement.setVisibility(View.GONE);
            } else {
                llHousingManagement.setVisibility(View.VISIBLE);
            }
        } else if (roomManager == 2) { //主播
            llHousingManagement.setVisibility(View.VISIBLE);
        } else {
            llHousingManagement.setVisibility(View.GONE);
        }
        tvKickOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onMineMoreListener != null) {
                    onMineMoreListener.onKickOut();
                }
                dismiss();
            }
        });

        tvForbiddenWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onMineMoreListener != null) {
                    onMineMoreListener.onForbiddenWords();
                }
                dismiss();
            }
        });

        tvReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onMineMoreListener != null) {
                    onMineMoreListener.onReport();
                }
                dismiss();
            }
        });
        tvBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onMineMoreListener != null) {
                    if (isBlack == 1) {
                        onMineMoreListener.onDelBlock();
                    }else
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
        return createPopupById(R.layout.pop_report);
    }

    private OnMineMoreListener onMineMoreListener;

    public void setOnMineMoreListener(OnMineMoreListener l) {
        this.onMineMoreListener = l;
    }


    public interface OnMineMoreListener {
        void onReport();

        void onBlock();

        void onForbiddenWords();

        void onKickOut();

        void onDelBlock();
    }
}
