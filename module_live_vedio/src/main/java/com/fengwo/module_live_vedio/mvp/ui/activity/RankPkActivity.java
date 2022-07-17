package com.fengwo.module_live_vedio.mvp.ui.activity;

import android.graphics.Color;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.mvp.presenter.RankPkPresenter;
import com.fengwo.module_live_vedio.mvp.ui.adapter.FragmentVpAdapter;
import com.fengwo.module_live_vedio.mvp.ui.fragment.RankPkFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RankPkActivity extends BaseMvpActivity {

    private final static int TYPE_SIMPLE = 1;
    private final static int TYPE_TEAM = 2;
    private final static int TYPE_WINGS = 3;

    @BindView(R2.id.tabview)
    TabLayout tabview;
    @BindView(R2.id.vp)
    ViewPager vp;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private FragmentVpAdapter fragmentVpAdapter;


    @Override
    public RankPkPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
//        setTitleBackground(Color.WHITE);
        new ToolBarBuilder().showBack(true)
                .setBackIcon(R.drawable.ic_back_black)
                .setTitle("主播PK榜")
                .setTitleColor(R.color.text_33)
                .build();
        fragmentList.add(RankPkFragment.newInstance(TYPE_SIMPLE));
        fragmentList.add(RankPkFragment.newInstance(TYPE_TEAM));
        fragmentList.add(RankPkFragment.newInstance(TYPE_WINGS));
        titleList.add("单人PK");
        titleList.add("组团PK");
        titleList.add("战队PK");
        fragmentVpAdapter = new FragmentVpAdapter(getSupportFragmentManager(), fragmentList, titleList);
        vp.setOffscreenPageLimit(2);
        vp.setAdapter(fragmentVpAdapter);
        tabview.setupWithViewPager(vp);
    }

    @Override
    protected int getContentView() {
        return R.layout.live_activity_rank_pk;
    }

}
