package com.fengwo.module_login.mvp.ui.activity;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.faceunity.ui.dialog.BaseDialogFragment;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseListActivity;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.dialog.ExitDialog;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.IntentRoomActivityUrils;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;

import com.fengwo.module_comment.bean.ZhuboDto;
import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_comment.widget.floatingview.FloatingView;
import com.fengwo.module_live_vedio.mvp.ui.activity.LivingRoomActivity;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_login.mvp.dto.WatchHistoryDto;
import com.fengwo.module_login.mvp.ui.pop.DeleteConfirmDialog;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;

import butterknife.BindView;
import io.reactivex.Flowable;

public class ViewsHistoryActivity extends BaseListActivity<WatchHistoryDto> implements DeleteConfirmDialog.OnDeleteConformCallback {

    private DeleteConfirmDialog confirmDialog;
    @BindView(R2.id.smartrefreshlayout)
    SmartRefreshLayout smartRefreshLayout;

    @Override
    protected void initView() {
        super.initView();
        SmartRefreshLayoutUtils.setClassicsColor(this, smartRefreshLayout, android.R.color.transparent, R.color.text_66);
        new ToolBarBuilder().showBack(true)
                .setTitle("观看历史")
                .setTitleColor(R.color.text_33)
                .setBackIcon(R.drawable.ic_back_black)
                .setRightTextColor(R.color.tab_primary_Dark)
                .setRightText("清空", view -> showDeleteConfirmDialog())
                .build();
//        setTitleBackground(Color.WHITE);
    }

    @Override
    public Flowable setNetObservable() {
        return new RetrofitUtils()
                .createApi(LoginApiService.class)
                .getWatchHistory(page + PAGE_SIZE);
    }

    @Override
    public RecyclerView.LayoutManager setLayoutManager() {
        return new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
    }

    @Override
    public int setItemLayoutRes() {
        return R.layout.login_item_viewshistory;
    }

    @Override
    public void bingViewHolder(BaseViewHolder helper, WatchHistoryDto item, int position) {
        ImageLoader.loadImg(helper.getView(R.id.iv_header), item.getHeadImg());
        helper.setText(R.id.tv_name, item.getNickname());
        helper.setText(R.id.tv_des, item.getSignature());
        ImageView sexIv = helper.getView(R.id.iv_sex);
        sexIv.setVisibility(View.VISIBLE);
        if (item.getSex() == 2) {
            sexIv.setEnabled(true);
            sexIv.setImageResource(R.drawable.icon_famale);
        } else if (item.getSex() == 1) {
            sexIv.setEnabled(false);
            sexIv.setImageResource(R.drawable.icon_male);
        } else {
            sexIv.setVisibility(View.GONE);
        }
        ImageView living = helper.getView(R.id.iv_living);
        if (item.isLiving()) {
            living.setVisibility(View.VISIBLE);
            ImageLoader.loadGif(living,R.drawable.live_cell_gif);
        } else {
            living.setVisibility(View.GONE);
        }
        helper.setText(R.id.tv_when, item.getLookLiveTime());
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getStatus() == 2) {

                    ArrayList<ZhuboDto> list = new ArrayList<>();
                    ZhuboDto zhuboDto = new ZhuboDto();
                    zhuboDto.channelId = item.getChannelId();
                    zhuboDto.headImg = item.getHeadImg();
                    list.add(zhuboDto);
               //     LivingRoomActivity livingRoomActivity = new LivingRoomActivity();
                   // livingRoomActivity.changeRooming(list.get(position).channelId+"");
//                    LivingRoomActivity.start(ViewsHistoryActivity.this, list, 0, true);
                    //showFVExitDialog(list, position);
                    showFVExitDialog(list, 0);
                } else {
                    int id = item.getChannelId();
                    ArouteUtils.toPathWithId(ArouterApi.USER_DETAIL, id);
                }
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_baselist;
    }

    private void showDeleteConfirmDialog() {
        if (confirmDialog == null) {
            confirmDialog = new DeleteConfirmDialog();
            confirmDialog.setCallback(this);
        }
        confirmDialog.show(getSupportFragmentManager(), "confirm");
    }

    @Override
    public void confirm() {
        new RetrofitUtils()
                .createApi(LoginApiService.class)
                .delWatchHistory().compose(io_main())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        adapter.setNewData(null);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }

    /**
     * 关闭悬浮窗提示
     * @param list
     * @param position
     */
    private void showFVExitDialog(ArrayList<ZhuboDto> list, int position) {
        FloatingView floatingView = FloatingView.getInstance();
        if (floatingView.isShow()) {
            ExitDialog dialog = new ExitDialog();
            dialog.setNegativeButtonText("取消")
                    .setPositiveButtonText("确定退出")
                    .setTip("进入直播间会退出达人房间\n印象值将归零，是否要退出")
                    .setGear(floatingView.getGear())
                    .setNickname(floatingView.getNickname())
                    .setExpireTime(floatingView.getExpireTime())
                    .setRoomId(floatingView.getRoomId())
                    .setHeadImg(floatingView.getHeadImg())
                    .setExitType(ExitDialog.ENTER_LIVING)
                    .addDialogClickListener(new BaseDialogFragment.OnClickListener() {
                        @Override
                        public void onConfirm() {
                            IntentRoomActivityUrils.setRoomActivity(list.get(0).channelId,list,0);
                       //     LivingRoomActivity.start(ViewsHistoryActivity.this, list, 0, true);
                        }

                        @Override
                        public void onCancel() {
                        }
                    });
            dialog.show(getSupportFragmentManager(), "");
        } else {
            IntentRoomActivityUrils.setRoomActivity(list.get(position).channelId,list,0);
 //           LivingRoomActivity.start(ViewsHistoryActivity.this, list, position);
        }
    }


}
