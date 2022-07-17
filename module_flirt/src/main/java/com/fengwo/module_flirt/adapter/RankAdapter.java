package com.fengwo.module_flirt.adapter;

import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_flirt.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_flirt.bean.FlirtRankBean;

import java.util.List;

public class RankAdapter extends BaseQuickAdapter<FlirtRankBean, BaseViewHolder> {

    public RankAdapter() {
        super(R.layout.item_rank);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, FlirtRankBean item) {
        helper.setText(R.id.tv_rank,helper.getLayoutPosition()+4+"");
        helper.setText(R.id.tv_name,item.nickname);
        helper.setText(R.id.tv_meilizhi,""+(int)item.charm);
        ImageLoader.loadImg(helper.getView(R.id.iv_header),item.headImg);
        CheckBox checkBox =  helper.getView(R.id.btn_attention);
        if (item.isAttention ==1){
            checkBox.setText("已关注");
            checkBox.setChecked(true);
        }else {
            checkBox.setText("关注");
            checkBox.setChecked(false);
        }
        helper.addOnClickListener(R.id.btn_attention);
    }


}
