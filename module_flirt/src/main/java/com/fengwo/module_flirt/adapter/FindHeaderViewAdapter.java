package com.fengwo.module_flirt.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.bean.FindHeaderDto;


/**
 * @Author BLCS
 * @Time 2020/8/11 16:00
 */
public class FindHeaderViewAdapter extends BaseQuickAdapter<FindHeaderDto, BaseViewHolder> {
    public FindHeaderViewAdapter() {
        super(R.layout.adapter_find_header);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder viewHolder, FindHeaderDto item) {
        ImageView view = viewHolder.getView(R.id.cv_find_adapter);
        ImageLoader.loadCircleWithBorder(view,item.getHeadImg(),2, Color.WHITE);
    }
}
