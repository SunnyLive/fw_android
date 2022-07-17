package com.fengwo.module_login.mvp.ui.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_login.R;
import com.fengwo.module_login.mvp.dto.MyCarDto;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/10/16
 */
public class MyCarAdapter extends BaseQuickAdapter<MyCarDto.RecordsBean, BaseViewHolder> {

    public MyCarAdapter(@Nullable List<MyCarDto.RecordsBean> data) {
        super(R.layout.login_item_mycar,data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MyCarDto.RecordsBean item) {
        helper.setText(R.id.tv_name,item.getMotoringName());
        if (1==item.getIsOpened()) {
            helper.setText(R.id.tv_buy, "续费");
            helper.getView(R.id.tv_use).setVisibility(View.VISIBLE);
            helper.setVisible(R.id.tv_buy,item.getMotoringType() == 0);
        }else {
            helper.setVisible(R.id.tv_buy,true);
            helper.setText(R.id.tv_buy, "使用");
            helper.getView(R.id.tv_use).setVisibility(View.GONE);
        }
        if (Integer.parseInt(item.getExpireTime().substring(0,4))-Integer.parseInt(TimeUtils.getCurrentYear())>50){
            helper.setText(R.id.tv_price, "活动专属");
            helper.getView(R.id.tv_limit).setVisibility(View.GONE);
        }else {
            helper.getView(R.id.tv_limit).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_price, TimeUtils.dealDateFormatToRecord(item.getExpireTime()));
        }
        ImageLoader.loadImgFitCenter(helper.getView(R.id.iv_img),item.getMotoringThumb());
        helper.addOnClickListener(R.id.tv_buy);
    }

}
