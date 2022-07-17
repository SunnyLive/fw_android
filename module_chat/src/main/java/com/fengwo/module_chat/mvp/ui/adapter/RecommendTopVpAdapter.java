package com.fengwo.module_chat.mvp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fengwo.module_chat.R;
import com.fengwo.module_comment.utils.ImageLoader;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/25
 */
public class RecommendTopVpAdapter extends PagerAdapter {


    private Context mContext;
    private List<String> imageList;

    public RecommendTopVpAdapter(Context mContext,List list) {
        this.mContext = mContext;
        this.imageList = list;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_only_image,null);
        ImageView imageView = view.findViewById(R.id.iv_image);
        ImageLoader.loadImg(imageView,imageList.get(position));

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
