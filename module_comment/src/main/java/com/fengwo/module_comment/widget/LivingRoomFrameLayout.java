package com.fengwo.module_comment.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.fengwo.module_comment.R;
import com.fengwo.module_comment.bean.ActBannerBean;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.widget.BannerView.act.ActBannerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LivingRoomFrameLayout extends FrameLayout {

    private List<View> vpAllView;

    private boolean isChatRv = false;
    private boolean isQuickMsgRv = false;
    private boolean isActBenner = false;
    private RecyclerView rvChat;
    private RecyclerView rvQuickMsg;
    private ActBannerView<ActBannerBean> actBanner;

    private final String TAG = "=====LivingRoomFrameLayout";


    public LivingRoomFrameLayout(@NonNull Context context) {
        super(context);
    }

    public LivingRoomFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LivingRoomFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private List<View> getAllChildViews(View view) {
        List<View> allchildren = new ArrayList<>();
        if (view instanceof ViewGroup) {
            if (view instanceof RecyclerView) {
                if (view.getId() == R.id.rv_chat)
                    rvChat = (RecyclerView) view;
                else if (view.getId() == R.id.tagQuickSdMsg) {
                    rvQuickMsg = (RecyclerView) view;
                }
            }
            if (view instanceof RelativeLayout) {
                if (view.getId() == R.id.rl_act_banner) {
                    actBanner = (ActBannerView<ActBannerBean>) ((RelativeLayout) view).getChildAt(0);
                }
            }
            ViewGroup vp = (ViewGroup) view;
            for (int i = 0; i < vp.getChildCount(); i++) {
                View viewchild = vp.getChildAt(i);
                allchildren.add(viewchild);
                //再次 调用本身（递归）
                allchildren.addAll(getAllChildViews(viewchild));
            }
        }
        return allchildren;
    }

    private int mLastX;
    public int mLastY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    private boolean isFirstToRv = true;
    private boolean isRv = false;
    private boolean isRvClick = false;

    public void setIsRvClick(boolean isRvClick) {
        this.isRvClick = isRvClick;
    }

    //(x,y)是否在view的区域内
    private boolean isTouchPointInView(View view, int x, int y) {
        if (view == null) {
            return false;
        }
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        //view.isClickable() &&
        if (y >= top && y <= bottom && x >= left
                && x <= right) {
            if (view instanceof RecyclerView) {
                return true;
            }
            if (view instanceof RelativeLayout) {
                return true;
            }
            if (view.hasOnClickListeners())
                return true;
            return false;
        }
        return false;
    }

    private Map<View, OnTouchListener> touchListeners = new HashMap<>();

    public void addOnCustomTouchListener(View view, OnTouchListener listener) {
        touchListeners.put(view, listener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {


            int x = (int) ev.getX();
            int y = (int) ev.getY();
            int action = ev.getAction();
            //fixme 这里有个隐患，事件响应的顺序是按照监听添加的顺序来的，应改成view的层级来
            View eventView = null;
            OnTouchListener listener = null;
            for (Map.Entry<View, OnTouchListener> next : touchListeners.entrySet()) {
                View view = next.getKey();
                if (isTouchPointInView(view, x, y)) {
                    if (eventView != null) {
                        if (getViewLevel(eventView) < getViewLevel(view)) {
                            eventView = view;
                            listener = next.getValue();
                        }
                    } else {
                        eventView = view;
                        listener = next.getValue();
                    }

                }
            }
            if (listener != null) {
//            Log.d("lucas","eventView:"+eventView);
                listener.onTouch(eventView, ev);
            }
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    if (CommentUtils.isListEmpty(vpAllView)) {
                        vpAllView = getAllChildViews(getChildAt(1));
                    }
                    mLastX = x;
                    isFirstToRv = true;
                    mLastY = y;
                    if (null != l) {
                        l.onTouch();
                    }
                    if (isTouchPointInView(actBanner, x, y)) {
                        isActBenner = true;
                    }
                    if (isTouchPointInView(rvChat, x, y)) {//如果选中的是 rvchat 设置标志
                        isChatRv = true;
                    }
                    if (isTouchPointInView(rvQuickMsg, x, y)) {//如果选中的是 rvchat 设置标志
                        isQuickMsgRv = true;
                    }
                case MotionEvent.ACTION_MOVE:
                    int dy = Math.abs(y - mLastY);
                    int dx = Math.abs(x - mLastX);
                    if (dy > dx) {
                        if (isFirstToRv) {
                            isFirstToRv = false;
                            ev.setAction(MotionEvent.ACTION_DOWN);
                        }
                        isRv = true;
                        if (dy > 200 && null != l) {
                            l.onVerticalScroll();
                        }
                        if (isChatRv)
                            return rvChat.dispatchTouchEvent(ev);//上下滑事件由rvchat消费
                        else if (isQuickMsgRv)
                            return rvQuickMsg.dispatchTouchEvent(ev);//上下滑事件由rvQuickMsg消费
                        return getChildAt(0).dispatchTouchEvent(ev);
                    } else {
                        isRv = false;
                        if (null != l) {
                            l.onHorivizontalScroll();
                        }
                        return getChildAt(1).dispatchTouchEvent(ev);
                    }
                case MotionEvent.ACTION_UP:
                    isChatRv = false;
                    isQuickMsgRv = false;
                    int dy1 = Math.abs(y - mLastY);
                    int dx1 = Math.abs(x - mLastX);
                    if (dy1 < 5 && dx1 < 5) {//up事件 如果移动距离小于5 认为是点击事件
                        for (int i = 0; i < vpAllView.size(); i++) {
                            if (isTouchPointInView(vpAllView.get(i), x, y)) {
                                isRv = false;
                                return getChildAt(1).dispatchTouchEvent(ev);//如果点击事件在vp上或者是vp上的rv， 则由vp上的view消费
                            }
                        }
                        isRv = true;
                        MotionEvent evMove = MotionEvent.obtain(ev);
                        MotionEvent evUp = MotionEvent.obtain(ev);
                        ev.setAction(MotionEvent.ACTION_DOWN);
                        getChildAt(0).dispatchTouchEvent(ev);
                        getChildAt(0).dispatchTouchEvent(evMove);
                        evUp.setAction(MotionEvent.ACTION_UP);
                        getChildAt(1).dispatchTouchEvent(evUp);
                        return getChildAt(0).dispatchTouchEvent(evUp);//否则构建 down move down 给 vp同级的rv，这样rv的item点击事件就能生效
                    } else {
                        if (isActBenner) {
                            isActBenner = false;
                            getChildAt(0).dispatchTouchEvent(ev);
                            return actBanner.dispatchTouchEvent(ev);
                            //       return
                        } else if (isRv) {
                            getChildAt(1).dispatchTouchEvent(ev);
                            return getChildAt(0).dispatchTouchEvent(ev);
                        } else {
                            getChildAt(0).dispatchTouchEvent(ev);
                            return getChildAt(1).dispatchTouchEvent(ev);
                        }
                    }
                default:
                    return getChildAt(1).dispatchTouchEvent(ev);
            }
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    private int getViewLevel(View eventView) {
        Object tag = eventView.getTag(R.id.live_id_view_level);
        int level = 0;
        if (tag != null) {
            level = (int) tag;
        }
        return level;
    }


    public interface OnScrollListener {
        void onVerticalScroll();

        void onHorivizontalScroll();

        void onTouch();
    }

    public static OnScrollListener l;

    public void setOnScrollListener(OnScrollListener l) {
        this.l = l;
    }

}
