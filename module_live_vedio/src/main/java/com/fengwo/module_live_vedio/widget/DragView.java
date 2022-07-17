package com.fengwo.module_live_vedio.widget;

/**
 * @author imac
 * @date 19/6/5
 */

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.fengwo.module_comment.utils.ScreenUtils;


/**
 * Created by aierJun on 2016/12/21.
 */
public class DragView extends RelativeLayout {
    private int _xDelta;
    private int _yDelta;
    /**
     * 最大拖动宽高距离
     */
    public int mMaxDragWidth;

    public int mMaxDragHeight ;
    /**
     * 最小拖动宽高距离
     */
    public int mMinDragWidth;

    public int mMinDragHeight;

    private boolean isDrag;

    private boolean isLongClick;
    private float DownX;
    private float DownY;

    private int mDragType;
    private int mPlayerType;

    private Object t;

    public DragView(Context context) {
        this(context,null);
    }

    public DragView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }


    public DragView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mMaxDragWidth = ScreenUtils.getScreenWidth(context);
        mMaxDragHeight = ScreenUtils.getScreenHeight(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        requestDisallowInterceptTouchEvent(true);
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                DownX = event.getRawX();
                DownY = event.getRawY();
                LayoutParams lParams = (LayoutParams) getLayoutParams();
                _xDelta = X - lParams.leftMargin;
                _yDelta = Y - lParams.topMargin;
                isDrag = false;
                isLongClick = false;
                break;
            case MotionEvent.ACTION_UP:
                Log.e("hhh","y="+event.getRawY()+"DownY="+DownY);
                if(event.getRawY()<360||event.getRawY()>1250){
                    mOnItemDragListener.onDraging(this,false);
                }else {
                    mOnItemDragListener.onDraging(this,true);
                }
            case MotionEvent.ACTION_CANCEL:
                //如果移动距离过小，则判定为点击
                if (Math.abs(event.getRawX() - DownX) <= 5 && Math.abs(event.getRawY() - DownY) <= 5) {
                    mOnItemDragListener.onClick(this, mDragType);
                } else {
                    mOnItemDragListener.onDragFinish(this, mDragType);
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:

                break;
            case MotionEvent.ACTION_MOVE:

                if (Math.abs(event.getRawX() - DownX) > 5 && Math.abs(event.getRawY() - DownY) > 5) {
                    if (!isDrag) {
                        mOnItemDragListener.onDragStart(mDragType);
                        isDrag = true;
                    } else {

                    }
                }
                int x = X - _xDelta;
                int y = Y - _yDelta;
                if (x <= mMinDragWidth) {
                    x = mMinDragWidth;
                }
                if (mMaxDragWidth < x + getWidth()) {
                    x = mMaxDragWidth - getWidth();
                }

                if (y <= mMinDragHeight) {
                    y = mMinDragHeight;
                }

                if (mMaxDragHeight < y + getHeight()) {
                    y = mMaxDragHeight - getHeight();
                }
            //    Log.e("hhhhh", "y=" + y);
                LayoutParams layoutParams = (LayoutParams) getLayoutParams();
                layoutParams.leftMargin = x;
                layoutParams.topMargin = y;
                this.setLayoutParams(layoutParams);
                break;
            default:
                break;
        }

        invalidate();
        return true;
    }


    public static boolean isCollsionWithRect(View view1, View view2) {
        int  y1, w1, h1, y2, w2, h2;
        int[] position = new int[2];
        view1.getLocationOnScreen(position);
        y1 = position[1];
        h1 = view1.getHeight();
        view2.getLocationOnScreen(position);
        y2 = position[1];
        h2 = view2.getHeight();
      if (y1 >= y2 && y1 >= y2 + h2) {//view的Y轴大于拖动距离  并且  view的Y轴大于
            return false;
        } else if (y1 <= y2 && y1 + h1 <= y2) {
            return false;
        }
        return true;
    }

    /**
     * 设置可移动区域
     *
     * @param view
     */
    public DragView setRectView(View view) {
        view.post(() -> {
            mMinDragHeight = view.getTop();
            mMaxDragHeight = mMinDragHeight + view.getMeasuredHeight();

            mMinDragWidth = view.getLeft();
            mMaxDragWidth = mMinDragWidth + view.getMeasuredWidth();
        });
        return this;
    }

    private OnViewDragListener mOnItemDragListener;

    public DragView setOnItemDragListener(OnViewDragListener onItemDragListener) {
        mOnItemDragListener = onItemDragListener;
        return this;
    }

    interface OnViewDragListener{

        void onClick( DragView dragView, int mDragType);

        void onDragFinish( DragView dragView, int mDragType);

        void onDragStart(int mDragType);

        void onDraging(DragView dragView,boolean type);
    }

}
