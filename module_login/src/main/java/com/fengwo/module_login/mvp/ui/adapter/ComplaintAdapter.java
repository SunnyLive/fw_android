package com.fengwo.module_login.mvp.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_login.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/1
 */
public class ComplaintAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public ComplaintAdapter(@Nullable List<String> data) {
        super(R.layout.item_complaint_list,data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        if (helper.getLayoutPosition() == 0){
            helper.setImageResource(R.id.iv_pic,R.drawable.ic_add_gray);
            helper.setVisible(R.id.iv_close, false);
            helper.setBackgroundRes(R.id.iv_pic,R.drawable.shape_dashgap_gray);
        }else {
            ImageLoader.loadImg(helper.getView(R.id.iv_pic), item);
            helper.addOnClickListener(R.id.iv_close);
        }
    }
}
