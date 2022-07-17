package com.fengwo.module_login.mvp.ui.activity;

import android.graphics.Color;

import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.mvp.ui.adapter.FragmentVpAdapter;
import com.fengwo.module_login.mvp.ui.fragment.GuardFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/22
 */
public class MyGuardActivity extends BaseMvpActivity {
    private final static int TYPE_MY_GUARD = 1;
    private final static int TYPE_GUARD_MY = 2;

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
                .setTitle("守护")
                .setTitleColor(R.color.text_33)
                .build();
        titleList.add("我守护的");
        titleList.add("我的守护");
        fragmentList.add(GuardFragment.newInstance(TYPE_MY_GUARD));
        fragmentList.add(GuardFragment.newInstance(TYPE_GUARD_MY));
        fragmentVpAdapter = new FragmentVpAdapter(getSupportFragmentManager(), fragmentList, titleList);
        vp.setOffscreenPageLimit(2);
        vp.setAdapter(fragmentVpAdapter);
        tabview.setupWithViewPager(vp);
    }

    @Override
    protected int getContentView() {
        return R.layout.base_tab_viewpage;
    }
}
