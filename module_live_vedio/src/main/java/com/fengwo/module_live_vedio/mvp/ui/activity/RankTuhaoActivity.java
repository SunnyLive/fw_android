package com.fengwo.module_live_vedio.mvp.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.widget.GradientTextView;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.mvp.dto.RankTuhaoDto;
import com.fengwo.module_live_vedio.mvp.presenter.RankTuhaoPresenter;
import com.fengwo.module_live_vedio.mvp.ui.adapter.FragmentVpAdapter;
import com.fengwo.module_live_vedio.mvp.ui.adapter.RankTuhaoAdapter;
import com.fengwo.module_live_vedio.mvp.ui.fragment.RankPkFragment;
import com.fengwo.module_live_vedio.mvp.ui.fragment.RankTuhaoFragment;
import com.fengwo.module_live_vedio.mvp.ui.iview.IRankTuhaoView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RankTuhaoActivity extends BaseMvpActivity implements TabLayout.OnTabSelectedListener {

    private final static int TYPE_HOURS = 1;
    private final static int TYPE_DAY = 2;
    private final static int TYPE_WEEK = 3;
    private final static int TYPE_MONTH = 4;

    @BindView(R2.id.tabview)
    TabLayout tabview;
    @BindView(R2.id.vp)
    ViewPager vp;
    @BindView(R2.id.tv_day)
    TextView tvDay;
    @BindView(R2.id.tv_week)
    TextView tvWeek;
    @BindView(R2.id.tv_month)
    TextView tvMonth;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private FragmentVpAdapter fragmentVpAdapter;

    @Override
    public RankTuhaoPresenter initPresenter() {
        return new RankTuhaoPresenter();
    }

    @Override
    protected void initView() {
        setTitleBackground(android.R.color.transparent);
        new ToolBarBuilder().showBack(true)
                .setTitle("名人榜")
                .build();
        fragmentList.add(RankTuhaoFragment.newInstance(TYPE_DAY));
        fragmentList.add(RankTuhaoFragment.newInstance(TYPE_WEEK));
        fragmentList.add(RankTuhaoFragment.newInstance(TYPE_MONTH));
        titleList.add("日榜");
        titleList.add("周榜");
        titleList.add("月榜");
        fragmentVpAdapter = new FragmentVpAdapter(getSupportFragmentManager(), fragmentList, titleList);
        vp.setOffscreenPageLimit(2);
        vp.setAdapter(fragmentVpAdapter);
        vp.setCurrentItem(1);
        tabview.addOnTabSelectedListener(this);
        tabview.setupWithViewPager(vp);
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                tabview.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected int getContentView() {
        return R.layout.live_activity_rank_tuhao;
    }

    private void reSetView(int position) {
        if (position == 0) {
            tvDay.setVisibility(View.VISIBLE);
            tvWeek.setVisibility(View.INVISIBLE);
            tvMonth.setVisibility(View.INVISIBLE);
        } else if (position == 1) {
            tvDay.setVisibility(View.INVISIBLE);
            tvWeek.setVisibility(View.VISIBLE);
            tvMonth.setVisibility(View.INVISIBLE);
        } else {
            tvDay.setVisibility(View.INVISIBLE);
            tvWeek.setVisibility(View.INVISIBLE);
            tvMonth.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        reSetView(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
