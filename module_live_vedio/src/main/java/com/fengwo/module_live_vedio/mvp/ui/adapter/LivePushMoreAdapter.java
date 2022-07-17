package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.faceunity.utils.BitmapUtil;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.LivePushMoreDto;
import com.umeng.socialize.utils.CommonUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/10/7
 */
public class LivePushMoreAdapter extends BaseQuickAdapter<LivePushMoreDto, BaseViewHolder> {


    public LivePushMoreAdapter(int layoutResId, @Nullable List<LivePushMoreDto> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, LivePushMoreDto item) {
        TextView view = helper.getView(R.id.iv_item_more_img);
        view.setText(item.getTitle());


        if(item.isOpen()&&item.getF_ivImg()!=0){
            view.setTextColor(Color.parseColor("#FE3C9C"));
            view.setCompoundDrawablesWithIntrinsicBounds(null,mContext.getResources().getDrawable(item.getF_ivImg()),null,null);
        }else {
            view.setTextColor(Color.parseColor("#9E9E9E"));
            view.setCompoundDrawablesWithIntrinsicBounds(null,mContext.getResources().getDrawable(item.getIvImg()),null,null);
        }

    }
}
