package com.fengwo.module_login.mvp.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.mvp.ui.fragment.ReceiveGiftFragment;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;

public class ReceiveGiftActivity extends BaseMvpActivity {

    @BindView(R2.id.tablayout)
    TabLayout tablayout;
    @BindView(R2.id.vp)
    ViewPager viewPager;

    private String[] titles = {"今日收礼", "本周收礼", "本月收礼"};

    @Override
    protected void initView() {
        initViewPager();
        initTab();
    }

    @Override
    protected int getContentView() {
        return R.layout.login_activity_receive_gift;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    /**
     * 初始化tabLayout
     */
    private void initTab() {
        tablayout.setupWithViewPager(viewPager);
    }

    private void initViewPager() {
        TabItemViewPagerAdapter adapter = new TabItemViewPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setOffscreenPageLimit(titles.length);
        viewPager.setAdapter(adapter);
    }

    private class TabItemViewPagerAdapter extends FragmentPagerAdapter {

        TabItemViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return ReceiveGiftFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}