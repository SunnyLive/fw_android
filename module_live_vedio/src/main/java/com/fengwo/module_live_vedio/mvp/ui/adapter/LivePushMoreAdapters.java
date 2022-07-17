package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.MakeExpansionDto;

import java.util.List;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/10/7
 */
public class LivePushMoreAdapters extends BaseQuickAdapter<MakeExpansionDto, BaseViewHolder> {


    public LivePushMoreAdapters(int layoutResId, @Nullable List<MakeExpansionDto> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MakeExpansionDto item) {
        TextView view = helper.getView(R.id.iv_item_more_img);
        ImageView im_pic = helper.getView(R.id.im_pic);
        im_pic.setImageResource(item.getIvImg());
        view.setText(item.getTitle());
        if (helper.getLayoutPosition() == getData().size() - 1){
            helper.getView(R.id.lin).setVisibility(View.GONE);
            helper.setGone(R.id.iv_widgets,true);
        }else{
            helper.getView(R.id.lin).setVisibility(View.VISIBLE);
            helper.setGone(R.id.iv_widgets,false);
        }

    }
}
