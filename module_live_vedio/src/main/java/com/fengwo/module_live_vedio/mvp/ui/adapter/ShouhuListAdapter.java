package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.GuardListDto;

import java.util.List;

public class ShouhuListAdapter extends BaseQuickAdapter<GuardListDto.Guard, BaseViewHolder> {

    public ShouhuListAdapter(@Nullable List<GuardListDto.Guard> data) {
        super(R.layout.live_item_shouhu_list, data);

    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, GuardListDto.Guard item) {
        ImageLoader.loadImg(helper.getView(R.id.iv_header), item.guardUserHeadImg);
        long time = TimeUtils.dealDateFormatTolong(item.guardDeadline);

        helper.setText(R.id.tv_num, "剩余守护天数:" + TimeUtils.culSurplusDay(time) + "天");
        helper.setText(R.id.tv_name, item.guardUserNickname);
        ImageView ivShouhu = helper.getView(R.id.iv_shouhu);
        ImageView ivSex = helper.getView(R.id.iv_sex);
        if (item.guardUserSex == 1){
            ivSex.setVisibility(View.VISIBLE);
            ivSex.setImageResource(R.drawable.login_ic_boy);
            ivSex.setBackgroundResource(R.drawable.login_bg_sex_boy);
        }else if (item.guardUserSex == 2){
            ivSex.setVisibility(View.VISIBLE);
            ivSex.setImageResource(R.drawable.login_ic_girl);
            ivSex.setBackgroundResource(R.drawable.login_bg_sex_girl);
        }else {
            ivSex.setVisibility(View.GONE);
        }
//        if (item.level == 1) {
//            ivShouhu.setImageResource(R.drawable.live_ic_xiaoshouhu);
//        } else {
//            ivShouhu.setImageResource(R.drawable.live_ic_shouhu_empty);
//        }
        ImageLoader.loadImg(ivShouhu,item.levelIcon);
        int userLevel = item.guardUserLevel;
        if (userLevel > 47) {
            userLevel = 20;
        }else if (userLevel <=0) {
            userLevel = 1;
        }

        helper.setImageResource(R.id.iv_level, ImageLoader.getResId("login_ic_v" + userLevel, R.drawable.class));
    }
}
