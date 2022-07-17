package com.fengwo.module_login.mvp.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.ScreenUtils;
import com.fengwo.module_login.mvp.dto.NobilityTypeDTO;

import java.util.List;

public class NobilityTypeAdapter extends PagerAdapter {

    private final OnItemClickListener listener;
    public List<NobilityTypeDTO> list;

    public NobilityTypeAdapter(@NonNull OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        ViewPager.LayoutParams lp = new ViewPager.LayoutParams();
        lp.width = (ScreenUtils.getScreenWidth(container.getContext())) / 3;
        lp.height = lp.width;
        imageView.setLayoutParams(lp);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageLoader.loadImg(imageView, list.get(position).levelIcon);
        container.addView(imageView);
        imageView.setOnClickListener(v -> listener.NobilityClick(list.get(position), position));
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (object instanceof ImageView && ((ImageView) object).getParent() != null) {
            container.removeView((ImageView) object);
        }
    }

    public interface OnItemClickListener {
        void NobilityClick(NobilityTypeDTO data, int position);
    }
}