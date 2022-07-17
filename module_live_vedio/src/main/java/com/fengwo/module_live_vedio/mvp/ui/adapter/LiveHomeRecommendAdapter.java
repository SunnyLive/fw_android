package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.bean.ZhuboDto;
import com.fengwo.module_comment.ext.AnimExtKt;
import com.fengwo.module_comment.utils.DataFormatUtils;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_live_vedio.R;

import java.util.List;

public class LiveHomeRecommendAdapter extends BaseQuickAdapter<ZhuboDto, BaseViewHolder> {
    private final Lifecycle mLifecycle;

    public LiveHomeRecommendAdapter(Lifecycle mLifecycle, @Nullable List<ZhuboDto> data) {
        super(R.layout.live_item_home, data);
        this.mLifecycle = mLifecycle;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder helper, int position) {
        super.onBindViewHolder(helper, position);
        ZhuboDto item = mData.get(position);
        ImageLoader.loadImg(helper.getView(R.id.iv_thumb), item.thumb);
        helper.setText(R.id.tv_title, item.title);
        helper.setText(R.id.tv_name, item.nickname);
        helper.setText(R.id.tv_looknum, DataFormatUtils.formatNumbers(item.lookNums));
        helper.addOnClickListener(R.id.root);
//        helper.itemView.setOnClickListener(new FastClickListener() {
//            @Override
//            public void onNoFastClick(View v) {
//                //                if (item.status==2) {
//                LivingRoomActivity.start(mContext, (ArrayList<ZhuboDto>) mData, position);
////                } else {
////                    ArouteUtils.toPathWithId(ArouterApi.USER_DETAIL,item.channelId);
////                }
//            }
//        });
        if (item.isHaveRed == 1) {
            helper.setVisible(R.id.img_red_packet, true);
            ImageView redPacketIcon = helper.getView(R.id.img_red_packet);
            if (!(redPacketIcon.getTag() instanceof Boolean)) {
                AnimExtKt.startRotateValueAnimator(redPacketIcon, mLifecycle);
                redPacketIcon.setTag(true);
            }
        } else {
            helper.setVisible(R.id.img_red_packet, false);
        }
        ImageView ivGif = helper.getView(R.id.iv_gif);
        LinearLayout llTab = helper.getView(R.id.ll_tab);
        llTab.removeAllViews();

        ImageView mIvLiveLevel = helper.getView(R.id.mIvLiveLevel);
        try {
            if (item.channelLevel > 0) {
                int level2Res = ImageLoader.getResId("login_ic_type3_v" + item.channelLevel, R.drawable.class);
                mIvLiveLevel.setImageResource(level2Res);
            } else {
                mIvLiveLevel.setVisibility(View.GONE);
            }
        } catch (Resources.NotFoundException e) {

        }

        if (!TextUtils.isEmpty(item.lastChannelFrame)) {
            helper.setVisible(R.id.iv_thumb_bg, true);
            ImageLoader.loadImg((ImageView) helper.getView(R.id.iv_thumb_bg), item.lastChannelFrame);//蜂后边框
        } else {
            helper.setVisible(R.id.iv_thumb_bg, false);
        }
        //活动大于3  并且活动图片不为空
        if (item.activityId > 3 && !TextUtils.isEmpty(item.lastChannelLable) | !TextUtils.isEmpty(item.channelOuterLable)) {
            //  helper.setVisible(R.id.iv_activity_tag, false);//标签
            helper.setText(R.id.tv_recomment, "");
            if (!TextUtils.isEmpty(item.lastChannelLable)) {//  1 lastChannelLable 不等一空 显示这个
                ImageLoader.loadImg((ImageView) helper.getView(R.id.iv_activity_tag), item.lastChannelLable);
            }
            if (!TextUtils.isEmpty(item.channelOuterLable)) {//  2 channelOuterLable 不等一空 显示这个
                ImageLoader.loadImg((ImageView) helper.getView(R.id.iv_activity_tag), item.channelOuterLable);
                L.e("tag", item.channelOuterLable);
            }

        } else {
            //   helper.setVisible(R.id.iv_activity_tag, true);//标签
            if (item.pkStatus > 0) {//3  是否在pk  pk显示 pk  不pk显示占位图
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(DensityUtils.dp2px(mContext, 30), DensityUtils.dp2px(mContext, 2), 0, 0);
                helper.getView(R.id.tv_recomment).setLayoutParams(params);
                //  helper.setText(R.id.tv_recomment, "激情对战");
                helper.setImageResource(R.id.iv_activity_tag, R.drawable.ic_pk);
                //     helper.setVisible(R.id.iv_txt, false);
            } else {
                // helper.setText(R.id.tv_recomment, "热门推荐卡");
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(DensityUtils.dp2px(mContext, 8), DensityUtils.dp2px(mContext, 2), 0, 0);
                helper.getView(R.id.tv_recomment).setLayoutParams(params);
                helper.setImageResource(R.id.iv_activity_tag, R.drawable.bg_live);
                //      helper.setVisible(R.id.iv_txt, true);
            }
        }
        helper.setGone(R.id.iv_gif, item.status == 2);//开播显示
        helper.setGone(R.id.im_hmz, item.status != 2);
        //是否开播
        if (item.status == 2) {
            //  helper.setVisible(R.id.ll_tag, item.status == 2);
            ((ImageView) helper.getView(R.id.iv_thumb_bg)).setBackgroundResource(0);
            //   helper.getView(R.id.iv_gif).setVisibility(View.VISIBLE);
            ImageLoader.loadGif(helper.getView(R.id.iv_gif), R.drawable.live_cell_gif);

            helper.setText(R.id.tv_looknum, DataFormatUtils.formatNumbers(item.lookNums));//观看人数


        } else {
            //  helper.setVisible(R.id.ll_tag, item.status == 2);
            //    helper.getView(R.id.iv_gif).setVisibility(View.INVISIBLE);
            helper.setText(R.id.tv_looknum, DataFormatUtils.formatNumbers(item.weekProfit));//上周花蜜值

        }
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ZhuboDto item) {

    }

    @Override
    public int getItemCount() {
        if (mData.size() % 2 != 0) {
            return mData.size() - 1;
        }
        return mData.size();
    }
}
