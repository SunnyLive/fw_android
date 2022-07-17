package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.MyGlideEngine;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.PkTeamInfo;
import com.fengwo.module_live_vedio.mvp.dto.StartPkAnimDto;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/10/10
 */
public class StartPkAnimAdapter extends BaseQuickAdapter<PkTeamInfo, BaseViewHolder> {

    boolean isWe = true;
    public StartPkAnimAdapter(@Nullable List<PkTeamInfo> data,boolean isWe) {
        super(R.layout.item_start_pk_anim, data);
        this.isWe = isWe;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PkTeamInfo item) {
        helper.setText(R.id.tv_name, item.getNickname());
        ImageLoader.loadCircleImg(helper.getView(R.id.iv_head), item.getHeadImg());
        if (isWe){
            ((CircleImageView)helper.getView(R.id.iv_head)).setBorderColor(Color.parseColor("#D225E1"));
        }else {
            ((CircleImageView)helper.getView(R.id.iv_head)).setBorderColor(Color.parseColor("#4F99F0"));
        }
    }
}
