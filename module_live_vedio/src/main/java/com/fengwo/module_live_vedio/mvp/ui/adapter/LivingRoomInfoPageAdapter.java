package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.fengwo.module_live_vedio.R;

import java.util.List;

public class LivingRoomInfoPageAdapter extends PagerAdapter {
    private List<View> viewList;

    public LivingRoomInfoPageAdapter(List<View> views) {
        viewList = views;
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    private String roomId;

    public void setLivingRoomId(String id) {
        roomId = id;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View v = viewList.get(position);
        container.addView(v);
        return v;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
