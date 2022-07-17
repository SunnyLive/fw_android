package com.fengwo.module_login.mvp.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_login.R;
import com.fengwo.module_login.mvp.dto.ReceiveGiftDto;

import java.util.List;

public class ReceiveGiftAdapter extends BaseQuickAdapter<ReceiveGiftDto.ListBean.RecordsBean, BaseViewHolder> {
    public ReceiveGiftAdapter(@Nullable List<ReceiveGiftDto.ListBean.RecordsBean> data) {
        super(R.layout.login_item_receivegift, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ReceiveGiftDto.ListBean.RecordsBean item) {

    }

}
