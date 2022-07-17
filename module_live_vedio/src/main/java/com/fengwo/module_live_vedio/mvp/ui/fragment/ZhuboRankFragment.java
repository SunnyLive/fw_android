package com.fengwo.module_live_vedio.mvp.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faceunity.ui.dialog.BaseDialogFragment;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseMvpFragment;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.ZhuboDto;
import com.fengwo.module_comment.dialog.ExitDialog;
import com.fengwo.module_comment.event.AttentionRefreshEvent;
import com.fengwo.module_comment.utils.ActivitysManager;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.IntentRoomActivityUrils;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_comment.widget.floatingview.FloatingView;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.eventbus.ChangeRoomEvent;
import com.fengwo.module_live_vedio.mvp.dto.RankZhuboDto;
import com.fengwo.module_live_vedio.mvp.ui.activity.BaseLiveingRoomActivity;
import com.fengwo.module_live_vedio.mvp.ui.adapter.ZhuboRankAdapter;
import com.fengwo.module_live_vedio.utils.AttentionUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

;
;

//RankZhuboDto
public class ZhuboRankFragment extends BaseMvpFragment implements OnLoadMoreListener, OnRefreshListener {


    @BindView(R2.id.recycleview)
    RecyclerView recycleview;
    @BindView(R2.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;
    private boolean isLoadmore;

    private ZhuboRankAdapter mAdapter;
    private Disposable disposable;

    public static ZhuboRankFragment newInstance(int type) {
        Bundle args = new Bundle();
        ZhuboRankFragment fragment = new ZhuboRankFragment();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initView(View v) {
        super.initView(v);
        SmartRefreshLayoutUtils.setClassicsColor(getActivity(), smartrefreshlayout, R.color.white, R.color.text_33);
        smartrefreshlayout.setOnRefreshListener(this);
        smartrefreshlayout.setEnableLoadMore(false);
        Disposable subscribe = RxBus.get().toObservable(AttentionRefreshEvent.class)
                .compose(bindToLifecycle()).subscribe(event ->
                        refreshAttentionStatus(event.refreshUid, event.isAttention));
        getRank();
        recycleview.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private void refreshAttentionStatus(int uid, int isAttention) {
        if (mAdapter != null && mAdapter.getData().size() > 0) {
            for (RankZhuboDto item : mAdapter.getData()) {
                if (item.value == uid) {
                    item.isAttension = isAttention;
                    mAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    public void getRank() {
        int type = getArguments().getInt("type");
        disposable = new RetrofitUtils().createApi(LiveApiService.class).getZhuboRank(type)
                .compose(RxUtils.applySchedulers())
                .subscribeWith(new LoadingObserver<HttpResult<List<RankZhuboDto>>>() {
                    @Override
                    public void _onNext(HttpResult<List<RankZhuboDto>> data) {
                        if (smartrefreshlayout != null) {
                            smartrefreshlayout.closeHeaderOrFooter();
                            smartrefreshlayout.finishRefresh();
                        }
                        if (recycleview == null) return;
                        mAdapter = new ZhuboRankAdapter();
                        recycleview.setAdapter(mAdapter);
                        View empty = LayoutInflater.from(getActivity()).inflate(com.fengwo.module_comment.R.layout.item_base_empty_view, null, false);
                        mAdapter.setEmptyView(empty);
                        mAdapter.bindToRecyclerView(recycleview);
                        if (data.isSuccess()) {
                            mAdapter.setAttentionListener((position, isChecked) -> {
                                if (isFastClick()) {
                                    return null;
                                }
                                if (mAdapter.getData().get(position).isAttension == 0) {
                                    addAttention(mAdapter.getData().get(position).value);
                                } else {
                                    delAttention(mAdapter.getData().get(position).value);
                                }
                                return null;
                            });
                            mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                                if (isFastClick()) {
                                    return;
                                }
                                int id = view.getId();
                                if (id == R.id.btn_attention) {

                                } else if (id == R.id.root) {
                                    if (mAdapter.getItem(position).liveStatus == 2) {

                                        boolean hashLiving = false;//已经启动BaseLiveingRoomActivity
                                        for (Activity activity : ActivitysManager.getInstance().getActivityStack()) {
                                            if (activity instanceof BaseLiveingRoomActivity) {
                                                hashLiving = true;
                                                break;
                                            }
                                        }
                                        if (hashLiving) {
                                            //如果已经在看直播了，可以直接关闭，发送Rx消息
                                            RankZhuboDto dto = mAdapter.getData().get(position);
                                            RxBus.get().post(new ChangeRoomEvent(dto.value + "", dto.headImg));
                                            requireActivity().finish();
                                        } else {
                                            ArrayList<ZhuboDto> list = new ArrayList<>();
                                            ZhuboDto zhuboDto = new ZhuboDto();
                                            zhuboDto.channelId = mAdapter.getItem(position).value;
                                            zhuboDto.headImg = mAdapter.getItem(position).headImg;
                                            list.add(zhuboDto);
                                            if (FloatingView.getInstance().isShow()) {
                                                showExitDialog(list);
                                            } else {
                                                IntentRoomActivityUrils.setRoomActivity(list.get(0).channelId, list, 0);
                                                //             LivingRoomActivity.start(getActivity(), list, 0, true);
                                            }
                                        }
                                    } else
                                        ArouteUtils.toPathWithId(ArouterApi.USER_DETAIL, mAdapter.getData().get(position).value);
                                }
                            });
                            mAdapter.setNewData(data.data);
                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
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
                        //               LivingRoomActivity.start(getActivity(), map, 0, true);
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

    private void delAttention(int id) {
        AttentionUtils.delAttention(id, new LoadingObserver<HttpResult>() {
            @Override
            public void _onNext(HttpResult data) {
                toastTip(data.description);
                if (data.isSuccess())
                    RxBus.get().post(new AttentionRefreshEvent(id, 0));
            }

            @Override
            public void _onError(String msg) {
                toastTip(msg);
            }
        });
    }

    private void addAttention(int id) {
        AttentionUtils.addAttention(id, new LoadingObserver<HttpResult>() {
            @Override
            public void _onNext(HttpResult data) {
                toastTip(data.description);
                if (data.isSuccess())
                    RxBus.get().post(new AttentionRefreshEvent(id, 1));
            }

            @Override
            public void _onError(String msg) {
                toastTip(msg);
            }
        });
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_baselist_notitle;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void initUI(Bundle savedInstanceState) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getRank();
        }
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        isLoadmore = true;
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getRank();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
