package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.ThisGiftDto;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/28
 */
public class ThisGiftAdapter extends BaseQuickAdapter<ThisGiftDto.RecordsBean, BaseViewHolder> {
    private int type;
    public ThisGiftAdapter(@Nullable List<ThisGiftDto.RecordsBean> data) {
        super(R.layout.item_live_profit,data);
        this.type = type;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ThisGiftDto.RecordsBean item) {
        helper.setText(R.id.tv_nick,item.getUserNickname());
        helper.setText(R.id.tv_total,item.getGiftName()+item.getQuantity()+"个");
        ImageLoader.loadImg(helper.getView(R.id.tv_header),item.getUserHeadImg());
    }
}
