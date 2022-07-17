package com.fengwo.module_vedio.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.fengwo.module_comment.base.BannerBean;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.widget.ViewPagerIndicator;
import com.fengwo.module_vedio.R;
import com.fengwo.module_vedio.mvp.dto.AdvertiseBean;
import com.fengwo.module_vedio.mvp.dto.VideoBanerDto;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/29
 */
public class BannerView extends ViewGroup {

    private View contentView;
    ViewPager vpBanner;
    private ArrayList<AdvertiseBean> mCircleList = new ArrayList<>();
    private Disposable intervalDisposable;
    private MyPagerAdapter pagerAdapter;
    private Context context;

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        contentView = LayoutInflater.from(context).inflate(R.layout.live_header_banner, null);
        vpBanner = contentView.findViewById(R.id.vp_banner);
    }


    public void setData(ArrayList<AdvertiseBean> list) {
        this.mCircleList.addAll(list);
        int size = list.size();
        if (size > 1) {
            this.mCircleList.add(0, list.get(size - 1));
            this.mCircleList.add(list.get(0));
        }
        ViewPagerIndicator viewPagerIndicator = contentView.findViewById(R.id.indicator_circle_line);
        pagerAdapter = new MyPagerAdapter();
        vpBanner.setAdapter(pagerAdapter);
        vpBanner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if (vpBanner.getCurrentItem() == 0)
                        vpBanner.setCurrentItem(mCircleList.size() - 2, false);
                    else if (vpBanner.getCurrentItem() == mCircleList.size() - 1)
                        vpBanner.setCurrentItem(1, false);
                    startBannerInterval();
                }
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    stopBannerInterval();
                }
            }
        });
        vpBanner.setCurrentItem(1);
        viewPagerIndicator.setViewPager(vpBanner, true);
        startBannerInterval();//初始化开始轮播
    }

    public void startBannerInterval() {
        if (mCircleList.size() > 0 && intervalDisposable == null)
            intervalDisposable = Flowable.interval(3, TimeUnit.SECONDS)
                    .compose(RxUtils.applySchedulers())
                    .subscribe(i -> {
                        vpBanner.setCurrentItem(vpBanner.getCurrentItem() + 1);
                    });//轮播的
    }

    public void stopBannerInterval() {
        if (intervalDisposable != null && !intervalDisposable.isDisposed()) {
            intervalDisposable.dispose();
            intervalDisposable = null;
        }//轮播的
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mCircleList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.live_item_banner_img, container, false);
            ImageView iv = view.findViewById(R.id.iv_circle_icon);
            ImageLoader.loadRouteImg(iv, mCircleList.get(position).image, iv.getWidth(), iv.getHeight());
//            view.getViewTreeObserver().addOnGlobalLayoutListener(() -> );
            container.addView(view);
            return view;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
