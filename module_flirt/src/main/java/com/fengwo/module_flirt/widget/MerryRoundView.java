package com.fengwo.module_flirt.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_flirt.Interfaces.OnCarrouselItemClickListener;
import com.fengwo.module_flirt.Interfaces.OnCarrouselItemSelectedListener;
import com.fengwo.module_flirt.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

/**
 * 旋转木马布局
 * Created by dalong on 2016/11/12.
 */

public class MerryRoundView extends RelativeLayout {

    private Context mContext;
    //自动旋转 默认不自动
    private boolean mAutoRotation;

    //旋转间隔时间  默认设置为2秒
    private int mRotationTime;

    //旋转木马旋转半径  圆的半径
    private float mCarrouselR;

    //camera和旋转木马距离
    private float mDistance = 2f * mCarrouselR;

    //旋转方向 分0顺时针和 1逆时针 俯视旋转木马看
    private int mRotateDirection;

    //handler
    private MerryRoundHandler mHandler;

    //手势处理
    private GestureDetector mGestureDetector;

    //x旋转
    private int mRotationX = -130;

    //Z旋转
    private int mRotationZ = 0;

    //旋转的角度
    private float mAngle = 0;
    private float mAngles = 0;
    //旋转木马子view
    private List<View> mCarrouselViews = new ArrayList<>();

    //旋转木马子view的数量
    private int viewCount;

    //半径扩散动画
    private ValueAnimator mAnimationR;

    //记录最后的角度 用来记录上一次取消touch之后的角度
    private float mLastAngle;

    //是否在触摸
    private boolean isTouching;

    //旋转动画
    private ValueAnimator restAnimator;

    //选中item
    private int selectItem;

    //item选中回调接口
    private OnCarrouselItemSelectedListener mOnCarrouselItemSelectedListener;

    //item点击回调接口
    private OnCarrouselItemClickListener mOnCarrouselItemClickListener;

    //x轴旋转动画
    private ValueAnimator xAnimation;

    //z轴旋转动画
    private ValueAnimator zAnimation;
    //触摸是否可以上下滑动
    private boolean isTB = true;


    public String my_tag = "context";


    int mRotationSpeed = 100;//旋转速度
    public boolean isture = false;


    public MerryRoundView(Context context) {
        this(context, null);
    }

    public MerryRoundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MerryRoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);


    }


    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MerryRoundView);
        mAutoRotation = typedArray.getBoolean(R.styleable.MerryRoundView_autoRotation, false);
        mRotationTime = typedArray.getInt(R.styleable.MerryRoundView_rotationTime, 2000);
        mCarrouselR = typedArray.getDimension(R.styleable.MerryRoundView_r, 540);
        mRotateDirection = typedArray.getInt(R.styleable.MerryRoundView_rotateDirection, 0);
        typedArray.recycle();
        mGestureDetector = new GestureDetector(context, getGestureDetectorController());
        initHandler();
    }


    /**
     * 初始化handler对象
     */
    private void initHandler() {
        mHandler = new MerryRoundHandler(mAutoRotation, mRotationTime, mRotateDirection) {
            @Override
            public void onRotating(MerryRoundDirection rotateDirection) {//接受到需要旋转指令
                try {
                    if (viewCount != 0) {//判断自动滑动从那边开始
                        int perAngle = 0;
                        switch (rotateDirection) {
                            case clockwise:
                                perAngle = 360 / viewCount;
                                break;
                            case anticlockwise:
                                perAngle = -360 / viewCount;
                                break;
                        }
                        if (mAngle == 360) {
                            mAngle = 0f;
                        }
                        startAnimRotation(mAngle + perAngle, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
    }

    //这两句本来做的是滑动和点击冲突 但后面看的没啥作用 在toch事件中处理了
    int MIN_DISTANCE = 50;
    boolean mHasScroll = true;

    private GestureDetector.SimpleOnGestureListener getGestureDetectorController() {
        return new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {


                //转换成弧度
                //      double radians= Math.toRadians(mRotationZ);
                //Math.cos(radians) 返回对应的radians弧度的余弦值
//                if(isTB){
//                    if(distanceY>0){
//
//                            mRotationX += distanceY;
//
//                    }else {
//
//                            mRotationX += distanceY;
//
//                    }
//                }

                mAngle += (distanceX / 4);

                //    KLog.e("tag", mAngle + "////" + distanceX);
                if (distanceX < 0) {

                    mRotateDirection = 1;
                    //       KLog.e("tag", "正转");
                } else {

                    //     KLog.e("tag", "反转");
                    mRotateDirection = 0;
                }
                //  Log.e("tag", mAngle + "");
                //初始化
                refreshLayout();
                if (Math.abs(distanceX) < MIN_DISTANCE) {
                    // 如果滑动小于最小滑动距离
                    return true;
                } else {
                    mHasScroll = true;
                }
                return true;
            }
        };
    }

    public void refreshLayout() {
        if (mCarrouselViews.size() <= 1) {
            return;
        }

        int maxChildWidth = mCarrouselViews.get(0).getWidth();
        for (int i = 1; i < mCarrouselViews.size(); i++) {
            maxChildWidth = Math.max(maxChildWidth, mCarrouselViews.get(i).getWidth());
        }

        for (int i = 0; i < mCarrouselViews.size(); i++) {
            View childView = mCarrouselViews.get(i);
            childView.setX(-childView.getWidth() / 2.0f);
            childView.setY(-childView.getHeight() / 2.0f);
            double radians = mAngle + 180 - i * 360F / viewCount;//旋转角度加180 - item * 360 /  view的数量

            float x0 = (float) Math.sin(Math.toRadians(radians)) * mCarrouselR;//角度
            float y0 = (float) (Math.cos(Math.toRadians(radians)) - 1) / 2 * mCarrouselR;//余弦值
            float scale0 = (mDistance - y0) / (mDistance + mCarrouselR); // view的距离 - 余弦值 /  view的距离 +半径

            float rotationX_y = (float) Math.sin(Math.toRadians((mRotationX / 4.0) * Math.cos(Math.toRadians(radians)))) * mCarrouselR;
            float rotationZ_y = -(float) Math.sin(Math.toRadians(-mRotationZ)) * x0;
            float rotationZ_x = (((float) Math.cos(Math.toRadians(-mRotationZ)) * x0) - x0);
            childView.setScaleX(scale0);
            childView.setScaleY(scale0);
            childView.setTranslationX(x0 + rotationZ_x - childView.getWidth() / 2.0F + maxChildWidth / 2.0f);
            childView.setTranslationY(rotationX_y + rotationZ_y + mCarrouselR / 2.0F - childView.getHeight() / 2.0F);
            childView.setAlpha(scale0);
        }
        List<View> arrayViewList = new ArrayList<>();
        arrayViewList.clear();
        for (int i = 0; i < mCarrouselViews.size(); i++) {
            arrayViewList.add(mCarrouselViews.get(i));
        }
        sortList(arrayViewList);

        postInvalidate();
        //  changeZ();
    }

    public static Animation shakeAnimation(int counts) {
        ScaleAnimation translateAnimation = new ScaleAnimation(1f, 1.1f, 1f, 1.1f, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 1f);
        //  Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new LinearInterpolator());
        translateAnimation.setDuration(300);
        return translateAnimation;
    }

    public boolean isContext() {
        for (int i = 0; i < mCarrouselViews.size(); i++) {
            if (null != mCarrouselViews.get(i).getTag()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 排序
     * 對子View 排序，然后根据变化选中是否重绘,这样是为了实现view 在显示的时候来控制当前要显示的是哪三个view，可以改变排序看下效果
     *
     * @param list
     */
    @SuppressWarnings("unchecked")
    private <T> void sortList(List<View> list) {
        @SuppressWarnings("rawtypes")
        Comparator comparator = new SortComparator();
        T[] array = list.toArray((T[]) new Object[list.size()]);
        Arrays.sort(array, comparator);
        int i = 0;
        ListIterator<T> it = (ListIterator<T>) list.listIterator();
        while (it.hasNext()) {
            it.next();
            it.set(array[i++]);
        }
        for (int j = 0; j < list.size(); j++) {
            list.get(j).bringToFront();
        }
    }

    /**
     * 筛选器
     */
    private class SortComparator implements Comparator<View> {
        @Override
        public int compare(View o1, View o2) {
            return (int) (1000 * o1.getScaleX() - 1000 * o2.getScaleX());
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        refreshLayout();
        if (mAutoRotation) {
            mHandler.sendEmptyMessageDelayed(MerryRoundHandler.mMsgWhat, mHandler.getmRotationTime());
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed || isture) {
            isture = false;
            checkChildView();
            startAnimationR();
        }
    }

    /**
     * 旋转木马半径打开动画
     */
    public void startAnimationR() {
        startAnimationR(1f, mCarrouselR);
    }

    /**
     * 旋转木马半径动画
     *
     * @param isOpen 是否打开  否则关闭
     */
    public void startAnimationR(boolean isOpen) {
        if (isOpen) {
            startAnimationR(1f, mCarrouselR);
        } else {
            startAnimationR(mCarrouselR, 1f);
        }
    }

    /**
     * 半径扩散、收缩动画 根据设置半径来实现
     *
     * @param from
     * @param to
     */
    public void startAnimationR(float from, float to) {
        KLog.e("tag", from + "/" + to);
        mAnimationR = ValueAnimator.ofFloat(from, to);
        mAnimationR.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //      KLog.e("tag", "/" + valueAnimator.getAnimatedValue());
                mCarrouselR = (Float) valueAnimator.getAnimatedValue();
                refreshLayout();
            }
        });
        mAnimationR.setInterpolator(new DecelerateInterpolator());
        mAnimationR.setDuration(1000);
        mAnimationR.start();

    }

    public void checkChildView() {
        //先清空views里边可能存在的view防止重复
        for (int i = 0; i < mCarrouselViews.size(); i++) {
            mCarrouselViews.remove(i);
        }
        final int count = getChildCount(); //获取子View的个数
        viewCount = count;
        for (int i = 0; i < count; i++) {
            final View view = getChildAt(i); //获取指定的子view
            final int position = i;
            mCarrouselViews.add(view);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    KLog.e("tag", "点击");
                    if (mOnCarrouselItemClickListener != null) {
                        mOnCarrouselItemClickListener.onItemClick(position);
                    }
                }
            });

        }

    }

    private Bitmap loadBitmapFromView(View v) {
        //     v.setBackgroundColor(Color.TRANSPARENT);
        int w = v.getWidth();
        int h = v.getHeight();
        if (w == 0) {
            loadBitmapFromView(v);
            return null;
        }
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bmp.setHasAlpha(true);
        Canvas c = new Canvas(bmp);
        //  c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        v.layout(v.getLeft(), v.getTop(), w + v.getLeft(), h + v.getTop());

        v.draw(c);

        return bmp;
    }

    /**
     * 复位
     */
    private void restView() {
        if (viewCount == 0) {
            return;
        }
        float resultAngle = 0;
        //平均角度
        float averageAngle = 360 / 5;
        if (mAngle < 0) {
            averageAngle = -averageAngle;
        }

        float minvalue = (int) (mAngle / averageAngle) * averageAngle;//最小角度
        float maxvalue = (int) (mAngle / averageAngle) * averageAngle + averageAngle;//最大角度
        if (mAngle >= 0) {//分为是否小于0的情况
            if (mAngle - mLastAngle > 0) {

                startAnimRotationsss(maxvalue, null);
            } else {
                startAnimRotationsss(minvalue, null);
            }
        } else {
            if (mAngle - mLastAngle < 0) {
                startAnimRotationsss(maxvalue, null);
            } else {
                startAnimRotationsss(minvalue, null);
            }
        }

    }

    /**
     * 动画旋转
     * decelerate_interpolator
     *
     * @param resultAngle
     * @param complete
     */
    private void startAnimRotationsss(float resultAngle, final Runnable complete) {
        if (mAngle == resultAngle) {
            return;
        }
        restAnimator = ValueAnimator.ofFloat(mAngle, resultAngle);

        //设置旋转匀速插值器
        restAnimator.setInterpolator(new LinearInterpolator());
        restAnimator.setDuration(250);
        restAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAngle = (Float) animation.getAnimatedValue();
                refreshLayout();

            }
        });


        restAnimator.start();
    }

    /**
     * 动画旋转
     *
     * @param resultAngle
     * @param complete
     */
    private void startAnimRotation(float resultAngle, final Runnable complete) {
        if (mAngle == resultAngle) {
            return;
        }
        restAnimator = ValueAnimator.ofFloat(mAngle, resultAngle);
        //设置旋转匀速插值器
        restAnimator.setInterpolator(new LinearInterpolator());
        restAnimator.setDuration(mRotationSpeed);
        restAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (isTouching == false) {
                    //  Log.e("tag", "0000000000000");
                    // mRotateDirection ==0?CarrouselRotateDirection.clockwise:CarrouselRotateDirection.anticlockwise;
                    //        mAngle = (Float) animation.getAnimatedValue();
                    if (mRotateDirection == 0) {
                        mAngle = (float) (mAngle + 0.05);
                    } else {
                        mAngle = (float) (mAngle - 0.05);

                    }

                    refreshLayout();
                }
            }
        });
        restAnimator.addListener(new Animator.AnimatorListener() {
            //            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                if (isTouching == false) {
                    try {
                        selectItem = calculateItem();
                    } catch (ArithmeticException e) {

                    }

                    if (selectItem < 0) {
                        selectItem = viewCount + selectItem;
                    }
                    if (mOnCarrouselItemSelectedListener != null) {
                        mOnCarrouselItemSelectedListener.selected(mCarrouselViews.get(selectItem), selectItem);
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        if (complete != null) {
            restAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    complete.run();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        restAnimator.start();
    }

    /**
     * 通过角度计算是第几个item
     *
     * @return
     */
    private int calculateItem() {
        return (int) (mAngle / (360 / viewCount)) % viewCount;
    }

    /**
     * 触摸操作
     *
     * @param event
     * @return
     */
    private boolean onTouch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mLastAngle = mAngle;
            isTouching = true;
        }

        boolean result = mGestureDetector.onTouchEvent(event);
        if (result) {
            this.getParent().requestDisallowInterceptTouchEvent(true);//通知父控件勿拦截本控件
        }
        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            isTouching = false;

            return true;
        }
        return true;
    }


    /**
     * 触摸方法
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        setCanAutoRotation(event);
        return true;
    }


    /**
     * 触摸停止计时器
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        onTouch(ev);
        setCanAutoRotation(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev != null && ev.getAction() == MotionEvent.ACTION_DOWN) {
            mHasScroll = false;
        } else if (ev != null && mHasScroll && ev.getAction() == MotionEvent.ACTION_UP) {
            //如果产生了滑动，则不传递事件到子view了
            return true;
        }
        return super.onInterceptTouchEvent(ev);

    }

    /**
     * 检测按下到抬起时旋转的角度
     */
    private float mTmpAngle;
    private float mLastX;
    private float mLastY;

    /**
     * 触摸时停止自动加载
     *
     * @param event
     */
    public void setCanAutoRotation(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouching = true;
                stopAutoRotation();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                resumeAutoRotation();
                isTouching = false;
                break;
        }
    }

    /**
     * 获取移动的角度
     */
    private float getAngle(float xTouch, float yTouch) {
        double x = xTouch - (mCarrouselR);
        double y = yTouch - (mCarrouselR);
        return (float) (Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
    }

    /**
     * 根据当前位置计算象限
     */
    private int getQuadrant(float x, float y) {
        int tmpX = (int) (x - mCarrouselR);
        int tmpY = (int) (y - mCarrouselR);
        if (tmpX >= 0) {
            return tmpY >= 0 ? 4 : 1;
        } else {
            return tmpY >= 0 ? 3 : 2;
        }

    }

    /**
     * 停止自动加载
     */
    public void stopAutoRotation() {
        if (mHandler != null && mAutoRotation) {
            mHandler.removeMessages(MerryRoundHandler.mMsgWhat);
        }
    }

    /**
     * 从新启动自动加载
     */
    public void resumeAutoRotation() {
        if (mHandler != null && mAutoRotation) {
            mHandler.sendEmptyMessageDelayed(MerryRoundHandler.mMsgWhat, mHandler.getmRotationTime());
        }
    }

    /**
     * 获取所有的view
     *
     * @return
     */
    public List<View> getViews() {
        return mCarrouselViews;
    }

    /**
     * 获取角度
     *
     * @return
     */
    public float getAngle() {
        return mAngle;
    }


    /**
     * 设置角度
     *
     * @param angle
     */
    public void setAngle(float angle) {
        this.mAngle = angle;
    }

    /**
     * 获取距离
     *
     * @return
     */
    public float getDistance() {
        return mDistance;
    }

    /**
     * 设置距离
     *
     * @param distance
     */
    public void setDistance(float distance) {
        this.mDistance = distance;
    }

    /**
     * 获取半径
     *
     * @return
     */
    public float getR() {
        return mCarrouselR;
    }

    /**
     * 获取选择是第几个item
     *
     * @return
     */
    public int getSelectItem() {
        return selectItem;
    }

    /**
     * 设置选中方法
     *
     * @param selectItem
     */
    public void setSelectItem(int selectItem) {
        if (selectItem >= 0) {
            float angle = 0;
            if (getSelectItem() == 0) {
                if (selectItem == mCarrouselViews.size()) {
                    angle = mAngle - (360 / viewCount);
                } else {
                    angle = mAngle + (360 / viewCount);
                }
            } else if (getSelectItem() == mCarrouselViews.size()) {
                if (selectItem == 0) {
                    angle = mAngle + (360 / viewCount);
                } else {
                    angle = mAngle - (360 / viewCount);
                }
            } else {
                if (selectItem > getSelectItem()) {
                    angle = mAngle + (360 / viewCount);
                } else {
                    angle = mAngle - (360 / viewCount);
                }
            }

            float resultAngle = 0;
            float part = 360 / viewCount;
            if (angle < 0) {
                part = -part;
            }
            //最小角度
            float minvalue = (int) (angle / part) * part;
            //最大角度
            float maxvalue = (int) (angle / part) * part;
            if (angle >= 0) {//分为是否小于0的情况
                if (angle - mLastAngle > 0) {
                    resultAngle = maxvalue;
                } else {
                    resultAngle = minvalue;
                }
            } else {
                if (angle - mLastAngle < 0) {
                    resultAngle = maxvalue;
                } else {
                    resultAngle = minvalue;
                }
            }

            if (viewCount > 0) startAnimRotationsss(resultAngle, null);
        }
    }

    /**
     * 设置半径
     *
     * @param r
     */
    public MerryRoundView setR(float r) {
        this.mCarrouselR = r;
        mDistance = 2f * r;
        return this;
    }

    /**
     * 选中回调接口实现
     *
     * @param mOnCarrouselItemSelectedListener
     */
    public void setOnCarrouselItemSelectedListener(OnCarrouselItemSelectedListener mOnCarrouselItemSelectedListener) {
        this.mOnCarrouselItemSelectedListener = mOnCarrouselItemSelectedListener;
    }

    /**
     * 点击事件回调
     *
     * @param mOnCarrouselItemClickListener
     */
    public void setOnCarrouselItemClickListener(OnCarrouselItemClickListener mOnCarrouselItemClickListener) {
        this.mOnCarrouselItemClickListener = mOnCarrouselItemClickListener;
    }


    /**
     * 设置是否自动切换
     *
     * @param autoRotation
     */
    public MerryRoundView setAutoRotation(boolean autoRotation) {
        this.mAutoRotation = autoRotation;
        mHandler.setAutoRotation(autoRotation);
        return this;
    }

    /**
     * 获取自动切换时间
     *
     * @return
     */
    public long getAutoRotationTime() {
        return mHandler.getmRotationTime();
    }

    /**
     * 设置自动切换时间间隔
     *
     * @param autoRotationTime
     */
    public MerryRoundView setAutoRotationTime(long autoRotationTime) {
        if (mHandler != null)
            mHandler.setmRotationTime(autoRotationTime);
        return this;
    }

    /**
     * 是否自动切换
     *
     * @return
     */
    public boolean isAutoRotation() {
        return mAutoRotation;
    }

    /**
     * 设置自动选择方向
     *
     * @param mCarrouselRotateDirection
     * @return
     */
    public MerryRoundView setAutoScrollDirection(MerryRoundDirection mCarrouselRotateDirection) {
        if (mHandler != null)
            mHandler.setmRotateDirection(mCarrouselRotateDirection);
        return this;
    }

    public void createXAnimation(int from, int to, boolean start) {
        if (xAnimation != null) if (xAnimation.isRunning() == true) xAnimation.cancel();
        xAnimation = ValueAnimator.ofInt(from, to);
        xAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRotationX = (Integer) animation.getAnimatedValue();
                refreshLayout();
            }
        });
        xAnimation.setInterpolator(new LinearInterpolator());
        xAnimation.setDuration(2000);
        if (start) xAnimation.start();
    }


    public ValueAnimator createZAnimation(int from, int to, boolean start) {
        if (zAnimation != null) if (zAnimation.isRunning() == true) zAnimation.cancel();
        zAnimation = ValueAnimator.ofInt(from, to);
        zAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRotationZ = (Integer) animation.getAnimatedValue();
                refreshLayout();
            }
        });
        zAnimation.setInterpolator(new LinearInterpolator());
        zAnimation.setDuration(2000);
        if (start) zAnimation.start();
        return zAnimation;
    }

    public MerryRoundView setRotationX(int mRotationX) {
        this.mRotationX = mRotationX;
        KLog.e("tag", "" + mRotationX);
        return this;
    }

    public MerryRoundView setRotationZ(int mRotationZ) {
        this.mRotationZ = mRotationZ;
        return this;
    }

    public float getRotationX() {
        return mRotationX;
    }

    public int getRotationZ() {
        return mRotationZ;
    }

    public ValueAnimator getRestAnimator() {
        return restAnimator;
    }

    public ValueAnimator getAnimationR() {
        return mAnimationR;
    }

    public void setAnimationZ(ValueAnimator zAnimation) {
        this.zAnimation = zAnimation;
    }

    public ValueAnimator getAnimationZ() {
        return zAnimation;
    }

    public void setAnimationX(ValueAnimator xAnimation) {
        this.xAnimation = xAnimation;
    }

    public ValueAnimator getAnimationX() {
        return xAnimation;
    }

    //
    public void resume() {

    }

    public void close() {
        stopAutoRotation();

        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (null != mAnimationR) {
            mAnimationR.cancel();
            mAnimationR.clone();
        }
        if (restAnimator != null) {
            restAnimator.cancel();
            restAnimator.removeAllUpdateListeners();
        }
//        for (int i = 0; i < getChildCount(); i++) {
//            AntForestView antForestView = (AntForestView) getChildAt(i);
//            antForestView.stopAnimate();
//        }
    }


}
