package com.fengwo.module_live_vedio.mvp.ui.fragment;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.faceunity.ui.dialog.BaseDialogFragment;
import com.fengwo.module_comment.base.BaseListFragment;
import com.fengwo.module_comment.bean.ZhuboDto;
import com.fengwo.module_comment.dialog.ExitDialog;
import com.fengwo.module_comment.event.RedPacketCountEvent;
import com.fengwo.module_comment.event.ToTopEvent;
import com.fengwo.module_comment.ext.AnimExtKt;
import com.fengwo.module_comment.utils.DarkUtil;
import com.fengwo.module_comment.utils.DataFormatUtils;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.IntentRoomActivityUrils;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_comment.widget.floatingview.FloatingView;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.mvp.ui.activity.RankPkActivity;
import com.fengwo.module_live_vedio.mvp.ui.activity.RankTopActivity;
import com.fengwo.module_live_vedio.mvp.ui.activity.RankTuhaoActivity;
import com.fengwo.module_live_vedio.utils.GridItemDecoration;

import java.util.ArrayList;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LiveHomeTabFragment extends BaseListFragment<ZhuboDto> implements View.OnClickListener {

    private final int MAX_SCROLL = 400;
    private int menuId;
    private String name;
    private boolean isVisiable;
    private int totalDy = 0;

    public static LiveHomeTabFragment newInstance(int menuId, String name) {
        Bundle args = new Bundle();
        args.putInt("menuid", menuId);
        args.putString("name", name);
        LiveHomeTabFragment fragment = new LiveHomeTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menuId = getArguments().getInt("menuid", -1);
        name = getArguments().getString("name");
    }

    @Override
    protected int setContentView() {
        return R.layout.live_fragment_hometab;
    }


    @SuppressLint("CheckResult")
    @Override
    public void initUI(Bundle savedInstanceState) {
        SmartRefreshLayoutUtils.setTransparentBgWithWhileText(getActivity(), smartRefreshLayout);
        RxBus.get().toObservable(ToTopEvent.class).compose(bindToLifecycle()).subscribe(new Consumer<ToTopEvent>() {
            @Override
            public void accept(ToTopEvent toTopEvent) throws Exception {
                if (!isVisiable) return;
                recyclerView.smoothScrollToPosition(0);
                recyclerView.setBackgroundColor(Color.argb(0, 255, 255, 255));
            }
        });
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
    protected void initRv() {
        super.initRv();
        GridItemDecoration decoration = new GridItemDecoration(DensityUtils.dp2px(getContext(), 10));
        decoration.setNeedPadding(true);
        recyclerView.addItemDecoration(decoration);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (!DarkUtil.isDarkTheme(getActivity())) {
                    totalDy -= dy;
                    int alpha = Math.min(Math.abs(totalDy), MAX_SCROLL) * 255 / MAX_SCROLL;
                    recyclerView.setBackgroundColor(Color.argb(alpha, 255, 255, 255));
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisiable) {
            //    onRefresh();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisiable = isVisibleToUser;
        if (isVisiable)
            onRefresh();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_totuhao) {
            startActivity(RankTuhaoActivity.class);
        } else if (id == R.id.btn_totop) {
            startActivity(RankTopActivity.class);
        } else if (id == R.id.btn_topk) {
            startActivity(RankPkActivity.class);
        }
    }

    @Override
    public Flowable setNetObservable() {
        String p = page + PAGE_SIZE;
        return new RetrofitUtils().createApi(LiveApiService.class).getZhuboList(p, menuId);
    }

    @Override
    public RecyclerView.LayoutManager setLayoutManager() {
        return new GridLayoutManager(getActivity(), 2);
    }

    @Override
    public int setItemLayoutRes() {
        return R.layout.live_item_home;
    }

    @Override
    public void bingViewHolder(BaseViewHolder helper, ZhuboDto item, int position) {
        ImageLoader.loadRouteImg(helper.getView(R.id.iv_thumb), item.thumb);
        helper.setText(R.id.tv_title, item.title);
        helper.setText(R.id.tv_name, item.nickname);
        helper.setText(R.id.tv_looknum, DataFormatUtils.formatNumbers(item.lookNums));
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
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FloatingView.getInstance().isShow()) {
                    showFVExitDialog(getData(), position);
                } else {
                    IntentRoomActivityUrils.setRoomActivity(getData().get(position).channelId, getData(), position);
                    //        LivingRoomActivity.start(getActivity(), getData(), position);
                }
            }
        });

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
            //  helper.setVisible(R.id.iv_activity_tag, false);//标签
            if (!TextUtils.isEmpty(item.lastChannelLable)) {
                ImageLoader.loadImg((ImageView) helper.getView(R.id.iv_activity_tag), item.lastChannelLable);
            }
            if (!TextUtils.isEmpty(item.channelOuterLable)) {
                ImageLoader.loadImg((ImageView) helper.getView(R.id.iv_activity_tag), item.channelOuterLable);
            }

        } else {
            //      helper.setVisible(R.id.iv_activity_tag, true);//标签
            if (item.pkStatus > 0) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(DensityUtils.dp2px(getActivity(), 30), DensityUtils.dp2px(getActivity(), 2), 0, 0);
                helper.getView(R.id.tv_recomment).setLayoutParams(params);
                //   helper.setText(R.id.tv_recomment, "激情对战");
                helper.setImageResource(R.id.iv_activity_tag, R.drawable.ic_pk);
                //     helper.setVisible(R.id.iv_txt, false);
            } else {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(DensityUtils.dp2px(getActivity(), 8), DensityUtils.dp2px(getActivity(), 2), 0, 0);
                helper.getView(R.id.tv_recomment).setLayoutParams(params);
                //        helper.setText(R.id.tv_recomment, "热门推荐卡");
                helper.setImageResource(R.id.iv_activity_tag, R.drawable.pic_live_zw);
                //      helper.setVisible(R.id.iv_txt, true);
            }
        }
        helper.setGone(R.id.iv_gif, item.status == 2);//开播显示  开播显示gif   反则显示花蜜值
        helper.setGone(R.id.im_hmz, item.status != 2);
        //是否开播
        if (item.status == 2) {
            //   helper.setVisible(R.id.ll_tag, item.status == 2);
            ((ImageView) helper.getView(R.id.iv_thumb_bg)).setBackgroundResource(0);
            //    helper.getView(R.id.iv_gif).setVisibility(View.VISIBLE);
            ImageLoader.loadGif(helper.getView(R.id.iv_gif), R.drawable.live_cell_gif);

            helper.setText(R.id.tv_looknum, DataFormatUtils.formatNumbers(item.lookNums));//观看人数


        } else {
            //    helper.setVisible(R.id.ll_tag, item.status == 2);
            //     helper.getView(R.id.iv_gif).setVisibility(View.INVISIBLE);
            helper.setText(R.id.tv_looknum, DataFormatUtils.formatNumbers(item.weekProfit));//上周花蜜值

        }

    }

    @Override
    public String setEmptyContent() {
        return null;
    }

    /**
     * 关闭悬浮窗提示
     *
     * @param collect
     * @param position
     */
    private void showFVExitDialog(ArrayList<ZhuboDto> collect, int position) {
        FloatingView floatingView = FloatingView.getInstance();
        if (floatingView.isShow()) {
            ExitDialog dialog = new ExitDialog();
            dialog.setNegativeButtonText("取消")
                    .setPositiveButtonText("确定退出")
                    .setTip("进入直播间会退出达人房间\n印象值将归零，是否要退出")
                    .setGear(floatingView.getGear())
                    .setNickname(floatingView.getNickname())
                    .setRoomId(floatingView.getRoomId())
                    .setExpireTime(floatingView.getExpireTime())
                    .setHeadImg(floatingView.getHeadImg())
                    .setExitType(ExitDialog.ENTER_LIVING)
                    .addDialogClickListener(new BaseDialogFragment.OnClickListener() {
                        @Override
                        public void onConfirm() {
                            IntentRoomActivityUrils.setRoomActivity(collect.get(position).channelId, collect, position);
                            //    LivingRoomActivity.start(getActivity(), collect, position);
                        }

                        @Override
                        public void onCancel() {
                        }
                    });
            dialog.show(getChildFragmentManager(), "");
        } else {
            IntentRoomActivityUrils.setRoomActivity(collect.get(position).channelId, collect, position);
            //     LivingRoomActivity.start(getActivity(), collect, position);
        }
    }
}

