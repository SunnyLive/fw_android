package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.AttentionHostDto;

import java.util.List;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/10/15
 */
public class InviteHostAdapter extends BaseQuickAdapter<AttentionHostDto, BaseViewHolder> {

    public InviteHostAdapter(@Nullable List<AttentionHostDto> data) {
        super(R.layout.item_attention_list,data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper,AttentionHostDto item) {
        helper.setText(R.id.tv_nick,item.getNickname());
        ImageLoader.loadCircleImg(helper.getView(R.id.iv_head),item.getHeadImg());
        helper.addOnClickListener(R.id.iv_invite);
        if (!TextUtils.isEmpty(item.getUserLevel())){
            int level = Integer.parseInt(item.getUserLevel());
            if (level > 46) {
                level = 46;
            }
            helper.setImageResource(R.id.iv_user_level, ImageLoader.getResId("login_ic_v" + level, R.drawable.class));
        }
        int level2 = item.getChannelLevel();
        if (level2 <= 0) {
            level2 = 1;
        }

        if (level2 > 50) {
            level2 = 50;
        }
        helper.setImageResource(R.id.iv_host_level, ImageLoader.getResId("login_ic_type3_v" + level2, R.drawable.class));
    }
}
