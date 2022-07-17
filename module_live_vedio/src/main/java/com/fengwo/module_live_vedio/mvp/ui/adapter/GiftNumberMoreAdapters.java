package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.MakeExpansionDto;
import com.fengwo.module_websocket.bean.WenboGiftMsg;

import java.util.List;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/10/7
 */
public class GiftNumberMoreAdapters extends BaseQuickAdapter<MakeExpansionDto, BaseViewHolder> {
    String num = "";
    Context contentl;

    public GiftNumberMoreAdapters(Context content, int layoutResId, @Nullable List<MakeExpansionDto> data, String num) {
        super(layoutResId, data);
        this.contentl = content;
        this.num = num;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MakeExpansionDto item) {
        TextView view = helper.getView(R.id.iv_item_more_img);
        view.setText(item.getTitle());
        LinearLayout ll_back = helper.getView(R.id.ll_back);
        TextView tv_lin = helper.getView(R.id.tv_lin);
        if (helper.getLayoutPosition() == getData().size() - 1)
        tv_lin.setVisibility(View.GONE);
        if (item.getTitle().equals(num)) {
            if(getData().size()==1){
                ll_back.setBackgroundResource(R.drawable.bg_context);
            }else {
                if (helper.getLayoutPosition() == 0) {
                    ll_back.setBackgroundResource(R.drawable.bg_topt);
                } else if (helper.getLayoutPosition() == getData().size() - 1) {
                    ll_back.setBackgroundResource(R.drawable.bg_btnt);

                } else {
                    ll_back.setBackgroundColor(Color.parseColor("#FE3C9C"));
                }
            }

            view.setTextColor(Color.parseColor("#ffffff"));
        } else {
            ll_back.setBackgroundColor(0);
            view.setTextColor(view.getContext().getResources().getColor(R.color.black));
        }
    }
}
