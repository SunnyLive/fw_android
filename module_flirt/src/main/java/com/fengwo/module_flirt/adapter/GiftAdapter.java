package com.fengwo.module_flirt.adapter;

import android.animation.AnimatorSet;
import android.graphics.Color;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.StringFormatUtils;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.bean.GiftDto;

import java.util.List;

public class GiftAdapter extends BaseQuickAdapter<GiftDto, BaseViewHolder> {

    private int checkPosition;
    private SparseArray<AnimatorSet> animationSetSparseArray;

    public GiftAdapter(@Nullable List<GiftDto> data) {
        super(R.layout.live_item_gift, data);
        checkPosition = 0;
        animationSetSparseArray = new SparseArray<>();
    }

    public void setCheckPosition(int p) {
        checkPosition = p;
//        RxBus.get().post(new ChangeGiftEvent(mData.get(p)));
        notifyDataSetChanged();
    }

    public GiftDto getGift() {
        if (checkPosition != -1) {
            return getItem(checkPosition);
        }
        return null;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, GiftDto item) {
        ImageLoader.loadImgFitCenter(helper.getView(R.id.iv), item.smallImgPath);
        helper.setText(R.id.tv_name, item.giftName);
        helper.setText(R.id.tv_price, StringFormatUtils.formatDouble(item.price));
        if (item.addDurationNum > 0) {
            helper.setGone(R.id.tv_time, true);
            helper.setText(R.id.tv_time, "+"+item.addDurationNum + "印象");
        } else {
            helper.setGone(R.id.tv_time, false);
        }
        helper.addOnClickListener(R.id.root);
    }

    private String transMin(int min) {
        if (min <= 60) {
            return "+" + min + "分钟";
        } else {
            int hours = min / 60;
            int m = min % 60;
            return "+" + hours + "小时" + m + "分钟";
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (checkPosition == position) {
            holder.itemView.setSelected(true);
            holder.itemView.setBackgroundResource(R.drawable.live_bg_gift_selected);
        } else {
            holder.setBackgroundColor(R.id.root, Color.TRANSPARENT);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPosition == position) {
                    return;
//                    checkPosition = -1;
//                    RxBus.get().post(new ChangeGiftEvent(null));
                } else {
                    animationSetSparseArray.get(position);
                    AnimatorSet set = animationSetSparseArray.get(position);
                    if (null != set)
                        set.cancel();
                    checkPosition = position;
//                    RxBus.get().post(new ChangeGiftEvent(mData.get(position)));
                }
                notifyDataSetChanged();
            }
        });
        if (checkPosition == position) {
            ImageView iv = holder.getView(R.id.iv);
            ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0.8f, 1f, 0.8f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setRepeatMode(Animation.REVERSE);
            scaleAnimation.setRepeatCount(Animation.INFINITE);
            scaleAnimation.setDuration(400);
            iv.startAnimation(scaleAnimation);
        }
    }
}
