package com.fengwo.module_login.mvp.ui.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_login.R;

import java.util.List;

public class MineGuardAdapter extends BaseQuickAdapter<UserInfo.UserGuardList, BaseViewHolder> {

    public MineGuardAdapter(@Nullable List<UserInfo.UserGuardList> data) {
        super(R.layout.item_mine_guard, data);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, UserInfo.UserGuardList item) {
        ImageView iv = helper.getView(R.id.iv_header);
        ImageLoader.loadImg(iv, item.levelIcon);
        helper.addOnClickListener(R.id.iv_header);
        helper.setText(R.id.tv_number,item.quantity+"");
    }


}
