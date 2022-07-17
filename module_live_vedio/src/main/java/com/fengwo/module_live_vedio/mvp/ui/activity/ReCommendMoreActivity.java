package com.fengwo.module_live_vedio.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.faceunity.ui.dialog.BaseDialogFragment;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseListActivity;
import com.fengwo.module_comment.bean.ZhuboDto;
import com.fengwo.module_comment.dialog.ExitDialog;
import com.fengwo.module_comment.event.RedPacketCountEvent;
import com.fengwo.module_comment.ext.AnimExtKt;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.DataFormatUtils;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.FastClickListener;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.IntentRoomActivityUrils;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_comment.widget.floatingview.FloatingView;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.utils.GridItemDecoration;
import com.google.gson.Gson;

import java.util.ArrayList;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/4
 */
public class ReCommendMoreActivity extends BaseListActivity<ZhuboDto> {

    @Override
    public Flowable setNetObservable() {
        return new RetrofitUtils().createApi(LiveApiService.class).getZhuboList(page + PAGE_SIZE, -1);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置Title
//        setTitleBackground(Color.WHITE);
        new ToolBarBuilder().showBack(true)
                .setBackIcon(R.drawable.ic_back_black)
                .setTitle("全部主播")
                .setTitleColor(R.color.text_33).build();
        // 设置上下拉刷新控件颜色
        SmartRefreshLayoutUtils.setTransparentBlackText(this, smartRefreshLayout);
        // 添加Item间距
        GridItemDecoration decoration = new GridItemDecoration(DensityUtils.dp2px(this, 10));
        decoration.setNeedPadding(true);
        recyclerView.addItemDecoration(decoration);
        //红包角标
        initRedPacketEventBus();
    }

    /**
     * 刷新红包角标
     */
    @SuppressLint("CheckResult")
    private void initRedPacketEventBus() {
        RxBus.get().toObservable(RedPacketCountEvent.class).compose(bindToLifecycle())
                .map(event -> {
                    KLog.i("RedPacketRefreshEventBus", "收到消息 : event : " + event.getChannelId());
                    if (adapter != null && adapter.getData() != null && !adapter.getData().isEmpty()) {
                        int i = 0;
                        for (ZhuboDto zhuboDto : adapter.getData()) {
                            if (zhuboDto.channelId == event.getChannelId()) {
                                int isHaveRed = event.isHasRedPacket() ? 1 : 0;
                                if (zhuboDto.isHaveRed != isHaveRed) {
                                    zhuboDto.isHaveRed = isHaveRed;
                                    event.setPositionRefresh(i);
                                    return event;
                                }
                                break;
                            }
                            i++;
                        }
                    }
                    return event;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    if (adapter == null || event.getPositionRefresh() == -1) {
                        return;
                    }
                    adapter.notifyDataSetChanged();
                    KLog.i("RedPacketRefreshEventBus", "收到消息 : event : " + event.getChannelId() + "  刷新UI");
                });
    }

    @Override
    public RecyclerView.LayoutManager setLayoutManager() {
        return new GridLayoutManager(this, 2);
    }

    @Override
    public int setItemLayoutRes() {
        return R.layout.live_item_home;
    }

    @Override
    public void bingViewHolder(BaseViewHolder helper, ZhuboDto item, int position) {
        if (item.isHaveRed == 1) {
            helper.setVisible(R.id.img_red_packet, true);
            ImageView redPacketIcon = helper.getView(R.id.img_red_packet);
            if (!(redPacketIcon.getTag() instanceof Boolean)) {
                AnimExtKt.startRotateValueAnimator(redPacketIcon, getLifecycle());
                redPacketIcon.setTag(true);
            }
        } else {
            helper.setVisible(R.id.img_red_packet, false);
        }
        ImageLoader.loadRouteImg(helper.getView(R.id.iv_thumb), item.thumb);
        helper.setText(R.id.tv_title, item.title);
        helper.setText(R.id.tv_name, item.nickname);
        helper.setText(R.id.tv_looknum, DataFormatUtils.formatNumbers(item.lookNums));
        helper.itemView.setOnClickListener(new FastClickListener() {
                                               @Override
                                               public void onNoFastClick(View v) {
                                                   if (item.status == 2) {
                                                       ArrayList<ZhuboDto> data1 = (ArrayList<ZhuboDto>) adapter.getData();
                                                       ////这里过滤掉position 和 data 对应不上。所以不需要过滤 内部去做过滤
//                                                       ArrayList<ZhuboDto> collect = (ArrayList<ZhuboDto>) StreamSupport.stream(data1)
//                                                               .filter(e -> e.status == 2)
//                                                               .collect(Collectors.toList());
                                                       if (FloatingView.getInstance().isShow()) {
                                                           showExitDialog(data1, position);
                                                       } else {
                                                           IntentRoomActivityUrils.setRoomActivity(data1.get(position).channelId, data1);
                                                           //LivingRoomActivity.start(ReCommendMoreActivity.this, data1, position);
                                                       }
                                                   } else {
                                                       ArouteUtils.toPathWithId(ArouterApi.USER_DETAIL, item.channelId);
                                                   }
                                               }
                                           }
        );
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
            helper.setText(R.id.tv_recomment, "");
            //   helper.setVisible(R.id.iv_activity_tag, false);//标签
            if (!TextUtils.isEmpty(item.lastChannelLable)) {
                ImageLoader.loadImg((ImageView) helper.getView(R.id.iv_activity_tag), item.lastChannelLable);
            }
            if (!TextUtils.isEmpty(item.channelOuterLable)) {
                ImageLoader.loadImg((ImageView) helper.getView(R.id.iv_activity_tag), item.channelOuterLable);
            }

        } else {
            //   helper.setVisible(R.id.iv_activity_tag, true);//标签
            if (item.pkStatus > 0) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(DensityUtils.dp2px(this, 30), DensityUtils.dp2px(this, 2), 0, 0);
                helper.getView(R.id.tv_recomment).setLayoutParams(params);
                //   helper.setText(R.id.tv_recomment, "激情对战");
                helper.setImageResource(R.id.iv_activity_tag, R.drawable.ic_pk);
                //     helper.setVisible(R.id.iv_txt, false);
            } else {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(DensityUtils.dp2px(this, 8), DensityUtils.dp2px(this, 2), 0, 0);
                helper.getView(R.id.tv_recomment).setLayoutParams(params);
                //   helper.setText(R.id.tv_recomment, "热门推荐卡");
                helper.setImageResource(R.id.iv_activity_tag, R.drawable.pic_live_zw);
                //      helper.setVisible(R.id.iv_txt, true);
            }
        }
        helper.setGone(R.id.iv_gif, item.status == 2);//开播显示
        helper.setGone(R.id.im_hmz, item.status != 2);
        //是否开播
        if (item.status == 2) {
            // helper.setVisible(R.id.ll_tag, item.status == 2);
            ((ImageView) helper.getView(R.id.iv_thumb_bg)).setBackgroundResource(0);
            //   helper.getView(R.id.iv_gif).setVisibility(View.VISIBLE);
            ImageLoader.loadGif(helper.getView(R.id.iv_gif), R.drawable.live_cell_gif);

            helper.setText(R.id.tv_looknum, DataFormatUtils.formatNumbers(item.lookNums));//观看人数


        } else {
            //    helper.setVisible(R.id.ll_tag, item.status == 2);
            //       helper.getView(R.id.iv_gif).setVisibility(View.INVISIBLE);
            helper.setText(R.id.tv_looknum, DataFormatUtils.formatNumbers(item.weekProfit));//上周花蜜值

        }

//        if (item.status == 2 && item.activityId == 1 && item.activityStatus > 0 && item.activityStatus < 5) {
//            helper.setVisible(R.id.ll_tag, false);
//            helper.setVisible(R.id.iv_activity_tag, item.status == 2);
//            ((ImageView) helper.getView(R.id.iv_activity_tag)).setImageResource(ImageLoader.getResId("ic_angel_home" + item.activityStatus, R.drawable.class));
//        } else if (item.status == 2 && item.activityId == 2 && item.activityStatus > 0 && item.activityStatus < 5) {
//            helper.setVisible(R.id.ll_tag, false);
//            helper.setVisible(R.id.iv_activity_tag, true);
//            ((ImageView) helper.getView(R.id.iv_activity_tag)).setImageResource(ImageLoader.getResId("ic_queen_home" + item.activityStatus, R.drawable.class));
//            if (item.activityStatus < 4) {
//                ((ImageView) helper.getView(R.id.iv_thumb_bg)).setBackgroundResource(ImageLoader.getResId("bg_queen_home" + item.activityStatus, R.drawable.class));
//            }
//        } else if (item.status == 2 && item.activityId == 3 && item.activityStatus > 0 && !TextUtils.isEmpty(item.channelOuterLable) && item.pkStatus > 0 && item.activityStatus == 5 ||
//                item.status == 2 && item.activityId == 3 && item.activityStatus > 0 && !TextUtils.isEmpty(item.channelOuterLable) && item.activityStatus < 5) {//pk排位赛
//            helper.setVisible(R.id.ll_tag, false);
//            helper.setVisible(R.id.iv_activity_tag, true);
//            ImageLoader.loadImg((ImageView) helper.getView(R.id.iv_activity_tag), item.channelOuterLable);
//            if (!TextUtils.isEmpty(item.lastChannelFrame)) {
//                ImageLoader.loadUrl((ImageView) helper.getView(R.id.iv_thumb_bg), item.lastChannelFrame);//蜂后边框
//            }
//        } else if (item.status == 2 && item.activityId == 3 && item.activityStatus > 0 && item.pkStatus == 0 && !TextUtils.isEmpty(item.lastChannelLable)) {//兼容蜂后活动 标签边框显示
//            helper.setVisible(R.id.ll_tag, false);
//            helper.setVisible(R.id.iv_activity_tag, true);
//            if (!TextUtils.isEmpty(item.lastChannelLable)) {
//                ImageLoader.loadImg((ImageView) helper.getView(R.id.iv_activity_tag), item.lastChannelLable);
//            }
//            if (!TextUtils.isEmpty(item.lastChannelFrame)) {
//                ImageLoader.loadUrl((ImageView) helper.getView(R.id.iv_thumb_bg), item.lastChannelFrame);//蜂后边框
//            }
//        }else if (item.activityId>3&& !TextUtils.isEmpty(item.channelOuterLable)){
//            helper.setVisible(R.id.ll_tag, false);
//            helper.setVisible(R.id.iv_activity_tag, true);
//            if (!TextUtils.isEmpty(item.channelOuterLable)) {
//                ImageLoader.loadImg((ImageView) helper.getView(R.id.iv_activity_tag), item.channelOuterLable);
//            }else if (!TextUtils.isEmpty(item.lastChannelLable)){
//                ImageLoader.loadImg((ImageView) helper.getView(R.id.iv_activity_tag), item.lastChannelLable);
//            }
//            if (!TextUtils.isEmpty(item.lastChannelFrame)) {
//                ImageLoader.loadUrl((ImageView) helper.getView(R.id.iv_thumb_bg), item.lastChannelFrame);//蜂后边框
//            }
//        } else {
//            helper.setVisible(R.id.ll_tag, item.status == 2);
//            helper.setVisible(R.id.iv_activity_tag, false);
//            ((ImageView) helper.getView(R.id.iv_thumb_bg)).setBackgroundResource(0);
//            if (item.pkStatus > 0) {
//                helper.getView(R.id.iv_gif).setVisibility(View.INVISIBLE);
//                helper.setText(R.id.tv_recomment, "激情对战");
//                  helper.setBackgroundRes(R.id.ll_tag, R.drawable.ic_pk);
//                helper.setVisible(R.id.iv_txt, false);
//            } else {
//                helper.getView(R.id.iv_gif).setVisibility(View.VISIBLE);
//                ImageLoader.loadGif(helper.getView(R.id.iv_gif), R.drawable.live_cell_gif);
//                helper.setText(R.id.tv_recomment, "");
//                   helper.setBackgroundRes(R.id.ll_tag, R.drawable.bg_live);
//                helper.setVisible(R.id.iv_txt, true);
//            }
//        }
    }

    public void showExitDialog(ArrayList<ZhuboDto> map, int position) {
        FloatingView floatingView = FloatingView.getInstance();
        ExitDialog dialog = new ExitDialog();
        dialog.setNegativeButtonText("取消")
                .setPositiveButtonText("确定退出")
                .addDialogClickListener(new BaseDialogFragment.OnClickListener() {
                    @Override
                    public void onConfirm() {
                        //LivingRoomActivity.start(ReCommendMoreActivity.this, map, position);
                        IntentRoomActivityUrils.setRoomActivity(map.get(position).channelId, map);
                    }

                    @Override
                    public void onCancel() {

                    }
                })
                .setGear(floatingView.getGear())
                .setNickname(floatingView.getNickname())
                .setExpireTime(floatingView.getExpireTime())
                .setHeadImg(floatingView.getHeadImg())
                .setRoomId(floatingView.getRoomId())
                .setTip("退出达人房间，印象值将归零\n是否要退出")
                .show(getSupportFragmentManager(), "");
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_baselist;
    }
}
