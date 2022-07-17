package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.RechargeDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class RechargeAdapter extends BaseQuickAdapter<RechargeDto, BaseViewHolder> {

    public int checkPosition = 0;
    private final DecimalFormat df = new DecimalFormat("###.##");


    public RechargeAdapter(@Nullable List<RechargeDto> data) {
        super(R.layout.live_item_recharge, data);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        View root = holder.getView(R.id.bg);
        if (position == checkPosition) {
            root.setBackgroundResource(R.drawable.login_bg_chongzhi_selected);
        } else {
            root.setBackgroundResource(R.drawable.login_bg_chongzhi_normal);
        }
//        root.setOnClickListener(v -> {
//            checkPosition = position;
//            notifyDataSetChanged();
//        });
        holder.addOnClickListener(R.id.bg);
    }

    public RechargeDto getSelected() {
        return mData.get(checkPosition);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, RechargeDto item) {
        helper.setText(R.id.tv_huazuan, formatDiamond(item.diamondNum) + "花钻");
        helper.setText(R.id.tv_price, String.format("%s元", df.format(item.price)));
        if (item.diamondGive > 0) {
            helper.getView(R.id.tv_give).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_give, "赠送" + item.diamondGive + "花钻");
        } else helper.getView(R.id.tv_give).setVisibility(View.INVISIBLE);

    }

    private String formatDiamond(int diamond) {
        if (diamond < 10000) return String.valueOf(diamond);
        try {
            BigDecimal divide = new BigDecimal(diamond)
                    .divide(new BigDecimal(10000), 2, RoundingMode.HALF_UP);
            return df.format(divide) + "万";
        } catch (Exception e) {
            return String.valueOf(diamond);
        }
    }
}
