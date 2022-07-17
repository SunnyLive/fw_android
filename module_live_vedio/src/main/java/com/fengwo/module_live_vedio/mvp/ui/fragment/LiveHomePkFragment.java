package com.fengwo.module_live_vedio.mvp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.fengwo.module_comment.base.BaseFragment;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BaseMvpFragment;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.widget.ViewPagerIndicator;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class LiveHomePkFragment extends BaseMvpFragment {
    @BindView(R2.id.vp_banner)
    ViewPager vpBanner;
    @BindView(R2.id.indicator_circle_line)
    ViewPagerIndicator indicatorCircleLine;
    @BindView(R2.id.ll_recomment)
    LinearLayout llRecomment;
    @BindView(R2.id.ll_hot)
    LinearLayout llHot;

    private LayoutInflater layoutInflater;

    @Override
    protected int setContentView() {
        return R.layout.live_fragment_pk;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        layoutInflater = LayoutInflater.from(getActivity());
        initHeaderBanner();
        initRecommentLl();
        initHtoLl();

    }

    private void initRecommentLl() {
        llRecomment.removeAllViews();
        for (int i = 0; i < 3; i++) {
            View v = layoutInflater.inflate(R.layout.live_item_pk, null, false);
            llRecomment.addView(v);
        }
    }

    private void initHtoLl() {
        llHot.removeAllViews();
        for (int i = 0; i < 3; i++) {
            View v = layoutInflater.inflate(R.layout.live_item_pk, null, false);
            llHot.addView(v);
        }
    }

    private ArrayList<String> mCircleList = new ArrayList<>();

    private void initHeaderBanner() {
        mCircleList.add("1");
        mCircleList.add("2");
        mCircleList.add("3");
        mCircleList.add("4");
        mCircleList.add("1");
        mCircleList.add(0, "4");
        vpBanner.setAdapter(new PagerAdapter() {
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
                TextView tv = view.findViewById(R.id.tv_position);
                tv.setText("" + mCircleList.get(position));
                container.addView(view);
                return view;
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }
        });
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
        indicatorCircleLine.setViewPager(vpBanner, true);
        startBannerInterval();//初始化开始轮播
    }

    private Disposable intervalDisposable;

    private void startBannerInterval() {
        if (mCircleList.size() > 0 && intervalDisposable == null)
            intervalDisposable = Flowable.interval(3, TimeUnit.SECONDS)
                    .compose(RxUtils.applySchedulers2())
                    .subscribe(i -> {
                        vpBanner.setCurrentItem(vpBanner.getCurrentItem() + 1);
                    });//轮播的
    }

    private void stopBannerInterval() {
        if (intervalDisposable != null && !intervalDisposable.isDisposed()) {
            intervalDisposable.dispose();
            intervalDisposable = null;
        }//轮播的
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            startBannerInterval();
        } else {
            stopBannerInterval();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        stopBannerInterval();
    }

    @Override
    public void onResume() {
        super.onResume();
        startBannerInterval();
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }
}
