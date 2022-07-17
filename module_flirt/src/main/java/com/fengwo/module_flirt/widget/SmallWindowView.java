package com.fengwo.module_flirt.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.fengwo.module_comment.utils.ScreenUtils;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.tencent.rtmp.ui.TXCloudVideoView;

import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 小窗口
 *
 * @Author BLCS
 * @Time 2020/3/31 11:08
 */
public class SmallWindowView extends FrameLayout {

    @BindView(R2.id.video_small_play)
    TXCloudVideoView videoSmallPlay;
    private float x;
    private float y;
    private int viewWidth;
    private int viewHeight;
    private int screenWidth;
    private int screenHeight;
    private int bottomHeight;

    public SmallWindowView(Context context) {
        super(context);
        init();
    }

    public SmallWindowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        screenWidth = ScreenUtils.getScreenWidth(getContext());
        screenHeight = ScreenUtils.getScreenHeight(getContext());
        viewWidth = screenWidth / 3;
        viewHeight = screenHeight / 4;

        setBackgroundColor(Color.BLACK);
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.include_samll_window, this, true);
        ButterKnife.bind(this, inflate);

        videoSmallPlay.setLayoutParams(new LayoutParams(viewWidth, viewHeight));

    }

    public View getVideoView() {
        return videoSmallPlay;
    }

    @OnClick({R2.id.iv_small_change, R2.id.btn_hide})
    public void viewClick(View v) {
        if (v.getId() == R.id.iv_small_change) {
            //隐藏小窗口
            setVisibility(GONE);
            //显示弹窗
            if (onSmallWindowClickListener != null) {
                onSmallWindowClickListener.clickSwitch();
            }
        } else if (v.getId() == R.id.btn_hide) {
            setVisibility(GONE);
            if (onSmallWindowClickListener != null) {
                onSmallWindowClickListener.clickHide();
            }
        }

    }

    public interface OnSmallWindowClickListener {
        void clickSwitch();
        void clickHide();
    }

    public OnSmallWindowClickListener onSmallWindowClickListener;

    public void addClickSwith(OnSmallWindowClickListener listener) {
        onSmallWindowClickListener = listener;
    }

    int mLastX = 0;
    int mLastY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getRawX(); //触摸点相对于屏幕的横坐标
        int y = (int) event.getRawY(); //触摸点相对于屏幕的纵坐标
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE: //当手势类型为移动时
                int deltaX = x - mLastX; //两次移动的x距离差
                int deltaY = y - mLastY;//两次移动的y的距离差
                //重新设置此view相对父容器的偏移量
                int translationX = (int) getTranslationX() + deltaX;
                int translationY = (int) getTranslationY() + deltaY;
                float left = x - event.getX() + deltaX; //左边距
                float top = y - event.getY() + deltaY; //上边距
                if (left > 0 && left < screenWidth - viewWidth) {
                    setTranslationX(translationX);
                }
                if (top > 0 && top < screenHeight - viewHeight + bottomHeight) {
                    setTranslationY(translationY);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        //记录上一次移动的坐标
        mLastX = x;
        mLastY = y;
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取宽-测量规则的模式和大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        // 获取高-测量规则的模式和大小
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 设置wrap_content的默认宽 / 高值
        // 默认宽/高的设定并无固定依据,根据需要灵活设置
        // 类似TextView,ImageView等针对wrap_content均在onMeasure()对设置默认宽 / 高值有特殊处理,具体读者可以自行查看
        int mWidth = viewWidth;
        int mHeight = viewHeight;
        // 当布局参数设置为wrap_content时，设置默认值
        if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT && getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(mWidth, mHeight);
            // 宽 / 高任意一个布局参数为= wrap_content时，都设置默认值
        } else if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(mWidth, heightSize);
        } else if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(widthSize, mHeight);
        }
    }

}
