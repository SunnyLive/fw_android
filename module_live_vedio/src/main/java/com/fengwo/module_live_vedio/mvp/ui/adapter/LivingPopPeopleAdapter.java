package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.DataFormatUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.WatcherDto;

import java.util.List;

public class LivingPopPeopleAdapter extends BaseQuickAdapter<WatcherDto, BaseViewHolder> {
    public LivingPopPeopleAdapter(@Nullable List<WatcherDto> data) {
        super(R.layout.live_item_livingroom_watchers, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, WatcherDto item) {
        ImageLoader.loadImg(helper.getView(R.id.iv_header), item.headImg);
        helper.setText(R.id.tv_name, item.nickname);
        ImageView ivSex = helper.getView(R.id.iv_sex);
        int id = ImageLoader.getUserLevel(item.userLevel);
        helper.setImageResource(R.id.iv_level, id);
        double xiaofei = 0;
        if (item.receive > 0) {
            xiaofei = item.receive;
        }
        if (item.consumNums > 0) {
            xiaofei = item.consumNums;
        }
        if (xiaofei > 0)
            helper.setText(R.id.tv_num, DataFormatUtils.formatNumbers(xiaofei));
        else {
            helper.setText(R.id.tv_num, "0");
        }
        helper.setVisible(R.id.iv_hat, true);
        if (xiaofei > 0 && helper.getLayoutPosition() == 0) {
            helper.setImageResource(R.id.iv_hat, R.drawable.live_ic_shouhu1);
        } else if (xiaofei > 0 && helper.getLayoutPosition() == 1) {
            helper.setImageResource(R.id.iv_hat, R.drawable.live_ic_shouhu2);
        } else if (xiaofei > 0 && helper.getLayoutPosition() == 2) {
            helper.setImageResource(R.id.iv_hat, R.drawable.live_ic_shouhu3);
        } else {
            helper.setVisible(R.id.iv_hat, false);
        }

        if (item.nobility > 0) {
            helper.setVisible(R.id.iv_vip_level, true);
            helper.setImageResource(R.id.iv_vip_level, ImageLoader.getVipLevel(item.nobility));
        } else {
            helper.setVisible(R.id.iv_vip_level, false);
        }

//        if (item.sex == 0) {
//            ivSex.setVisibility(View.GONE);
//        } else {
//            ivSex.setVisibility(View.VISIBLE);
//            if (item.sex == 1) {
//                ivSex.setImageResource(R.drawable.login_ic_boy);
//                ivSex.setBackgroundResource(R.drawable.login_bg_sex_boy);
//            } else {
//                ivSex.setImageResource(R.drawable.login_ic_girl);
//                ivSex.setBackgroundResource(R.drawable.login_bg_sex_girl);
//            }
//        }
        helper.addOnClickListener(R.id.root);
    }
}
