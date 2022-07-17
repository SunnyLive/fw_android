package com.fengwo.module_flirt.UI.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.chad.library.adapter.base.BaseViewHolder;
import com.faceunity.ui.dialog.BaseDialogFragment;
import com.fengwo.module_chat.mvp.api.ChatService;
import com.fengwo.module_comment.base.BaseListFragment;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.ZhuboDto;
import com.fengwo.module_comment.dialog.ExitDialog;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.IntentRoomActivityUrils;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.RxUtils;

import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.widget.RoundImageView;
import com.fengwo.module_comment.widget.floatingview.FloatingView;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.bean.event.ContactsNumRefreshEvent;
import com.fengwo.module_flirt.bean.event.OrderSortEvent;
import com.fengwo.module_live_vedio.mvp.ui.activity.LivingRoomActivity;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_login.mvp.dto.FansDto;
import com.fengwo.module_login.mvp.ui.activity.MineDetailActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;

public class ContactFragment extends BaseListFragment<FansDto> {

    public static final int TYPE_FRIENDS = 0;//好友
    public static final int TYPE_ATTENTION = 1;//关注
    public static final int TYPE_FANS = 2;//粉丝
    private int type = -1, orderBy = 1;// 排序->0：距离,1：活跃时间,2:添加好友时间,3:首字母 默认->1：活跃时间
    private AMapLocation location;

    private CompositeDisposable compositeDisposable;

    public static ContactFragment newInstance(int type, AMapLocation location) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putParcelable("location", location);
        ContactFragment fragment = new ContactFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Flowable setNetObservable() {
        assert getArguments() != null;
        if (type == -1)
            type = getArguments().getInt("type", -1);
        if (location == null)
            location = getArguments().getParcelable("location");

        String p = page + PAGE_SIZE;
        switch (type) {
            case TYPE_FRIENDS:
                return new RetrofitUtils().createApi(LoginApiService.class).getFriendList(p, location == null ?
                        null : location.getLatitude() + "", location == null ? null : location.getLongitude() + "", orderBy);
            case TYPE_ATTENTION:
                return new RetrofitUtils().createApi(LoginApiService.class).getAttentionList(p, location == null ?
                        null : location.getLatitude() + "", location == null ? null : location.getLongitude() + "", orderBy);
            case TYPE_FANS:
                return new RetrofitUtils().createApi(LoginApiService.class).getFansList(p, location == null ?
                        null : location.getLatitude() + "", location == null ? null : location.getLongitude() + "", orderBy);
        }
        return null;
    }

    @Override
    public RecyclerView.LayoutManager setLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    @Override
    public int setItemLayoutRes() {
        return R.layout.item_contact_mine;
    }

    @Override
    public void bingViewHolder(BaseViewHolder helper, FansDto item, int position) {
        RoundImageView avator = helper.getView(R.id.civ_head);
        ImageLoader.loadRouteImg(avator, item.headImg, 8);
        helper.setText(R.id.tv_name, item.nickname);
        //签名描述
        helper.setText(R.id.tv_signature, item.signature);
        //距离
        if (-1.0 == Double.parseDouble(item.distance)) {
            helper.setText(R.id.tv_distance, "未知距离");
        } else {
            long round = Math.round(Math.abs(Double.parseDouble(item.distance)));
            helper.setText(R.id.tv_distance, formatNums(round));
        }
        //在线状态
        helper.setText(R.id.tv_online, TextUtils.isEmpty(item.lastTime) ? "未知" : item.lastTime);
        helper.setTextColor(R.id.tv_online, "在线".equals(item.lastTime) ? Color.parseColor("#9965FF")
                : Objects.requireNonNull(getContext()).getResources().getColor(R.color.text_66));
        helper.setVisible(R.id.iv_gif_root, item.liveStatus == 2 || item.wenboLiveStatus == 1);
        ImageLoader.loadGif(helper.getView(R.id.iv_gif), R.drawable.live_cell_gif);
        //年龄
        TextView tvAge = helper.getView(R.id.tv_age);
        tvAge.setVisibility(View.VISIBLE);
        tvAge.setTextColor(Color.WHITE);
        tvAge.setText(TextUtils.isEmpty(item.age) ? "" : item.age);
        if (TextUtils.isEmpty(item.age))
            tvAge.setCompoundDrawablePadding(0);
        else
            tvAge.setCompoundDrawablePadding(4);

        //性别icon
        if (item.sex == 2) {
            tvAge.setBackgroundResource(R.drawable.shape_corner_girl);
            tvAge.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_girl, 0, 0, 0);
        } else if (item.sex == 1) {
            tvAge.setBackgroundResource(R.drawable.shape_corner_boy);
            tvAge.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_boy, 0, 0, 0);
        } else {
            tvAge.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
            if (!TextUtils.isEmpty(item.age)) {
                tvAge.setBackgroundResource(R.drawable.shape_corner_boy);
            } else
                //无性别 无年龄
                tvAge.setVisibility(View.GONE);
        }

//        //直播背景色
//        if (item.liveStatus == 2) {
//            helper.setBackgroundRes(R.id.iv_gif_root, R.drawable.shape_oval_boy_live_state);
//        } else if (item.wenboLiveStatus == 1) {
//            helper.setBackgroundRes(R.id.iv_gif_root, R.drawable.shape_oval_girl_live_state);
//        }

        if (type != 0)
            helper.setVisible(R.id.tv_friends, item.isAttention > 0);
        helper.setTextColor(R.id.tv_friends, Color.WHITE);

        if (type == 2) {
            //粉丝
            helper.setText(R.id.tv_attention, item.isAttention > 0 ? "互相关注" : "+ 关注");
            helper.setTextColor(R.id.tv_attention, item.isAttention > 0 ? Objects.requireNonNull(getContext()).getResources().getColor(R.color.text_99)
                    : Color.WHITE);
            helper.setBackgroundRes(R.id.tv_attention, item.isAttention > 0 ? R.drawable.bg_contact_gray
                    : R.drawable.bg_contact_home_tab_gradients);

            helper.setVisible(R.id.tv_attention, true);
            helper.setVisible(R.id.tv_online, false);
            helper.setVisible(R.id.tv_distance, false);
        } else {
            helper.setVisible(R.id.tv_attention, false);
            helper.setVisible(R.id.tv_online, true);
            helper.setVisible(R.id.tv_distance, true);
        }

        helper.getView(R.id.civ_head_root).setOnClickListener(view -> {
            if (item.liveStatus == 2) {
                //直播
                ArrayList<ZhuboDto> list = new ArrayList<>();
                ZhuboDto zhuboDto = new ZhuboDto();
                zhuboDto.channelId = item.id;
                zhuboDto.headImg = item.headImg;
                list.add(zhuboDto);
                if (FloatingView.getInstance().isShow()) {
                    showExitDialog(list);
                } else {
                    IntentRoomActivityUrils.setRoomActivity(list.get(0).channelId,list,0);

                }
            } else if (item.wenboLiveStatus == 1 && CommentUtils.isOpenFlirt) {
                ArouteUtils.toFlirtCardDetailsActivity(adapter.getData().get(position).id);
            } else {
                MineDetailActivity.startActivityWithUserId(getActivity(), adapter.getData().get(position).id);
            }
        });

        helper.addOnClickListener(R.id.tv_attention);
    }
    /**
     * 关闭悬浮窗弹框提示
     */
    public void showExitDialog(ArrayList<ZhuboDto>  list) {
        FloatingView floatingView = FloatingView.getInstance();
        ExitDialog dialog = new ExitDialog();
        dialog.setNegativeButtonText("取消")
                .setPositiveButtonText("确定退出")
                .addDialogClickListener(new BaseDialogFragment.OnClickListener() {
                    @Override
                    public void onConfirm() {
                        IntentRoomActivityUrils.setRoomActivity(list.get(0).channelId,list,0);

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
                .show(getActivity().getSupportFragmentManager(), "");
    }
    @SuppressLint("DefaultLocale")
    private String formatNums(long num) {
        if (num > 999)
            return String.format("%.1f", (double) num / 1000) + "km";
        else
            return num + "m";
    }

    @Override
    public String setEmptyContent() {
        switch (type) {
            case TYPE_FRIENDS:
                return "还没有好友呢\n喜欢的人互相关注可以成为好友哦~";
            case TYPE_ATTENTION:
                return "还没有关注呢\n喜欢的人互相关注可以成为好友哦~";
            case TYPE_FANS:
                return "还没有粉丝呢\n喜欢的人互相关注可以成为好友哦~";
        }
        return null;
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_baselist_notitle_white;
    }

    @SuppressLint("CheckResult")
    @Override
    public void initUI(Bundle savedInstanceState) {
        compositeDisposable = new CompositeDisposable();
        SmartRefreshLayoutUtils.setTransparentBlackText(getContext(), smartRefreshLayout);

        RxBus.get().toObservable(OrderSortEvent.class).subscribe(sortEventBean -> {
            orderBy = sortEventBean.orderBy;
            if (smartRefreshLayout != null) {
                recyclerView.scrollToPosition(0);
                smartRefreshLayout.autoRefresh();
            }
        });

        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (isFastClick()) return;
            if (view.getId() == R.id.tv_attention) {
                FansDto item = (FansDto) adapter.getItem(position);
                assert item != null;
                if (item.isAttention <= 0)//未关注
                    compositeDisposable.add(new RetrofitUtils().createApi(ChatService.class)
                            .addAttention(item.id + "")
                            .compose(RxUtils.applySchedulers2())
                            .subscribeWith(new LoadingObserver<HttpResult>() {
                                @Override
                                public void _onNext(HttpResult data) {
                                    item.isAttention = 1;
                                    adapter.notifyItemChanged(position);
                                    RxBus.get().post(new ContactsNumRefreshEvent());
                                }

                                @Override
                                public void _onError(String msg) {
                                    ToastUtils.showShort(getContext(), msg);
                                }
                            }));
                else
                    compositeDisposable.add(new RetrofitUtils().createApi(ChatService.class)
                            .removeAttention(item.id + "")
                            .compose(RxUtils.applySchedulers2())
                            .subscribeWith(new LoadingObserver<HttpResult>() {
                                @Override
                                public void _onNext(HttpResult data) {
                                    item.isAttention = 0;
                                    adapter.notifyItemChanged(position);
                                    RxBus.get().post(new ContactsNumRefreshEvent());
                                }

                                @Override
                                public void _onError(String msg) {
                                    ToastUtils.showShort(getContext(), msg);
                                }
                            }));
            }
        });
    }

    @Override
    public void setData(List<FansDto> datas, int page) {
        if (datas.size() != adapter.getData().size())
            RxBus.get().post(new ContactsNumRefreshEvent());
        super.setData(datas, page);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (compositeDisposable == null) return;
        compositeDisposable.isDisposed();
        compositeDisposable.clear();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
//            onRefresh();
        }
    }
}
