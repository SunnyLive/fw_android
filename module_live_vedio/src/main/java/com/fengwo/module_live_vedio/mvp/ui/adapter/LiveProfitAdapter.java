package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.LiveProfitDto;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/23
 */
public class LiveProfitAdapter extends BaseQuickAdapter<LiveProfitDto.RecordsBean, BaseViewHolder> {
    private int type;
    public LiveProfitAdapter(@Nullable List<LiveProfitDto.RecordsBean> data,int type) {
        super(R.layout.item_live_profit,data);
        this.type = type;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, LiveProfitDto.RecordsBean item) {
        helper.setText(R.id.tv_nick,item.getNickname());
        helper.setText(R.id.tv_total,item.getReceive()+"");
        if (type == 0){
            Drawable diamond = mContext.getResources().getDrawable(R.drawable.live_ic_diamond);
            diamond.setBounds(0,0,diamond.getMinimumWidth(),diamond.getMinimumHeight());
            TextView textView = helper.getView(R.id.tv_total);
            textView.setCompoundDrawables(null,null,diamond,null);
        }
        ImageLoader.loadImg(helper.getView(R.id.tv_header),item.getHeadImg());
    }
}
