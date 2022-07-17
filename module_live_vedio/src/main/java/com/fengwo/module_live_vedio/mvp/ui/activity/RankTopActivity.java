package com.fengwo.module_live_vedio.mvp.ui.activity;

import android.graphics.Color;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.mvp.ui.adapter.FragmentVpAdapter;
import com.fengwo.module_live_vedio.mvp.ui.fragment.ZhuboRankFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RankTopActivity extends BaseMvpActivity {

    public final static int TYPE_HOURSE = 1;
    public final static int TYPE_DAY = 2;
    public final static int TYPE_WEEK = 3;
    public final static int TYPE_MONTH = 4;

    @BindView(R2.id.tabview)
    TabLayout tabview;
    @BindView(R2.id.vp)
    ViewPager vp;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private FragmentVpAdapter fragmentVpAdapter;


    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
//        setTitleBackground(Color.WHITE);
        new ToolBarBuilder().showBack(true)
                .setBackIcon(R.drawable.ic_back_black)
                .setTitle("TOP榜")
                .setTitleColor(R.color.text_33)
                .build();
        fragmentList.add(ZhuboRankFragment.newInstance(TYPE_HOURSE));
        fragmentList.add(ZhuboRankFragment.newInstance(TYPE_DAY));
        fragmentList.add(ZhuboRankFragment.newInstance(TYPE_WEEK));
        fragmentList.add(ZhuboRankFragment.newInstance(TYPE_MONTH));
        titleList.add("小时榜");
        titleList.add("日榜");
        titleList.add("周榜");
        titleList.add("月榜");
        fragmentVpAdapter = new FragmentVpAdapter(getSupportFragmentManager(), fragmentList, titleList);
        vp.setOffscreenPageLimit(5);
        vp.setAdapter(fragmentVpAdapter);
        tabview.setupWithViewPager(vp);
    }

    @Override
    protected int getContentView() {
        return R.layout.live_activity_ranktop;
    }


}
