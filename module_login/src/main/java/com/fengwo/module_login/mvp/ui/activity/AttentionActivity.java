package com.fengwo.module_login.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.chad.library.adapter.base.BaseViewHolder;
import com.faceunity.ui.dialog.BaseDialogFragment;
import com.fengwo.module_comment.base.BaseListActivity;
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
import com.fengwo.module_comment.widget.CustomerDialog;
import com.fengwo.module_comment.widget.floatingview.FloatingView;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.eventbus.AttentionChangeEvent;
import com.fengwo.module_login.R;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_login.mvp.dto.FansDto;

import java.util.ArrayList;

import io.reactivex.Flowable;

public class AttentionActivity extends BaseListActivity<FansDto> {
    private final static String PAGE_SIZE = ",10";


    private AMapLocation location;

    public static void startActivity(Context context, AMapLocation location) {
        Intent i = new Intent(context, AttentionActivity.class);
        i.putExtra("location", location);
        context.startActivity(i);
    }

    @Override
    protected void initView() {
        super.initView();
//        setTitleBackground(Color.WHITE);
        SmartRefreshLayoutUtils.setTransparentBlackText(this, smartRefreshLayout);
        new ToolBarBuilder().showBack(true)
                .setBackIcon(R.drawable.ic_back_black)
                .setTitle("关注")
                .setTitleColor(R.color.text_33)
                .build();

    }

    @Override
    public void onResume() {
        super.onResume();
        page = 1;
        isRefresh = true;
        //p.getListData(page);
    }

    @Override
    public Flowable setNetObservable() {
        if (location == null)
            location = getIntent().getParcelableExtra("location");
        String p = page + PAGE_SIZE;
        return new RetrofitUtils().createApi(LoginApiService.class).getAttentionList(p, location == null ? null : location.getLatitude() + "", location == null ? null : location.getLongitude() + "", 1);
    }

    @Override
    public RecyclerView.LayoutManager setLayoutManager() {
        return new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
    }

    @Override
    public int setItemLayoutRes() {
        return R.layout.item_attention;
    }



    @Override
    public void bingViewHolder(BaseViewHolder helper, FansDto item, int position) {
        ImageView avator = helper.getView(R.id.civ_head);

        helper.getView(R.id.tv_attention).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (item.isAttention == 2) {
                    attention(item.id + "", item);
                } else {

                    new CustomerDialog.Builder(AttentionActivity.this)
                            .setMsg("是否确定取消关注")
                            .setPositiveButton(new CustomerDialog.onPositiveInterface() {
                                @Override
                                public void onPositive() {
                                        removeAttention(item.id + "", item);

                                }
                            }).create().show();
                }
            }
        });
        switch (item.isAttention) {
            case 0:
                helper.setText(R.id.tv_attention, "已关注");
                helper.setBackgroundRes(R.id.tv_attention, R.drawable.bg_attention_status);
                helper.setTextColor(R.id.tv_attention, getResources().getColor(R.color.gray_999999));
                break;
            case 1:
                helper.setText(R.id.tv_attention, "好友");
                helper.setBackgroundRes(R.id.tv_attention, R.drawable.bg_attention_status_red);
                helper.setTextColor(R.id.tv_attention, getResources().getColor(R.color.homt_tab_selsct));
                break;
            case 2:
                helper.setText(R.id.tv_attention, "关注");
                helper.setBackgroundRes(R.id.tv_attention, R.drawable.bg_attention_status_red_all);
                helper.setTextColor(R.id.tv_attention, getResources().getColor(R.color.text_white_arr));
                break;
        }
        helper.setText(R.id.tv_name, item.nickname);
        helper.setGone(R.id.tv_jf, item.userState == 0);
        if (item.userState == 0) {//是否被封禁
            helper.setTextColor(R.id.tv_name, ContextCompat.getColor(this, R.color.text_cc));
            avator.setImageResource(R.drawable.pic_zw);
            helper.setGone(R.id.tv_online, false);
            helper.setGone(R.id.tv_age, false);
        } else {
            helper.setGone(R.id.tv_online, true);
            helper.setGone(R.id.tv_age, true);
            ImageLoader.loadCircleImg(avator, item.headImg);
            helper.setTextColor(R.id.tv_name, ContextCompat.getColor(this, R.color.text_33));
            //签名描述
            helper.setText(R.id.tv_signature, item.signature);
            if ("在线".equals(item.lastTime)) {
                helper.setGone(R.id.tv_online, true);
            } else {
                helper.setGone(R.id.tv_online, false);
            }
            //年龄
            TextView tvAge = helper.getView(R.id.tv_age);
            if (item.wenboLiveStatus == 1 || item.liveStatus == 2) {
                helper.setGone(R.id.rl_zx_back, true);
            } else {
                helper.setGone(R.id.rl_zx_back, false);
            }
            ImageView sexGif = helper.getView(R.id.gif_sex);
            if (item.liveStatus == 2) {
                helper.setBackgroundRes(R.id.rl_zx_back, R.drawable.bg_zx_man);
                ImageLoader.loadGif(sexGif, R.drawable.gif_nan);

            }
            if (item.wenboLiveStatus == 1) {
                helper.setBackgroundRes(R.id.rl_zx_back, R.drawable.bg_zx_female);
                ImageLoader.loadGif(sexGif, R.drawable.gif_nv);
            }

//        helper.setVisible(R.id.iv_gif_root, item.liveStatus == 2 || item.wenboLiveStatus == 1);

            //   helper.setVisible(R.id.tv_is_live, item.liveStatus == 2 || item.wenboLiveStatus == 1);


            ImageLoader.loadGif(helper.getView(R.id.iv_gif), R.drawable.live_cell_gif);

            tvAge.setVisibility(View.VISIBLE);
            tvAge.setTextColor(Color.WHITE);
            tvAge.setText(TextUtils.isEmpty(item.age) ? "" : item.age);
            if (TextUtils.isEmpty(item.age))
                tvAge.setCompoundDrawablePadding(0);
            else
                tvAge.setCompoundDrawablePadding(4);

            //性别icon
            if (item.sex == 2) {
                tvAge.setBackgroundResource(R.drawable.shape_attention_corner_girl);
                tvAge.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_girl, 0, 0, 0);
            } else if (item.sex == 1) {
                tvAge.setBackgroundResource(R.drawable.shape_attention_corner_boy);
                tvAge.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_boy, 0, 0, 0);
            } else {
                tvAge.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                if (!TextUtils.isEmpty(item.age)) {
                    tvAge.setBackgroundResource(R.drawable.shape_attention_corner_boy);
                } else
                    //无性别 无年龄
                    tvAge.setVisibility(View.GONE);
            }
//
//        //直播背景色
//        if (item.liveStatus == 2) {
//            helper.setTextColor(R.id.tv_is_live, Color.parseColor("#FE3C9C"));
//            helper.setText(R.id.tv_is_live, "直播中");
//            helper.setBackgroundRes(R.id.tv_is_live, R.drawable.bg_attention_home_tab_gradient);
//        } else if (item.wenboLiveStatus == 1) {
//            helper.setTextColor(R.id.tv_is_live, Color.parseColor("#63A5FF"));
//            helper.setText(R.id.tv_is_live, "视频聊天");
//            helper.setBackgroundRes(R.id.tv_is_live, R.drawable.bg_attention_blue_home_tab_gradient);
//
//        }

//        if (type != 0)
//            helper.setVisible(R.id.tv_friends, item.isAttention > 0);
//        helper.setTextColor(R.id.tv_friends, Color.WHITE);


            helper.getView(R.id.tv_is_live).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item.wenboLiveStatus == 1 && CommentUtils.isOpenFlirt) {
                        ArouteUtils.toFlirtCardDetailsActivity(adapter.getData().get(position).id);
                    }
                }
            });

            helper.getView(R.id.cl_attention).setOnClickListener(v -> {

                if (item.liveStatus == 2) {
                    ArrayList<ZhuboDto> list = new ArrayList<>();
                    ZhuboDto zhuboDto = new ZhuboDto();
                    zhuboDto.channelId = item.id;
                    zhuboDto.headImg = item.headImg;
                    list.add(zhuboDto);
                    if (FloatingView.getInstance().isShow()) {
                        showExitDialog(list);
                    } else {
                        IntentRoomActivityUrils.setRoomActivity(list.get(0).channelId, list, 0);
                        //  LivingRoomActivity.start(AttentionActivity.this, list, 0, true);
                    }
                } else if (item.wenboLiveStatus == 1 && CommentUtils.isOpenFlirt) {
                    ArouteUtils.toFlirtCardDetailsActivity(adapter.getData().get(position).id);
                } else {
                    MineDetailActivity.startActivityWithUserId(AttentionActivity.this, adapter.getData().get(position).id);
                }

            });
        }


    }

    /**
     * 关闭悬浮窗弹框提示
     */
    public void showExitDialog(ArrayList<ZhuboDto> list) {
        FloatingView floatingView = FloatingView.getInstance();
        ExitDialog dialog = new ExitDialog();
        dialog.setNegativeButtonText("取消")
                .setPositiveButtonText("确定退出")
                .addDialogClickListener(new BaseDialogFragment.OnClickListener() {
                    @Override
                    public void onConfirm() {
                        IntentRoomActivityUrils.setRoomActivity(list.get(0).channelId, list, 0);
                        //    LivingRoomActivity.start(AttentionActivity.this, map, 0, true);
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

    //关注
    private void attention(String id, FansDto item) {
        new RetrofitUtils().createApi(LiveApiService.class).addAttention(id)
                .compose(RxUtils.applySchedulers())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        ToastUtils.showShort(AttentionActivity.this, data.description);
                        RxBus.get().post(new AttentionChangeEvent(true, false, 1, id + ""));
                        if (data.isSuccess()) {
                            item.isAttention = 0;
                            new RetrofitUtils().createApi(LoginApiService.class).getAttentionList("1,10", location == null ? null : location.getLatitude() + "", location == null ? null : location.getLongitude() + "", 1);
                            settifyDataSetChanged();
                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });

    }


    //取消关注
    public void removeAttention(String id, FansDto item) {
        new RetrofitUtils().createApi(LiveApiService.class).removeAttention(id)
                .compose(RxUtils.applySchedulers())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        ToastUtils.showShort(AttentionActivity.this, data.description);

                        item.isAttention = 2;
                        RxBus.get().post(new AttentionChangeEvent(false, false, 0, id));
                        settifyDataSetChanged();
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_baselist_white;
    }
}
