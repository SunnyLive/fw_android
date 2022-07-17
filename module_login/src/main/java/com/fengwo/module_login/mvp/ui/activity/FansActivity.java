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

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.amap.api.location.AMapLocation;
import com.chad.library.adapter.base.BaseViewHolder;
import com.faceunity.ui.dialog.BaseDialogFragment;
import com.fengwo.module_comment.base.BaseListActivity;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.ZhuboDto;
import com.fengwo.module_comment.dialog.ExitDialog;
import com.fengwo.module_comment.iservice.AttentionService;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.IntentRoomActivityUrils;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_comment.widget.floatingview.FloatingView;
import com.fengwo.module_login.R;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_login.mvp.dto.FansDto;

import java.util.ArrayList;

import io.reactivex.Flowable;

public class FansActivity extends BaseListActivity<FansDto> {

    @Autowired
    AttentionService attentionService;
    private final static String PAGE_SIZE = ",10";

    private AMapLocation location;
    private int type = 2;//0好友 1关注 2粉丝

    public static void startActivity(Context context, AMapLocation location) {
        Intent i = new Intent(context, FansActivity.class);
        i.putExtra("location", location);
        context.startActivity(i);
    }

    @Override
    public void onResume() {
        super.onResume();
        //refresh();

    }

    @Override
    protected void initView() {
        super.initView();
//        setTitleBackground(Color.WHITE);
        SmartRefreshLayoutUtils.setTransparentBlackText(this, smartRefreshLayout);
        new ToolBarBuilder().showBack(true)
                .setBackIcon(R.drawable.ic_back_black)
                .setTitle("粉丝")
                .setTitleColor(R.color.text_33)
                .build();
    }

    @Override
    public Flowable setNetObservable() {
        if (location == null)
            location = getIntent().getParcelableExtra("location");
        String p = page + PAGE_SIZE;
        return new RetrofitUtils().createApi(LoginApiService.class).getFansList(p, location == null ? null : location.getLatitude() + "", location == null ? null : location.getLongitude() + "", 1);
    }

    @Override
    public RecyclerView.LayoutManager setLayoutManager() {
        return new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
    }

    @Override
    public int setItemLayoutRes() {
        return R.layout.item_contact_mine;
    }

    @Override
    public void bingViewHolder(BaseViewHolder helper, FansDto item, int position) {
        ImageView avator = helper.getView(R.id.civ_head);
        helper.getView(R.id.tv_attention).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.isAttention > 0) {
                    attentionService.delAttention(item.id, new LoadingObserver<HttpResult>() {
                        @Override
                        public void _onNext(HttpResult data) {
                            if (data.isSuccess()) {
                                p.getListData(1);
                            }
                            toastTip(data.description);
                        }

                        @Override
                        public void _onError(String msg) {
                        }
                    });
                } else {
                    attentionService.addAttention(item.id, new LoadingObserver<HttpResult>() {
                        @Override
                        public void _onNext(HttpResult data) {
                            if (data.isSuccess()) {
                                p.getListData(1);
                            }
                            toastTip(data.description);
                        }

                        @Override
                        public void _onError(String msg) {

                        }
                    });
                }
            }
        });
        if (type == 2) {
            //粉丝
            helper.setText(R.id.tv_attention, item.isAttention > 0 ? "好友" : "关注");
            helper.setTextColor(R.id.tv_attention, item.isAttention > 0 ? getResources().getColor(R.color.text_99)
                    : Color.WHITE);
            helper.setBackgroundRes(R.id.tv_attention, item.isAttention > 0 ? R.drawable.bg_contact_gray
                    : R.drawable.bg_contact_home_tab_gradients);
            helper.setVisible(R.id.tv_attention, true);
            //     helper.setVisible(R.id.tv_online, false);
            helper.setVisible(R.id.tv_distance, false);
        } else {
            helper.setVisible(R.id.tv_attention, false);
            //   helper.setVisible(R.id.tv_online, true);
            helper.setVisible(R.id.tv_distance, true);
        }
        helper.setText(R.id.tv_name, item.nickname);
        helper.setGone(R.id.tv_jf, item.userState == 0);
        if (item.userState == 0) {//是否被封禁
            helper.setTextColor(R.id.tv_name, ContextCompat.getColor(this, R.color.text_cc) );
            avator.setImageResource(R.drawable.pic_zw);
            helper.setGone(R.id.tv_online, false);
            helper.setGone(R.id.tv_age, false);
        }else {
            helper.setGone(R.id.tv_online, true);
            helper.setGone(R.id.tv_age, true);
            ImageLoader.loadCircleImg(avator, item.headImg);
            helper.setTextColor(R.id.tv_name, ContextCompat.getColor(this, R.color.text_33) );
            //签名描述
            helper.setText(R.id.tv_signature, item.signature);
            //距离
            if (-1.0 == Double.parseDouble(item.distance)) {
                helper.setText(R.id.tv_distance, "未知距离");
            } else {
                long round = Math.round(Math.abs(Double.parseDouble(item.distance)));
                helper.setText(R.id.tv_distance, round > 1000 ? round / 1000 + "km" : round + "m");
            }

            //在线状态
            if("在线".equals(item.lastTime)){
                helper.setGone(R.id.tv_online,true);
            }else {
                helper.setGone(R.id.tv_online,false);
            }
            if(item.wenboLiveStatus==1||item.liveStatus==2){
                helper.setGone(R.id.iv_gif_root,true);
            }else {
                helper.setGone(R.id.iv_gif_root,false);
            }
            ImageView sexGif = helper.getView(R.id.iv_gif);
            if(item.liveStatus == 2){
                helper.setBackgroundRes(R.id.iv_gif_root,R.drawable.bg_zx_man);
                ImageLoader.loadGif(sexGif,R.drawable.gif_nan);
            }
            if(item.wenboLiveStatus == 1){


                helper.setBackgroundRes(R.id.iv_gif_root,R.drawable.bg_zx_female);
                ImageLoader.loadGif(sexGif,R.drawable.gif_nv);
            }
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

//        if (type != 0)
//            helper.setVisible(R.id.tv_friends, item.isAttention > 0);
//        helper.setTextColor(R.id.tv_friends, Color.WHITE);



            helper.getView(R.id.civ_head_root).setOnClickListener(v -> {
                if (item.liveStatus == 2) {
                    ArrayList<ZhuboDto> list = new ArrayList<>();
                    ZhuboDto zhuboDto = new ZhuboDto();
                    zhuboDto.channelId = item.id;
                    list.add(zhuboDto);
                    if (FloatingView.getInstance().isShow()) {
                        showExitDialog(list);
                    } else {
                        IntentRoomActivityUrils.setRoomActivity(list.get(0).channelId,list,0);
                        //    LivingRoomActivity.start(FansActivity.this, list, 0, true);
                    }
                } else if (item.wenboLiveStatus == 1&&CommentUtils.isOpenFlirt) {
                    ArouteUtils.toFlirtCardDetailsActivity(adapter.getData().get(position).id);
                } else {
                    MineDetailActivity.startActivityWithUserId(FansActivity.this, adapter.getData().get(position).id);
                }
            });


        }

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
                     //   LivingRoomActivity.start(FansActivity.this, map, 0, true);
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
        return R.layout.activity_baselist_white;
    }
}
