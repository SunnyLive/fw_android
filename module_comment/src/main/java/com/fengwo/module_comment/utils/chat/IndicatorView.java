package com.fengwo.module_comment.utils.chat;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.fengwo.module_comment.R;
import com.fengwo.module_comment.utils.DensityUtils;

import java.util.ArrayList;

/**
 * ================================================
 * 作    者：Fuzp
 * 版    本：1.0
 * 创建日期：2018/11/15.
 * 描    述：聊天底部片段页面指示器
 * 修订历史：
 * ================================================
 */
public class IndicatorView extends LinearLayout {
    private Context mContext;
    private ArrayList<View> mImageViews;//所有指示器集合
    private int size = 6;
    private int marginSize = 15;
    private int pointSize;//指示器的大小
    private int marginLeft;//间距

    public IndicatorView(Context context) {
        this(context, null);
    }

    public IndicatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        pointSize = DensityUtils.dp2px(context,size);
        marginLeft = DensityUtils.dp2px(context,marginSize);
    }

    /**
     * 初始化指示器
     * @param count 指示器的数量
     */
    public void initIndicator(int count){
        mImageViews = new ArrayList<>();
        this.removeAllViews();
        LayoutParams lp;
        for (int i = 0; i < count; i++) {
            View v = new View(mContext);
            lp = new LayoutParams(pointSize, pointSize);
            if (i != 0)
                lp.leftMargin = marginLeft;
            v.setLayoutParams(lp);
            if (i == 0) {
                v.setBackgroundResource(R.drawable.bg_circle_white);
            } else {
                v.setBackgroundResource(R.drawable.bg_circle_gary);
            }
            mImageViews.add(v);
            this.addView(v);
        }
    }

    /**
     * 页面移动时切换指示器
     */
    public void playByStartPointToNext(int startPosition, int nextPosition) {
        if (startPosition < 0 || nextPosition < 0 || nextPosition == startPosition) {
            startPosition = nextPosition = 0;
        }
        final View ViewStart = mImageViews.get(startPosition);
        final View ViewNext = mImageViews.get(nextPosition);
        ViewNext.setBackgroundResource(R.drawable.bg_circle_white);
        ViewStart.setBackgroundResource(R.drawable.bg_circle_gary);
    }
}
