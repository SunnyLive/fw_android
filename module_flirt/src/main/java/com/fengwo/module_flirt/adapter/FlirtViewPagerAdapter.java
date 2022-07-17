package com.fengwo.module_flirt.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * @Author BLCS
 * @Time 2020/3/27 16:30
 */
public class FlirtViewPagerAdapter extends FragmentStatePagerAdapter {
    private final ArrayList<Fragment> mFragments;

    public FlirtViewPagerAdapter(FragmentManager supportFragmentManager, int behaviorResumeOnlyCurrentFragment, ArrayList<Fragment> mFragments) {
        super(supportFragmentManager, behaviorResumeOnlyCurrentFragment);
        this.mFragments = mFragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
