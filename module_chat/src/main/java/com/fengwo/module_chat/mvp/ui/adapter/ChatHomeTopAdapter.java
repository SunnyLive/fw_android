package com.fengwo.module_chat.mvp.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.fengwo.module_chat.mvp.model.bean.AdvertiseBean;
import com.fengwo.module_comment.base.BrowserActivity;
import com.fengwo.module_comment.base.WebViewActivity;
import com.fengwo.module_comment.utils.ImageLoader;

import java.util.ArrayList;

public class ChatHomeTopAdapter extends PagerAdapter {

    private ArrayList<AdvertiseBean> list;

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        AdvertiseBean bean = list.get(position);
        ImageView view = new ImageView(container.getContext());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp);
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageLoader.loadImg(view, bean.image);
        container.addView(view);
        view.setOnClickListener(v -> BrowserActivity.start(container.getContext(), bean.title, bean.contentUrl));
        return view;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void setData(@NonNull ArrayList<AdvertiseBean> records) {
        list = records;
        notifyDataSetChanged();
    }
}
