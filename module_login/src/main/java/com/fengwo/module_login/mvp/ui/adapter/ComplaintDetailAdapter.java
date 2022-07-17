package com.fengwo.module_login.mvp.ui.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_login.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/1
 */
public class ComplaintDetailAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public ComplaintDetailAdapter(@Nullable List<String> data) {
        super(R.layout.item_complaint_list,data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
            ImageLoader.loadImg(helper.getView(R.id.iv_pic), item);
            helper.getView(R.id.iv_close).setVisibility(View.GONE);
    }
}
