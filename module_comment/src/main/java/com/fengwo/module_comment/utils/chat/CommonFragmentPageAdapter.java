package com.fengwo.module_comment.utils.chat;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * ================================================
 * 作    者：Fuzp
 * 版    本：1.0
 * 创建日期：2018/11/15.
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class CommonFragmentPageAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> list;

    public CommonFragmentPageAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }
}
