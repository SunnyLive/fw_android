package com.fengwo.module_login.mvp.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_login.R;
import com.fengwo.module_login.mvp.dto.AllCarDto;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/10/16
 */
public class AllCarAdapter extends BaseQuickAdapter<AllCarDto, BaseViewHolder> {
    public AllCarAdapter(@Nullable List<AllCarDto> data) {
        super(R.layout.login_item_allcar,data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, AllCarDto item) {
        helper.setText(R.id.tv_name,item.getMotoringName());
        helper.setText(R.id.tv_price,item.getMotoringPrice()+"花钻");
        helper.setText(R.id.tv_buy,"购买");
        ImageLoader.loadImgFitCenter(helper.getView(R.id.iv_img),item.getMotoringImg());
        helper.addOnClickListener(R.id.tv_buy);
    }
}
