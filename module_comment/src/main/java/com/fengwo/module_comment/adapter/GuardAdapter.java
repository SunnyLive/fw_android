package com.fengwo.module_comment.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.R;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.ImageLoader;

import java.util.List;

public class GuardAdapter extends BaseQuickAdapter<UserInfo.UserGuardList, BaseViewHolder> {

    public GuardAdapter(@Nullable List<UserInfo.UserGuardList> data) {
        super(R.layout.live_item_guard, data);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, UserInfo.UserGuardList item) {
        ImageView iv = helper.getView(R.id.iv_header);
        ImageLoader.loadImg(iv, item.guardUserHeadImg);
        helper.addOnClickListener(R.id.iv_header);
    }


}
