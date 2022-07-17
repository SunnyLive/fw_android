package com.fengwo.module_live_vedio.mvp.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.QuickTalkDto;

import java.util.List;

public class QuickSdMsgListAdapter extends BaseQuickAdapter<QuickTalkDto, BaseViewHolder> {

    public QuickSdMsgListAdapter(@Nullable List<QuickTalkDto> data) {
        super(R.layout.tag_quick_sd_msg_item, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, QuickTalkDto item) {
        helper.setText(R.id.tvTitle, item.getContent());
    }
}
