package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.base.BaseVH;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.GiftDto;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LiveEndGiftAdapter extends BaseQuickAdapter<GiftDto, BaseVH> {
    public LiveEndGiftAdapter() {
        super(R.layout.adapter_live_end_gift);
    }

    @Override
    protected void convert(@NonNull BaseVH helper, GiftDto item) {
        helper.setText(R.id.tv_gift_number, "x" + item.giftNumber);
        helper.setText(R.id.tv_gift_name, item.giftName);
        ImageView view = helper.getView(R.id.iv_gift);
        ImageLoader.loadImg(view, item.giftIcon);
    }
}
