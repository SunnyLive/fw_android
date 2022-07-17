package com.fengwo.module_login.mvp.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_login.R;
import com.fengwo.module_login.mvp.dto.RecordDto;

import java.util.List;

public class ChongzhiRecordAdapter extends BaseQuickAdapter<RecordDto, BaseViewHolder> {
    public ChongzhiRecordAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, RecordDto item) {
        helper.setText(R.id.tv_time, TimeUtils.dealDateFormatToRecord(item.createTime));
        helper.setText(R.id.tv_price, item.money);
        helper.setText(R.id.tv_title, item.getPayType());
        String status = "";
        if (item.status == 1) {
            status = "未支付";
        } else {//2
            status = "已支付";
        }

        helper.setText(R.id.tv_status, status);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }
}
