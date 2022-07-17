package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.NewTouTiaoListDto;
import com.fengwo.module_live_vedio.mvp.dto.TouTiaoListDto;

import java.util.List;

public class NewToutiaoListAdapter extends BaseQuickAdapter<NewTouTiaoListDto, BaseViewHolder> {

    private int checkPosition = 0;

    public NewToutiaoListAdapter(@Nullable List<NewTouTiaoListDto> data) {
        super(R.layout.item_toutiao_list_new, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, NewTouTiaoListDto item) {
        ImageLoader.loadImg(helper.getView(R.id.iv), item.getGiftPackageIcon());
        if (item.getGiftPackagePrice() > 0)
            helper.setText(R.id.tv_price, String.valueOf(item.getGiftPackagePrice()));
        helper.setText(R.id.tv_name, item.getGiftPackageName());
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (checkPosition == position) {
            holder.itemView.setSelected(true);
            //holder.itemView.setBackgroundResource(R.drawable.live_bg_gift_selected_tt);
            holder.setTextColor(R.id.tv_price, Color.parseColor("#FFD93F"));
            holder.setTextColor(R.id.tv_name, Color.parseColor("#FFD93F"));
            holder.setGone(R.id.im_wbd,true);

        } else {
            holder.itemView.setSelected(false);
            //holder.setBackgroundColor(R.id.root, Color.TRANSPARENT);
            holder.setTextColor(R.id.tv_price, Color.parseColor("#ffffff"));
            holder.setTextColor(R.id.tv_name, Color.parseColor("#ffffff"));
            holder.setGone(R.id.im_wbd,false);

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
