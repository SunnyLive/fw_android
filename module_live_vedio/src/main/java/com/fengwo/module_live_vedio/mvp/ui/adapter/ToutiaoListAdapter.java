package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.TouTiaoListDto;

import java.util.List;

public class ToutiaoListAdapter extends BaseQuickAdapter<TouTiaoListDto, BaseViewHolder> {

    private int checkPosition = 0;

    public ToutiaoListAdapter(@Nullable List<TouTiaoListDto> data) {
        super(R.layout.item_toutiao_list, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, TouTiaoListDto item) {
        ImageLoader.loadImg(helper.getView(R.id.iv), item.getGiftIcon());
        if (item.getGiftPrice()>0)helper.setText(R.id.tv_price,  String.valueOf(item.getGiftPrice()));
        helper.setText(R.id.tv_name, item.getGiftTypeText());
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (checkPosition == position) {
            holder.itemView.setBackgroundResource(R.drawable.live_bg_gift_selected);
        } else {
            holder.setBackgroundColor(R.id.root, Color.TRANSPARENT);
        }
        holder.addOnClickListener(R.id.root);
    }

    public void setCheckPosition(int p) {
        checkPosition = p;
        notifyDataSetChanged();
    }

    public int getCheckPosition() {
        return checkPosition;
    }
}
