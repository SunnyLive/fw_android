package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.StringFormatUtils;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.GiftDto;
import com.fengwo.module_live_vedio.mvp.ui.event.ChangeGiftEvent;

import java.util.List;

public class GiftAdapter extends BaseQuickAdapter<GiftDto, BaseViewHolder> {

    private int checkPosition = -1;
    private SparseArray<AnimatorSet> animationSetSparseArray;

    public GiftAdapter(Context mContext, @Nullable List<GiftDto> data) {
        super(R.layout.live_item_gift, data);
        this.mContext = mContext;
        animationSetSparseArray = new SparseArray<>();
    }

    public void setCheckPosition(int p) {
        checkPosition = p;
        RxBus.get().post(new ChangeGiftEvent(mData.get(p), p, true));
    }

    public void setCheckPosition1(int p) {
        if (p>mData.size())
            p = mData.size()-1;
        checkPosition = p;
        RxBus.get().post(new ChangeGiftEvent(mData.get(p), p, false));
    }

    public void setCheckPositions(int p) {
        checkPosition = TextUtils.isEmpty(mData.get(p).goodsName) ? p : -1;
        notifyDataSetChanged();

    }

    public int getCheckPosition() {
        return checkPosition;
    }

    public GiftDto getGift() {
        if (checkPosition != -1) {
            return getItem(checkPosition);
        }
        return null;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, GiftDto item) {
        int position = helper.getAdapterPosition();
        ImageLoader.loadImgFitCenter(helper.getView(R.id.iv), TextUtils.isEmpty(item.giftIcon) ? item.goodsIcon : item.giftIcon);
        helper.setText(R.id.tv_name, TextUtils.isEmpty(item.giftName) ? item.goodsName : item.giftName);
        if (item.giftPrice != 0) {
            helper.setText(R.id.tv_price, StringFormatUtils.formatDouble(item.giftPrice));
            helper.setGone(R.id.ll_price_parent, true);
        }

        //处理子nav选择的item结构
        // goodsType	    integer($int32)         物品类型：1礼物 2座驾 3贵族 4道具
        // goodsStatus      integer($int32)         物品状态：1未使用 2使用中 3已到期
        //如果是贵族的item  就显示按钮

        helper.setGone(R.id.tv_gift_validity_date, false);
        helper.setGone(R.id.tv_gift_date_remaining, false);
        helper.setGone(R.id.tv_remain_count, false);
        ////////////////////////////////////////////////////
        helper.setGone(R.id.tv_btn_gift, item.goodsType == 1 || item.goodsType == 2 || item.goodsType == 3);

        if (TextUtils.isEmpty(item.goodsName)) {
            if (checkPosition == -1) checkPosition = 0;
            helper.itemView.setSelected(checkPosition == helper.getAdapterPosition());
        } else {
            helper.setGone(R.id.tv_gift_validity_date, true);
            helper.setEnabled(R.id.tv_btn_gift, true);
            String total = item.goodsCount > 99 ? "99+" : item.goodsCount + "";
            helper.setText(R.id.tv_gift_count, total);
            if (item.goodsType == 4 || item.goodsType == 1) {
                if (checkPosition == -1) checkPosition = 0;
                helper.setGone(R.id.tv_gift_count, false);
                helper.setGone(R.id.tv_btn_gift, false);
                helper.setGone(R.id.tv_remain_count, true);
                helper.setText(R.id.tv_gift_validity_date, "总数量：" + item.goodsCount);
                if (item.goodsType == 1) {
                    helper.setText(R.id.tv_remain_count, getDays(item.remainValidDays));
                } else {
                    helper.setText(R.id.tv_remain_count, "剩余：" + item.remainCount);
                }
                if (checkPosition == helper.getAdapterPosition()){
                    Log.d("lucas","position:"+position+","+"adapter:"+this);
                }
                helper.itemView.setSelected(checkPosition == helper.getAdapterPosition());
            } else {
                helper.setGone(R.id.tv_gift_count, item.goodsCount > 1);
                helper.setGone(R.id.tv_btn_gift, true);
                helper.setText(R.id.tv_gift_validity_date, getDays(Integer.parseInt(item.goodsDuration)));
                switch (item.goodsStatus) {
                    case 1:
                        helper.itemView.setSelected(false);
                        helper.setText(R.id.tv_btn_gift, "立即使用");
                        helper.getView(R.id.tv_btn_gift).setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_gift_item_btn_red));
                        break;
                    case 2:
                        helper.setGone(R.id.tv_gift_date_remaining, true);
                        helper.itemView.setSelected(false);
                        helper.setText(R.id.tv_btn_gift, "续费");
                        //判断座驾是否开启
                        if ((item.goodsType == 2 && item.isOpened) || (item.goodsType == 3 && item.isMaxLevel)) {
                            checkPosition = position;
                            helper.itemView.setSelected(true);
                        } else if (item.goodsType == 3) {
                            helper.itemView.setSelected(false);
                        } else if (item.goodsType == 2) {
                            helper.itemView.setSelected(false);
                            helper.setText(R.id.tv_btn_gift, "立即启用");
                        }
                        helper.setText(R.id.tv_gift_date_remaining, getDays(item.remainDays));
                        helper.getView(R.id.tv_btn_gift).setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_gift_item_btn_red));
                        break;
                    case 3:
                        helper.itemView.setSelected(false);
                        helper.getView(R.id.tv_btn_gift).setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_gift_item_btn));
                        helper.setText(R.id.tv_btn_gift, "再次购买");
                        helper.setText(R.id.tv_gift_validity_date, "已失效");
                        break;
                }
            }

//            if (searchChoose()) {
//                checkPosition = 0;
//                helper.itemView.setSelected(checkPosition == helper.getAdapterPosition());
//            }

        }
        if (checkPosition != -1)
            ((Activity) mContext).findViewById(R.id.btn_send).setTag(getData().get(checkPosition));
        helper.getView(R.id.tv_btn_gift).setTag(item.goodsStatus);

        helper.addOnClickListener(R.id.root, R.id.tv_btn_gift);
    }

    private String getDays(int day) {
        return day == 0 ? "今日到期" : "有效期：" + day + "天";
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (!TextUtils.isEmpty(mData.get(position).goodsName)) return;
                if (checkPosition == position) {
                    return;
//                    checkPosition = -1;
//                    RxBus.get().post(new ChangeGiftEvent(null));
                } else {
                    AnimatorSet set = animationSetSparseArray.get(position);
                    if (null != set)
                        set.cancel();
                    checkPosition = position;
                    RxBus.get().post(new ChangeGiftEvent(mData.get(position), position, true));
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


    private boolean searchChoose() {

        for (GiftDto d : getData()) {
            if (d.isOpened && d.goodsStatus == 2 && d.goodsType == 2) {
                return false;
            }
            if (d.goodsStatus == 2 && d.goodsType == 3) {
                return false;
            }
        }
        return true;
    }


}
