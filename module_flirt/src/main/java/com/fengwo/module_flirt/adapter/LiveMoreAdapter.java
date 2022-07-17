package com.fengwo.module_flirt.adapter;

import android.graphics.Color;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_flirt.bean.LiveMoreBean;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.LivePushMoreDto;

import java.util.List;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/10/7
 */
public class LiveMoreAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {


    public LiveMoreAdapter(int layoutResId, @Nullable List<Integer> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Integer item) {
        TextView view = helper.getView(R.id.iv_item_more_img);
        view.setCompoundDrawablesWithIntrinsicBounds(null,mContext.getResources().getDrawable(item),null,null);


    }
}
