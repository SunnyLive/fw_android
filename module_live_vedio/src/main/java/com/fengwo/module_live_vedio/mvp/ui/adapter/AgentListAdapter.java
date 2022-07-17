package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.base.BaseVH;
import com.fengwo.module_comment.utils.DataFormatUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.BrokerRankDto;

import de.hdodenhof.circleimageview.CircleImageView;


public class AgentListAdapter extends BaseQuickAdapter<BrokerRankDto, BaseVH> {
    public AgentListAdapter() {
        super(R.layout.adapter_agent_list);
    }

    @Override
    protected void convert(@NonNull BaseVH helper, BrokerRankDto item) {
            int pos = helper.getAdapterPosition() + 4;
            helper.setText(R.id.tv_list_num, "" + pos);
            helper.setText(R.id.tv_list_name, item.getUserNickname());
            String values = DataFormatUtils.formatNumbers(Double.parseDouble(item.getTotalDivide()));
            helper.setText(R.id.tv_list_flowers, values + mContext.getString(R.string.flower_values));
            CircleImageView view = helper.getView(R.id.iv_list_head);
            ImageLoader.loadImg(view, item.getUserHeadImg());
    }
}
