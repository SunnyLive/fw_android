package com.fengwo.module_comment.widget.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.fengwo.module_comment.R;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.widget.ViewPagerIndicator;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FWBanner extends FrameLayout {

    private ViewPager viewPager;
    private ViewPagerIndicator indicator;

    private int mSelectColor;       // 选中颜色
    private int mDefaultColor;      // 默认颜色
    private float mRadius;          // 半径
    private float mRadiusSelected;   // 选中半径
    private float mLength;          // 线长
    private float mDistance;        // 间隔距离
    private int mDistanceType;      // 距离类型
    private int mIndicatorType;     // 点类型
    private int mNum;               // 个数
    private boolean mAnimation;     // 是否需要动画
    private float mIndicatorHeight; // indicator高度

    private CompositeDisposable disposable = new CompositeDisposable();

    /**
     * 布局,距离,半径
     */
    public interface DistanceType {
        int BY_RADIUS = 0;
        int BY_DISTANCE = 1;
        int BY_LAYOUT = 2;
    }

    /**
     * 线,圆,圆线,贝塞尔,弹性球,进度条
     */
    public interface IndicatorType {
        int LINE = 0;
        int CIRCLE = 1;
        int CIRCLE_LINE = 2;
        int BEZIER = 3;
        int SPRING = 4;
        int PROGRESS = 5;
    }

    public FWBanner(Context context) {
        this(context, null);
    }

    public FWBanner(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FWBanner(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 渲染视图
        inflate(context, R.layout.common_header_banner, this);
        // 初始化自定义属性
        initAttr(context.obtainStyledAttributes(attrs, R.styleable.FWBanner));
        // 初始化子控件
        initView();
    }

    private void initAttr(TypedArray ta) {
        mSelectColor = ta.getColor(R.styleable.FWBanner_fwb_selected_color, 0xffffffff);
        mDefaultColor = ta.getColor(R.styleable.FWBanner_fwb_default_color, 0xffcdcdcd);
        mRadius = ta.getDimension(R.styleable.FWBanner_fwb_radius, 2);
        mRadiusSelected = ta.getDimension(R.styleable.FWBanner_fwb_radius_selected, mRadius);
        mLength = ta.getDimension(R.styleable.FWBanner_fwb_length, 2 * mRadius);//px
        mDistance = ta.getDimension(R.styleable.FWBanner_fwb_distance, 3 * mRadius);//px
        mDistanceType = ta.getInteger(R.styleable.FWBanner_fwb_distanceType, DistanceType.BY_RADIUS);
        mIndicatorType = ta.getInteger(R.styleable.FWBanner_fwb_indicatorType, IndicatorType.CIRCLE);
        mNum = ta.getInteger(R.styleable.FWBanner_fwb_num, 0);
        mAnimation = ta.getBoolean(R.styleable.FWBanner_fwb_animation, true);
        mIndicatorHeight = ta.getDimension(R.styleable.FWBanner_fwb_indicator_height, 10);
        ta.recycle();
    }

    private void initView() {
        viewPager = findViewById(R.id.vp_banner);
        indicator = findViewById(R.id.indicator_circle_line);

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state == ViewPager.SCROLL_STATE_DRAGGING) stop();
                else if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if (viewPager.getCurrentItem() == 0)
                        viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 2, false);
                    else if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1)
                        viewPager.setCurrentItem(1, false);
                    autoPlay();
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });

//        indicator.setNum(mNum);
//        indicator.setDistance(mDistance);
//        indicator.setDistanceType(mDistanceType);
//        indicator.setRadius(mRadius);
//        indicator.setType(mIndicatorType);
//        indicator.setSelectedColor(mSelectColor);
//        indicator.setDefaultColor(mDefaultColor);
//        indicator.setLength(mLength);
//        indicator.setAnimation(mAnimation);
//        indicator.setRadiusSelected(mRadiusSelected);
    }

    public FWBanner setSelectColor(int selectColor) {
        mSelectColor = selectColor;
        return this;
    }

    public FWBanner setDefaultColor(int defaultColor) {
        mDefaultColor = defaultColor;
        return this;
    }

    public FWBanner setRadius(float radius) {
        mRadius = radius;
        return this;
    }

    public FWBanner setRadiusSelect(float radius) {
        mRadiusSelected = radius;
        return this;
    }

    public FWBanner setLength(float length) {
        mLength = length;
        return this;
    }

    public FWBanner setDistance(float distance) {
        mDistance = distance;
        return this;
    }

    public FWBanner setDistancetype(@IntRange(from = 0, to = 2) int type) {
        mDistanceType = type;
        return this;
    }

    public FWBanner setIndicatorType(@IntRange(from = 0, to = 5) int type) {
        mIndicatorType = type;
        return this;
    }

    public FWBanner setNumber(int number) {
        mNum = number;
        return this;
    }

    public FWBanner setAnimation(boolean needAnimation) {
        mAnimation = needAnimation;
        return this;
    }

    public FWBanner setInidcatorHeight(float indicatorHeight) {
        mIndicatorHeight = indicatorHeight;
        return this;
    }

    public FWBanner setAdapter(PagerAdapter adapter) {
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager, true);
        autoPlay();
        return this;
    }

    public void autoPlay() {
        if (!disposable.isDisposed() && disposable.size() > 0) return;
        Disposable subscribe = Observable.interval(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> viewPager.setCurrentItem(viewPager.getCurrentItem() + 1));
        disposable.add(subscribe);
    }

    public void stop() {
        disposable.clear();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (!disposable.isDisposed() && disposable.size() > 0) disposable.clear();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility != VISIBLE) stop();
        else if (viewPager.getAdapter() != null && viewPager.getAdapter().getCount() > 0)
            autoPlay();
    }
}