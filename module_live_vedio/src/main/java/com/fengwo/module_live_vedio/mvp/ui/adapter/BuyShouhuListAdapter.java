package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.graphics.Color;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.BuyShouhuDto;

import java.util.List;

public class BuyShouhuListAdapter extends BaseQuickAdapter<BuyShouhuDto.GuardListBean, BaseViewHolder> {

    private int checkIndex = 0;

    public BuyShouhuListAdapter(@Nullable List<BuyShouhuDto.GuardListBean> data) {
        super(R.layout.live_item_buyshouhu, data);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        LinearLayout llBg = holder.getView(R.id.bg);
        if (checkIndex == position) {
            llBg.setBackgroundResource(R.drawable.pic_sh_yx);

            holder.setTextColor(R.id.tv_day_num, Color.parseColor("#ffffff"));
            holder.setTextColor(R.id.tv_number, Color.parseColor("#ffffff"));
            holder.setTextColor(R.id.tv_hz, Color.parseColor("#ffffff"));

        } else {
            llBg.setBackgroundResource(0);

            holder.setTextColor(R.id.tv_day_num, mContext.getResources().getColor(R.color.bg_603939));
            holder.setTextColor(R.id.tv_number, Color.parseColor("#63A6FD"));
            holder.setTextColor(R.id.tv_hz, Color.parseColor("#63A6FD"));

        }
    }

    public BuyShouhuDto.GuardListBean getSelected() {
        return mData.get(checkIndex);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, BuyShouhuDto.GuardListBean item) {
        helper.setText(R.id.tv_day_num, item.getDayNumX() + "天守护");
        helper.setText(R.id.tv_number, item.getItemPriceX() + "");
        helper.addOnClickListener(R.id.bg);
    }

    public void check(int position) {
        checkIndex = position;
        notifyDataSetChanged();
    }

    public int getIndex() {
        return checkIndex;
    }

    public List<BuyShouhuDto.GuardListBean.PrivilegeVOListBean> getPrivilege() {
        return getData().get(checkIndex).getPrivilegeVOList();
    }
}
