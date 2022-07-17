package com.fengwo.module_chat.mvp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.fengwo.module_chat.R;
import com.fengwo.module_chat.mvp.model.bean.AdvertiseBean;
import com.fengwo.module_chat.mvp.ui.activity.social.userpage.UserSocialPageActivity;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.ScreenUtils;
import com.fengwo.module_comment.widget.RoundImageView;

import java.util.ArrayList;

public class ChatHomeTopicAdapter extends PagerAdapter {

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
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.chat_item_banner_img,container,false);
        ImageView imageView = view.findViewById(R.id.tv_position);
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.width = (int) (ScreenUtils.getScreenWidth(container.getContext()) * 0.835F);
        view.setLayoutParams(lp);
        ImageLoader.loadImg(imageView, bean.image);
        container.addView(view);
        view.setOnClickListener(v -> UserSocialPageActivity.start(container.getContext()));
        return view;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void setData(ArrayList<AdvertiseBean> data) {
        list = data;
        notifyDataSetChanged();
    }
}
