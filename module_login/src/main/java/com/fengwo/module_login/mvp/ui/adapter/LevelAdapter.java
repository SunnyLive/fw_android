package com.fengwo.module_login.mvp.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_login.R;

import androidx.annotation.NonNull;

public class LevelAdapter extends BaseQuickAdapter<UserInfo.UserMedalsList,BaseViewHolder> {


    public LevelAdapter() {
        super(R.layout.layout_level_item);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, UserInfo.UserMedalsList item) {
        ImageLoader.loadImg(helper.getView(R.id.iv_level),item.medalIcon);
    }
}
