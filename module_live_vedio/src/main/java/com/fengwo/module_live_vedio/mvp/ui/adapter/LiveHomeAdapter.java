package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
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

public class LiveHomeAdapter extends BaseQuickAdapter<ZhuboDto, BaseViewHolder> {
    private final Lifecycle mLifecycle;

    public LiveHomeAdapter(Lifecycle mLifecycle) {
        super(R.layout.live_item_home);
        this.mLifecycle = mLifecycle;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (getItemViewType(position) == 0) {
            int dp10 = (int) mContext.getResources().getDimension(R.dimen.dp_10);
            if (position % 2 == 0) {
                holder.itemView.setPadding(0, 0, dp10, dp10);
            } else {
                holder.itemView.setPadding(dp10, 0, 0, dp10);
            }
        }
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ZhuboDto item) {
        //   ImageLoader.loadRouteImg(helper.getView(R.id.iv_thumb), "https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/activityList/1600064314000*activityList5073274365.png");
        ImageLoader.loadRouteImg(helper.getView(R.id.iv_thumb), item.thumb);
        helper.setText(R.id.tv_title, item.title);
        helper.setText(R.id.tv_name, item.nickname);
        ImageView mIvLiveLevel = helper.getView(R.id.mIvLiveLevel);
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

        helper.addOnClickListener(R.id.root);

        {
            //活动大于3  并且活动图片不为空
            if (item.activityId > 3 && !TextUtils.isEmpty(item.lastChannelLable) | !TextUtils.isEmpty(item.channelOuterLable)) {
                helper.setText(R.id.tv_recomment, "");
                //   helper.setVisible(R.id.iv_activity_tag, true);//标签
                if (!TextUtils.isEmpty(item.lastChannelLable)) {
                    ImageLoader.loadImg((ImageView) helper.getView(R.id.iv_activity_tag), item.lastChannelLable);
                }
                if (!TextUtils.isEmpty(item.channelOuterLable)) {
                    //            helper.setVisible(R.id.iv_thumb,false);
                    ImageLoader.loadImg((ImageView) helper.getView(R.id.iv_activity_tag), item.channelOuterLable);
                    L.e("tag", item.channelOuterLable);
                }

            } else {
                //        helper.setVisible(R.id.iv_activity_tag, false);//标签
                if (item.pkStatus > 0) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(DensityUtils.dp2px(mContext, 30), DensityUtils.dp2px(mContext, 2), 0, 0);
                    helper.getView(R.id.tv_recomment).setLayoutParams(params);
                    //   helper.setText(R.id.tv_recomment, "激情对战");
                    helper.setImageResource(R.id.iv_activity_tag, R.drawable.ic_pk);
                    //     helper.setVisible(R.id.iv_txt, false);
                } else {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(DensityUtils.dp2px(mContext, 8), DensityUtils.dp2px(mContext, 2), 0, 0);
                    helper.getView(R.id.tv_recomment).setLayoutParams(params);
                    helper.setText(R.id.tv_recomment, "");
                    helper.setImageResource(R.id.iv_activity_tag, R.drawable.pic_live_zw);
                    //      helper.setVisible(R.id.iv_txt, true);
                }
            }
        }
        helper.setGone(R.id.iv_gif, item.status == 2);//开播显示
        helper.setGone(R.id.im_hmz, item.status != 2);
        //是否开播
        if (item.status == 2) {
            //    helper.setVisible(R.id.ll_tag, item.status == 2);
            ((ImageView) helper.getView(R.id.iv_thumb_bg)).setBackgroundResource(0);
            //     helper.getView(R.id.iv_gif).setVisibility(View.VISIBLE);
            ImageLoader.loadGif(helper.getView(R.id.iv_gif), R.drawable.live_cell_gif);

            helper.setText(R.id.tv_looknum, DataFormatUtils.formatNumbers(item.lookNums));//观看人数


        } else {
            //  helper.setVisible(R.id.ll_tag, item.status == 2);
            //     helper.getView(R.id.iv_gif).setVisibility(View.INVISIBLE);
            helper.setText(R.id.tv_looknum, DataFormatUtils.formatNumbers(item.weekProfit));//上周花蜜值

        }
    }
}
