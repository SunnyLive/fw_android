package com.fengwo.module_comment.utils.chat;

import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

/**
 * ================================================
 * 作    者：Fuzp
 * 版    本：1.0
 * 创建日期：2018/11/15.
 * 描    述：表情页面适配器
 * 修订历史：
 * ================================================
 */
public class EmotionPagerAdapter extends PagerAdapter {
    private List<GridView> gvs;

    public EmotionPagerAdapter(List<GridView> gvs) {
        this.gvs = gvs;
    }

    @Override
    public int getCount() {
        return gvs.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(gvs.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(gvs.get(position));
        return gvs.get(position);
    }
}
