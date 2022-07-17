package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.PkResultDto;
import com.fengwo.module_live_vedio.mvp.dto.TeamPkItemResultDto;
import com.fengwo.module_live_vedio.mvp.dto.TeamPkResultDto;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/8
 */
public class PkGroupResultAdapter extends BaseQuickAdapter<TeamPkItemResultDto , BaseViewHolder> {

    public PkGroupResultAdapter(@Nullable List<TeamPkItemResultDto> data) {
        super(R.layout.item_group_pk,data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, TeamPkItemResultDto item) {
        helper.setText(R.id.tvSelfNickname,item.getNickname());
        helper.setText(R.id.tvEnemyNickname,item.getObjectNickName());
        ImageView ivSelfStatus =  helper.getView(R.id.tvSelfStatus);
        ImageView ivEnemyStatus =  helper.getView(R.id.tvEnemyStatus);
        if (item.getIsWin()>0) {
           ivSelfStatus.setImageResource(R.drawable.ic_pk_group_single_win);
           ivEnemyStatus.setImageResource(R.drawable.ic_pk_group_single_fail);
        }else if (item.getIsWin() < 0){
            ivSelfStatus.setImageResource(R.drawable.ic_pk_group_single_fail);
            ivEnemyStatus.setImageResource(R.drawable.ic_pk_group_single_win);
        }else {
            ivSelfStatus.setImageResource(R.drawable.ic_pk_group_single_draw);
            ivEnemyStatus.setImageResource(R.drawable.ic_pk_group_single_draw);
        }
        ImageLoader.loadImg(helper.getView(R.id.ivSelfHeader),item.getHeadImg());
        ImageLoader.loadImg(helper.getView(R.id.ivEnemyHeader),item.getObjectHeadImg());
        if (item.getIsOver()==0){//逃跑
            helper.getView(R.id.ivSelfRunAway).setVisibility(View.VISIBLE);
        }else {
            helper.getView(R.id.ivSelfRunAway).setVisibility(View.GONE);
        }
        if (item.getObjectIsOver()==0){//逃跑
            helper.getView(R.id.ivEnemyRunAway).setVisibility(View.VISIBLE);
        }else {
            helper.getView(R.id.ivEnemyRunAway).setVisibility(View.GONE);
        }
    }
}
