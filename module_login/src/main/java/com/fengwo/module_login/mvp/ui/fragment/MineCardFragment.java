/*
 *  个人中心里面的动态信息页面
 *
 *  展示用户自己相关的动态信息
 *
 * */

package com.fengwo.module_login.mvp.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseMvpFragment;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.dialog.CommonDialog;
import com.fengwo.module_comment.event.FindDetailChangedEvent;
import com.fengwo.module_comment.event.RefreshCardList;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.ShareHelper;
import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_live_vedio.mvp.ui.pop.ShareCommonPopwindow;
import com.fengwo.module_login.R;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_login.mvp.dto.MineCardDto;
import com.fengwo.module_login.mvp.dto.ShareUrlDto;
import com.fengwo.module_login.mvp.presenter.MineCardPresenter;
import com.fengwo.module_login.mvp.ui.activity.MineCardDetailActivity;
import com.fengwo.module_login.mvp.ui.adapter.MineCardAdapter;
import com.fengwo.module_login.mvp.ui.iview.IMineCardView;
import com.fengwo.module_login.mvp.ui.pop.AuthorityPopWindow;
import com.fengwo.module_login.mvp.ui.pop.CardDetailView;
import com.fengwo.module_login.utils.UserManager;
import com.google.gson.Gson;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MineCardFragment extends BaseMvpFragment<IMineCardView, MineCardPresenter> implements IMineCardView {

    private final static String UID = "uid";
    private final static String ZAN = "iszan";

    private int mShareType = 0; //分享类型
    private String mShareHeadImg = "";
    private String mShareContent = "";

    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;
    private MineCardAdapter mMineCardAdapter;
    private ShareCommonPopwindow mShareCommonWindow;//分享弹框
    private CardDetailView mMineMoreView;

    private int mPosition = 0; //当前item下标
    private int userId;        //用户id
    private int pageIndex = 1;
    protected CompositeDisposable allRxbus;

    public static MineCardFragment newInstance(int uid, boolean isZan) {
        Bundle args = new Bundle();
        args.putInt(UID, uid);
        args.putBoolean(ZAN, isZan);
        MineCardFragment fragment = new MineCardFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected MineCardPresenter initPresenter() {
        return new MineCardPresenter();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        userId = getArguments().getInt(UID, -1);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int setContentView() {
        return R.layout.activity_baselist_notitle;
    }

    @SuppressLint("CheckResult")
    @Override
    public void initUI(Bundle savedInstanceState) {
        SmartRefreshLayoutUtils.setWhiteBlackText(getActivity(), smartRefreshLayout);
        recyclerView.setBackgroundColor(getResources().getColor(R.color.white));
        allRxbus = new CompositeDisposable();
        allRxbus.add(RxBus.get().toObservable(RefreshCardList.class)
                .subscribe(refreshCardList -> {
                    if (smartRefreshLayout != null) {
                        recyclerView.scrollToPosition(0);
                        smartRefreshLayout.autoRefresh();
                    }
                }));
        allRxbus.add(RxBus.get().toObservable(FindDetailChangedEvent.class).subscribe(data -> {
            MineCardDto.RecordsBean rb = getCurrentData();
            rb.setLikes(data.getLikeCount());
            rb.setComments(data.getCommentsCount());
            rb.setShares(data.getShares());
            rb.setIsLike(data.getIsLike() == 1);
            mMineCardAdapter.notifyItemChanged(mPosition);
        }));

    }

    /**
     * 当前position 的data
     *
     * @return data
     */
    private MineCardDto.RecordsBean getCurrentData() {
        return getData().get(mPosition);
    }

    @Override
    public void initView(View v) {
        super.initView(v);
        mMineCardAdapter = new MineCardAdapter(getContext());
        smartRefreshLayout = v.findViewById(com.fengwo.module_comment.R.id.smartrefreshlayout);
        recyclerView = v.findViewById(com.fengwo.module_comment.R.id.recycleview);
        recyclerView.setAdapter(mMineCardAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        mMineMoreView = new CardDetailView(getContext());
        mMineMoreView.setOnMoreItemClickListener(new CardDetailView.OnItemClickListener() {
            @Override
            public void modify() {
                ARouter.getInstance().build(ArouterApi.CHAT_POST_TREND)
                        .withInt("id", getCurrentData().getId())
                        .navigation(getActivity(), 1000);
            }

            @Override
            public void delete() {
                mineCardDel();
            }

            @Override
            public void authority() {
                AuthorityPopWindow aw = new AuthorityPopWindow(getContext());
                aw.setOnItemChooseListener(state -> p.setCardAuthority(getCurrentData().getId(), state));
                aw.showPopupWindow();
            }

            //状态范围 0 去掉置顶 1 置顶
            @Override
            public void stick() {
                p.setCardStick(getCurrentData().getId(), 1);
            }

            @Override
            public void unStick() {
                p.setCardStick(getCurrentData().getId(), 0);
            }
        });
        mMineCardAdapter.bindToRecyclerView(recyclerView);
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++pageIndex;
                p.getMineCardData(pageIndex, userId);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageIndex = 1;
                p.getMineCardData(pageIndex, userId);
            }
        });
        smartRefreshLayout.autoRefresh();
        mMineCardAdapter.setOnItemClickListener((adapter, view, position) -> {
            //跳转到详情通用模块
            mPosition = position;
            if (getCurrentData().getUserId() == UserManager.getInstance().getUser().id) {
                ARouter.getInstance().build(ArouterApi.MINE_DETAIL_CARD_ACTION)
                        .withInt(MineCardDetailActivity.CHAR_CARD_ID, getCurrentData().getId())
                        .navigation(getActivity(), 10010);
            } else {
                ARouter.getInstance().build(ArouterApi.FLIRT_FIND_DETAIL_ACTION)
                        .withInt(MineCardDetailActivity.CHAR_CARD_ID, getCurrentData().getId())
                        .navigation(getActivity(), 10010);
            }
        });
        mMineCardAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            MineCardDto.RecordsBean itemData = getData().get(position);
            int viewId = view.getId();
            mPosition = position;
            //更多操作
            if (viewId == R.id.iv_mine_card_more) {
                mMineMoreView.chooseItem(getCurrentData().getCardStatus());
                mMineMoreView.setStick(TextUtils.isEmpty(getCurrentData().getTopTime()));
                mMineMoreView.showPopupWindow();
            }
            //转发
            else if (viewId == R.id.tv_mine_card_transmit) {
                mShareHeadImg = itemData.getHeadImg();
                mShareContent = itemData.getExcerpt();
                showShareDialog(itemData.getId());
            }
            //点赞事件
            else if (viewId == R.id.tv_mine_card_give) {
                if (isFastClick()) return;
                boolean isLike = itemData.getIsLike();
                itemData.setIsLike(!isLike);
                //如果 isLike 为真  表示点击了取消操作
                int likes = isLike
                        ? itemData.getLikes() - 1
                        : itemData.getLikes() + 1;
                itemData.setLikes(likes);
                mMineCardAdapter.notifyItemChanged(position);
                p.requestCardLike(itemData.getId());
            }
            //评论
            else if (viewId == R.id.tv_mine_card_comment) {
                //跳转到详情通用模块
                //如果是用户自己的动态就往自己的详情跳转
                //如果不是 就跳转到别人的详情
                if (itemData.getUserId() == UserManager.getInstance().getUser().id) {
                    ARouter.getInstance().build(ArouterApi.MINE_DETAIL_CARD_ACTION)
                            .withInt(MineCardDetailActivity.CHAR_CARD_ID, itemData.getId())
                            .navigation(getActivity(), 10010);
                } else {
                    ARouter.getInstance().build(ArouterApi.FLIRT_FIND_DETAIL_ACTION)
                            .withInt(MineCardDetailActivity.CHAR_CARD_ID, itemData.getId())
                            .navigation(getActivity(), 10010);
                }
            }
        });
    }

    /**
     * 删除当前条目的动态信息
     */
    @SuppressLint("CheckResult")
    private void mineCardDel() {
        CommonDialog.getInstance("", "确定删除动态吗?", "取消", "确定", false)
                .addOnDialogListener(new CommonDialog.OnDialogListener() {
                    @Override
                    public void cancel() {

                    }

                    @Override
                    public void sure() {
                        Map map = new HashMap();
                        map.put("cardId", getCurrentData().getId());
                        String sBody = new Gson().toJson(map);
                        RequestBody body = RequestBody.create(sBody, MediaType.parse("application/json"));
                        new RetrofitUtils().createApi(LoginApiService.class)
                                .deleteMineCard(body)
                                .compose(io_main())
                                .subscribeWith(new LoadingObserver<HttpResult>() {
                                    @Override
                                    public void _onNext(HttpResult data) {
                                        if (data.isSuccess()) {
                                            getData().remove(getCurrentData());
                                            mMineCardAdapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void _onError(String msg) {

                                    }
                                });
                    }
                }).show(getFragmentManager(), "删除动态");
    }

    @Override
    public void success(MineCardDto data) {
        if (smartRefreshLayout.isRefreshing()) {
            smartRefreshLayout.finishRefresh();
            clear();
            recyclerView.scrollToPosition(0);
        }
        if (smartRefreshLayout.isLoading()) {
            smartRefreshLayout.finishLoadMore();
        }
        addData(data.getRecords());
    }

    private void clear() {
        if (!mMineCardAdapter.getData().isEmpty()) {
            mMineCardAdapter.getData().clear();
        }
    }

    private List<MineCardDto.RecordsBean> getData() {
        return mMineCardAdapter.getData();
    }

    private void addData(List<MineCardDto.RecordsBean> data) {
        if (mMineCardAdapter.getData().isEmpty()) {
            mMineCardAdapter.setNewData(data);
            if (data == null || data.size() <= 0) {
                View v = LayoutInflater.from(getActivity())
                        .inflate(com.fengwo.module_comment.R.layout.item_base_empty_view,
                                null, false);
                mMineCardAdapter.setEmptyView(v);
            }
        } else {
            mMineCardAdapter.addData(data);
        }
    }


    @Override
    public void fail(String message) {
        --pageIndex;
        ToastUtils.show(getContext().getApplicationContext(),
                message, 100);
    }

    @Override
    public void error(String error) {
        --pageIndex;
    }

    /**
     * 分享成功的回调
     *
     * @param cardId 动态id
     * @param s      分享接口数据
     */
    @Override
    public void resultShareInfo(int cardId, ShareUrlDto s) {
        ShareHelper.get().shareDynamic(getActivity(),
                s.getUrl(),
                "蜂窝互娱",
                mShareHeadImg,
                mShareContent,
                mShareType);
        for (int i = 0; i < getData().size(); i++) {
            MineCardDto.RecordsBean r = getData().get(i);
            if (cardId == r.getId()) {
                r.setShares(r.getShareCount() + 1);
                mMineCardAdapter.notifyDataSetChanged();
                break;
            }
        }
        if (null != mShareCommonWindow) {
            mShareCommonWindow.dismiss();
        }
    }

    @Override
    public void failShareInfo(String f) {

    }

    @Override
    public void errorShareInfo(String e) {

    }

    @Override
    public void resultCardLike(int cardId) {

    }

    @Override
    public void failCardLike(String f) {

    }

    @Override
    public void errorCardLike(String e) {

    }

    @Override
    public void resultStickSuccess(boolean isStick) {
        refreshData();
        ToastUtils.show(getContext(), mMineMoreView.isStick() ? "置顶成功" : "取消置顶成功", 1000);
    }

    @Override
    public void resultStickFail(String f) {

    }

    @Override
    public void resultAuthoritySuccess(boolean isAuthor) {
        ToastUtils.show(getContext(), "权限设置成功", 1000);
    }

    @Override
    public void resultAuthorityFail(String f) {

    }

    /**
     * 微信分享
     *
     * @param id 动态id
     */
    private void showShareDialog(int id) {
        mShareCommonWindow = new ShareCommonPopwindow(getContext());
        mShareCommonWindow.addClickListener(new ShareCommonPopwindow.OnShareClickListener() {
            @Override
            public void onWx() {
                mShareType = 0;
                p.requestCardShare(id);
            }

            @Override
            public void onWxCircle() {
                mShareType = 1;
                p.requestCardShare(id);
            }
        });
        mShareCommonWindow.showPopupWindow();
    }

    public void refreshData() {
        if (smartRefreshLayout != null) {
            smartRefreshLayout.autoRefresh();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(allRxbus!=null){
            allRxbus.clear();
        }
    }
}
