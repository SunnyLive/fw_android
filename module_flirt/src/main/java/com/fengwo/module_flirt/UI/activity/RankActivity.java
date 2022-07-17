package com.fengwo.module_flirt.UI.activity;

import android.graphics.Color;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.fengwo.module_comment.FragmentVpAdapter;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_flirt.UI.fragment.RankFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RankActivity extends BaseMvpActivity {

    private final static int TYPE_HOURSE = 1;
    private final static int TYPE_DAY = 2;
    private final static int TYPE_WEEK = 3;
    private final static int TYPE_MONTH = 4;

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
        setTitleBackground(Color.TRANSPARENT);
        new ToolBarBuilder().showBack(true)
                .setBackIcon(R.drawable.ic_arrow_white_left)
                .setTitle("魅力榜")
                .setTitleColor(R.color.text_white)
                .build();
        fragmentList.add(RankFragment.newInstance(TYPE_DAY));
        fragmentList.add(RankFragment.newInstance(TYPE_WEEK));
        fragmentList.add(RankFragment.newInstance(TYPE_MONTH));
        titleList.add("日榜");
        titleList.add("周榜");
        titleList.add("月榜");
        fragmentVpAdapter = new FragmentVpAdapter(getSupportFragmentManager(), fragmentList, titleList);
        vp.setOffscreenPageLimit(5);
        vp.setAdapter(fragmentVpAdapter);
        tabview.setupWithViewPager(vp);
        tabview.setTabTextColors(ContextCompat.getColor(this,R.color.text_white),ContextCompat.getColor(this,R.color.text_white));
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_rank;
    }
}
