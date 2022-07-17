package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.BannedDto;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/23
 */
public class ManagerAdapter extends BaseQuickAdapter<BannedDto, BaseViewHolder> {

    public ManagerAdapter(@Nullable List<BannedDto> data) {
        super(R.layout.live_item_invite_host,data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, BannedDto item) {
        helper.setText(R.id.tv_name,item.getNickname());
        ImageLoader.loadCircleImg(helper.getView(R.id.iv_head),item.getHeadImg());
        ((ImageView)helper.getView(R.id.iv_level)).setImageResource(ImageLoader.getResId("login_ic_v" + item.getUserLevel(), R.drawable.class));
        helper.addOnClickListener(R.id.tv_invite_send);
    }
}
